package com.dark_phoenix09.app2pcon2k20.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mycart")
data class mycart (

    @NonNull
    @PrimaryKey
    var image: String,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var description: String,
    @ColumnInfo
    var price: String,
    @ColumnInfo
    var code: String,
    @ColumnInfo
    var type: String
)