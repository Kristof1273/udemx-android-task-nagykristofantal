package com.nagykristofantal.udemxtask.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nagykristofantal.udemxtask.data.CartItem
import com.nagykristofantal.udemxtask.viewmodel.IceCreamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtrasScreen(
    viewModel: IceCreamViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedIceCream by viewModel.selectedIceCream.collectAsState()
    val extrasCategories by viewModel.extras.collectAsState()
    val basePrice by viewModel.basePrice.collectAsState()

    var selectedExtras by remember { mutableStateOf(mapOf<String, Set<com.nagykristofantal.udemxtask.data.ExtraItem>>()) }

    LaunchedEffect(extrasCategories) {
        if (selectedExtras.isEmpty() && extrasCategories.isNotEmpty()) {
            extrasCategories.forEach { category ->
                if (category.type.contains("tölcsér", ignoreCase = true)) {
                    val defaultItem = category.items.find { it.name.contains("normál", ignoreCase = true) }
                    defaultItem?.let {
                        selectedExtras = selectedExtras + (category.type to setOf(it))
                    }
                }
            }
        }
    }


    val flatSelectedExtras = selectedExtras.values.flatten()
    val totalPrice = basePrice + flatSelectedExtras.sumOf { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Extrák kiválasztása") },
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
            Row(
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Összesen: ${totalPrice.toInt()} €",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = {
                        selectedIceCream?.let { iceCream ->
                            val cartItem = CartItem(
                                iceCream = iceCream,
                                extras = flatSelectedExtras,
                                totalPrice = totalPrice
                            )
                            viewModel.addToCart(cartItem)
                            onNavigateBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3000F))
                ) {
                    Text("KOSÁRBA", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = selectedIceCream?.name ?: "",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE3000F)
            )
            Text(text = "Alapár: ${basePrice.toInt()} €", modifier = Modifier.padding(bottom = 8.dp))

            HorizontalDivider()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(extrasCategories) { category ->
                    Text(
                        text = category.type,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )

                    val isMultiSelect = category.type.equals("Egyéb", ignoreCase = true)

                    category.items.forEach { item ->
                        val currentCategorySelections = selectedExtras[category.type] ?: emptySet()
                        val isSelected = currentCategorySelections.contains(item)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isMultiSelect) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { isChecked ->
                                        val newSelections = if (isChecked) {
                                            currentCategorySelections + item
                                        } else {
                                            currentCategorySelections - item
                                        }
                                        selectedExtras = selectedExtras + (category.type to newSelections)
                                    }
                                )
                            } else {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        val newSelections = if (isSelected) {
                                            emptySet()
                                        } else {
                                            setOf(item)
                                        }
                                        selectedExtras = selectedExtras + (category.type to newSelections)
                                    }
                                )
                            }

                            Text(
                                text = item.name,
                                modifier = Modifier.weight(1f)
                            )
                            Text(text = "+${item.price.toInt()} €")
                        }
                    }
                }
            }
        }
    }
}