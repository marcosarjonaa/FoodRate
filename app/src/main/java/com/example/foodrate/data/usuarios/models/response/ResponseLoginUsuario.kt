package com.example.foodrate.data.usuarios.network.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseLoginUsuario(
    @SerializedName("id")
    @Expose
    val id: Int
 )
