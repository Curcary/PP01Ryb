package com.rybnikov.practice.Models

import com.rybnikov.practice.CurrentUser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Order {
    var OrderID:Int = 0
    var OrderDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
    var UserID:Int = CurrentUser.user.UserID
    var Ordered_Dishes: List<OrderedDish> = ArrayList()
    var OrderAddress:String = ""
    var CheckStatus:Boolean = true
}