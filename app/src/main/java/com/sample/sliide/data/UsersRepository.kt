package com.sample.sliide.data

import android.annotation.SuppressLint
import com.sample.sliide.data.model.AddUserDTO
import com.sample.sliide.data.model.UserDTO
import com.sample.sliide.domain.User
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
class UsersRepository(private val usersAPI: UsersAPI = UsersAPI) {

    fun users(): Single<List<User>> = usersAPI.usersService.users().map { usersDTO ->
        usersDTO.data.map {
            it.mapToUser()
        }
    }

    fun addUser(name: String, email: String): Single<User> =
        usersAPI.usersService.addUser(AddUserDTO(name, email)).map { it.data.mapToUser() }

    fun removeUser(id: Int): Single<Int> = usersAPI.usersService.removeUser(id).toSingle { id }

    private fun UserDTO.mapToUser(): User {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

        return User(
            id = id,
            name = name,
            email = email,
            createdHoursAgo = getDateDiff(
                simpleDateFormat,
                created_at,
                simpleDateFormat.format(Calendar.getInstance().time)
            )
        )
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getDateDiff(format: SimpleDateFormat, oldDate: String, newDate: String): Long {
        return try {
            TimeUnit.HOURS.convert(
                format.parse(newDate).time - format.parse(oldDate).time,
                TimeUnit.MILLISECONDS
            )
        } catch (e: Exception) {
            0
        }
    }
}