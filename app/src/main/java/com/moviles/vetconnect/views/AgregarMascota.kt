package com.moviles.vetconnect.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.BotonRegresar
import com.moviles.vetconnect.components.Botones
import com.moviles.vetconnect.components.OutlinedInputs
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.views.ui.theme.VETCONNECTTheme
import com.moviles.vetconnect.entities.Mascota
import com.moviles.vetconnect.model.LoginViewModel
import com.moviles.vetconnect.model.MascotasViewModel
import com.moviles.vetconnect.model.UsuarioViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro


class AgregarMascota : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VETCONNECTTheme {
                AgregarMascotaView()
            }
        }
    }
}

@Composable
fun AgregarMascotaView() {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    val vm= MascotasViewModel()
    val userId= FirebaseAuth.getInstance().currentUser?.uid






    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()

        .background(AzulClaro)
    ){
        BotonRegresar()
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ){
            Titulo("Agregar Mascota")
            SpaceTopBottom(10)
            OutlinedInputs("nombre", nombre) { nombre = it }
            SpaceTopBottom(10)
            OutlinedInputs("tipo", tipo) { tipo = it }
            SpaceTopBottom(10)
            OutlinedInputs("raza", raza) { raza = it }
            SpaceTopBottom(10)
            OutlinedInputs("fechaNacimiento", fechaNacimiento) { fechaNacimiento = it }
            SpaceTopBottom(10)
            OutlinedInputs("sexo", sexo) { sexo = it }
            SpaceTopBottom(10)
            OutlinedInputs("peso", peso) { peso = it }
            SpaceTopBottom(10)
            OutlinedInputs("color", color) { color = it }
            SpaceTopBottom(10)
            Botones("Agregar Mascota") {
                val mascota = Mascota(
                    "",
                    userId.toString(),
                    nombre,
                    tipo,
                    raza,
                    fechaNacimiento,
                    sexo,
                    peso,
                    color
                )
                vm.AñadirMascota(mascota)
                context.startActivity(Intent(context, Mascotas::class.java))
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    VETCONNECTTheme {
        AgregarMascotaView()

    }
}