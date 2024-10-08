package com.example.userblinkitclone.viewmodels

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userblinkitclone.models.Product
import com.example.userblinkitclone.roomdb.CartProductDao
import com.example.userblinkitclone.roomdb.CartProductDatabase
import com.example.userblinkitclone.roomdb.CartProducts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserViewModel(application: Application) : AndroidViewModel(application) {
    //initialization
    private var sharedPreferences: SharedPreferences =
        application.getSharedPreferences("My_Pref", MODE_PRIVATE)
    private val firebaseDatabaseInstance = FirebaseDatabase.getInstance(
        "https://blinkit-clone-b8338-default-rtdb.asia-southeast1.firebasedatabase.app/"
    )
    private val cartProductDao: CartProductDao =
        CartProductDatabase.getDatabaseInstance(application).cartProductDao()

    //Room Db
    suspend fun insertCartProduct(product: CartProducts) {
        cartProductDao.insertCartProduct(product)
    }

    suspend fun updateCartProduct(product: CartProducts) {
        cartProductDao.updateCartProduct(product)
    }

    suspend fun deleteCartProduct(productId: String) {
        cartProductDao.deleteCartProduct(productId)
    }

    fun getAllCartProduct() = cartProductDao.getAllCartProducts()

    //Firebase Integration
    fun fetchAllProducts(): Flow<List<Product>> = callbackFlow {
        val db = firebaseDatabaseInstance.getReference("Admins").child("AllProducts")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children) {
                    val value = product.getValue(Product::class.java)
                    if (value != null) {
                        products.add(value)
                    }
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener) }
    }

    fun getCategoryProduct(category: String): Flow<List<Product>> = callbackFlow {
        val db = firebaseDatabaseInstance.getReference("Admins")
            .child("ProductCategory/${category}")

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = ArrayList<Product>()
                for (product in snapshot.children) {
                    val value = product.getValue(Product::class.java)
                    if (value != null) {
                        products.add(value)
                    }
                }
                trySend(products)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        db.addValueEventListener(eventListener)
        awaitClose { db.removeEventListener(eventListener) }
    }

    //SharedPreference
    fun savingCartItemCount(itemCount: Int) {
        sharedPreferences.edit().putInt("itemCount", itemCount).apply()
    }

    fun fetchTotalCartItemCount(): MutableLiveData<Int> {
        val totalItemCount = MutableLiveData<Int>()
        totalItemCount.value = sharedPreferences.getInt("itemCount", 0)
        return totalItemCount
    }
}