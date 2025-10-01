package com.example.quicklunchv2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = MenuItemModel::class,
            parentColumns = ["id"],
            childColumns = ["menuId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val menuId: Int,
    var quantity: Int = 0,
    var note: String = "",
    var isSpicy: Boolean = false
)