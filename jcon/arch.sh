#!/bin/sh
#
#  Return canonical platform name.

set `uname -sr`

case "$1$2" in
    SunOS4*)    ARCH=sunos;;
    SunOS5*)    ARCH=solaris;;
    IRIX*)      ARCH=irix;;
    OSF*)       ARCH=digital;;
    ULTRIX*)    ARCH=ultrix;;
    Linux*)     ARCH=linux;;
    HP-UX*)     ARCH=hpux;;
    AIX*)       ARCH=aix;;
    *)          echo 1>&2 "unrecognized system: $*"; exit 1;;
esac

echo $ARCH
