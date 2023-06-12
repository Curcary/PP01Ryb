package com.rybnikov.practice.Adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.rybnikov.practice.Fragments.CartFragment
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.R

class CheckoutDishAdapter(context: Context, resource: Int, dishes: List<Dish>) :
    ArrayAdapter<Dish>(context, resource, dishes) {
    private var inflater: LayoutInflater
    private var layout = resource
    private var dishes:List<Dish>
    private var db: SQLiteDatabase
    init {
        this.inflater = LayoutInflater.from(context)
        this.dishes = dishes
        this.db = context.openOrCreateDatabase("Cart.db", Context.MODE_PRIVATE, null)
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
        val nameTextView = view.findViewById<TextView>(R.id.checkout_dish_name)
        val imageView: ImageView = view.findViewById(R.id.checkout_dish_image)
        val priceTextView = view.findViewById<TextView>(R.id.checkout_dish_price)
        val quantityTextView: TextView = view.findViewById(R.id.checkout_dish_quantity)
        val dish: Dish = dishes[position]
        nameTextView.text = dish.DishName
        val cursor = db.rawQuery("select * from Cart where DishID = "+dish.DishID, null)
        if (cursor.moveToFirst()){
            quantityTextView.text = cursor.getFloat(2).toString()
        }
        if (dish.DishImage!=null) {
            val bytephoto = convert(dish.DishImage)
            imageView.setImageBitmap(bytephoto)

        }
        priceTextView.text = "Цена: "+dish.DishPrice.toString()+" руб."
        return view
    }


}