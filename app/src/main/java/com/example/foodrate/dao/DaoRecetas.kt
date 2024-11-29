package com.example.foodrate.dao

import com.example.foodrate.interfaces.InterfaceDao
import com.example.foodrate.models.Recetas
import com.example.foodrate.objects_models.Repositorio

class DaoRecetas private constructor() : InterfaceDao {
    companion object{
        val myDao: DaoRecetas by lazy {
            DaoRecetas()
        }
    }

    override
    fun getDataRecetas(): List<Recetas>  = Repositorio.ListaRecetas
}