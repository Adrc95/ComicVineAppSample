package com.adrc95.domain.model

data class Character(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val longDescription: String?,
    val uri : String,
    val thumbnail : String,
    val favorite : Boolean
    //val urls : List<String>
)
