package com.invoiceapp.professional.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.invoiceapp.professional.data.dao.CustomerDao
import com.invoiceapp.professional.data.dao.InvoiceDao
import com.invoiceapp.professional.data.dao.InvoiceItemDao
import com.invoiceapp.professional.data.dao.ItemDao
import com.invoiceapp.professional.data.entity.Customer
import com.invoiceapp.professional.data.entity.Invoice
import com.invoiceapp.professional.data.entity.InvoiceItem
import com.invoiceapp.professional.data.entity.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Customer::class, Item::class, Invoice::class, InvoiceItem::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun customerDao(): CustomerDao
    abstract fun itemDao(): ItemDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun invoiceItemDao(): InvoiceItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "invoice_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }
        
        suspend fun populateDatabase(database: AppDatabase) {
            val customerDao = database.customerDao()
            val itemDao = database.itemDao()
            
            // Sample Customer
            val customer = Customer(
                name = "Acme Corporation",
                email = "contact@acmecorp.com",
                phone = "+1 (555) 123-4567",
                address = "123 Business Street\nNew York, NY 10001\nUnited States",
                taxId = "TAX-123456789"
            )
            customerDao.insert(customer)
            
            // Sample Items
            val items = listOf(
                Item(
                    name = "Web Development Service",
                    description = "Custom website development and design",
                    price = 2500.00,
                    unit = "project"
                ),
                Item(
                    name = "Mobile App Development",
                    description = "iOS and Android app development",
                    price = 5000.00,
                    unit = "project"
                ),
                Item(
                    name = "Consulting Hours",
                    description = "Technical consulting and advisory services",
                    price = 150.00,
                    unit = "hour"
                ),
                Item(
                    name = "UI/UX Design",
                    description = "User interface and experience design",
                    price = 1200.00,
                    unit = "project"
                ),
                Item(
                    name = "SEO Optimization",
                    description = "Search engine optimization services",
                    price = 800.00,
                    unit = "month"
                )
            )
            items.forEach { itemDao.insert(it) }
        }
    }
}