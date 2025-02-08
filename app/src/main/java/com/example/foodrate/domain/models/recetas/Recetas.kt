package com.example.foodrate.domain.models.recetas

data class Recetas(
    var name: String,
    var id: String,
    var descripcion: String,
    var nota: String,
    var image: String) {

    override fun toString(): String {
        return "Recetas(name='$name', id='$id',descripcion'$descripcion',nota='$nota', image='$image')"
    }
}