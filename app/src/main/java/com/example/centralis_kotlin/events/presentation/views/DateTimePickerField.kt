package com.example.centralis_kotlin.events.presentation.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.foundation.clickable
import java.util.Calendar
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun DateTimePickerField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    val themedContext = android.view.ContextThemeWrapper(context, com.example.centralis_kotlin.R.style.CustomDatePickerDialog)
    val calendar = Calendar.getInstance()

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text("Fecha y hora", color = Color(0xFFA68FCC)) },
        trailingIcon = {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier.clickable {
                    DatePickerDialog(
                        themedContext,
                        { _, year, month, day ->
                            TimePickerDialog(
                                themedContext,
                                { _, hour, minute ->
                                    val fecha = "%04d-%02d-%02dT%02d:%02d:00".format(
                                        year, month + 1, day, hour, minute
                                    )
                                    onValueChange(fecha)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                            ).show()
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            )
        },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF30214A),
            unfocusedContainerColor = Color(0xFF30214A),
            focusedTextColor = Color(0xFFA68FCC),
            unfocusedTextColor = Color(0xFFA68FCC),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}