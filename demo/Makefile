#  Makefile for graphics tests

all:
	set -x; for i in *.icn; do LPATH=../bin ../bin/jcont -us $$i; done


clean:
	rm -rf *.out *.err *.sum
	rm -rf jtmp *.java *.class *.zip *.jar *.jvm *.[cho] *.tx *.u[12]
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '^WHENCE=`' `find . -type f -perm -100 -print`  | xargs rm -f
