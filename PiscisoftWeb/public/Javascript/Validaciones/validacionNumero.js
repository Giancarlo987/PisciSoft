function validarNumero(numero) {
    let nuevaCapacidad = document.getElementById("capacidad").value
    let divCapacidad = document.getElementById("divCapacidad")
    if (!/^([0-9])*$/.test(numero)) {
        divCapacidad.innerHTML = `Ingrese un valor númerico positivo`
        nuevaCapacidad.innerHTML = ""
        return 0
    }
    else if (numero <= 0) {
        divCapacidad.innerHTML = `No se puede ingresar 0 como valor`
        nuevaCapacidad.innerHTML = ""
        return 0
    }
    else if (numero > 26) {
        divCapacidad.innerHTML = `Se ingreso un número mayor a 26`
        nuevaCapacidad.innerHTML = ""
        return 0
    }
    else {
        return 1
    }
}
