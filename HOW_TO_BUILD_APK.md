# 🚀 QUICK GUIDE: Convert to APK

## ⚠️ Current Issue
**Java JDK is not installed.** This is required to build APK files.

---

## ✅ EASIEST SOLUTION: Use Android Studio

### Step 1: Install Android Studio
1. Download: https://developer.android.com/studio
2. Install with default settings (includes Java)
3. Wait for initial setup to complete

### Step 2: Open Your Project
1. Launch Android Studio
2. Click "Open"
3. Select folder: `C:\Users\admin\Desktop\KushalBhaykaAutomaticbulb\Energy-saver`
4. Wait for Gradle sync (may take 5-10 minutes first time)

### Step 3: Build APK
1. Click **Build** menu
2. Select **Build Bundle(s) / APK(s)**
3. Click **Build APK(s)**
4. Wait for build to complete (1-5 minutes)
5. Click **locate** in the notification popup

### Step 4: Get Your APK
APK will be at: `app\build\outputs\apk\debug\app-debug.apk`

**That's it!** 🎉

---

## 🔧 ALTERNATIVE: Install Java + Command Line

### Step 1: Install Java
1. Download Java JDK 11: https://adoptium.net/
2. Install with default settings
3. Restart PowerShell

### Step 2: Build APK
```powershell
# Run this script
.\build-apk.ps1
```

### Step 3: Get Your APK
Find it at: `app\build\outputs\apk\debug\app-debug.apk`

---

## 📱 Installing APK on Phone

### Method 1: Direct Transfer (Easiest)
1. Copy `app-debug.apk` to your phone
   - Via USB cable
   - Via email to yourself
   - Via cloud storage
2. On phone, open **File Manager**
3. Navigate to the APK file
4. Tap the APK
5. If prompted, enable "Install from unknown sources"
6. Tap **Install**
7. Tap **Open** to launch

### Method 2: Via USB (if ADB installed)
```powershell
adb install app\build\outputs\apk\debug\app-debug.apk
```

---

## 📋 Summary

### To Build APK:
**Option 1:** Use Android Studio (RECOMMENDED) ⭐
- Easiest, includes everything
- No command line needed
- Visual interface

**Option 2:** Install Java + run `.\build-apk.ps1`
- Faster if you already have Java
- Command line based

### To Install APK:
1. Transfer to phone
2. Open file manager
3. Tap APK file
4. Install

---

## 📖 Detailed Instructions

See **BUILD_APK_GUIDE.md** for complete step-by-step instructions.

---

## ❓ Still Need Help?

1. **Read**: BUILD_APK_GUIDE.md (comprehensive guide)
2. **Try**: Android Studio method (easiest)
3. **Check**: Make sure Java is installed: `java -version`

---

*APK Size: ~15-25 MB when built*
*Install Time: ~10-30 seconds*
*Works on: Android 9.0+ devices*
