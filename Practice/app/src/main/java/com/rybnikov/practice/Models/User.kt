package com.rybnikov.practice.Models

class User( UserSurname:String,
           UserName: String, UserPatr:String, UserEmail:String, UserPassword: String) {
    var UserID:Int = 0
    var UserSurname:String = ""
    var UserName:String = ""
    var UserPatr:String =""
    var UserEmail:String = ""
    var UserPassword = ""
    init {
        this.UserName = UserName
        this.UserSurname = UserSurname
        this.UserPatr = UserPatr
        this.UserEmail = UserEmail
        this.UserPassword = UserPassword
    }
}