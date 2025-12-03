# 🔨 Build Instructions - Complete Guide

Step-by-step instructions to build signed APK and AAB files ready for Google Play Store.

## 🎯 What You'll Build

1. **Debug APK** - For testing on devices
2. **Signed Release APK** - For direct distribution
3. **Signed AAB (Android App Bundle)** - For Google Play Store upload

## 📋 Prerequisites

### Required Software
- ✅ Android Studio Hedgehog (2023.1.1) or later
- ✅ JDK 17
- ✅ Android SDK 34
- ✅ Git

### Verify Installation

```bash
# Check Java version
java -version
# Should show: openjdk version "17.x.x"

# Check Android SDK
echo $ANDROID_HOME
# Should show SDK path

# Check Gradle
./gradlew --version
# Should show Gradle 8.x
```

## 🚀 Step-by-Step Build Process

### Step 1: Clone Repository

```bash
git clone https://github.com/sampurnv/invoice-generator-android.git
cd invoice-generator-android
```

### Step 2: Open in Android Studio

1. Launch Android Studio
2. File → Open
3. Navigate to `invoice-generator-android` folder
4. Click "OK"
5. Wait for Gradle sync (may take 2-5 minutes)

### Step 3: Build Debug APK (For Testing)

```bash
# Command line
./gradlew assembleDebug

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
```

**Or in Android Studio:**
- Build → Build Bundle(s) / APK(s) → Build APK(s)
- Wait for build to complete
- Click "locate" in notification

**Install on device:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Step 4: Generate Signing Key (One-Time Setup)

```bash
# Navigate to project root
cd invoice-generator-android

# Generate keystore
keytool -genkey -v \
  -keystore invoice-app-release.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias invoice-app-key
```

**You'll be prompted for:**

```
Enter keystore password: [CREATE STRONG PASSWORD]
Re-enter new password: [REPEAT PASSWORD]
What is your first and last name? Your Company Name
What is the name of your organizational unit? Development
What is the name of your organization? Your Company Inc
What is the name of your City or Locality? New York
What is the name of your State or Province? NY
What is the two-letter country code for this unit? US
Is CN=..., OU=..., O=..., L=..., ST=..., C=... correct? yes

Enter key password for <invoice-app-key>: [SAME OR DIFFERENT PASSWORD]
```

**CRITICAL:** Save these passwords securely! You'll need them for every release.

### Step 5: Configure Signing in Project

Create `keystore.properties` in project root:

```properties
storePassword=YOUR_STORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=invoice-app-key
storeFile=invoice-app-release.jks
```

**Add to .gitignore:**
```bash
echo "keystore.properties" >> .gitignore
echo "*.jks" >> .gitignore
```

Update `app/build.gradle`:

```gradle
// Add at the top, before android block
def keystorePropertiesFile = rootProject.file("keystore.properties")
def keystoreProperties = new Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
}

android {
    ...
    
    signingConfigs {
        release {
            if (keystorePropertiesFile.exists()) {
                storeFile file(keystoreProperties['storeFile'])
                storePassword keystoreProperties['storePassword']
                keyAlias keystoreProperties['keyAlias']
                keyPassword keystoreProperties['keyPassword']
            }
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Step 6: Build Signed Release APK

```bash
# Clean previous builds
./gradlew clean

# Build signed APK
./gradlew assembleRelease

# Output location:
# app/build/outputs/apk/release/app-release.apk
```

**Verify signature:**
```bash
jarsigner -verify -verbose -certs app/build/outputs/apk/release/app-release.apk
```

Should show: "jar verified."

### Step 7: Build Android App Bundle (AAB) for Play Store

```bash
# Build AAB
./gradlew bundleRelease

# Output location:
# app/build/outputs/bundle/release/app-release.aab
```

**File size should be:** ~5-10 MB

### Step 8: Test Release Build

```bash
# Install release APK on device
adb install app/build/outputs/apk/release/app-release.apk

# Or drag and drop APK to emulator
```

**Test thoroughly:**
- Create customer
- Create item
- Create invoice
- Generate PDF
- Verify PDF content
- Search invoices
- All features working

## 📦 Build Outputs Summary

After successful build, you'll have:

| File | Location | Purpose | Size |
|------|----------|---------|------|
| app-debug.apk | app/build/outputs/apk/debug/ | Testing | ~8 MB |
| app-release.apk | app/build/outputs/apk/release/ | Direct distribution | ~6 MB |
| app-release.aab | app/build/outputs/bundle/release/ | Play Store upload | ~5 MB |

## 🔍 Verify Build Quality

### Check APK Contents

```bash
# Extract APK
unzip app-release.apk -d apk-contents

