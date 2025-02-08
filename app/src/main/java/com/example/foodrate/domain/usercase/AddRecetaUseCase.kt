package com.example.foodrate.domain.usercase

import com.example.foodrate.data.repository.RecetasRepository
import com.example.foodrate.domain.models.recetas.Recetas
import javax.inject.Inject

class AddRecetaUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
) {
    suspend operator fun invoke(receta: Recetas): Recetas? {
        if (recetasRepository.existReceta(receta.id)) { //La receta ya existe
            return recetasRepository.addReceta(receta);
        } else
            return null
    }
}