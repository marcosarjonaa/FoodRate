package com.example.foodrate.data.usuarios.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseRegisterUsuario(
    @SerializedName("mensaje")
    @Expose
    var mensaje: String
){}
