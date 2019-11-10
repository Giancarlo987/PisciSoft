var db= firebase.firestore();
var data = document.getElementById("userData");
var historyData = document.getElementById("TablaHistorial");



identificacionPersona=obtenerParametros()

db.collection("usuario").doc(identificacionPersona.id).get().then(
    doc => {
        data.innerHTML="";
        data.innerHTML = `  <br>
                            <p align="center"> <img src="${doc.data().foto}" width=180 height=194> </p>
                            <br>
                            <div class="col-md-5 offset-md-4">
                                <h3 align="left"> Información General </h3>
                                <p align="left"> <strong> Código </strong> : ${doc.data().codigo} </p>
                                <p align="left"> <strong> Nombre y Apellidos </strong> : ${doc.data().nombre} </p>
                                <p align="left"> <strong> Nivel de natacion </strong> : ${doc.data().nivel} </p>
                                <p align="left"> <strong> Numero de contacto </strong> : ${doc.data().celular} </p>
                                <p align="left"> <strong> Observaciones </strong> : ${doc.data().observaciones} </p>
                            </div>`;
    }
);


let reservaRef = db.collection("reserva").where('codUsuario', '==', identificacionPersona.id);
let turnoRef = db.collection("turno");
let query1 = reservaRef.get().then(
    snapshot => {
        snapshot.forEach( doc => {
            turnoRef.where(firebase.firestore.FieldPath.documentId(), '==', doc.data().codTurno).get().then(
                snapshot2 => {
                    snapshot2.forEach( doc2 => {
                        historyData.innerHTML += `<tr>
                                                  <td> ${doc2.data().fecha} </td>
                                                  <td> ${doc2.data().horaInicio} - ${doc2.data().horaFin} </td>
                                                  <td> ${doc.data().estado} </td>
                                                  </tr>`
                    })
                })
        })
});
