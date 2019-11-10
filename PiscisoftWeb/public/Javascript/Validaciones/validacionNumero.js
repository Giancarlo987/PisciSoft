function validarNumero(numero) {
    if (!/^([0-9])*$/.test(numero)) {
        return 0
    }
    else if (numero <= 0) {
        return 0
    }
    else if (numero > 26) {
        return 0
    }
    else {
        return 1
    }
}
