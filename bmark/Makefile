#  jcon/bmark/Makefile -- for running benchmarks
#
#  to build and run:	make
#  to repeat report:	make report
#  to rerun:		make rerun
#  to clean up:		make clean


ICONT = icont
JCONT = ../bin/jcont

PROGS = concord.icn deal.icn ipxref.icn queens.icn rsg.icn \
	tgrlink.icn geddump.icn
LIBS = options.icn post.icn shuffle.icn
P = $(PROGS:.icn=)

report:	run
	./kreport $P

run:	setup
	./timer concord.ixe <concord.dat		>/dev/null 2>concord.it
	./timer concord.jxe <concord.dat		>/dev/null 2>concord.jt
	./timer deal.ixe -h 1000			>/dev/null 2>deal.it
	./timer deal.jxe -h 1000			>/dev/null 2>deal.jt
	./timer ipxref.ixe <../tran/bytecode.icn	>/dev/null 2>ipxref.it
	./timer ipxref.jxe <../tran/bytecode.icn	>/dev/null 2>ipxref.jt
	./timer queens.ixe -n10				>/dev/null 2>queens.it
	./timer queens.jxe -n10				>/dev/null 2>queens.jt
	./timer rsg.ixe <rsg.dat			>/dev/null 2>rsg.it
	./timer rsg.jxe <rsg.dat			>/dev/null 2>rsg.jt
	./timer tgrlink.ixe tgrlink.dat			>/dev/null 2>tgrlink.it
	./timer tgrlink.jxe tgrlink.dat			>/dev/null 2>tgrlink.jt
	./timer geddump.ixe geddump.dat			>/dev/null 2>geddump.it
	JXOPTS="-mx50m -ms50m $$JXOPTS" \
	   ./timer geddump.jxe geddump.dat		>/dev/null 2>geddump.jt
	touch run

rerun:	
	rm -f run
	$(MAKE) run
	$(MAKE) report

setup:	$(PROGS) $(LIBS) timer
	$(ICONT) -sc $(LIBS)
	for f in $P; do $(ICONT) -s -o $$f.ixe $$f *.u2; done
	$(JCONT) -sc $(LIBS)
	for f in $P; do $(JCONT) -s -o $$f.jxe $$f *.zip; done
	touch ./setup

timer:	timer.c
	cc -o timer timer.c

clean:
	rm -rf setup timer run *.u? *.zip *.ixe *.jxe *.it *.jt
