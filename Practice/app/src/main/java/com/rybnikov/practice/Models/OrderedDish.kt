package com.rybnikov.practice.Models

class OrderedDish(DishID:Int, DishAmount:Int, DishPrice:Float, DishImage:String? = null, DishName:String) {
    var DishID:Int
    var DishPrice:Float
    var DishAmount:Int
    var DishImage:String?
    var DishName:String
    init {
        this.DishID = DishID
        this.DishAmount = DishAmount
        this.DishPrice = DishPrice
        this.DishImage = DishImage
        this.DishName = DishName
    }
}