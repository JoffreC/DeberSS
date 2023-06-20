
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

fun main(args: Array<String>) {
    var file = File("src/main/sistemaSolar.json")
    if (file.exists()) {
        var gson = Gson()
        FileReader("src/main/sistemaSolar.json").use { reader ->
            val ssDAO = gson.fromJson(reader, SistemaSolarDAO::class.java)
            var controller = Controller()
            controller.ss = ssDAO
            Menu()
            controller.opcionesPrincipales()
        }
    } else {
        var controller = Controller()
        Menu()
        controller.opcionesPrincipales()
    }

}

class Controller() {
    var sc = Scanner(System.`in`)
    var ss = SistemaSolarDAO()
    fun opcionesSP(numSS: Int) {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                menuActualizarSS()
                opcionesASS(numSS)
            }
            (2) -> {
                eliminarSistema(numSS)
            }
            (3) -> {
                Menu()
                opcionesPrincipales()
            }
        }
    }

    fun opcionesPrincipales() {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                var atr = leerAtributosSS()
                crearSS(atr)
                Menu()
                opcionesPrincipales()
            }
            (2) -> {
                mostrarSistemas()
                Menu()
                opcionesPrincipales()
            }
            (3) -> {
                mostrarSistemas()
                mostrarMensaje("Ingrese el número de sistema a actualizar: ")
                var numSS = Integer.parseInt(sc.nextLine())
                menuActualizarSS()
                opcionesASS(numSS)
            }
            (4) -> {
                mostrarSistemas()
                mostrarMensaje("Ingrese el número de sistema a eliminar: ")
                var numSS = Integer.parseInt(sc.nextLine())
                eliminarSistema(numSS)
            }
            (5) -> {
                System.out
                ss.guardar()
            }
        }
    }

    private fun mostrarSistemas() {
        for (i in 0 until (ss.getSistemas().size + 1)) {
            var sistema = ss.getSistema(i)
            if (sistema != null) {
                mostrarSistema(i, sistema)
            }
        }
    }

    private fun crearSS(atr: Array<Any?>) {
        var nombre = atr[0].toString()
        var numPlanetas = Integer.parseInt(atr[1].toString())
        var extension = atr[2].toString().toDouble()
        var estrellaCentral = atr[3].toString().toCharArray()[0]
        var planetas: ArrayList<Planeta> = arrayListOf()
        for (i in 0 until numPlanetas) {
            var atrP = leerAtributosPl(i + 1)
            var nombreP = atrP.get(0).toString()
            var numeroLunas = Integer.parseInt(atrP.get(1)?.toString())
            var tieneVida = false
            if(Integer.parseInt(atrP.get(2).toString()) == 1){
                tieneVida = true
            }
            var diametro = atrP.get(3).toString().toDouble()
            var clasificacion = atrP.get(4).toString().toCharArray().get(0)
            planetas.add(ss.createPlaneta(nombreP, numeroLunas, tieneVida, diametro, clasificacion))
        }
        ss.createSistemaSolar(nombre, numPlanetas, planetas, extension, estrellaCentral)
        mostrarMensaje("Sistema creado exitosamente")
        mostrarMensaje("---------------------------")

    }

    private fun eliminarSistema(numSS: Int) {
        var bool = ss.deleteSistemaSolar(numSS)
        if (bool) {
            mostrarMensaje("El sistema solar ha sido eliminado exitosamente")
            mostrarMensaje("-----------------------------------------------")
        } else {
            mostrarMensaje("Error al intentar borrar el sistema solar")
            mostrarMensaje("-----------------------------------------")
        }
        Menu()
        opcionesPrincipales()
    }

    private fun actualizarSistema(numAtr: Int, idSS: Int, atrib: String) {
        var exito: Boolean = false
        when (numAtr) {
            (1) -> {
                println(idSS)
                exito = ss.updateNombreSS(idSS, atrib)
            }
            (2) -> {
                exito = ss.updateExtensionSS(idSS, atrib.toDouble())
            }
            (3) -> {
                exito = ss.updateFechaSS(idSS, atrib)
            }
        }
        if (exito) {
            mostrarMensaje("Actualización exitosa")
            mostrarMensaje("---------------------")
        } else {
            mostrarMensaje("Error en la actualización")
            mostrarMensaje("-------------------------")
        }
        menuSP()
        opcionesSP(idSS)
    }

    private fun opcionesASS(numSS: Int) {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                mostrarMensaje("Ingrese el nuevo nombre: ")
                var nombre = sc.nextLine()
                println(numSS)
                actualizarSistema(1, numSS, nombre)
            }
            (2) -> {
                mostrarMensaje("Ingrese la nueva extension: ")
                var extension = sc.nextLine()
                actualizarSistema(2, numSS, extension)
            }
            (3) -> {
                mostrarMensaje("Ingrese la nueva estrella central G: estrella amarilla M: enana roja O: estrella masiva: ")
                var estrella = sc.nextLine()
                actualizarSistema(3, numSS, estrella)
            }
            (4) -> {
                agregarPlaneta(numSS)
                menuActualizarSS()
                opcionesASS(numSS)
            }
            (5) -> {
                eliminarPl(numSS)
                menuActualizarSS()
                opcionesASS(numSS)
            }
            (6) -> {
                actualizarInfoPlaneta(numSS)
            }
            (7) -> {
                menuSP()
                opcionesSP(numSS)
            }
        }
    }

    private fun actualizarInfoPlaneta(numSS: Int) {
        var sis = ss.getSistema(numSS)
        var arrPL = sis?.getPlanetas()
        mostrarPlanetas(arrPL)
        mostrarMensaje("Ingrese el número de planeta que desea actualizar: ")
        var numPL = Integer.parseInt(sc.nextLine())
        menuActualizarPl()
        opcionesAPl(numSS, numPL)
    }

    private fun actualizarPlaneta(numSS: Int, numPl: Int, opt: Int, atrib: String) {
        var exito: Boolean = false
        when (opt) {
            (1) -> {
                exito = ss.updateNombrePl(numSS, numPl, atrib)
            }

            (2) -> {
                exito = ss.updateNumLPl(numSS, numPl, Integer.parseInt(atrib))
            }
            (3) -> {
                exito = ss.updateDiametroPl(numSS, numPl, atrib.toDouble())
            }
            (4) -> {
                exito = ss.updateExistenciaVidaPl(numSS, numPl)
            }
            (5) -> {
                exito = ss.updateClasificacionPl(numSS, numPl, atrib.toCharArray()[0])
            }
        }
        if (exito) {
            mostrarMensaje("Actualización de planeta exitosa")
            mostrarMensaje("--------------------------------")
        } else {
            mostrarMensaje("Error en la actualización de planeta")
            mostrarMensaje("------------------------------------")
        }
        menuSP()
        opcionesSP(numSS)
    }

    private fun eliminarPl(numSS: Int) {
        var sis = ss.getSistema(numSS)
        var arrPL = sis?.getPlanetas()
        mostrarPlanetas(arrPL)
        mostrarMensaje("Ingrese el número de planeta a eliminar: ")
        var numPL = Integer.parseInt(sc.nextLine())
        eliminarPlaneta(numSS, numPL)
    }


    private fun eliminarPlaneta(numSS: Int, numPl: Int) {
        var bool = ss.deletePlaneta(numSS, numPl)
        if (bool) {
            ss.getSistemas().get(numSS)?.setNumeroPlanetas(ss.getSistemas().get(numSS)!!.getNumeroPlanetas() - 1)
            mostrarMensaje("Eliminación exitosa")
            mostrarMensaje("-------------------")
        } else {
            mostrarMensaje("Error en la eliminación")
            mostrarMensaje("-----------------------")
        }
        menuSP()
        opcionesSP(numSS)
    }

    private fun agregarPlaneta(sistema: Int) {
        var sis = ss.getSistema(sistema)
        if (sis != null) {
            var planetas = sis.getPlanetas()
            var numPla = sis.getNumeroPlanetas()
            mostrarMensaje("Ingrese los atributos del nuevo planeta: ")
            var atrP = leerAtributosPl(numPla + 1)
            var nombreP = atrP.get(0).toString()
            var numeroLunas = Integer.parseInt(atrP.get(1)?.toString())
            var tieneVida: Boolean = false
            if(Integer.parseInt(atrP.get(2).toString()) == 0){
                tieneVida = true
            }
            var diametro = atrP.get(3).toString().toDouble()
            var clasificacion = atrP.get(4).toString().toCharArray().get(0)
            planetas.add(ss.createPlaneta(nombreP, numeroLunas, tieneVida, diametro, clasificacion))
            sis.setNumeroPlanetas(numPla + 1)
        }
    }

    private fun opcionesAPl(numSS: Int, numPl: Int) {
        var opcion = Integer.parseInt(sc.nextLine())
        mostrarMensaje("--Actualización del Planeta " + (numPl + 1) + "--")
        when (opcion) {
            (1) -> {
                mostrarMensaje("Ingrese el nuevo nombre: ")
                var nombre = sc.nextLine()
                actualizarPlaneta(numSS, numPl, 1, nombre)
            }
            (2) -> {
                mostrarMensaje("Ingrese el nuevo número de lunas: ")
                var numLunas = sc.nextLine()
                actualizarPlaneta(numSS, numPl, 2, numLunas)
            }
            (3) -> {
                mostrarMensaje("Ingrese el nuevo diámetro: ")
                var diametro = sc.nextLine()
                actualizarPlaneta(numSS, numPl, 3, diametro)
            }
            (4) -> {
                actualizarPlaneta(numSS, numPl, 4, "")
            }
            (5) -> {
                mostrarMensaje("Ingrese la nueva clasificacion T: planetas rocosos G: planetas gaseosos H: planetas de hielo ")
                var clasificacion = sc.nextLine()
                actualizarPlaneta(numSS, numPl, 5, clasificacion)
            }
        }
    }
}

