package com.rybnikov.practice.Fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Converter
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.R

class DishFragment(dish: Dish):Fragment(R.layout.dish_fragment) {
    private val dish:Dish
    init {
        this.dish = dish
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dishName: TextView = view.findViewById(R.id.singleDishName)
        val dishPrice: TextView = view.findViewById(R.id.singleDishPrice)
        val dishDescription: TextView = view.findViewById(R.id.singleDishDescription)
        val dishToCartButton: Button = view.findViewById(R.id.singleDishAddToCartButton)
        val dishImage:ImageView = view.findViewById(R.id.singleDishImage)
        if (dish.DishImage!=null) {
            val bytephoto = Converter.convert(dish.DishImage)
            dishImage.setImageBitmap(bytephoto)

        }
        dishName.text = dish.DishName
        dishPrice.text = "Цена: "+dish.DishPrice.toString()+" руб."
        dishDescription.text = dish.DishDescription
        val db = view.context.openOrCreateDatabase("Cart.db", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select * from Cart where DishID = "+dish.DishID, null)
        if (cursor.moveToFirst()) dishToCartButton.text = "Добавлено"
        dishToCartButton.setOnClickListener{
            if (dishToCartButton.text=="Добавлено") {
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CartFragment()).commit()
            }
            else {
                db.execSQL("insert into Cart values ("+dish.DishID+", 1,"+dish.DishPrice+")")
                dishToCartButton.text = "Добавлено"
            }
        }
    }
}