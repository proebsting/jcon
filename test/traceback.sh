#!/bin/sh
#
#  special test of traceback 
rm -f traceback.out
$JCONT $JCOPT traceback.icn 
./traceback >/dev/null 2>traceback.out
echo "-------------------------" >>traceback.out
$JCONT $JCOPT -fn traceback.icn 
./traceback >/dev/null 2>>traceback.out
set -e 
cmp traceback.std traceback.out
