package com.example.imgselect

import android.content.Context
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dictionary.model.DictionaryViewModel
import com.example.imgselect.model.PhotoTakenViewModel
import com.example.imgselect.model.SummaryViewModel

@Composable
fun Navigation(window: Window,applicationContext: Context)
{
    var navController= rememberNavController()
    val photoViewModel= viewModel<PhotoTakenViewModel>()
    val dictionaryViewModel = viewModel<DictionaryViewModel>()
    val summaryViewModel = viewModel<SummaryViewModel>()
    NavHost(navController = navController, startDestination =Screen.MainScreen.route )
    {

        composable(route=Screen.MainScreen.route) {
           MainScreen(window,navController,photoViewModel)
        }
        composable(route=Screen.CameraScreen.route) {
            CameraScreen(applicationContext = applicationContext,photoViewModel)
        }
        composable(route = Screen.MeaningScreen.route) {
            MeaningListScreen(meaningList = dictionaryViewModel.getMeaningList())
        }
        composable(route = Screen.SummaryScreen.route) {
            SummaryScreen(summaryList = summaryViewModel.getSummaryList())
        }

        composable(route = Screen.ChatScreen.route) {
            ChatScreen()
        }

    }
}
