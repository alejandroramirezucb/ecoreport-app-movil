package com.example.ecoreport.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import com.example.ecoreport.ui_comp.screens.PantallaDetalle
import com.example.ecoreport.ui_comp.screens.PantallaEstadisticas
import com.example.ecoreport.ui_comp.screens.PantallaRegistro
import com.example.ecoreport.ui_comp.screens.PantallaReportes
import com.example.ecoreport.ui_comp.screens.PantallaReportesRemotos
import com.example.ecoreport.ui_comp.screens.PantallaInicio
import com.example.ecoreport.viewmodel.ReporteViewModel
import com.example.ecoreport.viewmodel.ReporteViewModelFactory

@Composable
fun AppNavigation(
    repository: ReporteRepository
) {

    val navController =
        rememberNavController()

    val factory =
        remember(repository) {

            ReporteViewModelFactory(
                repository
            )
        }

    val viewModel:
            ReporteViewModel =
        viewModel(
            factory = factory
        )

    val reportes by viewModel.reportes
        .collectAsStateWithLifecycle()

    var reporteSeleccionado by remember {
        mutableStateOf<ReporteEntity?>(null)
    }

    val categoriaSeleccionada by viewModel
        .categoriaSeleccionada
        .collectAsStateWithLifecycle()

    val reportesRemotos by viewModel.reportesRemotos
        .collectAsStateWithLifecycle()

    val cargando by viewModel.cargando
        .collectAsStateWithLifecycle()

    val error by viewModel.error
        .collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {

        composable("inicio") {

            PantallaInicio(
                reportes = reportes,

                irRegistro = {

                    navController.navigate(
                        "registro"
                    )
                },

                irReportes = {

                    navController.navigate(
                        "reportes"
                    )
                },

                irEstadisticas = {

                    navController.navigate(
                        "estadisticas"
                    )
                },

                irReportesRemotos = {

                    navController.navigate(
                        "reportesRemotos"
                    )
                }
            )
        }

        composable("registro") {

            PantallaRegistro(
                registrarReporte = {
                        reporte ->

                    viewModel.insertar(
                        reporte
                    )

                    navController
                        .popBackStack()
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable("reportes") {

            PantallaReportes(
                reportes = reportes,

                categoriaSeleccionada = categoriaSeleccionada,

                seleccionarCategoria = {
                    categoria ->
                    viewModel.seleccionarCategoria(categoria)
                },

                seleccionarReporte = {
                        reporte ->

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

        composable("reportesRemotos") {

            LaunchedEffect(Unit) {
                viewModel.cargarReportesRemotos()
            }

            PantallaReportesRemotos(
                reportes = reportesRemotos,

                cargando = cargando,

                error = error,

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable("detalle") {

            PantallaDetalle(
                reporte =
                    reporteSeleccionado,

                actualizarReporte = {
                        reporteActualizado ->

                    viewModel.actualizar(
                        reporteActualizado
                    )

                    reporteSeleccionado =
                        reporteActualizado
                },

                eliminarReporte = {
                        reporte ->

                    viewModel.eliminar(
                        reporte
                    )

                    reporteSeleccionado =
                        null

                    navController
                        .popBackStack()
                },

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }

        composable("estadisticas") {

            PantallaEstadisticas(
                reportes = reportes,

                volver = {

                    navController
                        .popBackStack()
                }
            )
        }
    }
}