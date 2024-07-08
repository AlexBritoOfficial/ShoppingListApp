package com.example.shoppinglist.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.model.ShoppingItem

@Composable
fun ShoppingItemEditor(
    shoppingItem: ShoppingItem,
    onEditComplete: (String, Int) -> Unit
) {

    var editedName by remember { mutableStateOf(shoppingItem.itemName) }
    var editedQTY by remember { mutableStateOf(shoppingItem.quantity.toString()) }
    var isEditing by remember { mutableStateOf(shoppingItem.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            // Edit Name
            BasicTextField(
                value = editedName,
                onValueChange = {
                    editedName = it
                },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            // Edit QTY
            BasicTextField(
                value = editedQTY,
                onValueChange = {
                    editedQTY = it
                },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        Button(onClick = {
            isEditing = false;
            onEditComplete(editedName, editedQTY.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }

}
