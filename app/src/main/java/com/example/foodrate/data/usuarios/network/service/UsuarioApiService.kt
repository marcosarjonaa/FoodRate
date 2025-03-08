package com.example.foodrate.data.usuarios.network.service

import android.util.Log
import com.example.foodrate.data.usuarios.models.request.RequestRegisterUsuario
import com.example.foodrate.data.usuarios.models.response.ResponseRegisterUsuario
import com.example.foodrate.data.usuarios.network.Interfaces.UsuariosApiServiceInterface
import com.example.foodrate.data.usuarios.network.models.request.RequestLoginUsuario
import com.example.foodrate.data.usuarios.network.models.response.ResponseLoginUsuario
import javax.inject.Inject

class UsuarioApiService @Inject constructor(
    private val apiService: UsuariosApiServiceInterface
) {
    suspend fun login(loginUsuario: RequestLoginUsuario): Result<ResponseLoginUsuario>{
        try {
            val respuesta = apiService.login(loginUsuario)
            if (respuesta.isSuccessful){
                respuesta.body()?.let {
                    body -> return Result.success(body)
                }?: return Result.failure(RuntimeException("Respues erronea en el logueo"))
            }else {
                //La estructura de la siguiente linea lo que hace es coger el body con el
                //error de respuesta, y en el caso de que no sea nula, la pasa a string y devuelve
                //y en el caso de que sea nula, pasa una cadena especifica
                val mensajeError = respuesta.errorBody()?.toString() ?: "Fallo al loguear"
                return Result.failure(RuntimeException(mensajeError))
            }
        } catch (e: Exception){
            return Result.failure(e)
        }
    }

        suspend fun register(registerUsuario: RequestRegisterUsuario): Result<ResponseRegisterUsuario>{
        try {
            val respuesta = apiService.register(registerUsuario)
            if (respuesta.isSuccessful){
                respuesta.body()?.let {
                    body -> return Result.success(body)
                } ?: return Result.failure(RuntimeException("Respuesta erronea del register"))
            }else {
                val mensajeError = respuesta.errorBody()?.toString() ?: "Fallo al registrar"
                return Result.failure(RuntimeException("$mensajeError, ${respuesta.code()}"))
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
    }
}