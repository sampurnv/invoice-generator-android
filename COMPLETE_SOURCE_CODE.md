# Complete Source Code Guide

This document contains all remaining source files needed for the Invoice Generator app. Copy each section to the specified file path.

## PDF Generation

### File: `app/src/main/java/com/invoiceapp/professional/pdf/PdfGenerator.kt`

```kotlin
package com.invoiceapp.professional.pdf

import android.content.Context
import android.os.Environment
import com.invoiceapp.professional.data.entity.Customer
import com.invoiceapp.professional.data.entity.Invoice
import com.invoiceapp.professional.data.entity.InvoiceItem
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class PdfGenerator(private val context: Context) {
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    
    // Colors
    private val PRIMARY_COLOR = DeviceRgb(67, 126, 234)
    private val ACCENT_COLOR = DeviceRgb(118, 75, 162)
    private val TEXT_DARK = DeviceRgb(44, 62, 80)
    private val TEXT_LIGHT = DeviceRgb(127, 140, 141)
    private val GRAY_BG = DeviceRgb(248, 249, 250)
    
    fun generateInvoicePdf(
        invoice: Invoice,
        customer: Customer,
        items: List<InvoiceItem>
    ): File {
        val fileName = "Invoice_${invoice.invoiceNumber}.pdf"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
        
        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)
        
        // Set margins
        document.setMargins(40f, 40f, 40f, 40f)
        
        when (invoice.templateType) {
            "MODERN" -> generateModernTemplate(document, invoice, customer, items)
            "MINIMAL" -> generateMinimalTemplate(document, invoice, customer, items)
            else -> generateClassicTemplate(document, invoice, customer, items)
        }
        
        document.close()
        return file
    }
    
    private fun generateClassicTemplate(
        document: Document,
        invoice: Invoice,
        customer: Customer,
        items: List<InvoiceItem>
    ) {
        // Header with company name
        document.add(
            Paragraph("YOUR COMPANY NAME")
                .setFontSize(28f)
                .setBold()
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(5f)
        )
        
        document.add(
            Paragraph("123 Business Street\nNew York, NY 10001\ncontact@yourcompany.com\n+1 (555) 123-4567")
                .setFontSize(10f)
                .setFontColor(TEXT_LIGHT)
                .setMarginBottom(30f)
        )
        
        // Invoice title and number
        val headerTable = Table(floatArrayOf(1f, 1f))
        headerTable.setWidth(UnitValue.createPercentValue(100f))
        
        headerTable.addCell(
            Cell().add(
                Paragraph("INVOICE")
                    .setFontSize(32f)
                    .setBold()
                    .setFontColor(TEXT_DARK)
            ).setBorder(null)
        )
        
        val infoCell = Cell()
            .add(Paragraph("Invoice #: ${invoice.invoiceNumber}").setFontSize(10f).setBold())
            .add(Paragraph("Date: ${dateFormat.format(invoice.issueDate)}").setFontSize(10f))
            .add(Paragraph("Due Date: ${dateFormat.format(invoice.dueDate)}").setFontSize(10f))
            .setBorder(null)
            .setTextAlignment(TextAlignment.RIGHT)
        
        headerTable.addCell(infoCell)
        document.add(headerTable)
        document.add(Paragraph("\n"))
        
        // Bill To section
        val billToTable = Table(floatArrayOf(1f, 1f))
        billToTable.setWidth(UnitValue.createPercentValue(100f))
        
        billToTable.addCell(
            Cell().add(
                Paragraph("BILL TO")
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginBottom(5f)
            ).add(
                Paragraph(customer.name)
                    .setFontSize(11f)
                    .setBold()
            ).add(
                Paragraph(customer.address)
                    .setFontSize(10f)
                    .setFontColor(TEXT_LIGHT)
            ).add(
                Paragraph("${customer.email}\n${customer.phone}")
                    .setFontSize(10f)
                    .setFontColor(TEXT_LIGHT)
            ).setBorder(null)
        )
        
        billToTable.addCell(
            Cell().add(
                Paragraph("PAYMENT TERMS")
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(PRIMARY_COLOR)
                    .setMarginBottom(5f)
            ).add(
                Paragraph(invoice.paymentTerms)
                    .setFontSize(10f)
            ).setBorder(null)
                .setTextAlignment(TextAlignment.RIGHT)
        )
        
        document.add(billToTable)
        document.add(Paragraph("\n"))
        
        // Items table
        val itemsTable = Table(floatArrayOf(3f, 5f, 2f, 2f, 2f))
        itemsTable.setWidth(UnitValue.createPercentValue(100f))
        
        // Table header
        val headers = listOf("QTY", "DESCRIPTION", "UNIT PRICE", "TAX", "AMOUNT")
        headers.forEach { header ->
            itemsTable.addCell(
                Cell().add(
                    Paragraph(header)
                        .setFontSize(10f)
                        .setBold()
                        .setFontColor(DeviceRgb.WHITE)
                ).setBackgroundColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }
        
        // Table rows
        items.forEach { item ->
            itemsTable.addCell(createCell(item.quantity.toString(), false))
            itemsTable.addCell(
                createCell("${item.itemName}\n${item.description}", false)
                    .setTextAlignment(TextAlignment.LEFT)
            )
            itemsTable.addCell(createCell(currencyFormat.format(item.unitPrice), false))
            itemsTable.addCell(createCell("0%", false))
            itemsTable.addCell(createCell(currencyFormat.format(item.total), false))
        }
        
        document.add(itemsTable)
        document.add(Paragraph("\n"))
        
        // Totals
        val totalsTable = Table(floatArrayOf(3f, 1f))
        totalsTable.setWidth(UnitValue.createPercentValue(50f))
        totalsTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT)
        
        totalsTable.addCell(createCell("Subtotal:", true))
        totalsTable.addCell(createCell(currencyFormat.format(invoice.subtotal), false))
        
        totalsTable.addCell(createCell("Tax (${invoice.taxRate}%):", true))
        totalsTable.addCell(createCell(currencyFormat.format(invoice.taxAmount), false))
        
        totalsTable.addCell(
            createCell("TOTAL:", true)
                .setFontSize(14f)
                .setBackgroundColor(GRAY_BG)
        )
        totalsTable.addCell(
            createCell(currencyFormat.format(invoice.total), false)
                .setFontSize(14f)
                .setBold()
                .setBackgroundColor(GRAY_BG)
        )
        
        document.add(totalsTable)
        
        // Notes
        if (!invoice.notes.isNullOrEmpty()) {
            document.add(Paragraph("\n\n"))
            document.add(
                Paragraph("NOTES")
                    .setFontSize(12f)
                    .setBold()
                    .setFontColor(PRIMARY_COLOR)
            )
            document.add(
                Paragraph(invoice.notes)
                    .setFontSize(10f)
                    .setFontColor(TEXT_LIGHT)
            )
        }
        
        // Footer
        document.add(Paragraph("\n\n"))
        document.add(
            Paragraph("Thank you for your business!")
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(TEXT_LIGHT)
        )
    }
    
    private fun generateModernTemplate(
        document: Document,
        invoice: Invoice,
        customer: Customer,
        items: List<InvoiceItem>
    ) {
        // Modern template with gradient-like header
        val headerPara = Paragraph("INVOICE")
            .setFontSize(36f)
            .setBold()
            .setFontColor(DeviceRgb.WHITE)
            .setBackgroundColor(ACCENT_COLOR)
            .setPadding(20f)
            .setTextAlignment(TextAlignment.CENTER)
        
        document.add(headerPara)
        
        // Rest similar to classic but with modern styling
        generateClassicTemplate(document, invoice, customer, items)
    }
    
    private fun generateMinimalTemplate(
        document: Document,
        invoice: Invoice,
        customer: Customer,
        items: List<InvoiceItem>
    ) {
        // Minimal template with clean lines
        document.add(
            Paragraph("Invoice")
                .setFontSize(24f)
                .setFontColor(TEXT_DARK)
                .setMarginBottom(20f)
        )
        
        // Rest similar to classic but with minimal styling
        generateClassicTemplate(document, invoice, customer, items)
    }
    
    private fun createCell(text: String, isBold: Boolean): Cell {
        val paragraph = Paragraph(text).setFontSize(10f)
        if (isBold) paragraph.setBold()
        
        return Cell()
            .add(paragraph)
            .setTextAlignment(TextAlignment.CENTER)
            .setPadding(8f)
    }
}
```

