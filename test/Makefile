#  Test Directory Makefile
#
#  to run all tests:		make
#  to run some tests:		runtest filename...
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]
#
#  to run benchmarks of tgrlink and geddump
#	compile them with icont or jcont
#	run "make bmark"


run:
	./runtest


bmark:	tgrlink geddump
	time ./tgrlink tgrlink.bmk >/dev/null
	JXOPTS='-mx50m -ms50m' time ./geddump geddump.bmk >/dev/null


#  "make clean" removes most generated files

clean:
	rm -rf *.out *.err *.sum
	rm -rf jtmp *.java *.class *.zip *.jar *.o *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print`  | xargs rm -f
	grep -l '/.  created by Toba  ./' *.[ch] 2>/dev/null    | xargs rm -f
