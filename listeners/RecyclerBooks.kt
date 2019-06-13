package com.example.bookreader.listeners

import com.example.bookreader.models.Book


interface RecyclerBooks {
    fun onClick(book : Book, position: Int)
}