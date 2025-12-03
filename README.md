# 📱 Professional Invoice Generator - Android App

A complete, production-ready Android application for creating professional invoices. Built with Kotlin, Room Database, and iText PDF generation.

## ✨ Features

- 📝 **Create Professional Invoices** - Quick invoice creation with customer and item selection
- 👥 **Customer Management** - Add, edit, and search customers
- 📦 **Item Catalogue** - Maintain your product/service catalogue
- 🔍 **Searchable History** - Find any invoice instantly
- 📄 **PDF Export** - Generate professional PDF invoices
- 🎨 **Multiple Templates** - Classic, Modern, and Minimal designs
- 💾 **Local Storage** - All data stored securely with Room/SQLite
- 📱 **Material Design** - Clean, touch-friendly interface

## 🚀 Quick Start

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Minimum Android 7.0 (API 24)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sampurnv/invoice-generator-android.git
   cd invoice-generator-android
   ```

2. **Open in Android Studio**
   - File → Open → Select project folder
   - Wait for Gradle sync to complete

3. **Run the app**
   - Connect Android device or start emulator
   - Click Run (▶️) or press Shift+F10

## 📦 Building APK/AAB

### Debug Build
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release Build (Signed)
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

### Android App Bundle (for Play Store)
```bash
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab
```

## 🎨 Customizing Invoice Templates

Invoice templates are defined in code for easy customization. You can modify colors, fonts, and layouts directly.

### Template Files Location
```
app/src/main/java/com/invoiceapp/professional/pdf/
├── PdfGenerator.kt          # Main PDF generation logic
├── ClassicTemplate.kt        # Classic template (default)
├── ModernTemplate.kt         # Modern template
└── MinimalTemplate.kt        # Minimal template
```

### Editing Templates

#### 1. Change Colors

Edit the template file (e.g., `ClassicTemplate.kt`):

```kotlin
// Primary color
private val PRIMARY_COLOR = DeviceRgb(67, 126, 234)  // Blue

// Accent color
private val ACCENT_COLOR = DeviceRgb(118, 75, 162)   // Purple

// Text colors
private val TEXT_DARK = DeviceRgb(44, 62, 80)
private val TEXT_LIGHT = DeviceRgb(127, 140, 141)
```

#### 2. Change Logo

Replace logo in template:

```kotlin
// In generatePDF() method
val logoPath = "path/to/your/logo.png"
val logo = ImageDataFactory.create(logoPath)
```

Or use placeholder text:

```kotlin
// Company name as logo
document.add(
    Paragraph("YOUR COMPANY NAME")
        .setFontSize(24f)
        .setBold()
        .setFontColor(PRIMARY_COLOR)
)
```

#### 3. Modify Layout

Adjust spacing, fonts, and structure:

```kotlin
// Change font sizes
private const val TITLE_SIZE = 28f
private const val HEADING_SIZE = 14f
private const val BODY_SIZE = 10f

// Adjust margins
document.setMargins(40f, 40f, 40f, 40f)

// Modify table structure
val table = Table(floatArrayOf(3f, 5f, 2f, 2f, 2f))
```

#### 4. Add Custom Fields

Add new fields to invoice:

```kotlin
// Add custom field
table.addCell(createCell("PO Number:", true))
table.addCell(createCell(invoice.poNumber ?: "N/A", false))
```

### After Editing Templates

1. **Rebuild the app**
   ```bash
   ./gradlew clean assembleRelease
   ```

2. **Test the changes**
   - Create a test invoice
   - Generate PDF
   - Verify the output

## 📱 App Structure

```
app/src/main/java/com/invoiceapp/professional/
├── data/
│   ├── entity/              # Room entities
│   ├── dao/                 # Database access objects
│   ├── repository/          # Data repositories
│   └── AppDatabase.kt       # Database configuration
├── ui/
│   ├── MainActivity.kt      # Invoice list
│   ├── CreateInvoiceActivity.kt
│   ├── CustomerActivity.kt
│   ├── ItemActivity.kt
│   └── InvoiceDetailActivity.kt
├── pdf/
│   └── PdfGenerator.kt      # PDF generation
└── InvoiceApplication.kt    # Application class
```

## 🗄️ Database Schema

### Tables
- **customers** - Customer information
- **items** - Product/service catalogue
- **invoices** - Invoice headers
- **invoice_items** - Invoice line items

### Sample Data
The app includes sample data on first launch:
- 1 customer (Acme Corporation)
- 5 items (Web Development, Mobile App, etc.)

## 🎯 Usage Workflow

1. **Create Customer** (optional if using existing)
   - Tap "Customers" → "+" button
   - Enter customer details
   - Save

2. **Add Items to Catalogue** (optional if using existing)
   - Tap "Items" → "+" button
   - Enter item details
   - Save

3. **Create Invoice**
   - Tap "+" button on main screen
   - Select customer
   - Add items and quantities
   - Set payment terms
   - Choose template
   - Save invoice

4. **Generate PDF**
   - Open invoice from history
   - Tap "Export PDF"
   - PDF saved to Downloads folder

5. **Search History**
   - Use search bar on main screen
   - Search by invoice number or status

## 🔧 Configuration

### App Settings

Edit `app/build.gradle`:

```gradle
defaultConfig {
    applicationId "com.invoiceapp.professional"  // Change package name
    versionCode 1                                 // Increment for updates
    versionName "1.0.0"                          // Version string
}
```

### Company Information

Edit template files to set default company info:

```kotlin
private const val COMPANY_NAME = "Your Company Name"
private const val COMPANY_ADDRESS = "123 Business St\nCity, State 12345"
private const val COMPANY_EMAIL = "contact@yourcompany.com"
private const val COMPANY_PHONE = "+1 (555) 123-4567"
```

## 📋 Testing Checklist

- [ ] Create a customer
- [ ] Create an item
- [ ] Create an invoice
- [ ] Add multiple items to invoice
- [ ] Save invoice
- [ ] View invoice in history
- [ ] Search for invoice
- [ ] Export PDF
- [ ] Open PDF and verify content
- [ ] Edit customer
- [ ] Edit item
- [ ] Delete invoice

## 🚀 Play Store Deployment

### 1. Generate Signing Key

```bash
keytool -genkey -v -keystore invoice-app-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias invoice-app
```

### 2. Configure Signing

Add to `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("invoice-app-key.jks")
            storePassword "your-password"
            keyAlias "invoice-app"
            keyPassword "your-password"
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
        }
    }
}
```

### 3. Build Release AAB

```bash
./gradlew bundleRelease
```

### 4. Upload to Play Console

1. Go to [Google Play Console](https://play.google.com/console)
2. Create new app
3. Upload AAB file
4. Fill in store listing
5. Submit for review

## 📄 License

MIT License - Free for personal and commercial use

## 🤝 Support

For issues or questions:
- GitHub Issues: [Create Issue](https://github.com/sampurnv/invoice-generator-android/issues)
- Email: support@invoiceapp.com

## 🎉 Credits

Built with:
- Kotlin
- Android Jetpack (Room, LiveData, ViewModel)
- Material Design Components
- iText 7 for PDF generation

---

**Ready for Google Play Store! 🚀**