package com.rybnikov.practice.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rybnikov.practice.Converter
import com.rybnikov.practice.Models.OrderedDish
import com.rybnikov.practice.R


class OrdersDishAdapter(ordered_dishes:List<OrderedDish>, context:Context): RecyclerView.Adapter<OrdersDishAdapter.ViewHolder>() {
    private val orderedDishes:List<OrderedDish>
    private val layoutInflater:LayoutInflater
    init {
        this.orderedDishes = ordered_dishes
        this.layoutInflater = LayoutInflater.from(context)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val dishImageView:ImageView = itemView.findViewById<ImageView>(R.id.ordered_dish_image)
        val priceTextView:TextView = itemView.findViewById(R.id.ordered_dish_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersDishAdapter.ViewHolder {
        val view = layoutInflater.inflate(R.layout.orders_dish_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersDishAdapter.ViewHolder, position: Int) {
        val orderedDish: OrderedDish = orderedDishes[position]
        if (orderedDish.DishImage!=null)
            holder.dishImageView.setImageBitmap(orderedDish.DishImage?.let { Converter.convert(it) })
        holder.priceTextView.text = (orderedDish.DishPrice*orderedDish.DishAmount).toString()+"₽"
    }

    override fun getItemCount(): Int {
        return orderedDishes.size
    }
}