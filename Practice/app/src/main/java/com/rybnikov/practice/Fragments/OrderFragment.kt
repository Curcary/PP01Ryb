package com.rybnikov.practice.Fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rybnikov.practice.Adapters.OrdersDishAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.Models.Order
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException


class OrderFragment(order: Order):Fragment(R.layout.order_fragment) {
    private val client by lazy {
        this@OrderFragment.context?.let { RestrauntApiClient.create(it) }
    }
    val order:Order
    init {
        this.order = order
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idView = view.findViewById<TextView>(R.id.orderIDTextView)
        val dateView: TextView = view.findViewById(R.id.orderDateTextView)
        val statusView = view.findViewById<TextView>(R.id.orderStatusTextView)
        val dishesView: RecyclerView = view.findViewById(R.id.orderDishesView)
        idView.text ="Номер: "+order.OrderID.toString()
        dateView.text = "Дата заказа: "+order.OrderDate
        when {
            !order.CheckStatus -> statusView.text = "Завершён"
            else -> statusView.text = "В доставке"
        }
        val layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dishesView.layoutManager = layoutManager
        dishesView.adapter = OrdersDishAdapter(order.Ordered_Dishes, view.context)

        val sendEmailButton: Button = view.findViewById(R.id.sendEmailButton)
        sendEmailButton.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                client?.sendEmail(CurrentUser.user.UserEmail, order)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                        { result ->
                            Toast.makeText(
                                this@OrderFragment.context,
                                result.message,
                                Toast.LENGTH_LONG
                            ).show()

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
                                this@OrderFragment.context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
            }
        }
    }
}