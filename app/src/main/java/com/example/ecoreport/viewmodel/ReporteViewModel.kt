package com.example.ecoreport.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.launch

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {
    val reportes = mutableStateListOf<ReporteEntity>()

    val reportesFiltrados = mutableStateListOf<ReporteEntity>()

    var estadoSeleccionado by mutableStateOf<String?>(null)
        private set

    var categoriaSeleccionada by mutableStateOf<String?>(null)
        private set

    init {
        cargarReportes()
    }

    fun cargarReportes() {
        viewModelScope.launch {
            val datos = repository.obtenerTodos()

            reportes.clear()

            reportes.addAll(datos)

            actualizarFiltrados()
        }
    }

    fun filtrarPorEstado(estado: String?) {
        estadoSeleccionado = estado
        categoriaSeleccionada = null

        viewModelScope.launch {
            actualizarFiltrados()
        }
    }

    fun filtrarPorCategoria(categoria: String?) {
        categoriaSeleccionada = categoria
        estadoSeleccionado = null

        viewModelScope.launch {
            actualizarFiltrados()
        }
    }

    private suspend fun actualizarFiltrados() {
        val estado = estadoSeleccionado
        val categoria = categoriaSeleccionada

        val datos = when {
            estado != null -> repository.obtenerPorEstado(estado)
            categoria != null -> repository.obtenerPorCategoria(categoria)
            else -> repository.obtenerTodos()
        }

        reportesFiltrados.clear()

        reportesFiltrados.addAll(
            datos.sortedByDescending { it.id }
        )
    }

    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
            cargarReportes()
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)

            cargarReportes()
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)

            cargarReportes()
        }
    }
}
