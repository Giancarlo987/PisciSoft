var db = firebase.firestore();
let hoy = new Date();
let semanaEnMilisegundos = 1000 * 60 * 60 * 24 * 7;


var datosFechas = document.getElementById("fechas")
datosFechas.innerHTML = ""
for (var i = 1; i < 3; i++) {
    let suma = hoy.getTime() + semanaEnMilisegundos * i;
    let fechaDentroDeUnaSemana = new Date(suma);
    let fechaInicio = new Fecha(fechaDentroDeUnaSemana, "Lunes");
    let fechaFin = new Fecha(fechaDentroDeUnaSemana, "Sabado");
    datosFechas.innerHTML += `<option> Del ${fechaInicio.generarFormatoFechaOriginal()} hasta el ${fechaFin.generarFormatoFechaOriginal()} </option>`;
}

function generarPop(dia, hora) {
    let p = new PopUpModificarUsuario(dia, hora);
    let g = new GuardarCambios(dia, hora)
    divCapacidad.innerHTML = ``
    p.mostrarDatosenPopUp()
    let boton = document.getElementById("botonGuardarC")
    boton.onclick = function () {
        let nuevaCapacidad = document.getElementById("capacidad").value
        if (validarNumero(nuevaCapacidad) == 1) {
            boton.setAttribute("data-dismiss", "modal")
            g.guardarTodoCambios()
        } else
            console.log(validarNumero(nuevaCapacidad))
    }

}


function habilitar() {
    var hab;
    let capacidad = document.getElementById("capacidad")
    let observacion = document.getElementById("observacion")
    let listaDesplegableProfesores = document.getElementById("profesores")
    let listaDesplegableEstado = document.getElementById("estado")
    let estado = listaDesplegableEstado.options[listaDesplegableEstado.selectedIndex].value;
    if (estado == "Cerrado") {
        hab1 = true
        hab2 = false
    }
    else if (estado == "Abierto") {
        hab1 = false
        hab2 = true
    }
    capacidad.disabled = hab1;
    listaDesplegableProfesores.disabled = hab1;
    observacion.disabled = hab2;
}
//Arreglar esto, por el cambio de la base de datos

// //diasSemana = ["Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"]
// function generadorTurnos() {
//     diasSemana = ["Sabado"]
//     for (var i = 0; i < diasSemana.length; i++) {
//         let nuevosTurnos = new Fecha(hoy, diasSemana[i])
//         nuevosTurnos.generarFormatoFecha()
//         nuevosTurnos.generarTurno()
//     }
// }