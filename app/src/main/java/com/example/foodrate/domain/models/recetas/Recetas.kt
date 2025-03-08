package com.example.foodrate.domain.models.recetas

data class Recetas(
    var id: Int,
    var idRecetas: Int,
    var name: String,
    var description: String,
    var nota: String,
    var image: String) {

    override fun toString(): String {
        return "Recetas(name='$name', id='$id',descripcion'$description',nota='$nota', image='$image')"
    }
}