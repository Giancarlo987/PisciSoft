import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp();

export const FuncionGuardarEnBase = functions.https.onRequest((req,res) => {
    admin.firestore().collection('colx').doc('docx').update(
        {textox: req.query.inf 
    }).then ( r => {
        res.redirect( "https://piscisoft.firebaseapp.com/");
    }).catch (err => {
        res.send('Hubo un error');
    })
});

export const PruebarGuardarEnBase = functions.https.onRequest((req,res) => {
    admin.firestore().collection('colx').doc(req.query.inf ).set(
        {textox: req.query.inf 
    }).then ( r => {
        res.redirect( "https://piscisoft.firebaseapp.com/");
    }).catch (err => {
        res.send('Hubo un error');
    })
});

export const PruebarReferencia = functions.https.onRequest((req,res) => {
    admin.firestore().collection('usuario').doc('20162554').get()
    .then ( r => {
        res.send( r.ref.get() );
    }).catch (err => {
        res.send('Hubo un error');
    })
});