function validarSiNumero(numero) {
    var capacidad = document.getElementById("capacidad")
    if (!/^([0-9])*$/.test(numero)) {
        alert("En capacidad no se coloco un número")
        capacidad.value=""
        return 0; //No es un numero
    } else if (numero > 26) {
        alert("La capacidad no puede ser mayor a 26")
        capacidad.value=""
    } else {
        return 1;
    }
}


PopUp = function (id) {
    this.id = id
    var db = firebase.firestore();

    var datosProfesor = document.getElementById("profesores");
    db.collection("profesor").get()
        .then(snapshot => {
            snapshot.forEach((doc) => {
                datosProfesor.innerHTML += `<option> ${doc.data().nombre} </option>`;
            });
        }
        );

    this.mostrarDatosenPopUp = function () {
        var tituloPop = document.getElementById("tituloPop")
        var capacidad = document.getElementById("capacidad")
        db.collection("horario").doc(this.id).get()
            .then(doc => {
                tituloPop.innerHTML = `${doc.data().diaSemana} ${doc.data().horaInicio} - ${doc.data().horaFin}  `;
                capacidad.value = doc.data().capacidadTotal
            });
    }

    this.guardarCambioCapacidad = function (nuevaCapacidad) {
        db.collection('horario').doc(this.id).update(
            {
                capacidadTotal: nuevaCapacidad
            }).then(r => {
                document.getElementById("myForm").style.display = "none";
                alert('Guardado correctamente');
            }).catch(err => {
                document.getElementById("myForm").style.display = "none";
                alert('Hubo un error,repitalo nuevamente');
            })

    }


}

function generarPop(id) {
    var p = new PopUp(id);
    var boton = document.getElementById("botonGuardarC")

    boton.onclick = function () {
        var nuevaCapacidad = parseInt(document.getElementById("capacidad").value)
        if (validarSiNumero(nuevaCapacidad) == 1) {
            boton.setAttribute("data-dismiss", "modal");
            //p.guardarCambioCapacidad(nuevaCapacidad)
        }

    }
    p.mostrarDatosenPopUp()
}



