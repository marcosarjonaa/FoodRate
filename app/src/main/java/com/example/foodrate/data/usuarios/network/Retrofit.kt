package com.example.foodrate.data.usuarios.network

import com.example.foodrate.data.recetas.network.repository.RecetasApiServiceInterface
import com.example.foodrate.data.usuarios.network.Interfaces.UsuariosApiServiceInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Retrofit {
    private const val URL_BASE = "http://10.0.2.2:8081"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = retrofit2.Retrofit
        .Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    fun provideUsuarioApiServiceInterface(retrofit: Retrofit): UsuariosApiServiceInterface {
        return retrofit.create(UsuariosApiServiceInterface::class.java)
    }

    @Provides
    fun provideRecetasApiServiceInterface(retrofit: Retrofit): RecetasApiServiceInterface {
        return retrofit.create(RecetasApiServiceInterface::class.java)
    }
}