# Verify files present
ls apk-contents/
# Should see: AndroidManifest.xml, classes.dex, resources.arsc, res/, lib/
```

### Analyze APK Size

In Android Studio:
- Build → Analyze APK
- Select app-release.apk
- Review size breakdown
- Identify large files

### Test ProGuard

```bash
# Verify code obfuscation
unzip -p app-release.apk classes.dex | strings | grep "com.invoiceapp"
# Should show obfuscated class names
```

## 🐛 Troubleshooting

### Build Fails with "Keystore not found"

**Solution:**
```bash
# Verify keystore file exists
ls invoice-app-release.jks

# Check path in keystore.properties
cat keystore.properties

# Ensure path is correct (relative or absolute)
```

### Build Fails with "Wrong password"

**Solution:**
- Double-check passwords in `keystore.properties`
- Ensure no extra spaces
- Try regenerating keystore if password forgotten

### Gradle Sync Failed

**Solution:**
```bash
# Update Gradle wrapper
./gradlew wrapper --gradle-version 8.1

# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Sync again in Android Studio
File → Sync Project with Gradle Files
```

### Out of Memory Error

**Solution:**

Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
```

### APK Too Large

**Solution:**

Enable additional optimizations in `app/build.gradle`:

```gradle
buildTypes {
    release {
        minifyEnabled true
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

## 📱 Installing on Device

### Via USB

```bash
# Enable USB debugging on device
# Connect device via USB
adb devices
# Should show your device

# Install APK
adb install -r app/build/outputs/apk/release/app-release.apk
```

### Via File Transfer

1. Copy APK to device
2. Open file manager on device
3. Tap APK file
4. Allow "Install from unknown sources" if prompted
5. Tap "Install"

## 🎯 Build Variants

### Debug Build
- Faster build time
- Includes debugging symbols
- Larger file size
- Not optimized

```bash
./gradlew assembleDebug
```

### Release Build
- Slower build time
- Code obfuscated
- Optimized and minified
- Smaller file size
- Production-ready

```bash
./gradlew assembleRelease
```

## 📊 Build Configuration

### Current Configuration

```gradle
android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.invoiceapp.professional"
        minSdk 24        // Android 7.0+
        targetSdk 34     // Android 14
        versionCode 1
        versionName "1.0.0"
    }
}
```

### For Updates

Increment version for each release:

```gradle
defaultConfig {
    versionCode 2        // Increment by 1
    versionName "1.0.1"  // Update version string
}
```

## 🔐 Security Best Practices

1. **Never commit keystore files**
   ```bash
   # Add to .gitignore
   *.jks
   keystore.properties
   ```

2. **Store passwords securely**
   - Use password manager
   - Don't share in plain text
   - Don't commit to version control

3. **Backup keystore**
   - Store in multiple secure locations
   - Cloud backup (encrypted)
   - Physical backup

4. **Use different keys for different apps**
   - Don't reuse keystores
   - Generate new key per app

## ✅ Pre-Release Checklist

Before building final release:

- [ ] All features tested
- [ ] No crashes
- [ ] Version code incremented
- [ ] Version name updated
- [ ] ProGuard rules configured
- [ ] Keystore backed up
- [ ] Passwords documented securely
- [ ] App icon finalized
- [ ] Package name correct
- [ ] Permissions minimal
- [ ] Privacy policy ready

## 📤 Upload to Play Store

### Using Play Console

1. Go to [Google Play Console](https://play.google.com/console)
2. Select your app
3. Production → Create new release
4. Upload `app-release.aab`
5. Fill release notes
6. Review and rollout

### Using Command Line (Advanced)

```bash
# Install bundletool
brew install bundletool  # Mac
# or download from GitHub

# Generate APKs from AAB
bundletool build-apks \
  --bundle=app/build/outputs/bundle/release/app-release.aab \
  --output=app-release.apks \
  --ks=invoice-app-release.jks \
  --ks-key-alias=invoice-app-key

# Install on connected device
bundletool install-apks --apks=app-release.apks
```

## 🎉 Success!

You now have:
- ✅ Signed APK for direct distribution
- ✅ Signed AAB for Play Store
- ✅ Tested and verified builds
- ✅ Ready for production deployment

---

**Build complete! Ready to publish! 🚀**