#  Makefile for rough jcon documentation

OUT = ../bin/jcon.txt

DASHES = -------------------------------------

$(OUT):	sep.t hdr.t jcont.t notes.txt
	cat hdr.t sep.t jcont.t \
	    sep.t notes.txt >$(OUT)
sep.t:
	echo ""						> sep.t
	echo $(DASHES)$(DASHES)				>>sep.t
	echo ""						>>sep.t

hdr.t:	FORCE
	echo "Jcon Usage and Status Notes"		> hdr.t
	date						>>hdr.t

jcont.t:  ../bin/jcont shdoc.sed
	sed -f shdoc.sed ../bin/jcont			> jcont.t

kwds.t:	  ../rts/iKeywords.java kwds.sed
	echo "Keywords implemented:"			> kwds.t
	echo ""						>>kwds.t
	sed -f kwds.sed ../rts/iKeywords.java | sort | \
	    pr -4 -t -w 60 -l 12 | sed 's/^/	/'	>>kwds.t

FORCE:

clean:
	rm -f $(OUT) *.t
