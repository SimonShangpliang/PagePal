package com.example.imgselect

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    fun onTakePhoto(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun removePhoto() {
        _bitmap.value = null
    }

}
