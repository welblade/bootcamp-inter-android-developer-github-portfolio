package br.com.dio.app.repositories.core

import java.text.SimpleDateFormat
import java.util.*
val locate =  Locale("pt", "BR")
fun Date.format(pattern: String = "dd/MM/yyyy"): String {
    return SimpleDateFormat(pattern, locate).format(this)
}
