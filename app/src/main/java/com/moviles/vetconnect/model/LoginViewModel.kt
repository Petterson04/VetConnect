package com.moviles.vetconnect.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moviles.vetconnect.entities.Usuario
import com.moviles.vetconnect.entities.LoginUiState


class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // 🔹 Login
    fun login(email: String, password: String) {
        uiState = uiState.copy(loading = true, error = null)

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { doc ->
                        if (doc.exists()) {
                            val role = doc.getString("rol") ?: "Cliente"
                            uiState = uiState.copy(
                                success = true,
                                role = role,
                                loading = false
                            )
                        } else {
                            uiState = uiState.copy(
                                error = "Documento de usuario no encontrado",
                                loading = false
                            )
                        }
                    }
                    .addOnFailureListener {
                        uiState = uiState.copy(
                            error = it.message,
                            loading = false
                        )
                    }
            }
            .addOnFailureListener {
                uiState = uiState.copy(
                    error = it.message,
                    loading = false
                )
            }
    }



    // 🔹 Registro
    fun registrar(usuario: Usuario, contrasena: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(usuario.email, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    val userData = usuario.copy(id = uid)
                    firestore.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener { onResult(true, null) }
                        .addOnFailureListener { onResult(false, it.message) }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
