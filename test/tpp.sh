#!/bin/sh
#
#  special test of preprocessing and related error detection

$JCONT -E tpp.icn >tpp.out 2>tpp.err
set -e
cmp tpp.std tpp.out
cmp tpp.stde tpp.err
