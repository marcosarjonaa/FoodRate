package com.example.foodrate.ui.views.fragment.detalles

import com.example.foodrate.data.recetas.objects_models.RecetasRepositorio
import com.example.foodrate.domain.interfaces.InterfaceDaoRecetas
import com.example.foodrate.domain.models.recetas.Recetas

class RecetasDAO private constructor(): InterfaceDaoRecetas {
    companion object {
        val recetasdao: RecetasDAO by lazy {
            RecetasDAO()
        }
    }

    override fun getDataRecetas(): List<Recetas> {
        return RecetasRepositorio.listaRecetas
    }
}