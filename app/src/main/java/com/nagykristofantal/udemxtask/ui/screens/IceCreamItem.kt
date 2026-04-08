package com.nagykristofantal.udemxtask

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nagykristofantal.udemxtask.data.IceCream
import com.nagykristofantal.udemxtask.data.Status
import androidx.compose.ui.res.painterResource
@Composable
fun IceCreamItem(iceCream: IceCream, basePrice: Double, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = iceCream.imageUrl,
                contentDescription = iceCream.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
                fallback = painterResource(id = R.drawable.logo),
                error = painterResource(id = R.drawable.logo)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = iceCream.name, style = MaterialTheme.typography.titleLarge)

                if (iceCream.status == Status.AVAILABLE) {
                    Text(text = "${basePrice.toInt()} €", style = MaterialTheme.typography.bodyLarge)
                }
            }

            when (iceCream.status) {
                Status.AVAILABLE -> {
                    Button(onClick = { onAddToCart() }) {
                        Text("KOSÁRBA")
                    }
                }
                Status.MELTED -> {
                    Text("Kifogyott", color = Color.Red, modifier = Modifier.padding(8.dp))
                }
                Status.UNAVAILABLE -> {
                    Text("Nem is volt", color = Color.Gray, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}