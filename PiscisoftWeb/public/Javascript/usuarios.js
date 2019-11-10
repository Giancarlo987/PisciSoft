var db = firebase.firestore(); 
var tabla = document.getElementById("tablaUsuarios");

function generarPaginacion() {
    let cab_filtrado =document.getElementById('cab_filtrado');
    cab_filtrado.innerHTML =''
    var p = new Paginador(
        document.getElementById('paginador'),
        document.getElementById('tablaUsuarios'),
        5
    );
    p.Mostrar()
}

//Tabla de usuarios actualizando en tiempo real 
function buscarTiempoReal() {
    var texto = document.getElementById("texto").value;
    if (texto == "") {
        TablaUsuario(false,"")
    } else {
        tabla.innerHTML = "";
        TablaUsuario(true, texto)
    }
    console.log(texto)
}


TablaUsuario()