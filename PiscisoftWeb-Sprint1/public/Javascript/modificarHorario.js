var db = firebase.firestore();


function validarNumero(numero){
    let nuevaCapacidad = document.getElementById("capacidad").value
    if (!/^([0-9])*$/.test(numero)){
        alert("En capacidad se ingreso un texto o un número negativo")
        nuevaCapacidad.innerHTML =""
        return 0
    }
    else if(numero<0){
        alert("Se ingreso un número negativo")
        nuevaCapacidad.innerHTML =""
        return 0
    }
    else if(numero>26){
        alert("Se ingreso un número mayor a 26")
        nuevaCapacidad.innerHTML =""
        return 0
    }
    else{
        return 1
    }
}

Fecha = function (fecha, diaSemana) {
    this.fecha = fecha;
    this.diaSemana = diaSemana

    fechaSemana = this.fecha
    let unDiaAdicional = 1000 * 60 * 60 * 24

    var day = fechaSemana.getDay() || 7;
    if (day !== 1) {
        fechaSemana.setHours(-24 * (day - 1));
    }
    if (diaSemana == "Martes") {
        fechaSemana = new Date(fechaSemana.getTime() + unDiaAdicional)
    } else if (diaSemana == "Miercoles") {
        fechaSemana = new Date(fechaSemana.getTime() + unDiaAdicional * 2)
    } else if (diaSemana == "Jueves") {
        fechaSemana = new Date(fechaSemana.getTime() + unDiaAdicional * 3)
    } else if (diaSemana == "Viernes") {
        fechaSemana = new Date(fechaSemana.getTime() + unDiaAdicional * 4)
    } else if (diaSemana == "Sabado") {
        fechaSemana = new Date(fechaSemana.getTime() + unDiaAdicional * 5)
    }

    this.generarFormatoFecha = function () {

        var anho = fechaSemana.getFullYear()
        var mes = fechaSemana.getMonth() + 1
        var dia = fechaSemana.getDate()

        if (mes < 10) {
            mes = "0" + mes
        }

        if (dia < 10) {
            dia = "0" + dia
        }

        var fechaActual = anho + "-" + mes + "-" + dia
        return fechaActual
    }

}


let hoy = new Date();
let semanaEnMilisegundos = 1000 * 60 * 60 * 24 * 7;
let suma = hoy.getTime() + semanaEnMilisegundos; //getTime devuelve milisegundos de esa fecha
let fechaDentroDeUnaSemana = new Date(suma);


PopUp = function (id) {
    this.id = id

    this.mostrarDatosenPopUp = function () {
        var tituloPop = document.getElementById("tituloPop")
        var capacidad = document.getElementById("capacidad")
        let datosProfesor = document.getElementById("profesores");
        datosProfesor.innerHTML = ""
        let profesorSeleccionado = ""
        db.collection("horario").doc(this.id).get()
            .then(doc => {
                tituloPop.innerHTML = `${doc.data().diaSemana} ${doc.data().horaInicio} - ${doc.data().horaFin}  `;
                capacidad.value = doc.data().capacidadTotal
                db.collection("profesor").doc(doc.data().codProfesor.toString()).get()
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
            }
            );
    }


    this.guardarCambioCapacidad = function () {
        let nuevaCapacidad = parseInt(document.getElementById("capacidad").value)
        let listaDesplegableProfesor = document.getElementById("profesores");
        let valorSeleccionado = listaDesplegableProfesor.options[listaDesplegableProfesor.selectedIndex].value;
        let codigoValorSelec = 10

        db.collection("profesor").where("nombre","==",valorSeleccionado).get()
            .then(snapshot => {
                if (snapshot.empty) {
                  console.log('No hay doc');
                  return;
                }
            
                snapshot.forEach(doc => {
                  codigoValorSelec = doc.id
                  codigoValorSelec = parseInt(codigoValorSelec)
                  db.collection('horario').doc(this.id).update(
                    {
                        capacidadTotal: nuevaCapacidad,
                        codProfesor: codigoValorSelec
                    }).then(r => {
                        document.getElementById("myForm").style.display = "none";
                        
                        alert('Guardado correctamente');
                    }).catch(err => {
                        document.getElementById("myForm").style.display = "none";
                        alert('Hubo un error,repitalo nuevamente');
                    })
                });
            })
    }
}

function generarPop(id) {
    let p = new PopUp(id);
    let hoy = new Date();
    let semanaEnMilisegundos = 1000 * 60 * 60 * 24 * 7;

    var posRaya = id.indexOf("-")
    var cadena = id
    diaTabla = cadena.substring(0, posRaya);

    let boton = document.getElementById("botonGuardarC")
    boton.onclick = function () {
        let nuevaCapacidad = document.getElementById("capacidad").value
        if (validarNumero(nuevaCapacidad)==1){
            boton.setAttribute("data-dismiss", "modal")
            p.guardarCambioCapacidad()
        }
    }
    p.mostrarDatosenPopUp()

    var datosFechas = document.getElementById("fechas")
    datosFechas.innerHTML = ""
    for (var i = 0; i < 3; i++) {

        let suma = hoy.getTime() + semanaEnMilisegundos * i;
        let fechaDentroDeUnaSemana = new Date(suma);
        let fecha = new Fecha(fechaDentroDeUnaSemana, diaTabla);
        datosFechas.innerHTML += `<option> ${fecha.generarFormatoFecha()} </option>`;
    }

}



