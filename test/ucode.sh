#!/bin/sh
#
#  special test of ucode generation
#  rebuilds tgrlink & geddump using ucode mode

set -e
$JCONT -b -c -s tgrlink.icn
icont -s -o utiger tgrlink.u2
./utiger <tgrlink.dat >utiger.out
rm utiger
cmp tgrlink.std utiger.out
$JCONT -b -c -s geddump.icn
icont -o ugedd -s geddump.u2
./ugedd <geddump.dat >ugedd.out
rm ugedd
cmp geddump.std ugedd.out
