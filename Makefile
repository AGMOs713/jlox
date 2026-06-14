JAR = jlox.jar
MAIN = com.craftinginterpreters.lox.Lox
BUILDDIR = bin
SRCDIR = src

SOURCELOX  = src/com/craftinginterpreters/lox/
SOURCETOOL = src/com/craftinginterpreters/tool/

all: $(JAR) expr

$(JAR):
	mkdir -p $(BUILDDIR)
	javac -d $(BUILDDIR) $(SOURCELOX)*.java
	jar cfe $(JAR) $(MAIN) -C $(BUILDDIR) .

expr: 
	touch $(SOURCELOX)Expr.java
	javac $(SOURCETOOL)GenerateAst.java 
	cd src && \
	java com.craftinginterpreters.tool.GenerateAst ./com/craftinginterpreters/lox && \
	rm ./com/craftinginterpreters/tool/GenerateAst.class

clean:
	rm -rf \
		$(BUILDDIR) \
		$(JAR) \
		$(SOURCELOX)*.class 

run: $(JAR)
	java -jar $(JAR)

.PHONY: all clean run expr
