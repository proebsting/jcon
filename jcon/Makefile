build:
	./upclass *.java
	cd ..; jar cfM bin/rts.zip rts/*.class 2>&1 | sed '/<GC: /d'

clean:
	rm -f *.class
