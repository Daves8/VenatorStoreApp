package com.venator.venatorstoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.navigation.NavigationBarView
import com.venator.venatorstoreapp.adapters.ItemsAdapter
import com.venator.venatorstoreapp.databinding.ActivityAllItemsBinding
import com.venator.venatorstoreapp.utils.ApiLinks
import java.util.*

class AllItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllItemsBinding
    private val items: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ItemsAdapter(layoutInflater) {
            val intent = Intent(this, ItemActivity::class.java)
            intent.putExtra("itemId", it.id.toString())
            startActivity(intent)
        }
        binding.items.adapter = adapter
        binding.items.layoutManager = LinearLayoutManager(this)
        adapter.submitList(items.toList())

        val queue = Volley.newRequestQueue(this)
        val url = ApiLinks.GET_ALL_ITEMS
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                items.addAll(jacksonObjectMapper().readValue<List<Item>>(String(response.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)))
                adapter.submitList(items.toList())
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

        binding.bottomNavigationView.selectedItemId = R.id.allItems_item
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account_item -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                R.id.cart_item -> {
                    val i = Intent(this, CartActivity::class.java)
                    startActivity(i)
                }
            }
            false
        })
    }
}