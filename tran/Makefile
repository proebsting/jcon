ICONT = icont -s -u

JTRAN = ../bin/jtran


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
	cg.icn \
	optimize.icn \
	null_gen.icn \
	dump_gen.icn \
	dot_gen.icn \
	bc_gen.icn \
	bytecode.icn \
	java_gen.icn

TRANSRC = $(BASESRC) $(LEXSRC) $(PARSESRC) $(IRGENSRC) $(CODEGENSRC)


all:	$(JTRAN)

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
