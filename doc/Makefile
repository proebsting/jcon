#  Makefile for rough jcon documentation

OUT = jcon.txt

DASHES = -------------------------------------

$(OUT):	sep.t hdr.t jcont.t kwds.t funcs.t notes.txt
	cat hdr.t sep.t jcont.t sep.t kwds.t sep.t \
	    funcs.t sep.t notes.txt >$(OUT)
sep.t:
	echo ""						> sep.t
	echo $(DASHES)$(DASHES)				>>sep.t
	echo ""						>>sep.t

hdr.t:	FORCE
	echo "Jcon Usage and Status Notes"		> hdr.t
	date						>>hdr.t

jcont.t:  ../test/jcont shdoc.sed
	sed -f shdoc.sed ../test/jcont			> jcont.t

kwds.t:	  ../rts/iKeywords.java kwds.sed
	echo "Keywords implemented:"			> kwds.t
	echo ""						>>kwds.t
	sed -f kwds.sed ../rts/iKeywords.java | sort | \
	    pr -4 -t -w 60 -l 12 | sed 's/^/	/'	>>kwds.t

funcs.t:  ../rts/iBuiltins.java funcs.sed
	echo "Functions implemented:"			> funcs.t
	echo ""						>>funcs.t
	sed -f funcs.sed ../rts/iBuiltins.java | sort | \
	    pr -5 -t -w 60 -l 19 | sed 's/^/	/'	>>funcs.t

FORCE:

clean:
	rm -f $(OUT) *.t
