var db = firebase.firestore();
let hoyDia = new Date();
let semanaEnMilisegundo = 1000 * 60 * 60 * 24 * 7;

function pintarTablaInicio() {
    let fechaDentroDeUnaSemana = new Date(hoyDia.getTime() + semanaEnMilisegundo);
    let fechaInicio = new Fecha(fechaDentroDeUnaSemana, "Lunes");
    let fechaFin = new Fecha(fechaDentroDeUnaSemana, "Domingo");

    


    db.collection("turno").where("estado", "==", "Cerrado").where(firebase.firestore.FieldPath.documentId(), ">=", fechaInicio.generarFormatoFechaBD()).where(firebase.firestore.FieldPath.documentId(), "<=", fechaFin.generarFormatoFechaBD()).get()
    .then(function(querySnapshot) {
        querySnapshot.forEach(function(doc) {
            let casilla = document.getElementById(`${doc.data().dia}-${doc.data().horaInicio}`)
            casilla.setAttribute("bgcolor", "#FF0000")
        });

    });
    


}
pintarTablaInicio()

function pintarTabla(){
    celdasTabla=document.getElementsByClassName("celdaTabla");
    for (i = 0; i < celdasTabla.length; i++) {
        celdasTabla[i].style.backgroundColor = "#FFFFFF";
    }
    let valorlistaDesplegableFecha = datosFechas.value;
    let fechaInicio;
    let fechaFin;
    if(valorlistaDesplegableFecha==1){
        let fechaDentroDeUnaSemana = new Date(hoy.getTime() + semanaEnMilisegundos);
        fechaInicio = new Fecha(fechaDentroDeUnaSemana, "Lunes");
        fechaFin = new Fecha(fechaDentroDeUnaSemana, "Domingo");
    }else{
        let fechaDentroDeUnaSemana = new Date(hoy.getTime() + semanaEnMilisegundos*2);
        fechaInicio = new Fecha(fechaDentroDeUnaSemana, "Lunes");
        fechaFin = new Fecha(fechaDentroDeUnaSemana, "Domingo");
    }
    db.collection("turno").where("estado", "==", "Cerrado").where(firebase.firestore.FieldPath.documentId(), ">=", fechaInicio.generarFormatoFechaBD()).where(firebase.firestore.FieldPath.documentId(), "<=", fechaFin.generarFormatoFechaBD()).get()
    .then(function(querySnapshot) {
        querySnapshot.forEach(function(doc) {
            let casilla = document.getElementById(`${doc.data().dia}-${doc.data().horaInicio}`)
            casilla.style.backgroundColor = "#FF0000";
        });
    });
}
