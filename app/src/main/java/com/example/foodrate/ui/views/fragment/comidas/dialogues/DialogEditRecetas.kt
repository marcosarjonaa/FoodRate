package com.example.foodrate.ui.views.fragment.comidas.dialogues

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.foodrate.databinding.DialogEditRecetasBinding
import com.example.foodrate.domain.models.recetas.ArgumentRecetas
import com.example.foodrate.domain.models.recetas.Recetas
import com.example.foodrate.domain.models.restaurantes.ArgumentRestaurantes
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DialogEditRecetas(
    private val recetasToUpdate: Recetas,
    private val updateRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    private lateinit var  inicioActividadCamara: ActivityResultLauncher<Intent>
    private lateinit var binding: DialogEditRecetasBinding
    private var imageUri: Uri? = null
    private var currentPhotoPath: String? = null

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 100
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            binding.etImage.setImageBitmap(bitmap)
            imageUri = Uri.fromFile(File(currentPhotoPath))
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            binding.etImage.setImageURI(it)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogEditRecetasBinding.inflate(it.layoutInflater)
            inicioActividadCamara = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    binding.etImage.setImageBitmap(bitmap)
                    imageUri = Uri.fromFile(File(currentPhotoPath))
                }
            }


            binding.btnSelectImage.setOnClickListener { openGallery() }
            binding.btnTakePhoto.setOnClickListener { checkCameraPermission() }
            binding.btnSaveToGallery.setOnClickListener { saveImageToGallery() }

            arguments?.let { args ->
                binding.etName.setText(args.getString(ArgumentRecetas.ARGUMENT_NAME))
                binding.etId.setText(args.getString(ArgumentRecetas.ARGUMENT_ID))
                binding.etdescripcion.setText(args.getString(ArgumentRecetas.ARGUMENT_DESCRIPTION))
                binding.etNota.setText(args.getString(ArgumentRecetas.ARGUMENT_NOTA))
                val imageUriString = args.getString(ArgumentRecetas.ARGUMENT_IMAGE)
                if (!imageUriString.isNullOrEmpty()) {
                    binding.etImage.setImageURI(Uri.parse(imageUriString))
                    imageUri = Uri.parse(imageUriString)
                }
            }

            AlertDialog.Builder(requireContext())
                .setView(binding.root)
                .setTitle("Editar receta")
                .setPositiveButton("Aceptar") { _, _ ->
                    val updatedReceta = recoverDataLayout()
                    if (updatedReceta.name.isEmpty() || updatedReceta.id==0 ||
                        updatedReceta.description.isEmpty() || updatedReceta.nota.isEmpty()) {
                        Toast.makeText(requireContext(), "Algún campo está vacío", Toast.LENGTH_LONG).show()
                    } else {
                        updateRecetasDialog(updatedReceta)
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intentCamera)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun saveImageToGallery() {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "No hay imagen para guardar", Toast.LENGTH_SHORT).show()
            return
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "Receta_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/FoodRate")
        }

        val contentResolver = requireContext().contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                val outputStream = contentResolver.openOutputStream(it)
                val bitmap = getBitmapFromUri(imageUri!!)
                if (outputStream != null) {
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                outputStream?.close()
                Toast.makeText(requireContext(), "Imagen guardada en galería", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al guardar la imagen", Toast.LENGTH_LONG).show()
            }
        } ?: Toast.makeText(requireContext(), "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show()
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                //Se pone lo de arriba para que no se preocupe de que las funciones esten desfasadas
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun recoverDataLayout(): Recetas {
        return Recetas(
            Integer.parseInt(binding.etId.text.toString()),
            Integer.parseInt(binding.etId.text.toString()),
            binding.etName.text.toString(),
            binding.etdescripcion.text.toString(),
            binding.etNota.text.toString(),
            imageUri?.toString() ?: ""
        )
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }
}
