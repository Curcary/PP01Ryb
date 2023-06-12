package com.rybnikov.practice.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.R


class CartDishAdapter(context: Context, resource: Int, dishes: ArrayList<Dish>) :
    ArrayAdapter<Dish>(context, resource, dishes) {
    private var inflater: LayoutInflater
    private var layout = resource
    private var dishes:ArrayList<Dish>
    private var db: SQLiteDatabase
    init {
        this.inflater = LayoutInflater.from(context)
        this.dishes = dishes
        this.db = context.openOrCreateDatabase("Cart.db", MODE_PRIVATE, null)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateSum(){
        val sumTextView:TextView = (context as AppCompatActivity).findViewById(R.id.cartSumTextView)
        var sum = 0f
        val cursor = db.rawQuery("select * from Cart", null)

        if (cursor.moveToFirst())
        while (true) {
            sum += cursor.getInt(1)*cursor.getFloat(2)
            if (cursor.isLast) break
            cursor.moveToNext()
        }
        sumTextView.text = "Сумма: "+sum+" руб."
    }
    @Throws(IllegalArgumentException::class)
    private fun convert(base64Str: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(layout, parent, false)
        val nameTextView = view.findViewById<TextView>(R.id.cartdishTextView)
        val quantityTextView = view.findViewById<TextView>(R.id.quantityTextView)
        val imageView:ImageView = view.findViewById(R.id.cartdishImageView)
        val priceTextView = view.findViewById<TextView>(R.id.cartdishPriceTextView)
        val plusbutton:Button = view.findViewById(R.id.plusButton)
        val minusbutton:Button = view.findViewById(R.id.minusButton)
        val deletebutton:Button = view.findViewById(R.id.deleteDishButton)
        val dish: Dish = dishes[position]
        deletebutton.setOnClickListener{
            db.execSQL("delete from Cart where DishID = "+dish.DishID)
            this.remove(dish)
            updateSum()
        }
        var cursor = db.rawQuery("select * from Cart where DishID = "+dish.DishID, null)
        cursor.moveToFirst()
        quantityTextView.text = cursor.getInt(1).toString()
        nameTextView.text = dish.DishName
        plusbutton.setOnClickListener {
            db.execSQL(
                "update Cart set DishCount = DishCount+1 where DishID=" + cursor.getInt(
                    0
                )
            )
            val cursor = db.rawQuery("select * from Cart where DishID = "+dish.DishID, null)
            cursor.moveToFirst()
            quantityTextView.text = cursor.getInt(1).toString()
            updateSum()
        }
        minusbutton.setOnClickListener {
            if (quantityTextView.text!="1") {
                db.execSQL(
                    "update Cart set DishCount = DishCount-1 where DishID=" + cursor.getInt(
                        0
                    )
                )
                val cursor = db.rawQuery("select * from Cart where DishID = " + dish.DishID, null)
                cursor.moveToFirst()
                quantityTextView.text = cursor.getInt(1).toString()
                updateSum()
            }
        }
        if (dish.DishImage!=null) {
            val bytephoto = convert(dish.DishImage)
            imageView.setImageBitmap(bytephoto)

        }
        priceTextView.text = "Цена: "+dish.DishPrice.toString()+" руб."
        return view
    }


}