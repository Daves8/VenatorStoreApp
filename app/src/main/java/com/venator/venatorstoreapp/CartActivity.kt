package com.venator.venatorstoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.navigation.NavigationBarView
import com.venator.venatorstoreapp.adapters.ItemsAdapter
import com.venator.venatorstoreapp.adapters.ItemsInCartAdapter
import com.venator.venatorstoreapp.databinding.ActivityCartBinding
import com.venator.venatorstoreapp.databinding.ActivityMainBinding
import com.venator.venatorstoreapp.dialogs.CartDialog
import com.venator.venatorstoreapp.utils.ApiLinks

class CartActivity : AppCompatActivity(), CartDialog.CartDialogListener {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ItemsInCartAdapter
    private val itemsInCart: MutableList<Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buyBtn.setOnClickListener {
            val dialog = CartDialog(
                resources.getString(R.string.cart_dialogue1) + " " + itemsInCart.size.toString() + " " + resources.getString(
                    R.string.cart_dialogue2
                ) + " " + getSumPrice().toString() + " зол."
            )
            dialog.show(supportFragmentManager, "cartDialog")

        }

        adapter = ItemsInCartAdapter(layoutInflater) {
            // запрос на удаление
            val queueDelete = Volley.newRequestQueue(this)
            val urlDelete = ApiLinks.CART + it.id
            val stringRequestDelete = StringRequest(
                Request.Method.DELETE, urlDelete,
                { response ->
                    itemsInCart.remove(it)
                    adapter.submitList(itemsInCart.toList())
                    binding.buyBtn.isEnabled = itemsInCart.size != 0
                    binding.summary1.text =
                        resources.getString(R.string.cart_summary1) + " " + itemsInCart.size.toString()
                    binding.summary2.text =
                        resources.getString(R.string.cart_summary2) + " " + getSumPrice().toString() + " зол."
                },
                {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                })
            queueDelete.add(stringRequestDelete)
        }
        binding.itemsInCart.adapter = adapter
        binding.itemsInCart.layoutManager = LinearLayoutManager(this)
        adapter.submitList(itemsInCart.toList())

        val queue = Volley.newRequestQueue(this)
        val url = ApiLinks.GET_ITEMS_IN_CART
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                itemsInCart.addAll(
                    jacksonObjectMapper().readValue<List<Item>>(
                        String(
                            response.toByteArray(
                                Charsets.ISO_8859_1
                            ), Charsets.UTF_8
                        )
                    )
                )
                binding.buyBtn.isEnabled = itemsInCart.size != 0
                binding.summary1.text =
                    resources.getString(R.string.cart_summary1) + " " + itemsInCart.size.toString()
                binding.summary2.text =
                    resources.getString(R.string.cart_summary2) + " " + getSumPrice().toString() + " зол."
                adapter.submitList(itemsInCart.toList())
            },
            {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)


        binding.bottomNavigationView.selectedItemId = R.id.cart_item
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.allItems_item -> {
                    val i = Intent(this, AllItemsActivity::class.java)
                    startActivity(i)
                }
                R.id.account_item -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
            }
            false
        })
    }

    private fun getSumPrice(): Double {
        var sum: Double = 0.0
        for (item in itemsInCart) {
            sum += item.cost
        }
        return sum
    }

    override fun onDialogResult(result: Boolean) {
        if (result) {
            val queue = Volley.newRequestQueue(this)
            val url = ApiLinks.BUY_ALL
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    if (response.toBoolean()) {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.cart_confirmTrue),
                            Toast.LENGTH_LONG
                        ).show()
                        val i = Intent(this, AllItemsActivity::class.java)
                        startActivity(i)
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.cart_confirmFalse),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                })
            queue.add(stringRequest)
        }
    }
}