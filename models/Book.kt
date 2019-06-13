package com.example.bookreader.models

import java.io.Serializable

data class Book(val Id : Int, val Title : String, val Autor : String, val BookPortrait : String, val ImageHolder : String, val Description : String, val Content : String) : Serializable