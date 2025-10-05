# Energy-saver App Testing Suite

## Overview
Comprehensive testing suite for the EnergySaver Android application using Espresso UI testing framework.

## Test Structure

### 1. MainActivityTest.kt
**Purpose**: Tests the main UI components and interactions

**Tests Included**:
- Activity launches successfully
- Camera preview is displayed
- Toggle button exists and becomes enabled
- Detection and torch status indicators are present
- Control panel is displayed
- UI components handle user interactions
- Loading and error states
- Double-tap handling

**Total Tests**: 16

### 2. PermissionTest.kt
**Purpose**: Tests permission handling

**Tests Included**:
- Camera permission granted
- Flashlight permission granted
- All required permissions granted
- Device has camera feature
- Device has flashlight feature

**Total Tests**: 5

### 3. CameraFunctionalityTest.kt
**Purpose**: Tests camera initialization and functionality

**Tests Included**:
- Camera preview visibility
- Camera preview enabled state
- Successful camera initialization
- Correct layout structure
- Camera lifecycle handling

**Total Tests**: 5

### 4. DetectionFlowTest.kt
**Purpose**: Tests human detection flow

**Tests Included**:
- Detection indicator visibility
- Detection status text visibility
- Initial detection status
- Detection flow after toggle
- Detection persistence during session
- Detection stops after toggle off

**Total Tests**: 7

### 5. TorchControlTest.kt
**Purpose**: Tests flashlight control

**Tests Included**:
- Torch indicator visibility
- Torch status text visibility
- Initial torch state (off)
- Torch status after enabling
- Torch turns off after disabling
- Multiple toggle handling
- State persistence

**Total Tests**: 8

### 6. IntegrationTest.kt
**Purpose**: Tests complete app functionality end-to-end

**Tests Included**:
- Complete user flow from launch to usage
- Multiple interaction handling without crashes
- Lifecycle management
- All components working together
- Error handling
- Long-running sessions

**Total Tests**: 6

## Total Test Coverage
- **47 individual test cases**
- **6 test classes**
- **Full UI coverage**
- **Permission testing**
- **Camera functionality**
- **Detection system**
- **Torch control**
- **Integration scenarios**

## Running the Tests

### Command Line (Gradle)

1. **Run all instrumentation tests**:
```powershell
.\gradlew connectedAndroidTest
```

2. **Run specific test class**:
```powershell
.\gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.energysaver.app.MainActivityTest
```

3. **Run unit tests**:
```powershell
.\gradlew test
```

4. **Run all tests**:
```powershell
.\gradlew test connectedAndroidTest
```

### Android Studio

1. **Run all tests**:
   - Right-click on `app/src/androidTest` folder
   - Select "Run 'Tests in 'androidTest''"

2. **Run specific test class**:
   - Open the test file
   - Click the green play button next to the class name
   - Or right-click and select "Run 'ClassName'"

3. **Run single test**:
   - Click the green play button next to the test method
   - Or right-click on the method and select "Run 'testName'"

## Prerequisites

### Device Requirements
- Android device or emulator running Android 9.0 (API 28) or higher
- Device with working camera
- Device with flashlight/flash capability
- USB debugging enabled (for physical devices)

### Build Requirements
- Android Studio Arctic Fox or later
- Gradle 7.0+
- Android SDK 28+

## Test Execution Steps

### 1. Setup
```powershell
# Navigate to project directory
cd c:\Users\admin\Desktop\KushalBhaykaAutomaticbulb\Energy-saver

# Ensure Gradle wrapper is executable
.\gradlew clean
```

### 2. Build the App
```powershell
.\gradlew assembleDebug
```

### 3. Build Test APK
```powershell
.\gradlew assembleDebugAndroidTest
```

### 4. Run Tests
```powershell
.\gradlew connectedAndroidTest
```

### 5. View Test Reports
Test reports are generated at:
```
app/build/reports/androidTests/connected/index.html
```

## Test Results Interpretation

### Success Indicators
- ✅ All tests pass (green)
- No crashes or exceptions
- UI elements respond correctly
- Permissions granted
- Camera initializes
- Detection system active
- Torch control works

### Potential Issues
- ❌ Permission denied: Enable permissions in device settings
- ❌ Camera unavailable: Check if camera is in use by another app
- ❌ Flashlight unavailable: Device may not have flash capability
- ❌ Timeout errors: Increase wait times in tests or use slower device

## CI/CD Integration

### GitHub Actions Example
```yaml
name: Android CI

on: [push, pull_request]

jobs:
  test:
    runs-on: macos-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK
      uses: actions/setup-java@v2
      with:
        java-version: '11'
    - name: Run Tests
      run: ./gradlew test
    - name: Run Instrumentation Tests
      uses: reactivecircus/android-emulator-runner@v2
      with:
        api-level: 29
        script: ./gradlew connectedAndroidTest
```

## Test Coverage Areas

### ✅ Functional Testing
- UI component rendering
- Button interactions
- State management
- Permission handling
- Camera initialization
- Detection flow
- Torch control

### ✅ Integration Testing
- Complete user workflows
- Component interaction
- Lifecycle management
- Error handling
- State persistence

### ✅ Stability Testing
- Rapid interactions
- Multiple toggles
- Long-running sessions
- Lifecycle transitions
- Memory management

## Troubleshooting

### Tests Not Running
1. Check device connection: `adb devices`
2. Ensure app is not installed: `adb uninstall com.energysaver.app`
3. Clean build: `.\gradlew clean`
4. Rebuild: `.\gradlew build`

### Permission Errors
1. Manually grant permissions on device
2. Use `GrantPermissionRule` in tests (already included)
3. Check manifest declarations

### Camera Errors
1. Close other camera apps
2. Restart device
3. Check camera permissions in device settings
4. Use physical device instead of emulator

### Timeout Errors
1. Increase `Thread.sleep()` durations
2. Use IdlingResources for better synchronization
3. Use slower device or emulator with more resources

## Best Practices

1. **Run tests on physical device** for accurate camera/flash testing
2. **Ensure good lighting** for detection tests
3. **Close other apps** before running tests
4. **Keep device awake** during test execution
5. **Review logs** for detailed error information
6. **Run tests in isolation** if flakiness occurs

## Continuous Improvement

### Future Test Additions
- Performance testing
- Battery usage testing
- Network condition testing
- Accessibility testing
- Screenshot testing
- Stress testing with memory profiling

### Metrics to Track
- Test execution time
- Pass/fail rates
- Code coverage percentage
- Flakiness indicators
- Device compatibility

## Conclusion

This comprehensive test suite ensures the EnergySaver app is fully functional across:
- ✅ UI interactions
- ✅ Permissions
- ✅ Camera functionality
- ✅ Detection system
- ✅ Torch control
- ✅ Complete user workflows

Run these tests regularly to maintain app quality and catch regressions early.
