run:
	./runtest

clean:
	rm -rf jtmp *.java *.class *.out *.zip
	grep -l '$${ICONX-/' `find . -type f -perm -100 -print` | xargs rm -f
	grep -l '/.  created by Toba  ./' *.[ch] 2>/dev/null   | xargs rm -f
