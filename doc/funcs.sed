#  sed script to extract list of functions

/declare(env,/!d
s/[^"]*"//
s/".*//
