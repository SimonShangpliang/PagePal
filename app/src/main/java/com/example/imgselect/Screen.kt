package com.example.imgselect


sealed class Screen(val route:String) {
    object WebViewScreen:Screen("web_view_screen")
    object StartScreen:Screen("start_screen")

    object HomeScreen:Screen("home_view_screen")
    object ProfileScreen:Screen("profile_screen")
    object MainScreen:Screen("main_screen")
    object CameraScreen:Screen("camera_screen")
    object PdfScreen:Screen("pdf_screen")
    object MeaningScreen:Screen("meaning_screen")

    object SummaryScreen:Screen("summary_screen")

    object ChatScreen: Screen("chat_screen")

    object ChatListScreen: Screen("chat_list_screen")

    object FullChatScreen: Screen("full_chat_screen")

    object FullSummaryList: Screen("full_summary_list")

    object SingleDeckScreen: Screen("single_deck_screen")

    object SingleRowOfFlashLib: Screen("single_row_of_flash_lib")

    object GridOfRecentPDF: Screen("grid_of_recent_pdf")

    object GridOfRecentWebsites: Screen("grid_of_recent_websites")


}