package com.example.foodrate.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrate.databinding.ActivityCardviewBinding
import com.example.foodrate.models.Recetas

class ViewHRecetas (
    view: View,
    var deleteOnClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder (view){
    var binding: ActivityCardviewBinding

    init {
        binding = ActivityCardviewBinding.bind(view)
    }

    fun renderize(recetas: Recetas, position: Int){
        binding.txtNombre.setText(recetas.name)
        binding.txtDescripcion.setText(recetas.descripcion)
        binding.txtNota.setText(recetas.nota)
        Glide
            .with(itemView.context)
            .load(recetas.image)
            .centerCrop()
            .into(binding.ivRecetas)
        setOnClickListener(position)
    }

    private fun setOnClickListener(position : Int) {
        binding.ivDelete.setOnClickListener {
            deleteOnClick(position)
        }
    }
}