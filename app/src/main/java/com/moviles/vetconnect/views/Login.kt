package com.moviles.vetconnect.views

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.vetconnect.components.*
import com.moviles.vetconnect.model.LoginViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro
import com.moviles.vetconnect.ui.theme.Gris
import com.moviles.vetconnect.ui.theme.VETCONNECTTheme


class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VETCONNECTTheme {
              NavGraphs()
            }
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavHostController? = null,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 👇 Observa el uiState para que Compose se redibuje
    val ui by remember { derivedStateOf { viewModel.uiState } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulClaro)
    ) {
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Titulo("Login")
            TextosSimples("Correo", Gris)
            OutlinedInputs("Correo", email) { email = it }
            TextosSimples("Contraseña", Gris)
            OutlinedInputs("Contraseña", password) { password = it }

            SpaceTopBottom(10)

            Botones("Iniciar Sesión") {
                viewModel.login(email, password)
                Log.d("LoginScreen", "Email: $email, Password: $password")
                Log.d("LoginScreen", "UI State: $ui")
            // solo llama login
            }

            // 👇 Muestra error si existe
            if (ui.error != null) {
                Text(text = ui.error!!, color = Color.Red)
            }

        }
    }

    // 🔥 Navegación según rol
    LaunchedEffect(ui.success, ui.role) {
        if (ui.success && ui.role != null && navController != null) {
            when (ui.role) {
                "Veterinario" -> navController.navigate("pantalla_vet") {
                    popUpTo("login") { inclusive = true }
                }
                "Cliente" -> navController.navigate("pantalla_cliente") {
                    popUpTo("login") { inclusive = true }
                }
                else -> navController.navigate("pantalla_generica") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}


@Composable
fun NavGraphs() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }
        composable("pantalla_vet") {
            VeterinarioViews()
        }
        composable("pantalla_cliente") {
            ClienteViews()
        }
        composable("pantalla_generica") {
            PantallaGenerica()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    VETCONNECTTheme {
        NavGraphs()

    }
}