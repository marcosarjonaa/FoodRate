package com.example.foodrate.domain.usercase

import com.example.foodrate.data.recetas.repository.RecetasRepository
import com.example.foodrate.domain.models.recetas.Recetas
import javax.inject.Inject

class GetNativeRecetasUseCase @Inject constructor(
    val recetasRepository: RecetasRepository
) {
    suspend operator fun invoke(): MutableList<Recetas>{
        return recetasRepository.getNativeRecetas().toMutableList();
    }
}