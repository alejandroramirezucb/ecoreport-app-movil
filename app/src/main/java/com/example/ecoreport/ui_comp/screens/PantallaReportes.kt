package com.example.ecoreport.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.ReporteEntity

private val estadosFiltro = listOf(
    "PENDIENTE" to "Pendiente",
    "EN_PROCESO" to "En proceso",
    "RESUELTO" to "Resuelto"
)

@Composable
fun PantallaReportes(
    reportes: List<ReporteEntity>,
    categorias: List<String>,
    estadoSeleccionado: String?,
    categoriaSeleccionada: String?,
    filtrarPorEstado: (String?) -> Unit,
    filtrarPorCategoria: (String?) -> Unit,
    seleccionarReporte: (ReporteEntity) -> Unit,
    volver: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Reportes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Filtrar por estado",
            style = MaterialTheme.typography.labelLarge
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = estadoSeleccionado == null,
                    onClick = { filtrarPorEstado(null) },
                    label = { Text("Todos") }
                )
            }

            items(estadosFiltro) { (valor, etiqueta) ->
                FilterChip(
                    selected = estadoSeleccionado == valor,
                    onClick = { filtrarPorEstado(valor) },
                    label = { Text(etiqueta) }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Filtrar por categoría",
            style = MaterialTheme.typography.labelLarge
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = categoriaSeleccionada == null,
                    onClick = { filtrarPorCategoria(null) },
                    label = { Text("Todas") }
                )
            }

            items(categorias) { categoria ->
                FilterChip(
                    selected = categoriaSeleccionada == categoria,
                    onClick = { filtrarPorCategoria(categoria) },
                    label = { Text(categoria) }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (reportes.isEmpty()) {

            Text(
                text = if (estadoSeleccionado != null || categoriaSeleccionada != null) {
                    "No hay reportes con ese filtro."
                } else {
                    "No existen reportes registrados."
                }
            )

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    items = reportes,
                    key = { reporte ->
                        reporte.id
                    }
                ) { reporte ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                seleccionarReporte(reporte)
                            },

                        shape = RoundedCornerShape(16.dp),

                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Text(
                                text = reporte.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Categoría: ${reporte.categoria}"
                            )

                            Text(
                                text = "Ubicación: ${reporte.ubicacion}"
                            )

                            Text(
                                text = "Estado: ${reporte.estado}"
                            )

                            Text(
                                text = "Fecha: ${reporte.fecha}"
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Presiona para ver el detalle",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedButton(
            onClick = volver,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "VOLVER"
            )
        }
    }
}