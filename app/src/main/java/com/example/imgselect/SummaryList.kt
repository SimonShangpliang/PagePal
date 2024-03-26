package com.example.imgselect

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.imgselect.data.Summary
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.TextRecognitionViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Composable
fun SummaryScreen(summaryList: LiveData<List<Summary>> , navController: NavController , chatViewModel: ChatViewModel , chatViewModelWithImage: ChatViewModelWithImage) {
    val summary by summaryList.observeAsState(initial  = emptyList())
    LazyColumn {
        items(summary) {summary ->
            SummaryRow(summary = summary , navController = navController , chatViewModel = chatViewModel ,chatViewModelWithImage = chatViewModelWithImage )

        }
    }
}


@Composable
fun SummaryRow(summary: Summary , chatViewModel: ChatViewModel , navController: NavController , chatViewModelWithImage: ChatViewModelWithImage) {
    Row() {
        val imageBitmap = summary.image?.toImageBitmap()

//        var text by remember {
//            mutableStateOf("")
//        }

//        var handleText: (String) -> Unit = { updatedText ->
//            text = updatedText
//            chatViewModel.imageText = updatedText
//            Log.d("UpdatedText" , updatedText)
//            Log.d("text" , text)
//
//        }

        if(imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.clickable (
                    onClick = {
                        chatViewModelWithImage.isImageSelected = true
                        //chatViewModel.bitmap = imageBitmap
                        val bitmap = summary.image?.toBitmap()
                        val byteArray = bitmap?.let { compressBitmap(it) }
                      //  chatViewModelWithImage.imageList.add(byteArray?.toBitmap())

//                    navController.navigate(Screen.ChatScreen.route)
                        navController.popBackStack()
                    }
                )
            )
        }

        summary.summary?.let { Text(it) }
    }
}

fun ByteArray.toImageBitmap(): ImageBitmap? {
    return try {
        val inputStream = ByteArrayInputStream(this)
        val array = inputStream.readBytes()
        android.graphics.BitmapFactory.decodeByteArray(array, 0, array.size)?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ByteArray.toBitmap() : Bitmap? {
    return try {
        val inputStream = ByteArrayInputStream(this)
        val array = inputStream.readBytes()
        Log.d("ImageData" , "Size: ${array.size} bytes")
        BitmapFactory.decodeByteArray(array , 0 , array.size)
    } catch(e: Exception) {
        e.printStackTrace()
        null
    }
}

fun compressBitmap(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG , 30 , stream)

    val byteArray = stream.toByteArray()
    bitmap.recycle()
    return byteArray

}


