#  jcon/test/Makefile -- for running automated tests
#
#  to run all tests:		make
#  to run some tests:		runtest filename...
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]


TLIMIT = 10  # maximum compilation + execution time for any one test (in sec)

run:
	ulimit -t $(TLIMIT); ./runtest

clean:
	rm -rf tmp? tmp.* *.out *.err *.sum
	rm -rf jtmp *.class *.zip *.jar *.jvm *.[cho] *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^THISFILE=`' `find . -type f -perm -100 -print`  | xargs rm -f
