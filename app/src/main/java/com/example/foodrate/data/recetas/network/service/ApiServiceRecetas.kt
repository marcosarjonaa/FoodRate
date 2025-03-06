package com.example.foodrate.data.recetas.network.service

import com.example.foodrate.data.recetas.network.models.request.RequestRecetas
import com.example.foodrate.data.recetas.network.models.response.ResponseRecetas
import com.example.foodrate.data.recetas.network.repository.RecetasApiServiceInterface
import javax.inject.Inject

class ApiServiceRecetas @Inject constructor(
    private val recetasApiServiceInterface: RecetasApiServiceInterface
) {

    suspend fun getAllRecetas(token: String): Result<List<ResponseRecetas>> {
        try {
            val respuesta= recetasApiServiceInterface.getAllRecetas("Bearer $token")
            if (respuesta.isSuccessful) {
                return Result.success(respuesta.body() ?: emptyList())
            } else {
                return Result.failure(RuntimeException(respuesta.errorBody()?.string() ?: "Fallo"))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

    suspend fun postRecetas(token: String, requestRecetas: RequestRecetas): Result<ResponseRecetas>{
        try {
            val respuesta = recetasApiServiceInterface.postRecetas("Bearer $token", requestRecetas)
            if(respuesta.isSuccessful) {
                return respuesta.body()?.let {
                    Result.success(it)
                } ?: Result.failure(RuntimeException("Falta cuerpo"))
            }else{
                return Result.failure(RuntimeException(respuesta.errorBody()?.string() ?: "Error creando receta"))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

    suspend fun patchRecetas(token: String, idReceta: Int, requestRecetas: RequestRecetas) : Result<ResponseRecetas>{
        try {
            val respuesta = recetasApiServiceInterface.patchRecetas("Bearer $token", idReceta, requestRecetas)
            if (respuesta.isSuccessful){
                respuesta.body()?.let {
                    return Result.success(it)
                } ?: return Result.failure(RuntimeException("Falta cuerpo"))
            } else {
                return Result.failure(RuntimeException(respuesta.errorBody()?.string() ?: "Fallo al actualizar"))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

    suspend fun deleteRecetas(token: String, idReceta: Int): Result<Unit> {
        try {
            val respuesta = recetasApiServiceInterface.deleteRecetas("Bearer $token", idReceta)
            if (respuesta.isSuccessful) {
                return Result.success(Unit)
            } else {
                return Result.failure(RuntimeException(respuesta.errorBody()?.string() ?: "Fallo eliminando"))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }
}