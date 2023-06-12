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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.rybnikov.practice.Converter
import com.rybnikov.practice.Fragments.CartFragment
import com.rybnikov.practice.Fragments.DishFragment
import com.rybnikov.practice.Fragments.OrderFragment
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.Models.Order
import com.rybnikov.practice.R


class OrdersAdapter(context: Context, resource: Int, orders: List<Order>) :
    ArrayAdapter<Order>(context, resource, orders) {
    private var inflater: LayoutInflater
    private var layout = resource
    private var orders:List<Order>
    init {
        this.inflater = LayoutInflater.from(context)
        this.orders = orders
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(layout, parent, false)
        val idView = view.findViewById<TextView>(R.id.ordersIDTextView)
        val dateView:TextView = view.findViewById(R.id.ordersDateTextView)
        val statusView = view.findViewById<TextView>(R.id.ordersStatusTextView)
        val dishesView:RecyclerView = view.findViewById(R.id.ordersDishesView)
        val order: Order = orders[position]
        view.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, OrderFragment(order)).commit()
        }
        idView.text ="Номер: "+order.OrderID.toString()
        dateView.text = "Дата заказа: "+order.OrderDate
        when {
            !order.CheckStatus -> statusView.text = "Завершён"
            else -> statusView.text = "В доставке"
        }
        val layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dishesView.layoutManager = layoutManager
        dishesView.adapter = OrdersDishAdapter(order.Ordered_Dishes, context)
        return view
    }
}