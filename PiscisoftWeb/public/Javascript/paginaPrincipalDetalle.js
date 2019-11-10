let db = firebase.firestore();
let identificacionFecha = obtenerParametros()

let hoy = new Date()
let fecha = new Fecha(hoy, identificacionFecha.dia)
let cap = document.getElementById("capacidad")

function mostrarTurno() {
    db.collection("turno").doc(`${fecha.generarFormatoFechaBD()}.${identificacionFecha.hora}`).get().then(
        doc => {
            cap.innerHTML += `${doc.data().capacidadCubierta}/${doc.data().capacidadTotal}`
        }
    );
}