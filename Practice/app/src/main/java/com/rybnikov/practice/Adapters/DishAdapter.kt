package com.rybnikov.practice.Adapters

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.rybnikov.practice.Converter
import com.rybnikov.practice.Fragments.CartFragment
import com.rybnikov.practice.Fragments.DishFragment
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.R


class DishAdapter(context: Context, resource: Int, dishes: List<Dish>) :
    ArrayAdapter<Dish>(context, resource, dishes) {
    private var inflater: LayoutInflater
    private var layout = resource
    private var productList: List<Dish>? = dishes
    private var dishes:List<Dish>
    private lateinit var db: SQLiteDatabase
    private lateinit var addButton:Button
    init {
        this.inflater = LayoutInflater.from(context)
        this.dishes = dishes
        this.db = context.openOrCreateDatabase("Cart.db", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS Cart (DishID INTEGER, DishCount INTEGER, DishPrice FLOAT)")
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(layout, parent, false)
        val nameTextView = view.findViewById<TextView>(R.id.dishNameTextView)
        val imageView:ImageView = view.findViewById(R.id.dishImage)
        val priceTextView = view.findViewById<TextView>(R.id.dishPriceTextView)
        val button:Button = view.findViewById(R.id.toCartButton)
        val dish: Dish = dishes[position]
        val cursor = db.rawQuery("select * from Cart where DishID = "+dish.DishID, null)
        if (cursor.moveToFirst()) button.text = "Добавлено"
        nameTextView.text = dish.DishName
        button.setOnClickListener{
            if (button.text=="Добавлено") {
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CartFragment()).commit()
            }
            else {
                db.execSQL("insert into Cart values ("+dish.DishID+", 1,"+dish.DishPrice+")")
                button.text = "Добавлено"
            }

        }
        if (dish.DishImage!=null) {
            val bytephoto = Converter.convert(dish.DishImage)
            imageView.setImageBitmap(bytephoto)

        }
        priceTextView.text = "Цена: "+dish.DishPrice.toString()+" руб."
        view.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, DishFragment(dish)).commit()
        }
        return view
    }


}