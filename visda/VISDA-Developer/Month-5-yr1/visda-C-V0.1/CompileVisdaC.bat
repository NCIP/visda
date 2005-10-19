@echo off
echo ------------------------------------------------------------
echo -- This DOS batch script will compile VISDA C++ codes.
echo -- It assumes that you have the Visual C++ compiler 
echo --  and Java in your PATH.
echo ------------------------------------------------------------


set OUTDIR=.\out
set OBJDIR=.\out
set SRCDIR=.\src
set HEADERDIR=.\header
set LIBDIR=.\lib

if not exist %OBJDIR%/%NULL% mkdir %OBJDIR%
if not exist %OUTDIR%/%NULL% mkdir %OUTDIR%

del %OBJDIR%\mathtool.obj
del %OBJDIR%\veCov.obj
del %OBJDIR%\veModel.obj
del %OBJDIR%\vePCA.obj
del %OBJDIR%\vePCAPPM.obj
del %OBJDIR%\veSNR.obj
del %OBJDIR%\veSubEM.obj
del %OBJDIR%\veSubPCA.obj
del %OBJDIR%\veSubPCAPPM.obj
del %LIBDIR%\VISDA_CLib.lib
del %OBJDIR%\CFMCore.obj
del %OBJDIR%\DRMCore.obj
del %OBJDIR%\FeaturePreSelector.obj
del %OBJDIR%\VeVisT.obj
del %OUTDIR%\VISDA_CJavaInterface.dll
del %OUTDIR%\VISDA_CJavaInterface.exp
del %OUTDIR%\VISDA_CJavaInterface.ilk
del %OUTDIR%\VISDA_CJavaInterface.lib

cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\mathtool.cpp  
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veCov.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veModel.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\vePCA.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\vePCAPPM.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veSNR.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veSubEM.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veSubPCA.cpp
cl /I"%HEADERDIR%" /Fo"%OBJDIR%\\" /c %SRCDIR%\veSubPCAPPM.cpp
cl /I"%HEADERDIR%" /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /MD /nologo /W3 /GX /O2 /Fo"%OBJDIR%\\" /c %SRCDIR%\CFMCore.cpp
cl /I"%HEADERDIR%" /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /MD /nologo /W3 /GX /O2 /Fo"%OBJDIR%\\" /c %SRCDIR%\DRMCore.cpp
cl /I"%HEADERDIR%" /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /MD /nologo /W3 /GX /O2 /Fo"%OBJDIR%\\" /c %SRCDIR%\FeaturePreSelector.cpp
cl /I"%HEADERDIR%" /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /MD /nologo /W3 /GX /O2 /Fo"%OBJDIR%\\" /c %SRCDIR%\VeVisT.cpp

link -lib /nologo /LIBPATH:"%LIBDIR%" /out:"%LIBDIR%\VISDA_CLib.lib" %OBJDIR%\mathtool.obj %OBJDIR%/veCov.obj %OBJDIR%/veModel.obj %OBJDIR%/vePCA.obj %OBJDIR%/vePCAPPM.obj %OBJDIR%/veSubEM.obj %OBJDIR%/veSubPCA.obj %OBJDIR%/veSubPCAPPM.obj %OBJDIR%/veSNR.obj 
link clapack.lib VISDA_CLib.lib /libpath:"%LIBDIR%" /nologo /dll /incremental:yes /out:"%OUTDIR%\VISDA_CJavaInterface.dll" %OBJDIR%/CFMCore.obj %OBJDIR%/DRMCore.obj %OBJDIR%/FeaturePreSelector.obj %OBJDIR%/VeVisT.obj
  
