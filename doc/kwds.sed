#  sed script to extract list of keywords

/^	*\/\/	\(\&[a-z]*\)/s//\1/p
/^[ 	]*\/\//d
/^[ 	]*iEnv.declareKey/!d
s/[^"]*"//
s/".*//
s/^/\&/
