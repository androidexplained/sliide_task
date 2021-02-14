package com.sample.sliide.data

import com.sample.sliide.data.model.AddUserDTO
import com.sample.sliide.data.model.AddUsersDTO
import com.sample.sliide.data.model.UsersDTO
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface UsersService {
    @GET("public-api/users")
    fun users(): Single<UsersDTO>

    @POST("public-api/users")
    fun addUser(@Body addUserDTO: AddUserDTO): Single<AddUsersDTO>

    @DELETE("public-api/users/{id}")
    fun removeUser(@Path(value = "id") id: Int): Completable
}