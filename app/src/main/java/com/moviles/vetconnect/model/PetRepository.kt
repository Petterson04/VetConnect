package com.moviles.vetconnect.model

import com.google.firebase.firestore.FirebaseFirestore
import com.moviles.vetconnect.entities.Mascota
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PetRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val petsCollection = firestore.collection("Mascotas")

    suspend fun addPet(pet: Mascota) {
        val docRef = petsCollection.document()
        val petWithId = pet.copy(id = docRef.id)
        docRef.set(petWithId).await()
    }

    fun getUserPets(userId: String?): Flow<List<Mascota>> = callbackFlow {
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = petsCollection
            .whereEqualTo("idUsuario", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val pets = snapshot?.toObjects(Mascota::class.java) ?: emptyList()
                trySend(pets)
            }

        awaitClose { listener.remove() }
    }
    suspend fun deletePet(petId: String, onComplete: (Boolean) -> Unit) {
        petsCollection.document(petId).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

}
