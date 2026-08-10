package com.moviles.vetconnect.entities

data class Usuario(
    var id: String = "",
    var nombre: String = "",
    var email: String = "",
    var numero: String,
    var rol: String = "",
    var fechaRegistro: String = ""
) {

    constructor() : this("", "", "", "", "", "")
}
