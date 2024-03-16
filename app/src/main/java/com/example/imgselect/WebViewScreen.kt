package com.example.imgselect

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen() {
    var url by remember { mutableStateOf("https://www.google.com") }
    val context = LocalContext.current

    //coordinates
    var startOffsetX by remember { mutableStateOf(0f) }
    var endOffsetX by remember { mutableStateOf(0f) }
    var startOffsetY by remember { mutableStateOf(0f) }
    var endOffsetY by remember { mutableStateOf(0f) }
    var focus by remember{
        mutableStateOf(false)
    }
    var selectedBitmap by remember { mutableStateOf(createBitmap(1,1, config = Bitmap.Config.ARGB_8888)) }
    //coordinates
    // State to track if WebView is loading
//    val isLoading by viewModel.isLoading
    var isLoading by remember{ mutableStateOf(false) }
    val setLoading: (Boolean) -> Unit = { updatedProgress ->
        Log.d("MainActivity","donedone")
        // Update the progress value in your Composable
        isLoading = updatedProgress
    }
    val webViewHolder = remember { WebViewHolder(context,setLoading) }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)) {
                    TextField(
                        value = url,
                        onValueChange = { newUrl -> url = newUrl }
                    )
                    Button(
                        onClick = {
                            // Set loading state to true when clicking the button
                            // Load the URL
isLoading=true
                            webViewHolder.loadUrl(url)
                        }
                    ) {
                        Text("Load")
                    }
                }
                Button(
                    onClick = {
                        if(focus==true)
                        {
                            startOffsetX=0f
                            startOffsetY=0f
                            endOffsetX=0f
                            endOffsetY=0f
                        }
                        else
                        {
                            startOffsetX=300f
                            startOffsetY=300f
                            endOffsetX=600f
                            endOffsetY=600f
                        }
                        //On button press, launch the photo picker
                        focus=!focus

                    }
                ) {
                    if(focus==false){
                        Text("Crop")}
                    else
                    {
                        Text("Stop Crop")
                    }
                }

                // Show loading text if WebView is loading
                if (isLoading) {
                    CircularProgressIndicator()
              }
Box(
    modifier=Modifier.pointerInput(Unit)
    {

        detectTapGestures(onDoubleTap = { change ->
            //setting coordinates of selected images
            if (focus) {
                startOffsetX = change.x + 20
                startOffsetY = change.y + 20
                endOffsetX = change.x + 300
                endOffsetY = change.y + 300
            }
        }) { }
    }

        .pointerInput(Unit) {

            detectDragGestures() { change, point ->
                if (focus) {
                    val distanceToStart = euclideanDistance(
                        startOffsetX,
                        startOffsetY,
                        change.position.x,
                        change.position.y
                    )
                    val distanceToEnd = euclideanDistance(
                        startOffsetX,
                        endOffsetY,
                        change.position.x,
                        change.position.y
                    )
                    val distanceToStartEnd = euclideanDistance(
                        endOffsetX,
                        startOffsetY,
                        change.position.x,
                        change.position.y
                    )
                    val distanceToEndEnd = euclideanDistance(
                        endOffsetX,
                        endOffsetY,
                        change.position.x,
                        change.position.y
                    )

                    val minDistance = minOf(
                        distanceToStart,
                        distanceToEnd,
                        distanceToStartEnd,
                        distanceToEndEnd
                    )
                    when (minDistance) {
                        distanceToStart -> {
                            startOffsetX = change.position.x
                            startOffsetY = change.position.y
                        }

                        distanceToEnd -> {
                            startOffsetX = change.position.x
                            endOffsetY = change.position.y
                        }

                        distanceToStartEnd -> {
                            endOffsetX = change.position.x
                            startOffsetY = change.position.y
                        }

                        distanceToEndEnd -> {
                            endOffsetX = change.position.x
                            endOffsetY = change.position.y
                        }
                    }
                }
            }
        }
){
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .zIndex(2f)) {
        Log.d("MainActivity", "DONE")
        val topLeftX = startOffsetX
        val topLeftY = startOffsetY
        val bottomRightX = endOffsetX
        val bottomRightY = endOffsetY

        val rectangleTopLeft = Offset(topLeftX, topLeftY)
        if ((bottomRightX != 0f && bottomRightY != 0f)) {
            drawRect(
                color = Color.Blue.copy(alpha = 0.3f),
                topLeft = rectangleTopLeft,
                size = Size(Math.abs(bottomRightX - topLeftX), Math.abs(bottomRightY - topLeftY))
            )

        }
    }
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        webViewHolder.apply{loadUrl(url)}.webView
                    }
                )}
            }
        }
    }
}

private fun reloadWebView(webViewHolder: WebViewHolder, url: String) {
    webViewHolder.loadUrl(url)
}


