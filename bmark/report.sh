#!/bin/ksh
#
#  report progname...

echo ''
echo '          icont  jcont    ratio'
for p; do
    cat $p.it $p.jt | sed '
	s/.*real *\(.*\)user.*system/user 0m\1/
	s/m/ /
	s/s *$//
    ' | awk '
       /user/	{ t1 = t2; t2 = 60 * $2 + $3; }
       END      { printf("%-8s %6.2f %6.2f %8.2f\n", \
       		  "'$p'", t1, t2, t2 / t1); }
    '
done
echo ''
