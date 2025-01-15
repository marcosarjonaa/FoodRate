package com.example.foodrate.controller

import android.content.Context
import android.util.Log;
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.MainActivity
import com.example.foodrate.adapter.AdapterRecetas
import com.example.foodrate.dao.DaoRecetas
import com.example.foodrate.dialogues.DialogAddRecetas
import com.example.foodrate.dialogues.DialogDeleteRecetas
import com.example.foodrate.dialogues.DialogEditRecetas
import com.example.foodrate.FragmentComidas
import com.example.foodrate.FragmentComidasDirections
import com.example.foodrate.models.Recetas

class Controller (val contextoActivity: Context, val fragment: FragmentComidas) {
    private val context = fragment.requireContext();
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
    }
}