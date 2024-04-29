package com.example.imgselect

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.impl.utils.MatrixExt.postRotate
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

fun takePhoto(controller: LifecycleCameraController, applicationContext: Context,flashMode:Int, onPhotoTaken:(Bitmap)->Unit)
{

    val imageCapture = controller ?: return // Ensure imageCapture is not null

    val flashModeSetting = when (flashMode) {
        ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_ON
        ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_OFF
        ImageCapture.FLASH_MODE_AUTO -> ImageCapture.FLASH_MODE_AUTO
        else -> ImageCapture.FLASH_MODE_AUTO // Default to AUTO if unknown mode is provided
    }

    // Set flash mode
    imageCapture.imageCaptureFlashMode = flashModeSetting
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object: ImageCapture.OnImageCapturedCallback(){
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val bitmap = image.toBitmap()
                val rotatedBitmap = rotateBitmapIfRequired(bitmap, image)

                onPhotoTaken(rotatedBitmap)
               Log.d("ManActivity2",bitmap.toString())
            }
            override fun onError(exception: ImageCaptureException){
                super.onError(exception)
                Log.d("ManActivity","Couldnt take pricture")
            }



        }

    )
}
fun rotateBitmapIfRequired(bitmap: Bitmap, image: ImageProxy): Bitmap {
    val rotationDegrees = when (image.imageInfo.rotationDegrees) {
        0 -> 0
        90 -> 90
        180 -> 180
        270 -> 270
        else -> 0 // Default to 0 degrees if rotation is not 0, 90, 180, or 270
    }

    return if (rotationDegrees != 0) {
        val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}

