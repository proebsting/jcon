ICONT = icont -s -u

JTRAN = ../bin/jtran
JLINK = ../bin/jlink


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

all:	$(JTRAN) $(JLINK)

$(JLINK):  linker.icn bytecode.icn
	$(ICONT) -o $(JLINK) linker.icn bytecode.icn

$(JTRAN):  $(TRANSRC)
	$(ICONT) -o $(JTRAN) $(TRANSRC)

oplexgen: oplexgen.icn
	$(ICONT) -o oplexgen oplexgen.icn

interfacegen: interfacegen.icn
	$(ICONT) -o interfacegen interfacegen.icn

interface.icn: interfacegen
	./interfacegen > interface.icn

do_ops.icn: oplexgen
	./oplexgen > do_ops.icn

clean:
	rm -f do_ops.icn interface.icn interfacegen oplexgen *.u[12]
