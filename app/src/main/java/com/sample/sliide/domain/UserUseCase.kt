package com.sample.sliide.domain

import com.sample.sliide.data.UsersRepository

class UserUseCase(private val usersRepository: UsersRepository = UsersRepository()) {

    fun users() = usersRepository.users()

    fun addUser(name: String, email: String) = usersRepository.addUser(name, email)

    fun removeUser(id: Int) = usersRepository.removeUser(id)
}