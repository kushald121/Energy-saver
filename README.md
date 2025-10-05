# EnergySaver - Intelligent Lighting Control App

## Overview

EnergySaver is an intelligent Android application that automatically controls your smartphone's flashlight based on human presence detection. Using advanced computer vision and machine learning, it provides illumination only when spaces are occupied, reducing energy waste and providing hands-free lighting control.

## Features

### Core Functionality
- **Human Detection**: Uses ML Kit object detection to identify human presence in real-time
- **Automatic Torch Control**: Intelligently turns flashlight on/off based on detection
- **Smart Debouncing**: Prevents flickering with configurable detection delays
- **Responsive UI**: Real-time status indicators and smooth animations

### Technical Features
- **CameraX Integration**: Modern camera API for reliable camera access
- **ML Kit Object Detection**: Google's machine learning for person detection
- **Kotlin Coroutines**: Asynchronous processing for smooth performance
- **MVVM Architecture**: Clean separation of concerns with ViewModel pattern
- **Permission Management**: Robust handling of camera and flashlight permissions

## Architecture

### Components

1. **CameraManager**: Handles camera initialization, preview, and lifecycle
2. **ObjectDetector**: ML Kit integration for human detection
3. **TorchManager**: Flashlight control with safety checks
4. **TorchController**: Smart torch control with activation/deactivation delays
5. **DetectionDebouncer**: Prevents detection flickering
6. **StatusManager**: UI animations and status management
7. **PermissionManager**: Runtime permission handling

### Data Flow

```
Camera → ObjectDetector → DetectionDebouncer → TorchController → TorchManager
                                                      ↓
UI State ← MainViewModel ← StatusManager ← UI Updates
```

## Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 28+ (Android 9.0+)
- Device with camera and flashlight

### Build Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device

```bash
git clone <repository-url>
cd EnergySaver
./gradlew build
```

## Usage

1. **Launch App**: Open EnergySaver on your device
2. **Grant Permissions**: Allow camera and flashlight access when prompted
3. **Position Device**: Point camera at the area you want to monitor
4. **Enable Energy Saver**: Tap the toggle button to start automatic control
5. **Monitor Status**: Watch the indicators for detection and torch status

### Status Indicators

- **Green Circle**: Human detected
- **Orange Circle**: Searching for humans
- **Yellow Circle**: Torch is ON
- **Gray Circle**: Torch is OFF

## Configuration

### Detection Settings
- **Frame Rate**: Processes every 3rd frame for performance
- **Debounce Delay**: 500ms default to prevent flickering
- **Detection Area**: Focuses on upper 60% of frame for upper body detection

### Torch Settings
- **Activation Delay**: 200ms before turning on
- **Deactivation Delay**: 1000ms before turning off
- **Safety Checks**: Validates camera and flashlight availability

## Performance Optimizations

- **Frame Rate Limiting**: Processes every 3rd frame to reduce CPU usage
- **Memory Management**: Proper cleanup of camera and ML Kit resources
- **Background Processing**: Uses dedicated executor for image analysis
- **State Management**: Efficient UI updates with LiveData

## Error Handling

- **Permission Denied**: Graceful fallback with settings redirect
- **Camera Unavailable**: Clear error messages and recovery suggestions
- **Detection Failures**: Silent handling to prevent UI spam
- **Torch Errors**: User-friendly error messages with retry options

## Testing

### Manual Testing Checklist
- [ ] Permission requests work correctly
- [ ] Camera preview displays properly
- [ ] Human detection responds to movement
- [ ] Torch activates/deactivates smoothly
- [ ] App handles device rotation
- [ ] Background/foreground transitions work
- [ ] Error states display appropriate messages

### Device Compatibility
- Tested on Android 9.0+ devices
- Requires back camera with flash
- Works in various lighting conditions
- Optimized for portrait orientation

## Future Enhancements

### Planned Features
- **Custom Detection Zones**: Define specific areas for detection
- **Sensitivity Settings**: Adjustable detection thresholds
- **Multiple Torch Modes**: Different brightness levels
- **Scheduling**: Time-based automatic control
- **Analytics**: Usage statistics and energy savings tracking

### Technical Improvements
- **Pose Detection**: More accurate human detection
- **Custom ML Models**: Device-specific optimization
- **Background Processing**: Continue detection when app is minimized
- **Multi-Camera Support**: Front camera option for self-monitoring

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, feature requests, or questions:
- Create an issue on GitHub
- Check the troubleshooting section
- Review the FAQ

## Acknowledgments

- Google ML Kit for object detection capabilities
- Android CameraX team for camera APIs
- Material Design for UI components
- Kotlin Coroutines for asynchronous programming

