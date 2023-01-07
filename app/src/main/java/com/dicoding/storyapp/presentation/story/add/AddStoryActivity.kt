package com.dicoding.storyapp.presentation.story.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.base.BaseActivity
import com.dicoding.storyapp.base.BaseResult
import com.dicoding.storyapp.databinding.ActivityAddStoryBinding
import com.dicoding.storyapp.presentation.story.StoryViewModel
import com.dicoding.storyapp.rotateBitmap
import com.dicoding.storyapp.uriToFile
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : BaseActivity() {
    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!

    private val storyViewModel: StoryViewModel by viewModel()

    private var photoFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        const val ADD_STORY_RESULT_OK = 201
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        fun startActivityForResult(context: Context, intentLauncher: ActivityResultLauncher<Intent>) {
            val starter = Intent(context, AddStoryActivity::class.java)
            intentLauncher.launch(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        initAction()
        initObserver()
    }

    private fun initAction(){
        with(binding){
            buttonCamera.setOnClickListener { startCameraX() }
            buttonGallery.setOnClickListener { startGallery() }
            buttonAdd.setOnClickListener {
                if(photoFile == null){
                    Toast.makeText(this@AddStoryActivity, "Silahkan tambahkan foto terlebih dahulu", Toast.LENGTH_LONG).show()
                } else if(edAddDescription.text.toString().isEmpty()){
                    Toast.makeText(this@AddStoryActivity, "Silahkan tambahkan deskripsi terlebih dahulu", Toast.LENGTH_LONG).show()
                } else {
                    lifecycleScope.launch {
                        val finalFile = if(photoFile!!.length() / 1024 > 1000){
                            Compressor
                                .compress(this@AddStoryActivity,photoFile!!){
                                    quality(80)
                                    size(maxFileSize = 1_048_576)
                                }
                        } else {
                            photoFile
                        }
                        storyViewModel.postStory(edAddDescription.text.toString(), finalFile!!)
                    }
                }
            }
        }
    }

    private fun initObserver(){
        storyViewModel.uploadStory.observe(this){
            when (it) {
                is BaseResult.Loading -> {
                    showProgressDialog()
                }
                is BaseResult.Success -> {
                    hideProgressDialog()
                    Toast.makeText(this, "Post story sukses", Toast.LENGTH_LONG).show()
                    setResult(ADD_STORY_RESULT_OK)
                    finish()
                }
                is BaseResult.Error -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                    hideProgressDialog()
                }
                else -> {
                    hideProgressDialog()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            photoFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            photoFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }
}