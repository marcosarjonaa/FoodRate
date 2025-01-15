package com.example.foodrate.interfaces

import com.example.foodrate.models.Restaurantes

interface InterfaceDaoRestaurantes {
    fun getDataRestaurantes(): List<Restaurantes>
}