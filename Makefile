MAKE=make
SHELL=/bin/sh


build:
	cd src; $(MAKE)
	cd rts; $(MAKE)

test:	build
	cd test; $(MAKE)

clean:
	cd src; $(MAKE) clean
	cd rts; $(MAKE) clean
	cd test; $(MAKE) clean

