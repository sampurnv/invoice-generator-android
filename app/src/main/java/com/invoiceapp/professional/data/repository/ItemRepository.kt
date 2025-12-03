package com.invoiceapp.professional.data.repository

import androidx.lifecycle.LiveData
import com.invoiceapp.professional.data.dao.ItemDao
import com.invoiceapp.professional.data.entity.Item

class ItemRepository(private val itemDao: ItemDao) {
    
    val allItems: LiveData<List<Item>> = itemDao.getAllItems()
    
    suspend fun insert(item: Item): Long {
        return itemDao.insert(item)
    }
    
    suspend fun update(item: Item) {
        itemDao.update(item)
    }
    
    suspend fun delete(item: Item) {
        itemDao.delete(item)
    }
    
    suspend fun getItemById(itemId: Long): Item? {
        return itemDao.getItemById(itemId)
    }
    
    fun searchItems(query: String): LiveData<List<Item>> {
        return itemDao.searchItems(query)
    }
}