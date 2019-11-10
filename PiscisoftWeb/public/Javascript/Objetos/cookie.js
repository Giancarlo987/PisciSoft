function MakeCookie() { 
    var usuarioNombre = document.getElementById('nombre').value;
    var usuarioPassword = document.getElementById('password').value
    var store = firebase.firestore();
    var docRef = store.collection("AdministradorUsuario").doc("AtyyZuBXX3ButSAB9OLH");
    docRef.get().then(
        doc => {
            if (doc.data().usuario == usuarioNombre && doc.data().password == usuarioPassword) {
                document.cookie = "Login=true; expires= Fri, 01 Dec 9999 00:00:00 GMT";
                window.location.href = "https://piscisoft.firebaseapp.com/paginaPrincipal.html";
            } else {
                document.getElementById('nombre').value =""
                document.getElementById('password').value = ""
                console.log("g")
                $(document).ready(function () {
                    $("#ventanaError").modal("show");
                });
            }
        }
    )
}

