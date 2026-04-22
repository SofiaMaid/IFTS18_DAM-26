package com.example.myapplication

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import kotlin.random.Random
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PantallaInicio()
        }
    }
}

// Cálculos
fun promedioSinParticipacion(a: Int, b: Int): Int = (a + b) / 2

fun promedioConParticipacion(a: Int, b: Int, c: Int): Int = (a + b + c) / 3

// Nota de participación aleatoria
fun generarNotaRandomParticipacion(): Int {
    return Random.nextInt(1, 11) // del 1 al 10
}

@Composable
fun PantallaInicio(modifier: Modifier = Modifier) {
    //Notas a ingresar
    var nombreTexto by remember { mutableStateOf("") }
    var nota1Texto by remember { mutableStateOf("") }
    var nota2Texto by remember { mutableStateOf("") }

    //Resultados
    var notaSinParticipacion by remember { mutableStateOf(0) }
    var notaConParticipacion by remember { mutableStateOf(0) }

    //Otros
    var ultimaOperacion by remember { mutableStateOf("Ninguna") }
    var mensajeRandom by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "App de Notas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por favor, completa los siguientes campos:",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombreTexto,
            onValueChange = { nombreTexto = it },
            label = { Text("Nombre y apellido") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nota1Texto,
            onValueChange = { nota1Texto = it },
            label = { Text("Nota primer parcial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nota2Texto,
            onValueChange = { nota2Texto = it },
            label = { Text("Nota segundo parcial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Botón para calcular promedio sin nota de participación
        Button(onClick = {
            //Validación
            if (nombreTexto.isBlank() || nota1Texto.isBlank() || nota2Texto.isBlank()) {
                mensajeRandom = "Por favor, completa todos los campos" //Chequear campos completos
            } else {

                val n1 = nota1Texto.toIntOrNull()
                val n2 = nota2Texto.toIntOrNull()

                if (n1 == null || n2 == null) {
                    mensajeRandom = "Las notas deben ser numéricas"//Chequear valores numéricos
                } else {
                    notaSinParticipacion = promedioSinParticipacion(n1, n2)
                    ultimaOperacion = "Promedio sin participación"
                    mensajeRandom = ""
                }
            }

        }) {
            Text("Promedio sin participación")
        }

        Spacer(modifier = Modifier.height(8.dp))

        //Botón para calcular promedio con nota de participación
        Button(onClick = {

            if (nombreTexto.isBlank() || nota1Texto.isBlank() || nota2Texto.isBlank()) {
                mensajeRandom = "Por favor, completa todos los campos"
            } else {

                val n1 = nota1Texto.toIntOrNull()
                val n2 = nota2Texto.toIntOrNull()

                if (n1 == null || n2 == null) {
                    mensajeRandom = "Las notas deben ser numéricas"
                } else {

                    val random = generarNotaRandomParticipacion()

                    notaConParticipacion = promedioConParticipacion(n1, n2, random)
                    ultimaOperacion = "Promedio con participación"
                    mensajeRandom = "Nota de participación: $random"
                }
            }

        }) {
            Text("Promedio con participación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pantalla con resultados
        Text(
            text = "Alumno: $nombreTexto",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Promedio sin participación: $notaSinParticipacion",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Promedio con participación: $notaConParticipacion",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = mensajeRandom,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Última operación: $ultimaOperacion",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
