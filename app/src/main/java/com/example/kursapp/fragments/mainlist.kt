package com.example.kursapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kursapp.Product
import com.example.kursapp.ProductAdapter
import com.example.kursapp.R
import com.example.kursapp.databinding.FragmentMainlistBinding
import com.google.firebase.database.*

class mainlist : Fragment(), ProductAdapter.OnProductClickListener {
    private lateinit var binding: FragmentMainlistBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseReference: DatabaseReference
    private var selectedCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация базы данных Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("products")

        // Создание слушателя для базы данных Firebase
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<String>()
                // Проходимся по каждой категории и добавляем её название в список
                for (categorySnapshot in dataSnapshot.children) {
                    categories.add(categorySnapshot.key ?: "")
                }
                // Добавляем пункт "Все товары"
                categories.add(0, "Все товары")

                // Создаем адаптер для Spinner и передаем список категорий
                val spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    categories
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categorySpinner.adapter = spinnerAdapter

                // Восстанавливаем выбранную категорию, если она была сохранена
                selectedCategory?.let { category ->
                    val spinnerPosition = (binding.categorySpinner.adapter as ArrayAdapter<String>).getPosition(category)
                    binding.categorySpinner.setSelection(spinnerPosition)
                }

                // Добавляем слушателя для выбора категории
                binding.categorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedCategory = categories[position]
                            if (selectedCategory == "Все товары") {
                                // Если выбран пункт "Все товары", показываем все товары без сортировки
                                val allProducts = mutableListOf<Product>()
                                for (categorySnapshot in dataSnapshot.children) {
                                    for (productSnapshot in categorySnapshot.children) {
                                        val product =
                                            productSnapshot.getValue(Product::class.java)
                                        product?.let { allProducts.add(it) }
                                    }
                                }
                                // Обновляем данные в адаптере
                                productAdapter.updateData(allProducts)
                            } else {
                                // Иначе фильтруем товары по выбранной категории
                                val productList = mutableListOf<Product>()
                                for (categorySnapshot in dataSnapshot.children) {
                                    if (categorySnapshot.key == selectedCategory) {
                                        for (productSnapshot in categorySnapshot.children) {
                                            val product =
                                                productSnapshot.getValue(Product::class.java)
                                            product?.let { productList.add(it) }
                                        }
                                    }
                                }
                                // Обновляем данные в адаптере
                                productAdapter.updateData(productList)
                            }
                            // Сохраняем выбранную категорию
                            this@mainlist.selectedCategory = selectedCategory
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Ничего не делаем при отсутствии выбранной категории
                        }
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при чтении данных из базы данных
            }
        }

        // Добавляем слушателя к базе данных Firebase
        databaseReference.addValueEventListener(valueEventListener)

        // Инициализируем RecyclerView
        productAdapter = ProductAdapter(emptyList(), this) // Передаем текущий фрагмент как обработчик нажатий
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = productAdapter
    }

    override fun onProductClick(product: Product) {
        // Создание Bundle для передачи информации о продукте в новый фрагмент
        val bundle = Bundle()
        bundle.putParcelable("product", product)

        // Получение NavController из контейнера фрагментов
        val navController = findNavController()

        // Навигация к новому фрагменту, передача информации о продукте
        navController.navigate(R.id.action_mainlist_to_fragmentDetail, bundle)
    }
}
