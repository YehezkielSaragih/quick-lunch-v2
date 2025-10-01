package com.example.quicklunchv2.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.quicklunchv2.model.OrderItemModel

@Dao
interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(order: OrderItemModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(orders: List<OrderItemModel>)

    @Query("SELECT * FROM order_items")
    fun getAll(): List<OrderItemModel>

    @Query("""
    SELECT SUM(o.quantity * m.price) 
    FROM order_items o 
    JOIN menu_items m ON o.menuId = m.id""")
    fun getTotalPrice(): Int

    @Query("SELECT * FROM order_items WHERE id = :id LIMIT 1")
    fun getById(id: Int): OrderItemModel?

    @Query("DELETE FROM order_items")
    fun clear()
}