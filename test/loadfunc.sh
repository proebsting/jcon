#!/bin/sh
#
#  special test of loadfunc()

set -e
CLASSPATH=../bin/rts.zip javac factors.java sum3.java threen.java
jar cfM jfuncs.zip factors*.class sum3*.class threen*.class
$JCONT -s loadfunc.icn
./loadfunc >loadfunc.out
cmp loadfunc.std loadfunc.out
rm jfuncs.zip factors*.class sum3*.class threen*.class
