package com.example.imgselect

import android.content.Context
import android.util.Log
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.data.Chat
import com.example.imgselect.data.Summary
import com.example.imgselect.model.ChatViewModel
import com.example.imgselect.model.ChatViewModelWithImage
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.model.SummaryViewModel
import com.example.imgselect.model.TextRecognitionViewModel
import com.example.imgselect.model.WebHistoryViewModel
import com.example.mytestapp.GridOfARowOfFlashLib
import com.example.mytestapp.flashCardLibrary

@Composable
fun Navigation(window: Window,applicationContext: Context,currScreen: (String)->Unit) {
    var navController = rememberNavController()
    val photoViewModel = viewModel<PhotoTakenViewModel>()
    val dictionaryViewModel = viewModel<DictionaryViewModel>()
    val chatViewModel = viewModel<ChatViewModel>()
    val summaryViewModel = viewModel<SummaryViewModel>()
    val chatViewModelWithImage = viewModel<ChatViewModelWithImage>()
    val typewriterViewModel = viewModel<TypewriterViewModel>()
    val textRecognitionViewModel = viewModel<TextRecognitionViewModel>()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route)
    {

        composable(route = Screen.MainScreen.route) {
            MainScreen(
                window,
                navController,
                photoViewModel,
                chatViewModel,
                chatViewModelWithImage,
                viewModel = typewriterViewModel,
                textRecognitionViewModel
            )
            currScreen(Screen.MainScreen.route)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController)
            currScreen(Screen.ProfileScreen.route)
        }
        composable(route = Screen.CameraScreen.route) {
            CameraScreen(applicationContext = applicationContext, photoViewModel)
            currScreen(Screen.CameraScreen.route)

        }

        composable(route = Screen.MeaningScreen.route) {
            MeaningListScreen(meaningList = dictionaryViewModel.getMeaningList())
            currScreen(Screen.MeaningScreen.route)

        }
        composable(route = Screen.SummaryScreen.route) {
            //SummaryScreen(summaryList = summaryViewModel.getSummaryList() , navController = navController , chatViewModel = chatViewModel , chatViewModelWithImage = chatViewModelWithImage)
            SummaryScreen(
                summaryList = summaryViewModel.getSummaryList(),
                navController = navController,
                summaryViewModel = summaryViewModel
            ) { summary ->
                navController.navigate("${Screen.FullSummaryList.route}/${summary.id}")
            }
            currScreen(Screen.SummaryScreen.route)

        }
        composable(route = Screen.PdfScreen.route) {

            currScreen(Screen.PdfScreen.route)

        }
        composable(route = Screen.HomeScreen.route) {
            Home(navController)
            currScreen(Screen.HomeScreen.route)

        }

        composable(route = Screen.ChatScreen.route) {
            //     ChatScreen(  chatViewModel = chatViewModel , chatViewModelWithImage = chatViewModelWithImage , viewModel = typewriterViewModel,)
            currScreen(Screen.ChatScreen.route)

        }
        composable(route = Screen.WebViewScreen.route) {
            WebViewScreen(
                window = window,
                navController = navController,
                textRecognitionViewModel,
                dictionaryViewModel
            )
            currScreen(Screen.WebViewScreen.route)

        }

        composable(route = Screen.ChatListScreen.route) {
            SavedChatsScreen(
                chatList = chatViewModel.getChatList(),
                chatViewModel = chatViewModel
            ) { chat ->
                navController.navigate("${Screen.FullChatScreen.route}/${chat.id}")
            }
            currScreen(Screen.ChatListScreen.route)

        }

        composable(route = "${Screen.FullChatScreen.route}/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")?.toInt()
            var chat by remember { mutableStateOf<Chat?>(null) }

            LaunchedEffect(chatId) {
                chatId?.let {
                    chat = chatViewModel.getChat(it)
                }
            }

            chat?.let { FullChatScreen(it) }
        }

        composable(route = "${Screen.FullSummaryList.route}/{summaryId}") { backStackEntry ->
            val summaryId = backStackEntry.arguments?.getString("summaryId")?.toInt()
            var summary by remember { mutableStateOf<Summary?>(null) }

            LaunchedEffect(summaryId) {
                summaryId?.let {
                    summary = summaryViewModel.getSummary(summaryId)
                }
            }

            summary?.let { SummaryListPage(summary = summary!!) }
        }

        composable(route = "${Screen.SingleDeckScreen.route}/{id}/{identifier}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val identifier = backStackEntry.arguments?.getString("identifier")?.toInt()

            Log.d("Identifiers", "${id}")
            Log.d("Identifiers", "${identifier}")

            if (id != null) {
                if (identifier != null) {
                    SingleDeckScreen(
                        basis = id,
                        identifier = identifier,
                        dictionaryViewModel = dictionaryViewModel,
                        list = dictionaryViewModel.getMeaningList()
                    )
                }
            }

        }

        composable(route = "${Screen.SingleDeckScreen.route}/{year}/{month}/{day}/{identifier}") { backStackEntry ->
            val year = backStackEntry.arguments?.getString("year")
            val month = backStackEntry.arguments?.getString("month")
            val day = backStackEntry.arguments?.getString("day")
            val identifier = backStackEntry.arguments?.getString("identifier")?.toInt()


            // Assuming you want to use a format like "YYYY/MM/DD" for the date
            val date =
                if (year != null && month != null && day != null) "$year/$month/$day" else null
            Log.d("Date", "Date: $date")
            Log.d("Identifiers", "Identifier: $identifier")

            if (date != null && identifier != null) {
                // Now you can use date and identifier as needed
                SingleDeckScreen(
                    basis = date,
                    identifier = identifier,
                    dictionaryViewModel = dictionaryViewModel,
                    list = dictionaryViewModel.getMeaningList()
                )
            }
        }



        composable(route = Screen.MeaningScreen.route) {
            //MeaningListScreen(meaningList = dictionaryViewModel.getMeaningList())
            flashCardLibrary(
                dictionaryViewModel = dictionaryViewModel,
                navController = navController
            )
        }

        composable(route = "${Screen.SingleRowOfFlashLib.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val list = when(id) {
                "0" -> dictionaryViewModel.setOfTitle
                "1" -> dictionaryViewModel.setOfDates
                else -> dictionaryViewModel.setOfWords
            }
            
            GridOfARowOfFlashLib(elements = list.toList() , dictionaryViewModel , navController)
        }
    }
}
