package com.lgcns.crossdev.onboarding1.presentation.dialog

import android.Manifest
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.lgcns.crossdev.onboarding1.presentation.databinding.DialogPicAddBinding
import java.io.File
interface PicAddDialogListener {
    fun onPictureTake(uri: Uri, file: File)
    fun onGalleryGet(uri: Uri)
}

class PicAddDialog(
    private var picAddDialogListener: PicAddDialogListener
): DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogPicAddBinding? = null
    private val binding get() = _binding!!
    private var picUri: Uri? = null
    private var photoFile: File? = null
    private lateinit var cameraPermission: ActivityResultLauncher<String> // Camera Permission
    private lateinit var storagePermission: ActivityResultLauncher<Array<String>> // Storage Permission
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri> // Call Camera Application
    private lateinit var galleryLauncher: ActivityResultLauncher<String> // Call Gallery Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPicAddBinding.inflate(inflater, container, false)
        val view = binding.root

        init()
        setClickListener()

        return view
    }

    override fun onResume() {
        super.onResume()
        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun init() {
        storagePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { permission -> permission.value }) {
                openGallery()
            } else {
                Toast.makeText(binding.root.context, "Permission must be approved", Toast.LENGTH_SHORT).show()
            }
        }

        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(binding.root.context, "Permission must be approved", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if(isSuccess) {
                dismiss()
                picUri?.let { picAddDialogListener.onPictureTake(it, photoFile!!) }
            }
        }
        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                dismiss()
                picAddDialogListener.onGalleryGet(uri)
            }
        }

    }

    private fun openCamera() {
        photoFile = File.createTempFile("IMG_", ".jpg", binding.root.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        picUri = FileProvider.getUriForFile(binding.root.context, "${binding.root.context.packageName}.provider", photoFile!!)
        cameraLauncher.launch(picUri)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun setClickListener() {
        binding.btnCamera.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
        binding.btnGallery.setOnClickListener {
            val permissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            }
            else {
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            storagePermission.launch(permissions)
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

