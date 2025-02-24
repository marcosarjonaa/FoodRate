package com.example.foodrate.ui.views.fragment.comidas.dialogues

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
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
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.foodrate.R
import com.example.foodrate.databinding.DialogAddRecetasBinding
import com.example.foodrate.domain.models.recetas.Recetas
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DialogAddRecetas(
    private val onNewRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogAddRecetasBinding
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
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity)
            val inflater = myActivity.layoutInflater
            val viewDialog = inflater.inflate(R.layout.dialog_add_recetas, null)
            binding = DialogAddRecetasBinding.bind(viewDialog)

            binding.btnSelectImage.setOnClickListener { openGallery() }
            binding.btnTakePhoto.setOnClickListener { checkCameraPermission() }
            binding.btnSaveToGallery.setOnClickListener { saveImageToGallery() }

            builder.setView(viewDialog)
                .setMessage("Añadir una receta")
                .setPositiveButton("Crear") { _, _ ->
                    val receta = recoverDataLayout()
                    if (validacion(receta)) {
                        onNewRecetasDialog(receta)
                        Toast.makeText(myActivity, "Receta creada", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(myActivity, "Llena todos los campos", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    Toast.makeText(myActivity, "Has cerrado la ventana de añadir", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            builder.create()
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
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun recoverDataLayout(): Recetas {
        return Recetas(
            binding.etName.text.toString(),
            binding.etId.text.toString(),
            binding.etDescripcion.text.toString(),
            binding.etNota.text.toString(),
            imageUri?.toString() ?: ""
        )
    }

    private fun validacion(receta: Recetas): Boolean {
        return receta.name.isNotEmpty() &&
                receta.id.isNotEmpty() &&
                receta.descripcion.isNotEmpty() &&
                receta.nota.isNotEmpty() &&
                receta.image.isNotEmpty()
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }
}
