#  Test Directory Makefile
#
#  to run all tests:		make
#  to run some tests:		runtest filename...
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]


run:
	./runtest


#  "make clean" removes most generated files

clean:
	rm -rf *.out *.err *.sum
	rm -rf jtmp *.java *.class *.zip *.jcz *.o *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print`  | xargs rm -f
	grep -l '/.  created by Toba  ./' *.[ch] 2>/dev/null    | xargs rm -f
