package com.invoiceapp.professional.data.repository

import androidx.lifecycle.LiveData
import com.invoiceapp.professional.data.dao.InvoiceDao
import com.invoiceapp.professional.data.dao.InvoiceItemDao
import com.invoiceapp.professional.data.entity.Invoice
import com.invoiceapp.professional.data.entity.InvoiceItem
import com.invoiceapp.professional.data.model.InvoiceWithDetails

class InvoiceRepository(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao
) {
    
    val allInvoices: LiveData<List<Invoice>> = invoiceDao.getAllInvoices()
    
    suspend fun insert(invoice: Invoice, items: List<InvoiceItem>): Long {
        val invoiceId = invoiceDao.insert(invoice)
        val itemsWithInvoiceId = items.map { it.copy(invoiceId = invoiceId) }
        invoiceItemDao.insertAll(itemsWithInvoiceId)
        return invoiceId
    }
    
    suspend fun update(invoice: Invoice) {
        invoiceDao.update(invoice)
    }
    
    suspend fun delete(invoice: Invoice) {
        invoiceDao.delete(invoice)
    }
    
    suspend fun getInvoiceById(invoiceId: Long): Invoice? {
        return invoiceDao.getInvoiceById(invoiceId)
    }
    
    suspend fun getInvoiceWithDetails(invoiceId: Long): InvoiceWithDetails? {
        return invoiceDao.getInvoiceWithDetails(invoiceId)
    }
    
    fun searchInvoices(query: String): LiveData<List<Invoice>> {
        return invoiceDao.searchInvoices(query)
    }
    
    fun getInvoicesByCustomer(customerId: Long): LiveData<List<Invoice>> {
        return invoiceDao.getInvoicesByCustomer(customerId)
    }
    
    suspend fun generateInvoiceNumber(): String {
        val lastNumber = invoiceDao.getLastInvoiceNumber() ?: 0
        return "INV-${String.format("%06d", lastNumber + 1)}"
    }
}