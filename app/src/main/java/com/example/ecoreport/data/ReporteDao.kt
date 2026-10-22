package com.example.ecoreport.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReporteDao {

    @Insert
    suspend fun insertar(
        reporte: ReporteEntity
    )

    @Query(
        "SELECT * FROM reportes ORDER BY id DESC"
    )
    fun obtenerTodos():
            Flow<List<ReporteEntity>>

    @Query(
        """
            SELECT * 
            FROM reportes 
            WHERE (:categoria IS NULL OR categoria = :categoria)
            ORDER BY id DESC
        """
    )
    fun obtenerPorCategoria(
        categoria: String?
    ):
            Flow<List<ReporteEntity>>

    @Query(
        """
            SELECT *
            FROM reportes
            WHERE (:estado IS NULL OR estado = :estado)
            AND (:categoria IS NULL OR categoria LIKE '%' || :categoria || '%')
            ORDER BY id DESC
        """
    )
    fun obtenerFiltrados(
        estado: String?,
        categoria: String?
    ):
            Flow<List<ReporteEntity>>

    @Update
    suspend fun actualizar(
        reporte: ReporteEntity
    )

    @Delete
    suspend fun eliminar(
        reporte: ReporteEntity
    )
}