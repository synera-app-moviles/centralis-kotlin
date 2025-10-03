package com.example.centralis_kotlin.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomDropDownMenu(
    label: String,
    placeholder: String,
    selectedOption: T?,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
    getDisplayText: (T) -> String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        // Label
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = label,
                color = Color(0xFFFFFFFF),
                fontSize = 16.sp,
            )
        }
        
        // Dropdown container
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .menuAnchor()
                    .clip(shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF302149),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .clickable { expanded = !expanded }
            ) {
                Text(
                    text = selectedOption?.let { getDisplayText(it) } ?: placeholder,
                    style = TextStyle(
                        color = if (selectedOption != null) Color(0xFFFFFFFF) else Color(0xFFFFFFFF).copy(alpha = 0.6f),
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown arrow",
                    tint = Color(0xFFFFFFFF)
                )
            }
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color(0xFF302149))
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = getDisplayText(option),
                                color = Color(0xFFFFFFFF),
                                fontSize = 16.sp
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        modifier = Modifier.background(Color(0xFF302149))
                    )
                }
            }
        }
    }
}