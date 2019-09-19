var db= firebase.firestore();
var data = document.getElementById("userData");

db.collection("usuario").doc("20160315").get().then(
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