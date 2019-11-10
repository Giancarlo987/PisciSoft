TurnoWeb = function (dia, hora) {
    this.dia = dia
    this.hora = hora
    this.fechaElegida = generarFecha(dia)
    let etiquetaTituloVal = document.getElementById("tituloVal")

    this.guardarCambioCapacidad = function (nuevaCapacidad) {
        let etiquetaValidador = document.getElementById("validador")
        db.collection('turno').doc(this.fechaElegida.generarFormatoFechaBD() + "." + hora).update(
            {
                capacidadTotal: parseInt(nuevaCapacidad)
            }).then(r => {
            }).catch(err => {
                etiquetaTituloVal.innerHTML = ` Excepto:<br>`
                etiquetaValidador.innerHTML += ` -Capacidad <br>`
            })
    }

    this.guardarCambioProfesor = function (nuevoProfesor) {
        let etiquetaValidador = document.getElementById("validador")
        let codigoValorSelec = 10
        db.collection("profesor").where("nombre", "==", nuevoProfesor).get()
            .then(snapshot => {
                if (snapshot.empty) {
                    return;
                }
                snapshot.forEach(doc => {
                    codigoValorSelec = parseInt(doc.id)
                    db.collection('turno').doc(this.fechaElegida.generarFormatoFechaBD() + "." + hora).update(
                        {
                            profesor: codigoValorSelec
                        }).then(r => {
                        }).catch(err => {
                            etiquetaTituloVal.innerHTML = ` Excepto:<br>`
                            etiquetaValidador.innerHTML += ` -Profesor <br>`
                        })
                });
            })
    }

    this.cambiarTurno = function (nuevoEstado) {
        let etiquetaValidador = document.getElementById("validador")
        db.collection('turno').doc(this.fechaElegida.generarFormatoFechaBD() + "." + hora).update(
            {
                estado: nuevoEstado
            }).then(r => {
                if (nuevoEstado == "Abierto") {
                    let casilla = document.getElementById(`${dia}-${hora}`)
                    casilla.style.backgroundColor = "#FFFFFF";
                }
                if (nuevoEstado == "Cerrado") {
                    let casilla = document.getElementById(`${dia}-${hora}`)
                    casilla.style.backgroundColor = "#FF0000";
                }
            }).catch(err => {
                etiquetaTituloVal.innerHTML = ` Excepto:<br>`
                etiquetaValidador.innerHTML += ` -Turno <br>`
            })
    }

    this.guardarObservacion = function (observacion) {
        
        let etiquetaValidador = document.getElementById("validador")
        db.collection('turno').doc(this.fechaElegida.generarFormatoFechaBD() + "." + hora).update(
            {
                observaciones: observacion
            }).then(r => {
            }).catch(err => {
                etiquetaTituloVal.innerHTML = ` Excepto:<br>`
                etiquetaValidador.innerHTML += ` -Observación <br>`
            })
    }



    this.guardarTodoCambios = function (nuevaCapacidad,nuevoProfesor,nuevoEstado,observacion) {
        this.guardarCambioProfesor(nuevoProfesor)
        this.guardarCambioCapacidad(nuevaCapacidad)
        this.guardarObservacion(observacion)
        this.cambiarTurno(nuevoEstado)
        if (true) {
            document.getElementById("ventanaCambios").style.display = "none";
            $(document).ready(function () {
                $("#ventanaGuardado").modal("show");
            });
            pintarTabla()
        }
    }
}