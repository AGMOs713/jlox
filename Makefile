JAR = jlox.jar
MAIN = com.craftinginterpreters.lox.Lox
BUILDDIR = bin
SRCDIR = src

SOURCES = src/com/craftinginterpreters/lox/*.java

all: $(JAR)

$(JAR):
	mkdir -p $(BUILDDIR)
	javac -d $(BUILDDIR) $(SOURCES)
	jar cfe $(JAR) $(MAIN) -C $(BUILDDIR) .

clean:
	rm -rf $(BUILDDIR) $(JAR)

run: $(JAR)
	java -jar $(JAR)

.PHONY: all clean run
