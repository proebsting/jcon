#  Makefile for Jcon documentation


SECTLIST = Sections

SECTPAGES = intro.htm usage.htm diffs.htm graphics.htm dynload.htm \
		perf.htm relnotes.htm install.htm refs.htm

HEAD1 = head1.txt
HEAD2 = head2.txt
SECTINPUT = $(HEAD1) $(HEAD2) $(SECTPAGES:.htm=.txt)

BIGDOC = jcon.htm
BIGTMP = jcon.tmp

INDEX = index.htm

BUILT = $(BIGDOC) $(INDEX) $(SECTPAGES)


all:	$(BUILT)

$(BIGDOC) $(SECTPAGES): docpro $(SECTLIST) $(SECTINPUT) fixlinks.sed
	./docpro $(SECTLIST) $(HEAD1) $(HEAD2) >$(BIGTMP)
	sed -f fixlinks.sed <$(BIGTMP) > $(BIGDOC)

$(INDEX): index.txt
	cp index.txt index.htm

docpro:	docpro.icn
	icont -us docpro


lint:	$(BUILT)
	# we don't really use netscape extensions, only stylesheets
	weblint -x netscape *.htm

clean:
	rm -f docpro $(BUILT) $(BIGTMP)
