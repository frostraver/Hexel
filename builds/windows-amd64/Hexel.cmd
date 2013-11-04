
@echo off
echo =============
echo HEXEL 
echo =============

set path=%path%;C:\Program Files\Java\jre7\bin
set path=%path%;C:\Program Files (x86)\Java\jre7\bin

for %%X in (java.exe) do (set FOUND=%%~$PATH:X)
if defined FOUND (
	echo you gotta leave this open until I get the energy to build a real launcher, sorry
	java -Xmx8192m -cp bin\Hexel.jar;lib\jogl-all.jar;lib\gluegen.jar;lib/guava-14.0.1.jar -Djava.library.path=lib\windows-amd64 Hexel.Hexel > NUL
) else (
	echo you need java installed, install at http://java.com/en/download/index.jsp
	pause
)





