package com.example.quicklunchv2.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quicklunchv2.R
import com.example.quicklunchv2.adapter.MenuItemAdapter
import com.example.quicklunchv2.config.AppDatabase
import com.example.quicklunchv2.model.MenuItemModel
import com.example.quicklunchv2.model.OrderItemModel
import org.json.JSONArray
import org.json.JSONObject

class OrderActivity : ComponentActivity() {

    private lateinit var rvMenu: RecyclerView
    private lateinit var btnGoConfirm: Button
    private lateinit var btnGoSummary: Button

    private lateinit var db: AppDatabase
    private lateinit var menuList: List<MenuItemModel>
    private lateinit var orderList: MutableList<OrderItemModel>
    private lateinit var adapter: MenuItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // Setup
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        // Function
        initViews()
        db = AppDatabase.get(this)
        seedData()
        loadData()
        setupRecyclerView()
        setupButtons()
    }

    override fun onRestart() {
        // Setup
        super.onRestart()
        // Function
        loadData()
        setupRecyclerView()
    }

    private fun initViews() {
        rvMenu = findViewById(R.id.rvMenu)
        btnGoConfirm = findViewById(R.id.btnGoConfirm)
        btnGoSummary = findViewById(R.id.btnGoSummary)
    }

    private fun seedData(){
        // Sample menu items
        val sampleMenu = listOf(
            MenuItemModel(name = "Nasi Goreng", price = 20000, iconResId = R.drawable.sample_img),
            MenuItemModel(name = "Mie Ayam", price = 15000, iconResId = R.drawable.sample_img),
            MenuItemModel(name = "Sate Ayam", price = 25000, iconResId = R.drawable.sample_img),
            MenuItemModel(name = "Bakso", price = 18000, iconResId = R.drawable.sample_img),
            MenuItemModel(name = "Ayam Goreng", price = 22000, iconResId = R.drawable.sample_img)
        )
        // Insert menu items into Room (IDs auto-generated)
        db.menuItemDao().insertAll(sampleMenu)
    }

    private fun loadData() {
        // Fetch all menu items
        menuList = db.menuItemDao().getAll()
        // Initialize empty order list
        orderList = mutableListOf()
    }

    private fun setupRecyclerView() {
        adapter = MenuItemAdapter(menuList, orderList)
        rvMenu.layoutManager = LinearLayoutManager(this)
        rvMenu.adapter = adapter
    }

    private fun setupButtons() {
        val sharedPref = getSharedPreferences("ORDER_PREFS", MODE_PRIVATE)

        btnGoConfirm.setOnClickListener {
            saveOrdersToPrefs(sharedPref)
            goToConfirmation()
        }

        btnGoSummary.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }

    private fun saveOrdersToPrefs(sharedPref: android.content.SharedPreferences) {
        val jsonArray = JSONArray()
        adapter.orderList.filter { it.quantity > 0 }.forEach { order ->
            val jsonObject = JSONObject()
            jsonObject.put("menuId", order.menuId)
            jsonObject.put("quantity", order.quantity)
            jsonObject.put("note", order.note)
            jsonObject.put("isSpicy", order.isSpicy)
            jsonArray.put(jsonObject)
        }
        sharedPref.edit().putString("order_list", jsonArray.toString()).apply()
    }

    private fun goToConfirmation() {
        startActivity(Intent(this, ConfirmationActivity::class.java))
    }
}