package com.example.imgselect

import android.content.Context
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.mytestapp.flashCardLibrary

@Composable
fun Navigation(window: Window,applicationContext: Context)
{
    var navController= rememberNavController()
    val photoViewModel= viewModel<PhotoTakenViewModel>()
    val dictionaryViewModel = viewModel<DictionaryViewModel>()
    val chatViewModel = viewModel<ChatViewModel>()
    val summaryViewModel = viewModel<SummaryViewModel>()
    val chatViewModelWithImage = viewModel<ChatViewModelWithImage>()
    val typewriterViewModel = viewModel<TypewriterViewModel>()
    val textRecognitionViewModel= viewModel<TextRecognitionViewModel>()

    NavHost(navController = navController, startDestination =Screen.MainScreen.route )
    {

        composable(route=Screen.MainScreen.route) {
           MainScreen(window,navController,photoViewModel,chatViewModel, chatViewModelWithImage , viewModel = typewriterViewModel,textRecognitionViewModel)
        }
        composable(route=Screen.CameraScreen.route) {
            CameraScreen(applicationContext = applicationContext,photoViewModel)
        }

        composable(route = Screen.SummaryScreen.route) {
            //SummaryScreen(summaryList = summaryViewModel.getSummaryList() , navController = navController , chatViewModel = chatViewModel , chatViewModelWithImage = chatViewModelWithImage)
            SummaryScreen(summaryList = summaryViewModel.getSummaryList(), navController = navController , summaryViewModel = summaryViewModel){summary->
                navController.navigate("${Screen.FullSummaryList.route}/${summary.id}")
            }
        }

        composable(route = Screen.ChatScreen.route) {
            ChatScreen(  chatViewModel = chatViewModel , chatViewModelWithImage = chatViewModelWithImage , viewModel = typewriterViewModel,)
        }
        composable(route = Screen.WebViewScreen.route) {
            WebViewScreen( window=window,navController = navController,textRecognitionViewModel,dictionaryViewModel)
        }
        
        composable(route = Screen.ChatListScreen.route) {
            SavedChatsScreen(chatList = chatViewModel.getChatList() , chatViewModel = chatViewModel) { chat ->
                navController.navigate("${Screen.FullChatScreen.route}/${chat.id}")
            }
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

        composable(route = "${Screen.FullSummaryList.route}/{summaryId}") {backStackEntry->
            val summaryId = backStackEntry.arguments?.getString("summaryId")?.toInt()
            var summary by remember { mutableStateOf<Summary?>(null)}

            LaunchedEffect(summaryId) {
                summaryId?.let {
                    summary = summaryViewModel.getSummary(summaryId)
                }
            }
            
            summary?.let { SummaryListPage(summary = summary!!)}
        }

        composable(route = "${Screen.SingleDeckScreen.route}/{id}") {backStackEntry->
            val id = backStackEntry.arguments?.getString("id")

        }

        composable(route = Screen.MeaningScreen.route) {
            //MeaningListScreen(meaningList = dictionaryViewModel.getMeaningList())
            flashCardLibrary(dictionaryViewModel = dictionaryViewModel , navController = navController)
        }




    }
}
