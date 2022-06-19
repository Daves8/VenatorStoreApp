package com.venator.venatorstoreapp.utils

class ApiLinks {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8000/api/venator/"

        const val GET_ALL_ITEMS = BASE_URL + "allItems/"
        const val CART = BASE_URL + "cart/"
        const val USER = BASE_URL + "user/"
        const val IS_CART_CONTAIN = BASE_URL + "isCartContain/"
        const val GET_ITEMS_IN_CART = BASE_URL + "getItemsInCart/"
        const val BUY_ALL = BASE_URL + "buyAll/"
    }
}