var db= firebase.firestore();
var data = document.getElementById("userData");

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