#   Top-level Makefile for Jcon
#
#   to build:	make
#
#   to test:	make test
#
#   to install:	make install DEST=path
#		(puts files in $DEST)


MAKE = make
SHELL = /bin/sh

BUILT = jtran rts.zip jcon.txt
INSTALL = jcont $(BUILT)
DEST = /cs/jcon/`/home/gmt/sh/arch`/bin


build:
	cd tran; $(MAKE)
	cd rts; $(MAKE)
	cd doc; $(MAKE)

test:	build
	cd test; $(MAKE)

install: build
	cd bin; cp $(INSTALL) $(DEST)

clean:
	cd tran; $(MAKE) clean
	cd rts;  $(MAKE) clean
	cd doc;  $(MAKE) clean
	cd test; $(MAKE) clean
	cd bin;  rm -f $(BUILT)
