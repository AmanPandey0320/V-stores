package com.dark_phoenix09.app2pcon2k20.myCart

import androidx.room.*
import com.dark_phoenix09.app2pcon2k20.models.mycart

@Dao
interface cartDAO {
    @Query("SELECT * FROM mycart")
    fun getCart():Array<mycart>

    @Insert
    fun insertIntoCart(mycart: mycart)

    @Delete
    fun deleteFromCart(mycart: mycart)


}