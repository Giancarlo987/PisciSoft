var store = firebase.firestore();

Paginador = function (divPaginador, tabla, tamPagina) {
    this.miDiv = divPaginador
    this.tabla = tabla
    this.tamPagina = tamPagina
    this.pagActual = 1
    this.paginas = Math.floor((this.tabla.rows.length - 1) / this.tamPagina)

    if (this.tabla.rows.length - 1 > this.paginas * this.tamPagina) { //Por si es mayor al calculo realizado
        this.paginas = this.paginas + 1
    }

    this.SetPagina = function (num) {
        if (num < 0 || num > this.paginas) {
            console.log("aqui")
            console.log(num)
            console.log(this.paginas)
            return
        }

        this.pagActual = num
        var min = 1 + (this.pagActual - 1) * this.tamPagina
        var max = min + this.tamPagina - 1

        for (var i = 1; i < this.tabla.rows.length; i++) {
            if (i < min || i > max)
                this.tabla.rows[i].style.display = 'none'
            else
                this.tabla.rows[i].style.display = ''
        }
    }

    this.Mostrar = function () {
        var tblPaginador = document.createElement('table')
        tblPaginador.className = "table-primary"
        var fil = tblPaginador.insertRow(tblPaginador.rows.length)
        var ant = fil.insertCell(fil.cells.length)
        ant.innerHTML = '<<'
        ant.className = 'pag_num'
        var self = this
        ant.onclick = function () {
            if (self.pagActual == 1) {
                return
            }
            self.SetPagina(self.pagActual - 1)
            pintar(self.paginas, self.pagActual)
        }
        for (var i = 1; i <= this.paginas; i++) {
            var num1 = fil.insertCell(fil.cells.length);
            num1.innerHTML = `${i}`
            if (i == 1) {
                num1.className = 'pag_selec'
            } else {
                num1.className = 'pag_num'
            } num1.id = i
            num1.onclick = function () {
                if (self.pagActual == this.getAttribute('id')) {
                    return;
                }
                pintar(self.paginas, this.getAttribute('id'))
                self.SetPagina(this.getAttribute('id'))
            }
        }

        var sig = fil.insertCell(fil.cells.length)
        sig.innerHTML = '>>'
        sig.className = 'pag_num'
        sig.onclick = function () {
            if (self.pagActual == self.paginas) {
                return
            }
            self.SetPagina(parseInt(self.pagActual) + 1) //ACA CAMBIO
            pintar(self.paginas, self.pagActual)
        }

        this.miDiv.appendChild(tblPaginador)


        this.SetPagina(this.pagActual)
    }
}



function pintar(tamano, pag_selec) {
    for (var j = 1; j <= tamano; j++) {
        if (j == pag_selec) {
            let pag_seleccionada = document.getElementById(pag_selec)
            pag_seleccionada.className = 'pag_selec'
        } else {
            let pagNoSeleccionada = document.getElementById(j)
            pagNoSeleccionada.className = 'pag_num'
        }
    }
}