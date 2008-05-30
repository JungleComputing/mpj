@echo off

if "%OS%"=="Windows_NT" @setlocal

rem %~dp0 is expanded pathname of the current script under NT

if "%MPJ_HOME%X"=="X" set MPJ_HOME=%~dp0..

set JAVACLASSPATH=%CLASSPATH%;
for %%i in ("%MPJ_HOME%\lib\*.jar") do call "%MPJ_HOME%\bin\AddToMpjClassPath.bat" %%i

set MPJ_APP_ARGS=

:setupArgs
if ""%1""=="""" goto doneStart
set MPJ_APP_ARGS=%MPJ_APP_ARGS% %1
shift
goto setupArgs

:doneStart

java -classpath "%JAVACLASSPATH%" -Dlog4j.configuration=file:"%MPJ_HOME%"\log4j.properties %MPJ_APP_ARGS%

if "%OS%"=="Windows_NT" @endlocal
