package com.rybnikov.practice.Fragments

import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.CartDishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CartFragment: Fragment(R.layout.cart_fragment) {
    lateinit var db:SQLiteDatabase
    private val client by lazy {
        this@CartFragment.context?.let { RestrauntApiClient.create(it) }
    }
    lateinit var dishes: ArrayList<Dish>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = view.context.openOrCreateDatabase("Cart.db", MODE_PRIVATE, null)
        val cursor = db.rawQuery("select * from cart", null)
        if (cursor.moveToFirst()) {
            val listView = view.findViewById<ListView>(R.id.cartListView)
            var ids_str: String = ""
            while (true) {
                ids_str += cursor.getInt(0).toString()
                if (cursor.isLast) break
                ids_str += "+"
                cursor.moveToNext()
            }
            CoroutineScope(Dispatchers.IO).launch {
                client?.getDishesByIDs(
                    ids_str
                )
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { result ->
                            val adapter: CartDishAdapter? =
                                this@CartFragment.context?.let {
                                    CartDishAdapter(
                                        it,
                                        R.layout.dish_cart_item,
                                        result
                                    )
                                }
                            listView.adapter = adapter
                            dishes = result
                        },
                        { error ->
                            var errorMessage: String = ""
                            errorMessage = when (error) {
                                is HttpException -> {
                                    when (error.code()) {
                                        401 -> "Необходимо авторизоваться"
                                        403 -> "Это действие нельзя выполнить"
                                        else -> "Неизвестная ошибка"
                                    }
                                }
                                else -> {
                                    error.localizedMessage
                                }
                            }
                            println(errorMessage)
                            Toast.makeText(this@CartFragment.context, errorMessage, Toast.LENGTH_LONG)
                                .show()
                        }
                    )
            }
        }
        updateSum()

        val makeOrderButton:Button = view.findViewById(R.id.goToCheckoutButton)

        makeOrderButton.setOnClickListener{
            if (CurrentUser.user.UserID!=0)
            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, CheckoutFragment()).commit()
            else
                (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, RequestLoginFragment()).commit()
        }
    }
    private fun updateSum(){
        val sumTextView: TextView = (context as AppCompatActivity).findViewById(R.id.cartSumTextView)
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
}