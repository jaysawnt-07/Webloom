@echo off
cd /d "%~dp0"
title WebForge - Java Website Builder & Template Studio

echo ========================================================
echo   WebForge: Java Website Builder & Template Studio
echo ========================================================
echo.
echo Starting application on http://localhost:8080 ...
echo Press Ctrl+C to stop.
echo.

"C:\PROGRA~1\ECLIPS~1\JDK-17~1.101\bin\java.exe" -jar website-builder.jar

pause
