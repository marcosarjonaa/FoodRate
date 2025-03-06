package com.example.foodrate.data.recetas.network.repository

import com.example.foodrate.data.recetas.network.models.request.RequestRecetas
import com.example.foodrate.data.recetas.network.models.response.ResponseRecetas
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface RecetasApiServiceInterface {
    @GET("recetas")
    suspend fun getAllRecetas(@Header("Authorization") token: String): Response<List<ResponseRecetas>>

    @POST("recetas")
    suspend fun postRecetas(@Header("Authorization") token: String,
        @Body recetas: RequestRecetas)
    : Response<ResponseRecetas>

    @PATCH("recetas/{idReceta}")
    suspend fun patchRecetas(@Header("Authorization") token: String,
         @Path("idReceta") idReceta: Int,
         @Body receta: RequestRecetas)
    : Response<ResponseRecetas>

    @DELETE("recetas/{idReceta}")
    suspend fun deleteRecetas(@Header("Authorization") token: String,
          @Path("idReceta") idReceta: Int)
    : Response<Unit>
}