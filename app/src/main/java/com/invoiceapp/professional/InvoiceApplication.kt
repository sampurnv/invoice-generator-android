package com.invoiceapp.professional

import android.app.Application
import com.invoiceapp.professional.data.AppDatabase
import com.invoiceapp.professional.data.repository.CustomerRepository
import com.invoiceapp.professional.data.repository.InvoiceRepository
import com.invoiceapp.professional.data.repository.ItemRepository

class InvoiceApplication : Application() {
    
    val database by lazy { AppDatabase.getDatabase(this) }
    val customerRepository by lazy { CustomerRepository(database.customerDao()) }
    val itemRepository by lazy { ItemRepository(database.itemDao()) }
    val invoiceRepository by lazy { InvoiceRepository(database.invoiceDao(), database.invoiceItemDao()) }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: InvoiceApplication
            private set
    }
}