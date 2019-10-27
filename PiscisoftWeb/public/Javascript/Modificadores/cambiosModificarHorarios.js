GuardarCambios = function (dia, hora) {
    this.dia = dia
    this.hora = hora
    this.fechaElegida  = generarFecha(dia)

    let etiquetaTituloVal = document.getElementById("tituloVal")

    this.guardarCambioCapacidad = function () {
        let nuevaCapacidad = document.getElementById("capacidad").value
        
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

    this.guardarCambioProfesor = function () {
        let listaDesplegableProfesor = document.getElementById("profesores");
        let nuevoProfesor = listaDesplegableProfesor.options[listaDesplegableProfesor.selectedIndex].value;
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

    this.cambiarTurno = function () {
        let listaDesplegableEstado = document.getElementById("estado");
        let nuevoEstado = listaDesplegableEstado.options[listaDesplegableEstado.selectedIndex].value;
        let etiquetaValidador = document.getElementById("validador")
        db.collection('turno').doc(this.fechaElegida.generarFormatoFechaBD() + "." + hora).update(
            {
                estado: nuevoEstado
            }).then(r => {
            }).catch(err => {
                etiquetaTituloVal.innerHTML = ` Excepto:<br>`   
                etiquetaValidador.innerHTML += ` -Turno <br>`
            })
    }

    this.guardarObservacion = function () {
        let observacion = document.getElementById("observacion").value
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



    this.guardarTodoCambios = function () {
        this.guardarCambioProfesor()
        this.guardarCambioCapacidad()
        this.guardarObservacion()
        this.cambiarTurno()
        if (true) {
            document.getElementById("ventanaCambios").style.display = "none";
            $(document).ready(function () {
                $("#ventanaGuardado").modal("show");
            });
        }
    }
}