PopUpModificarUsuario = function (dia, hora) {
    this.mostrarDatosenPopUp = function () {
        let tituloPop = document.getElementById("tituloPop")
        let capacidad = document.getElementById("capacidad")
        let observacion = document.getElementById("observacion")
        let datosProfesor = document.getElementById("profesores")
        let datosEstado = document.getElementById("estado")


        valoresEstado = ["Abierto", "Cerrado"]

        datosEstado.innerHTML = ""
        datosProfesor.innerHTML = ""
        let profesorSeleccionado = ""

        let fechaElegida  = generarFecha(dia)
        db.collection("turno").doc(fechaElegida.generarFormatoFechaBD() + "." + hora).get()
            .then(doc => {
                tituloPop.innerHTML = `${dia}:  ${doc.data().horaInicio} - ${doc.data().horaFin}  `;
                capacidad.value = doc.data().capacidadTotal
                observacion.value = doc.data().observaciones
                datosEstado.innerHTML += `<option> ${doc.data().estado} </option>`;
                db.collection("profesor").doc(doc.data().profesor.toString()).get()
                    .then(doc1 => {
                        profesorSeleccionado = doc1.data().nombre
                        datosProfesor.innerHTML += `<option> ${doc1.data().nombre} </option>`;
                    });
                db.collection("profesor").get()
                    .then(snapshot => {
                        snapshot.forEach((doc2) => {
                            if (profesorSeleccionado != doc2.data().nombre) {
                                datosProfesor.innerHTML += `<option> ${doc2.data().nombre} </option>`;
                            }
                        });
                        
                    }
                    );
                for (var i = 0; i < 2; i++) {
                    if (doc.data().estado != valoresEstado[i])
                        datosEstado.innerHTML += `<option> ${valoresEstado[i]} </option>`;
                }
                habilitar()
            }
            );
    }
}