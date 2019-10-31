function TablaUsuario(filtro, palabra) {
    let texto = "";
    let fin = "";
    if (filtro == true) {
        texto = palabra;
        fin = texto.replace(/.$/, c => String.fromCharCode(c.charCodeAt(0) + 1));
    } else {
        texto = '1';
        fin = '9999999999999999999999999';
    }
    
    db.collection("usuario").where(firebase.firestore.FieldPath.documentId(), ">=", texto).where(firebase.firestore.FieldPath.documentId(), "<", fin).onSnapshot((querySnapshot) => {
        tabla.innerHTML =''
        tabla.innerHTML = ` <thead>
                            <tr style="background-color:	#689ACA; text-align:center">
                            <th scope="col" style="color:	#FFFFFF; text-align:center ">Código</th>
                            <th scope="col" style="color:	#FFFFFF; text-align:center">Foto</th>
                            <th scope="col" style="color:	#FFFFFF; text-align:center">Nombre Completo</th>
                            <th scope="col" style="color:	#FFFFFF; text-align:center">Teléfono</th>
                            <th scope="col" style="color:	#FFFFFF; text-align:center">Observación</th>
                            <th scope="col" style="color:	#FFFFFF; text-align:center">Ver detalles</th>
                         </tr>
                         </thead>`;
        querySnapshot.forEach((doc) => {
            tabla.innerHTML += `<tr>   
                            <th scope='row' style='vertical-align:middle'>${doc.id}</th> 
                            <td valign="middle" style='vertical-align:middle' ><img src='${doc.data().foto}' width=109 height=123></td> 
                            <td style='vertical-align:middle'>${doc.data().nombre}</td> 
                            <td style='vertical-align:middle'>${doc.data().celular}</td>
                            <td style='vertical-align:middle'>${doc.data().observaciones}</td>
                            <td style='vertical-align:middle'><button class="btn btn-info" style="background: #FC663D;" onclick="location.href='./usuariosDetalle.html?id=${doc.id}'">Ver detalle</button></td>   
                        </tr>`;
        });
    });
}