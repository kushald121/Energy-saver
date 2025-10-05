# Quick Test Script - Runs all tests at once
# PowerShell script for quick test execution

Write-Host "Energy-saver Quick Test" -ForegroundColor Cyan
Write-Host "======================" -ForegroundColor Cyan
Write-Host ""

# Check device connection
Write-Host "Checking device connection..." -ForegroundColor Yellow
$devices = & adb devices
if ($devices -notmatch "device$") {
    Write-Host "No device connected!" -ForegroundColor Red
    exit 1
}
Write-Host "Device connected ✓" -ForegroundColor Green
Write-Host ""

# Grant permissions
Write-Host "Granting permissions..." -ForegroundColor Yellow
& adb shell pm grant com.energysaver.app android.permission.CAMERA 2>$null
Write-Host "Permissions granted ✓" -ForegroundColor Green
Write-Host ""

# Run all tests
Write-Host "Running all instrumentation tests..." -ForegroundColor Yellow
Write-Host "(This will take a few minutes)" -ForegroundColor Cyan
Write-Host ""

& .\gradlew.bat connectedAndroidTest

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "All tests passed! ✓" -ForegroundColor Green
    
    # Open report
    $reportPath = "app\build\reports\androidTests\connected\index.html"
    if (Test-Path $reportPath) {
        Write-Host "Opening test report..." -ForegroundColor Cyan
        Start-Process $reportPath
    }
} else {
    Write-Host ""
    Write-Host "Some tests failed!" -ForegroundColor Red
    Write-Host "Check the test report for details." -ForegroundColor Yellow
}
