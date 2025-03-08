package com.example.foodrate.data.usuarios.network.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestLoginUsuario(
    @SerializedName("dni")
    @Expose
    val dni: String,

    @SerializedName("password")
    @Expose
    val password: String
)

