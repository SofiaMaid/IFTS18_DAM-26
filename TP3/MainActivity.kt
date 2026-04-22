package com.example.calculadoraimc

// Imports necesarios para que funcione
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Imports de Compose
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

import androidx.compose.material3.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions


// Activity principal que se ejecuta al iniciar la app
class MainActivity : ComponentActivity() {

    // Método que se ejecuta al crear la Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent permite usar Jetpack Compose en lugar de XML
        setContent {
            PantallaIngreso(this)
        }
    }
}

// Composable que representa la pantalla de ingreso
@Composable
fun PantallaIngreso(context: Context) {

    // Variables de estado (persisten ante rotación de pantalla)
    // Nota: Para calcular el IMC necesito: Peso (kg) / Altura^2 (m²)
    var nombre by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var altura by rememberSaveable { mutableStateOf("") }

    // Variable para mostrar errores
    var error by remember { mutableStateOf("") }

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .verticalScroll(rememberScrollState()) // Permite scroll
            .padding(16.dp), // Espaciado

        verticalArrangement = Arrangement.Center, // Centrado vertical
        horizontalAlignment = Alignment.CenterHorizontally // Centrado horizontal
    ) {

        // Título principal
        Text(
            text = "Calculadora de IMC",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Texto con instrucción general
        Text(
            text = "Por favor, completa tus datos:",
            fontSize = 20.sp,
        )

        // Campo nombre
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo peso
        TextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo altura
        TextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón calcular
        Button(onClick = {

            // Validación de campos vacíos
            if (nombre.isEmpty() || peso.isEmpty() || altura.isEmpty()) {
                error = "Por favor, completa todos los campos."
            } else {

                // Conversión segura a Double
                val pesoDouble = peso.toDoubleOrNull()
                val alturaDouble = altura.toDoubleOrNull()

                // Validación numérica
                if (pesoDouble == null || alturaDouble == null) {
                    error = "Por favor, ingresa valores numéricos válidos."
                } else {

                    // Cálculo del IMC
                    val imc = pesoDouble / (alturaDouble * alturaDouble)

                    // Intent para pasar a ResultActivity
                    val intent = Intent(context, ResultActivity::class.java)

                    // Envío de datos
                    intent.putExtra("nombre", nombre)
                    intent.putExtra("imc", imc)

                    // Inicio nueva pantalla
                    context.startActivity(intent)
                }
            }

        }) {
            Text("Calcular IMC")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mostrar error si existe
        Text(error)
    }
}
