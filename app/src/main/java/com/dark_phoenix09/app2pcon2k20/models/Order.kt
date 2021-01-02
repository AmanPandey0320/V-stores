package com.dark_phoenix09.app2pcon2k20.models

data class Order(
    val orderID:String,
    val code:String,
    val quantity:String,
    val total:String,
    val pName:String,
    val bName:String,
    val bAddress:String,
    val bMobile:String,
    val pMode:String,
    val status:String
)