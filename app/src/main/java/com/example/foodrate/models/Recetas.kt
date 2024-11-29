package com.example.foodrate.models

class Recetas(
    var name: String,
    var descripcion: String,
    var nota: String,
    var image: String
) {
    override fun toString(): String {
        return "Recetas(name='$name', descripcion'$descripcion',nota='$nota', image='$image')"
    }
}