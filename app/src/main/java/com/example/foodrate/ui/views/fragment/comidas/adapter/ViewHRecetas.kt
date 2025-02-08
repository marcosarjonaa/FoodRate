package com.example.foodrate.ui.views.fragment.comidas.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrate.databinding.ActivityCardviewBinding
import com.example.foodrate.domain.models.recetas.Recetas

class ViewHRecetas (
    view: View,
    var deleteOnClick: (Int) -> Unit,
    var updateOnClick: (Int) -> Unit,
    var itemClick: (Int) -> Unit
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
        itemView.setOnClickListener{
            itemClick(position)
        }
        binding.ivDelete.setOnClickListener {
            deleteOnClick(position)
        }
        binding.ivEdit.setOnClickListener {
            updateOnClick(position)
        }
    }
}