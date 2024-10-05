package com.example.userblinkitclone.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update

@Dao
interface CartProductDao {
    @Insert
    fun insertCartProduct(products: CartProducts)

    @Update
    fun updateCartProduct(products: CartProducts)
}