package com.example.foodrate.ui.views.fragment.Restaurantes.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrate.databinding.FragmentviewrestauranteBinding
import com.example.foodrate.domain.models.restaurantes.Restaurantes

class ViewRestaurantes ( view: View) : RecyclerView.ViewHolder(view) {
    var binding: FragmentviewrestauranteBinding
    init {
        binding = FragmentviewrestauranteBinding.bind(view)
    }
    fun renderize(restaurantes: Restaurantes) {
        binding.nombre.setText(restaurantes.nombre)
        binding.lugar .setText(restaurantes.lugar)
        Glide
            .with(itemView.context)
            .load(restaurantes.imagen)
            .centerCrop()
            .into(binding.imagen)


    }
}