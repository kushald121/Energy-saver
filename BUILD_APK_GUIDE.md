# 📱 Building APK for Energy-saver App

## Current Issue
Java JDK is not installed or not in PATH. This is required to build the APK.

---

## 🎯 Solution Options

### Option 1: Install Java JDK (Recommended)

#### Step 1: Download and Install Java
1. Download **Java JDK 11 or higher** from:
   - Oracle JDK: https://www.oracle.com/java/technologies/downloads/
   - OR OpenJDK: https://adoptium.net/

2. Install the JDK
   - Run the installer
   - Use default installation path (e.g., `C:\Program Files\Java\jdk-11.0.x`)

#### Step 2: Set Environment Variables

**Option A: Through System Settings**
1. Open Start Menu → Search "Environment Variables"
2. Click "Edit the system environment variables"
3. Click "Environment Variables" button
4. Under "System variables", click "New"
5. Variable name: `JAVA_HOME`
6. Variable value: `C:\Program Files\Java\jdk-11.0.x` (your JDK path)
7. Find "Path" variable, click "Edit"
8. Add new entry: `%JAVA_HOME%\bin`
9. Click OK on all dialogs
10. **Restart PowerShell**

**Option B: Quick PowerShell Command**
```powershell
# Set for current session only (temporary)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11.0.x"
$env:Path += ";$env:JAVA_HOME\bin"
```

#### Step 3: Verify Java Installation
```powershell
java -version
```

Should show something like:
```
java version "11.0.x"
Java(TM) SE Runtime Environment (build 11.0.x)
```

#### Step 4: Build APK
```powershell
# Navigate to project
cd c:\Users\admin\Desktop\KushalBhaykaAutomaticbulb\Energy-saver

# Clean previous builds
.\gradlew.bat clean

# Build debug APK
.\gradlew.bat assembleDebug

# Build release APK (unsigned)
.\gradlew.bat assembleRelease
```

#### Step 5: Find Your APK
**Debug APK** (ready to install):
```
app\build\outputs\apk\debug\app-debug.apk
```

**Release APK** (needs signing):
```
app\build\outputs\apk\release\app-release-unsigned.apk
```

---

### Option 2: Build Using Android Studio (Easiest)

#### Step 1: Install Android Studio
1. Download from: https://developer.android.com/studio
2. Install with default settings
3. Let it download SDK components

#### Step 2: Open Project
1. Launch Android Studio
2. File → Open
3. Select: `c:\Users\admin\Desktop\KushalBhaykaAutomaticbulb\Energy-saver`
4. Wait for Gradle sync to complete

#### Step 3: Build APK
1. **For Debug APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for build to complete
   - Click "locate" in notification to find APK

2. **For Release APK:**
   - Build → Generate Signed Bundle / APK
   - Select APK
   - Create or select keystore
   - Follow wizard

#### Step 4: Find APK
Android Studio will show notification with "locate" link.

Or manually:
```
app\build\outputs\apk\debug\app-debug.apk
```

---

### Option 3: Use Portable Java (No Installation)

#### Step 1: Download Portable JDK
1. Go to: https://adoptium.net/temurin/releases/
2. Download ZIP version (not installer)
3. Extract to: `C:\Java\jdk-11`

#### Step 2: Set Java for Current Session
```powershell
# In PowerShell
$env:JAVA_HOME = "C:\Java\jdk-11"
$env:Path = "C:\Java\jdk-11\bin;$env:Path"

# Verify
java -version
```

#### Step 3: Build APK
```powershell
cd c:\Users\admin\Desktop\KushalBhaykaAutomaticbulb\Energy-saver
.\gradlew.bat assembleDebug
```

---

## 🚀 Quick Build Script

Once Java is installed, save this as `build-apk.ps1`:

```powershell
# Build APK Script for Energy-saver

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Building Energy-saver APK" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check Java
Write-Host "Checking Java..." -ForegroundColor Yellow
try {
    $javaVersion = & java -version 2>&1
    Write-Host "✓ Java found: $($javaVersion[0])" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found!" -ForegroundColor Red
    Write-Host "Please install Java JDK and try again." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "Cleaning previous builds..." -ForegroundColor Yellow
& .\gradlew.bat clean

Write-Host ""
Write-Host "Building Debug APK..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "✓ APK Built Successfully!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host ""
    
    $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
    
    if (Test-Path $apkPath) {
        Write-Host "APK Location:" -ForegroundColor Cyan
        Write-Host "  $apkPath" -ForegroundColor White
        Write-Host ""
        
        $apkSize = (Get-Item $apkPath).Length / 1MB
        Write-Host "APK Size: $([math]::Round($apkSize, 2)) MB" -ForegroundColor Cyan
        Write-Host ""
        
        Write-Host "Opening APK folder..." -ForegroundColor Yellow
        Invoke-Item (Split-Path $apkPath)
        
        Write-Host ""
        Write-Host "To install on device:" -ForegroundColor Yellow
        Write-Host "  adb install $apkPath" -ForegroundColor White
    } else {
        Write-Host "⚠ APK file not found at expected location" -ForegroundColor Yellow
    }
} else {
    Write-Host ""
    Write-Host "✗ Build Failed!" -ForegroundColor Red
    Write-Host "Check the error messages above." -ForegroundColor Yellow
}
```

