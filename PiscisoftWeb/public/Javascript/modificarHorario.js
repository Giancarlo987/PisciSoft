var db = firebase.firestore();
let hoy = new Date();
let semanaEnMilisegundos = 1000 * 60 * 60 * 24 * 7;


var datosFechas = document.getElementById("fechas")
datosFechas.innerHTML = ""
for (var i = 1; i < 3; i++) {
    let suma = hoy.getTime() + semanaEnMilisegundos * i;
    let fechaDentroDeUnaSemana = new Date(suma);
    let fechaInicio = new Fecha(fechaDentroDeUnaSemana, "Lunes");
    let fechaFin = new Fecha(fechaDentroDeUnaSemana, "Domingo");
    datosFechas.innerHTML += `<option value=${i}> Del ${fechaInicio.generarFormatoFechaOriginal()} hasta el ${fechaFin.generarFormatoFechaOriginal()} </option>`;
}

function generarPop(dia, hora) {
    let divCapacidad = document.getElementById("divCapacidad")
    let divObservacion = document.getElementById("divObservacion")
    let p = new PopUpModificarUsuario(dia, hora);
    let t = new TurnoWeb(dia, hora)
    divCapacidad.innerHTML = ``
    divObservacion.innerHTML = ``
    p.mostrarDatosenPopUp()
    let boton = document.getElementById("botonGuardarC")

    boton.onclick = function () {
        let nuevaCapacidad = document.getElementById("capacidad").value

        let listaDesplegableProfesor = document.getElementById("profesores");
        let nuevoProfesor = listaDesplegableProfesor.options[listaDesplegableProfesor.selectedIndex].value;

        let listaDesplegableEstado = document.getElementById("estado");
        let nuevoEstado = listaDesplegableEstado.options[listaDesplegableEstado.selectedIndex].value;



        let observacion = document.getElementById("observacion").value
        if (nuevoEstado == "Cerrado" && observacion == "") {
            boton.setAttribute("data-dismiss", "")
            divObservacion.innerHTML = "Ingrese una observación"
        }
        else if (validarNumero(nuevaCapacidad) == 1) {
            boton.setAttribute("data-dismiss", "modal")
            
            t.guardarTodoCambios(nuevaCapacidad, nuevoProfesor, nuevoEstado, observacion)
        } else {
            boton.setAttribute("data-dismiss", "")
            divCapacidad.innerHTML = `Ingrese un valor númerico positivo y menor a 27`
            nuevaCapacidad.innerHTML = ""
        }
    }

}


function habilitar() {
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
        document.getElementById("observacion").value=""
    }
    capacidad.disabled = hab1;
    listaDesplegableProfesores.disabled = hab1;
    observacion.disabled = hab2;
}


function generadorTurnos() { //Solo esta generando para la siguiente semana
    diasSemana = ["Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"]
    for (var i = 0; i < diasSemana.length; i++) {
        let suma = hoy.getTime() + semanaEnMilisegundos * 1;
        let fechaDentroDeUnaSemana = new Date(suma);
        let nuevosTurnos = new Fecha(fechaDentroDeUnaSemana, diasSemana[i])
        nuevosTurnos.generarFormatoFechaBD()
        nuevosTurnos.generarTurno()
    }
}
//generadorTurnos()