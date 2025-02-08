package com.example.foodrate.domain.models.restaurantes

import com.example.foodrate.data.datasource.restaurantes.RestaurantesLista

class RestaurantesData {
    companion object{
        var listaRestaurantes:List<Restaurantes> = emptyList()
    }

    fun getListaRestaurantes(): List<Restaurantes>{
        return RestaurantesLista.ListaRestaurantes
    }
}