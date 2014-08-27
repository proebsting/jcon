#   Top-level Makefile for Jcon
#
#
#   to build:		make
#
#   to test:		make test
#
#   to install:		make install DEST=path   (puts files in $DEST)
#			(or: just copy the ./bin directory)
#
#   to clean up:	make clean
#
#  
#   to build using Jcon (optional), after completing initial build:
#			make jj

DEST = /must/specify/DEST/to/install


build:
	cd html; $(MAKE)
	cd tran; $(MAKE)
	cd jcon; $(MAKE)

jj:
	cd tran; $(MAKE) jj


test:	build
	cd test; $(MAKE)


install: 
	test -d $(DEST) || mkdir $(DEST)
	cp bin/j* bin/jcon.zip $(DEST)


clean:
	rm -f */.#*
	cd html;  $(MAKE) clean
	cd tran;  $(MAKE) clean
	cd jcon;  $(MAKE) clean
	cd test;  $(MAKE) clean
	cd gtest; $(MAKE) clean
	cd expt;  $(MAKE) clean
	cd demo;  $(MAKE) clean
	cd bmark; $(MAKE) clean
	cd bin;   $(MAKE) clean
	rm -rf jvmtest


# clean up for distribution by removing locally-specific files

dclean:	
	rm -f `find * -type f | xargs grep -l '<< *ARIZONA-ONLY *>>' | cat`
	rm -rf `find * -type d -name CVS -o -type -d -name .git`` 
