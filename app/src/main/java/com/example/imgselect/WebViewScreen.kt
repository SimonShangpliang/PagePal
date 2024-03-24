package com.example.imgselect

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.Window
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.DictionaryNetwork.WordData
import com.example.imgselect.data.Web
import com.example.imgselect.model.TextRecognitionViewModel
import com.example.imgselect.model.TextResult
import com.example.imgselect.model.WebHistoryViewModel
import com.example.imgselect.ui.theme.backgroundcolor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(window:Window,navController: NavController,textViewModel:TextRecognitionViewModel,dictionaryViewModel: DictionaryViewModel) {

    var url by rememberSaveable { mutableStateOf("https://www.google.com") }
    var currIndexPage by remember{ mutableStateOf(0) }
    var isLoading by remember{ mutableStateOf(false) }
    var urlList= mutableListOf<String>()
    val context = LocalContext.current
    var modifier:Modifier by remember {
        mutableStateOf(Modifier)
    }

      val webHistoryViewModel =viewModel<WebHistoryViewModel>()


    val setModifier: (Modifier) -> Unit = { modi ->
        modifier = modi
    }

    val setLoading: (Boolean) -> Unit = { updatedProgress ->
        // Update the progress value in your Composable
        isLoading = updatedProgress

    }

    val setUrl: (String) -> Unit = { newURl ->
        Log.d("MainActivity","String set")

        url = newURl
        if(!urlList.contains(url)){

            urlList.add(newURl)

            val newWeb = Web(website = url, date = System.currentTimeMillis())
            if(!newURl.contentEquals("https://www.google.com/")){
                CoroutineScope(Dispatchers.IO).launch {

                    webHistoryViewModel.addWeb(newWeb)
                }}

            currIndexPage+=1
        }
    }
    val webViewHolder = remember { WebViewHolder(context,setLoading,setUrl) }

    val setUrlHistory: (String) -> Unit = { newURl ->
        Log.d("MainActivity","String set")
webViewHolder.loadUrl(newURl)
        url = newURl
        if(!urlList.contains(url)){

            urlList.add(newURl)

            val newWeb = Web(website = url, date = System.currentTimeMillis())
            if(!newURl.contentEquals("https://www.google.com/")){
                CoroutineScope(Dispatchers.IO).launch {

                    webHistoryViewModel.addWeb(newWeb)
                }}

            currIndexPage+=1
        }
    }
    LaunchedEffect(Unit )
    {
        urlList.add("https://www.google.com/")
    }

    DisposableEffect(Unit) {
        // This block of code will be executed when the composable is removed from the composition
        onDispose {
           webViewHolder.close()
        }
    }
    var webHistory by remember {
        mutableStateOf(false)
    }
//val disp=LocalOnBackPressedDispatcherOwner.current
//    DisposableEffect(Unit) {
//        val dispatcher = disp?.onBackPressedDispatcher
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (currIndexPage > 0) {
//                    currIndexPage = currIndexPage - 1
//                    urlList.removeLast()
//                }
//                urlList.forEach{it->
//                    Log.d("main",it)
//
//                }
//
//                url = urlList[currIndexPage]
//                webViewHolder.loadUrl(url)
//            }
//        }
//
//        dispatcher?.addCallback(callback)
//
//        onDispose {
//            callback.remove()
//        }
//    }

    //coordinates
    // State to track if WebView is loading
//    val isLoading by viewModel.isLoading
    //val imageBitmap: ImageBitmap = selectedBitmap!!.asImageBitmap()
    val webListHistory by webHistoryViewModel.webHistory.observeAsState(emptyList())

    var box by remember { mutableStateOf(emptyList<TextResult>()) }
    Surface {

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent) // Ensure the background is transparent

        ) {
            if(box.size==-1) {

            }
            else{
if(webHistory)
{
    WebHistoryDialog(setShowDialog = { webHistory = it }, webListHistory,webHistoryViewModel,setUrlHistory)
}
                Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)) {
                    TextField(

                        modifier=Modifier.fillMaxWidth(0.7f),
                        value = url,
                        onValueChange = { newUrl -> url = newUrl },
                        singleLine = true,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isLoading = true
                                webViewHolder.loadUrl(url)
                            }
                        ), trailingIcon = {
if(url!=""){IconButton(onClick = { url=""

}) {
    Icon(Icons.Default.Clear,"clear",tint=Color.White)

}}
                        }


                    )
                    Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()){
                        IconButton(modifier=Modifier.size(30.dp),
                            onClick = {
                                // Set loading state to true when clicking the button
                                // Load the URL
                                isLoading = true
                                webViewHolder.loadUrl(url)
                                if(!urlList.contains(url)){
                                    currIndexPage++;
                                    urlList.add(url)
                                    if(!url.contentEquals("https://www.google.com/")){

                                        val newWeb = Web(website = url, date = System.currentTimeMillis())
                                   CoroutineScope(Dispatchers.IO).launch {             webHistoryViewModel.addWeb(newWeb)
                                    }}
                                }
                            }
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator()
                            }else{
                            Icon(Icons.Default.Refresh,"refresh",tint=Color.White)}
                        }


                        IconButton(
                            onClick = {
                                // Set loading state to true when clicking the button
                                // Load the URL
                                if (currIndexPage > 0) {
                                    currIndexPage = currIndexPage - 1
                                    urlList.removeLast()
                                }
                                urlList.forEach { it ->
                                    Log.d("main", it)
                                }

                                url = urlList[currIndexPage]
                                webViewHolder.loadUrl(url)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.CenterVertically)
                        ) {

                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }

                        IconButton(modifier=Modifier.size(30.dp),
                            onClick = {
webHistory=true

                                //History
                            }
                        ) {
                            Icon(painter= painterResource(id = R.drawable.baseline_manage_history_24),"refresh",tint=Color.White)
                        }

                    }

                    Button(modifier=Modifier.size(30.dp),
                        onClick = {
//                            if (focus == true) {
//                                startOffsetX = 0f
//                                startOffsetY = 0f
//                                endOffsetX = 0f
//                                endOffsetY = 0f
//                            } else {
//                                startOffsetX = 300f
//                                startOffsetY = 300f
//                                endOffsetX = 600f
//                                endOffsetY = 600f
//                            }
//                            focus = !focus
//                            Log.d("main",modifier.toString())
//                           if(focus===true)
//                           {
//                               setModifier(default_mod)
//                           }else
//                           {
//setModifier(Modifier)
//                           }

                        }
                    ) {
//                        if (focus == false) {
//                            Text("C")
//                        } else {
//                            Text("S C")
//                        }
                    }
                }
                // Show loading text if WebView is loading

