# ⚡ Quick Start Guide - Invoice Generator

Get up and running in 5 minutes!

## 🚀 Super Quick Setup

### 1. Download & Open (2 minutes)

```bash
# Clone repository
git clone https://github.com/sampurnv/invoice-generator-android.git

# Open in Android Studio
# File → Open → Select 'invoice-generator-android' folder
```

### 2. Run the App (1 minute)

- Click Run button (▶️) in Android Studio
- Select device/emulator
- Wait for app to launch

### 3. Create Your First Invoice (2 minutes)

**The app includes sample data on first launch:**
- ✅ 1 sample customer (Acme Corporation)
- ✅ 5 sample items (Web Development, Mobile App, etc.)

**Create invoice:**
1. Tap "+" button (bottom right)
2. Select "Acme Corporation" from customer dropdown
3. Tap "Add Item"
4. Select "Web Development Service"
5. Enter quantity: "1"
6. Tap "Save Invoice"
7. Done! Invoice created with number INV-000001

**Generate PDF:**
1. Tap on the invoice you just created
2. Tap "Export PDF" button
3. PDF saved to Downloads folder
4. Open Downloads and view your invoice!

## 🎯 That's It!

You've successfully:
- ✅ Installed the app
- ✅ Created an invoice
- ✅ Generated a PDF
- ✅ Verified it works

## 📱 Next Steps

### Add Your Own Data

#### Add Customer
1. Menu → Customers
2. Tap "+"
3. Fill in details
4. Save

#### Add Item
1. Menu → Items
2. Tap "+"
3. Fill in details
4. Save

#### Create Invoice
1. Main screen → "+"
2. Select your customer
3. Add your items
4. Generate PDF

### Customize Templates

See `TEMPLATE_CUSTOMIZATION.md` for:
- Changing colors
- Adding logo
- Modifying layout
- Company information

### Build for Release

See `BUILD_INSTRUCTIONS.md` for:
- Creating signing key
- Building signed APK
- Building AAB for Play Store

## 🧪 Quick Test

Run through this workflow to verify everything works:

```
1. Open app ✓
2. See sample data ✓
3. Create invoice ✓
4. Add 2 items ✓
5. Save invoice ✓
6. Find in history ✓
7. Export PDF ✓
8. Open PDF ✓
9. Verify content ✓
```

## 📚 Documentation

- **README.md** - Overview and features
- **TEMPLATE_CUSTOMIZATION.md** - Customize invoices
- **BUILD_INSTRUCTIONS.md** - Build APK/AAB
- **DEPLOYMENT_GUIDE.md** - Publish to Play Store
- **TESTING_GUIDE.md** - Complete testing procedures

## 🆘 Quick Troubleshooting

### App Won't Build
```bash
./gradlew clean
./gradlew build
```

### Gradle Sync Failed
```
File → Invalidate Caches / Restart
```

### Can't Find PDF
```
Check: Device → Files → Downloads folder
```

### Sample Data Not Showing
```
Uninstall app completely
Reinstall
Sample data loads on first launch
```

## 💡 Pro Tips

1. **Use Search** - Find invoices instantly by number or customer
2. **Templates** - Try all three templates to find your favorite
3. **Backup** - Export database regularly (future feature)
4. **Customize** - Edit templates to match your brand

## 🎉 You're Ready!

The app is fully functional with:
- ✅ Sample customer and items pre-loaded
- ✅ All features working
- ✅ PDF generation ready
- ✅ Ready to customize
- ✅ Ready to build for release

---

**Start creating professional invoices now! 📄**