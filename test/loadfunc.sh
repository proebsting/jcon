#!/bin/sh
#
#  special test of loadfunc()

JC=${JC-javac}

set -e
CLASSPATH=../bin/jcon.zip $JC factors.java sum3.java threen.java
jar cfM jfuncs.zip factors*.class sum3*.class threen*.class
$JCONT -s -c load[12].icn
$JCONT -s loadfunc.icn
./loadfunc >loadfunc.out
cmp loadfunc.std loadfunc.out
rm jfuncs.zip load?.zip factors*.class sum3*.class threen*.class
