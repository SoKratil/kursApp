package com.example.kursapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kursapp.Product
import com.example.kursapp.databinding.FragmentBuildBinding
import com.example.kursapp.db.AppDatabase
import com.example.kursapp.db.ProductEntity
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Buildfragment : Fragment() {
    private var _binding: FragmentBuildBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BuildFragmentViewModel by viewModels()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var appDatabase: AppDatabase
    private var products: Map<String, Map<String, Product>> = emptyMap()
    private var selectedGraphicsCard: Product? = null
    private var selectedMotherboard: Product? = null
    private var selectedProcessor: Product? = null
    private var selectedRAM: Product? = null
    private var selectedSSD: Product? = null
    private var totalPrice: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация базы данных Room
        appDatabase = AppDatabase.getInstance(requireContext())

        // Инициализация базы данных Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("products")

        // Загрузка данных о продуктах из базы данных
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Переменная для хранения данных о продуктах из Firebase
                val productsMap = dataSnapshot.value as? Map<String, Map<String, Map<String, Any>>>?

                // Преобразование данных из Firebase в Map<String, Map<String, Product>>
                products = productsMap?.mapValues { entry ->
                    entry.value.mapValues { (_, value) ->
                        // Преобразование Map<String, Any> в Product
                        Product(
                            name = value["name"] as? String ?: "",
                            price = (value["price"] as? Double) ?: 0.0,
                            description = value["description"] as? String ?: "",
                            imgUrl = value["imgUrl"] as? String ?: ""
                        )
                    }
                } ?: emptyMap()

                // Настройка спиннеров после загрузки данных
                setupSpinners()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при загрузке данных из базы данных
            }
        })

        // Настройка кнопки "Сохранить в базу данных"
        setupSaveButton()
        viewModel.selectedGraphicsCard.observe(viewLifecycleOwner) { product ->
            // Обновление выбранной графической карты
            selectedGraphicsCard = product
            calculateTotalPrice()
        }

        viewModel.selectedMotherboard.observe(viewLifecycleOwner) { product ->
            // Обновление выбранной материнской платы
            selectedMotherboard = product
            calculateTotalPrice()
        }

        viewModel.selectedProcessor.observe(viewLifecycleOwner) { product ->
            // Обновление выбранного процессора
            selectedProcessor = product
            calculateTotalPrice()
        }

        viewModel.selectedRAM.observe(viewLifecycleOwner) { product ->
            // Обновление выбранной оперативной памяти
            selectedRAM = product
            calculateTotalPrice()
        }

        viewModel.selectedSSD.observe(viewLifecycleOwner) { product ->
            // Обновление выбранного SSD
            selectedSSD = product
            calculateTotalPrice()
        }
    }

    private fun setupSpinners() {
        // Настройка адаптера для спиннера графических карт
        val graphicsCardAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>().apply {
                add("Выбрать") // Добавляем пустой элемент в начало списка
                addAll(products["graphics_cards"]?.values?.map { it.name } ?: emptyList())
            }
        )
        binding.spinnerGraphicsCards.adapter = graphicsCardAdapter
        binding.spinnerGraphicsCards.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGraphicsCard = if (position > 0) {
                    products["graphics_cards"]?.values?.elementAtOrNull(position - 1)
                } else {
                    null
                }
                calculateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedGraphicsCard = null
                calculateTotalPrice()
            }
        }


        // Настройка адаптера для спиннера материнских плат
        val motherboardAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>().apply {
                add("Выбрать") // Добавляем пустой элемент в начало списка
                addAll(products["motherboards"]?.values?.map { it.name } ?: emptyList())
            }
        )
        binding.spinnerMotherboards.adapter = motherboardAdapter
        binding.spinnerMotherboards.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedMotherboard = if (position > 0) {
                    products["motherboards"]?.values?.elementAtOrNull(position - 1)
                } else {
                    null
                }
                calculateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedMotherboard = null
                calculateTotalPrice()
            }
        }


        // Настройка адаптера для спиннера процессоров
        val processorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>().apply {
                add("Выбрать") // Добавляем пустой элемент в начало списка
                addAll(products["processors"]?.values?.map { it.name } ?: emptyList())
            }
        )
        binding.spinnerProcessors.adapter = processorAdapter
        binding.spinnerProcessors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedProcessor = if (position > 0) {
                    products["processors"]?.values?.elementAtOrNull(position - 1)
                } else {
                    null
                }
                calculateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedProcessor = null
                calculateTotalPrice()
            }
        }

        // Настройка адаптера для спиннера оперативной памяти (RAM)
        val ramAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>().apply {
                add("Выбрать") // Добавляем пустой элемент в начало списка
                addAll(products["ram"]?.values?.map { it.name } ?: emptyList())
            }
        )
        binding.spinnerRAM.adapter = ramAdapter
        binding.spinnerRAM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRAM = if (position > 0) {
                    products["ram"]?.values?.elementAtOrNull(position - 1)
                } else {
                    null
                }
                calculateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedRAM = null
                calculateTotalPrice()
            }
        }

        // Настройка адаптера для спиннера SSD
        val ssdAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf<String>().apply {
                add("Выбрать") // Добавляем пустой элемент в начало списка
                addAll(products["ssd"]?.values?.map { it.name } ?: emptyList())
            }
        )
        binding.spinnerSSD.adapter = ssdAdapter
        binding.spinnerSSD.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSSD = if (position > 0) {
                    products["ssd"]?.values?.elementAtOrNull(position - 1)
                } else {
                    null
                }
                calculateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSSD = null
                calculateTotalPrice()
            }
        }


