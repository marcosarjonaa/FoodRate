package com.example.foodrate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrate.R
import com.example.foodrate.models.Recetas

class AdapterRecetas(
    var listRecetas: MutableList<Recetas>,
    var deleteClick: (Int) -> Unit,
    ) : RecyclerView.Adapter<ViewHRecetas>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRecetas {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRecetas = R.layout.activity_cardview
            return ViewHRecetas(layoutInflater.inflate(layoutItemRecetas, parent, false), deleteClick)
        }

        override fun onBindViewHolder(holder: ViewHRecetas, position: Int) {
            holder.renderize(listRecetas[position], position)
        }

        override fun getItemCount(): Int = listRecetas.size


}