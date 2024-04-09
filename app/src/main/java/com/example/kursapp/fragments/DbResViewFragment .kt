package com.example.kursapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursapp.DbAdapterResView

import com.example.kursapp.databinding.FragmentDbResviewBinding
import com.example.kursapp.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DbResViewFragment : Fragment() {
    private var _binding: FragmentDbResviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDbResviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getInstance(requireContext())
        val productDao = database.productDao()

        GlobalScope.launch(Dispatchers.IO) {
            val maxAssemblyId = productDao.getMaxAssemblyId() ?: 0
            val allProducts = mutableListOf<List<DbAdapterResView.Product>>()
            for (i in 1..maxAssemblyId) {
                val productsForAssembly = productDao.getProductsForAssembly(i.toLong())
                val convertedProducts = productsForAssembly.map { productEntity ->
                    DbAdapterResView.Product(productEntity.name, productEntity.price.toString())
                }
                val groupedProducts = convertedProducts.chunked(5)
                allProducts.addAll(groupedProducts)
            }

            // После получения всех данных из базы, обновите интерфейс в главном потоке
            launch(Dispatchers.Main) {
                val recyclerView = binding.recyclerView2
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val adapter = DbAdapterResView(requireContext(), allProducts, maxAssemblyId)
                recyclerView.adapter = adapter
            }
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
