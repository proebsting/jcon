#!/bin/sh
#
#  special test of procedure tracing output

rm -f tracing.out
$JCONT $JCOPT -fn tracing.icn 
./tracing 2>tracing.out
set -e 
cmp tracing.std tracing.out
