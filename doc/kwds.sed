#  sed script to extract list of keywords
#
#  <<ARIZONA-ONLY>> -- not currently being used

/^	*\/\/	\(\&[a-z]*\)/s//\1/p
/^[ 	]*\/\//d
/^[ 	]*iEnv.declareKey/!d
s/[^"]*"//
s/".*//
s/^/\&/
