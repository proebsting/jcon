#  sed script to extract list of functions
#
#  <<ARIZONA-ONLY>> -- not currently being used

/^[ 	]*\/\//d
/^[ 	]*declare("/!d
s/[^"]*"//
s/".*//
