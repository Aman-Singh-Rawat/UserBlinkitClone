package com.example.userblinkitclone.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(products: CartProducts)

    @Update
    suspend fun updateCartProduct(products: CartProducts)

    @Query("DELETE FROM CartProducts WHERE productId = :productId")
    suspend fun deleteCartProduct(productId: String)

    @Query("SELECT * FROM CartProducts")
    fun getAllCartProducts(): LiveData<List<CartProducts>>

}