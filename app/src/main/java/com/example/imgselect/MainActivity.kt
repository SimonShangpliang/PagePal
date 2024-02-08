package com.example.imgselect
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.Window
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.model.DiscussUiState
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.model.SummaryViewModel
import com.example.imgselect.model.TextRecognitionViewModel
import com.example.imgselect.ui.theme.ImgselectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.abs
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImgselectTheme {
                if(!hasCameraPermission())
                {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(android.Manifest.permission.CAMERA),0
                    )
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
Navigation(window = window, applicationContext =applicationContext )

                }
            }

        }
    }
    private fun hasCameraPermission()=
        ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED

}


@Composable
fun MainScreen(window: Window,navController: NavController,photoViewModel: PhotoTakenViewModel) {
    //These are used to represent the cropped regions from screen
    var startOffsetX by remember { mutableStateOf(0f) }
    var endOffsetX by remember { mutableStateOf(0f) }
    var startOffsetY by remember { mutableStateOf(0f) }
    var endOffsetY by remember { mutableStateOf(0f) }
    var selectedBitmap by remember { mutableStateOf(createBitmap(1,1, config = Bitmap.Config.ARGB_8888)) }
    val textViewModel=viewModel<TextRecognitionViewModel>()
    val dictionaryViewModel=viewModel<DictionaryViewModel>()
    val summaryViewModel=viewModel<SummaryViewModel>()
    //The URI of the photo that the user has picked
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val photoTaken by rememberUpdatedState(newValue = photoViewModel.bitmap.collectAsState().value)
    Log.d("ManActivity2",photoTaken.toString())
    val imageBitmap: ImageBitmap = selectedBitmap!!.asImageBitmap()
    var focus by remember{
        mutableStateOf(false)
    }
    //The launcher we will use for the PickVisualMedia contract.
    //When .launch()ed, this will display the photo picker.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        //When the user has selected a photo, its URI is returned here
        photoUri = uri
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)){

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxHeight(0.6f)
            .fillMaxWidth()
            .pointerInput(Unit)
            {

                detectTapGestures(onDoubleTap =  { change ->
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
    ) {

        Column() {

Box() {

    DisplayImageFromUri(photoUri = photoUri,focus=focus)
    DisplayRotatedImage(photoTaken = photoTaken, degrees = 90f,focus=focus)
    //Do use this for seeing the cropped region real time
//    Image(modifier = Modifier
//        .padding(0.dp)
//        ,bitmap = imageBitmap, contentDescription = "Bitmap Image",                contentScale = ContentScale.Crop
//    )
}
        }

//Whatever you select from the upper box gets drawn by this into a particular size
        Canvas(modifier = Modifier.fillMaxWidth().zIndex(2f)) {
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
                    size = Size(abs(bottomRightX - topLeftX), abs(bottomRightY - topLeftY))
                )
            }
        }
    }
        //just some neccesary vals and vars
        var text by remember {
            mutableStateOf("")
        }
        var handleText: (String) -> Unit = { updatedText ->
            text = updatedText

        }
        val snackbarHostState = SnackbarHostState()
        val coroutineScope = rememberCoroutineScope()
        //

        Row(modifier=Modifier){
            Button(
                onClick = {
                    //On button press, launch the photo picker
                    launcher.launch(
                        PickVisualMediaRequest(
                            //Here we request only photos. Change this to .ImageAndVideo if you want videos too.
                            //Or use .VideoOnly if you only want videos.
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Text("Select Photo")
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
            if(focus==false){
                Text("Zoom Enabled/Crop Disabled",color=Color.Black)}
            else
            {
                Text("Zoom Disabled/Crop Enabled",color=Color.Black)
            }
        }
        Row() {
            //Crop an image
            Button(onClick = {
                captureSelectedRegion(window,startOffsetX,startOffsetY,endOffsetX,endOffsetY,{selectedBitmap=it})
            })
            {
                Text(text = "select Image Cropped")

            }
            //Text Recognition using MLkit

            val context = LocalContext.current
            Button(
                onClick = {
                    if (selectedBitmap.height == 1 && selectedBitmap.width == 1) {
                        Toast.makeText(
                            context,
                            "Select a Region and press select image cropped",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        textViewModel.performTextRecognition(selectedBitmap, handleText)
                        coroutineScope.launch {
                            // Display Snackbar with the recognized text
                            delay(2000)
                            dictionaryViewModel.word = text
                            snackbarHostState.showSnackbar(
                                message = text,
                                actionLabel = "Dismiss"
                            )


                        }
                    }
                },
                content = {
                    Text(text = "Extract text")

                }
            )
        }
//Text Recognition done
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)) {
                Log.d("12MainActivit y","inside")


                Column {
                    Column(modifier=Modifier){
Row() {


    Button(
        onClick = {
            dictionaryViewModel.getMeaning()

        },
        modifier = Modifier
    ) {
        Text("Get Meaning")
    }
    Button(
        onClick = { navController.navigate(Screen.CameraScreen.route) },
        modifier = Modifier
    ) {
        Text("Camera")
    }
    Button(onClick = { /*TODO*/
        summaryViewModel.questioning("Summarize this"+ text)
        // Display Snackbar with the recognized text

    }) {
        Text("Get Summary")

    }
}
                        Row() {
                            Button(onClick = {dictionaryViewModel.saveMeaning()}) {
                                Text("Save Meaning")
                            }

                            Button(onClick = { summaryViewModel.saveSummaryWithImage(selectedBitmap) }) {
                                Text("Save Summary")
                            }
                        }
                        
                        Row() {
                            Button(onClick = {navController.navigate(Screen.MeaningScreen.route)}) {
                                Text("View Meaning")
                                
                            }

                            Button(onClick = {navController.navigate(Screen.SummaryScreen.route)}) {
                                Text("View Summary")
                            }

                            Button(onClick = {navController.navigate(Screen.ChatScreen.route)}) {
                                Text("Chat")
                            }
                        }
}

                    //Summary part

                    val appUiState=summaryViewModel.uiState.collectAsState().value
                    when(appUiState) {
                        is DiscussUiState.Success->{
                            val scroll= rememberScrollState()

                            Text(modifier = Modifier
                                .height(130.dp)
                                .verticalScroll(scroll),
                              text="Summary ::"+ appUiState.outputText
                            )}
                        is DiscussUiState.Loading ->
                        {

                            Text(modifier =Modifier.height(100.dp),
                                text="Summary ::Loading"
                            )}

                        is DiscussUiState.Error->
                        {
                            Text(modifier =Modifier.height(100.dp),
                                text="Summary ::Error"
                            )
                        }
                        else->
                        {
                            Text(modifier =Modifier.height(100.dp),
                                text="Summary ::"
                            )}




                    }
                    //Summary part over

                //Display word Meaning
                val meaningsAndDefinitions: List<String> =
                    dictionaryViewModel.response?.flatMap { response ->
                        response.meanings.flatMap { meaning ->
                            meaning.definitions.map { definition ->
                                definition.definition
                            }
                        }
                    } ?: emptyList()
val meaningsScroll= rememberScrollState()
                    var wordMeaning="Word Meaning ::" +meaningsAndDefinitions.joinToString("\n")
                    if(dictionaryViewModel.wrongWord==true)
                    {
                        wordMeaning="No such word found"
                    }
                Text(
                    text = wordMeaning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                        .height(120.dp)
                        .background(Color.DarkGray)
                        .verticalScroll(meaningsScroll)
                )

                }
                //snackbar for displaying text
//word meaning over

                SnackbarHost(snackbarHostState)

        }
}
}

fun rotateBitmap(source: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

@Composable
fun DisplayImageFromUri(photoUri: Uri?, modifier: Modifier = Modifier,focus:Boolean) {
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


        var state= rememberTransformableState{
            zoomChange, panChange, rotationChange ->
            if(focus==false){
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
                .takeIf { !focus }?.transformable(state)?:(modifier
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
fun DisplayRotatedImage(photoTaken: Bitmap?, degrees: Float, modifier: Modifier = Modifier,focus:Boolean) {
    if (photoTaken != null) {
        var scale by remember{
            mutableStateOf(1f)
        }
        var offset by remember{
            mutableStateOf(Offset.Zero)
        }
        BoxWithConstraints(modifier= Modifier
            .fillMaxWidth()

        ){


            var state= rememberTransformableState{
                    zoomChange, panChange, rotationChange ->
                if(focus==false){
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
                    .takeIf { !focus }?.transformable(state)?:(modifier
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
private fun captureSelectedRegion(
    window:Window,
    startOffsetX: Float,
    startOffsetY: Float,
    endOffsetX: Float,
    endOffsetY: Float,
    onCaptureResult: (Bitmap) -> Unit
) {
    var width=abs(startOffsetX -  endOffsetX)
    var height= abs(startOffsetY - endOffsetY)


    var bitmap:Bitmap= createBitmap(1,1,Bitmap.Config.ARGB_8888)
    if(width.toInt()>0&& height.toInt()>0){   bitmap  = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        var sourceRect: Rect? = Rect(0,0,0,0) // Initializing with default values
        sourceRect = Rect(startOffsetX.toInt(), startOffsetY.toInt()+80, (endOffsetX).toInt(), (endOffsetY).toInt()+80)
        val handler = Handler(Looper.getMainLooper())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use PixelCopy with Rect (API level 26+)

            PixelCopy.request(window,sourceRect,bitmap,{copyResult->
                if (copyResult == PixelCopy.SUCCESS) {
                    // The specified screen content has been copied to the bitmap
                    // Now you can use 'bitmap' as needed
                   // selectedBitmap=bitmap
onCaptureResult(bitmap)
                    Log.d("MainActivity",bitmap.toString())
                } else {

                    Log.e("PixelCopy", "Failed to copy screen content")
                }

            },handler)

        } else {
            // Use alternative methods for lower API levels
            // For instance, View.draw() or View.getDrawingCache()
        }}
}
fun euclideanDistance(x1:Float, y1: Float, x2: Float, y2: Float): Float {
    val deltaX = x2 - x1
    val deltaY = y2 - y1

    return sqrt(deltaX * deltaX + deltaY * deltaY).toFloat()
}