package com.example.foodrate.ui.views.fragment.comidas.controller

import android.content.Context
import android.util.Log;
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.ui.views.activities.main.MainActivity
import com.example.foodrate.ui.views.fragment.comidas.adapter.AdapterRecetas

import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogAddRecetas
import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogDeleteRecetas
import com.example.foodrate.ui.views.fragment.comidas.dialogues.DialogEditRecetas
import com.example.foodrate.ui.views.fragment.comidas.FragmentComidas

import com.example.foodrate.domain.models.recetas.Recetas

class Controller (val contextoActivity: Context, val fragment: FragmentComidas) {
    //Lo comento tod o porque no sirve para nada

    /*private val context = fragment.requireContext();
    lateinit var listRecetas : MutableList<Recetas>
    private lateinit var adapter: AdapterRecetas
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(context)

    fun initdata(){
        listRecetas = DaoRecetas.myDao.getDataRecetas().toMutableList()
        setAdapter()
        initOnClickListener()
    }

    private fun navigateToDetails(pos: Int) {
        fragment.findNavController().navigate(
            FragmentComidasDirections.actionFragmentComidasToFragmentDetalles(
                idItem = Integer.parseInt(listRecetas[pos].id)
            )
        )
    }

    fun setAdapter(){
        adapter = AdapterRecetas(
            listRecetas,
            { pos -> deleteRecetas(pos) },
            { pos -> updateRecetas(pos) },
            { pos -> navigateToDetails(pos) }
    )
        fragment.binding.myRecyclerView.layoutManager = layoutManager
        fragment.binding.myRecyclerView.adapter = adapter
    }

    fun deleteRecetas(posicion : Int){
        val myActivity = context as MainActivity
        val dialogDelete = DialogDeleteRecetas(posicion){
            if(posicion in listRecetas.indices){
                listRecetas.removeAt(posicion)
                fragment.binding.myRecyclerView.adapter?.apply{
                    notifyItemRemoved(posicion)
                    notifyItemRangeChanged(posicion, listRecetas.size - posicion)
                }
                Toast.makeText(context, "Se eliminó la receta en la posicion $posicion", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "Índice fuera de rango: $posicion", Toast.LENGTH_LONG).show()
            }
        }
        dialogDelete.show(myActivity.supportFragmentManager, "Borro una receta")
    }

    private fun initOnClickListener() {
        fragment.binding.btnAdd.setOnClickListener{
            addReceta()
        }
    }

    fun updateRecetas(pos: Int) {
        val editDialog = DialogEditRecetas(listRecetas.get(pos)){
            editReceta -> okOnEditReceta(editReceta, pos)
        }
        val myActivity = context as MainActivity
        editDialog.show(myActivity.supportFragmentManager, "Edito una receta")
    }

    private fun okOnEditReceta(editRecetas: Recetas, pos: Int){
        listRecetas.removeAt(pos);
        adapter.notifyItemRemoved(pos);
        listRecetas.add(pos, editRecetas);
        adapter.notifyItemInserted(pos);

        fragment.binding.myRecyclerView.post {
            layoutManager.scrollToPositionWithOffset(pos, 20)
        }
    }

    fun addReceta() {
        Toast.makeText(context, "Añadimos una receta", Toast.LENGTH_LONG).show()
        val dialog = DialogAddRecetas { receta -> okOnNewReceta(receta)}
        val myActivity = context as MainActivity;
        dialog.show(myActivity.supportFragmentManager, "Añado una nueva receta");
    }

    private fun okOnNewReceta(receta: Recetas){
        Log.d("Controler", "Añadiendo receta: $receta");
        listRecetas.add(listRecetas.size, receta);
        adapter.notifyItemInserted(listRecetas.lastIndex)
        val myActivity = context as MainActivity;
        fragment.binding.myRecyclerView.post{
            layoutManager.scrollToPositionWithOffset(listRecetas.size, 25)
        }
    }*/
}