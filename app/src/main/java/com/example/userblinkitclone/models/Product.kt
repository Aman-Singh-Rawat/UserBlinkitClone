package com.example.userblinkitclone.models

import java.util.UUID


data class Product(
    var productRandomId: String = UUID.randomUUID().toString().replace("-", ""),
    var productTitle: String? = null,
    var productQuantity: Int? = null,
    var productUnit: String? = null,
    var productPrice: Int? = null,
    var productStock: Int? = null,
    var productCategory: String? = null,
    var productType: String? = null,
    var itemCount: Int? = null,
    var adminUid: String? = null,
    var productImageUris: ArrayList<String?>? = null
)

