#  sed script to extract the header of a shell file

1d
/^$/q
/^[^#]/q
s/^#/ /
