#   Top-level Makefile for Jcon
#
#   to build:	make
#
#   to test:	make test
#
#   to install:	make install DEST=path
#		(puts files in $DEST/bin and $DEST/lib)


MAKE=make
SHELL=/bin/sh

DEST=/cs/jive/`rts/arch.sh`


build:
	cd src; $(MAKE)
	cd rts; $(MAKE)
	cd doc; $(MAKE)

test:	build
	cd test; $(MAKE)

install: build
	test -d $(DEST)     || mkdir $(DEST)
	test -d $(DEST)/bin || mkdir $(DEST)/bin
	test -d $(DEST)/lib || mkdir $(DEST)/lib
	test -d $(DEST)/doc || mkdir $(DEST)/doc
	cp rts/rts.zip $(DEST)/lib
	cp src/jcon    $(DEST)/lib
	cp test/jcont $(DEST)/bin
	cp doc/jcon.txt $(DEST)/doc

clean:
	cd src; $(MAKE) clean
	cd rts; $(MAKE) clean
	cd doc; $(MAKE) clean
	cd test; $(MAKE) clean
