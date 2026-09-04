package com.example.calculadoraimpresiones

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reglas de negocio
private const val PRECIO_FIJO = 0.50
private const val PRECIO_POR_PAGINA = 0.01
private const val PAGINAS_POR_HOJA = 2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculadoraImpresionesScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraImpresionesScreen() {
    var cantidadText by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf("paginas") } // "paginas" o "hojas"

    // Cálculo dinámico del total
    val total: Double = remember(cantidadText, tipoSeleccionado) {
        val cantidad = cantidadText.toIntOrNull() ?: 0
        if (cantidad <= 0) {
            0.0
        } else {
            val paginas = if (tipoSeleccionado == "paginas") cantidad else cantidad * PAGINAS_POR_HOJA
            PRECIO_FIJO + (PRECIO_POR_PAGINA * paginas)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CALCULADORA DE IMPRESIONES",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo de texto para la cantidad
                OutlinedTextField(
                    value = cantidadText,
                    onValueChange = { input ->
                        // Solo permite números positivos
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            cantidadText = input
                        }
                    },
                    label = { Text("Cantidad") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector entre Páginas y Hojas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = tipoSeleccionado == "paginas",
                            onClick = { tipoSeleccionado = "paginas" }
                        )
                        Text(text = "Páginas")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = tipoSeleccionado == "hojas",
                            onClick = { tipoSeleccionado = "hojas" }
                        )
                        Text(text = "Hojas")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Muestra del resultado
                Text(
                    text = "TOTAL",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Text(
                    text = String.format("$%.2f", total),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón Limpiar
                Button(
                    onClick = {
                        cantidadText = ""
                        tipoSeleccionado = "paginas"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Limpiar")
                }
            }
        }
    }
}
