# 🎨 Invoice Template Customization Guide

Complete guide to customizing invoice templates for your brand.

## 📍 Template Files Location

```
app/src/main/java/com/invoiceapp/professional/pdf/PdfGenerator.kt
```

All three templates (Classic, Modern, Minimal) are in this single file for easy editing.

## 🎨 Quick Customization

### 1. Change Brand Colors

Find the color definitions at the top of `PdfGenerator.kt`:

```kotlin
// EDIT THESE COLORS
private val PRIMARY_COLOR = DeviceRgb(67, 126, 234)    // Main brand color
private val ACCENT_COLOR = DeviceRgb(118, 75, 162)     // Secondary color
private val TEXT_DARK = DeviceRgb(44, 62, 80)          // Dark text
private val TEXT_LIGHT = DeviceRgb(127, 140, 141)      // Light text
private val GRAY_BG = DeviceRgb(248, 249, 250)         // Background
```

**RGB Color Format:** `DeviceRgb(Red, Green, Blue)` where each value is 0-255

**Example Color Changes:**

```kotlin
// Professional Blue
private val PRIMARY_COLOR = DeviceRgb(0, 102, 204)

// Corporate Green
private val PRIMARY_COLOR = DeviceRgb(34, 139, 34)

// Modern Purple
private val PRIMARY_COLOR = DeviceRgb(156, 39, 176)

// Elegant Black
private val PRIMARY_COLOR = DeviceRgb(33, 33, 33)
```

### 2. Change Company Information

Find and edit these lines in `generateClassicTemplate()`:

```kotlin
// EDIT COMPANY NAME
document.add(
    Paragraph("YOUR COMPANY NAME")  // ← Change this
        .setFontSize(28f)
        .setBold()
        .setFontColor(PRIMARY_COLOR)
)

// EDIT COMPANY DETAILS
document.add(
    Paragraph(
        "123 Business Street\n" +           // ← Change address
        "New York, NY 10001\n" +            // ← Change city/state
        "contact@yourcompany.com\n" +      // ← Change email
        "+1 (555) 123-4567"                // ← Change phone
    )
        .setFontSize(10f)
        .setFontColor(TEXT_LIGHT)
)
```

### 3. Add Your Logo

#### Option A: Use Image Logo

```kotlin
// Add at the top of generateClassicTemplate()
try {
    // Place logo.png in app/src/main/res/drawable/
    val logoStream = context.resources.openRawResource(R.drawable.logo)
    val logo = ImageDataFactory.create(logoStream.readBytes())
    val logoImage = Image(logo)
        .setWidth(150f)
        .setHeight(50f)
    
    document.add(logoImage)
} catch (e: Exception) {
    // Fallback to text if logo not found
    document.add(Paragraph("YOUR COMPANY NAME"))
}
```

#### Option B: Text-Based Logo (Current)

Already implemented - just change the text:

```kotlin
document.add(
    Paragraph("ACME CORP")  // Your company name
        .setFontSize(28f)
        .setBold()
)
```

### 4. Modify Font Sizes

```kotlin
// Find these constants and adjust
private const val TITLE_SIZE = 32f        // "INVOICE" title
private const val COMPANY_SIZE = 28f      // Company name
private const val HEADING_SIZE = 12f      // Section headings
private const val BODY_SIZE = 10f         // Regular text
private const val SMALL_SIZE = 9f         // Small text
```

### 5. Adjust Spacing and Margins

```kotlin
// Document margins (top, right, bottom, left)
document.setMargins(40f, 40f, 40f, 40f)

// Section spacing
document.add(Paragraph("\n"))        // Small gap
document.add(Paragraph("\n\n"))      // Medium gap
document.add(Paragraph("\n\n\n"))    // Large gap

// Element margins
.setMarginBottom(20f)
.setMarginTop(10f)
```

## 🎯 Template-Specific Customization

### Classic Template

**Characteristics:**
- Traditional layout
- Blue header
- Clean tables
- Professional look

**Customize:**

