import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


fun main(args: Array<String>) {
    var controller = Controller()
    Menu()
    controller.opcionesPrincipales()
}

class Controller() {
    var sc = Scanner(System.`in`)
    var ss = SistemaSolarRepository()
    var sp = PlanetaRepository()
    fun opcionS() {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                solicitarSS("sistema solar para mostrar informacion de sus planetas: ")
                var numSS = Integer.parseInt(sc.nextLine()) - 1
                mostrarInformacionSS(numSS)
                opcionesSP(numSS)
            }
            (2) -> {
                Menu()
                opcionesPrincipales()
            }
        }
    }

    fun opcionesSP(numSS: Int) {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                var atr = leerAtributosPl(1)
                crearPls(numSS, 1, atr)
            }
            (2) -> {
                actualizarPlaneta(numSS)
            }
            (3) -> {
                eliminarPlaneta(numSS)
            }
            (4) -> {
                Menu()
                opcionesPrincipales()
            }
        }
    }

    private fun eliminarPlaneta(numSS:Int) {
        var id = Integer.parseInt(sc.nextLine())
        var bool = sp.deletePlaneta(numSS,id)
        if(bool) {
            mensajeConfirmacion("ha sido eliminado exitosamente")
        }else{
            mensajeConfirmacion("no ha sido eliminado debido a un error")
        }
        menuSP()
        opcionesSP(numSS)
    }

    private fun actualizarPlaneta(numSS:Int) {
        solicitarSS("planeta para actualizar: ")
        var id = Integer.parseInt(sc.nextLine())
        var atrP = leerAtributosPl(1)
        var nombreP = atrP.get(0)?.get(0).toString()
        var numeroLunas = Integer.parseInt(atrP.get(0)?.get(1).toString())
        var tieneVida = atrP.get(0)?.get(2).toString().toBoolean()
        var diametro = atrP.get(0)?.get(3).toString().toDouble()
        var clasificacion = atrP.get(0)?.get(4).toString().toCharArray()[0]
        var bool = sp.updatePlaneta(numSS,id,nombreP,numeroLunas,tieneVida,diametro,clasificacion)
        if(bool){
            mensajeConfirmacion("ha sido actualizado exitosamente")
        }else{
            mensajeConfirmacion("no ha sido actualizado debido a un error")
        }
        menuSP()
        opcionesSP(numSS)
    }

    private fun mostrarInformacionSS(numSS: Int) {
        var s = sp.getPlanetas()
        mostrarInformacionPlaneta(numSS, s[numSS])
        menuSP()
    }

    fun opcionesPrincipales() {
        var opcion = Integer.parseInt(sc.nextLine())
        when (opcion) {
            (1) -> {
                var atr = leerAtributosSS()
                crearSS(atr)
            }
            (2) -> {
                mostrarSistemas()
            }
            (3) -> {
                actualizarSistema()
            }
            (4) -> {
                eliminarSistema()
            }
            (5) -> {
                System.out
            }
        }
    }

    private fun eliminarSistema() {
        solicitarSS("sistema solar a eliminar: ")
        var sis = Integer.parseInt(sc.nextLine())
        var bool = ss.deleteSistemaSolar(sis)
        if(bool) {
            mensajeConfirmacion("ha sido eliminado exitosamente")
        }else{
            mensajeConfirmacion("no ha sido eliminado debido a un error")
        }
        Menu()
        opcionesPrincipales()
    }

    private fun actualizarSistema() {
        solicitarSS("sistema solar a actualizar: ")
        var sis = Integer.parseInt(sc.nextLine())
        mostrarSistema(sis,ss.getSistemas(),sp.getPlanetas())
        var atr = leerAtributosSS()
        var nombre = atr[0].toString()
        var numPlanetas = Integer.parseInt(atr[1].toString())
        var presenciaVida = atr[2].toString().toBoolean()
        var extension = atr[3].toString().toDouble()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var fecha = LocalDate.parse(atr[4].toString(), dateFormatter)
        var bool = ss.updateSistemaSolar(sis, nombre, numPlanetas, presenciaVida, extension, fecha)
        if(bool) {
            mensajeConfirmacion("ha sido actualizado exitosamente")
        }else{
            mensajeConfirmacion("no ha sido actualizado debido a un error")
        }
        Menu()
        opcionesPrincipales()
    }

    private fun mostrarSistemas() {
        var sistemasSolares = ss.getSistemas()
        var planetas = sp.getPlanetas()
        var scr = mostrarSistemasSolares(sistemasSolares, planetas)
        if (scr == 0) {
            Menu()
            opcionesPrincipales()
        } else {
            mostrarMenuSistema()
            opcionS()
        }
    }

    private fun crearSS(atr: Array<Any?>) {
        var nombre = atr[0].toString()
        var numPlanetas = Integer.parseInt(atr[1].toString())
        var presenciaVida = atr[2].toString().toBoolean()
        var extension = atr[3].toString().toDouble()
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var fecha = LocalDate.parse(atr[4].toString(), dateFormatter)
        ss.createSistemaSolar(nombre, numPlanetas, presenciaVida, extension, fecha)
        var atrP = leerAtributosPl(numPlanetas)
        crearPls(ss.getId(), numPlanetas, atrP)
    }

    private fun crearPls(sistema: Int, numPlanetas: Int, atrP: HashMap<Int, Array<Any?>>) {
        for (i in 0 until numPlanetas) {
            var nombreP = atrP.get(i)?.get(0).toString()
            var numeroLunas = Integer.parseInt(atrP.get(i)?.get(1).toString())
            var tieneVida = atrP.get(i)?.get(2).toString().toBoolean()
            var diametro = atrP.get(i)?.get(3).toString().toDouble()
            var clasificacion = atrP.get(i)?.get(4).toString().toCharArray()[0]
            sp.createPlaneta(sistema, nombreP, numeroLunas, tieneVida, diametro, clasificacion)
        }
        menuSP()
        opcionesSP(ss.getId())
    }
}
fun mensajeConfirmacion(complemento:String){
    println("El elemento $complemento ")
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
    println("1.- Agregar planeta")
    println("2.- Actualizar planeta")
    println("3.- Borrar planeta")
    println("4.- Menú Principal")
    print("Opción: ")
}

