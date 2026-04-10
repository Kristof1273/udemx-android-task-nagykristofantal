package com.nagykristofantal.udemxtask.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nagykristofantal.udemxtask.viewmodel.IceCreamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: IceCreamViewModel,
    onNavigateBack: () -> Unit,
    onOrderSuccess: () -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val totalCartPrice = cart.sumOf { it.totalPrice }
    val context = LocalContext.current // Ez kell a Toast üzenethez

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kosár") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Vissza")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE3000F),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fizetendő: ${totalCartPrice.toInt()} €",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            viewModel.placeOrder(
                                onSuccess = {
                                    Toast.makeText(context, "Rendelés sikeresen leadva!", Toast.LENGTH_LONG).show()
                                    onOrderSuccess()
                                },
                                onError = { hiba ->
                                    Toast.makeText(context, hiba, Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3000F))
                    ) {
                        Text("MEGRENDELÉS", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        if (cart.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("A kosarad üres.", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(cart) { cartItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cartItem.iceCream.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (cartItem.extras.isNotEmpty()) {
                                    val extrasText = cartItem.extras.joinToString { it.name }
                                    Text(text = "+ $extrasText", style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(
                                    text = "${cartItem.totalPrice.toInt()} €",
                                    color = Color(0xFFE3000F),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            IconButton(onClick = { viewModel.removeFromCart(cartItem) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Törlés", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}