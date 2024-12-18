package com.example.sqlitepractice1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var productListView: ListView
    private lateinit var productName: EditText
    private lateinit var productWeight: EditText
    private lateinit var productPrice: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        productListView = findViewById(R.id.productListView)
        productName = findViewById(R.id.productName)
        productWeight = findViewById(R.id.productWeight)
        productPrice = findViewById(R.id.productPrice)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = productName.text.toString()
            val weight = productWeight.text.toString().toDoubleOrNull() ?: 0.0
            val price = productPrice.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isNotEmpty()) {
                dbHelper.addProduct(name, weight, price)
                loadProducts()
                productName.text.clear()
                productWeight.text.clear()
                productPrice.text.clear()
            }
        }

        loadProducts()
    }

    private fun loadProducts() {
        val cursor = dbHelper.getAllProducts()
        val products = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
            val weight = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_WEIGHT))
            val price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE))
            products.add("$name - $weight кг - $price руб")
        }
        cursor.close()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, products)
        productListView.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}