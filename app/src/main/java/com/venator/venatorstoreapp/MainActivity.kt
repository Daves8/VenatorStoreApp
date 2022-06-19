package com.venator.venatorstoreapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.android.material.navigation.NavigationBarView
import com.venator.venatorstoreapp.databinding.ActivityMainBinding
import com.venator.venatorstoreapp.utils.ApiLinks


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val queue = Volley.newRequestQueue(this)
        val url = ApiLinks.USER
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val user = jacksonObjectMapper().readValue<User>(
                    String(
                        response.toByteArray(Charsets.ISO_8859_1),
                        Charsets.UTF_8
                    )
                )
                binding.profileImage.setImageResource(user.imageId)
                binding.fullNameValue.text = user.fullName
                binding.nameValue.text = user.name
                binding.goldValue.text = user.gold.toString()
                binding.emailValue.text = user.email
                binding.phoneNumberValue.text = user.phoneNumber
            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

        binding.bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
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
}