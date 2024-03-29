package com.example.imgselect

import android.content.Context
import android.media.Image
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.ui.theme.CameraBottom
import com.example.imgselect.ui.theme.aliceBlue
import com.example.imgselect.ui.theme.interestcolour2

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
    val photo=photoViewModel.bitmap.collectAsState()
    var photoUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        photoUri = uri
        if(uri!=null)
        {
            photoViewModel.setUriFocus(true)
        }
    }
    if(photo.value == null&&photoUri==null) {
        CameraPreview(controller = controller, modifier = Modifier)



                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .height(94.dp)
                                .fillMaxWidth()
                                .background(color = CameraBottom)
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxHeight().fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        launcher.launch(
                                            PickVisualMediaRequest(
                                                //Here we request only photos. Change this to .ImageAndVideo if you want videos too.
                                                //Or use .VideoOnly if you only want videos.
                                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .background(
                                            color = Color.Transparent
                                        )
                                        .padding(start = 39.3.dp)
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.insert_photo),
                                        contentDescription = null, tint= aliceBlue
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        takePhoto(
                                            controller, applicationContext,
                                            {
                                                photoViewModel.onTakePhoto(it)
                                            },
                                        )/*TODO*/
                                    },
                                    modifier = Modifier.background(
                                        color = Color.Transparent
                                    )
                                ) {
                                    RoundImage(
                                        image = painterResource(id = R.drawable.ellipse_9),
                                        modifier = Modifier
                                            .size(80.dp),
                                        color = aliceBlue,
                                        borderWidth = 2f
                                    )
                                }
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    modifier = Modifier
                                        .background(
                                            color = Color.Transparent
                                        )
                                        .padding(end = 39.3.dp)
                                        .size(40.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.flash_auto),
                                        contentDescription = null,tint= aliceBlue
                                    )
                                }
                            }
                        }

                    }

        }
       else{
        Box(modifier=Modifier.fillMaxSize()){
            if(photoUri==null){
                DisplayRotatedImage(photoTaken = photo.value, degrees = 90f, photoTakenViewModel = photoViewModel)

            }else
            {
                DisplayImageFromUri(photoUri = photoUri, photoTakenViewModel = photoViewModel)
            }
        IconButton(onClick = { /*TODO*/
        photoViewModel.removePhoto()
            photoUri=null
            photoViewModel.setUriFocus(false)

        },modifier= Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "delete",modifier=Modifier.size(40.dp))}

           }
        }
    }
}