package com.example.foodrate.ui.views.fragment.Restaurantes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrate.R
import com.example.foodrate.domain.models.restaurantes.Restaurantes

class AdapterRestaurantes (var listRestaurantes: MutableList<Restaurantes>) : RecyclerView.Adapter<ViewRestaurantes>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewRestaurantes {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRestaurantes = R.layout.fragmentviewrestaurante
            return ViewRestaurantes(layoutInflater.inflate(layoutItemRestaurantes, parent, false))
        }

        override fun getItemCount(): Int  = listRestaurantes.size

        override fun onBindViewHolder(holder: ViewRestaurantes, position: Int) {
            holder.renderize(listRestaurantes[position])
        }

    }