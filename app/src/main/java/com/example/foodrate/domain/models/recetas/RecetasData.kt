package com.example.foodrate.domain.models.recetas

import com.example.foodrate.domain.interfaces.InterfaceDao
import com.example.foodrate.domain.interfaces.InterfaceDaoRecetas

class RecetasData private constructor(){
    var recetas : MutableList<Recetas> = mutableListOf()

    fun getLastPos() = recetas.size -1

    fun getSize() = recetas.size

    companion object{
        val listaRecetas : RecetasData by lazy {
            RecetasData()
        }
    }
}