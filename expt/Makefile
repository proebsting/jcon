#  Makefile for experimentation directory
#
#  to clean up:			make clean
#
#  to compile a file:		./jcont filename
#  to execute:			./filename [args]

#  NOT TESTED RECENTLY:
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
	rm -rf jtmp *.java *.class *.zip *.jcz *.o *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print`  | xargs rm -f
	grep -l '/.  created by Toba  ./' *.[ch] 2>/dev/null    | xargs rm -f
