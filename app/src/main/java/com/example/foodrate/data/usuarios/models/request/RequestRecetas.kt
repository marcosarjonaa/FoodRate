package com.example.foodrate.data.usuarios.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestRecetas(
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String?,
    var descripcion: String?,
    var nota: String?,
    var image: String?
){}