//                    Image(modifier = Modifier
//        .padding(0.dp)
//        ,bitmap = imageBitmap, contentDescription = "Bitmap Image",                contentScale = ContentScale.Crop
//    )
Box(
    modifier= modifier
){
    Log.d("MainActivity", "DONE")

//    Canvas(modifier = Modifier
//        .fillMaxWidth()
//        .zIndex(2f)) {
//        val topLeftX = startOffsetX
//        val topLeftY = startOffsetY
//        val bottomRightX = endOffsetX
//        val bottomRightY = endOffsetY
//
//        val rectangleTopLeft = Offset(topLeftX, topLeftY)
//        if ((bottomRightX != 0f && bottomRightY != 0f)) {
//            drawRect(
//                color = Color.Blue.copy(alpha = 0.3f),
//                topLeft = rectangleTopLeft,
//                size = Size(Math.abs(bottomRightX - topLeftX), Math.abs(bottomRightY - topLeftY))
//            )
//
//        }
//    }
                AndroidView(
                    modifier = Modifier.fillMaxSize()
                    //    .verticalScroll(scroll)
                    ,
                    factory = { context ->
                        webViewHolder.apply{loadUrl(url)}.webView
                    }
                )}
            }
        }
    }}
}

@Composable
fun BackHandler(onBackPressed: () -> Unit) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(key1 = dispatcher) {
        dispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}
