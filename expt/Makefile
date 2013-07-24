#  jcon/expt/Makefile -- for cleaning up after experiments
#
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]
#
#  to execute with profiling:	./jxprof filename [args]


.SUFFIXES: .prof .sum

.prof.sum:
	$(MAKE) $(MFLAGS) profsum
	./profsum <$*.prof >$@


default:
	@echo "make what?"


profsum:  profsum.icn
	icont -us profsum



#  "make clean" removes most generated files, but not .prof files

clean:
	rm -rf *.out *.err *.sum
	rm -rf jtmp *.java *.class *.zip *.jar *.jvm *.[cho] *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^THISFILE=`' `find . -type f -perm -100 -print`  | xargs rm -f