---

## 📦 APK Types Explained

### Debug APK
- **Purpose**: Testing and development
- **Signing**: Auto-signed with debug key
- **Size**: Larger (includes debug info)
- **Ready to install**: Yes
- **Location**: `app\build\outputs\apk\debug\app-debug.apk`
- **Command**: `.\gradlew.bat assembleDebug`

### Release APK
- **Purpose**: Production/distribution
- **Signing**: Requires your keystore
- **Size**: Optimized and smaller
- **Ready to install**: After signing
- **Location**: `app\build\outputs\apk\release\app-release-unsigned.apk`
- **Command**: `.\gradlew.bat assembleRelease`

---

## 📱 Installing the APK

### Method 1: USB Install
```powershell
# Enable USB debugging on device first
# Then connect device and run:
adb install app\build\outputs\apk\debug\app-debug.apk
```

### Method 2: Direct Transfer
1. Copy APK to phone (via USB, email, cloud)
2. On phone, open file manager
3. Tap the APK file
4. Enable "Install from unknown sources" if prompted
5. Tap Install

### Method 3: Using Android Studio
1. Connect device
2. Run → Run 'app'
3. Select your device
4. App installs and launches automatically

---

## 🔐 Signing Release APK (For Production)

### Step 1: Create Keystore (One-time)
```powershell
# Generate keystore
keytool -genkey -v -keystore energy-saver.keystore -alias energy-saver -keyalg RSA -keysize 2048 -validity 10000

# Enter details when prompted
# SAVE THE PASSWORD - You'll need it!
```

### Step 2: Sign APK
```powershell
# Build unsigned release APK
.\gradlew.bat assembleRelease

# Sign the APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore energy-saver.keystore app\build\outputs\apk\release\app-release-unsigned.apk energy-saver

# Align APK (optional but recommended)
zipalign -v 4 app\build\outputs\apk\release\app-release-unsigned.apk app\build\outputs\apk\release\app-release.apk
```

---

## 🛠️ Troubleshooting

### Error: JAVA_HOME not set
**Solution**: Install Java and set JAVA_HOME (see Option 1 above)

### Error: SDK not found
**Solution**: Install Android Studio or set ANDROID_HOME
```powershell
$env:ANDROID_HOME = "C:\Users\admin\AppData\Local\Android\Sdk"
```

### Error: Build failed
**Solution**: Check error message, might need to:
- Update Gradle: `.\gradlew.bat wrapper --gradle-version 8.0`
- Sync dependencies: Open in Android Studio and sync

### Error: Out of memory
**Solution**: Increase Gradle memory in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2048m
```

---

## ⚡ Quick Commands Reference

```powershell
# Clean build
.\gradlew.bat clean

# Build debug APK
.\gradlew.bat assembleDebug

# Build release APK
.\gradlew.bat assembleRelease

# Install debug APK
adb install app\build\outputs\apk\debug\app-debug.apk

# Uninstall app
adb uninstall com.energysaver.app

# View connected devices
adb devices

# Check Java version
java -version

# Check Gradle version
.\gradlew.bat -v
```

---

## 📊 Expected Build Output

```
BUILD SUCCESSFUL in 1m 23s
45 actionable tasks: 45 executed

APK Location:
  app\build\outputs\apk\debug\app-debug.apk

APK Size: ~15-25 MB
```

---

## ✅ Final Checklist

- [ ] Java JDK 11+ installed
- [ ] JAVA_HOME environment variable set
- [ ] Java in PATH
- [ ] PowerShell restarted
- [ ] Navigated to project directory
- [ ] Run `.\gradlew.bat assembleDebug`
- [ ] APK created successfully
- [ ] APK found in `app\build\outputs\apk\debug\`
- [ ] APK ready to install

---

## 🎯 Next Steps

1. **Install Java** (if not done)
2. **Run**: `.\gradlew.bat assembleDebug`
3. **Find APK**: `app\build\outputs\apk\debug\app-debug.apk`
4. **Install**: Transfer to phone and install
5. **Test**: Run the app and verify functionality

---

*Build Guide v1.0*
*For Energy-saver Android App*
*Created: October 6, 2025*
