package com.example.quicklunchv2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quicklunchv2.R
import com.example.quicklunchv2.model.MenuItemModel
import com.example.quicklunchv2.model.OrderItemModel

class OrderItemAdapter(
    val orderList: List<OrderItemModel>,
    val menuList: List<MenuItemModel>
) : RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>() {

    class OrderItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderItem: TextView = view.findViewById(R.id.tvOrderItem)
        val tvOrderItemNote: TextView = view.findViewById(R.id.tvOrderItemNote)
        val tvOrderItemPrice: TextView = view.findViewById(R.id.tvOrderItemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_order_item, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val order = orderList[position]

        // Find the corresponding menu item
        val menu = menuList.find { it.id == order.menuId }

        menu?.let {
            // Display menu name + quantity + spicy label
            val spicyText = if (order.isSpicy) " - Spicy" else ""
            holder.tvOrderItem.text = "${it.name} x${order.quantity}$spicyText"

            // Display note or "-" if empty
            holder.tvOrderItemNote.text = if (order.note.isNotEmpty()) order.note else "-"

            // Display total price
            val totalPrice = it.price * order.quantity
            holder.tvOrderItemPrice.text = "Rp$totalPrice"
        }
    }

    override fun getItemCount(): Int = orderList.size
}