# Build APK Script for Energy-saver

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Building Energy-saver APK" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check Java
Write-Host "Step 1: Checking Java..." -ForegroundColor Yellow
try {
    $javaVersion = & java -version 2>&1
    Write-Host "✓ Java found: $($javaVersion[0])" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Java JDK 11 or higher:" -ForegroundColor Yellow
    Write-Host "  1. Download from: https://adoptium.net/" -ForegroundColor White
    Write-Host "  2. Install with default settings" -ForegroundColor White
    Write-Host "  3. Restart PowerShell" -ForegroundColor White
    Write-Host "  4. Run this script again" -ForegroundColor White
    Write-Host ""
    Write-Host "Or use Android Studio to build (easier):" -ForegroundColor Yellow
    Write-Host "  1. Install Android Studio" -ForegroundColor White
    Write-Host "  2. Open this project" -ForegroundColor White
    Write-Host "  3. Build > Build Bundle(s) / APK(s) > Build APK(s)" -ForegroundColor White
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Step 2: Cleaning previous builds..." -ForegroundColor Yellow
& .\gradlew.bat clean

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Clean failed" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Clean successful" -ForegroundColor Green

Write-Host ""
Write-Host "Step 3: Building Debug APK..." -ForegroundColor Yellow
Write-Host "(This may take a few minutes on first build)" -ForegroundColor Cyan
& .\gradlew.bat assembleDebug

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "✓ APK Built Successfully!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host ""
    
    $apkPath = "app\build\outputs\apk\debug\app-debug.apk"
    
    if (Test-Path $apkPath) {
        $fullPath = Resolve-Path $apkPath
        Write-Host "APK Location:" -ForegroundColor Cyan
        Write-Host "  $fullPath" -ForegroundColor White
        Write-Host ""
        
        $apkSize = (Get-Item $apkPath).Length / 1MB
        Write-Host "APK Size: $([math]::Round($apkSize, 2)) MB" -ForegroundColor Cyan
        Write-Host ""
        
        Write-Host "=====================================" -ForegroundColor Cyan
        Write-Host "How to Install:" -ForegroundColor Cyan
        Write-Host "=====================================" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Option 1: Via USB (if ADB available)" -ForegroundColor Yellow
        Write-Host "  adb install `"$apkPath`"" -ForegroundColor White
        Write-Host ""
        Write-Host "Option 2: Direct Transfer" -ForegroundColor Yellow
        Write-Host "  1. Copy APK to your phone" -ForegroundColor White
        Write-Host "  2. Open file manager on phone" -ForegroundColor White
        Write-Host "  3. Tap the APK file" -ForegroundColor White
        Write-Host "  4. Enable 'Install from unknown sources' if prompted" -ForegroundColor White
        Write-Host "  5. Tap Install" -ForegroundColor White
        Write-Host ""
        
        Write-Host "Opening APK folder..." -ForegroundColor Yellow
        Start-Sleep -Seconds 1
        Invoke-Item (Split-Path $apkPath)
        
        Write-Host ""
        Write-Host "✓ Build complete! APK is ready to install." -ForegroundColor Green
    } else {
        Write-Host "⚠ APK file not found at expected location" -ForegroundColor Yellow
        Write-Host "Check: app\build\outputs\apk\debug\" -ForegroundColor White
    }
} else {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Red
    Write-Host "✗ Build Failed!" -ForegroundColor Red
    Write-Host "=====================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common solutions:" -ForegroundColor Yellow
    Write-Host "  1. Make sure Java JDK is installed" -ForegroundColor White
    Write-Host "  2. Check error messages above" -ForegroundColor White
    Write-Host "  3. Try opening project in Android Studio" -ForegroundColor White
    Write-Host "  4. Check BUILD_APK_GUIDE.md for detailed help" -ForegroundColor White
    Write-Host ""
}

Write-Host ""
Read-Host "Press Enter to exit"
