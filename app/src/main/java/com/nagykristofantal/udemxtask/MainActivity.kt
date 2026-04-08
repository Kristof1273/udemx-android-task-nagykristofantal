package com.nagykristofantal.udemxtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nagykristofantal.udemxtask.ui.theme.Udemx_taskTheme
import com.nagykristofantal.udemxtask.viewmodel.IceCreamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Udemx_taskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IceCreamListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IceCreamListScreen(
    modifier: Modifier = Modifier,
    viewModel: IceCreamViewModel = viewModel()
) {
    val iceCreams by viewModel.iceCreams.collectAsState()
    val basePrice by viewModel.basePrice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "GRANDMA'S HOMEMADE ICE CREAM",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text(text = errorMessage ?: "", color = Color.Red)
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(iceCreams) { iceCream ->
                            IceCreamItem(
                                iceCream = iceCream,
                                basePrice = basePrice,
                                onAddToCart = {
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}