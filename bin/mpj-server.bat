@echo off

if "%OS%"=="Windows_NT" @setlocal

rem %~dp0 is expanded pathname of the current script under NT

if "%MPJ_HOME%X"=="X" set MPJ_HOME=%~dp0..

set JAVACLASSPATH=%CLASSPATH%;
for %%i in ("%MPJ_HOME%\lib\*.jar") do call "%MPJ_HOME%\bin\AddToMpjClassPath.bat" %%i

set SERVER_ARGS=

:setupArgs

if ""%1""=="""" goto doneArgs

set SERVER_ARGS=%SERVER_ARGS% "%1"

shift
goto setupArgs

rem This label provides a place for the argument list loop to break out
rem and for NT handling to skip to.

:doneArgs

java -classpath "%JAVACLASSPATH%" -Dlog4j.configuration=file:"%MPJ_HOME%"\log4j.properties ibis.server.Server %SERVER_ARGS%

if "%OS%"=="Windows_NT" @endlocal
