package com.example.ecoreport.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecoreport.data.remote.ReporteDto

@Composable
fun PantallaReportesRemotos(

    reportes:
    List<ReporteDto>,

    cargando:
    Boolean,

    error:
    String?,

    crearReporte:
        (String, String, String, String, String, String) -> Unit,

    editarReporte:
        (Int, String, String, String, String, String, String) -> Unit,

    eliminarReporte:
        (Int) -> Unit,

    volver:
        () -> Unit

) {

    var reporteEditando by remember {
        mutableStateOf<ReporteDto?>(null)
    }

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var categoria by remember {
        mutableStateOf("")
    }

    var ubicacion by remember {
        mutableStateOf("")
    }

    var fecha by remember {
        mutableStateOf("")
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    20.dp
                )
    ) {

        Text(
            text =
                "Reportes remotos",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(
                    16.dp
                )
        )

        when {

            cargando -> {

                CircularProgressIndicator()
            }

            error != null -> {

                Text(
                    text =
                        error,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }

            else -> {

                LazyColumn(
                    modifier =
                        Modifier.weight(
                            1f
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    item {

                        Column {

                            OutlinedTextField(
                                value = titulo,
                                onValueChange = { titulo = it },
                                label = { Text("Título") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = categoria,
                                onValueChange = { categoria = it },
                                label = { Text("Categoría") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = ubicacion,
                                onValueChange = { ubicacion = it },
                                label = { Text("Ubicación") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = fecha,
                                onValueChange = { fecha = it },
                                label = { Text("Fecha") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {

                                    val editando =
                                        reporteEditando

                                    if (editando == null) {

                                        crearReporte(
                                            titulo,
                                            descripcion,
                                            categoria,
                                            ubicacion,
                                            "PENDIENTE",
                                            fecha
                                        )

                                    } else {

                                        editarReporte(
                                            editando.id,
                                            titulo,
                                            descripcion,
                                            categoria,
                                            ubicacion,
                                            editando.estado,
                                            fecha
                                        )
                                    }

                                    reporteEditando = null
                                    titulo = ""
                                    descripcion = ""
                                    categoria = ""
                                    ubicacion = ""
                                    fecha = ""
                                },

                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                            ) {

                                Text(
                                    text =
                                        if (reporteEditando == null)
                                            "CREAR REPORTE"
                                        else
                                            "GUARDAR CAMBIOS"
                                )
                            }
                        }
                    }

                    if (reportes.isEmpty()) {

                        item {

                            Text(
                                text =
                                    "No existen reportes remotos."
                            )
                        }
                    }

                    items(
                        items =
                            reportes,

                        key = {
                                reporte ->

                            reporte.id
                        }
                    ) {
                            reporte ->

                        Card(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),

                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),

                            elevation =
                                CardDefaults
                                    .cardElevation(
                                        defaultElevation =
                                            4.dp
                                    )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(
                                        16.dp
                                    )
                            ) {

                                Text(
                                    text =
                                        reporte.titulo,

                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Text(
                                    text =
                                        "Categoría: ${reporte.categoria}"
                                )

                                Text(
                                    text =
                                        "Ubicación: ${reporte.ubicacion}"
                                )

                                Text(
                                    text =
                                        "Estado: ${reporte.estado}"
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            10.dp
                                        )
                                )

                                Row(
                                    horizontalArrangement =
                                        Arrangement.spacedBy(
                                            8.dp
                                        )
                                ) {

                                    OutlinedButton(
                                        onClick = {
                                            reporteEditando = reporte
                                            titulo = reporte.titulo
                                            descripcion = reporte.descripcion
                                            categoria = reporte.categoria
                                            ubicacion = reporte.ubicacion
                                            fecha = reporte.fecha
                                        }
                                    ) {

                                        Text(
                                            text =
                                                "EDITAR"
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            eliminarReporte(
                                                reporte.id
                                            )
                                        }
                                    ) {

                                        Text(
                                            text =
                                                "ELIMINAR"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )

        OutlinedButton(
            onClick =
                volver,

            modifier =
                Modifier
                    .fillMaxWidth()
        ) {

            Text(
                text =
                    "VOLVER"
            )
        }
    }
}