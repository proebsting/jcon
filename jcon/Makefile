TOBA=t11


build:
	./upclass *.java
	cd ..; jar cfM bin/rts.zip rts/*.class 2>&1 | sed '/<GC: /d'


# build package for use with Toba
# warning: this takes a long time
package:  build
	CLASSPATH=..:$$CLASSPATH $(TOBA) -O -P JCON *.class
	rm -rf ../bin/JCON
	mv JCON ../bin
	

clean:
	rm -f *.class
