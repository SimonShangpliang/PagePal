package com.example.imgselect
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.animations.LoadingAnimation
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.ModeViewModel
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.model.SummaryViewModel
import com.example.imgselect.model.TextRecognitionViewModel
import com.example.imgselect.model.TextResult
import com.example.imgselect.ui.theme.ImgselectTheme

import com.kamatiaakash.text_to_speech_using_jetpack_compose.AudioViewModel

import com.rizzi.bouquet.HorizontalPDFReader
import com.rizzi.bouquet.HorizontalPdfReaderState
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.VerticalPdfReaderState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    private val viewModel: PDFViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var currScreen by remember{ mutableStateOf(Screen.HomeScreen.route) }

            if(currScreen==Screen.PdfScreen.route)
            {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                )

                onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        viewModel.clearResource()
                        currScreen= Screen.HomeScreen.route
                    }
                })
            }
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp
            val screenWidth = configuration.screenWidthDp
            val context = LocalContext.current
            //coordinates
            var startOffsetX by remember { mutableStateOf(0f) }
            var endOffsetX by remember { mutableStateOf(0f) }
            var startOffsetY by remember { mutableStateOf(0f) }
            var endOffsetY by remember { mutableStateOf(0f) }
            var focus by remember{
                mutableStateOf(false)
            }
            var modifier:Modifier by remember {
                mutableStateOf(Modifier)
            }
            val setModifier: (Modifier) -> Unit = { modi ->
                modifier = modi
            }
            val screensWithBottomScaffold=listOf(Screen.WebViewScreen.route,Screen.CameraScreen.route
                ,Screen.PdfScreen.route
            )
            var selectedBitmap by remember { mutableStateOf(createBitmap(1,1, config = Bitmap.Config.ARGB_8888)) }
            var selectedBitmapSS:Bitmap? by remember { mutableStateOf(null) }


            val default_mod= Modifier
                .pointerInput(Unit)
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


            val chatViewModel = viewModel<ChatViewModel>()
            val chatViewModelWithImage = viewModel<ChatViewModelWithImage>()
            val typewriterViewModel = viewModel<TypewriterViewModel>()
            val dictionaryViewModel = viewModel<DictionaryViewModel>()
            val textViewModel = viewModel<TextRecognitionViewModel>()
            val modeViewModel = viewModel<ModeViewModel>()
            val audioViewModel=viewModel<AudioViewModel>()

            val isImageMode=modeViewModel.isImageMode.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            var box by remember { mutableStateOf(emptyList<TextResult>()) }


            var summaryDialog by remember{
                mutableStateOf(false)
            }
            var interpretDialog by remember{
                mutableStateOf(false)
            }

            var summaryText by remember{
                mutableStateOf("Sample summary text")
            }
            var cropBox by remember{
                mutableStateOf(false)
            }
            var cropBox2 by remember{
                mutableStateOf(false)
            }
            var cropBox3 by remember{
                mutableStateOf(false)
            }
            var summaryViewModel=viewModel<SummaryViewModel>()
            ImgselectTheme {
                // flashCardLibrary()

                if(!hasCameraPermission())
                {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(android.Manifest.permission.CAMERA),0
                    )
                }
                var meaning by remember{
                    mutableStateOf(false)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var scaffoldState = rememberBottomSheetScaffoldState(

                        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
                    )
                    var setZeroOffset by remember{ mutableStateOf(0) }
                    AnimatedVisibility(summaryDialog)
                    {

                        SummaryDialog(setShowDialog = {summaryDialog = it
                            focus=false
                            Log.d("main","jere herere")
                            startOffsetX=0f
                            startOffsetY=0f
                            endOffsetX=0f
                            endOffsetY=0f
                        }, summaryViewModel,audioViewModel,{setZeroOffset=it

                        })


                    }
                    AnimatedVisibility(interpretDialog)
                    {

                        InterpretDialog(setShowDialog = {interpretDialog = it
                            focus=false

                            startOffsetX=0f
                            startOffsetY=0f
                            endOffsetX=0f
                            endOffsetY=0f
                        }, chatViewModelWithImage,audioViewModel,{

                        })


                    }
                    if(summaryViewModel.dialogVisible) {
                        AlertDialog(
                            onDismissRequest = { summaryViewModel.dialogVisible = false },
                            title = {Text("Enter a title")},
                            text = {
                                TextField(
                                    value = summaryViewModel.title,
                                    onValueChange = {summaryViewModel.title = it},
                                    label = {Text("Title")}
                                )
                            },
                            confirmButton = {
                                Button(onClick = {
                                    summaryViewModel.dialogVisible = false
                                    summaryViewModel.saveSummaryWithImage(selectedBitmap , title = summaryViewModel.title)
                                }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                    var setHeight by remember{ mutableStateOf(80.dp) }
                    var cropDone by remember{ mutableStateOf(true) }
                    var cropDone2 by remember{ mutableStateOf(true) }
                    var cropDone3 by remember{ mutableStateOf(true) }

                    val photoTakenViewModel=viewModel<PhotoTakenViewModel>()
                    val photoStatus=photoTakenViewModel.bitmap.collectAsState()
                    val uriOpened=photoTakenViewModel.uriOpened.collectAsState()

                    BottomSheetScaffold(
                        scaffoldState = scaffoldState,
                        sheetContent = {
AnimatedContent(targetState = isImageMode.value,label="top Bar") {it->
    when(it)
    {
        false->{
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(Color.Black)
                    .height(40.dp)

                    .clickable {
                        if (cropDone) {
                            if (currScreen == Screen.PdfScreen.route&&viewModel.docSelected.value) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    async {
                                        selectedBitmapSS =
                                            captureEntireScreen(
                                                context = context,
                                                window,
                                                screenWidth,
                                                screenHeight
                                            )

                                    }.await()
                                }
                            }

if(cropBox==true){
    selectedBitmapSS=null
    cropBox = false
    setHeight = 80.dp
}else{
                            cropBox = true
                            setHeight = 140.dp}
                        } else {

                            coroutineScope.launch {

                                selectedBitmap = async {
                                    captureSelectedRegion(
                                        context,
                                        window,
                                        startOffsetX,
                                        startOffsetY,
                                        endOffsetX,
                                        endOffsetY
                                    )

                                }.await()
                                val textResponse = async {
                                    textViewModel.performOnlyTextRecognition(
                                        selectedBitmap
                                    )

                                }.await()
                                summaryDialog = true
                                summaryText = textResponse
                                cropDone = true
                                selectedBitmapSS = null
                                setModifier(Modifier)
                                Log.d("MainAct", textResponse)
                                summaryViewModel.questioningSummary(summaryText)
                                photoTakenViewModel.setFocus(false)

                            }
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (cropDone) "Summary" else "Done",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(
                                    start = if (cropDone) 20.dp else 15.dp,
                                    end = if (cropDone) 20.dp else 8.dp
                                )
                                .align(Alignment.CenterVertically)
                            //   .align(Alignment.Center)


                        )
                        AnimatedVisibility (!cropDone) {
                            LoadingAnimation(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically),
                                circleSize = 10.dp,
                                spaceBetween = 5.dp,
                                travelDistance = 8.dp
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                        .animateContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier

                            .width(120.dp)
                            .height(5.dp)
                            .background(
                                Color.Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }

                Box(modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(Color.Black)
                    .height(40.dp)
                    .clickable {
                        if (meaning == false) {
                            coroutineScope.launch {
                                selectedBitmap = async {
                                    //     captureSelectedRegion(window, startOffsetX, startOffsetY, endOffsetX, endOffsetY)
                                    captureEntireScreen(
                                        context = context,
                                        window,
                                        screenWidth,
                                        screenHeight
                                    )
                                }.await()
                                box = async {
                                    textViewModel.performTextRecognition(
                                        selectedBitmap
                                    )
                                }.await()
                                meaning = true
                                Log.d("MainAct", box.toString())
                            }
                        } else {
                            meaning = false
                        }
                    }
                ) {

                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (meaning) "Stop" else "Meaning",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(
                                    start = if (cropDone) 20.dp else 15.dp,
                                    end = if ( cropDone) 20.dp else 8.dp
                                )
                                .align(Alignment.CenterVertically)
                            //   .align(Alignment.Center)

                        )
                        AnimatedVisibility (meaning) {
                            LoadingAnimation(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically),
                                circleSize = 10.dp,
                                spaceBetween = 5.dp,
                                travelDistance = 8.dp
                            )
                        }
                    }
                }

            }
        }
        else ->{
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(Color.Black)
                    .height(40.dp)

                    .clickable {
                        if (cropDone3) {
                            if (currScreen == Screen.PdfScreen.route&&viewModel.docSelected.value) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    async {
                                        selectedBitmapSS =
                                            captureEntireScreen(
                                                context = context,
                                                window,
                                                screenWidth,
                                                screenHeight
                                            )

                                    }.await()
                                }
                            }
                            if(cropBox3==true){
                                selectedBitmapSS=null
                                cropBox3 = false
                                setHeight = 80.dp
                            }else{
                                cropBox3= true
                                setHeight = 140.dp}

                        } else {

                            coroutineScope.launch {

                                selectedBitmap = async {
                                    captureSelectedRegion(
                                        context,
                                        window,
                                        startOffsetX,
                                        startOffsetY,
                                        endOffsetX,
                                        endOffsetY
                                    )

                                }.await()
                                val byteArray = selectedBitmap?.let { compressBitmap(it) }
                                chatViewModelWithImage.setInterpretImage(byteArray?.toBitmap())
                                interpretDialog = true
                              //  summaryText = textResponse
                                cropDone3 = true
                                selectedBitmapSS = null
                                setModifier(Modifier)

                                //   Log.d("MainAct", textResponse)
                                chatViewModelWithImage.getInterpretResponseFromChatBot()
                                photoTakenViewModel.setFocus(false)

                            }
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (cropDone3) "Interpret" else "Done",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(
                                    start = if (cropDone3) 20.dp else 15.dp,
                                    end = if (cropDone3) 20.dp else 8.dp
                                )
                                .align(Alignment.CenterVertically)
                            //   .align(Alignment.Center)


                        )
                        AnimatedVisibility (!cropDone3) {
                            LoadingAnimation(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically),
                                circleSize = 10.dp,
                                spaceBetween = 5.dp,
                                travelDistance = 8.dp
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                        .animateContentSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier

                            .width(120.dp)
                            .height(5.dp)
                            .background(
                                Color.Gray,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }

                Box(modifier = Modifier
                    .clip(
                        RoundedCornerShape(20.dp)
                    )
                    .background(Color.Black)
                    .height(40.dp)
                    .clickable {

                        if (cropDone2) {
                            if (currScreen == Screen.PdfScreen.route&&viewModel.docSelected.value) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    async {
                                        selectedBitmapSS =
                                            captureEntireScreen(
                                                context = context,
                                                window,
                                                screenWidth,
                                                screenHeight
                                            )

                                    }.await()
                                }
                            }
                            if(cropBox2==true){
                                selectedBitmapSS=null
                                cropBox2 = false
                                setHeight = 80.dp
                            }else{
                                cropBox2= true
                                setHeight = 140.dp}

                        } else {

                            coroutineScope.launch {

                                selectedBitmap = async {
                                    captureSelectedRegion(
                                        context,
                                        window,
                                        startOffsetX,
                                        startOffsetY,
                                        endOffsetX,
                                        endOffsetY
                                    )

                                }.await()
                                val byteArray = selectedBitmap?.let { compressBitmap(it) }
                              chatViewModelWithImage.addBitmapToList(byteArray?.toBitmap())
                                startOffsetX=0f
                                startOffsetY=0f
                                endOffsetX=0f
                                endOffsetY=0f
                                cropDone2 = true
                                selectedBitmapSS = null
                                setModifier(Modifier)

                                focus=false

                                //   Log.d("MainAct", textResponse)
                             //   summaryViewModel.questioningSummary(summaryText)
                                photoTakenViewModel.setFocus(false)
                            }}


                    }
                ) {

                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (!cropDone2) "Done" else "Add Image",
                            fontSize = 15.sp,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(
                                    start = if (cropDone2) 20.dp else 15.dp,
                                    end = if (cropDone2) 20.dp else 8.dp
                                )
                                .align(Alignment.CenterVertically)
                            //   .align(Alignment.Center)

                        )
                        AnimatedVisibility (!cropDone2) {
                            LoadingAnimation(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .align(Alignment.CenterVertically),
                                circleSize = 10.dp,
                                spaceBetween = 5.dp,
                                travelDistance = 8.dp
                            )
                        }
                    }
                }

            }
        }
    }

}

                            LaunchedEffect(currScreen,photoStatus.value,uriOpened.value)
                            {
                                startOffsetX=0f
                                startOffsetY=0f
                                endOffsetX=0f
                                endOffsetY=0f
                                meaning=false
                              //  selectedBitmapSS=null

                                // if(currScreen==Screen.)
                                if(screensWithBottomScaffold.contains(currScreen))
                                {
                                    if(currScreen==Screen.CameraScreen.route){
                                        if(photoStatus.value==null&&uriOpened.value==false){
                                            setHeight=0.dp
                                       }else
                                        {
                                            setHeight=80.dp
                                        }
                                    }else {
                                        setHeight = 80.dp
                                    }
                                }else
                                {

                                    setHeight=0.dp
                                }
                            }
                            if(cropBox)
                            {
                                Box(modifier= Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.95f)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(Color.Black)
                                )
                                {
                                    val scope= rememberCoroutineScope()
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        OutlinedButton(onClick = {

                                            cropBox=false
                                            setHeight=80.dp
//                                            scope.launch{
//                                            scaffoldState.bottomSheetState.collapse()}
                                            cropDone=false
                                            if (focus == true) {
                                                startOffsetX = 0f
                                                startOffsetY = 0f
                                                endOffsetX = 0f
                                                endOffsetY = 0f
                                            } else {
                                                startOffsetX = 300f
                                                startOffsetY = 300f
                                                endOffsetX = 600f
                                                endOffsetY = 600f
                                            }
                                            photoTakenViewModel.setFocus(true)
                                            focus = !focus
                                            if(focus==true)
                                            {
                                                setModifier(default_mod)
                                            }else
                                            {
                                                setModifier(Modifier)
                                            }


                                        }) {
                                            Text(text = "Crop Region",color=Color.LightGray)
                                        }
                                        OutlinedButton(onClick = {
                                            setHeight=80.dp

                                            coroutineScope.launch {
                                                selectedBitmap = async {
                                                    //     captureSelectedRegion(window, startOffsetX, startOffsetY, endOffsetX, endOffsetY)
                                                    captureEntireScreen(
                                                        context = context,
                                                        window,
                                                        screenWidth,
                                                        screenHeight
                                                    )
                                                }.await()
                                                val textResponse = async {
                                                    textViewModel.performOnlyTextRecognition(
                                                        selectedBitmap
                                                    )

                                                }.await()
                                                summaryDialog = true
                                                summaryText = textResponse
selectedBitmapSS=null
                                                Log.d("MainAct", textResponse)
                                                summaryViewModel.questioningSummary(summaryText)}
                                            cropBox=false
                                        }) {
                                            Text(text = "Full Screen",color=Color.LightGray)

                                        }


                                    }

                                }

                            }
                            if(cropBox2)
                            {
                                Box(modifier= Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.95f)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(Color.Black)
                                )
                                {
                                    val scope= rememberCoroutineScope()
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        OutlinedButton(onClick = {
                                            setHeight=80.dp

                                            cropBox2=false

                                            cropDone2=false
                                            if (focus == true) {
                                                startOffsetX = 0f
                                                startOffsetY = 0f
                                                endOffsetX = 0f
                                                endOffsetY = 0f
                                            } else {
                                                startOffsetX = 300f
                                                startOffsetY = 300f
                                                endOffsetX = 600f
                                                endOffsetY = 600f
                                            }
                                            photoTakenViewModel.setFocus(true)
                                            focus = !focus
//                                            scope.launch{
//                                                scaffoldState.bottomSheetState.collapse()}
                                            if(focus==true)
                                            {
                                                setModifier(default_mod)
                                            }else
                                            {
                                                setModifier(Modifier)
                                            }


                                        }) {
                                            Text(text = "Crop Region",color=Color.LightGray)
                                        }
                                        OutlinedButton(onClick = {
                                            setHeight=80.dp
                                            cropBox2=false

                                            coroutineScope.launch {
                                                selectedBitmap = async {
                                                    //     captureSelectedRegion(window, startOffsetX, startOffsetY, endOffsetX, endOffsetY)
                                                    captureEntireScreen(
                                                        context = context,
                                                        window,
                                                        screenWidth,
                                                        screenHeight
                                                    )
                                                }.await()

                                              //  summaryText = textResponse
                                                val byteArray = selectedBitmap.let { compressBitmap(it) }
                                                chatViewModelWithImage.addBitmapToList(byteArray?.toBitmap())
                                           //     Log.d("MainAct", textResponse)
                                                selectedBitmapSS=null
                                                //summaryViewModel.questioningSummary(summaryText)}

                                        }
                                        }) {
                                            Text(text = "Full Screen",color=Color.LightGray)

                                        }


                                    }

                                }

                            }
                            if(cropBox3)
                            {
                                Box(modifier= Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.95f)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(Color.Black)
                                )
                                {
                                    val scope= rememberCoroutineScope()
                                    Row(modifier= Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        OutlinedButton(onClick = {
                                            setHeight=80.dp

                                            cropBox3=false

                                            cropDone3=false
                                            if (focus == true) {
                                                startOffsetX = 0f
                                                startOffsetY = 0f
                                                endOffsetX = 0f
                                                endOffsetY = 0f
                                            } else {
                                                startOffsetX = 300f
                                                startOffsetY = 300f
                                                endOffsetX = 600f
                                                endOffsetY = 600f
                                            }
                                            photoTakenViewModel.setFocus(true)
                                            focus = !focus

                                            if(focus===true)
                                            {
                                                setModifier(default_mod)
                                            }else
                                            {
                                                setModifier(Modifier)
                                            }


                                        }) {
                                            Text(text = "Crop Region",color=Color.LightGray)
                                        }
                                        OutlinedButton(onClick = {

                                            cropBox3=false
                                            setHeight=80.dp
                                            coroutineScope.launch {
                                                selectedBitmap = async {
                                                    //     captureSelectedRegion(window, startOffsetX, startOffsetY, endOffsetX, endOffsetY)
                                                    captureEntireScreen(
                                                        context = context,
                                                        window,
                                                        screenWidth,
                                                        screenHeight
                                                    )
                                                }.await()

                                                //  summaryText = textResponse
                                                val byteArray = selectedBitmap?.let { compressBitmap(it) }
                                                chatViewModelWithImage.setInterpretImage(byteArray?.toBitmap())
                                                //     Log.d("MainAct", textResponse)
                                                chatViewModelWithImage.getInterpretResponseFromChatBot()
selectedBitmapSS=null
                                                //summaryViewModel.questioningSummary(summaryText)}
interpretDialog=true

                                            }}) {
                                            Text(text = "Full Screen",color=Color.LightGray)

                                        }


                                    }

                                }

                            }



                            ChatScreen(chatViewModel = chatViewModel, chatViewModelWithImage = chatViewModelWithImage , viewModel = typewriterViewModel,modeViewModel)
                        },
                        sheetPeekHeight = setHeight, // Set this to the desired height to show a peek of the bottom sheet
                        sheetGesturesEnabled = true,
                        sheetElevation = 8.dp,
                        sheetShape = RoundedCornerShape(topStart=40.dp, topEnd = 40.dp),
                        sheetBackgroundColor = Color(0xff1E1E1E)
                    ) { innerPadding ->
                        Canvas(modifier =
                        Modifier
                            .fillMaxSize()
                            .zIndex(6f)
//


                        ) {


                            if ((endOffsetX != 0f &&  endOffsetY != 0f)) {

                                drawRect(
                                    color = Color.Blue.copy(alpha = 0.3f),
                                    topLeft =  Offset(startOffsetX,startOffsetY),
                                    size = Size(Math.abs(endOffsetX - startOffsetX), Math.abs(endOffsetY - startOffsetY))
                                )

                            }

                        }

                        Box(modifier=modifier) {
                            if(currScreen==Screen.PdfScreen.route){
                                Log.d("Main","true")
                                PDFViewer(selectedBitmapSS)

                            }else{
                                Navigation(window = window, applicationContext = applicationContext,{currScreen=it})}
                            if(meaning){   DrawBoundingBoxes(
                                selectedBitmap,
                                textResults = box,
                                dictionaryViewModel,audioViewModel
                            )}
                        }

                    }

                }

            }

        }
    }



    @Composable
    fun PDFViewer(selectedBitmapSS: Bitmap?){
        androidx.compose.material.Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val scaffoldState = rememberScaffoldState()
            val state = viewModel.stateFlow.collectAsState()

            Box(modifier = Modifier
                .background(Color.Black)
                .zIndex(0f)) {
                when (val actualState = state.value) {
                    null -> SelectionView()
                    is VerticalPdfReaderState -> {
                        if(selectedBitmapSS==null){
                            PDFView(
                                pdfState = actualState,
                                scaffoldState = scaffoldState)}else
                        {
                            Image(bitmap = selectedBitmapSS.asImageBitmap(), contentDescription ="",modifier=Modifier.fillMaxSize(),            contentScale = ContentScale.Crop
                            )
                        }
                    }

                    is HorizontalPdfReaderState ->
                        if(selectedBitmapSS==null){
                            HPDFView(
                                pdfState = actualState,
                                scaffoldState = scaffoldState
                            )}else
                        {
                            Image(bitmap = selectedBitmapSS.asImageBitmap(), contentDescription ="",modifier=Modifier.fillMaxSize(),            contentScale = ContentScale.Crop
                            )
                        }
                }
            }
        }
    }






    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SelectionElement(
        title: String,
        text: String,
        onClick: () -> Unit
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            backgroundColor = androidx.compose.material.MaterialTheme.colors.surface,
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                androidx.compose.material.Text(
                    text = title,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 4.dp
                    ),
                    style = androidx.compose.material.MaterialTheme.typography.body1
                )
                androidx.compose.material.Text(
                    text = text,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 16.dp
                    ),
                    style = androidx.compose.material.MaterialTheme.typography.caption
                )
            }
        }
    }

    @Composable
    fun SelectionView() {
        Column(modifier = Modifier.fillMaxSize()) {
            SelectionElement(
                title = "Open Local file",
                text = "Open a file in device memory"
            ) {
                openDocumentPicker()
            }

            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                androidx.compose.material.Text(text = "List view",color= Color.White)
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = viewModel.switchState.value,
                    onCheckedChange = {
                        viewModel.switchState.value = it

                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                        uncheckedTrackColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                        uncheckedTrackAlpha = 0.54f
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                androidx.compose.material.Text(text = "Pager view",color= Color.White)
            }
        }
    }

    @Composable
    fun PDFView(
        pdfState: VerticalPdfReaderState,
        scaffoldState: ScaffoldState
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
                    .zIndex(0f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f)
            ) {
                LinearProgressIndicator(
                    progress = pdfState.loadPercent / 100f,
                    color = Color.Red,
                    backgroundColor = Color.Green,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .background(
                                color = androidx.compose.material.MaterialTheme.colors.surface.copy(alpha = 0.5f),
                                shape = androidx.compose.material.MaterialTheme.shapes.medium
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material.Text(
                            text = "Page: ${pdfState.currentPage}/${pdfState.pdfPageCount}",
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 4.dp,
                                top = 8.dp
                            )
                        )

                    }
                }
            }
            LaunchedEffect(key1 = pdfState.error) {
                pdfState.error?.let {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message ?: "Error"
                    )
                }
            }
        }
    }

    @Composable
    fun HPDFView(
        pdfState: HorizontalPdfReaderState,
        scaffoldState: ScaffoldState
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            HorizontalPDFReader(
                state = pdfState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                LinearProgressIndicator(
                    progress = pdfState.loadPercent / 100f,
                    color = Color.Red,
                    backgroundColor = Color.Green,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .background(
                                color = androidx.compose.material.MaterialTheme.colors.surface.copy(alpha = 0.5f),
                                shape = androidx.compose.material.MaterialTheme.shapes.medium
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material.Text(
                            text = "Page: ${pdfState.currentPage}/${pdfState.pdfPageCount}",
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 4.dp,
                                top = 8.dp
                            )
                        )


                    }
                }
            }
            LaunchedEffect(key1 = pdfState.error) {
                pdfState.error?.let {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message ?: "Error"
                    )
                }
            }
        }
    }

    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST_CODE)
    }

    private fun openDocument(documentUri: Uri) {
        documentUri.path?.let {
            viewModel.openResource(
                ResourceType.Local(
                    documentUri
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { documentUri ->
                contentResolver.takePersistableUriPermission(
                    documentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                openDocument(documentUri)
                viewModel.setDocSelected(true)
            }
        }else
        {
            viewModel.setDocSelected(false)

        }
    }





    private fun hasCameraPermission()=
        ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED

}






fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

@Composable
fun DisplayImageFromUri(photoUri: Uri?, modifier: Modifier = Modifier,photoTakenViewModel: PhotoTakenViewModel) {
    if (photoUri != null) {

        val painter = rememberAsyncImagePainter(
            ImageRequest
                .Builder(LocalContext.current)
                .data(data = photoUri)
                .build()
        )

        var scale by remember{
            mutableStateOf(1f)
        }
        var offset by remember{
            mutableStateOf(Offset.Zero)
        }
        BoxWithConstraints(modifier= Modifier
            .fillMaxWidth()

        ){
            val focus=photoTakenViewModel.focus.collectAsState()


            var state= rememberTransformableState{
                    zoomChange, panChange, rotationChange ->
                if(focus.value==false){
                    scale=(scale*zoomChange).coerceIn(1f,5f)

                    val extraWidth=(scale-1)*constraints.maxWidth
                    val extraHeight=(scale-1)*constraints.maxHeight

                    val maxX=extraWidth/2
                    val maxY=extraHeight/2
                    offset=Offset(
                        x=(offset.x+scale*panChange.x).coerceIn(-maxX,maxX),
                        y=(offset.y+scale*panChange.y).coerceIn(-maxY,maxY)

                    )}
            }
            Image(
                painter = painter,
                contentDescription = null,
                modifier = modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .zIndex(1f)
                    //.border(6.0.dp, Color.Gray)
                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y

                    }
                    .takeIf { !focus.value }?.transformable(state)?:(modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .zIndex(1f)
                    //.border(6.0.dp, Color.Gray)
                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y

                    })
                ,
                contentScale = ContentScale.Crop
            )
        }}
}

