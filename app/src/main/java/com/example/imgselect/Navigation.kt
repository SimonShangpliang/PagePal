package com.example.imgselect

import android.content.Context
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(window: Window,applicationContext: Context)
{
    var navController= rememberNavController()
val photoViewModel= viewModel<PhotoTakenViewModel>()
    NavHost(navController = navController, startDestination =Screen.MainScreen.route )
    {

        composable(route=Screen.MainScreen.route) {
           MainScreen(window,navController,photoViewModel)
        }
        composable(route=Screen.CameraScreen.route) {
            CameraScreen(applicationContext = applicationContext,photoViewModel)
        }

    }
}
