package com.example.kursapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.kursapp.Product
import com.example.kursapp.databinding.FragmentDetailBinding
import com.squareup.picasso.Picasso

class FragmentDetail : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение данных о продукте из переданных аргументов
        val product = arguments?.getParcelable<Product>("product")

        // Проверка на null
        product?.let {
            // Установка данных продукта в соответствующие views
            binding.productNameTextView.text = product.name
            binding.productDescriptionTextView.text = product.description
            binding.productPriceTextView.text = "Price: $${product.price}"

            // Загрузка изображения с помощью Picasso
            Picasso.get().load(product.imgUrl).into(binding.productImageView)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
