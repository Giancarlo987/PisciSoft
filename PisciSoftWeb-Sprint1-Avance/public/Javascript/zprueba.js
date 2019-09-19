console.log("web.js")

var db= firebase.firestore();


var prueba = document.getElementById("resultadoDB");



db.collection("colx").doc("docx")
    .onSnapshot(function(doc) {
        if (doc.exists) { 
		  	prueba.innerHTML="";
		  	prueba.innerHTML=doc.data().textox;
		} else { 
		  console.log ('documento no encontrado'); 
		} 
});