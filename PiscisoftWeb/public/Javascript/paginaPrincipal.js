let hoy = new Date()
let semanaActual = document.getElementById("fechaSemana")
let fechaInicio = new Fecha(hoy, "Lunes")
let fechaFin = new Fecha(hoy, "Sabado")
semanaActual.innerHTML = `Semana: ${fechaInicio.generarFormatoFechaOriginal()} hasta el ${fechaFin.generarFormatoFechaOriginal()}`;

