package com.invoiceapp.professional.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.invoiceapp.professional.data.entity.Customer
import com.invoiceapp.professional.data.entity.Invoice
import com.invoiceapp.professional.data.entity.InvoiceItem

data class InvoiceWithDetails(
    @Embedded val invoice: Invoice,
    @Relation(
        parentColumn = "customerId",
        entityColumn = "id"
    )
    val customer: Customer,
    @Relation(
        parentColumn = "id",
        entityColumn = "invoiceId"
    )
    val items: List<InvoiceItem>
)