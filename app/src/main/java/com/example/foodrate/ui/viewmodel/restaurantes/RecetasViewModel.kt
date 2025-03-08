package com.example.foodrate.ui.viewmodel.recetas

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope;

import com.example.foodrate.domain.models.recetas.Recetas
import com.example.foodrate.domain.models.recetas.RecetasData
import com.example.foodrate.domain.usercase.AddRecetaUseCase
import com.example.foodrate.domain.usercase.DeleteRecetaUseCase
import com.example.foodrate.domain.usercase.GetNativeRecetasUseCase
import com.example.foodrate.domain.usercase.GetRecetaByIdUseCase
import com.example.foodrate.domain.usercase.GetRecetasUseCase
import com.example.foodrate.domain.usercase.UpdateRecetaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecetasViewModel @Inject constructor(
    val addRecetaUseCase: AddRecetaUseCase,
    val updateRecetaUseCase: UpdateRecetaUseCase,
    val deleteRecetaUseCase: DeleteRecetaUseCase,
    val getNativeRecetasUseCase: GetNativeRecetasUseCase,
    val getRecetaByIdUseCase: GetRecetaByIdUseCase,
    val getRecetasUseCase: GetRecetasUseCase
    ): ViewModel(){
    val recetasLiveData = MutableLiveData<List<Recetas>>();
    val addRecetaLiveData = MutableLiveData<Recetas>();
    val posicionUpdateRecetaLiveData = MutableLiveData<Int>();
    val posicionDeleteRecetaLiveData= MutableLiveData<Int>();
    val detallesRecetasLiveData = MutableLiveData<Recetas>();

    fun showRecetas() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = if(RecetasData.listaRecetas.recetas.isEmpty()){
                getNativeRecetasUseCase()
            }else {
                getRecetasUseCase()
            }

            data?.let {
                recetasLiveData.postValue(it as List<Recetas>?)
                RecetasData.listaRecetas.recetas = it
            }
        }
    }

    fun addReceta(receta: Recetas){
        viewModelScope.launch(Dispatchers.IO) {
            val nuevaReceta = addRecetaUseCase(receta)
            nuevaReceta?.let {
                showRecetas()
                addRecetaLiveData.postValue(receta)
            }
        }
    }

    fun updateRecetas(id: Int, receta: Recetas) {
        viewModelScope.launch(Dispatchers.IO) {
            val editada = updateRecetaUseCase(id, receta)
            editada?.let {
                val listaEditada = recetasLiveData.value?.toMutableList() ?: mutableListOf()
                val index = listaEditada.indexOfFirst { it.id== id }
                if (index!=-1){
                    listaEditada[id] = receta
                    recetasLiveData.postValue(listaEditada)
                }
            }
        }
    }

    fun deleteReceta(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteRecetaUseCase(id)
            result?.let {
                posicionDeleteRecetaLiveData.postValue(id)
            }
        }
    }
}