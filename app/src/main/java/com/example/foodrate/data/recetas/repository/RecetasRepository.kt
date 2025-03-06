package com.example.foodrate.data.recetas.repository

import androidx.room.util.copy
import com.example.foodrate.data.recetas.datasource.recetas.Repositorio
import com.example.foodrate.domain.interfaces.InterfaceDao
import com.example.foodrate.domain.models.recetas.Recetas
import com.example.foodrate.domain.models.recetas.RecetasData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecetasRepository @Inject constructor(): InterfaceDao{
    
    override suspend fun getNativeRecetas(): List<Recetas> = Repositorio.ListaRecetas

    override suspend fun addReceta(receta: Recetas): Recetas {
        RecetasData.listaRecetas.recetas.add(receta)
        return receta
    }

    override suspend fun updateReceta(id: String, receta: Recetas): Boolean {
        return if (Integer.parseInt(id) < RecetasData.listaRecetas.recetas.size){
            RecetasData.listaRecetas.recetas[Integer.parseInt(id)] = RecetasData.listaRecetas.recetas.get(Integer.parseInt(id)).copy(
                id = receta.id,
                name = receta.name,
                descripcion = receta.descripcion,
                nota = receta.nota,
                image = receta.image
            )
            true
        }
        else
            false
    }

    override suspend fun delReceta(id: String): Boolean {
        return if(Integer.parseInt(id) < RecetasData.listaRecetas.recetas.size){
            RecetasData.listaRecetas.recetas.removeAt(Integer.parseInt(id))
            true
        }
        else
            false
    }

    override suspend fun getRecetaById(id: String): Recetas? {
        return RecetasData.listaRecetas.recetas.firstOrNull { r -> r.id == id }
    }

    override suspend fun existReceta(id: String): Boolean {
        return RecetasData.listaRecetas.recetas.any { r -> r.id == id }
    }

    override fun getDataRecetas(): List<Recetas> = RecetasData.listaRecetas.recetas
}