package com.example.foodrate.domain.interfaces

import com.example.foodrate.domain.models.restaurantes.Restaurantes

interface InterfaceDaoRestaurantes {
    fun getDataRestaurantes(): List<Restaurantes>
}