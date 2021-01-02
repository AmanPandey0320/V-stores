package com.dark_phoenix09.app2pcon2k20.myCart

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dark_phoenix09.app2pcon2k20.models.mycart

@Database(entities = arrayOf(mycart::class),version = 1)
abstract class myCartDatabase:RoomDatabase() {
    abstract fun cartDAO():cartDAO
}