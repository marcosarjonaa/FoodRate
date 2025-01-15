package com.example.foodrate.dao

import com.example.foodrate.interfaces.InterfaceDaoRestaurantes
import com.example.foodrate.models.Restaurantes
import com.example.foodrate.objects_models.RestaurantesLista

class DaoRestaurantes private constructor(): InterfaceDaoRestaurantes {
        companion object {
            val myDao: DaoRestaurantes by lazy {
                DaoRestaurantes()
            }
        }

        override fun getDataRestaurantes(): List<Restaurantes> = RestaurantesLista.ListaRestaurantes
    }