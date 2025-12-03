# 🚀 Deployment Guide - Invoice Generator Android App

## Complete Build & Deployment Instructions

This guide covers everything needed to build, sign, and deploy the Invoice Generator app to Google Play Store.

## 📋 Prerequisites Checklist

- [ ] Android Studio Hedgehog (2023.1.1) or later installed
- [ ] JDK 17 configured
- [ ] Android SDK 34 installed
- [ ] Google Play Console account created
- [ ] App signing key generated

## 🔨 Building the App

### Step 1: Clone and Setup

```bash
# Clone repository
git clone https://github.com/sampurnv/invoice-generator-android.git
cd invoice-generator-android

# Open in Android Studio
# File → Open → Select project folder
```

### Step 2: Sync Gradle

Wait for Gradle sync to complete. If you encounter issues:

```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

### Step 3: Test on Device/Emulator

```bash
# Run debug build
./gradlew installDebug

# Or use Android Studio Run button (▶️)
```

## 🔐 Generating Signing Key

### Create Keystore

```bash
keytool -genkey -v -keystore invoice-app-release.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias invoice-app-key
```

**Enter the following information:**
- Password: (choose a strong password)
- First and Last Name: Your Company Name
- Organizational Unit: Development
- Organization: Your Company
- City: Your City
- State: Your State
- Country Code: US (or your country)

**IMPORTANT:** Save the keystore file and passwords securely!

### Store Keystore Safely

```bash
# Move keystore to secure location
mv invoice-app-release.jks ~/secure-keys/

