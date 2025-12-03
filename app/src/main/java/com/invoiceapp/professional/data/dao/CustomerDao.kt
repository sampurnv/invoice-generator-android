package com.invoiceapp.professional.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.invoiceapp.professional.data.entity.Customer

@Dao
interface CustomerDao {
    
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): LiveData<List<Customer>>
    
    @Query("SELECT * FROM customers WHERE id = :customerId")
    suspend fun getCustomerById(customerId: Long): Customer?
    
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    fun searchCustomers(query: String): LiveData<List<Customer>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: Customer): Long
    
    @Update
    suspend fun update(customer: Customer)
    
    @Delete
    suspend fun delete(customer: Customer)
    
    @Query("DELETE FROM customers WHERE id = :customerId")
    suspend fun deleteById(customerId: Long)
}