package com.example.ecoreport.repository

import com.example.ecoreport.data.ReporteDao
import com.example.ecoreport.data.ReporteEntity

class ReporteRepository (
    private val reporteDao: ReporteDao
) {
    suspend fun obtenerTodos():
            List<ReporteEntity> {
        return reporteDao.obtenerTodos()
    }

    suspend fun obtenerPorCategoria(categoria: String):
            List<ReporteEntity> {
        return reporteDao.obtenerPorCategoria(categoria)
    }

    suspend fun obtenerPorEstado(estado: String):
            List<ReporteEntity> {
        return reporteDao.obtenerPorEstado(estado)
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
