var db= firebase.firestore(); //Declarar la base de datos firestore
var tabla = document.getElementById("tablaUsuarios");
//Tabla de usuarios actualizando en tiempo real 
function mostrarTabla(filtro,palabra){
    let texto = "";
    let fin = "";
    if (filtro==true){
        texto = palabra;
        fin = texto.replace(/.$/, c => String.fromCharCode(c.charCodeAt(0) + 1));

    }else{
        texto='1';
        fin='9999999999999999999999999';
    }
    db.collection("usuario").where(firebase.firestore.FieldPath.documentId(), ">=",texto).where(firebase.firestore.FieldPath.documentId(), "<",fin).onSnapshot((querySnapshot) => {
        tabla.innerHTML="";
        querySnapshot.forEach((doc) => {
            tabla.innerHTML +=	`<tr>   
                            <th scope='row' style='vertical-align:middle'>${doc.id}</th> 
                            <td valign="middle" style='vertical-align:middle' ><img src='${doc.data().foto}' width=109 height=123></td> 
                            <td style='vertical-align:middle'>${doc.data().nombre}</td> 
                            <td style='vertical-align:middle'>${doc.data().celular}</td>
                            <td style='vertical-align:middle'>${doc.data().observacion}</td>
                            <td style='vertical-align:middle'><button class="btn btn-danger">Ver detalle</button></td>  
                        </tr>`;
            });
    });
}

mostrarTabla()

console.log(document.getElementById("texto").value)
//Aun en prueba
function buscarTiempoReal(){
    var texto = document.getElementById("texto").value;
    if (texto == ""){
        mostrarTabla(false)
    }else{
        tabla.innerHTML="";
        mostrarTabla(true,texto)
    }
    console.log(texto)
}