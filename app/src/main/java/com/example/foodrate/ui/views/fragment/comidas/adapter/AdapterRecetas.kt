package com.example.foodrate.ui.views.fragment.comidas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrate.R
import com.example.foodrate.domain.models.recetas.Recetas

class AdapterRecetas(
    var listRecetas: MutableList<Recetas>,
    var updateClick: (Int) -> Unit,
    var deleteClick: (Int) -> Unit,
    var itemClick: (Int) -> Unit
    ) : RecyclerView.Adapter<ViewHRecetas>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRecetas {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRecetas = R.layout.activity_cardview
            return ViewHRecetas(layoutInflater.inflate(layoutItemRecetas, parent, false), updateClick, deleteClick, itemClick)
        }

        override fun onBindViewHolder(holder: ViewHRecetas, position: Int) {
            holder.renderize(listRecetas[position], position)
        }

        override fun getItemCount(): Int = listRecetas.size


}