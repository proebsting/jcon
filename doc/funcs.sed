#  sed script to extract list of functions

/^[ 	]*\/\//d
/^[ 	]*declare("/!d
s/[^"]*"//
s/".*//