fun leerAtributosPl(numPlanetas: Int): HashMap<Int, Array<Any?>> {
    var atrPl = HashMap<Int, Array<Any?>>()
    var sc = Scanner(System.`in`)
    for (i in 0 until numPlanetas) {
        println()
        var arr = arrayOfNulls<Any?>(5)
        println("Planeta " + (i + 1))
        println("Ingrese el nombre: ")
        var n = sc.nextLine()
        arr[0] = n
        println("xd" + atrPl.get(i)?.set(0, n))
        println("Ingrese el número de lunas")
        var nl = sc.nextLine()
        arr[1] = nl
        println("Esta habitado: 0:Si 1:No")
        var b = sc.nextLine()
        arr[2] = b
        println("Ingrese el diametro")
        var d = sc.nextLine()
        arr[3] = d
        println("Ingrese la clasificacion c: carbono o: oxigeno")
        var fd = sc.nextLine()
        arr[4] = fd
        atrPl[i] = arr
    }
    return atrPl
}

fun leerAtributosSS(): Array<Any?> {
    var atr = arrayOfNulls<Any?>(5)
    var sc = Scanner(System.`in`)
    println("Ingrese el nombre: ")
    atr[0] = sc.nextLine()
    println("Ingrese el número de planetas")
    atr[1] = sc.nextLine()
    println("Tiene planetas con vida: 0:Si 1:No")
    atr[2] = sc.nextLine()
    println("Ingrese la extensión")
    atr[3] = sc.nextLine()
    println("Ingrese la fecha de descubrimiento en formato dd/MM/aaaa")
    atr[4] = sc.nextLine()
    return atr
}

fun mostrarSistema(id: Int, sistemas: HashMap<Int, SistemaSolar>, planetas: HashMap<Int, MutableList<Planeta>>) {
    if (sistemas.size > 0) {
        var i = sistemas.get(id)
        if (i != null) {
            println("Sistema Solar " + (id + 1))
            println("nombre: " + i.getNombre())
            println("presencia de vida: " + (if (i.getTieneVida()) "Si" else "No"))
            println("extensión: " + i.getExtension())
            println("fecha descubrimiento: " + i.getFechaDescubrimiento())
            println("número planetas: " + i.getNumeroPlanetas())
            for (j in 0 until i.getNumeroPlanetas()) {
                var planeta = planetas.get(j)?.get(j)
                println("Planeta " + (j + 1))
                println("nombre: " + planeta?.getNombre())
            }
            println("")
        }
    }
}

fun mostrarSistemasSolares(sistemas: HashMap<Int, SistemaSolar>, planetas: HashMap<Int, MutableList<Planeta>>): Int {
    var id = 0
    if (sistemas.size > 0) {
        for (index in 0 until sistemas.size) {
            var i = sistemas.get(index)
            if (i != null) {
                id++
                println("Sistema Solar " + id)
                println("nombre: " + i.getNombre())
                println("presencia de vida: " + (if (i.getTieneVida()) "Si" else "No"))
                println("extensión: " + i.getExtension())
                println("fecha descubrimiento: " + i.getFechaDescubrimiento())
                println("número planetas: " + i.getNumeroPlanetas())
                for (j in 0 until i.getNumeroPlanetas()) {
                    var planeta = planetas.get(j)?.get(j)
                    println("Planeta " + (j + 1))
                    println("nombre: " + planeta?.getNombre())
                }
                println("")
            }
        }
        return 1
    } else {
        println("No hay sistemas solares aún")
        return 0
    }
}

fun mostrarMenuSistema() {
    println("-------Opciónes-------")
    println("1.- Ver informacion de planetas de un sistema solar")
    println("2.- Ver informacion de planetas de todos los sistemas solares")
    println("3.- Regresar a menú principal")
    print("Opción: ")
}

