var db= firebase.firestore();
var data = document.getElementById("userData");
var historyData = document.getElementById("TablaHistorial");

function getGET()
{
    var loc = document.location.href;
    if(loc.indexOf('?')>0)
    {
        var getString = loc.split('?')[1];
        var GET = getString.split('&');
        var get = {};
        for(var i = 0, l = GET.length; i < l; i++){
            var tmp = GET[i].split('=');
            get[tmp[0]] = unescape(decodeURI(tmp[1]));
        }
        return get;
    }
}

db.collection("usuario").doc(getGET().id).get().then(
    doc => {
        data.innerHTML="";
        data.innerHTML = `  <br>
                            <p align="center"> <img src="${doc.data().foto}" width=180 height=194> </p>
                            <br>
                            <p align="center"> Informacion General </p>
                            <p align="center"> codigo : ${doc.data().codigo} </p>
                            <p align="center"> Nombre y Apellidos : ${doc.data().nombre} </p>
                            <p align="center"> Nivel de natacion : ${doc.data().nivel} </p>
                            <p align="center"> Numero de contacto : ${doc.data().celular} </p>
                            <p align="center"> Observaciones : ${doc.data().observaciones} </p>`;
    }
);


let reservaRef = db.collection("reserva").where('codUsuario', '==', getGET().id);
let turnoRef = db.collection("turno");
let query1 = reservaRef.get().then(
    snapshot => {
        snapshot.forEach( doc => {
            turnoRef.where('codTurno', '==', doc.data().codTurno).get().then(
                snapshot2 => {
                    snapshot2.forEach( doc2 => {
                        historyData.innerHTML += `<tr>
                                                  <td> ${doc2.data().fecha} </td>
                                                  <td> ${doc2.data().codHorario} </td>
                                                  <td> ${doc.data().estado} </td>
                                                  </tr>`
                    })
                })
        })
});
