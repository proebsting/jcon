#  Makefile for jcon/tran


ICONT = icont -u -s
JCONT = ../bin/jcont -fa -s

.SUFFIXES: .icn .u2 .zip
.icn.u2:	; $(ICONT) -c $<
.icn.zip:	; $(JCONT) -c $<


BASESRC = \
	dump.icn

LEXSRC = \
	do_ops.icn \
	preprocessor.icn \
	lexer.icn

PARSESRC = \
	ast.icn \
	key.icn \
	parse.icn

IRGENSRC = \
	ir.icn \
	irgen.icn

CODEGENSRC = \
	gen_bc.icn \
	gen_dump.icn \
	gen_dot.icn \
	gen_ucode.icn \
	optimize.icn \
	interface.icn \
	bytecode.icn 

TRANSRC = $(BASESRC) \
	$(LEXSRC)  \
	$(PARSESRC) \
	$(IRGENSRC) \
	$(CODEGENSRC) \
	jtran_main.icn

LINKSRC = \
	linker.icn \
	bytecode.icn

TRANOBJ = $(TRANSRC:.icn=.u2)
LINKOBJ = $(LINKSRC:.icn=.u2)

TRANZIP = $(TRANSRC:.icn=.zip)
LINKZIP = $(LINKSRC:.icn=.zip)


# usual versions built by icont

all:	jtran jlink

jtran:	$(TRANOBJ)
	$(ICONT) -o jtran $(TRANOBJ)
	cp jtran ../bin

jlink:	$(LINKOBJ)
	$(ICONT) -o jlink $(LINKOBJ)
	cp jlink ../bin


# versions built by jcon (not built by default)

jj:	jjtran jjlink

jjtran:	$(TRANZIP)
	$(JCONT) -o jjtran $(TRANZIP)
	cp jjtran ../bin

jjlink:	$(LINKZIP)
	$(JCONT) -o jjlink $(LINKZIP)
	cp jjlink ../bin


oplexgen: oplexgen.icn
	$(ICONT) -o oplexgen oplexgen.icn

interfacegen: interfacegen.icn
	$(ICONT) -o interfacegen interfacegen.icn

interface.icn: interfacegen
	./interfacegen > interface.icn

do_ops.icn: oplexgen
	./oplexgen > do_ops.icn


clean:
	rm -f *.u[12] *.zip do_ops.icn interface.icn 
	rm -f jtran jjtran jlink jjlink interfacegen oplexgen
