# Energy-saver Test Execution Script
# PowerShell script to run comprehensive tests

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Energy-saver App Testing Suite" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Check if we're in the correct directory
if (-not (Test-Path ".\gradlew.bat")) {
    Write-Host "Error: gradlew.bat not found. Please run this script from the project root directory." -ForegroundColor Red
    exit 1
}

Write-Host "Step 1: Checking connected devices..." -ForegroundColor Yellow
$devices = & adb devices
Write-Host $devices
Write-Host ""

if ($devices -match "device$") {
    Write-Host "✓ Device connected" -ForegroundColor Green
} else {
    Write-Host "⚠ No device connected. Please connect an Android device or start an emulator." -ForegroundColor Red
    Write-Host "  Tip: You can start an emulator from Android Studio or run 'emulator -avd <avd_name>'" -ForegroundColor Yellow
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

Write-Host "Step 3: Building debug APK..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Build failed" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Build successful" -ForegroundColor Green
Write-Host ""

Write-Host "Step 4: Building test APK..." -ForegroundColor Yellow
& .\gradlew.bat assembleDebugAndroidTest
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Test APK build failed" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Test APK build successful" -ForegroundColor Green
Write-Host ""

Write-Host "Step 5: Running unit tests..." -ForegroundColor Yellow
& .\gradlew.bat test
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Unit tests failed" -ForegroundColor Red
} else {
    Write-Host "✓ Unit tests passed" -ForegroundColor Green
}
Write-Host ""

Write-Host "Step 6: Installing app on device..." -ForegroundColor Yellow
& .\gradlew.bat installDebug
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ App installation failed" -ForegroundColor Red
    exit 1
}
Write-Host "✓ App installed successfully" -ForegroundColor Green
Write-Host ""

Write-Host "Step 7: Granting required permissions..." -ForegroundColor Yellow
& adb shell pm grant com.energysaver.app android.permission.CAMERA
Write-Host "✓ Permissions granted" -ForegroundColor Green
Write-Host ""

Write-Host "Step 8: Running instrumentation tests..." -ForegroundColor Yellow
Write-Host "This may take several minutes..." -ForegroundColor Cyan
Write-Host ""

# Run each test class separately for better visibility
$testClasses = @(
    "com.energysaver.app.MainActivityTest",
    "com.energysaver.app.PermissionTest",
    "com.energysaver.app.CameraFunctionalityTest",
    "com.energysaver.app.DetectionFlowTest",
    "com.energysaver.app.TorchControlTest",
    "com.energysaver.app.IntegrationTest"
)

$passedTests = 0
$failedTests = 0

foreach ($testClass in $testClasses) {
    $testName = $testClass.Split('.')[-1]
    Write-Host "  Running $testName..." -ForegroundColor Cyan
    
    & .\gradlew.bat connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=$testClass
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✓ $testName passed" -ForegroundColor Green
        $passedTests++
    } else {
        Write-Host "  ✗ $testName failed" -ForegroundColor Red
        $failedTests++
    }
    Write-Host ""
}

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Test Execution Summary" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Test Classes Passed: $passedTests / $($testClasses.Count)" -ForegroundColor $(if ($passedTests -eq $testClasses.Count) { "Green" } else { "Yellow" })
Write-Host "Test Classes Failed: $failedTests / $($testClasses.Count)" -ForegroundColor $(if ($failedTests -eq 0) { "Green" } else { "Red" })
Write-Host ""

Write-Host "Step 9: Generating test report..." -ForegroundColor Yellow
$reportPath = "app\build\reports\androidTests\connected\index.html"
if (Test-Path $reportPath) {
    Write-Host "✓ Test report generated at: $reportPath" -ForegroundColor Green
    Write-Host ""
    Write-Host "Opening test report in browser..." -ForegroundColor Cyan
    Start-Process $reportPath
} else {
    Write-Host "⚠ Test report not found" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Testing Complete!" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

if ($failedTests -eq 0) {
    Write-Host "✓ All tests passed! The app is functional." -ForegroundColor Green
    exit 0
} else {
    Write-Host "⚠ Some tests failed. Please review the test report." -ForegroundColor Yellow
    exit 1
}
