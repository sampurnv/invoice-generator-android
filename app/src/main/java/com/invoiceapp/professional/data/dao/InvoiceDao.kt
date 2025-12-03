package com.invoiceapp.professional.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.invoiceapp.professional.data.entity.Invoice
import com.invoiceapp.professional.data.model.InvoiceWithDetails

@Dao
interface InvoiceDao {
    
    @Query("SELECT * FROM invoices ORDER BY createdAt DESC")
    fun getAllInvoices(): LiveData<List<Invoice>>
    
    @Query("SELECT * FROM invoices WHERE id = :invoiceId")
    suspend fun getInvoiceById(invoiceId: Long): Invoice?
    
    @Transaction
    @Query("SELECT * FROM invoices WHERE id = :invoiceId")
    suspend fun getInvoiceWithDetails(invoiceId: Long): InvoiceWithDetails?
    
    @Query("SELECT * FROM invoices WHERE invoiceNumber LIKE '%' || :query || '%' OR status LIKE '%' || :query || '%'")
    fun searchInvoices(query: String): LiveData<List<Invoice>>
    
    @Query("SELECT * FROM invoices WHERE customerId = :customerId ORDER BY createdAt DESC")
    fun getInvoicesByCustomer(customerId: Long): LiveData<List<Invoice>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: Invoice): Long
    
    @Update
    suspend fun update(invoice: Invoice)
    
    @Delete
    suspend fun delete(invoice: Invoice)
    
    @Query("DELETE FROM invoices WHERE id = :invoiceId")
    suspend fun deleteById(invoiceId: Long)
    
    @Query("SELECT MAX(CAST(SUBSTR(invoiceNumber, 5) AS INTEGER)) FROM invoices WHERE invoiceNumber LIKE 'INV-%'")
    suspend fun getLastInvoiceNumber(): Int?
}