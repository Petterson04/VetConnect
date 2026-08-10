package com.moviles.vetconnect.model

import androidx.compose.ui.util.fastMapNotNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.moviles.vetconnect.entities.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
sealed class VeterinarianUiState {
    object Loading : VeterinarianUiState()
    data class Success(val veterinarians: List<Usuario>) : VeterinarianUiState()
    data class Error(val message: String) : VeterinarianUiState()
}
class UsuarioViewModel: ViewModel() {

    private val db = Firebase.firestore

    private var listausuario = MutableStateFlow<List<Usuario>>(emptyList())
    var _listausuarios = listausuario.asStateFlow()


    init {

        ObtnerUsuarios()

    }



    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    fun loadUserName(userId: String) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nombre = document.getString("nombre") ?: ""
                    _userName.value = nombre
                }
            }
    }


    fun ObtnerUsuarios() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = db.collection("users")
                .get()
                .await()
            val usuarios = result.documents.fastMapNotNull {
                it.toObject(Usuario::class.java)
            }
            listausuario.value = usuarios
        }
    }

    private val _vetState = MutableStateFlow<VeterinarianUiState>(VeterinarianUiState.Loading)
    val vetState: StateFlow<VeterinarianUiState> = _vetState

    // Obtener veterinarios
    fun getVeterinarians() {
        _vetState.value = VeterinarianUiState.Loading
        db.collection("users")
            .whereEqualTo("rol", "Veterinario")
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { it.toObject(Usuario::class.java)?.copy(id = it.id) }
                _vetState.value = VeterinarianUiState.Success(list)
            }
            .addOnFailureListener { e ->
                _vetState.value = VeterinarianUiState.Error(e.message ?: "Error desconocido")
            }
    }
}








