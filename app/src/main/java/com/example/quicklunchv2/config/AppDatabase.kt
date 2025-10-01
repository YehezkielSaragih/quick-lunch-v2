package com.example.quicklunchv2.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quicklunchv2.model.MenuItemModel
import com.example.quicklunchv2.model.OrderItemModel
import com.example.quicklunchv2.repository.MenuItemDao
import com.example.quicklunchv2.repository.OrderItemDao

@Database(
    entities = [MenuItemModel::class, OrderItemModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao
    abstract fun orderItemDao(): OrderItemDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(ctx: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDatabase::class.java,
                    "quicklunch.db"
                )
                    // DEMO: izinkan query di main thread.
                    // Produksi sebaiknya pakai coroutine atau RxJava.
                    .allowMainThreadQueries()
                    .build().also { INSTANCE = it }
            }
    }
}