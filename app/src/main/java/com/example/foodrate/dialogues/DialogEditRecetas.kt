package com.example.foodrate.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.foodrate.databinding.DialogEditRecetasBinding
import com.example.foodrate.models.Recetas
import com.example.foodrate.objects_models.ArgumentsRecetas

class DialogEditRecetas (
    val recetasToUpdate: Recetas,
    val updateRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    private val ARGUMENT_NAME : String = ArgumentsRecetas.ARGUMENT_NAME
    private val ARGUMENT_ID : String = ArgumentsRecetas.ARGUMENT_ID
    private val ARGUMENT_DESCRIPCION : String = ArgumentsRecetas.ARGUMENT_DESCRIPCION
    private val ARGUMENT_NOTA : String = ArgumentsRecetas.ARGUMENT_NOTA
    private val ARGUMENT_IMAGE : String = ArgumentsRecetas.ARGUMENT_IMAGE
    init{
        val args = Bundle().apply {
            putString(ARGUMENT_NAME, recetasToUpdate.name)
            putString(ARGUMENT_ID, recetasToUpdate.id)
            putString(ARGUMENT_DESCRIPCION, recetasToUpdate.descripcion)
            putString(ARGUMENT_NOTA, recetasToUpdate.nota)
            putString(ARGUMENT_IMAGE, recetasToUpdate.image)
        }
        this.arguments = args
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val inflater = requireActivity().layoutInflater
            val binding = DialogEditRecetasBinding.inflate(inflater)
            arguments?.let { args ->  // seteo los datos iniciales en los campos del dialogo
                binding.etName.setText(args.getString(ARGUMENT_NAME))
                binding.etId.setText(args.getString(ARGUMENT_ID))
                binding.etdescripcion.setText(args.getString(ARGUMENT_DESCRIPCION))
                binding.etNota.setText(args.getString(ARGUMENT_NOTA))
                binding.etImage.setText(args.getString(ARGUMENT_IMAGE))
            }
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(binding.root)
                .setTitle("Editar receta: ")
                .setPositiveButton("Aceptar") { dialog, id ->
                    val updateRecetas = recoverDataLayout(binding)

                    if (updateRecetas.name.isEmpty() ||
                        updateRecetas.id.isEmpty() ||
                        updateRecetas.descripcion.isEmpty() ||
                        updateRecetas.nota.isEmpty() ||
                        updateRecetas.image.isEmpty()
                    ) {
                        Toast.makeText(requireContext(), "Algún campo está vacío", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    } else {
                        updateRecetasDialog(updateRecetas)

                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun recoverDataLayout(binding: DialogEditRecetasBinding): Recetas {
        return Recetas(
            binding.etName.text.toString(),
            binding.etId.id.toString(),
            binding.etdescripcion.text.toString(),
            binding.etNota.text.toString(),
            binding.etImage.text.toString()
        )
    }
}