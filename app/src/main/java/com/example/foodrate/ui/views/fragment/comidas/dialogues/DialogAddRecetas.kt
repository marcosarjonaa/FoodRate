package com.example.foodrate.ui.views.fragment.comidas.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.foodrate.R
import com.example.foodrate.databinding.DialogAddRecetasBinding
import com.example.foodrate.domain.models.recetas.Recetas

class DialogAddRecetas (
    private val onNewRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity);
            val inflater = myActivity.layoutInflater;
            val viewDialog = inflater.inflate(R.layout.dialog_add_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("Añadir una receta")
                .setPositiveButton("Crear",
                    DialogInterface.OnClickListener{
                        dialog, id ->
                        val receta = recoverDataLayout(viewDialog)
                        if(validacion(receta)){
                            onNewRecetasDialog(receta);
                            Toast.makeText(myActivity, "receta creada", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(myActivity, "Llena todos los campos", Toast.LENGTH_LONG).show()
                        }
                    }
                ).setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener{dialog, id ->
                        Toast.makeText(myActivity, "Has cerrado la ventana de añadir", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    ).create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun validacion(receta: Recetas): Boolean {
        return receta.name.isNotEmpty() &&
                receta.id.isNotEmpty() &&
                receta.descripcion.isNotEmpty() &&
                receta.nota.isNotEmpty() &&
                receta.image.isNotEmpty()
    }

    private fun recoverDataLayout(view: View): Recetas {
        val binding = DialogAddRecetasBinding.bind(view)
        return Recetas(
            binding.etName.text.toString(),
            binding.etId.text.toString(),
            binding.etDescripcion.text.toString(),
            binding.etNota.text.toString(),
            binding.etImage.text.toString()
        )
    }
}