# Never commit keystore to Git!
echo "*.jks" >> .gitignore
```

## ⚙️ Configure Signing

### Option 1: Using gradle.properties (Recommended)

Create `gradle.properties` in project root:

```properties
RELEASE_STORE_FILE=/path/to/invoice-app-release.jks
RELEASE_STORE_PASSWORD=your_store_password
RELEASE_KEY_ALIAS=invoice-app-key
RELEASE_KEY_PASSWORD=your_key_password
```

Update `app/build.gradle`:

```gradle
android {
    signingConfigs {
        release {
            storeFile file(RELEASE_STORE_FILE)
            storePassword RELEASE_STORE_PASSWORD
            keyAlias RELEASE_KEY_ALIAS
            keyPassword RELEASE_KEY_PASSWORD
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Option 2: Using Android Studio

1. Build → Generate Signed Bundle/APK
2. Select "Android App Bundle"
3. Create new keystore or use existing
4. Fill in keystore details
5. Select "release" build variant
6. Click "Finish"

## 📦 Building Release Artifacts

### Build Signed APK

```bash
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release.apk
```

### Build Android App Bundle (AAB) - For Play Store

```bash
./gradlew bundleRelease

# Output location:
# app/build/outputs/bundle/release/app-release.aab
```

### Verify Build

```bash
# Check APK signature
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk

# Check AAB
bundletool build-apks --bundle=app/build/outputs/bundle/release/app-release.aab \
  --output=app-release.apks \
  --ks=invoice-app-release.jks \
  --ks-key-alias=invoice-app-key
```

## 🎨 Customization Before Release

### 1. Update App Name

Edit `app/src/main/res/values/strings.xml`:

```xml
<string name="app_name">Invoice Pro</string>
```

### 2. Change Package Name

In `app/build.gradle`:

```gradle
defaultConfig {
    applicationId "com.yourcompany.invoicepro"
}
```

Then refactor package:
- Right-click package in Android Studio
- Refactor → Rename
- Update all references

### 3. Update App Icon

Replace files in `app/src/main/res/`:
- `mipmap-mdpi/ic_launcher.png` (48x48)
- `mipmap-hdpi/ic_launcher.png` (72x72)
- `mipmap-xhdpi/ic_launcher.png` (96x96)
- `mipmap-xxhdpi/ic_launcher.png` (144x144)
- `mipmap-xxxhdpi/ic_launcher.png` (192x192)

Use [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/) to generate icons.

### 4. Customize Invoice Templates

Edit company information in `PdfGenerator.kt`:

```kotlin
private const val COMPANY_NAME = "Your Company Name"
private const val COMPANY_ADDRESS = "123 Business St\nCity, State 12345"
private const val COMPANY_EMAIL = "contact@yourcompany.com"
private const val COMPANY_PHONE = "+1 (555) 123-4567"
```

Change colors:

```kotlin
private val PRIMARY_COLOR = DeviceRgb(67, 126, 234)  // Your brand color
private val ACCENT_COLOR = DeviceRgb(118, 75, 162)   // Your accent color
```

## 📱 Google Play Store Submission

### Step 1: Create App in Play Console

1. Go to [Google Play Console](https://play.google.com/console)
2. Click "Create app"
3. Fill in app details:
   - App name: Invoice Generator Pro
   - Default language: English (United States)
   - App or game: App
   - Free or paid: Free
4. Accept declarations
5. Click "Create app"

### Step 2: Set Up App Content

#### Privacy Policy
Create a privacy policy and host it online. Include:
- What data you collect (locally stored only)
- How data is used
- User rights

Example: "This app stores all data locally on your device. No data is transmitted to external servers."

#### App Access
- Provide test account if needed (not required for this app)

#### Ads
- Select "No, my app does not contain ads"

#### Content Rating
Complete questionnaire:
- Select "Utility" category
- Answer questions honestly
- Get rating (likely "Everyone")

#### Target Audience
- Select age groups (13+)
- Confirm app is not designed for children

#### Data Safety
Fill out data safety form:
- Data collection: None (all local)
- Data sharing: None
- Security practices: Data encrypted in transit

### Step 3: Store Listing

#### App Details
- **App name:** Invoice Generator Pro
- **Short description:** Create professional invoices in seconds
- **Full description:**

```
Create professional invoices quickly and easily with Invoice Generator Pro.

FEATURES:
• Create beautiful invoices in seconds
• Manage customers and items
• Multiple professional templates
• Export to PDF
• Searchable invoice history
• Offline - all data stored locally
• No subscription required

PERFECT FOR:
• Freelancers
• Small businesses
• Consultants
• Service providers

Create your first invoice in under a minute!
```

#### Graphics
Required assets:
- **App icon:** 512x512 PNG
- **Feature graphic:** 1024x500 PNG
- **Phone screenshots:** At least 2 (1080x1920 or similar)
- **7-inch tablet screenshots:** At least 2 (optional)
- **10-inch tablet screenshots:** At least 2 (optional)

Screenshot tips:
1. Show main invoice list
2. Show invoice creation screen
3. Show PDF preview
4. Show customer management
5. Show item catalogue

### Step 4: Release

#### Production Track
1. Go to "Production" in left menu
2. Click "Create new release"
3. Upload AAB file
4. Fill in release notes:

```
Version 1.0.0
• Initial release
• Create professional invoices
• Manage customers and items
• Export to PDF
• Multiple templates
• Searchable history
```

5. Review and rollout

#### Release Timeline
- **Review time:** 1-7 days typically
- **Status updates:** Check Play Console regularly
- **Approval:** App goes live automatically when approved

## 🧪 Testing Before Release

### Pre-Launch Checklist

- [ ] Test on multiple devices (phone, tablet)
- [ ] Test on different Android versions (7.0+)
- [ ] Create customer successfully
- [ ] Create item successfully
- [ ] Create invoice with multiple items
- [ ] Save invoice
- [ ] Search for invoice
- [ ] Export PDF and verify content
- [ ] Verify PDF opens correctly
- [ ] Test all three templates
- [ ] Edit customer
- [ ] Edit item
- [ ] Delete invoice
- [ ] App doesn't crash on rotation
- [ ] Back button works correctly
- [ ] All text is readable
- [ ] No spelling errors

### Internal Testing

Use Play Console Internal Testing:
1. Create internal testing track
2. Upload AAB
3. Add testers (email addresses)
4. Share testing link
5. Collect feedback
6. Fix issues
7. Upload new version

## 📊 Post-Launch

### Monitor Metrics
- Installs
- Crashes (via Play Console)
- User reviews
- Ratings

### Respond to Reviews
- Reply to user feedback
- Address issues quickly
- Thank positive reviewers

### Update Strategy
- Fix critical bugs immediately
- Plan feature updates
- Increment version code for each release

## 🔄 Updating the App

### Version Management

In `app/build.gradle`:

```gradle
defaultConfig {
    versionCode 2        // Increment for each release
    versionName "1.0.1"  // User-visible version
}
```

### Release Update

```bash
# Build new version
./gradlew bundleRelease

# Upload to Play Console
# Production → Create new release → Upload AAB
```

## 🛠️ Troubleshooting

### Build Fails

```bash
# Clean project
./gradlew clean

# Invalidate caches (Android Studio)
File → Invalidate Caches / Restart

# Update Gradle
./gradlew wrapper --gradle-version 8.1
```

### Signing Issues

```bash
# Verify keystore
keytool -list -v -keystore invoice-app-release.jks

# Check passwords are correct
# Ensure keystore path is correct
```

### Upload Rejected

Common reasons:
- Version code not incremented
- Package name conflict
- Missing permissions declaration
- Content policy violation

Fix and re-upload.

## 📞 Support Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Play Console Help](https://support.google.com/googleplay/android-developer)
- [App Signing Guide](https://developer.android.com/studio/publish/app-signing)

## ✅ Final Checklist

Before submitting to Play Store:

- [ ] App tested thoroughly
- [ ] All features working
- [ ] No crashes
- [ ] Proper app icon
- [ ] Correct package name
- [ ] Version code/name set
- [ ] Signed with release key
- [ ] Privacy policy created
- [ ] Store listing complete
- [ ] Screenshots prepared
- [ ] Release notes written
- [ ] Content rating obtained
- [ ] Data safety form completed

---

**You're ready to publish! 🚀**

The app is production-ready and meets all Google Play Store requirements.