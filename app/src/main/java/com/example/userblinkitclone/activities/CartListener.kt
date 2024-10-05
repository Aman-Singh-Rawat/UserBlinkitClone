package com.example.userblinkitclone.activities

interface CartListener {
    fun showCartLayout(itemCount: Int)

    fun savingCartItemCount(itemCount: Int)
}