# Manual Test Checklist for Energy-saver App

## Pre-Test Setup
- [ ] Android device or emulator is connected
- [ ] Device running Android 9.0 (API 28) or higher
- [ ] Device has working camera
- [ ] Device has flashlight capability
- [ ] Good lighting conditions for testing
- [ ] No other apps using the camera

## Installation Tests
- [ ] App installs successfully
- [ ] App icon appears in launcher
- [ ] App launches without crash
- [ ] Splash screen (if any) displays correctly

## Permission Tests
- [ ] Camera permission request appears on first launch
- [ ] Flashlight permission request appears
- [ ] App explains why permissions are needed
- [ ] Denying permissions shows appropriate message
- [ ] Granting permissions enables app functionality
- [ ] Permission can be revoked in settings
- [ ] App handles revoked permissions gracefully

## UI Tests
- [ ] Camera preview displays correctly
- [ ] Camera preview fills expected area
- [ ] Toggle button is visible
- [ ] Toggle button text is clear
- [ ] Detection indicator is visible
- [ ] Torch indicator is visible
- [ ] Status texts are readable
- [ ] Layout looks good in portrait mode
- [ ] Colors and theme are consistent

## Camera Functionality
- [ ] Camera initializes within 3 seconds
- [ ] Camera preview shows live feed
- [ ] Camera preview is smooth (no lag)
- [ ] Camera focuses properly
- [ ] Camera works in different lighting
- [ ] App handles camera interruption (incoming call)
- [ ] Camera releases when app closes

## Detection System
- [ ] Detection starts when toggle is enabled
- [ ] Detection indicator changes color when person detected
- [ ] Detection text updates ("Searching..." → "Detected!")
- [ ] Detection works from 1-3 meters distance
- [ ] Detection works with different body positions
- [ ] Detection handles multiple people
- [ ] Detection doesn't trigger on objects/pets
- [ ] Detection stops when toggle is disabled

## Torch Control
- [ ] Torch turns ON when person detected
- [ ] Torch turns OFF when person leaves
- [ ] Torch has appropriate delay (no flickering)
- [ ] Torch stays ON while person present
- [ ] Torch turns OFF immediately when toggle disabled
- [ ] Torch indicator updates correctly
- [ ] Torch brightness is adequate
- [ ] Torch doesn't overheat during extended use

## Toggle Button Functionality
- [ ] Button starts in disabled state
- [ ] Button enables after initialization
- [ ] Button responds to tap
- [ ] Button shows visual feedback on tap
- [ ] Button enables energy saver mode
- [ ] Second tap disables energy saver
- [ ] Button state persists during session
- [ ] Rapid tapping doesn't cause issues

## Error Handling
- [ ] Camera unavailable shows error message
- [ ] Torch unavailable shows error message
- [ ] Permission denied shows helpful message
- [ ] Error messages are clear and actionable
- [ ] Errors don't crash the app
- [ ] App recovers from errors gracefully

## Performance Tests
- [ ] App launches in < 3 seconds
- [ ] Camera preview starts in < 3 seconds
- [ ] Detection responds in < 1 second
- [ ] Torch activates in < 500ms
- [ ] UI remains responsive during operation
- [ ] No noticeable memory leaks
- [ ] Battery drain is reasonable
- [ ] App doesn't overheat device

## Lifecycle Tests
- [ ] App handles home button (minimize)
- [ ] App handles back button
- [ ] App handles recent apps switch
- [ ] App handles screen rotation (if enabled)
- [ ] App handles incoming call
- [ ] App handles low battery state
- [ ] App handles screen off/on
- [ ] Camera releases on app pause
- [ ] Torch turns off on app pause

## Edge Cases
- [ ] App works in very bright conditions
- [ ] App works in low light conditions
- [ ] App handles flashlight already in use
- [ ] App handles camera already in use
- [ ] App handles rapid person movement
- [ ] App handles person at edge of frame
- [ ] App handles partial person visibility
- [ ] Multiple rapid toggles don't cause issues

## Long-Running Tests
- [ ] App runs stable for 5 minutes
- [ ] App runs stable for 10 minutes
- [ ] No memory leaks after extended use
- [ ] Detection remains accurate over time
- [ ] Torch remains functional over time
- [ ] No performance degradation

## Stress Tests
- [ ] 10 rapid toggles in a row
- [ ] Detection with rapid movement
- [ ] Torch on/off 20+ times
- [ ] App pause/resume 10+ times
- [ ] Low memory conditions
- [ ] Low battery conditions

## Accessibility
- [ ] Button text is large enough
- [ ] Status text is readable
- [ ] Color contrast is sufficient
- [ ] Touch targets are adequate size
- [ ] Works with TalkBack (if supported)

## Uninstallation
- [ ] App uninstalls cleanly
- [ ] No data left behind (verify in Settings)
- [ ] Reinstall works correctly

## Test Results Summary

**Date**: _______________
**Device**: _______________
**Android Version**: _______________
**Tester**: _______________

**Total Tests**: 105
**Passed**: _______________
**Failed**: _______________
**Skipped**: _______________

**Overall Result**: ☐ PASS  ☐ FAIL  ☐ NEEDS WORK

**Critical Issues Found**:
1. _______________________________________________
2. _______________________________________________
3. _______________________________________________

**Minor Issues Found**:
1. _______________________________________________
2. _______________________________________________
3. _______________________________________________

**Recommendations**:
_______________________________________________
_______________________________________________
_______________________________________________

**Tester Signature**: _______________