@Composable
fun DisplayRotatedImage(photoTaken: Bitmap?, degrees: Float, modifier: Modifier = Modifier,photoTakenViewModel: PhotoTakenViewModel) {
    if (photoTaken != null) {
        var scale by remember{
            mutableStateOf(1f)
        }
        var offset by remember{
            mutableStateOf(Offset.Zero)
        }
        val focus=photoTakenViewModel.focus.collectAsState()
        BoxWithConstraints(modifier= Modifier
            .fillMaxWidth()
            .zIndex(0f)

        ){


            var state= rememberTransformableState{
                    zoomChange, panChange, rotationChange ->
                if(focus.value==false){
                    scale=(scale*zoomChange).coerceIn(1f,5f)

                    val extraWidth=(scale-1)*constraints.maxWidth
                    val extraHeight=(scale-1)*constraints.maxHeight

                    val maxX=extraWidth/2
                    val maxY=extraHeight/2
                    offset=Offset(
                        x=(offset.x+scale*panChange.x).coerceIn(-maxX,maxX),
                        y=(offset.y+scale*panChange.y).coerceIn(-maxY,maxY)

                    )}
            }
            Image(
                bitmap = rotateBitmap(photoTaken, degrees)!!.asImageBitmap(),
                contentDescription = null,

                modifier = modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .zIndex(1f)
                    //.border(6.0.dp, Color.Gray)
                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y

                    }
                    .takeIf { !focus.value }?.transformable(state)?:(modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .zIndex(1f)
                    //.border(6.0.dp, Color.Gray)
                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y

                    })
                ,
                contentScale = ContentScale.Crop
            )
        }
        Log.d("ManActivity", photoTaken.toString())

    }
}
private const val OPEN_DOCUMENT_REQUEST_CODE = 0x33