fun mostrarPlanetas(arrPl: MutableList<Planeta>?) {
    if (arrPl != null) {
        for (j in 0 until arrPl.size) {
            var planeta = arrPl.get(j)
            println("Planeta " + (j + 1))
            println("nombre: " + planeta?.getNombre())
            println("número de lunas: " + planeta?.getNumeroLunas())
            println("diámetro: " + planeta?.getDiametro())
            println("habitado: " + planeta?.getHabitado())
            println("clasificación: " + planeta?.getClasificacion())
        }
    }
}

fun menuActualizarPl() {
    println("-------Actualización de Planeta-------")
    println("1.- Actualizar nombre")
    println("2.- Actualizar número de lunas")
    println("3.- Actualizar diametro")
    println("4.- Actualizar si existe vida")
    println("5.- Actualizar clasificación")
    println("6.- Atras")
    print("Opción: ")
}

fun menuActualizarSS() {
    println("-------Actualización de información de Sistema Solar-------")
    println("1.- Actualizar nombre")
    println("2.- Actualizar extensión")
    println("3.- Actualizar fecha descubrimiento")
    println("4.- Agregar planeta")
    println("5.- Eliminar planeta")
    println("6.- Actualizar planeta")
    println("7.- Atras")
    print("Opción: ")
}

