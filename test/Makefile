#  Test Directory Makefile
#
#  to run all tests:		make
#  to run some tests:		runtest filename...
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]
#  to execute with profiling:	./jxprof filename [args]
#  to summarize profile:	make filename.sum


.SUFFIXES: .prof .sum

.prof.sum:
	$(MAKE) $(MFLAGS) profsum
	./profsum <$*.prof >$@


run:
	./runtest


profsum:  profsum.icn
	icont -us profsum



#  "make clean" removes most generated files, but not .prof files

clean:
	rm -rf jtmp *.java *.class *.out *.err *.zip *.u[12] *.sum
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '/.  created by Toba  ./' *.[ch] 2>/dev/null   | xargs rm -f
