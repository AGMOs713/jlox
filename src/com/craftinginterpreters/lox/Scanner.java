package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN);   break;
            case ')': addToken(RIGHT_PAREN);  break;
            case '{': addToken(LEFT_BRACE);   break;
            case '}': addToken(RIGHT_BRACE);  break;
            case ',': addToken(COMMA);        break;
            case '.': addToken(DOT);          break;
            case '-': addToken(MINUS);        break;
            case '+': addToken(PLUS);         break;
            case ';': addToken(SEMICOLON);    break;
            case '*': addToken(STAR);         break;

            case '0': number(); break;
            case '1': number(); break;
            case '2': number(); break;
            case '3': number(); break;
            case '4': number(); break;
            case '5': number(); break;
            case '6': number(); break;
            case '7': number(); break;
            case '8': number(); break;
            case '9': number(); break;
            
            case '_': identifier(); break;
            case 'A': identifier(); break;
            case 'B': identifier(); break;
            case 'C': identifier(); break;
            case 'D': identifier(); break;
            case 'E': identifier(); break;
            case 'F': identifier(); break;
            case 'G': identifier(); break;
            case 'H': identifier(); break;
            case 'I': identifier(); break;
            case 'J': identifier(); break;
            case 'K': identifier(); break;
            case 'L': identifier(); break;
            case 'M': identifier(); break;
            case 'N': identifier(); break;
            case 'O': identifier(); break;
            case 'P': identifier(); break;
            case 'Q': identifier(); break;
            case 'R': identifier(); break;
            case 'S': identifier(); break;
            case 'T': identifier(); break;
            case 'U': identifier(); break;
            case 'V': identifier(); break;
            case 'W': identifier(); break;
            case 'X': identifier(); break;
            case 'Y': identifier(); break;
            case 'Z': identifier(); break;
            case 'a': identifier(); break;
            case 'b': identifier(); break;
            case 'c': identifier(); break;
            case 'd': identifier(); break;
            case 'e': identifier(); break;
            case 'f': identifier(); break;
            case 'g': identifier(); break;
            case 'h': identifier(); break;
            case 'i': identifier(); break;
            case 'j': identifier(); break;
            case 'k': identifier(); break;
            case 'l': identifier(); break;
            case 'm': identifier(); break;
            case 'n': identifier(); break;
            case 'o': identifier(); break;
            case 'p': identifier(); break;
            case 'q': identifier(); break;
            case 'r': identifier(); break;
            case 's': identifier(); break;
            case 't': identifier(); break;
            case 'u': identifier(); break;
            case 'v': identifier(); break;
            case 'w': identifier(); break;
            case 'x': identifier(); break;
            case 'y': identifier(); break;
            case 'z': identifier(); break;

            case '!': 
                addToken(match('=') ? BANG_EQUAL : BANG); 
                break;
            case '=': 
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }

            case '\n': line++;
            case ' ':
            case '\r': break;
            case '\t':
                Lox.error(line,
                    "Tab ('\\t') detected, please don't use them. Ever."
                );
                break;

            case '"': string(); break;

            default:
                Lox.error(line, "Unexpected character.");
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean match(char expected) {
        if (
            isAtEnd() || 
            (source.charAt(current) != expected)
        ) return false;
        
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return 
            (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
