package com.example.foodrate.domain.usercase

import com.example.foodrate.data.recetas.repository.RecetasRepository
import javax.inject.Inject

class DeleteRecetaUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
){
    suspend operator fun invoke(id:Int): Boolean{
        return recetasRepository.delReceta(id);
    }
}