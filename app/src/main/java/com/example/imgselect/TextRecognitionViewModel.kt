package com.example.imgselect

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionViewModel : ViewModel() {

    fun performTextRecognition(bitmap: Bitmap,handleText:(String)->Unit) {
        val image = InputImage.fromBitmap(bitmap, 0) // Create InputImage from Bitmap
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                var text2=""
                for (block in visionText.textBlocks) {
                    val boundingBox = block.boundingBox
                    val cornerPoints = block.cornerPoints
                    val text = block.text
                    text2+=" "+text
                    Log.d("MainActivity",text)

//                        for (line in block.lines) {
//                            // ...
//                            for (element in line.elements) {
//                                // ...
//                            }
//                        }
                }
                handleText(text2)
                // Handle success and process the recognized text
                // ...
            }
            .addOnFailureListener { e ->
                // Handle failure or errors
                // ...
            }
    }
}