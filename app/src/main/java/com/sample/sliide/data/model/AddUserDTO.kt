package com.sample.sliide.data.model

data class AddUserDTO(
    val name: String,
    val email: String,
    val gender: String = "Female",
    val status: String = "Active",
)