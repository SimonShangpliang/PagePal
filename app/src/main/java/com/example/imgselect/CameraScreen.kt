package com.example.imgselect

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CameraScreen(applicationContext:Context,photoViewModel: PhotoTakenViewModel)
{
    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

Box(modifier = Modifier.fillMaxSize())
{
    CameraPreview(controller =controller,modifier=Modifier )
    Button(onClick = { takePhoto(controller,applicationContext,{
        photoViewModel.onTakePhoto(it)
    },
        )/*TODO*/ },
        shape=MaterialTheme.shapes.extraSmall,
        modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp) ) {
        Text("Take Photo")

    }
}
    
}