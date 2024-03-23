package com.example.imgselect


sealed class Screen(val route:String) {
    object WebViewScreen:Screen("web_view_screen")

    object MainScreen:Screen("main_screen")
    object CameraScreen:Screen("camera_screen")

    object MeaningScreen:Screen("meaning_screen")

    object SummaryScreen:Screen("summary_screen")

    object ChatScreen: Screen("chat_screen")

    object ChatListScreen: Screen("chat_list_screen")


}