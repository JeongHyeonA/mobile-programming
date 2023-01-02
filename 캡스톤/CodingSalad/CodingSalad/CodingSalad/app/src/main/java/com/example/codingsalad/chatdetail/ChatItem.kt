package com.example.codingsalad.chatdetail

data class ChatItem (
    val sellerID: String,
    val message: String,
) {
    constructor(): this("","")
}