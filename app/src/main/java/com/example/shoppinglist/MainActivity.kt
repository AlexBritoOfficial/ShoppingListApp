package com.example.shoppinglist

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.composables.LocationSelectionScreen
import com.example.shoppinglist.composables.ShoppingListApp
import com.example.shoppinglist.model.LocationData
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import com.example.shoppinglist.utils.LocationUtils

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)

                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val locationViewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController = navController, startDestination = "shoppinglistscreen") {
        composable("shoppinglistscreen") {
            ShoppingListApp(
                locationUtils = locationUtils,
                locationViewModel = locationViewModel,
                navController = navController,
                context = context,
                address = locationViewModel.address.value.firstOrNull()?.formatted_address
                    ?: "No Address"
            )
        }

        dialog("locationscreen") { navBackStackEntry ->
            locationViewModel.location.value?.let { it1 ->

                LocationSelectionScreen(locationData = it1, onLocationSelected = { locationData ->
                    locationViewModel.fetchAddress("${locationData.latitude},${locationData.longitude}")
                    navController.popBackStack()
                })
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingList() {
    ShoppingListTheme {
        // ShoppingListApp()
    }
}