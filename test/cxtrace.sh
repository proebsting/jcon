#!/bin/sh
#
#  special test of co-expression tracing
#  (edits output to match v9's omission of co-expression return count)
set -e 
$JCONT $JCOPT -fd cxtrace.icn 
./cxtrace 2>&1 | sed '/;/s/([0-9][0-9]*)//g' >cxtrace.out
cmp cxtrace.std cxtrace.out
