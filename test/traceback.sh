#!/bin/sh
#
#  special test of traceback 
rm -f traceback.out
$JCONT $JCOPT traceback.icn 
./traceback >traceback.out 2>&1 
echo "-------------------------" >>traceback.out
$JCONT $JCOPT -fn traceback.icn 
./traceback >>traceback.out 2>&1
set -e 
cmp traceback.std traceback.out
