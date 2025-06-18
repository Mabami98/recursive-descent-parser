# Recursive Descent Parser in Java

This project implements a recursive descent parser and lexer for a simple toy programming language. The parser reads and processes source code, tokenizes it using a custom lexer, builds a parse tree, and reports syntax errors where appropriate.

The codebase was developed as part of university coursework in compiler construction and language processing.

---

## Features

- Custom lexer that converts source code into token streams
- Recursive descent parser for a simple programming language grammar
- Parse tree construction
- Custom syntax error detection and reporting
- Support for basic language constructs: variable declarations, conditionals, print statements

---

## File Overview

- `Main.java` – Entry point for parsing source code files
- `Lexer.java` – Tokenizes input into lexical units
- `Parser.java` – Recursive descent implementation of grammar
- `Token.java` – Token structure and types
- `ParseTree.java` – Representation of the parse tree
- `SyntaxError.java` – Custom exception for reporting parsing errors
- `Leona.java` – Defines grammar rules for the toy language

---
