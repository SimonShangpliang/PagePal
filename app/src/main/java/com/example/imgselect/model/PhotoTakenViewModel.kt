package com.example.imgselect.model

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PhotoTakenViewModel: ViewModel() {
//    private val _bitmaps= MutableStateFlow<List<ByteArray>>(emptyList())
//    val bitmaps=_bitmaps.asStateFlow()
//
//    fun onTakePhoto(bitmap:ByteArray){
//        _bitmaps.value+=bitmap}
//    fun removePhoto()
//    {
//        _bitmaps.value= emptyList()
//    }
//    fun removeSpecificPhoto()
//    {
//
//    }

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()
    private val _focus = MutableStateFlow(false)
    val focus: StateFlow<Boolean> = _focus
    private val _uriOpened = MutableStateFlow(false)
    val uriOpened: StateFlow<Boolean> = _uriOpened
    fun setFocus(value: Boolean) {
        _focus.value = value
    }
    fun setUriFocus(value: Boolean) {
        _uriOpened.value = value
    }

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun removePhoto() {
        _bitmap.value = null
    }

}
