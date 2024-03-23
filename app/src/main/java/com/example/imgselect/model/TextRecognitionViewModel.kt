package com.example.imgselect.model

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TextRecognitionViewModel : ViewModel() {
    private val recognizer: TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun performOnlyTextRecognition(bitmap: Bitmap): String {
        return suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0) // Create InputImage from Bitmap
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    var text2 = ""
                    for (block in visionText.textBlocks) {
                        val text = block.text
                        text2 += " $text\n" // Add line break after each text block
                    }
                    continuation.resume(text2.trim())
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

    suspend fun performTextRecognition(bitmap: Bitmap): List<TextResult> {
        return suspendCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val textResults = mutableListOf<TextResult>()
                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                val word = element.text
                                val boundingBox = element.boundingBox
                                val textResult = TextResult(word, boundingBox)
                                textResults.add(textResult)
                            }
                        }
                    }
                    continuation.resume(textResults)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }


}
data class TextResult(val word: String, val boundingBox: Rect?)
