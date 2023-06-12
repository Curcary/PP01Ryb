package com.rybnikov.practice.Fragments

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rybnikov.practice.Adapters.OrdersAdapter
import com.rybnikov.practice.Api.RestrauntApiClient
import com.rybnikov.practice.CurrentUser
import com.rybnikov.practice.Models.Order
import com.rybnikov.practice.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.HttpException

class OrdersFragment: Fragment(R.layout.orders_fragment) {
    private val client by lazy {
        this@OrdersFragment.context?.let { RestrauntApiClient.create(it) }
    }
    fun setList(view: View ) {
        var orders: List<Order> = ArrayList()
        CoroutineScope(Dispatchers.IO).launch {
            client?.getUsersOrders(
                CurrentUser.user.UserID
            )
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result ->
                        orders = result
                        for (i in orders.indices) {
                            client?.getOrderedDishesByID(
                                orders[i].OrderID, orders[i].OrderDate
                            )
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe(
                                    { result ->
                                        orders[i].Ordered_Dishes = result
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
                                            this@OrdersFragment.context,
                                            errorMessage,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                )
                            Thread.sleep(50)
                        }

                        val ordersListView: ListView =
                            view.findViewById(R.id.ordersListView)
                        val ordersAdapter =
                            context?.let { OrdersAdapter(it, R.layout.order_item, orders.asReversed()) }
                        ordersListView.adapter = ordersAdapter
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
                        Toast.makeText(this@OrdersFragment.context, errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                )

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setList(view)

    }
}