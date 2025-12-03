package com.invoiceapp.professional.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val taxId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)