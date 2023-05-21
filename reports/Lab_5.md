#  Parser & AST

## Course: Formal Languages & Finite Automata
## Author: Gutu Dinu

---

## Theory

A parser is a component of a compiler or interpreter that takes the raw source code of a programming language as input and produces a structured representation of that code. Its primary objective is to recognize the syntactic structure of the input and ensure it conforms to the grammar rules of the language.

The parser performs a process called parsing, which involves breaking down the input into a series of tokens and constructing a parse tree or an abstract syntax tree (AST). The parse tree represents the hierarchical structure of the input code, including all the syntactic details, such as parentheses, operators, and keywords. On the other hand, an AST is a simplified version of the parse tree, where non-essential details are removed, and the focus is placed on the logical structure of the program.

An AST is a tree-like data structure that captures the essential elements of a program in a language-agnostic manner. It represents the program's abstract syntax, abstracting away from specific details of syntax like parentheses or semicolons. Instead, it focuses on the relationships between different elements, such as expressions, statements, and declarations, and their hierarchy and dependencies.

ASTs are widely used in various stages of program analysis and manipulation, including type checking, optimization, code generation, and refactoring. They provide a high-level, easily traversable representation of the program's structure, enabling tools and algorithms to reason about the code and perform various transformations and analyses.


## Objectives:

1. Get familiar with parsing, what it is and how it can be programmed.
2. Get familiar with the concept of AST.
3. In addition to what has been done in the 3rd lab work do the following:
   1. Implement the necessary data structures for an AST that could be used for the text I have processed in the 3rd lab work.
   2. Implement a simple parser program that could extract the syntactic information from the input text.

## Implementation description

### Parser class

* The parser class simply takes the tokens outputed by the lexer.
* Starting from the first position it looks through all the tokens to find its syntactic meaning

```
public class Parser {
    private final List<Token> tokens;
    private int pos;
    private final StringBuilder ast = new StringBuilder();

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }
```


### Parse function

* The following functions looks through the token list to find key tokens.
*  Once it finds one it moves to its separate function in which each of the tokens that make that part up are parsed.
```
public void parse() throws Exception {
        while (pos < tokens.size()) {
            Token token = tokens.get(pos);
            if (token.getTokenType().equals("IDENTIFIER")) {
                // Variable declaration
                parseVariableDeclaration();
            } else if (token.getTokenType().equals("PRINT")) {
                // Print statement
                parsePrintStatement();
            } else if (token.getTokenType().equals("WHILE")) {
                // While loop
                parseWhileLoop();
            .....
```
### Parse statements

* Here is an example of a statement function.
* In the while it first parses its expression.
* After that it looks inside the while statement itself to look for other key tokens that are in themselves separate statements.
* To check for such statements it returns to the previous parse function

```
private void parseWhileLoop() throws Exception {
        consumeToken("WHILE");
        consumeToken("LPAREN");
        Token condition = parseExpression();
        consumeToken("RPAREN");
        consumeToken("LBRACE");
        System.out.println("While loop: " + condition.getValue());
        while (!tokens.get(pos).getTokenType().equals("RBRACE")) {
            parse();
        }
        consumeToken("RBRACE");
    }
```
## Results

### Input 1

```
Input: 

x = 0;
while( x != 5) {
print(x);
x = x + 1;
}

Tokenized form: [(IDENTIFIER, x), (ASSIGNMENT, =), (INTEGER, 0), (SEMICOLON, ;), 
(WHILE, while), (LPAREN, (), (IDENTIFIER, x), (NOT_EQUALS, !=), (INTEGER, 5), 
(RPAREN, )), (LBRACE, {), (PRINT, print), (LPAREN, (), (IDENTIFIER, x), (RPAREN, )), 
(SEMICOLON, ;), (IDENTIFIER, x), (ASSIGNMENT, =), (IDENTIFIER, x), (PLUS, +), 
(INTEGER, 1), (SEMICOLON, ;), (RBRACE, }), (null, )]

Variable assignment: x = 0
While loop: x != 5
Print statement: x
Variable assignment: x = x + 1

Abstract Syntax Tree:

Assignment Statement {
   Identifier: x
   Integer: 0
}

While Statement {
   Expression {
      Identifier: x
      Operator: !=
      Integer: 5
   }
   Statement {
      Print Statement {
         Expression {
            Identifier: x 
         }
      }
      Assignment Statement {
         Identifier: x
         Expression {
            Identifier: x
            Operator: +
            Integer: 1
         }
      }
   }
}


```

