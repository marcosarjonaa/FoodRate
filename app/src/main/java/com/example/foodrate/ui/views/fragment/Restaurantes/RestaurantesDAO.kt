package com.example.foodrate.ui.views.fragment.Restaurantes

import com.example.foodrate.data.recetas.objects_models.ListaRestaurantes
import com.example.foodrate.domain.interfaces.InterfaceDaoRestaurantes
import com.example.foodrate.domain.models.restaurantes.Restaurantes

class RestaurantesDAO private constructor(): InterfaceDaoRestaurantes {

    companion object {
        val myDao: RestaurantesDAO by lazy {
            RestaurantesDAO()
        }
    }

    override fun getDataRestaurantes(): List<Restaurantes> {
        return ListaRestaurantes().listaRestaurantes
    }
}