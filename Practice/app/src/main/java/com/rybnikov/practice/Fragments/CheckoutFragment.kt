package com.rybnikov.practice.Fragments

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.CartDishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.Models.Dish
import com.rybnikov.practice.Models.Order
import com.rybnikov.practice.Models.OrderedDish
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CheckoutFragment: Fragment(R.layout.checkout_fragment) {
    lateinit var db: SQLiteDatabase
    private val client by lazy {
        this@CheckoutFragment.context?.let { RestrauntApiClient.create(it) }
    }
    lateinit var dishes: ArrayList<Dish>
    var amounts: ArrayList<Int> = ArrayList()

    private fun updateSum() {
        val cursor = db.rawQuery("select * from Cart", null)
        if (cursor.moveToFirst())
            while (true) {
                if (cursor.isLast) break
                cursor.moveToNext()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sumTextView: TextView =
            (context as AppCompatActivity).findViewById(R.id.checkoutSumTextView)
        db = view.context.openOrCreateDatabase("Cart.db", Context.MODE_PRIVATE, null)
        val addressEditText:EditText = view.findViewById(R.id.checkoutEdittext)
        val cursor = db.rawQuery("select * from cart", null)
        if (cursor.moveToFirst()) {
            val listView = view.findViewById<ListView>(R.id.checkoutListView)
            var ids_str: String = ""
            var sum = 0f
            while (true) {
                ids_str += cursor.getInt(0).toString()
                amounts.add(cursor.getInt(1))
                sum += cursor.getInt(1) * cursor.getFloat(2)
                if (cursor.isLast) break
                ids_str += "+"
                cursor.moveToNext()
            }
            sumTextView.text = "Сумма: " + sum + " руб."

            CoroutineScope(Dispatchers.IO).launch {
                client?.getDishesByIDs(
                    ids_str
                )
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { result ->
                            val adapter: CartDishAdapter? =
                                this@CheckoutFragment.context?.let {
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
                            Toast.makeText(
                                this@CheckoutFragment.context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    )
            }
        }
        updateSum()

        val makeOrderButton: Button = view.findViewById(R.id.makeOrderButton)
        makeOrderButton.setOnClickListener {
            val order: Order = Order()
            order.OrderAddress= addressEditText.text.toString()
            var array: ArrayList<OrderedDish> = ArrayList()
            for (dish in 0 until dishes.size) {
                array.add(OrderedDish(dishes[dish].DishID, amounts[dish], dishes[dish].DishPrice,null, dishes[dish].DishName))
            }
            order.Ordered_Dishes = array
            CoroutineScope(Dispatchers.IO).launch {
                client?.makeOrder(
                    order
                )
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { result ->
                            Toast.makeText(
                                this@CheckoutFragment.context,
                                result.message,
                                Toast.LENGTH_LONG
                            ).show()
                            db.execSQL("delete from Cart")
                            (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_view, OrdersFragment()).commit()
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
                            Toast.makeText(
                                this@CheckoutFragment.context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
            }
        }
    }
}
