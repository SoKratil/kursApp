package com.example.kursapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kursapp.Product
import com.example.kursapp.ProductAdapter


import com.example.kursapp.R
import com.google.firebase.database.*

class mainlist : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mainlist, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация базы данных Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("products")

        // Создание слушателя для базы данных Firebase
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val productList = mutableListOf<Product>()
                // Проходимся по каждому элементу в базе данных и добавляем его в список продуктов
                for (categorySnapshot in dataSnapshot.children) {
                    for (productSnapshot in categorySnapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let { productList.add(it) }
                    }
                }
                // Создание адаптера и передача списка продуктов
                productAdapter = ProductAdapter(productList)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = productAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки при чтении данных из базы данных
            }
        }

        // Добавляем слушателя к базе данных Firebase
        databaseReference.addValueEventListener(valueEventListener)
    }
}
