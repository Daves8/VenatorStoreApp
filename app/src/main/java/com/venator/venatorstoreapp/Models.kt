package com.venator.venatorstoreapp

import java.util.*

data class User(
    val id: Int,
    val name: String,
    val fullName: String,
    val gold: Double,
    val imageId: Int,
    val email: String,
    val phoneNumber: String
)

data class Item(
    val id: Int,
    val dateAdded: Calendar,
    val imageId: Int,
    val title: String,
    val description: String,
    val cost: Double,
    val category: Category,
    val hidden: Boolean
)

data class Comment(
    val id: Int,
    val authorId: Int,
    val text: String,
    val dateAdded: Calendar
)

enum class Category(val localizedName: String) {
    House("Дом"),
    Shop("Лавка"),
    Horse("Лошадь"),
    Jacket("Куртка"),
    Pants("Штаны"),
    Boots("Сапоги"),
    Sword("Меч"),
    Bow("Лук"),
    Knife("Нож")
}