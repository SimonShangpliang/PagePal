package com.example.imgselect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.LiveData
import com.example.imgselect.data.Summary
import java.io.ByteArrayInputStream
import java.io.InputStream

@Composable
fun SummaryScreen(summaryList: LiveData<List<Summary>>) {
    val summary by summaryList.observeAsState(initial  = emptyList())
    LazyColumn {
        items(summary) {summary ->
            SummaryRow(summary = summary)

        }
    }
}


@Composable
fun SummaryRow(summary: Summary) {
    Row() {
        val imageBitmap = summary.image?.toImageBitmap()

        if(imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        summary.summary?.let { Text(it) }
    }
}

fun ByteArray.toImageBitmap(): ImageBitmap? {
    return try {
        val inputStream = ByteArrayInputStream(this)
        val array = inputStream.readBytes()
        android.graphics.BitmapFactory.decodeByteArray(array, 0, array.size)?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
