package com.invoiceapp.professional.data.dao

import androidx.room.*
import com.invoiceapp.professional.data.entity.InvoiceItem

@Dao
interface InvoiceItemDao {
    
    @Query("SELECT * FROM invoice_items WHERE invoiceId = :invoiceId")
    suspend fun getItemsByInvoiceId(invoiceId: Long): List<InvoiceItem>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoiceItem: InvoiceItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoiceItems: List<InvoiceItem>)
    
    @Update
    suspend fun update(invoiceItem: InvoiceItem)
    
    @Delete
    suspend fun delete(invoiceItem: InvoiceItem)
    
    @Query("DELETE FROM invoice_items WHERE invoiceId = :invoiceId")
    suspend fun deleteByInvoiceId(invoiceId: Long)
}