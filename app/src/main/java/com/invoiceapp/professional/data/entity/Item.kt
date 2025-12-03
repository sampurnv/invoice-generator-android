package com.invoiceapp.professional.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val unit: String = "unit",
    val createdAt: Long = System.currentTimeMillis()
)