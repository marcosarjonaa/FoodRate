package com.example.foodrate.domain.Usuarios.models

data class UsuarioData(
    var id: Int,
    var dni: String,
    var email: String,
    var password: String,
    var nombre: String,
    var token: String
){

}
