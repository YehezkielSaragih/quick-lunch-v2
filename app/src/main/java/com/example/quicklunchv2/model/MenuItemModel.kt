package com.example.quicklunchv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Int,
    val iconResId: Int
)