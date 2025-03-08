package com.example.foodrate.domain.usercase

import com.example.foodrate.data.recetas.repository.RecetasRepository
import com.example.foodrate.domain.models.recetas.Recetas
import javax.inject.Inject

class UpdateRecetaUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
) {
    suspend operator fun invoke(id: Int, receta: Recetas): Boolean{
        if (recetasRepository.existReceta(id)){
            return recetasRepository.updateReceta(id, receta);
        }
        else
            return false
    }
}