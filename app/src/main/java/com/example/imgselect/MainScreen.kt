package com.example.imgselect

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.DiscussUiState
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.model.SummaryViewModel
import com.example.imgselect.model.TextRecognitionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(window: Window, navController: NavController, photoViewModel: PhotoTakenViewModel, chatViewModel: ChatViewModel, chatViewModelWithImage: ChatViewModelWithImage, viewModel: TypewriterViewModel, textViewModel: TextRecognitionViewModel) {
    //These are used to represent the cropped regions from screen
    var startOffsetX by remember { mutableStateOf(0f) }
    var endOffsetX by remember { mutableStateOf(0f) }
    var startOffsetY by remember { mutableStateOf(0f) }
    var endOffsetY by remember { mutableStateOf(0f) }
    var selectedBitmap by remember { mutableStateOf(createBitmap(1,1, config = Bitmap.Config.ARGB_8888)) }
    val dictionaryViewModel= viewModel<DictionaryViewModel>()
    val summaryViewModel= viewModel<SummaryViewModel>()
    //The URI of the photo that the user has picked
    var photoUri: Uri? by remember { mutableStateOf(null) }
    val photoTaken by rememberUpdatedState(newValue = photoViewModel.bitmap.collectAsState().value)
    Log.d("ManActivity2",photoTaken.toString())
    val imageBitmap: ImageBitmap = selectedBitmap!!.asImageBitmap()
    var focus by remember{
        mutableStateOf(false)
    }
    val context= LocalContext.current
    //The launcher we will use for the PickVisualMedia contract.
    //When .launch()ed, this will display the photo picker.
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        photoUri = uri
    }


