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
	./ktime concord.ixe <concord.dat		>/dev/null 2>concord.it
	./ktime concord.jxe <concord.dat		>/dev/null 2>concord.jt
	./ktime deal.ixe -h 1000			>/dev/null 2>deal.it
	./ktime deal.jxe -h 1000			>/dev/null 2>deal.jt
	./ktime ipxref.ixe <../tran/bytecode.icn	>/dev/null 2>ipxref.it
	./ktime ipxref.jxe <../tran/bytecode.icn	>/dev/null 2>ipxref.jt
	./ktime queens.ixe -n10				>/dev/null 2>queens.it
	./ktime queens.jxe -n10				>/dev/null 2>queens.jt
	./ktime rsg.ixe <rsg.dat			>/dev/null 2>rsg.it
	./ktime rsg.jxe <rsg.dat			>/dev/null 2>rsg.jt
	./ktime tgrlink.ixe tgrlink.dat			>/dev/null 2>tgrlink.it
	./ktime tgrlink.jxe tgrlink.dat			>/dev/null 2>tgrlink.jt
	./ktime geddump.ixe geddump.dat			>/dev/null 2>geddump.it
	JXOPTS="-mx50m -ms50m $$JXOPTS" \
	   ./ktime geddump.jxe geddump.dat		>/dev/null 2>geddump.jt
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
