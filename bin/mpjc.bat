@echo off

if "%OS%"=="Windows_NT" @setlocal

rem %~dp0 is expanded pathname of the current script under NT

if "%MPJ_HOME%X"=="X" set MPJ_HOME=%~dp0..

set MPJC_ARGS=

:setupArgs
if ""%1""=="""" goto doneStart
set MPJC_ARGS=%MPJC_ARGS% "%1"
shift
goto setupArgs

:doneStart

java -classpath "%JAVACLASSPATH%;%MPJ_HOME%\lib\*" -Dlog4j.configuration=file:"%MPJ_HOME%"\log4j.properties ibis.compile.Ibisc %MPJC_ARGS%

if "%OS%"=="Windows_NT" @endlocal

