package com.example.foodrate.controller

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.MainActivity
import com.example.foodrate.adapter.AdapterRecetas
import com.example.foodrate.dao.DaoRecetas
import com.example.foodrate.models.Recetas

class Controller (val context: Context) {
    lateinit var listRecetas : MutableList<Recetas>
    private lateinit var adapter: AdapterRecetas
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(context)

    init {
        initdata()
        setAdapter()
        logOut()
    }

    fun initdata(){
        listRecetas = DaoRecetas.myDao.getDataRecetas().toMutableList()
    }

    fun logOut(){
        Toast.makeText(context, "He mostrado los datos", Toast.LENGTH_LONG).show()
        listRecetas.forEach{
            println(it)
        }
    }

    fun setAdapter(){
        val myActivity = context as MainActivity
        adapter = AdapterRecetas(
            listRecetas,
            { pos -> deleteRecetas(pos) }
        )
        myActivity.bindingMain.myRecyclerView.layoutManager = layoutManager
        myActivity.bindingMain.myRecyclerView.adapter = adapter
    }

    fun deleteRecetas(posicion : Int){
        val myActivity = context as MainActivity
        if(posicion in listRecetas.indices){
            listRecetas.removeAt(posicion)
            myActivity.bindingMain.myRecyclerView.adapter?.apply{
                notifyItemRemoved(posicion)
                notifyItemRangeChanged(posicion, listRecetas.size - posicion)
            }
            Toast.makeText(context, "Se eliminó el monumento en la posicion $posicion", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(context, "Índice fuera de ranfo: $posicion", Toast.LENGTH_LONG).show()
        }
    }
}