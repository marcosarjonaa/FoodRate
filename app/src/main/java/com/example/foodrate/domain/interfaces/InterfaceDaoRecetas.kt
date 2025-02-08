package com.example.foodrate.domain.interfaces

import com.example.foodrate.domain.models.recetas.Recetas

interface InterfaceDaoRecetas {
    fun getDataRecetas(): List<Recetas>
}