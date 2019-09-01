import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase)

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.respuestaWeb = functions.https.onRequest((req,res) => {
    res.send('<h1> Hola mundo </h1>' + 
             '<p> Esta es la pagina de respuesta para la prueba de Firebase Cloud Functions </p>' + 
             '<p> Si deseas leer la documentacion haz click aqui </p>' + 
             '<a href="https://firebase.google.com/docs/?authuser=0"> Click </a>');

});

exports.FuncionBaseDeDatos = functions.https.onRequest((req,res) => {
    admin.firestore().collection('formularioDatos').add({
        nombre: req.query.nombre,
        apellidos: req.query.apellidos
    }).then ( r => {
        res.send('Completed');
    }).catch (err => {
        res.send('Hubo un error');
    })
});

exports.FuncionLeerBaseDatos = functions.https.onRequest((req,res) => {
    admin.firestore().collection('formularioDatos').doc('2w6OAbKjhm0mu9UEfUiS').get()
    .then ( r => {
        res.send('<h1> Datos del documento: </h1>' + 
                  '<p> Nombre : ' + r.get("nombre") + '</p>' +
                  '<p> Apellidos : ' + r.get("apellidos") + '</p>')
    }).catch (err => {
        res.send('Hubo un error')
    })
});

