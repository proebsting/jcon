#  jcon/test/Makefile -- for running automated tests
#
#  to run all tests:		make
#  to run some tests:		runtest filename...
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]
#
#  to run benchmarks of tgrlink and geddump
#				compile them with icont or jcont
#				make bmark


run:
	./runtest


bmark:	tgrlink geddump
	JXOPTS='-noverify' time ./tgrlink tgrlink.bmk >/dev/null
	JXOPTS='-noverify -mx50m -ms50m' time ./geddump geddump.bmk >/dev/null


#  "make clean" removes most generated files

clean:
	rm -rf tmp.* *.out *.err *.sum
	rm -rf jtmp *.java *.class *.zip *.jar *.jvm *.[cho] *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print`  | xargs rm -f
