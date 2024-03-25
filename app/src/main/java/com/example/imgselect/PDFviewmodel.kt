package com.example.imgselect

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rizzi.bouquet.HorizontalPdfReaderState
import com.rizzi.bouquet.PdfReaderState
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPdfReaderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PDFViewModel : ViewModel() {
    private val _docSelected = MutableStateFlow(false)
    val docSelected: StateFlow<Boolean> = _docSelected
    private val mStateFlow = MutableStateFlow<PdfReaderState?>(null)
    val stateFlow: StateFlow<PdfReaderState?> = mStateFlow

    val switchState = mutableStateOf(false)
    fun setDocSelected(value: Boolean) {
        _docSelected.value = value
    }
    fun openResource(resourceType: ResourceType) {
        mStateFlow.tryEmit(
            if (switchState.value) {
                HorizontalPdfReaderState(resourceType, true)
            } else {
                VerticalPdfReaderState(resourceType, true)
            }
        )
    }

    fun clearResource() {
        mStateFlow.tryEmit(null)
    }
}