fun Menu() {
    println("-------Sistema CRUD para Sistema Solares-------")
    println("1.- Agregar Sistema solar")
    println("2.- Mostrar sistemas solares")
    println("3.- Actualizar Sistema Solar")
    println("4.- Borrar Sistema Solar")
    println("5.- Salir")
    print("Opción: ")
}

fun menuSP() {
    println("1.- Actualizar sistema")
    println("2.- Eliminar sistema")
    println("3.- Menú Principal")
    print("Opción: ")
}

fun leerAtributosPl(numPlaneta: Int): Array<Any?> {
    var arr = arrayOfNulls<Any?>(5)
    var sc = Scanner(System.`in`)
    println()
    println("Planeta " + numPlaneta)
    println("Ingrese el nombre: ")
    var n = sc.nextLine()
    arr[0] = n
    println("Ingrese el número de lunas")
    var nl = sc.nextLine()
    arr[1] = nl
    println("Esta habitado 1:Si 0:No")
    var b = sc.nextLine()
    arr[2] = b
    println("Ingrese el diametro")
    var d = sc.nextLine()
    arr[3] = d
    println("Ingrese la clasificacion T: planetas rocosos G: planetas gaseosos H: planetas de hielo")
    var fd = sc.nextLine()
    arr[4] = fd
    return arr
}

