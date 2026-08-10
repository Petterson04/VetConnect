package com.moviles.vetconnect.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.R
import com.moviles.vetconnect.components.Botones
import com.moviles.vetconnect.components.OutlinedInputs
import com.moviles.vetconnect.components.SelectorFecha
import com.moviles.vetconnect.components.SpaceBetween
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.TextosSimples
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.entities.Cita
import com.moviles.vetconnect.components.SelectorHora
import com.moviles.vetconnect.entities.Mascota
import com.moviles.vetconnect.entities.Usuario
import com.moviles.vetconnect.model.CitasViewModel
import com.moviles.vetconnect.model.MascotasViewModel
import com.moviles.vetconnect.model.PetsUiState
import com.moviles.vetconnect.model.UsuarioViewModel
import com.moviles.vetconnect.model.VeterinarianUiState
import com.moviles.vetconnect.ui.theme.AzulClaro
import com.moviles.vetconnect.ui.theme.Gris
import com.moviles.vetconnect.ui.theme.Verde
import com.moviles.vetconnect.views.ui.theme.VETCONNECTTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.moviles.vetconnect.components.BotonRegresar

class AgregarCita : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_CALENDAR),
                100
            )
        }
        setContent {
            VETCONNECTTheme {
                AgregarCitaViews()
            }
        }
    }
}

@Composable
fun AgregarCitaViews() {
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var razon by remember { mutableStateOf("") }
    var userId = FirebaseAuth.getInstance().currentUser?.uid
    var showDialog by remember {
        mutableStateOf(false)
    }
    var showHora by remember {
        mutableStateOf(false)
    }
    val state = rememberDatePickerState()
    val veterinario = remember { mutableStateOf<Usuario?>(null) }
    val mascota = remember { mutableStateOf<Mascota?>(null) }
    val context= LocalContext.current


    Box(modifier = Modifier
        .fillMaxSize()

        .background(AzulClaro)
    ){

        SpaceTopBottom(15)
        BotonRegresar()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ){
            Titulo("Agregar Cita")
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Ingresa la fecha",Gris)
                SpaceBetween(10)
                OutlinedButton(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Verde,
                        contentColor = Color.White
                    )
                ) {
                    Row() {
                        TextosSimples("fecha", Color.White,)
                        SpaceBetween(15)
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                            contentDescription = "Imagen de calendario"

                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextosSimples("Ingresa la hora", Gris)
                SpaceBetween(10)
                OutlinedButton(
                    onClick = { showHora = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Verde,
                        contentColor = Color.White
                    )
                ) {
                    Row() {
                        TextosSimples("hora", Color.White,)
                       SpaceBetween(10)
                        Icon(
                            painter = painterResource(id = R.drawable.time2),
                            contentDescription = "Imagen de calendario"

                        )
                    }
                }
            }
            SpaceTopBottom(15)
            TextosSimples("Veterinario", Gris)
            VeterinarianDropdown(
                    veterinario.value?.nombre?: "Selecciona un veterinario",
                {selectedVet ->
                    veterinario.value = selectedVet
                }
                )
            Log.d("------------------>>>> UserId: ", veterinario.value?.id.toString())
            Log.d("------------------>>>> Vet: ", veterinario.value?.nombre.toString())
            Log.d("------------------>>>> Mascota: ", mascota.value?.id.toString())
            Log.d("------------------>>>> Mascota: ", mascota.value?.nombre.toString())
            SpaceTopBottom(15)
            TextosSimples("Mascota", Gris)
            MascotasDropdown(mascota.value?.nombre?: "Selecciona una mascota",
                {selectedPet ->
                    mascota.value = selectedPet
                })
            TextosSimples("Razón de la cita", Gris)
            OutlinedInputs("Razón", razon) { razon = it }
            SpaceTopBottom(15)
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                state.selectedDateMillis?.let { millis ->
                                    val formatter =
                                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    formatter.timeZone= TimeZone.getTimeZone("UTC")
                                    fecha = formatter.format(Date(millis))
                                }
                                Log.d("------------------>>>> Feccha: ", fecha)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Verde,
                                contentColor = Color.White
                            )
                        ) {
                            TextosSimples("Confirmar", Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showDialog = false }) {
                            TextosSimples("Cancelar", Verde)
                        }
                    }

                ) {
                    DatePicker(state = state)
                }
            }
            if (showHora) {
                SelectorHora() { hora = it }

            }
            SpaceTopBottom(15)
            Botones("Agregar Cita") {
                val cita = Cita(
                    "",
                    userId.toString(),
                    mascota.value?.id.toString(),
                    veterinario.value?.id.toString(),
                    mascota.value?.nombre.toString(),
                    veterinario.value?.nombre.toString(),
                    fecha,
                    hora,
                    razon
                )
                CitasViewModel().AñadirCita(
                    cita,
                    onComplete = {
                        if (it) {
                            agregarEventoCalendario(
                                context,
                                "Cita con ${veterinario.value?.nombre}",
                                "Cita con ${mascota.value?.nombre}",
                                System.currentTimeMillis()
                            )
                        }
                    }
                )

            }

        }
    }
   
}



@Composable
fun VeterinarianDropdown(
    selectedVet: String,
    onVetSelected: (Usuario) -> Unit
) {
    val viewModel: UsuarioViewModel = viewModel()
    val vetState by viewModel.vetState.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedVet) }

    // Cargar veterinarios SOLO UNA VEZ
    LaunchedEffect(Unit) {
        viewModel.getVeterinarians()
    }

    when (vetState) {
        is VeterinarianUiState.Loading -> Text("Cargando veterinarios...")

        is VeterinarianUiState.Success -> {
            val vets = (vetState as VeterinarianUiState.Success).veterinarians

            Box {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona veterinario") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                painterResource(id = R.drawable.outline_arrow_downward_24),
                                contentDescription = null
                            )
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    vets.forEach { vet ->
                        DropdownMenuItem(
                            text = { Text(vet.nombre) },
                            onClick = {
                                selectedText = vet.nombre
                                onVetSelected(vet)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        is VeterinarianUiState.Error -> Text("Error al cargar veterinarios")
    }
}



@Composable
fun MascotasDropdown(
    selectedPet: String,
    onPetSelected: (Mascota) -> Unit
) {
    val viewModel: MascotasViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val pets by viewModel.pets.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedPet) }

    // Cargar mascotas SOLO UNA VEZ
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.CargarMascotas(userId)
        }
    }

    Box {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecciona mascota") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painterResource(id = R.drawable.outline_arrow_downward_24),
                        contentDescription = null
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            pets.forEach { pet ->
                DropdownMenuItem(
                    text = { Text(pet.nombre) },
                    onClick = {
                        selectedText = pet.nombre
                        onPetSelected(pet)
                        expanded = false
                    }
                )
            }
        }
    }
}


fun agregarEventoCalendario(context: Context, titulo: String, descripcion: String, fecha: Long) {
    val calIntent = Intent(Intent.ACTION_INSERT).apply {
        type = "vnd.android.cursor.item/event"
        putExtra(CalendarContract.Events.TITLE, titulo)
        putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fecha)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, fecha + 60 * 60 * 1000) // 1 hora después
        putExtra(CalendarContract.Reminders.MINUTES, 1440) // 24 horas antes (en minutos)
        putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
    }
    context.startActivity(calIntent)
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    VETCONNECTTheme {
        AgregarCitaViews()
    }
}