#  jcon/rts/Makefile -- for building the Jcon runtime system
#
#  Creates ../bin/rts.zip
#
#  The "upclass" script is used to accomplish partial rebuilds.
#  It is dependable only when small, local changes are made.
#  When a change affects other classes, run "make clean" before rebuilding.


build: iTrampoline.java compile

iTrampoline.java: trampolinegen
	./trampolinegen > iTrampoline.java

trampolinegen: ../tran/trampolinegen.icn
	icont -o trampolinegen ../tran/trampolinegen.icn

compile:
	./upclass *.java
	cd ..; jar cfM bin/rts.zip rts/*.class 2>&1 | sed '/<GC: /d'


clean:
	rm -f *.class *.[cho] trampolinegen
