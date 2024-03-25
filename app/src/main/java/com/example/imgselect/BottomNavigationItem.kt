package com.example.imgselect

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavigationItem(
    val title :String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val route:String
)
