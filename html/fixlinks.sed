#  sed directives to turn file links into internal links
#  (for mutating separate pages into one big web page)
#
#  changes HREF=file.htm into HREF="#file"

s/ [Hh][Rr][Ee][Ff]="*\([a-z]*\)\.htm"*/ HREF="#\1"/g
