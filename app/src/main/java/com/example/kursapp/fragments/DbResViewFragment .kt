package com.example.kursapp.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursapp.DbAdapterResView
import com.example.kursapp.databinding.FragmentDbResviewBinding
import com.example.kursapp.db.AppDatabase
import com.example.kursapp.db.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DbResViewFragment : Fragment() {
    private var _binding: FragmentDbResviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var productDao: ProductDao

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
        productDao = database.productDao()

        GlobalScope.launch(Dispatchers.IO) {
            val allProducts = mutableListOf<List<DbAdapterResView.Product>>()
            val assemblyIds = mutableListOf<Int>()

            // Получаем список уникальных идентификаторов сборок
            val uniqueAssemblyIds = productDao.getUniqueAssemblyIds()
            for (assemblyId in uniqueAssemblyIds) {
                val productsForAssembly = productDao.getProductsForAssembly(assemblyId)
                val convertedProducts = productsForAssembly.map { productEntity ->
                    DbAdapterResView.Product(productEntity.name, productEntity.price.toString())
                }
                val groupedProducts = convertedProducts.chunked(5)
                allProducts.addAll(groupedProducts)
                assemblyIds.add(assemblyId.toInt()) // Преобразуем Long в Int
            }

            launch(Dispatchers.Main) {
                val recyclerView = binding.recyclerView2
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val adapter = DbAdapterResView(requireContext(), allProducts, assemblyIds, object : DbAdapterResView.OnItemClickListener {
                    override fun onItemClick(assemblyId: Int) {
                        // Вызов метода для отображения диалогового окна с подтверждением удаления сборки
                        showDeleteConfirmationDialog(assemblyId)
                    }
                })
                recyclerView.adapter = adapter
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Функция для отображения диалогового окна с подтверждением удаления сборки
    private fun showDeleteConfirmationDialog(assemblyId: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage("Вы уверены, что хотите удалить сборку?")
            .setPositiveButton("Да") { _: DialogInterface, _: Int ->
                // Пользователь подтвердил удаление
                deleteAssembly(assemblyId)
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    // Функция для удаления сборки из базы данных
    private fun deleteAssembly(assemblyId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            productDao.deleteProductsByAssemblyId(assemblyId)
            updateList()
        }

    }
    private fun updateList() {
        GlobalScope.launch(Dispatchers.IO) {
            val allProducts = mutableListOf<List<DbAdapterResView.Product>>()
            val newAssemblyIds = mutableListOf<Int>()

            // Получаем список уникальных идентификаторов сборок
            val uniqueAssemblyIds = productDao.getUniqueAssemblyIds()
            for (assemblyId in uniqueAssemblyIds) {
                val productsForAssembly = productDao.getProductsForAssembly(assemblyId)
                val convertedProducts = productsForAssembly.map { productEntity ->
                    DbAdapterResView.Product(productEntity.name, productEntity.price.toString())
                }
                val groupedProducts = convertedProducts.chunked(5)
                allProducts.addAll(groupedProducts)
                newAssemblyIds.add(assemblyId.toInt()) // Преобразуем Long в Int
            }

            launch(Dispatchers.Main) {
                // Создаем новый экземпляр адаптера с обновленными данными
                val newAdapter = DbAdapterResView(requireContext(), allProducts, newAssemblyIds, object : DbAdapterResView.OnItemClickListener {
                    override fun onItemClick(assemblyId: Int) {
                        // Вызов метода для отображения диалогового окна с подтверждением удаления сборки
                        showDeleteConfirmationDialog(assemblyId)
                    }
                })

                // Устанавливаем новый адаптер в RecyclerView
                binding.recyclerView2.adapter = newAdapter
            }
        }
    }



}
