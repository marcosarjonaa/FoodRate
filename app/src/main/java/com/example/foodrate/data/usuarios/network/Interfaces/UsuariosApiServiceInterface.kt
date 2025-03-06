package com.example.foodrate.data.usuarios.network.Interfaces

import com.example.foodrate.data.usuarios.models.request.RequestRegisterUsuario
import com.example.foodrate.data.usuarios.models.response.ResponseRegisterUsuario
import com.example.foodrate.data.usuarios.network.models.request.RequestLoginUsuario
import com.example.foodrate.data.usuarios.network.models.response.ResponseLoginUsuario
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuariosApiServiceInterface {

    @POST("auth")
    suspend fun login(@Body loginUsuario: RequestLoginUsuario): retrofit2.Response<ResponseLoginUsuario>

    @POST("register")
    suspend fun register(@Body registerUsuario: RequestRegisterUsuario): retrofit2.Response<ResponseRegisterUsuario>

}