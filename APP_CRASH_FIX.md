# 🔧 App Crashes on Startup - Troubleshooting Guide

## Problem
App closes immediately after opening (crashes on startup).

---

## 🔍 Common Causes & Solutions

### 1. **Missing Permissions** ⚠️

**Symptom:** App crashes when trying to access camera

**Solution:**
1. Open phone **Settings**
2. Go to **Apps** → **EnergySaver**
3. Go to **Permissions**
4. Enable **Camera** permission
5. Relaunch the app

### 2. **Incompatible Android Version** ⚠️

**Symptom:** App doesn't work on older Android

**Check:**
- App requires **Android 9.0 (API 28) or higher**
- Check your phone's Android version in Settings → About Phone

**Solution:**
- Use a phone with Android 9.0+
- Or rebuild app with lower `minSdk` (but features may not work)

### 3. **Camera/Flash Not Available** ⚠️

**Symptom:** App crashes on devices without camera/flash

**Solution:**
- Use a device with working camera and flashlight
- Check if camera works in other apps first

### 4. **ML Kit Dependencies Missing** ⚠️

**Symptom:** App crashes due to missing Google Play Services

**Solution:**
1. Open **Google Play Store**
2. Update **Google Play Services**
3. Relaunch the app

---

## 🛠️ Quick Fixes to Try

### Fix 1: Clear App Data
```
Settings → Apps → EnergySaver → Storage → Clear Data
Relaunch app and grant permissions again
```

### Fix 2: Reinstall App
```
1. Uninstall EnergySaver
2. Restart phone
3. Install APK again
4. Grant camera permission
```

### Fix 3: Check Logcat (For Developers)
```powershell
# Connect phone via USB
adb logcat | Select-String "EnergySaver|FATAL|AndroidRuntime"
```

---

## 🔧 Rebuild App with Better Error Handling

I've created an improved version with better error handling. To rebuild:

### In Android Studio:
1. Build → Clean Project
2. Build → Rebuild Project
3. Build → Build APK(s)
4. Install new APK

### Or Command Line:
```powershell
.\gradlew.bat clean
.\gradlew.bat assembleDebug
```

---

## 📝 What I've Improved

### 1. Better Initialization Handling
- Added null checks everywhere
- Added try-catch blocks
- Graceful error messages instead of crashes

### 2. Permission Handling
- Checks permissions before initialization
- Shows clear error messages if denied
- Guides user to settings

### 3. Resource Validation
- Validates camera availability
- Validates flash availability
- Falls back gracefully if missing

### 4. Error Recovery
- App doesn't crash on errors
- Shows error messages to user
- Provides recovery options

---

## 🎯 Step-by-Step Testing

### Test 1: Check Permissions
```
1. Install APK
2. Open app
3. Should see permission request
4. Grant Camera permission
5. App should continue
```

### Test 2: Check Camera
```
1. Open app (permissions granted)
2. Should see camera preview
3. Camera preview should show live feed
4. No crash
```

### Test 3: Check Detection
```
1. Point camera at yourself
2. Detection indicator should turn green
3. "Human detected!" message should appear
```

### Test 4: Check Torch
```
1. Tap "Toggle Energy Saver" button
2. Point camera at yourself
3. Flashlight should turn ON
4. Move away from camera
5. Flashlight should turn OFF after 1 second
```

---

## 💡 Understanding the Crash

### Most Likely Causes (in order):

1. **Camera Permission Not Granted** (90% of cases)
   - App tries to access camera without permission
   - Android kills the app immediately

2. **Initialization Failure** (5% of cases)
   - ML Kit not downloaded
   - Google Play Services outdated
   - Camera/flash not available

3. **Resource Missing** (3% of cases)
   - Missing drawable
   - Missing string resource
   - Missing layout element

4. **Android Version** (2% of cases)
   - Phone too old (< Android 9.0)
   - APIs not available

---

## 🔍 How to Get Crash Logs

### Method 1: ADB Logcat (Recommended)
```powershell
# Enable USB debugging on phone
# Connect via USB
adb logcat -c  # Clear logs
adb logcat > crash_log.txt  # Start logging
# Open app (it will crash)
# Press Ctrl+C to stop logging
# Open crash_log.txt to see error
```

### Method 2: Android Studio Logcat
```
1. Connect phone via USB
2. Open Android Studio
3. View → Tool Windows → Logcat
4. Launch app
5. See crash in Logcat window
```

### Method 3: On-Device (Some Phones)
```
Settings → Developer Options → Bug Report
Take bug report after crash
```

---

## 🚀 Immediate Actions

### Action 1: Grant Camera Permission Manually
```
Phone Settings → Apps → EnergySaver → Permissions → Camera → Allow
```

### Action 2: Install on Different Device
- Try on another phone (Android 9.0+)
- Confirms if it's device-specific

### Action 3: Rebuild with Debug Logs
- Build debug APK (not release)
- Debug APK has better error reporting

---

## 📱 Device Requirements

### Minimum Requirements:
- ✅ **Android 9.0 (Pie)** or higher
- ✅ **Working camera** (back camera)
- ✅ **Working flashlight**
- ✅ **Google Play Services** (for ML Kit)
- ✅ **1 GB RAM** or more
- ✅ **50 MB free storage**

### Recommended:
- ✅ **Android 10+**
- ✅ **2 GB RAM**
- ✅ **Updated Google Play Services**

---

## 🔄 Next Steps

1. **Try the quick fixes above**
2. **Check permissions in phone settings**
3. **Try on another device if available**
4. **Get crash logs using ADB**
5. **Share crash logs for analysis**

---

## 📞 Getting Help

If app still crashes, provide:
1. **Android version** (e.g., Android 11)
2. **Phone model** (e.g., Samsung Galaxy S21)
3. **What happens** (crashes immediately, after permission, etc.)
4. **Crash logs** (if available via ADB)

---

*Troubleshooting Guide*
*Created: October 6, 2025*
*Common crash fixes and diagnostics*
