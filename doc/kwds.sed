#  sed script to extract list of keywords

/env.declareKey/!d
s/[^"]*"//
s/".*//
s/^/\&/
