package com.moviles.vetconnect.views

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.BotonRegresar
import com.moviles.vetconnect.components.Botones
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.entities.Cita
import com.moviles.vetconnect.model.AppointmentUiState
import com.moviles.vetconnect.model.CitasViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro
import com.moviles.vetconnect.views.ui.theme.VETCONNECTTheme

class Citas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VETCONNECTTheme {
                AgregarCita(CitasViewModel())
            }
        }
    }
}

@Composable
fun AgregarCita(viewModel: CitasViewModel = viewModel()) {
    val context = LocalContext.current
    val clientId = FirebaseAuth.getInstance().currentUser?.uid
    val uiState by viewModel.uiState.collectAsState()

    // Cargar citas al iniciar el Composable
    clientId?.let {
        viewModel.getAppointmentsByClient(it)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulClaro)
    ) {
        BotonRegresar()
        SpaceTopBottom(100)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SpaceTopBottom(100)
            Titulo("Citas agendadas")
            SpaceTopBottom(10)
            Botones("Agregar Nueva Cita") {
                val citas = Intent(
                    context,
                    AgregarCita::class.java
                )
                context.startActivity(citas)
            }
            SpaceTopBottom(10)

            when (uiState) {
                is AppointmentUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AppointmentUiState.Success -> {
                    val appointments = (uiState as AppointmentUiState.Success).appointments

                    if (appointments.isEmpty()) {
                        Text("No tienes citas programadas")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(appointments) { appointment ->
                                AppointmentItem(appointment = appointment, viewModel = viewModel, clientId = clientId)
                            }
                        }
                    }
                }

                is AppointmentUiState.Error -> {
                    Text("Error: ${(uiState as AppointmentUiState.Error).message}")
                }
            }
        }
    }
}

@Composable
fun AppointmentItem(
    appointment: Cita,
    viewModel: CitasViewModel,
    clientId: String?
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Mascota: ${appointment.nombreMascota}")
            Text("Veterinario: ${appointment.nombreVeterinario}")
            Text("Fecha: ${appointment.fecha}")
            Text("Hora: ${appointment.hora}")
            Text("Razón: ${appointment.razon}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    viewModel.deleteAppointment(appointment.id) { success ->
                        if (success) {
                            viewModel.getAppointmentsByClient(clientId)
                        }
                    }
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    VETCONNECTTheme {
        AgregarCita(CitasViewModel())

    }
}