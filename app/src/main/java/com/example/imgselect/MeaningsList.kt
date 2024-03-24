   package com.example.imgselect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.imgselect.data.Meaning

@Composable
fun MeaningListScreen(meaningList: LiveData<List<Meaning>>) {

    val meanings by meaningList.observeAsState(initial = emptyList())
    LazyColumn {
        items(meanings) {meaning ->
            MeaningRow(meaning = meaning)

        }
    }

}

@Composable
fun MeaningRow(meaning: Meaning) {
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(10.dp)) {
//            meaning.word?.let { Text(it) }
//            meaning.meaning?.let { Text(it) }

        }

    }
}