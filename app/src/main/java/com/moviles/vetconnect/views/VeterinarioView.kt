package com.moviles.vetconnect.views

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.entities.Cita
import com.moviles.vetconnect.model.AppointmentUiState
import com.moviles.vetconnect.model.CitasViewModel
import com.moviles.vetconnect.model.UsuarioViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro
import com.moviles.vetconnect.views.ui.theme.VETCONNECTTheme

class VeterinarioView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VETCONNECTTheme {
                VeterinarioViews()
            }
        }
            }
        }

    @Composable
    fun VeterinarioViews() {
        val viewModelCita: CitasViewModel = viewModel()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val uiState by viewModelCita.uiState.collectAsState()

        //Colectar el nombre
        val viewModel:UsuarioViewModel= viewModel()
        val userName = viewModel.userName.collectAsState()




        LaunchedEffect(userId) {
            if (userId != null) {
                viewModel.loadUserName(userId)
            }
        }
        LaunchedEffect(userId) {
            if (userId != null) {
                viewModelCita.getAppointmentsByVeterinarian(userId)
            }
        }





        Box(modifier = Modifier
            .fillMaxSize()

            .background(AzulClaro)
        ) {
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()

            ) {
                SpaceTopBottom(100)
                Titulo("Hola ${userName.value}")

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
                                    AppointmentItemVet(appointment = appointment, viewModel = viewModelCita, clientId = userId)
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
fun AppointmentItemVet(
    appointment: Cita,
    viewModel: CitasViewModel = viewModel(),
    clientId: String?
) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Mascota: ${appointment.nombreMascota}")
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
                            viewModel.getAppointmentsByVeterinarian(clientId.toString())
                        }
                    }
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }

            }
        }
    }
}

