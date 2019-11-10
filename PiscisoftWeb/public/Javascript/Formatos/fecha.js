Fecha = function (fecha, diaSemana) {
    this.diaSemana = diaSemana
    this.fechaSemana = fecha
    let unDiaAdicional = 1000 * 60 * 60 * 24
    let day = this.fechaSemana.getDay() || 7;

    if (day !== 1)
        this.fechaSemana.setHours(-24 * (day - 1))
    if (diaSemana == "Martes")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional)
    else if (diaSemana == "Miercoles")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional * 2)
    else if (diaSemana == "Jueves")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional * 3)
    else if (diaSemana == "Viernes")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional * 4)
    else if (diaSemana == "Sabado")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional * 5)
    else if (diaSemana == "Domingo")
        this.fechaSemana = new Date(this.fechaSemana.getTime() + unDiaAdicional * 6)

    this.generarFormatoFechaBD = function () {
        let anho = this.fechaSemana.getFullYear()
        let mes = this.fechaSemana.getMonth() + 1
        let dia = this.fechaSemana.getDate()
        if (mes < 10)
            mes = "0" + mes
        if (dia < 10)
            dia = "0" + dia
        let formatoFechaBD = anho + "-" + mes + "-" + dia
        return formatoFechaBD
    }

    this.generarFormatoFechaOriginal = function () {
        let mes = this.fechaSemana.getMonth() + 1
        let dia = this.fechaSemana.getDate()
        if (mes < 10)
            mes = "0" + mes
        if (dia < 10)
            dia = "0" + dia
        let formatoFechaOr = dia + "-" + mes
        return formatoFechaOr
    }

    this.generarTurno = function () {
        horariosInicio = [ "5:30", "6:30", "7:30", "8:30", "9:30",
                        "10:30", "11:30", "12:30", "13:30", "14:30",
                        "15:30", "16:30", "17:30", "18:30", "19:30"] 
        horariosFin = ["6:15", "7:15", "8:15", "9:15", "10:15",
                       "11:15", "12:15", "13:15", "14:15", "15:15",
                       "16:15", "17:15", "18:15", "19:15", "20:15"]   
        for (var j = 0; j < horariosInicio.length; j++) {
            let estado="Abierto"
            let observaciones = null
            let profesor = 100
            if ((this.diaSemana == "Lunes" || this.diaSemana == "Miercoles") && j >= 8 && j <= 10) {
                estado="Cerrado"
                observaciones = "Mantenimiento"
            }
            else if (this.diaSemana == "Sabado" && j == 0) {
                estado="Cerrado"
                observaciones = "Mantenimiento"
            }
            else if (this.diaSemana == "Sabado" && j>=8) {
                return
            }
            
            if (j>= 4 && j <= 7)
                profesor = 200
            else if (j>= 8 && j <= 11)
                profesor = 300
            else if (j>= 12 && j <= 14)
                profesor = 400

            var docData = {
                capacidadCubierta: parseInt(0),
                capacidadTotal: parseInt(16),
                estado: estado,
                fecha: this.generarFormatoFechaBD(),
                horaFin: horariosFin[j],
                horaInicio: horariosInicio[j],
                id: this.generarFormatoFechaBD() + "." + horariosInicio[j],
                observaciones: observaciones,
                profesor: profesor,
                dia: this.diaSemana
            };
            console.log(docData.profesor)
            console.log(`db.collection("turno").doc("${this.generarFormatoFechaBD()+ "." + horariosInicio[j]}").set(${docData})`)
            //db.collection("turno").doc(this.generarFormatoFechaBD() + "." + horariosInicio[j]).set(docData)
        }
    }
}

generarFecha = function (dia) {
    let listaDesplegableFecha = document.getElementById("fechas")
    let sum = null
    let semanaElegida = listaDesplegableFecha.options.selectedIndex
    if (semanaElegida == 0)
        sum = hoy.getTime() + semanaEnMilisegundos * 1

    else if (semanaElegida == 1)
        sum = hoy.getTime() + semanaEnMilisegundos * 2
    let lunesSemanaElegida = new Date(sum)
    let fechaElegida = new Fecha(lunesSemanaElegida, dia)
    return fechaElegida

}
