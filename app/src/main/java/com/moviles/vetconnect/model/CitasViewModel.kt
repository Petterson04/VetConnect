package com.moviles.vetconnect.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.vetconnect.entities.Cita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow

sealed class AppointmentUiState {
    object Loading : AppointmentUiState()
    data class Success(val appointments: List<Cita>) : AppointmentUiState()
    data class Error(val message: String) : AppointmentUiState()
}
class CitasViewModel: ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow<AppointmentUiState>(AppointmentUiState.Loading)
    val uiState: StateFlow<AppointmentUiState> = _uiState




    fun AñadirCita(appointment: Cita, onComplete: (Boolean) -> Unit) {
        val newRef = db.collection("citas").document()
        val appointmentWithId = appointment.copy(id = newRef.id)

        newRef.set(appointmentWithId)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
    fun deleteAppointment(appointmentId: String, onComplete: (Boolean) -> Unit) {
        db.collection("citas")
            .document(appointmentId)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
    // Editar cita
    fun updateAppointment(appointment: Cita, onComplete: (Boolean) -> Unit) {
        if (appointment.id.isEmpty()) {
            onComplete(false)
            return
        }

        db.collection("citas")
            .document(appointment.id)
            .set(appointment)  // Sobrescribe toda la cita, también puedes usar update() para campos específicos
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getAppointmentsByClient(clientId: String?) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading
            db.collection("citas")
                .whereEqualTo("idCliente", clientId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.documents.mapNotNull { it.toObject(Cita::class.java) }
                    _uiState.value = AppointmentUiState.Success(list)
                }
                .addOnFailureListener { e ->
                    _uiState.value = AppointmentUiState.Error(e.message ?: "Error desconocido")
                }
        }
    }
    fun getAppointmentsByVeterinarian(vetId: String) {
        viewModelScope.launch {
            _uiState.value = AppointmentUiState.Loading
            db.collection("citas")
                .whereEqualTo("idVeterinario", vetId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val list = snapshot.documents.mapNotNull { it.toObject(Cita::class.java) }
                    _uiState.value = AppointmentUiState.Success(list)
                }
                .addOnFailureListener { e ->
                    _uiState.value = AppointmentUiState.Error(e.message ?: "Error desconocido")
                }
        }
    }

    }
