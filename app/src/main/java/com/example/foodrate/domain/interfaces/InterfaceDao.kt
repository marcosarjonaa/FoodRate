package com.example.foodrate.domain.interfaces

import com.example.foodrate.domain.models.recetas.Recetas

interface InterfaceDao {
    suspend fun getNativeRecetas(): List<Recetas>
    suspend fun addReceta(receta: Recetas): Recetas
    suspend fun updateReceta(id: String, receta: Recetas): Boolean
    suspend fun delReceta(id: String): Boolean //Devuelve true en caso de que elimine y falso en caso contrario
    suspend fun getRecetaById(id : String): Recetas? //Es nullable porque puede no encontrar
    suspend fun existReceta(position : String): Boolean //Boolean porque solo te pide encontrarla
    fun getDataRecetas(): List<Recetas>
}