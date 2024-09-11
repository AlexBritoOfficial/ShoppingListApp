package com.example.shoppinglist.composables

import android.Manifest
import android.content.Context
import android.location.Address
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.emptyLongSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shoppinglist.LocationViewModel
import com.example.shoppinglist.MainActivity
import com.example.shoppinglist.model.ShoppingItem
import com.example.shoppinglist.utils.LocationUtils
import java.util.UUID


@Composable
fun ShoppingListApp(locationUtils: LocationUtils,
                    locationViewModel: LocationViewModel,
                    navController: NavController,
                    context: Context,
                    address: String) {

    var stateShoppingListItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    // Permissions Activity Launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                ) {
                    locationUtils.requestLocaionUpdates(locationViewModel = locationViewModel)
                } else {
                    val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) || ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                    if (rationaleRequired) {
                        Toast.makeText(
                            context,
                            "Location Permission is required for this feature to work",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Location Permission is required, please enable in Android Settings",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(100.dp)
        ) {
            Text(text = "Add Item")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(stateShoppingListItems) { shoppingItem ->

                if(shoppingItem.isEditing){
                    ShoppingItemEditor(shoppingItem = shoppingItem, onEditComplete =  { editedName, editedQuantity ->
                        stateShoppingListItems = stateShoppingListItems.map { it.copy(isEditing = false) }

                        val editedItem = stateShoppingListItems.find { it.id.equals(shoppingItem.id) }
                        editedItem?.let {
                            it.itemName = editedName
                            it.quantity = editedQuantity
                            it.address = address
                        }
                    })
                }

                else {
                    ShoppingListItem(shoppingItem, isEditClick = {
                        stateShoppingListItems = stateShoppingListItems.map { it.copy(isEditing = it.id == shoppingItem.id) }
                    }, onDeleteClick = {
                        stateShoppingListItems = stateShoppingListItems - shoppingItem
                    })
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        if (itemName.isNotBlank() && itemQuantity.isNotBlank()) {
                            val shoppingItem = ShoppingItem(
                                id = stateShoppingListItems.size + 1,
                                itemName = itemName,
                                quantity = itemQuantity.toInt(),
                                address = address
                            )

                            stateShoppingListItems = stateShoppingListItems + shoppingItem
                            showDialog = false;
                            itemName = ""
                            itemQuantity = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            },
            title = { Text(text = "Add Shopping Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        placeholder = { Text(text = "Item Name")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        placeholder = { Text(text = "Item Quantity")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    Button(onClick = {
                        if(locationUtils.hasLocationEnabled(context)){
                            locationUtils.requestLocaionUpdates(locationViewModel)

                            navController.navigate("locationscreen"){
                                this.launchSingleTop
                            }
                        }
                        else{
                            requestPermissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }
                    }) {
                        Text(text = "Address")
                    }
                }
            }

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListAppPreview() {

}