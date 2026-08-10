package com.moviles.vetconnect.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.vetconnect.entities.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
sealed class PetsUiState {
    object Loading : PetsUiState()
    data class Success(val pets: List<Mascota>) : PetsUiState()
    data class Error(val message: String) : PetsUiState()
}

class MascotasViewModel(private val repository: PetRepository = PetRepository()): ViewModel() {


    private val _pets = MutableStateFlow<List<Mascota>>(emptyList())
    val pets: StateFlow<List<Mascota>> = _pets

    fun CargarMascotas(userId: String?) {
        viewModelScope.launch {
            repository.getUserPets(userId).collect {
                _pets.value = it
            }
        }
    }

    fun AñadirMascota(pet: Mascota) {
        viewModelScope.launch {
            repository.addPet(pet)
        }
    }

    fun eliminarMascota(petId: String, function: () -> Unit) {
        viewModelScope.launch {
            repository.deletePet(
                petId,
                onComplete = { success ->
                    if (success) {
                        function()
                    }
                }
            )
        }
    }
}

