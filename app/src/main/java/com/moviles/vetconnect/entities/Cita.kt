package com.moviles.vetconnect.entities

data class Cita(
    val id: String = "",
    val idCliente: String = "",
    val idMascota: String = "",
    val idVeterinario: String = "",
    val nombreMascota: String = "",
    val nombreVeterinario: String = "",
    val fecha: String = "",
    val hora: String = "",
    val razon: String = "",

)
