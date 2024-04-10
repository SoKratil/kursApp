package com.example.kursapp.presentation.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kursapp.presentation.adapters.DbAdapterResView
import com.example.kursapp.databinding.FragmentDbResviewBinding
import com.example.kursapp.data.db.AppDatabase
import com.example.kursapp.data.db.ProductDao
import com.example.kursapp.data.repository.ProductRepositoryImpl
import com.example.kursapp.domain.useCases.DeleteProductsByAssemblyIdUseCase
import com.example.kursapp.domain.useCases.GetProductsForAssemblyUseCase
import com.example.kursapp.domain.useCases.GetUniqueAssemblyIdsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DbResViewFragment : Fragment() {
    private var _binding: FragmentDbResviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var deleteProductsByAssemblyIdUseCase: DeleteProductsByAssemblyIdUseCase
    private lateinit var getUniqueAssemblyIdsUseCase: GetUniqueAssemblyIdsUseCase
    private lateinit var getProductsForAssemblyUseCase: GetProductsForAssemblyUseCase
    private lateinit var productDao: ProductDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDbResviewBinding.inflate(inflater, container, false)
        val database = AppDatabase.getInstance(requireContext())
        productDao = database.productDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productRepository = ProductRepositoryImpl(productDao)
        deleteProductsByAssemblyIdUseCase = DeleteProductsByAssemblyIdUseCase(productRepository)
        getUniqueAssemblyIdsUseCase = GetUniqueAssemblyIdsUseCase(productRepository)
        getProductsForAssemblyUseCase = GetProductsForAssemblyUseCase(productRepository)

        GlobalScope.launch(Dispatchers.IO) {
            val allProducts = mutableListOf<List<DbAdapterResView.Product>>()
            val assemblyIds = mutableListOf<Int>()

            // Получаем список уникальных идентификаторов сборок
            val uniqueAssemblyIds = productRepository.getUniqueAssemblyIds()
            for (assemblyId in uniqueAssemblyIds) {
                val productsForAssembly = productRepository.getProductsForAssembly(assemblyId)
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
            deleteProductsByAssemblyIdUseCase.execute(assemblyId)
            updateList()
        }

    }
    private fun updateList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val allProducts = mutableListOf<List<DbAdapterResView.Product>>()
            val newAssemblyIds = mutableListOf<Int>()

            // Получаем список уникальных идентификаторов сборок
            val uniqueAssemblyIds = getUniqueAssemblyIdsUseCase.execute()
            for (assemblyId in uniqueAssemblyIds) {
                val productsForAssembly = getProductsForAssemblyUseCase.execute(assemblyId)
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
