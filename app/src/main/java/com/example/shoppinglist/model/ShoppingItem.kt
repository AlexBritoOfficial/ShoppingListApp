package com.example.shoppinglist.model

data class ShoppingItem(
    val id: Int,
    var itemName:
    String,
    var quantity: Int,
    var isEditing: Boolean = false,
    var address: String = "")
