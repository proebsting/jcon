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


FORCE:

clean:
	rm -f $(OUT) *.t
