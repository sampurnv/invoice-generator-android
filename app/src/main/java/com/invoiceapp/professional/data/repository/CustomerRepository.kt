package com.invoiceapp.professional.data.repository

import androidx.lifecycle.LiveData
import com.invoiceapp.professional.data.dao.CustomerDao
import com.invoiceapp.professional.data.entity.Customer

class CustomerRepository(private val customerDao: CustomerDao) {
    
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers()
    
    suspend fun insert(customer: Customer): Long {
        return customerDao.insert(customer)
    }
    
    suspend fun update(customer: Customer) {
        customerDao.update(customer)
    }
    
    suspend fun delete(customer: Customer) {
        customerDao.delete(customer)
    }
    
    suspend fun getCustomerById(customerId: Long): Customer? {
        return customerDao.getCustomerById(customerId)
    }
    
    fun searchCustomers(query: String): LiveData<List<Customer>> {
        return customerDao.searchCustomers(query)
    }
}