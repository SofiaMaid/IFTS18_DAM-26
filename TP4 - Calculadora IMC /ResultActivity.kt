package com.example.calculadoraimc

// Imports necesarios para que funcione
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


// Activity que muestra el resultado del IMC
class ResultActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupero datos enviados desde MainActivity
        val nombre = intent.getStringExtra("nombre") ?: ""
        val imc = intent.getDoubleExtra("imc", 0.0)

        setContent {
            PantallaResultado(nombre, imc, this)
        }
    }
}

// Composable de la pantalla de resultados
@Composable
fun PantallaResultado(nombre: String, imc: Double, context: Context) {

    // Clasificación del IMC:
    // Nota: Según la OMS se puede clasificar en Bajo peso (menor a 18.5), Peso saludable (18.5-24.9), Sobrepeso (25-29.9) y Obesidad (30 o más)
    val clasificacion = when {
        imc < 18.5 -> "Bajo peso"
        imc < 25 -> "Normal"
        imc < 30 -> "Sobrepeso"
        else -> "Obesidad"
    }

    // SharedPreferences para persistencia
    val prefs = context.getSharedPreferences("IMC_DATA", Context.MODE_PRIVATE)

    // Obtengo último IMC para comparar
    val imcAnterior = prefs.getFloat("ultimo_imc", -1f)

    // Mensaje si bajó el IMC
    val mensajeFelicitacion =
        if (imcAnterior != -1f && imc < imcAnterior)
            "¡Felicitaciones! Bajaste tu IMC 🎉"
        else ""

    // Obtengo historial
    var historial by remember {
        mutableStateOf(
            prefs.getString("historial", "")!!
                .split(",")
                .filter { it.isNotEmpty() }
                .toMutableList()
        )
    }

    // Agrego nuevo valor
    historial.add(String.format("%.2f", imc))

    // Mantengo solo 3 registros
    if (historial.size > 3) historial.removeAt(0)

    // Guardo datos actualizados
    prefs.edit()
        .putString("historial", historial.joinToString(","))
        .putFloat("ultimo_imc", imc.toFloat())
        .apply()

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Título
        Text(
            text = "Resultado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Datos
        Text("Nombre: $nombre")
        Spacer(modifier = Modifier.height(8.dp))

        Text("IMC: %.2f".format(imc))
        Spacer(modifier = Modifier.height(8.dp))

        Text("Clasificación: $clasificacion")

        Spacer(modifier = Modifier.height(12.dp))

        // Mensaje de mejora
        if (mensajeFelicitacion.isNotEmpty()) {
            Text(mensajeFelicitacion)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Historial (solo se muestra si hay datos)
        if (historial.isNotEmpty()) {

            Text("Historial (últimos 3):")
            Spacer(modifier = Modifier.height(8.dp))

            historial.forEach {
                Text(it)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón para volver (porque sino no puedo seguir agregando resultados)
        Button(onClick = {
            // Cierra esta Activity y vuelve a la anterior
            (context as? ComponentActivity)?.finish()
        }) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Botón para borrar datos guardados (para hacer las pruebas)
        Button(onClick = {
            prefs.edit()
                .remove("historial")
                .remove("ultimo_imc")
                .apply()

            // Limpia lista en pantalla
            historial = mutableListOf()

        }) {
            Text("Limpiar historial")
        }
    }
}
