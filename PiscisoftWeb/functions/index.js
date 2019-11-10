//Inicializar functions y admin(base de datos)
const functions = require('firebase-functions');
const firebase = require('firebase-admin');
const express = require('express');
const engines= require('consolidate');
const firebaseApp=firebase.initializeApp()



//Prueba de HBS....aun en implementacion
const app = express();
app.engine('hbs', engines.handlebars);
app.set('views','./views');
app.set('view engine','hbs');

app.get('/FuncionPruebaHBS',(request,response) =>{
    response.set('Cache-Control','public, max-age-300, s-maxage-600');
    response.render('prueba', {facts});
});
exports.FuncionPruebaHBS = functions.https.onRequest(app);

//Fin

//Función de autenticación //Función reemplazada por la autenticación en la creación de Cookies
/*
exports.Autenticacion = functions.https.onRequest((req,res) => {
    let store = firebase.firestore()
    store.collection('AdministradorUsuario').doc("AtyyZuBXX3ButSAB9OLH").get().then(doc  => {
        if (doc.data().usuario==req.query.usuario && doc.data().password==req.query.password) {
            res.redirect("https://piscisoft.firebaseapp.com/paginaPrincipal.html")

        }
        else {
            res.send("No se ingreso correctamente")
        }
    }).catch (err => {
        res.send('Hubo un error');
    })
});
*/

//Función actualizar
exports.FuncionGuardarEnBase = functions.https.onRequest((req,res) => {
    firebase.firestore().collection('colx').doc('docx').update(
        {textox: req.query.inf 
    }).then ( r => {
        res.redirect( "https://piscisoft.firebaseapp.com/");
    }).catch (err => {
        res.send('Hubo un error');
    })
});
//FIN


//Para probar logica de firestore

exports.PruebarGuardarEnBase = functions.https.onRequest((req,res) => {
    firebase.firestore().collection('colx').doc(req.query.inf ).set(
        {textox: req.query.inf 
    }).then ( r => {
        res.redirect( "https://piscisoft.firebaseapp.com/");
    }).catch (err => {
        res.send('Hubo un error');
    })
});


exports.PruebarReferencia = functions.https.onRequest((req,res) => {
    firebase.firestore().collection('usuario').doc('20162554').get()
    .then ( r => {
        res.send( r.ref.get() );
    }).catch (err => {
        res.send('Hubo un error');
    })
});