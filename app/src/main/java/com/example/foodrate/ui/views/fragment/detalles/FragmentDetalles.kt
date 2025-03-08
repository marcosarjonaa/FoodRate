package com.example.foodrate.ui.views.fragment.detalles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodrate.databinding.FragmentDetallesBinding
import com.example.foodrate.domain.models.recetas.RecetasData

class FragmentDetalles : Fragment() {
    private lateinit var binding: FragmentDetallesBinding
    private val args: FragmentDetallesArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idItem = args.idItem

        val recetas = RecetasDAO.recetasdao.getDataRecetas()[idItem]

        binding.nombre.text = recetas.name
        binding.description.text = recetas.description
        Glide.with(this)
            .load(recetas.image)
            .centerCrop()
            .into(binding.imagen)

    }


}