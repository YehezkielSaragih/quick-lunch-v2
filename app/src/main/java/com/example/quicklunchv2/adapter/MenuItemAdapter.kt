package com.example.quicklunchv2.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quicklunchv2.R
import com.example.quicklunchv2.model.OrderItemModel
import com.example.quicklunchv2.model.MenuItemModel

class MenuItemAdapter(
    val menuList: List<MenuItemModel>,
    val orderList: MutableList<OrderItemModel>
) : RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder>() {

    // ViewHolder class to hold references to item views
    class MenuItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMenu: ImageView = view.findViewById(R.id.ivMenu)
        val tvNamePrice: TextView = view.findViewById(R.id.tvNamePrice)
        val etQty: EditText = view.findViewById(R.id.etQty)
        val etNote: EditText = view.findViewById(R.id.etNote)
        val switchSpicy: Switch = view.findViewById(R.id.switchSpicy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_menu_item, parent, false)
        return MenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menu = menuList[position]

        // Set menu name and price
        holder.tvNamePrice.text = "${menu.name} - Rp${menu.price}"

        // Set menu image
        holder.ivMenu.setImageResource(menu.iconResId)

        // Get or create related order item
        var order = orderList.getOrNull(position)
        if (order == null) {
            order = OrderItemModel(menuId = menu.id)
            orderList.add(order)
        }

        // Initialize fields with existing order data
        holder.etQty.setText(if (order.quantity > 0) order.quantity.toString() else "")
        holder.etNote.setText(order.note)
        holder.switchSpicy.isChecked = order.isSpicy

        // Listener for quantity input
        holder.etQty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val qty = s.toString().toIntOrNull() ?: 0
                order.quantity = qty
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Listener for note input
        holder.etNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                order.note = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Listener for spicy switch toggle
        holder.switchSpicy.setOnCheckedChangeListener { _, isChecked ->
            order.isSpicy = isChecked
        }
    }

    override fun getItemCount(): Int = menuList.size
}