suspend fun captureSelectedRegion(context:Context,
                                  window: Window,
                                  startOffsetX: Float,
                                  startOffsetY: Float,
                                  endOffsetX: Float,
                                  endOffsetY: Float
): Bitmap {
    val width = Math.abs(startOffsetX - endOffsetX)
    val height = Math.abs(startOffsetY - endOffsetY)
    val statusBarHeight = getStatusBarHeight(context)
    if (width.toInt() <= 0 || height.toInt() <= 0) {
        throw IllegalArgumentException("Invalid region dimensions")
    }

    return suspendCoroutine { continuation ->
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val sourceRect = Rect(
            startOffsetX.toInt(),
            startOffsetY.toInt() + statusBarHeight,
            endOffsetX.toInt(),
            endOffsetY.toInt() + statusBarHeight
        )
        val handler = Handler(Looper.getMainLooper())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use PixelCopy with Rect (API level 26+)
            PixelCopy.request(window, sourceRect, bitmap, { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {

                    continuation.resume(bitmap)
                } else {
                    bitmap.recycle() // Recycle the bitmap in case of failure

                    continuation.resumeWithException(RuntimeException("Failed to copy screen content"))
                }
            }, handler)
        } else {
            bitmap.recycle() // Recycle the bitmap in case of failure

            // Use alternative methods for lower API levels
            // For instance, View.draw() or View.getDrawingCache()
            continuation.resumeWithException(RuntimeException("PixelCopy is not supported on this device"))
        }
    }
}
private fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
}
suspend fun captureEntireScreen(
    context: Context,
    window: Window,
    widthInDp: Int,
    heightInDp: Int
): Bitmap {
    val displayMetrics = context.resources.displayMetrics
    val width = (widthInDp * displayMetrics.density).toInt()
    val height = (heightInDp * displayMetrics.density).toInt()
    val statusBarHeight = getStatusBarHeight(context)

    return suspendCancellableCoroutine { continuation ->
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val sourceRect = Rect(0, statusBarHeight.toInt(), width, height+statusBarHeight.toInt())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use PixelCopy with Rect (API level 26+)
            val handler = Handler(Looper.getMainLooper())
            PixelCopy.request(window, sourceRect, bitmap, { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    continuation.resume(bitmap)
                } else {
                    bitmap.recycle() // Recycle the bitmap in case of failure

                    continuation.resumeWithException(RuntimeException("Failed to copy screen content"))
                }
            }, handler)
        } else {
            bitmap.recycle() // Recycle the bitmap in case of failure

            continuation.resumeWithException(RuntimeException("PixelCopy is not supported on this device"))
        }

        // Cancellation handler if needed
        continuation.invokeOnCancellation {
            // Handle cancellation if needed
        }
    }
}
fun euclideanDistance(x1:Float, y1: Float, x2: Float, y2: Float): Float {
    val deltaX = x2 - x1
    val deltaY = y2 - y1

    return sqrt(deltaX * deltaX + deltaY * deltaY).toFloat()
}

