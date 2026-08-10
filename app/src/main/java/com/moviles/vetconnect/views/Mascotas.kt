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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.BotonRegresar
import com.moviles.vetconnect.components.Botones
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.model.MascotasViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro

class Mascotas : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                PetListScreen(MascotasViewModel())
        }

        }
    }

@Composable
fun PetListScreen(viewModel: MascotasViewModel ) {
    val userId= FirebaseAuth.getInstance().currentUser?.uid
    val pets by viewModel.pets.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(userId) {
        viewModel.CargarMascotas(userId)
    }
    Box(modifier = Modifier
        .fillMaxSize()

        .background(AzulClaro)
    ) {
        BotonRegresar()
        SpaceTopBottom(100)
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ) {
            SpaceTopBottom(100)
            Titulo("Lista de Mascotas")
            Botones("Agregar Mascota") {
                val mascotas= Intent(context, AgregarMascota::class.java)
                context.startActivity(mascotas)
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(pets) { pet ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),

                        ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nombre: ${pet.nombre}")
                            Text("Tipo: ${pet.tipo}")
                            Text("Edad: ${pet.raza}")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                viewModel.eliminarMascota(pet.id) {
                                    viewModel.CargarMascotas(userId)
                                }
                            }) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.error)
                            }

                        }
                    }
                }
            }
        }
    }
}