fun leerAtributosSS(): Array<Any?> {
    var atr = arrayOfNulls<Any?>(5)
    var sc = Scanner(System.`in`)
    println("Ingrese el nombre: ")
    atr[0] = sc.nextLine()
    println("Ingrese el número de planetas:")
    atr[1] = sc.nextLine()
    println("Ingrese la extensión:")
    atr[2] = sc.nextLine()
    println("Ingrese la estrella central G: estrella amarilla M: enana roja O: estrella masiva:")
    atr[3] = sc.nextLine()
    return atr
}

fun mostrarMensaje(mensaje: String) {
    println(mensaje)
}

fun mostrarSistema(id: Int, sistema: SistemaSolar) {
    println("Sistema Solar " + (id))
    println("nombre: " + sistema.getNombre())
    println("extensión: " + sistema.getExtension())
    println("estrella central: " + sistema.getEstrellaCentral())
    println("número planetas: " + sistema.getNumeroPlanetas())
    for (j in 0 until sistema.getNumeroPlanetas()) {
        var planeta = sistema.getPlanetas().get(j)
        println("Planeta " + (j + 1))
        println("nombre: " + planeta?.getNombre())
        println("número de lunas: " + planeta?.getNumeroLunas())
        println("diámetro: " + planeta?.getDiametro())
        println("habitado: " + planeta?.getHabitado())
        println("clasificación: " + planeta?.getClasificacion())
    }
    println("")
}

