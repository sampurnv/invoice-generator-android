package com.invoiceapp.professional.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "invoices",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("customerId")]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val invoiceNumber: String,
    val customerId: Long,
    val issueDate: Date,
    val dueDate: Date,
    val subtotal: Double,
    val taxRate: Double = 0.0,
    val taxAmount: Double = 0.0,
    val total: Double,
    val notes: String? = null,
    val paymentTerms: String = "Net 30",
    val status: String = "UNPAID", // UNPAID, PAID, OVERDUE
    val templateType: String = "CLASSIC", // CLASSIC, MODERN, MINIMAL
    val createdAt: Date = Date(),
    val pdfPath: String? = null
)