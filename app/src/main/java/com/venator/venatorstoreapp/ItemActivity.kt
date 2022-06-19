package com.venator.venatorstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.navigation.NavigationBarView
import com.venator.venatorstoreapp.databinding.ActivityItemBinding
import com.venator.venatorstoreapp.utils.ApiLinks
import org.json.JSONObject
import java.util.HashMap

class ItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBinding

    //private val comments: MutableList<Comment> = mutableListOf()
    var id: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("itemId") ?: "-1"


        val queue = Volley.newRequestQueue(this)
        val url = ApiLinks.GET_ALL_ITEMS + id
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val item = jacksonObjectMapper().readValue<Item>(
                    String(
                        response.toByteArray(Charsets.ISO_8859_1),
                        Charsets.UTF_8
                    )
                )

                binding.itemTitle.text = item.title
                binding.itemDescription.text = item.description
                binding.itemImage.setImageResource(item.imageId)
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

        val queueInitBtnImg = Volley.newRequestQueue(this)
        val urlInitBtnImg = ApiLinks.IS_CART_CONTAIN + id
        val stringRequestInitBtnImg = StringRequest(
            Request.Method.GET, urlInitBtnImg,
            { response ->
                if (response.toBoolean()) {
                    binding.buy.setImageResource(R.drawable.ic_cart_remove)
                } else {
                    binding.buy.setImageResource(R.drawable.ic_cart_add)
                }
            },
            {
                Toast.makeText(this, "Error - is cart contain", Toast.LENGTH_LONG).show()
            })
        queueInitBtnImg.add(stringRequestInitBtnImg)
        binding.buy.setOnClickListener {
            makeRequest()
        }

        binding.bottomNavigationView.selectedItemId = R.id.allItems_item
        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account_item -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                R.id.allItems_item -> {
                    val i = Intent(this, AllItemsActivity::class.java)
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

    private fun makeRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = ApiLinks.IS_CART_CONTAIN + id
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                if (response.toBoolean()) {
                    // запрос на удаление
                    val queueDelete = Volley.newRequestQueue(this)
                    val urlDelete = ApiLinks.CART + id
                    val stringRequestDelete = StringRequest(
                        Request.Method.DELETE, urlDelete,
                        { response ->
                            binding.buy.setImageResource(R.drawable.ic_cart_add)
                        },
                        {
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        })
                    queueDelete.add(stringRequestDelete)
                } else {
                    // запрос на добавление
                    val queueAdd = Volley.newRequestQueue(this)
                    val urlAdd = ApiLinks.CART
                    val parAdd = JSONObject()
                    parAdd.put("id", id)
                    val stringRequestAdd =
                        JsonObjectRequest(Request.Method.POST, urlAdd, parAdd,
                            {
                                binding.buy.setImageResource(R.drawable.ic_cart_remove)
                            }, {
                                binding.itemDescription.text = it.message
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            })
                    queueAdd.add(stringRequestAdd)
                }
            },
            {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }
}