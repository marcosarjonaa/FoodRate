package com.example.foodrate.data.datasource.restaurantes

import com.example.foodrate.domain.models.restaurantes.Restaurantes

object RestaurantesLista {
    var ListaRestaurantes : List<Restaurantes> = listOf(
        Restaurantes(
            "Las tres culturas",
            "Lucena, c/Heredia, 2",
            "https://tresculturasrestaurante.com/wp-content/uploads/2020/05/cropped-a.jpg"
        ),
        Restaurantes(
            "El Valle",
            "Lucena, c/Federico Garcia Lorca",
            "https://lasubbetica.com/wp-content/uploads/2021/03/f1ecfd028cd2094237b449135fffbd58.jpg"
        ),
        Restaurantes(
            "Asador Manolo",
            "Lucena, Rda. Llano de las Tinajerías",
            "https://lasubbetica.com/wp-content/uploads/2021/03/2016-10-05.jpg"
        ),
        Restaurantes(
            "Japanish",
            "Lucena, Av. Santa Teresa, 14",
            "https://japanishtapas.com/wp-content/uploads/2023/06/foto-inicio-02.jpg"
        ),
        Restaurantes(
            "La Mafia",
            "Lucena, Rds. P.º Viejo, 7",
            "https://lamafia.es/lucena/wp-content/uploads/sites/72/2022/03/la-mafia-lucena-2-825x463.jpg"
        ),
        Restaurantes(
            "",
            "",
            ""
        )
    );
}