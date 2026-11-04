package com.example.ecoreport.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.data.remote.CrearReporteDto
import com.example.ecoreport.data.remote.ReporteDto
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada
    val reportes:
    StateFlow<List<ReporteEntity>> =
        _categoriaSeleccionada
            .flatMapLatest {
                categoria ->
                repository.obtenerPorCategoria(categoria)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted
                    .WhileSubscribed(5000),
                initialValue = emptyList()
            )



    fun insertar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.insertar(reporte)
        }
    }

    fun eliminar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.eliminar(reporte)
        }
    }

    fun actualizar(reporte: ReporteEntity) {
        viewModelScope.launch {
            repository.actualizar(reporte)
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
    }

    //DATOS REMOTOS
    private val _reportesRemotos = MutableStateFlow<List<ReporteDto>>(emptyList())
    val reportesRemotos: StateFlow<List<ReporteDto>> = _reportesRemotos
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarReportesRemotos() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                val datos = repository.obtenerReportesRemotos()

                _reportesRemotos.value = datos
            } catch (
                e: Exception
            ) {
                _error.value = "No se pudieron cargar los reportes"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearNuevoReporteRemoto(
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            _cargando.value = true

            _error.value = null

            try {
                val reporte = CrearReporteDto(
                    titulo,
                    descripcion,
                    categoria,
                    ubicacion,
                    estado = "PENDIENTE",
                    fecha
                )

                repository.crearReporte(reporte)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Hubo un error al momento de crear el registro, por favor comuniquese con soporte"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun editarReporteRemoto(
        id: Int,
        titulo: String,
        descripcion: String,
        categoria: String,
        ubicacion: String,
        estado: String,
        fecha: String
    ) {
        viewModelScope.launch {
            _cargando.value = true

            _error.value = null

            try {
                val reporte = CrearReporteDto(
                    titulo,
                    descripcion,
                    categoria,
                    ubicacion,
                    estado,
                    fecha
                )

                repository.editarReporte(id, reporte)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Hubo un error al momento de editar el registro, por favor comuniquese con soporte"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun eliminarReporteRemoto(
        id: Int
    ) {
        viewModelScope.launch {
            _cargando.value = true

            _error.value = null

            try {
                repository.eliminarReporte(id)
                cargarReportesRemotos()
            } catch (e: Exception) {
                _error.value = "Hubo un error al momento de eliminar el registro, por favor comuniquese con soporte"
            } finally {
                _cargando.value = false
            }
        }
    }
}