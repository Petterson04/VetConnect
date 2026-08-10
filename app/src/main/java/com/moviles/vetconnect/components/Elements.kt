package com.moviles.vetconnect.components




import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.Font
import com.moviles.vetconnect.R
import com.moviles.vetconnect.ui.theme.Verde
import com.moviles.vetconnect.ui.theme.Gris
import com.moviles.vetconnect.ui.theme.negro
import com.moviles.vetconnect.ui.theme.AzulClaro

import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment


@Composable
fun Botones(
    Text: String,
    onClickAction: () -> Unit,
){
    OutlinedButton(
        onClick = {
            onClickAction()
        },
        modifier = Modifier
            .padding(15.dp)
            .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
            .width(450.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Verde,
            contentColor = Gris
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text = Text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun BotonRegresar(modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    IconButton(
        onClick = {
            if (context is Activity) {
                context.finish() // Termina la Activity actual
            }
        },
        modifier = modifier
            .size(100.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_arrow_back_24), // Usa el ícono que tengas
            contentDescription = "Regresar",
            modifier= Modifier.size(75.dp)

        )
    }
}

@Composable
fun TextosSimples(text: String?, color: Color)
{
    if (text != null) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp
        )
    }
}
@Composable
fun TextosInformacion(text: String, color: Color){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.ExtraBold,
        fontStyle = FontStyle.Italic,

        fontSize = 30.sp
    )
}
@Composable
fun OutlinedInputs(title: String, text: String, onValueChange: (String) -> Unit)
{
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            TextosSimples(text = title, Gris )
        },
        modifier = Modifier
            .width(350.dp)
        ,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            cursorColor = Color.White
        )
    )
}
@Composable
fun Titulo(text: String) {
    Text(
        text = text,
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold,
        color = negro,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily.Cursive
    )
}

@Composable
fun SpaceTopBottom(dimension: Int) {
    Spacer(modifier = Modifier.height(dimension.dp))
}

/*Son los espacios que hay entre elementos*/
@Composable
fun SpaceBetween(dimension: Int) {
    Spacer(modifier = Modifier.width(dimension.dp))
}

@Composable
fun MiDialogoSimple(
    mostrarDialogo: Boolean,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("¿Estás seguro?")
            },
            text = {
                Text("Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Botones("Confirmar"){onConfirmar() }
            },
            dismissButton = {
                Botones("Cancelar") {onDismiss() }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFecha(
    fechaSeleccionada: String,
    onFechaSeleccionada: (String) -> Unit
) {
    val contexto = LocalContext.current
    val calendario = Calendar.getInstance()

    val year = calendario.get(Calendar.YEAR)
    val month = calendario.get(Calendar.MONTH)
    val day = calendario.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = remember {
        DatePickerDialog(
            contexto,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Formatea la fecha como dd/MM/yyyy
                val fechaFormateada = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                onFechaSeleccionada(fechaFormateada)
            },
            year,
            month,
            day
        )
    }
    Button(onClick = {
        datePickerDialog.show()
    }) {
        Row {
            Text(text = "Seleccionar fecha")
            SpaceBetween(5)
            Icon(   painter = painterResource(id = R.drawable.outline_calendar_month_24),
                contentDescription = "Calendario",
                tint = Color.White)

        }
    }

}



/* Permite seleccionar una hora y mostrarla en la pantalla */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHora(
    onHoraSeleccionada: (String) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    val horaFormateada = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)

    // Actualiza el valor en cada recomposición (opcional)
    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        onHoraSeleccionada(horaFormateada)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Seleccionar hora/ 24 hrs",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            SpaceTopBottom(10)

            TimeInput(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = Color.Green,
                    clockDialSelectedContentColor = Color.Black,
                    clockDialUnselectedContentColor = Color.Yellow,
                    periodSelectorSelectedContainerColor = Color.Green,
                    periodSelectorUnselectedContainerColor = Color.Yellow
                )
            )

            SpaceTopBottom(10)

            Text(
                text = "Hora seleccionada: $horaFormateada",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }}