```kotlin
private fun generateClassicTemplate(...) {
    // Change header style
    document.add(
        Paragraph("INVOICE")
            .setFontSize(32f)           // ← Adjust size
            .setBold()                  // ← Remove for regular
            .setFontColor(PRIMARY_COLOR) // ← Change color
    )
    
    // Modify table header background
    .setBackgroundColor(PRIMARY_COLOR)  // ← Change to your color
}
```

### Modern Template

**Characteristics:**
- Bold colored header
- Contemporary design
- Gradient-like appearance

**Customize:**

```kotlin
private fun generateModernTemplate(...) {
    // Change header background
    val headerPara = Paragraph("INVOICE")
        .setBackgroundColor(ACCENT_COLOR)  // ← Change color
        .setPadding(20f)                   // ← Adjust padding
        .setFontSize(36f)                  // ← Adjust size
}
```

### Minimal Template

**Characteristics:**
- Clean and simple
- Minimal colors
- Maximum readability

**Customize:**

```kotlin
private fun generateMinimalTemplate(...) {
    // Adjust minimalist header
    document.add(
        Paragraph("Invoice")
            .setFontSize(24f)              // ← Smaller, cleaner
            .setFontColor(TEXT_DARK)       // ← Dark, not colored
    )
}
```

## 🔧 Advanced Customization

### Add Custom Fields

#### Add Tax ID Field

```kotlin
// In bill-to section, add:
if (!customer.taxId.isNullOrEmpty()) {
    billToTable.addCell(
        Cell().add(
            Paragraph("Tax ID: ${customer.taxId}")
                .setFontSize(10f)
                .setFontColor(TEXT_LIGHT)
        ).setBorder(null)
    )
}
```

#### Add Purchase Order Number

```kotlin
// In header info section, add:
infoCell.add(
    Paragraph("PO #: ${invoice.poNumber ?: "N/A"}")
        .setFontSize(10f)
)
```

### Modify Table Structure

```kotlin
// Change column widths
val itemsTable = Table(floatArrayOf(
    2f,   // Quantity (narrower)
    6f,   // Description (wider)
    2f,   // Unit Price
    2f,   // Tax
    2f    // Amount
))

// Add new column
val itemsTable = Table(floatArrayOf(3f, 5f, 2f, 2f, 1f, 2f))
// Add header
itemsTable.addCell(createCell("DISCOUNT", true))
// Add data
itemsTable.addCell(createCell("10%", false))
```

### Add Watermark

```kotlin
// Add watermark for unpaid invoices
if (invoice.status == "UNPAID") {
    val watermark = Paragraph("UNPAID")
        .setFontSize(80f)
        .setFontColor(DeviceRgb(255, 0, 0))
        .setOpacity(0.2f)
        .setRotationAngle(Math.toRadians(45.0))
    
    // Position watermark
    document.add(watermark)
}
```

### Add Footer

```kotlin
// Add footer with page numbers
document.add(
    Paragraph("Page 1 of 1")
        .setFontSize(9f)
        .setTextAlignment(TextAlignment.CENTER)
        .setFontColor(TEXT_LIGHT)
        .setFixedPosition(
            40f,                    // x position
            20f,                    // y position from bottom
            pdfDoc.defaultPageSize.width - 80f  // width
        )
)
```

### Add Bank Details

```kotlin
// Add payment information section
document.add(Paragraph("\n"))
document.add(
    Paragraph("PAYMENT INFORMATION")
        .setFontSize(12f)
        .setBold()
        .setFontColor(PRIMARY_COLOR)
)

val bankTable = Table(floatArrayOf(1f, 2f))
bankTable.addCell(createCell("Bank Name:", true))
bankTable.addCell(createCell("Your Bank", false))
bankTable.addCell(createCell("Account Number:", true))
bankTable.addCell(createCell("1234567890", false))
bankTable.addCell(createCell("SWIFT Code:", true))
bankTable.addCell(createCell("ABCD1234", false))

document.add(bankTable)
```

## 🖼️ Adding Images

### Company Logo

