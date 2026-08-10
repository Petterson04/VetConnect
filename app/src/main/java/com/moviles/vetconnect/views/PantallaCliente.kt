package com.moviles.vetconnect.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.vetconnect.components.Botones
import com.moviles.vetconnect.components.OutlinedInputs
import com.moviles.vetconnect.components.SpaceTopBottom
import com.moviles.vetconnect.components.Titulo
import com.moviles.vetconnect.model.UsuarioViewModel
import com.moviles.vetconnect.ui.theme.AzulClaro
import com.moviles.vetconnect.views.ui.theme.VETCONNECTTheme


@Composable
fun ClienteViews() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val context = LocalContext.current

    val viewModel:UsuarioViewModel= viewModel()
    val userName = viewModel.userName.collectAsState()

    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.loadUserName(userId)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulClaro)
    ) {
        SpaceTopBottom(100)
        cerrar()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Titulo("Hola ${userName.value}")
            Botones("Mascotas") {
                val mascotas= Intent(context, Mascotas::class.java)
                context.startActivity(mascotas)
            }
            Botones("Citas"){
                val citas= Intent(context, Citas::class.java)
                context.startActivity(citas)
            }
        }

    }
}
@Composable
fun cerrar(){
    val context = LocalContext.current
    OutlinedButton(
        onClick = {FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, Login::class.java)
            context.startActivity(intent)},
        modifier =
            Modifier.size(150.dp)
            .wrapContentSize()
            .border(2.dp, Color.Transparent, RoundedCornerShape(12.dp))
            .width(450.dp)
            .height(45.dp)
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White
        ),
        shape = RectangleShape
    ) {
        Text(
            "Cerrar sesión", color = Color.White, modifier = Modifier.padding(5.dp),

            )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewCerrarSesionButton() {
    cerrar()
}

