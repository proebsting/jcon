#  Makefile for Jcon documentation


SECTLIST = Sections

SECTPAGES = intro.htm usage.htm diffs.htm graphics.htm loadfunc.htm \
		perf.htm releases.htm install.htm refs.htm
SECTINPUT = $(SECTPAGES:.htm=.txt)

HEAD1 = head1.txt
HEAD2 = head2.txt
BIGDOC = jcon.htm

INDEX = index.htm

BUILT = $(BIGDOC) $(INDEX) $(SECTPAGES)


all:	$(BUILT)

$(BIGDOC) $(SECTPAGES): docpro $(SECTLIST) $(HEAD1) $(HEAD2) $(SECTINPUT)
	./docpro $(SECTLIST) $(HEAD1) $(HEAD2) >$(BIGDOC)

$(INDEX): index.txt
	cp index.txt index.htm

docpro:	docpro.icn
	icont -us docpro


clean:
	rm -f docpro $(BUILT)