fun solicitarSS(complemento: String) {
    print("Ingrese el id del $complemento")
}

fun mostrarInformacionPlaneta(sistema: Int, planetas: MutableList<Planeta>?) {
    println("Planetas del sistema: $sistema")
    if (planetas != null) {
        for (j in 0 until planetas.size) {
            var planeta = planetas.get(j)
            println("Planeta " + (j + 1))
            println("nombre: " + planeta?.getNombre())
            println("número de lunas: " + planeta?.getNumeroLunas())
            println("diámetro: " + planeta?.getDiametro())
            println("habitado: " + planeta?.getHabitado())
            println("clasificación: " + planeta?.getClasificacion())
        }
    }
}

class SistemaSolar(
    private var nombre: String,
    private var numeroPlanetas: Int,
    private var presenciaVida: Boolean,
    private var extension: Double,
    private var fechaDescubrimiento: LocalDate
) {
    init {
        this.nombre
        this.numeroPlanetas
        this.presenciaVida
        this.extension
        this.fechaDescubrimiento
    }

    constructor(
        nombre: String,
        numeroPlanetas: Int,
        presenciaVida: Boolean,
        extension: Double?,
        fechaDescubrimiento: LocalDate?
    ) : this(
        nombre,
        numeroPlanetas,
        presenciaVida,
        if (extension == null) 0.0 else extension,
        if (fechaDescubrimiento == null) LocalDate.of(0, 1, 1) else fechaDescubrimiento
    )

    fun getNombre(): String {
        return nombre
    }

    fun getNumeroPlanetas(): Int {
        return numeroPlanetas
    }

    fun getTieneVida(): Boolean {
        return presenciaVida
    }

    fun getExtension(): Double {
        return extension
    }

    fun getFechaDescubrimiento(): LocalDate {
        return fechaDescubrimiento
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun setNumeroPlanetas(numeroPlanetas: Int) {
        this.numeroPlanetas = numeroPlanetas
    }

    fun setPresenciaVida(habitado: Boolean) {
        this.presenciaVida = habitado
    }

    fun setExtension(extension: Double) {
        this.extension = extension
    }

    fun setFechaDescubrimiento(fechaDescubrimiento: LocalDate) {
        this.fechaDescubrimiento = fechaDescubrimiento
    }
}

class SistemaSolarRepository {
    private val sistemaSolarList = HashMap<Int, SistemaSolar>()
    private var id = 0
    fun createSistemaSolar(
        name: String,
        numPlanetas: Int,
        presenciaVida: Boolean,
        extension: Double,
        fechaDescubrimiento: LocalDate
    ) {
        val sistema = SistemaSolar(name, numPlanetas, presenciaVida, extension, fechaDescubrimiento)
        id++
        sistemaSolarList[id] = sistema
    }

    fun getSistemas(): HashMap<Int, SistemaSolar> {
        return sistemaSolarList
    }

    fun getId(): Int {
        return id
    }

    fun updateSistemaSolar(
        id: Int,
        nombreNuevo: String,
        numeroPlanetas: Int,
        presenciaVida: Boolean,
        extension: Double,
        fecha: LocalDate
    ): Boolean {
        val sistemaSolar = getSistema(id)
        if (sistemaSolar != null) {
            sistemaSolar.setNombre(nombreNuevo)
            sistemaSolar.setNumeroPlanetas(numeroPlanetas)
            sistemaSolar.setPresenciaVida(presenciaVida)
            sistemaSolar.setExtension(extension)
            sistemaSolar.setFechaDescubrimiento(fecha)
            return true
        } else {
            return false
        }
    }

    fun getSistema(id: Int): SistemaSolar? {
        return sistemaSolarList.get(id)
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

class PlanetaRepository {
    private val planetaList = HashMap<Int, MutableList<Planeta>>()
    fun createPlaneta(
        id: Int,
        name: String,
        numLunas: Int,
        habitado: Boolean,
        diametro: Double,
        clasificacion: Char
    ) {
        val planeta = Planeta(name, numLunas, habitado, diametro, clasificacion)
        planetaList[id]?.add(planeta)
    }

    fun getPlanetas(): HashMap<Int, MutableList<Planeta>> {
        return planetaList
    }
    fun getPlaneta(ss:Int,id:Int):Planeta?{
        return planetaList[ss]?.get(id)
    }
    fun updatePlaneta(ss:Int, id:Int, nombre:String, numeroLunas:Int, habitado:Boolean, diametro:Double, clasificacion:Char):Boolean{
        var planeta = planetaList[ss]?.get(id)
        if(planeta != null){
            planeta.setNombre(nombre)
            planeta.setNumeroLunas(numeroLunas)
            planeta.setHabitado(habitado)
            planeta.setDiametro(diametro)
            planeta.setClasificacion(clasificacion)
            return true
        }else{
            return false
        }
    }
    fun deletePlaneta(ss:Int, id:Int):Boolean{
        var planeta = planetaList[ss]?.get(id)
        if(planeta != null) {
            planetaList[ss]?.remove(planeta)
            return true
        }else{
            return false
        }
    }
}