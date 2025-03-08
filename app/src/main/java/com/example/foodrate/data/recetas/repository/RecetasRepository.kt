package com.example.foodrate.data.recetas.repository

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.annotation.IntegerRes
import com.example.foodrate.data.recetas.network.models.request.RequestRecetas
import com.example.foodrate.data.recetas.network.repository.RecetasApiServiceInterface
import com.example.foodrate.data.recetas.network.service.ApiServiceRecetas
import com.example.foodrate.domain.Usuarios.models.Usuario
import com.example.foodrate.domain.interfaces.InterfaceDao
import com.example.foodrate.domain.models.recetas.Recetas
import com.example.foodrate.domain.models.recetas.RecetasData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecetasRepository @Inject constructor(
    val apiServiceRecetas: ApiServiceRecetas
): InterfaceDao{

    @SuppressLint("SuspiciousIdentation")
    override suspend fun getNativeRecetas(): List<Recetas> {
        try {
            val respuesta = apiServiceRecetas.getAllRecetas(Usuario.token.toString())
            return respuesta.getOrNull()?.map {
                responseRecetas -> Recetas(
                    id = responseRecetas.idReceta,
                    idRecetas = responseRecetas.idReceta,
                    name = responseRecetas.name,
                    description = responseRecetas.description,
                    nota = responseRecetas.nota,
                    image = responseRecetas.image
                )
            } ?: return emptyList()
        }catch (e: Exception){
            return emptyList()
        }
    }

    override suspend fun addReceta(receta: Recetas): Recetas? {
        try {
            val requestRecetas = RequestRecetas(
                idReceta = receta.idRecetas,
                name = receta.name,
                description = receta.description,
                nota = receta.nota,
                image = receta.image
            )

            val respuesta = apiServiceRecetas.postRecetas(Usuario.token.toString(), requestRecetas)

            if (respuesta.isSuccess){
                respuesta.getOrNull()?.let { responseRecetas ->
                    val newReceta = Recetas(
                        id = responseRecetas.idReceta,
                        idRecetas = responseRecetas.idReceta,
                        name = responseRecetas.name,
                        description = responseRecetas.description,
                        nota = responseRecetas.nota,
                        image =  responseRecetas.image
                    )

                    RecetasData.listaRecetas.recetas.add(newReceta)
                    return receta
                }
            }else {
                return null
            }
            return null
        } catch (e: Exception){
            return null
        }
    }

    override suspend fun updateReceta(id: Int, receta: Recetas): Boolean {
        try {
            val requestRecetas = RequestRecetas(
                idReceta = receta.idRecetas,
                name = receta.name,
                description = receta.description,
                nota = receta.nota,
                image = receta.image
            )
            var recetaId = getNativeRecetas()[id].idRecetas
            val respuesta = apiServiceRecetas.patchRecetas(Usuario.token.toString(), recetaId, requestRecetas)

            if (respuesta.isSuccess){
                respuesta.getOrNull()?.let { responseRecetas ->
                    Recetas(
                        id = responseRecetas.idReceta,
                        idRecetas = responseRecetas.idReceta,
                        name = responseRecetas.name,
                        description = responseRecetas.description,
                        nota = responseRecetas.nota,
                        image =  responseRecetas.image
                    )

                    val receta = getNativeRecetas().toMutableList()
                    if (id in receta.indices){
                        receta[id].idRecetas
                        return true
                    } else {
                        return false
                    }
                } ?: return false
            } else {
                Log.e("Error Receta Actualizar ", "fallo en api")
                return false
            }
        }catch (e: Exception){
            Log.e("Error Receta Actualizar", "Excepcion recetas actualizar")
            return false
        }
    }

    override suspend fun delReceta(id: Int): Boolean {
        try {
            var idRecetas = getNativeRecetas()[id].idRecetas
            val respuesta = apiServiceRecetas.deleteRecetas(Usuario.token.toString(), idRecetas)
            if (respuesta.isSuccess){
                return true
            } else {
                return false
            }
        }catch (e: Exception){
            return false
        }
    }

    override suspend fun getRecetaById(id: Int): Recetas? {
        return RecetasData.listaRecetas.recetas.firstOrNull { r -> r.id == id }
    }

    override suspend fun existReceta(id: Int): Boolean {
        return RecetasData.listaRecetas.recetas.any { r -> r.id == id }
    }

    override fun getDataRecetas(): List<Recetas> = RecetasData.listaRecetas.recetas
}