### Input 2
```
Input:

if ( x == 0) {
print(x);
} else {
print(1);
}

Tokenized form: [(IF, if), (LPAREN, (), (IDENTIFIER, x), (EQUALS, ==), (INTEGER, 0), 
(RPAREN, )), (LBRACE, {), (PRINT, print), (LPAREN, (), (IDENTIFIER, x), (RPAREN, )), 
(SEMICOLON, ;), (RBRACE, }), (ELSE, else), (LBRACE, {), (PRINT, print), (LPAREN, (), 
(INTEGER, 1), (RPAREN, )), (SEMICOLON, ;), (RBRACE, }), (null, )]

If statement: x == 0
Print statement: x
Else statement:
Print statement: 1

Abstract Syntax Tree:

If Statement {
   Expression {
      Identifier: x
      Operator: ==
      Integer: 0
   }
   Statement {
      Print Statement {
         Expression {
            Identifier: x 
         }
      }
   }
}

Else Statement {
   Statement {
      Print Statement {
         Expression {
            Integer: 1 
         }
      }
   }
}


```

### Input 3

```
Input: 

y = 5;
x = y + 2;
print(x);

Tokenized form: [(IDENTIFIER, y), (ASSIGNMENT, =), (INTEGER, 5), (SEMICOLON, ;), 
(IDENTIFIER, x), (ASSIGNMENT, =), (IDENTIFIER, y), (PLUS, +), (INTEGER, 2), 
(SEMICOLON, ;), (PRINT, print), (LPAREN, (), (IDENTIFIER, x), 
(RPAREN, )), (SEMICOLON, ;), (null, )]

Variable assignment: y = 5
Variable assignment: x = y + 2
Print statement: x

Abstract Syntax Tree:

Assignment Statement {
   Identifier: y
   Integer: 5
		
Assignment Statement {
    Identifier: x
    Expression {
       Identifier: y
       Operator: +
       Integer: 2
   }
}
				
Print Statement {
   Expression {
   Identifier: x 
   }
}


```

## Conclusion

Programming a parser and abstract syntax tree (AST) is a 
fundamental skill in the realm of programming language 
development and analysis. These components enable 
us to understand, manipulate, and analyze the 
structure of programming languages.

By writing a parser, I could more effectively process and 
validate the syntax of the source code. The parser breaks down 
the code into tokens and constructs a parse tree or an AST, 
representing the hierarchical structure of the program.

The AST served as a simplified, language-agnostic representation of the program's 
abstract syntax. It focused on the logical relationships between different elements 
of the code, allowing for high-level analysis and manipulation. ASTs are invaluable in various 
stages of program analysis, such as type checking, optimization, code generation, and refactoring.

Through these assignments, I have developed my programming skills and my understanding of these 
fundamental ideas while gaining practical experience dealing with parsing and ASTs. 
In addition to achieving my initial goals, I have also expanded my skill set for 
processing and evaluating textual data.

Last but not least it has given me insightful knowledge of ASTs, parsing, and their practical uses. 
I now possess the knowledge and abilities necessary to handle increasingly challenging parsing 
tasks and investigate potential future developments in this area. 
I can without a doubt say that working on this project has improved my development as a student.

Ultimately, the ability to program a parser and AST empowers developers and 
language designers to create powerful tools, such as compilers, interpreters, 
static analyzers, and IDE features. These tools enhance the productivity, 
correctness, and maintainability of software projects, making the 
understanding and manipulation of programming languages more efficient and effective.