private fun reloadWebView(webViewHolder: WebViewHolder, url: String) {
    webViewHolder.loadUrl(url)
}


@Composable
fun DrawBoundingBoxes(bitmap: Bitmap,textResults: List<TextResult>,dictionaryViewModel:DictionaryViewModel) {
    val context = LocalContext.current
    val density = LocalDensity.current.density
    var listMeaning by remember{ mutableStateOf<List<WordData>?>( emptyList()) }
    var clickedBoxIndex by remember { mutableStateOf<Int?>(null) }
var showDialog by remember{
    mutableStateOf(false)
}
    LaunchedEffect(listMeaning )
 {
     if(listMeaning?.isNotEmpty()==true){

         showDialog=true;
     }
 }
    LaunchedEffect(textResults)
    {
        clickedBoxIndex=null
        Log.d("main","recomposed")
    }
if(showDialog)
{
    Log.d("main",listMeaning.toString())
    WordMeaningDialog(setShowDialog = {showDialog=it},listMeaning, dictionaryViewModel )
}

    if(dictionaryViewModel.dialogVisible) {
        AlertDialog(
            onDismissRequest = { dictionaryViewModel.dialogVisible = false },
            title = {Text("Enter a title")},
            text = {
                androidx.compose.material.TextField(
                    value = dictionaryViewModel.title,
                    onValueChange = {dictionaryViewModel.title = it},
                    label = {Text("Title")}
                )
            },
            confirmButton = {
                Button(onClick = {
                    val current = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("yyyy/MM/dd")
                    val date = dateFormat.format(current.time)
                    dictionaryViewModel.dialogVisible = false
                    dictionaryViewModel.saveMeaning()
                    dictionaryViewModel.setOfTitle.add(dictionaryViewModel.title)
                    dictionaryViewModel.setOfDates.add(date)
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .zIndex(3f)) {
        textResults.forEachIndexed{ index,result ->
            val boundingBox = result.boundingBox

            // Convert bounding box coordinates to pixels
            val left = boundingBox!!.left.toFloat()/density
            val top = boundingBox!!.top.toFloat()/density
            val right = boundingBox!!.right.toFloat()/density
            val bottom = boundingBox!!.bottom.toFloat()/density
            val eighty=80.00/density
            // Draw highlighted area around the text
            Box(
                modifier = Modifier.offset(x = left.dp, y = top.dp)
                    .size(width = right.dp - left.dp, height = bottom.dp - top.dp)
                    .background(
//if (index == clickedBoxIndex)
                        Color(255, 0, 0, 50)
//   else Color.Transparent
                    ) // Red color with 50% opacity

                    .clickable {
                        // Handle click event for this bounding box
                        //handleClick(result)
                        clickedBoxIndex = index
                        CoroutineScope(Dispatchers.IO).launch {
                            dictionaryViewModel.word = result.word

                            async {
                                dictionaryViewModel.getMeaning()
                            }.await()
                            async {
                                listMeaning =
                                    dictionaryViewModel.processResponse()
//                                dictionaryViewModel.response?.flatMap { response ->
//                                    response.meanings.flatMap { meaning ->
//                                        meaning.definitions.map { definition ->
//                                            definition.definition
//
//                                        }
//                                    }
//                                } ?: emptyList()}.await()
                            }.await()
                        }


                    }
            )

            // Draw bounding box rectangle
//            Box(
//                modifier = Modifier
//                    .offset(x = left, y = top - 80)
//                    .size(width = right - left, height = bottom - top)
//                    .border(BorderStroke(2.dp, Color.Red))
//            )
        }
    }

}