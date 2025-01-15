package com.example.foodrate.models

class Restaurantes(
    var nombre: String,
    var lugar: String,
    var imagen: String
) {
    override fun toString(): String {
        return "Restaurantes(name='$nombre', lugar'$lugar', image='$imagen')"
    }
}