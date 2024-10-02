package com.example.userblinkitclone.viewmodels

import androidx.lifecycle.ViewModel
import com.example.userblinkitclone.models.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserViewModel : ViewModel() {
    private val firebaseDatabaseInstance = FirebaseDatabase.getInstance(
        "https://blinkit-clone-b8338-default-rtdb.asia-southeast1.firebasedatabase.app/"
    )
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

    fun getCategoryProduct(category: String) : Flow<List<Product>> = callbackFlow {
        val db = firebaseDatabaseInstance.getReference("Admins")
            .child("ProductCategory/${category}")

        val eventListener = object: ValueEventListener {
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
}