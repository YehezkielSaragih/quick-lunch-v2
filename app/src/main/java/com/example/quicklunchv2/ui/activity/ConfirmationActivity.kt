package com.example.quicklunchv2.ui.activity

import android.content.Intent
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
import org.json.JSONArray
import androidx.core.content.edit

class ConfirmationActivity : ComponentActivity() {

    private lateinit var rvOrders: RecyclerView
    private lateinit var tvTotalItems: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnBack: Button
    private lateinit var btnConfirm: Button

    private lateinit var db: AppDatabase
    private lateinit var orderList: MutableList<OrderItemModel>
    private lateinit var menuList: List<MenuItemModel>
    private lateinit var adapter: OrderItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        // Function
        initViews()
        db = AppDatabase.get(this)
        loadDataFromPrefs()
        setupRecyclerView()
        displayTotals()
        setupButtons()
    }

    private fun initViews() {
        rvOrders = findViewById(R.id.rvOrders)
        tvTotalItems = findViewById(R.id.tvTotalOrderItem)
        tvTotalPrice = findViewById(R.id.tvTotalOrderPrice)
        btnBack = findViewById(R.id.btnBack)
        btnConfirm = findViewById(R.id.btnConfirm)
    }

    private fun loadDataFromPrefs() {
        val sharedPref = getSharedPreferences("ORDER_PREFS", MODE_PRIVATE)
        val orderJson = sharedPref.getString("order_list", "[]") ?: "[]"
        val jsonArray = JSONArray(orderJson)
        orderList = mutableListOf()
        val menuIds = mutableListOf<Int>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val menuId = obj.getInt("menuId")
            menuIds.add(menuId)
            orderList.add(
                OrderItemModel(
                    menuId = menuId,
                    quantity = obj.getInt("quantity"),
                    note = obj.getString("note"),
                    isSpicy = obj.getBoolean("isSpicy")
                )
            )
        }
        // Save menuIds for later query
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
        tvTotalItems.text = getString(R.string.total_order_count, totalItems)
        tvTotalPrice.text = getString(R.string.total_order_price, totalPrice)
    }

    private fun setupButtons() {
        btnBack.setOnClickListener { finish() }
        btnConfirm.setOnClickListener {
            saveOrdersToDb()
            clearPrefs()
            goToSummary()
        }
    }

    private fun saveOrdersToDb() {
        if (orderList.isNotEmpty()) db.orderItemDao().insertAll(orderList)
    }

    private fun clearPrefs() {
        getSharedPreferences("ORDER_PREFS", MODE_PRIVATE).edit { remove("order_list") }
    }

    private fun goToSummary() {
        startActivity(Intent(this, SummaryActivity::class.java))
        finish()
    }
}