package com.example.foodrate.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.foodrate.R

class DialogDeleteRecetas (
    val pos : Int,
    val onDeleteRecetasDialog: (Int) -> Unit
) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity)
            val inflater = myActivity.layoutInflater
            val viewDialog = inflater.inflate(R.layout.dialog_delete_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("¿Quieres eliminar la receta?")
                .setPositiveButton("Eliminar") { dialog, id ->
                    onDeleteRecetasDialog(pos)
                }
                .setNegativeButton("Cancelar") { dialog, id ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }
}