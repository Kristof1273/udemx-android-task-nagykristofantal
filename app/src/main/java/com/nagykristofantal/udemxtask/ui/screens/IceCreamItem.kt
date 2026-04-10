package com.nagykristofantal.udemxtask.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nagykristofantal.udemxtask.R
import com.nagykristofantal.udemxtask.data.IceCream
import com.nagykristofantal.udemxtask.data.Status

@Composable
fun IceCreamItem(iceCream: IceCream, basePrice: Double, onAddToCart: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {

        AsyncImage(
            model = iceCream.imageUrl,
            contentDescription = iceCream.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            fallback = painterResource(id = R.drawable.logo),
            error = painterResource(id = R.drawable.logo)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3000F))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = iceCream.name,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (iceCream.status == Status.AVAILABLE) {
                    Text(
                        text = "${basePrice.toInt()} €",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            when (iceCream.status) {
                Status.AVAILABLE -> {
                    Button(
                        onClick = { onAddToCart() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFE3000F)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = "KOSÁRBA", fontWeight = FontWeight.Bold)
                    }
                }
                Status.MELTED -> {
                    Text("Kifogyott", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Status.UNAVAILABLE -> {
                    Text("Nem is volt", color = Color.LightGray, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}