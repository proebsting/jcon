#   Top-level Makefile for Jcon
#
#   to build:	make
#
#   to test:	make test
#
#   to install:	make install DEST=path   (puts files in $DEST)
#		(or: just copy the ./bin directory)
#
#   to clean up: make clean


BUILT = jtran jlink rts.zip jcon.txt
INSTALL = jcont $(BUILT)

DEST = /must/specify/DEST/to/install


build:
	cd tran; $(MAKE)
	cd rts; $(MAKE)
	cd doc; $(MAKE)


test:	build
	cd test; $(MAKE)


install: 
	test -d $(DEST)
	cd bin; cp $(INSTALL) $(DEST)

az-install:
	$(MAKE) install DEST=/cs/jcon/`/home/gmt/sh/platform`/bin


clean:
	cd tran; $(MAKE) clean
	cd rts;  $(MAKE) clean
	cd doc;  $(MAKE) clean
	cd test; $(MAKE) clean
	cd bin;  rm -f $(BUILT)
