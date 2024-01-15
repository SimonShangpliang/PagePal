import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dictionary.model.DictionaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(dictionaryViewModel: DictionaryViewModel = viewModel()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = dictionaryViewModel.word,
            onValueChange = { dictionaryViewModel.word = it },
            label = { Text("Enter word") }
        )

        Button(
            onClick = { dictionaryViewModel.getMeaning() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Meaning")
        }

        val meaningsAndDefinitions: List<String> =
            dictionaryViewModel.response?.flatMap { response ->
                response.meanings.flatMap { meaning ->
                    meaning.definitions.map { definition ->
                        definition.definition
                    }
                }
            } ?: emptyList()

        Text(
            text = meaningsAndDefinitions.joinToString("\n"),
            modifier = Modifier.fillMaxWidth()
        )
    }
}