package com.example.imgselect.model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModeViewModel: ViewModel() {




        private val _isImageMode = MutableStateFlow(false)
        val isImageMode: StateFlow<Boolean> = _isImageMode

        fun setMode(value: Boolean) {
            _isImageMode.value = value
        }



}