package com.example.userblinkitclone.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartProducts")
data class CartProducts(
    @PrimaryKey
    val productId: String = "random",

    var productTitle: String? = null,
    var productQuantity: String? = null,
    var productPrice: String? = null,
    var productStock: Int? = null,
    var productCategory: String? = null,
    var productCount: Int? = null,
    var productImage: String? = null,
    var adminUid: String? = null,

    )