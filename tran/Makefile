ICONT = icont -s -u

JTRAN = ../bin/jtran
JLINK = ../bin/jlink


BASESRC = \
	globals.icn \
	dump.icn

LEXSRC = \
	do_ops.icn \
	preprocessor.icn \
	lexer.icn

PARSESRC = \
	ast.icn \
	key.icn \
	add_defaults.icn \
	parse.icn

IRGENSRC = \
	ir.icn \
	irgen.icn

CODEGENSRC = \
	bc_gen.icn \
	dump_gen.icn \
	dot_gen.icn \
	java_gen.icn \
	null_gen.icn \
	ucode_gen.icn \
	cg.icn \
	optimize.icn \
	bytecode.icn 

TRANSRC = $(BASESRC) $(LEXSRC) $(PARSESRC) $(IRGENSRC) $(CODEGENSRC)


all:	$(JTRAN) $(JLINK)

$(JLINK):  linker.icn bytecode.icn
	$(ICONT) -o $(JLINK) linker.icn bytecode.icn

gen:	gen.icn
	$(ICONT) gen.icn

$(JTRAN):  $(TRANSRC) jtran_main.icn
	$(ICONT) -o $(JTRAN) $(TRANSRC) jtran_main.icn 

parse:	$(BASESRC) $(LEXSRC) $(PARSESRC) parse_main.icn
	$(ICONT) -o parse $(BASESRC) $(LEXSRC) $(PARSESRC) parse_main.icn 

lexer:	$(BASESRC) $(LEXSRC) lexer_main.icn 
	$(ICONT) -o lexer $(BASESRC) $(LEXSRC) lexer_main.icn 

oplexgen: init.icn oplexgen.icn
	$(ICONT) -o oplexgen oplexgen.icn init.icn

do_ops.icn: oplexgen
	./oplexgen tree > do_ops.icn

tst:	$(JTRAN)
	$(JTRAN) -target=null test/all.icn

clean:
	rm -f do_ops.icn lexer parse oplexgen gen *.u[12]
