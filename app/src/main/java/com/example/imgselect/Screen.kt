package com.example.imgselect


sealed class Screen(val route:String) {
    object MainScreen:Screen("main_screen")
    object CameraScreen:Screen("camera_screen")

    object MeaningScreen:Screen("meaning_screen")

    object SummaryScreen:Screen("summary_screen")

    object ChatScreen: Screen("chat_screen")


}