package com.example.foodrate.ui.views.fragment.comidas

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.databinding.FragmentComidasBinding
import com.example.foodrate.domain.models.recetas.Recetas
import com.example.foodrate.ui.viewmodel.recetas.RecetasViewModel
import com.example.foodrate.ui.views.fragment.comidas.adapter.AdapterRecetas
import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogAddRecetas
import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogDeleteRecetas
import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogEditRecetas
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentComidas() : Fragment() {
    lateinit var binding: FragmentComidasBinding
    val recetasViewModel: RecetasViewModel by viewModels()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapterRecetas: AdapterRecetas


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComidasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(activity)
        binding.myRecyclerView.layoutManager=layoutManager
        setAdapter(mutableListOf())
        setObservers()
        recetasViewModel.showRecetas()
        buttonAddOnClickListener()
    }

    private fun setAdapter(listaRecetas: MutableList<Recetas>){
        adapterRecetas = AdapterRecetas(
            listaRecetas,
            {
                pos -> editarReceta(pos)
            },
            {
                pos -> eliminarReceta(pos)
            },
            {
                pos -> navegarADetalles(pos)
            }
        )
        binding.myRecyclerView.adapter = adapterRecetas
    }

    fun buttonAddOnClickListener() {
        binding.btnAdd.setOnClickListener {
            Toast.makeText(context, "Nueva Receta", Toast.LENGTH_SHORT).show()
            val dialogo = DialogAddRecetas {
                receta -> recetasViewModel.addReceta(receta)
            }
            dialogo.show(requireActivity().supportFragmentManager, "Nueva receta")
        }
    }

    fun editarReceta(posicion : Int) {
        if (posicion in adapterRecetas.listRecetas.indices){
            val receta = adapterRecetas.listRecetas[posicion]
            val dialogo = DialogEditRecetas(receta) { editarReceta ->
                recetasViewModel.updateRecetas(editarReceta.id, editarReceta)
            }
            dialogo.show(requireActivity().supportFragmentManager, "Hemos editado una receta")
        }else {
            Toast.makeText(context, "Posicion no existente", Toast.LENGTH_SHORT).show()
        }
    }

    fun eliminarReceta(posicion: Int){
        if (posicion in adapterRecetas.listRecetas.indices){
            val dialogo = DialogDeleteRecetas(posicion){
                recetasViewModel.deleteReceta(posicion.toString())
            }
            dialogo.show(requireActivity().supportFragmentManager, "Queremos eliminar")
        }else {
            Toast.makeText(context, "No se puede eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    fun navegarADetalles(posicion: Int){
        if (posicion in adapterRecetas.listRecetas.indices) {
            val recetaId = adapterRecetas.listRecetas[posicion].id
            findNavController().navigate(
                FragmentComidasDirections.actionFragmentComidasToFragmentDetalles(
                    idItem = posicion
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        recetasViewModel.recetasLiveData.observe(viewLifecycleOwner, Observer { receta->
            adapterRecetas.listRecetas = receta.toMutableList()
            adapterRecetas.notifyDataSetChanged()
        })

        recetasViewModel.addRecetaLiveData.observe(viewLifecycleOwner, Observer { nuevaReceta ->
            nuevaReceta?.let {
                adapterRecetas.listRecetas.add(it);
                adapterRecetas.notifyItemInserted(adapterRecetas.listRecetas.size-1)
                layoutManager.scrollToPosition(adapterRecetas.listRecetas.size-1)
            }
        })

        recetasViewModel.posicionDeleteRecetaLiveData.observe(viewLifecycleOwner, Observer { posicion ->
            if (posicion in adapterRecetas.listRecetas.indices){
                adapterRecetas.listRecetas.removeAt(posicion)
                adapterRecetas.notifyItemRemoved(posicion)
            }
        })

        recetasViewModel.posicionUpdateRecetaLiveData.observe(viewLifecycleOwner, Observer { posicion ->
            if (posicion in adapterRecetas.listRecetas.indices){
                val recetaEditada = recetasViewModel.recetasLiveData.value?.getOrNull(posicion)
                recetaEditada?.let {
                    adapterRecetas.listRecetas[posicion] = it
                    adapterRecetas.notifyItemChanged(posicion)
                }
            }
        })
    }


}