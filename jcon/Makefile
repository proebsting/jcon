#  jcon/rts/Makefile -- for building the Jcon runtime system
#
#  Creates ../bin/rts.zip
#
#  Uses a script because it's hard to handle this correctly in the Makefile


build:  iTrampoline.java
	./rebuild

iTrampoline.java: trampgen.icn
	icont -us trampgen -x >iTrampoline.java


clean:
	rm -f *.class trampgen iTrampoline.java
