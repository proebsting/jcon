#  sed script to extract list of keywords

/^[ 	]*\/\//d
/^[ 	]*iEnv.declareKey/!d
s/[^"]*"//
s/".*//
s/^/\&/
