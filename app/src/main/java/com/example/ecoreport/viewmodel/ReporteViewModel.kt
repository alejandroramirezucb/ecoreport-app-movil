package com.example.ecoreport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoreport.data.ReporteEntity
import com.example.ecoreport.repository.ReporteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReporteViewModel (
    private val repository: ReporteRepository
): ViewModel() {

    private val _estadoSeleccionado = MutableStateFlow<String?>(null)
    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    val estadoSeleccionado: StateFlow<String?> = _estadoSeleccionado
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada

    val todos:
    StateFlow<List<ReporteEntity>> =
        repository.obtenerTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted
                    .WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val reportes:
    StateFlow<List<ReporteEntity>> =
        combine(
            _estadoSeleccionado,
            _categoriaSeleccionada
        ) { estado, categoria ->
            estado to categoria
        }
            .flatMapLatest { (estado, categoria) ->
                repository.obtenerFiltrados(estado, categoria)
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

    fun aplicarFiltros(estado: String?, categoria: String?) {
        _estadoSeleccionado.value = estado
        _categoriaSeleccionada.value = categoria?.trim()?.ifEmpty { null }
    }
}
