package com.example.foodrate.interfaces

import com.example.foodrate.models.Recetas

interface InterfaceDao {
    fun getDataRecetas(): List<Recetas>
}