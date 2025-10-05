# Generate Placeholder Launcher Icons
# This script creates simple colored PNG files as launcher icons

Write-Host "Generating placeholder launcher icons..." -ForegroundColor Cyan

# Add System.Drawing assembly
Add-Type -AssemblyName System.Drawing

# Define icon sizes for different densities
$iconSizes = @{
    "mipmap-mdpi" = 48
    "mipmap-hdpi" = 72
    "mipmap-xhdpi" = 96
    "mipmap-xxhdpi" = 144
    "mipmap-xxxhdpi" = 192
}

# Base path
$basePath = "app\src\main\res"

foreach ($folder in $iconSizes.Keys) {
    $size = $iconSizes[$folder]
    $folderPath = Join-Path $basePath $folder
    
    # Create folder if it doesn't exist
    if (-not (Test-Path $folderPath)) {
        New-Item -ItemType Directory -Path $folderPath -Force | Out-Null
    }
    
    # Create ic_launcher.png
    $launcherPath = Join-Path $folderPath "ic_launcher.png"
    $bitmap = New-Object System.Drawing.Bitmap($size, $size)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    # Fill with blue background
    $blueBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(33, 150, 243))
    $graphics.FillRectangle($blueBrush, 0, 0, $size, $size)
    
    # Draw white circle (simple bulb representation)
    $whiteBrush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::White)
    $circleSize = [int]($size * 0.6)
    $circleX = [int](($size - $circleSize) / 2)
    $circleY = [int](($size - $circleSize) / 2)
    $graphics.FillEllipse($whiteBrush, $circleX, $circleY, $circleSize, $circleSize)
    
    # Save PNG
    $bitmap.Save($launcherPath, [System.Drawing.Imaging.ImageFormat]::Png)
    
    # Cleanup
    $graphics.Dispose()
    $bitmap.Dispose()
    $blueBrush.Dispose()
    $whiteBrush.Dispose()
    
    # Copy as round icon
    $roundPath = Join-Path $folderPath "ic_launcher_round.png"
    Copy-Item $launcherPath $roundPath
    
    Write-Host "  Created icons in $folder ($size x $size)" -ForegroundColor Green
}

Write-Host ""
Write-Host "All launcher icons generated successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "You can now build the APK." -ForegroundColor Cyan
