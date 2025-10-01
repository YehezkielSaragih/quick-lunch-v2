package com.example.quicklunchv2.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quicklunchv2.model.MenuItemModel

@Dao
interface MenuItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(menu: MenuItemModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(menus: List<MenuItemModel>)

    @Query("SELECT * FROM menu_items")
    fun getAll(): List<MenuItemModel>

    @Query("SELECT * FROM menu_items WHERE id IN (:ids)")
    fun getMenusByIds(ids: List<Int>): List<MenuItemModel>

    @Query("SELECT * FROM menu_items WHERE id = :id LIMIT 1")
    fun getById(id: Int): MenuItemModel?

    @Query("DELETE FROM menu_items")
    fun clear()
}
