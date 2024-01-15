package com.example.imgselect

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraPreview(
    controller:LifecycleCameraController,
    modifier: Modifier=Modifier
){
    val lifecycleOwner= LocalLifecycleOwner.current
    AndroidView(factory = {
        PreviewView(it).apply{
            this.controller=controller
            controller.bindToLifecycle(lifecycleOwner)
        }
    },
        modifier=modifier.fillMaxSize()

    )
}

fun takePhoto(controller: LifecycleCameraController, applicationContext: Context, onPhotoTaken:(Bitmap)->Unit)
{

    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object: ImageCapture.OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val bitmap = image.toBitmap()

                onPhotoTaken(bitmap)
               Log.d("ManActivity2",bitmap.toString())
            }
            override fun onError(exception: ImageCaptureException){
                super.onError(exception)
                Log.d("ManActivity","Couldnt take pricture")
            }



        }

    )
}