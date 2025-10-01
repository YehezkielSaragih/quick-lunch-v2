package com.example.quicklunchv2.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quicklunchv2.R
import com.example.quicklunchv2.adapter.OrderItemAdapter
import com.example.quicklunchv2.config.AppDatabase
import com.example.quicklunchv2.model.MenuItemModel
import com.example.quicklunchv2.model.OrderItemModel

class SummaryActivity : ComponentActivity() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var tvTotalOrderItems: TextView
    private lateinit var tvTotalOrderPrice: TextView
    private lateinit var btnBack: Button
    private lateinit var btnReset: Button

    private lateinit var db: AppDatabase
    private lateinit var orderList: MutableList<OrderItemModel>
    private lateinit var menuList: List<MenuItemModel>
    private lateinit var adapter: OrderItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)
        // Function
        initViews()
        db = AppDatabase.get(this)
        loadData()
        setupRecyclerView()
        displayTotals()
        setupButtons()
    }

    private fun initViews() {
        rvOrders = findViewById(R.id.rvOrders)
        tvTotalOrderItems = findViewById(R.id.tvTotalOrderItems)
        tvTotalOrderPrice = findViewById(R.id.tvTotalOrderPrice)
        btnBack = findViewById(R.id.btnBack)
        btnReset = findViewById(R.id.btnReset)
    }

    private fun loadData() {
        orderList = db.orderItemDao().getAll().toMutableList()
        val menuIds = orderList.map { it.menuId }
        menuList = db.menuItemDao().getMenusByIds(menuIds)
    }

    private fun setupRecyclerView() {
        adapter = OrderItemAdapter(orderList, menuList)
        rvOrders.layoutManager = LinearLayoutManager(this)
        rvOrders.adapter = adapter
    }

    private fun displayTotals() {
        val menuMap = menuList.associateBy { it.id }
        val totalItems = orderList.sumOf { it.quantity }
        val totalPrice = orderList.sumOf { order -> (menuMap[order.menuId]?.price ?: 0) * order.quantity }
        tvTotalOrderItems.text = getString(R.string.total_order_count, totalItems)
        tvTotalOrderPrice.text = getString(R.string.total_order_price, totalPrice)
    }

    private fun setupButtons() {
        btnBack.setOnClickListener { finish() }
        btnReset.setOnClickListener { resetOrders() }
    }

    private fun resetOrders() {
        db.orderItemDao().clear()
        orderList.clear()
        adapter.notifyDataSetChanged()
        tvTotalOrderItems.text = getString(R.string.total_order_count, 0)
        tvTotalOrderPrice.text = getString(R.string.total_order_price, 0)
    }
}