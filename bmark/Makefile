#  Makefile for running benchmarks

ICONT = icont
JCONT = ../bin/jcont

PROGS = concord.icn deal.icn ipxref.icn queens.icn rsg.icn
LIBS = options.icn post.icn shuffle.icn
P = $(PROGS:.icn=)

report:	run
	ksh report.sh $P

run:	setup
	ksh -c 'time concord.ixe <concord.dat'		>/dev/null 2>concord.it
	ksh -c 'time concord.jxe <concord.dat'		>/dev/null 2>concord.jt
	ksh -c 'time deal.ixe -h 1000'			>/dev/null 2>deal.it
	ksh -c 'time deal.jxe -h 1000'			>/dev/null 2>deal.jt
	ksh -c 'time ipxref.ixe <../tran/bytecode.icn'	>/dev/null 2>ipxref.it
	ksh -c 'time ipxref.jxe <../tran/bytecode.icn'	>/dev/null 2>ipxref.jt
	ksh -c 'time queens.ixe -n10'			>/dev/null 2>queens.it
	ksh -c 'time queens.jxe -n10'			>/dev/null 2>queens.jt
	ksh -c 'time rsg.ixe <rsg.dat'			>/dev/null 2>rsg.it
	ksh -c 'time rsg.jxe <rsg.dat'			>/dev/null 2>rsg.jt
	touch run

rerun:	
	rm -f run
	$(MAKE) run

setup:	$(PROGS) $(LIBS)
	$(ICONT) -sc $(LIBS)
	for f in $P; do $(ICONT) -s -o $$f.ixe $$f *.u2; done
	$(JCONT) -sc $(LIBS)
	for f in $P; do $(JCONT) -s -o $$f.jxe $$f *.zip; done
	touch ./setup

clean:
	rm -rf setup run *.u? *.zip *.ixe *.jxe *.it *.jt