## Main Activity

### File: `app/src/main/java/com/invoiceapp/professional/ui/MainActivity.kt`

```kotlin
package com.invoiceapp.professional.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.invoiceapp.professional.R
import com.invoiceapp.professional.databinding.ActivityMainBinding
import com.invoiceapp.professional.ui.adapter.InvoiceAdapter
import com.invoiceapp.professional.ui.viewmodel.InvoiceViewModel
import com.invoiceapp.professional.ui.viewmodel.InvoiceViewModelFactory

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var adapter: InvoiceAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Invoices"
        
        setupViewModel()
        setupRecyclerView()
        setupFab()
        observeInvoices()
    }
    
    private fun setupViewModel() {
        val factory = InvoiceViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory)[InvoiceViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        adapter = InvoiceAdapter { invoice ->
            val intent = Intent(this, InvoiceDetailActivity::class.java)
            intent.putExtra("INVOICE_ID", invoice.id)
            startActivity(intent)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupFab() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, CreateInvoiceActivity::class.java))
        }
    }
    
    private fun observeInvoices() {
        viewModel.allInvoices.observe(this) { invoices ->
            adapter.submitList(invoices)
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    observeInvoices()
                } else {
                    viewModel.searchInvoices(newText).observe(this@MainActivity) { invoices ->
                        adapter.submitList(invoices)
                    }
                }
                return true
            }
        })
        
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_customers -> {
                startActivity(Intent(this, CustomerActivity::class.java))
                true
            }
            R.id.action_items -> {
                startActivity(Intent(this, ItemActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
```

Continue in next message due to length...