#!/bin/sh
#
#  special test of linking and argument handling

set -e
rm -f link[12] link[12].zip
$JCONT $JCOPT -c link2
$JCONT $JCOPT link1
./link1 quick brown fox >linking.out
$JCONT $JCOPT -c link1
$JCONT $JCOPT link2.zip link1.zip
echo "" >>linking.out
./link2 lazy gray dog >>linking.out
cmp linking.std linking.out
