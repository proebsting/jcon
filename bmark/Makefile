#  Makefile for running benchmarks

ICONT = icont
JCONT = ../bin/jcont

PROGS = concord.icn deal.icn ipxref.icn queens.icn rsg.icn
LIBS = options.icn post.icn shuffle.icn
P = $(PROGS:.icn=)


run:		setup
		@echo ''
		@echo '          icont  jcont    ratio'
		@./measure concord concord.dat
		@./measure deal /dev/null -h 1000
		@./measure ipxref ../tran/bytecode.icn
		@./measure queens /dev/null -n10
		@./measure rsg rsg.dat
		@echo ''


setup:		$(PROGS) $(LIBS)
		$(ICONT) -sc $(LIBS)
		for f in $P; do $(ICONT) -s -o $$f.ixe $$f *.u2; done
		$(JCONT) -sc $(LIBS)
		for f in $P; do $(JCONT) -s -o $$f.jxe $$f *.zip; done
		touch ./setup

clean:
		rm -rf setup *.u? *.zip *.ixe *.jxe
