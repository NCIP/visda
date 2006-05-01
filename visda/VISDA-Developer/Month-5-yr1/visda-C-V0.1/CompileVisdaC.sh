echo ------------------------------------------------------------
echo -- This shell script will compile VISDA C++ codes.
echo -- It assumes that you have the gcc compiler in your PATH.
echo ------------------------------------------------------------

CC=gcc
CFLAGS="-O2 -Wall -fPIC -DVISDALINUX"
# CFLAGS="-O2 -Wall -fPIC -D_DEBUG -DDEBUG -DVISDALINUX"
OUTDIR=./out
OBJDIR=./out
SRCDIR=./src
INCDIR=./header

rm ${OBJDIR}/*.o ${OBJDIR}/*.a
rm ${OUTDIR}/*.so

g++ $CFLAGS -c -o $OBJDIR/mathtool.o $SRCDIR/mathtool.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veCov.o $SRCDIR/veCov.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veModel.o $SRCDIR/veModel.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/vePCA.o $SRCDIR/vePCA.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/vePCAPPM.o $SRCDIR/vePCAPPM.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veSNR.o $SRCDIR/veSNR.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veSubEM.o $SRCDIR/veSubEM.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veSubPCA.o $SRCDIR/veSubPCA.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/veSubPCAPPM.o $SRCDIR/veSubPCAPPM.cpp -I$INCDIR
g++ $CFLAGS -c -o $OBJDIR/CFMCore.o $SRCDIR/CFMCore.cpp -I$INCDIR -I/opt/jdk1.5.0_04/include -I/opt/jdk1.5.0_04/include/linux
g++ $CFLAGS -c -o $OBJDIR/DRMCore.o $SRCDIR/DRMCore.cpp -I$INCDIR -I/opt/jdk1.5.0_04/include -I/opt/jdk1.5.0_04/include/linux
g++ $CFLAGS -c -o $OBJDIR/FeaturePreSelector.o $SRCDIR/FeaturePreSelector.cpp -I$INCDIR -I/opt/jdk1.5.0_04/include -I/opt/jdk1.5.0_04/include/linux
g++ $CFLAGS -c -o $OBJDIR/VeVisT.o $SRCDIR/VeVisT.cpp -I$INCDIR -I/opt/jdk1.5.0_04/include -I/opt/jdk1.5.0_04/include/linux

ar -r $OBJDIR/VISDA_CLib.a $OBJDIR/mathtool.o $OBJDIR/veCov.o $OBJDIR/veModel.o $OBJDIR/vePCA.o $OBJDIR/vePCAPPM.o $OBJDIR/veSubEM.o $OBJDIR/veSubPCA.o $OBJDIR/veSubPCAPPM.o $OBJDIR/veSNR.o

g++ $CFLAGS -shared -o $OUTDIR/libVISDA_CJavaInterface.so $OBJDIR/CFMCore.o $OBJDIR/DRMCore.o $OBJDIR/FeaturePreSelector.o $OBJDIR/VeVisT.o  $OBJDIR/VISDA_CLib.a -lm -llapack -lpthread

echo "Compilation finished successfully."


