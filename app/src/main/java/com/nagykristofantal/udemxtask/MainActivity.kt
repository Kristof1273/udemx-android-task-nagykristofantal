package com.nagykristofantal.udemxtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nagykristofantal.udemxtask.data.IceCream
import com.nagykristofantal.udemxtask.ui.screens.CartScreen
import com.nagykristofantal.udemxtask.ui.screens.ExtrasScreen
import com.nagykristofantal.udemxtask.ui.screens.IceCreamItem
import com.nagykristofantal.udemxtask.ui.theme.Udemx_taskTheme
import com.nagykristofantal.udemxtask.viewmodel.IceCreamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Udemx_taskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val viewModel: IceCreamViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "list_screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("list_screen") {
                            IceCreamListScreen(
                                viewModel = viewModel,
                                onNavigateToExtras = { iceCream ->
                                    viewModel.selectIceCream(iceCream)
                                    navController.navigate("extras_screen")
                                },
                                onNavigateToCart = {
                                    navController.navigate("cart_screen")
                                }
                            )
                        }

                        composable("extras_screen") {
                            ExtrasScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("cart_screen") {
                            CartScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() },
                                onOrderSuccess = {
                                    navController.popBackStack("list_screen", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IceCreamListScreen(
    modifier: Modifier = Modifier,
    viewModel: IceCreamViewModel = viewModel(),
    onNavigateToExtras: (IceCream) -> Unit,
    onNavigateToCart: () -> Unit
) {
    val iceCreams by viewModel.iceCreams.collectAsState()
    val basePrice by viewModel.basePrice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val cart by viewModel.cart.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3000F))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "GRANDMA'S",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "HOMEMADE ICE CREAM",
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.sortIceCreamsByStatus() }) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Rendezés",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { onNavigateToCart() }) {
                    BadgedBox(
                        badge = {
                            if (cart.isNotEmpty()) {
                                Badge {
                                    Text(cart.size.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.cart_outline),
                            contentDescription = "Kosár",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
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
                                    onNavigateToExtras(iceCream)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}