```kotlin
// Method 1: From drawable resources
val logoDrawable = ContextCompat.getDrawable(context, R.drawable.company_logo)
val bitmap = (logoDrawable as BitmapDrawable).bitmap
val stream = ByteArrayOutputStream()
bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
val logo = ImageDataFactory.create(stream.toByteArray())

document.add(
    Image(logo)
        .setWidth(120f)
        .setHeight(40f)
)

// Method 2: From file
val logoFile = File(context.filesDir, "logo.png")
if (logoFile.exists()) {
    val logo = ImageDataFactory.create(logoFile.absolutePath)
    document.add(Image(logo).setWidth(120f))
}
```

### Signature Image

```kotlin
// Add signature at bottom
val signatureFile = File(context.filesDir, "signature.png")
if (signatureFile.exists()) {
    document.add(Paragraph("\n\n"))
    document.add(
        Paragraph("Authorized Signature")
            .setFontSize(10f)
            .setTextAlignment(TextAlignment.RIGHT)
    )
    val signature = ImageDataFactory.create(signatureFile.absolutePath)
    document.add(
        Image(signature)
            .setWidth(100f)
            .setHorizontalAlignment(HorizontalAlignment.RIGHT)
    )
}
```

## 🎨 Color Schemes

### Professional Blue (Default)
```kotlin
private val PRIMARY_COLOR = DeviceRgb(67, 126, 234)
private val ACCENT_COLOR = DeviceRgb(118, 75, 162)
```

### Corporate Green
```kotlin
private val PRIMARY_COLOR = DeviceRgb(34, 139, 34)
private val ACCENT_COLOR = DeviceRgb(46, 204, 113)
```

### Modern Orange
```kotlin
private val PRIMARY_COLOR = DeviceRgb(255, 127, 0)
private val ACCENT_COLOR = DeviceRgb(255, 165, 0)
```

### Elegant Black
```kotlin
private val PRIMARY_COLOR = DeviceRgb(33, 33, 33)
private val ACCENT_COLOR = DeviceRgb(66, 66, 66)
```

### Tech Purple
```kotlin
private val PRIMARY_COLOR = DeviceRgb(156, 39, 176)
private val ACCENT_COLOR = DeviceRgb(186, 104, 200)
```

## 🔄 After Making Changes

### 1. Rebuild the App

```bash
# Clean build
./gradlew clean

# Build release
./gradlew assembleRelease
```

### 2. Test Changes

- Create a new invoice
- Generate PDF
- Open and verify the PDF
- Check all customizations appear correctly

### 3. Deploy Update

```bash
# Build AAB for Play Store
./gradlew bundleRelease

# Upload to Play Console
```

## 💡 Tips

1. **Test on Real Device** - PDFs may look different on device vs emulator
2. **Keep Backups** - Save original template before major changes
3. **Use Version Control** - Commit changes to Git
4. **Test All Templates** - Ensure changes work across all three templates
5. **Check PDF Readers** - Test PDFs in multiple PDF readers

## 🐛 Common Issues

### Colors Not Showing
- Verify RGB values are 0-255
- Check DeviceRgb syntax is correct

### Logo Not Appearing
- Verify image file exists
- Check file path is correct
- Ensure image format is supported (PNG, JPG)

### Text Overlapping
- Adjust margins and padding
- Reduce font sizes
- Add spacing between sections

### PDF Too Large
- Compress images before adding
- Reduce image dimensions
- Optimize logo file size

## 📚 iText Documentation

For advanced PDF customization:
- [iText 7 Documentation](https://itextpdf.com/en/resources/api-documentation)
- [iText Examples](https://github.com/itext/itext7-samples)

## ✅ Customization Checklist

- [ ] Company name updated
- [ ] Company address updated
- [ ] Contact information updated
- [ ] Brand colors applied
- [ ] Logo added (if desired)
- [ ] Font sizes adjusted
- [ ] Spacing optimized
- [ ] Custom fields added (if needed)
- [ ] All templates tested
- [ ] PDF output verified
- [ ] App rebuilt
- [ ] Changes committed to Git

---

**Your invoices, your brand! 🎨**