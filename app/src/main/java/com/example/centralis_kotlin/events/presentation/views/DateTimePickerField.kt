package com.example.centralis_kotlin.events.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import java.util.Calendar
import java.util.Locale


private enum class Mode { DATE, TIME }

@Composable
fun DateTimePickerField(
    value: String,
    onValueChange: (String) -> Unit,

    selectedColor: Color = Color(0xFFA68FCC),
    headerBackground: Color = Color(0xFF302149),
    textColor: Color = Color.White,
    minuteStep: Int = 1
) {
    val nowCal = Calendar.getInstance()

    val initial = remember(value) {
        try {
            if (value.isBlank()) null else {
                val parts = value.split('T')
                val datePart = parts.getOrNull(0)
                val timePart = parts.getOrNull(1)
                if (datePart == null) null else {
                    val dateParts = datePart.split('-')
                    val y = dateParts.getOrNull(0)?.toIntOrNull() ?: nowCal.get(Calendar.YEAR)
                    val m = dateParts.getOrNull(1)?.toIntOrNull() ?: (nowCal.get(Calendar.MONTH) + 1)
                    val d = dateParts.getOrNull(2)?.toIntOrNull() ?: nowCal.get(Calendar.DAY_OF_MONTH)
                    val hour = timePart?.split(':')?.getOrNull(0)?.toIntOrNull() ?: nowCal.get(Calendar.HOUR_OF_DAY)
                    val minute = timePart?.split(':')?.getOrNull(1)?.toIntOrNull() ?: nowCal.get(Calendar.MINUTE)
                    Quad(y, m, d, hour, minute)
                }
            }
        } catch (e: Exception) { null }
    }

    var showDialog by remember { mutableStateOf(false) }
    var selYear by remember { mutableStateOf(initial?.year ?: nowCal.get(Calendar.YEAR)) }
    var selMonth by remember { mutableStateOf(initial?.month ?: (nowCal.get(Calendar.MONTH) + 1)) } // 1-12
    var selDay by remember { mutableStateOf(initial?.day ?: nowCal.get(Calendar.DAY_OF_MONTH)) }
    var selHour by remember { mutableStateOf(initial?.hour ?: nowCal.get(Calendar.HOUR_OF_DAY)) }

    fun snapMinuteToStep(minute: Int, step: Int = minuteStep): Int {
        if (step <= 1) return minute.coerceIn(0, 59)
        val snapped = ((minute + step / 2) / step) * step
        return snapped.coerceIn(0, 60 - step)
    }
    var selMinute by remember {
        mutableStateOf(
            initial?.minute?.let { snapMinuteToStep(it) } ?: snapMinuteToStep(nowCal.get(Calendar.MINUTE))
        )
    }


    var mode by remember { mutableStateOf(Mode.DATE) }

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text("Fecha y hora", color = selectedColor) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { showDialog = true; mode = Mode.DATE }
                    .padding(4.dp),
                tint = selectedColor
            )
        },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF30214A),
            unfocusedContainerColor = Color(0xFF30214A),
            focusedTextColor = selectedColor,
            unfocusedTextColor = selectedColor,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFF170F24),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // header con navegación de meses
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(headerBackground)
                            .padding(8.dp)
                    ) {
                        IconButton(onClick = {
                            // retroceder un mes
                            if (selMonth == 1) { selMonth = 12; selYear -= 1 } else selMonth -= 1
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior", tint = textColor)
                        }
                        Text(
                            text = "${monthName(selMonth)} $selYear",
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                        )
                        IconButton(onClick = {
                            // avanzar un mes
                            if (selMonth == 12) { selMonth = 1; selYear += 1 } else selMonth += 1
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente", tint = textColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (mode == Mode.DATE) {
                        // calendario
                        CalendarGridByCalendar(
                            year = selYear,
                            month = selMonth,
                            selectedDay = selDay,
                            onDateSelected = { y, m, d -> selYear = y; selMonth = m; selDay = d },
                            selectedColor = selectedColor,
                            textColor = textColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showDialog = false }) { Text("Cancelar", color = selectedColor) }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { mode = Mode.TIME }) { Text("Siguiente") }
                        }
                    } else {
                        // vista de hora y minutos: minutos debajo de hora
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Hora", color = textColor)
                            NumberPickerRow(
                                range = (0..23).toList(),
                                selected = selHour,
                                onSelect = { selHour = it },
                                selectedColor = selectedColor,
                                textColor = textColor
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Minutos", color = textColor)
                            NumberPickerRow(
                                range = (0 until 60 step minuteStep).toList(),
                                selected = selMinute,
                                onSelect = { selMinute = it },
                                selectedColor = selectedColor,
                                textColor = textColor
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { mode = Mode.DATE }) { Text("Atrás", color = selectedColor) }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    val iso = String.format(Locale.getDefault(), "%04d-%02d-%02dT%02d:%02d:00", selYear, selMonth, selDay, selHour, selMinute)
                                    onValueChange(iso)
                                    showDialog = false
                                }) { Text("Aceptar") }
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun monthName(month: Int): String {
    return Calendar.getInstance().apply { set(Calendar.MONTH, month - 1) }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
}

@Composable
private fun CalendarGridByCalendar(
    year: Int,
    month: Int, // 1-12
    selectedDay: Int,
    onDateSelected: (Int, Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    startWithMonday: Boolean = true,
    selectedColor: Color = Color(0xFFA68FCC),
    textColor: Color = Color.White
) {
    val cal = Calendar.getInstance().apply { set(Calendar.YEAR, year); set(Calendar.MONTH, month - 1); set(Calendar.DAY_OF_MONTH, 1) }
    val monthLength = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstWeekDayValue = cal.get(Calendar.DAY_OF_WEEK) // 1=Sun..7=Sat
    val offset = if (startWithMonday) (if (firstWeekDayValue == Calendar.SUNDAY) 6 else firstWeekDayValue - 2) else (firstWeekDayValue - 1)
    val totalCells = ((offset + monthLength + 6) / 7) * 7

    Column(modifier = modifier) {
        val daysOfWeek = if (startWithMonday) listOf("L","M","X","J","V","S","D") else listOf("D","L","M","X","J","V","S")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            daysOfWeek.forEach { d ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(d, color = textColor, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        for (row in 0 until totalCells / 7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val idx = row * 7 + col
                    val dayNumber = idx - offset + 1
                    Box(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), contentAlignment = Alignment.Center) {
                        if (dayNumber in 1..monthLength) {
                            val isSelected = dayNumber == selectedDay
                            val bg = if (isSelected) selectedColor else Color.Transparent
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(bg).clickable { onDateSelected(year, month, dayNumber) }, contentAlignment = Alignment.Center) {
                                Text(text = dayNumber.toString(), color = if (isSelected) Color.White else textColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberPickerRow(
    range: List<Int>,
    selected: Int,
    onSelect: (Int) -> Unit,
    selectedColor: Color = Color(0xFFA68FCC),
    textColor: Color = Color.White
) {
    LazyRow(modifier = Modifier.height(56.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(range) { v ->
            val isSelected = v == selected
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(56.dp)
                    .height(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) selectedColor else Color.Transparent)
                    .clickable { onSelect(v) },
                contentAlignment = Alignment.Center
            ) {
                Text(text = String.format(Locale.getDefault(), "%02d", v), color = if (isSelected) Color.White else textColor)
            }
        }
    }
}

private data class Quad(val year: Int, val month: Int, val day: Int, val hour: Int, val minute: Int)