package com.example.foodrate.controller

import android.content.Context
import android.util.Log;
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrate.MainActivity
import com.example.foodrate.adapter.AdapterRecetas
import com.example.foodrate.dao.DaoRecetas
import com.example.foodrate.dialogues.DialogAddRecetas
import com.example.foodrate.dialogues.DialogDeleteRecetas
import com.example.foodrate.dialogues.DialogEditRecetas
import com.example.foodrate.models.Recetas

class Controller (val context: Context) {
    lateinit var listRecetas : MutableList<Recetas>
    private lateinit var adapter: AdapterRecetas
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(context)

    init {
        initdata()
        setAdapter()
        logOut()
        initOnClickListener()
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
            { pos -> deleteRecetas(pos) },
            { pos -> updateRecetas(pos) }
        )
        myActivity.bindingMain.myRecyclerView.layoutManager = layoutManager
        myActivity.bindingMain.myRecyclerView.adapter = adapter
    }

    fun deleteRecetas(posicion : Int){
        val myActivity = context as MainActivity
        val dialogDelete = DialogDeleteRecetas(posicion){
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
        dialogDelete.show(myActivity.supportFragmentManager, "Borro una receta")
    }

    private fun initOnClickListener() {
        val myActivity = context as MainActivity;
        myActivity.bindingMain.btnAdd.setOnClickListener{
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

        val myActivity = context as MainActivity;
        myActivity.bindingMain.myRecyclerView.post {
            layoutManager.scrollToPositionWithOffset(pos, 20);
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
        myActivity.bindingMain.myRecyclerView.post{
            layoutManager.scrollToPositionWithOffset(listRecetas.size, 25)
        }
    }
}