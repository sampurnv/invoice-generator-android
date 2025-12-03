package com.invoiceapp.professional.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.invoiceapp.professional.data.entity.Item

@Dao
interface ItemDao {
    
    @Query("SELECT * FROM items ORDER BY name ASC")
    fun getAllItems(): LiveData<List<Item>>
    
    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): Item?
    
    @Query("SELECT * FROM items WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchItems(query: String): LiveData<List<Item>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long
    
    @Update
    suspend fun update(item: Item)
    
    @Delete
    suspend fun delete(item: Item)
    
    @Query("DELETE FROM items WHERE id = :itemId")
    suspend fun deleteById(itemId: Long)
}