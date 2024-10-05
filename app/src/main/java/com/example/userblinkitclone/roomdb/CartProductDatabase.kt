package com.example.userblinkitclone.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CartProducts::class], version = 1, exportSchema = false)
abstract class CartProductDatabase : RoomDatabase() {

    abstract fun cartProductDao(): CartProductDao

    companion object {
        @Volatile
        var INSTANCE: CartProductDatabase? = null

        fun getDatabaseInstance(context: Context): CartProductDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        CartProductDatabase::class.java,
                        "CartProduct"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}