class SistemaSolar(
    private var nombre: String,
    private var numeroPlanetas: Int,
    private var planetas: MutableList<Planeta>,
    private var extension: Double,
    private var estrellaCentral: Char
) {
    init {
        this.nombre
        this.numeroPlanetas
        this.planetas
        this.extension
        this.estrellaCentral
    }

    constructor(
        nombre: String,
        numeroPlanetas: Int,
        planetas: MutableList<Planeta>,
        extension: Double?,
        estrellaCentral: Char
    ) : this(
        nombre,
        numeroPlanetas,
        planetas,
        if (extension == null) 0.0 else extension,
        estrellaCentral
    )

    fun getNombre(): String {
        return nombre
    }

    fun getNumeroPlanetas(): Int {
        return numeroPlanetas
    }

    fun getPlanetas(): MutableList<Planeta> {
        return planetas
    }

    fun getExtension(): Double {
        return extension
    }

    fun getEstrellaCentral(): Char {
        return estrellaCentral
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun setNumeroPlanetas(numeroPlanetas: Int) {
        this.numeroPlanetas = numeroPlanetas
    }

    fun setPlanetas(planetas: MutableList<Planeta>) {
        this.planetas = planetas
    }

    fun setExtension(extension: Double) {
        this.extension = extension
    }

    fun setExtrellaCentral(estrellaCentral: Char) {
        this.estrellaCentral = estrellaCentral
    }
}

class SistemaSolarDAO {
    private val sistemaSolarList = HashMap<Int, SistemaSolar>()
    var id = 0
    fun createSistemaSolar(
        name: String,
        numPlanetas: Int,
        planetas: MutableList<Planeta>,
        extension: Double,
        estrellaCentral: Char
    ): SistemaSolar {
        val sistema = SistemaSolar(name, numPlanetas, planetas, extension, estrellaCentral)
        id++
        sistemaSolarList[id] = sistema
        return sistema
    }

    fun createPlaneta(
        name: String,
        numLunas: Int,
        habitado: Boolean,
        diametro: Double,
        clasificacion: Char
    ): Planeta {
        val planeta = Planeta(name, numLunas, habitado, diametro, clasificacion)
        return planeta
    }

    fun deletePlaneta(ss: Int, id: Int): Boolean {
        var planeta = sistemaSolarList[ss]?.getPlanetas()?.get(id - 1)

        if (planeta != null) {
            sistemaSolarList[ss]?.getPlanetas()?.remove(planeta)
            return true
        } else {
            return false
        }
    }

    fun getSistemas(): HashMap<Int, SistemaSolar> {
        return sistemaSolarList
    }


    fun getSistema(id: Int): SistemaSolar? {
        return sistemaSolarList[id]
    }

    fun deleteSistemaSolar(id: Int): Boolean {
        val sistemaSolar = getSistema(id)
        if (sistemaSolar != null) {
            sistemaSolarList.remove(id)
            return true
        } else {
            return false
        }
    }

    fun updateNombreSS(idSS: Int, nombre: String): Boolean {
        var sistema = sistemaSolarList[idSS]
        if (sistema != null) {
            sistema.setNombre(nombre)
            return true
        } else {
            return false
        }
    }

    fun updateExtensionSS(idSS: Int, extension: Double): Boolean {
        var sistema = sistemaSolarList[idSS]
        if (sistema != null) {
            sistema?.setExtension(extension)
            return true
        } else {
            return false
        }
    }

    fun updateFechaSS(idSS: Int, atrib: String): Boolean {
        var estrellaCentral = atrib.toString().toCharArray()[0]
        var sistema = sistemaSolarList[idSS]
        if (sistema != null) {
            sistema?.setExtrellaCentral(estrellaCentral)
            return true
        } else {
            return false
        }
    }

    fun updateNombrePl(numSS: Int, numPl: Int, nombre: String): Boolean {
        var planeta = sistemaSolarList.get(numSS)?.getPlanetas()?.get(numPl)
        if (planeta != null) {
            planeta.setNombre(nombre)
            return true
        } else {
            return false
        }
    }

    fun updateNumLPl(numSS: Int, numPl: Int, numL: Int): Boolean {
        var planeta = sistemaSolarList.get(numSS)?.getPlanetas()?.get(numPl)
        if (planeta != null) {
            planeta.setNumeroLunas(numL)
            return true
        } else {
            return false
        }
    }

    fun updateDiametroPl(numSS: Int, numPl: Int, diametro: Double): Boolean {
        var planeta = sistemaSolarList.get(numSS)?.getPlanetas()?.get(numPl)
        if (planeta != null) {
            planeta.setDiametro(diametro)
            return true
        } else {
            return false
        }
    }

    fun updateExistenciaVidaPl(numSS: Int, numPl: Int): Boolean {
        var planeta = sistemaSolarList.get(numSS)?.getPlanetas()?.get(numPl)
        if (planeta != null) {
            if (planeta.getHabitado()) {
                planeta.setHabitado(false)
            } else {
                planeta.setHabitado(true)
            }
            return true
        } else {
            return false
        }
    }

    fun updateClasificacionPl(numSS: Int, numPl: Int, clasificacion: Char): Boolean {
        var planeta = sistemaSolarList.get(numSS)?.getPlanetas()?.get(numPl)
        if (planeta != null) {
            planeta.setClasificacion(clasificacion)
            return true
        } else {
            return false
        }
    }

    fun guardar() {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()
        val json = gson.toJson(this)
        FileWriter("src/main/sistemaSolar.json").use { writer ->
            writer.write(json)
        }

    }
}

class Planeta(
    private var nombre: String,
    private var numeroLunas: Int,
    private var habitado: Boolean,
    private var diametro: Double,
    private var clasificacion: Char
) {
    init {
        this.nombre
        this.numeroLunas
        this.habitado
        this.diametro
        this.clasificacion
    }

    fun getNombre(): String {
        return nombre
    }

    fun getNumeroLunas(): Int {
        return numeroLunas
    }

    fun getHabitado(): Boolean {
        return habitado
    }

    fun getDiametro(): Double {
        return diametro
    }

    fun getClasificacion(): Char {
        return clasificacion
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun setNumeroLunas(numeroLunas: Int) {
        this.numeroLunas = numeroLunas
    }

    fun setHabitado(habitado: Boolean) {
        this.habitado = habitado
    }

    fun setDiametro(diametro: Double) {
        this.diametro = diametro
    }

    fun setClasificacion(clasificacion: Char) {
        this.clasificacion = clasificacion
    }
}
