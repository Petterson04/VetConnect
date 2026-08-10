package com.moviles.vetconnect.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moviles.vetconnect.ui.theme.VETCONNECTTheme
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.moviles.vetconnect.ui.theme.Gris
import com.moviles.vetconnect.R
import com.moviles.vetconnect.entities.Usuario
import com.moviles.vetconnect.model.LoginViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro
import kotlin.jvm.java
import com.moviles.vetconnect.views.Login
import com.moviles.vetconnect.model.UsuarioViewModel
import com.moviles.vetconnect.ui.theme.Verde
import java.time.LocalDate
import java.time.format.DateTimeFormatter



class Registro : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VETCONNECTTheme {
                Registra()
            }
        }
    }
}

@Composable
fun Registra(){
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var contraseñaVisible by remember { mutableStateOf(false) }
    var rol by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    val vm = UsuarioViewModel()
    val vmLogin= LoginViewModel()

    var passwordVisible by remember { mutableStateOf(false) }
    val errores = validarcontrasena(contrasena)

    val enabled = errores.isEmpty()






    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()

        .background(AzulClaro)
    ){
        Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Titulo("Registrar usuario")
            SpaceTopBottom(5)
            OutlinedInputs("Nombre", nombre) { nombre = it }
            SpaceTopBottom(5)
            OutlinedInputs("Correo", correo) { correo = it }
            SpaceTopBottom(5)
            OutlinedInputs("Numero", numero) { numero = it }
            SpaceTopBottom(5)
            TextosSimples("Contraseña",Gris)
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Ingresar contrasena", color = Color.White, ) },

                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password),
                isError= errores.isNotEmpty(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (passwordVisible) "Ocultar contrasena" else "Mostrar contrasena",
                            tint = Color.White
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(15.dp) )
            if (errores.isNotEmpty()) {
                Column {
                    errores.forEach { error ->
                        Text(text = "❌ $error", color = Color.Red,)
                    }
                }
            }
            SpaceTopBottom(5)
            TextosSimples("Rol",Gris)
            RoleSpinner(selectedRol= rol, onRoleSelect={rol=it})
            SpaceTopBottom(5)
            val fechaAcual: String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val usuario = Usuario(
                id = "",
                nombre = nombre,
                email = correo,
                numero = numero,
                rol = rol,
                fechaRegistro =fechaAcual )
            OutlinedButton(
                onClick = {
                    vmLogin.registrar(usuario,contrasena){ success, error ->
                        if (success) {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, Login::class.java))

                        } else {
                            val errorMsg = ""
                            Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Verde,
                    contentColor = Gris
                ),
                enabled = enabled
                    ){
                TextosSimples("Registrar", Gris)
            }


            SpaceTopBottom(5)
            Text("¿Ya tienes una cuenta? Inicia sesion",
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, Login::class.java))
                    },
                color = Color.White
            )




        }
    }
}
fun validarcontrasena(contrasena: String): List<String> {
    val errores = mutableListOf<String>()
    if (!contrasena.any { it.isUpperCase() }) errores.add("Debe incluir al menos una letra mayúscula.")
    if(!contrasena.any(){it.isLowerCase()})errores.add("Debe incluir al menos una letra miniscula")
    if (!contrasena.any { it.isDigit() }) errores.add("Debe incluir al menos un número.")
    return errores
}
@Composable
fun RoleSpinner(
    selectedRol: String?,
    onRoleSelect: (String) -> Unit,
){
    val roles= listOf("Veterinario","Cliente")
    var expanded by remember { mutableStateOf(false) }
    Box(){
    if (selectedRol != null) {
        OutlinedTextField(
            value = selectedRol,
            onValueChange = {},
            readOnly = true,
            label = { Text("Selecciona un rol") },
            modifier = Modifier
                .fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painterResource(id = R.drawable.outline_arrow_downward_24),
                        contentDescription = null
                    )
                }
                }
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = true },

    ){
        roles.forEach {role->
            DropdownMenuItem(
                onClick = {
                    onRoleSelect(role)
                    expanded = false
                },
                text = { Text(text = role) },

                )

        }
    }
}
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VETCONNECTTheme {
        Registra()
    }
}