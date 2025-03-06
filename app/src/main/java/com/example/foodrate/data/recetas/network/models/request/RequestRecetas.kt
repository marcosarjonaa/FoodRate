package com.example.foodrate.data.recetas.network.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestRecetas(
    @SerializedName("idReceta")
    @Expose
    var idReceta: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("image")
    @Expose
    var image: String,

    @SerializedName("nota")
    @Expose
    var nota: String
){

}
