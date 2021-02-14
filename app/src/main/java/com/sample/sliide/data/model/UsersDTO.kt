package com.sample.sliide.data.model

data class UsersDTO(
    val code: Int,
    val meta: PageInfoDTO,
    val data: List<UserDTO>
)

data class AddUsersDTO(
    val code: Int,
    val meta: PageInfoDTO,
    val data: UserDTO
)