package com.example.ecoreport.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.*
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui.screens.*
import com.example.ecoreport.ui_comp.screens.PantallaInicio
import com.example.ecoreport.viewmodel.ReporteViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    repository: ReporteRepository
) {

    val navController =
        rememberNavController()

    val viewModel: ReporteViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                ReporteViewModel(repository)
            }
        }
    )
    val reportes =
        remember {
            mutableStateListOf<
                    ReporteEntity
                    >()
        }

    var reporteSeleccionado by remember {
        mutableStateOf<
                ReporteEntity?
                >(null)
    }

    val scope =
        rememberCoroutineScope()

    suspend fun cargarReportes() {

        val datos =
            repository.obtenerTodos()

        reportes.clear()

        reportes.addAll(
            datos
        )
    }

    LaunchedEffect(Unit) {

        cargarReportes()

    }

    NavHost(
        navController =
            navController,

        startDestination =
            "inicio"
    ) {

        composable(
            "inicio"
        ) {

            PantallaInicio (
                reportes =
                    viewModel.reportes,

                irRegistro = {
                    navController
                        .navigate(
                            "registro"
                        )
                },

                irReportes = {
                    navController
                        .navigate(
                            "reportes"
                        )
                },

                irEstadisticas = {
                    navController
                        .navigate(
                            "estadisticas"
                        )
                }
            )
        }
        composable(
            "registro"
        ) {

            PantallaRegistro(
                registrarReporte = { reporte ->

                    viewModel.insertar(reporte)

                    navController
                        .popBackStack()
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "reportes"
        ) {

            PantallaReportes(
                reportes =
                    viewModel.reportesFiltrados,

                categorias =
                    viewModel.reportes
                        .map { it.categoria }
                        .distinct(),

                estadoSeleccionado =
                    viewModel.estadoSeleccionado,

                categoriaSeleccionada =
                    viewModel.categoriaSeleccionada,

                filtrarPorEstado =
                    viewModel::filtrarPorEstado,

                filtrarPorCategoria =
                    viewModel::filtrarPorCategoria,

                seleccionarReporte = { reporte ->

                    reporteSeleccionado =
                        reporte

                    navController.navigate(
                        "detalle"
                    )
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "detalle"
        ) {

            PantallaDetalle(
                reporte =
                    reporteSeleccionado,

                actualizarReporte = {
                    reporteActualizado ->
                    viewModel.actualizar(reporteActualizado)
                    reporteSeleccionado = reporteActualizado
                },

                eliminarReporte = { reporte ->

                    viewModel.eliminar(reporte)

                    navController
                        .popBackStack()
                },

                volver = {
                    navController
                        .popBackStack()
                }
            )
        }
        composable(
            "estadisticas"
        ) {

            PantallaEstadisticas(
                reportes =
                    reportes,

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
    }
}