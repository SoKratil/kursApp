package com.example.kursapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.navigation.fragment.findNavController
import com.example.kursapp.Product

import com.example.kursapp.R
import com.squareup.picasso.Picasso

class FragmentDetail : Fragment() {

    private lateinit var productNameTextView: TextView
    private lateinit var productDescriptionTextView: TextView
    private lateinit var productPriceTextView: TextView
    private lateinit var productImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)


        // Инициализация views
        productNameTextView = view.findViewById(R.id.productNameTextView)
        productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView)
        productPriceTextView = view.findViewById(R.id.productPriceTextView)
        productImageView = view.findViewById(R.id.productImageView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение данных о продукте из переданных аргументов
        val product = arguments?.getParcelable<Product>(/* key = */ "product")

        // Проверка на null
        product?.let {
            // Установка данных продукта в соответствующие views
            productNameTextView.text = product.name
            productDescriptionTextView.text = product.description
            productPriceTextView.text = "Price: $${product.price}"

            // Загрузка изображения с помощью Picasso
            Picasso.get().load(product.imgUrl).into(productImageView)

            view.findViewById<ImageButton>(R.id.backtomainButton).setOnClickListener {
                // Возвращаемся на предыдущий экран
                findNavController().popBackStack()
            }
        }
    }
}
