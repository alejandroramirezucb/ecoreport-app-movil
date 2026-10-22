package com.example.ecoreport.repository

import com.example.ecoreport.data.ReporteDao
import com.example.ecoreport.data.ReporteEntity
import kotlinx.coroutines.flow.Flow

class ReporteRepository (
    private val reporteDao: ReporteDao
) {
    fun obtenerTodos():
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerTodos()
    }
    fun obtenerPorCategoria(
        categoria: String?
    ):
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerPorCategoria(categoria)
    }

    fun obtenerFiltrados(
        estado: String?,
        categoria: String?
    ):
            Flow<List<ReporteEntity>> {
        return reporteDao.obtenerFiltrados(estado, categoria)
    }

    suspend fun insertar(reporte: ReporteEntity){
        reporteDao.insertar(reporte)
    }

    suspend fun actualizar(reporte: ReporteEntity) {
        reporteDao.actualizar(reporte)
    }

    suspend fun eliminar(reporte: ReporteEntity) {
        reporteDao.eliminar(reporte)
    }
}
