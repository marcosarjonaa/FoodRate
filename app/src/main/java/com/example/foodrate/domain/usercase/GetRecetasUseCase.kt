package com.example.foodrate.domain.usercase

import com.example.foodrate.data.repository.RecetasRepository
import com.example.foodrate.domain.models.recetas.Recetas
import javax.inject.Inject

class GetRecetasUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
) {
    suspend operator fun invoke(): MutableList<Recetas>{
        return recetasRepository.getDataRecetas().toMutableList()
    }
}