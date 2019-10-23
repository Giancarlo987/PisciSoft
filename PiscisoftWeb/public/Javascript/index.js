function comprobarCredenciales() {
    let nombre = document.getElementById("nombre").value
    let password = document.getElementById("password").value
    let divMensajeVacioNombre = document.getElementById("mensajeVacioNombre")
    let divMensajeVacioPassword = document.getElementById("mensajeVacioPassword")
    if (validarVacio(nombre) == 1) {
        divMensajeVacioNombre.innerHTML = `Introduzca un usuario`
    }else
    divMensajeVacioNombre.innerHTML = ``
    if (validarVacio(password) == 1) {
        divMensajeVacioPassword.innerHTML = `Introduzca una contraseña`
    }else
        divMensajeVacioPassword.innerHTML = ``
    if((validarVacio(nombre) == 0) && (validarVacio(password) == 0))
        MakeCookie()
}