// Настройка спиннера графических карт
        viewModel.selectedGraphicsCard.value?.let { product ->
            binding.spinnerGraphicsCards.setSelection(graphicsCardAdapter.getPosition(product.name))
        }

// Настройка спиннера материнских плат
        viewModel.selectedMotherboard.value?.let { product ->
            binding.spinnerMotherboards.setSelection(motherboardAdapter.getPosition(product.name))
        }

// Настройка спиннера процессоров
        viewModel.selectedProcessor.value?.let { product ->
            binding.spinnerProcessors.setSelection(processorAdapter.getPosition(product.name))
        }

// Настройка спиннера оперативной памяти (RAM)
        viewModel.selectedRAM.value?.let { product ->
            binding.spinnerRAM.setSelection(ramAdapter.getPosition(product.name))
        }

// Настройка спиннера SSD
        viewModel.selectedSSD.value?.let { product ->
            binding.spinnerSSD.setSelection(ssdAdapter.getPosition(product.name))
        }

    }


    private fun calculateTotalPrice() {
        // Сначала обнуляем сумму
        totalPrice = 0.0
        // Затем добавляем цены выбранных продуктов, если они выбраны
        selectedGraphicsCard?.let { totalPrice += it.price }
        selectedMotherboard?.let { totalPrice += it.price }
        selectedProcessor?.let { totalPrice += it.price }
        selectedRAM?.let { totalPrice += it.price }
        selectedSSD?.let { totalPrice += it.price }
        // Обновляем отображение на экране
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        // Обновляем текстовое поле с суммой
        binding.textViewTotalPrice.text = "${totalPrice.toString()}$"
    }

    private fun setupSaveButton() {
        binding.buttonSaveToDatabase.setOnClickListener {
            // Проверяем, выбран ли элемент в каждом спиннере
            if (selectedGraphicsCard == null || selectedMotherboard == null ||
                selectedProcessor == null || selectedRAM == null || selectedSSD == null) {
                // Если какой-либо из спиннеров не имеет выбранного элемента, выводим сообщение об ошибке
                showToast("Пожалуйста, выберите продукты для всех компонентов перед сохранением.")
                return@setOnClickListener
            }

            // Запускаем корутину для выполнения операции в фоновом потоке
            lifecycleScope.launch {
                // Получаем максимальный assemblyId из базы данных в фоновом режиме
                val maxAssemblyId = withContext(Dispatchers.IO) {
                    appDatabase.productDao().getMaxAssemblyId()
                }

                // Вычисляем новый assemblyId
                val newAssemblyId = maxAssemblyId?.plus(1L) ?: 1L

                // Создаем объекты ProductEntity для каждого выбранного товара с новым assemblyId
                val productsToSave = mutableListOf<ProductEntity>()

                selectedGraphicsCard?.let { product ->
                    productsToSave.add(ProductEntity(name = product.name, price = product.price, assemblyId = newAssemblyId))
                }
                selectedMotherboard?.let { product ->
                    productsToSave.add(ProductEntity(name = product.name, price = product.price, assemblyId = newAssemblyId))
                }
                selectedProcessor?.let { product ->
                    productsToSave.add(ProductEntity(name = product.name, price = product.price, assemblyId = newAssemblyId))
                }
                selectedRAM?.let { product ->
                    productsToSave.add(ProductEntity(name = product.name, price = product.price, assemblyId = newAssemblyId))
                }
                selectedSSD?.let { product ->
                    productsToSave.add(ProductEntity(name = product.name, price = product.price, assemblyId = newAssemblyId))
                }

                // Вставляем данные в базу данных в фоновом режиме
                insertProductsToDatabase(productsToSave)

                // Отображаем уведомление об успешном сохранении
                showToast("Продукты успешно сохранены в базу данных")
            }
        }
    }


    private fun showToast(message: String) {
        // Создаем и отображаем уведомление Toast
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private suspend  fun insertProductsToDatabase(products: List<ProductEntity>) {
        withContext(Dispatchers.IO) {
            // Получаем DAO для доступа к базе данных
            val productDao = appDatabase.productDao()

            // Вставляем продукты в базу данных
            for (product in products) {
                productDao.insertProduct(product)
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setSelectedGraphicsCard(selectedGraphicsCard)
        viewModel.setSelectedMotherboard(selectedMotherboard)
        viewModel.setSelectedProcessor(selectedProcessor)
        viewModel.setSelectedRAM(selectedRAM)
        viewModel.setSelectedSSD(selectedSSD)
        _binding = null
    }
}
