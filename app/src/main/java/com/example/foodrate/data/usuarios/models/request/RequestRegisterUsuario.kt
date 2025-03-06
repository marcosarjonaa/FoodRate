package com.example.foodrate.data.usuarios.models.request

import androidx.credentials.CreatePasswordRequest
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestRegisterUsuario(
    @SerializedName("dni")
    @Expose
    var dni: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("password")
    @Expose
    var password: String,

    @SerializedName("nombre")
    @Expose
    var nombre: String,

    @SerializedName("token")
    @Expose
    var token: String

)
