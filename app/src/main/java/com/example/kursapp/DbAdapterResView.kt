package com.example.kursapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DbAdapterResView(private val context: Context, private val dataList: List<Data>) :
    RecyclerView.Adapter<DbAdapterResView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.db_product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.titleTextView.text = data.title
        holder.totalPriceTextView.text = "Total Price: ${data.totalPrice}"

        // Set product data
        for (i in 0 until data.products.size) {
            val product = data.products[i]
            when (i) {
                0 -> {
                    holder.productName1TextView.text = product.name
                    holder.productPrice1TextView.text = product.price
                }
                1 -> {
                    holder.productName2TextView.text = product.name
                    holder.productPrice2TextView.text = product.price
                }
                2 -> {
                    holder.productName3TextView.text = product.name
                    holder.productPrice3TextView.text = product.price
                }
                3 -> {
                    holder.productName4TextView.text = product.name
                    holder.productPrice4TextView.text = product.price
                }
                4 -> {
                    holder.productName5TextView.text = product.name
                    holder.productPrice5TextView.text = product.price
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewAssemblyTitle)
        val totalPriceTextView: TextView = itemView.findViewById(R.id.textViewTotalPrice)
        val productName1TextView: TextView = itemView.findViewById(R.id.textViewProductName1)
        val productPrice1TextView: TextView = itemView.findViewById(R.id.textViewProductPrice1)
        val productName2TextView: TextView = itemView.findViewById(R.id.textViewProductName2)
        val productPrice2TextView: TextView = itemView.findViewById(R.id.textViewProductPrice2)
        val productName3TextView: TextView = itemView.findViewById(R.id.textViewProductName3)
        val productPrice3TextView: TextView = itemView.findViewById(R.id.textViewProductPrice3)
        val productName4TextView: TextView = itemView.findViewById(R.id.textViewProductName4)
        val productPrice4TextView: TextView = itemView.findViewById(R.id.textViewProductPrice4)
        val productName5TextView: TextView = itemView.findViewById(R.id.textViewProductName5)
        val productPrice5TextView: TextView = itemView.findViewById(R.id.textViewProductPrice5)
    }

    data class Data(val title: String, val totalPrice: String, val products: List<Product>)

    data class Product(val name: String, val price: String)
}
