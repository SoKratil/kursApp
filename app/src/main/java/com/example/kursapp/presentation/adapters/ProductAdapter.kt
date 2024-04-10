package com.example.kursapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kursapp.presentation.Product
import com.example.kursapp.R

import com.squareup.picasso.Picasso

class ProductAdapter(
    private var productList: List<Product>,
    private val listener: OnProductClickListener // Интерфейс для обработки нажатий
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        val imageView: ImageView = itemView.findViewById(R.id.productImageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val product = productList[position]
                listener.onProductClick(product) // Вызываем метод интерфейса при нажатии на элемент
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "${product.price.toString()}$"
        holder.descriptionTextView.text = product.description
        // Загрузка изображения с помощью библиотеки Picasso
        Picasso.get().load(product.imgUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }

    // Интерфейс для обработки нажатий на элементы списка
    interface OnProductClickListener {
        fun onProductClick(product: Product)
    }
}
