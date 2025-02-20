package com.example.foodrate.domain.usercase

import com.example.foodrate.data.repository.RecetasRepository
import com.example.foodrate.domain.models.recetas.Recetas
import javax.inject.Inject

class GetRecetaByIdUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
) {
    suspend operator fun invoke(id: String): Recetas?{
        return recetasRepository.getRecetaById(id)
    }
}