//    BottomSheetScaffold(
//        scaffoldState = scaffoldState,
//        sheetContent = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Black),
//                    modifier = Modifier.wrapContentSize()
//                ) {
//                    Text(
//                        text = "Summary",
//                        fontSize = 15.sp,
//                        color = Color.LightGray,
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .clickable {
//
//                            }
//                    )
//                }
//
//                Box(
//                    modifier = Modifier
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Divider(
//                        color = Color.Gray,
//                        modifier = Modifier
//                            .width(36.dp)
//                            .height(5.dp)
//                            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
//                    )
//                }
//
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Black),
//                    modifier = Modifier.wrapContentSize()
//                ) {
//                    Text(
//                        text = "Meaning",
//                        fontSize = 15.sp,
//                        color = Color.LightGray,
//                        modifier = Modifier
//                            .padding(horizontal = 16.dp)
//                            .clickable {
//
//                            }
//                    )
//                }
//            }
//            ChatScreen(chatViewModel = chatViewModel, navController = navController, chatViewModelWithImage = chatViewModelWithImage , viewModel = viewModel)
//        },
//        sheetPeekHeight = 80.dp, // Set this to the desired height to show a peek of the bottom sheet
//        sheetGesturesEnabled = true,
//        sheetElevation = 8.dp,
//        sheetShape = RoundedCornerShape(40.dp),
//        sheetBackgroundColor = Color.DarkGray,
//
//    ) { innerPadding ->
    // Pass the necessary parameters to your main content
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Magenta)){

        if(summaryViewModel.dialogVisible) {
            AlertDialog(
                onDismissRequest = { summaryViewModel.dialogVisible = false },
                title = { Text("Enter a title") },
                text = {
                    TextField(
                        value = summaryViewModel.title,
                        onValueChange = {summaryViewModel.title = it},
                        label = { Text("Title") }
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

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxHeight(0.6f)
                .fillMaxWidth()

        ) {

            Column() {

                Box() {

                      //  DisplayImageFromUri(photoUri = photoUri,focus=focus)
//                        DisplayRotatedImage(photoTaken = photoTaken, degrees = 90f,focus=focus)
                    //Do use this for seeing the cropped region real time

                }
            }

//Whatever you select from the upper box gets drawn by this into a particular size
//                Canvas(modifier = Modifier
//                    .fillMaxWidth()
//                    .zIndex(2f)) {
//                    Log.d("MainActivity", "DONE")
//                    val topLeftX = startOffsetX
//                    val topLeftY = startOffsetY
//                    val bottomRightX = endOffsetX
//                    val bottomRightY = endOffsetY
//
//                    val rectangleTopLeft = Offset(topLeftX, topLeftY)
//                    if ((bottomRightX != 0f && bottomRightY != 0f)) {
//                        drawRect(
//                            color = Color.Blue.copy(alpha = 0.3f),
//                            topLeft = rectangleTopLeft,
//                            size = Size(abs(bottomRightX - topLeftX), abs(bottomRightY - topLeftY))
//                        )
//
//                    }
//                }
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

//            Image(modifier = Modifier
//                .padding(0.dp)
//                .zIndex(4f)
//                ,bitmap = imageBitmap, contentDescription = "Bitmap Image",                contentScale = ContentScale.Crop
//            )
        Row(modifier= Modifier){
            Button(onClick = {
                summaryViewModel.dialogVisible = true
                //summaryViewModel.saveSummaryWithImage(selectedBitmap)
            }) {
                Text("Save Summary")
            }
            Button(
                onClick = {
                    //On button press, launch the photo picker
                    summaryViewModel.output = ""
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
                    Text("Crop")
                }
                else
                {
                    Text("Stop Crop")
                }
            }
//                if(focus==false){
//                    Text("Zoom Enabled/Crop Disabled",color=Color.Black)}
//                else
//                {
//                    Text("Zoom Disabled/Crop Enabled",color=Color.Black)
//                }
        }
        Row() {
            //Crop an image
            Button(
                onClick = { navController.navigate(Screen.CameraScreen.route) },
                modifier = Modifier
            ) {
                Text("Camera")
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
                        coroutineScope.launch {
                            //  textViewModel.performTextRecognition(selBi)

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
            Button(onClick = {navController.navigate(Screen.WebViewScreen.route)})
            { Text("WebView") }
        }
//Text Recognition done
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)) {
            Log.d("12MainActivit y","inside")


            Column {
                Column(modifier= Modifier){
                    Row() {


                        Button(
                            onClick = {
                                // dictionaryViewModel.getMeaning()

                            },
                            modifier = Modifier
                        ) {
                            Text("Get Meaning")
                        }

                        Button(onClick = { /*TODO*/
                            summaryViewModel.questioning("Summarize this"+ text)
                            // Display Snackbar with the recognized text

                        }) {
                            Text("Get Summary")


                        }
                    }
                }

            }
            Row() {
                Button(onClick = {dictionaryViewModel.saveMeaning()}) {
                    Text("Save Meaning")
                }


            }

            Row() {
                Button(onClick = {navController.navigate(Screen.MeaningScreen.route)}) {
                    Text("View Meaning")

                }

                Button(onClick = {navController.navigate(Screen.SummaryScreen.route)}) {
                    Text("View Summary")
                }
//
//                            Button(onClick = {navController.navigate(Screen.ChatScreen.route)}) {
//                                Text("Chat")
//                            }
            }

            Button(
                onClick = {
                    chatViewModel.getChatList()
                    navController.navigate(Screen.ChatListScreen.route)

                }
            ) {
                Text(
                    text = "View Chats"
                )

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
                )
            }
            is DiscussUiState.Loading ->
            {

                Text(modifier = Modifier.height(100.dp),
                    text="Summary ::Loading"
                )
            }

            is DiscussUiState.Error->
            {
                Text(modifier = Modifier.height(100.dp),
                    text="Summary ::Error"
                )
            }
            else->
            {
                Text(modifier = Modifier.height(100.dp),
                    text="Summary ::"
                )
            }




        }
        //Summary part over

        //Display word Meaning


    }

    //snackbar for displaying text
//word meaning over



    // }
}
