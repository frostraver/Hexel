package Hexel.util;

import java.awt.event.KeyEvent;

public class CharToKeyEvent {

    public static int convert(char character) {
        switch (character) {
            case 'a': return KeyEvent.VK_A;
            case 'b': return KeyEvent.VK_B;
            case 'c': return KeyEvent.VK_C;
            case 'd': return KeyEvent.VK_D;
            case 'e': return KeyEvent.VK_E;
            case 'f': return KeyEvent.VK_F;
            case 'g': return KeyEvent.VK_G;
            case 'h': return KeyEvent.VK_H;
            case 'i': return KeyEvent.VK_I;
            case 'j': return KeyEvent.VK_J;
            case 'k': return KeyEvent.VK_K;
            case 'l': return KeyEvent.VK_L;
            case 'm': return KeyEvent.VK_M;
            case 'n': return KeyEvent.VK_N;
            case 'o': return KeyEvent.VK_O;
            case 'p': return KeyEvent.VK_P;
            case 'q': return KeyEvent.VK_Q;
            case 'r': return KeyEvent.VK_R;
            case 's': return KeyEvent.VK_S;
            case 't': return KeyEvent.VK_T;
            case 'u': return KeyEvent.VK_U;
            case 'v': return KeyEvent.VK_V;
            case 'w': return KeyEvent.VK_W;
            case 'x': return KeyEvent.VK_X;
            case 'y': return KeyEvent.VK_Y;
            case 'z': return KeyEvent.VK_Z;
            case 'A': return KeyEvent.VK_A;
            case 'B': return KeyEvent.VK_B;
            case 'C': return KeyEvent.VK_C;
            case 'D': return KeyEvent.VK_D;
            case 'E': return KeyEvent.VK_E;
            case 'F': return KeyEvent.VK_F;
            case 'G': return KeyEvent.VK_G;
            case 'H': return KeyEvent.VK_H;
            case 'I': return KeyEvent.VK_I;
            case 'J': return KeyEvent.VK_J;
            case 'K': return KeyEvent.VK_K;
            case 'L': return KeyEvent.VK_L;
            case 'M': return KeyEvent.VK_M;
            case 'N': return KeyEvent.VK_N;
            case 'O': return KeyEvent.VK_O;
            case 'P': return KeyEvent.VK_P;
            case 'Q': return KeyEvent.VK_Q;
            case 'R': return KeyEvent.VK_R;
            case 'S': return KeyEvent.VK_S;
            case 'T': return KeyEvent.VK_T;
            case 'U': return KeyEvent.VK_U;
            case 'V': return KeyEvent.VK_V;
            case 'W': return KeyEvent.VK_W;
            case 'X': return KeyEvent.VK_X;
            case 'Y': return KeyEvent.VK_Y;
            case 'Z': return KeyEvent.VK_Z;
            case '`': return KeyEvent.VK_BACK_QUOTE;
            case '0': return KeyEvent.VK_0;
            case '1': return KeyEvent.VK_1;
            case '2': return KeyEvent.VK_2;
            case '3': return KeyEvent.VK_3;
            case '4': return KeyEvent.VK_4;
            case '5': return KeyEvent.VK_5;
            case '6': return KeyEvent.VK_6;
            case '7': return KeyEvent.VK_7;
            case '8': return KeyEvent.VK_8;
            case '9': return KeyEvent.VK_9;
            case '-': return KeyEvent.VK_MINUS;
            case '=': return KeyEvent.VK_EQUALS;
            case '!': return KeyEvent.VK_EXCLAMATION_MARK;
            case '@': return KeyEvent.VK_AT;
            case '#': return KeyEvent.VK_NUMBER_SIGN;
            case '$': return KeyEvent.VK_DOLLAR;
            case '^': return KeyEvent.VK_CIRCUMFLEX;
            case '&': return KeyEvent.VK_AMPERSAND;
            case '*': return KeyEvent.VK_ASTERISK;
            case '(': return KeyEvent.VK_LEFT_PARENTHESIS;
            case ')': return KeyEvent.VK_RIGHT_PARENTHESIS;
            case '_': return KeyEvent.VK_UNDERSCORE;
            case '+': return KeyEvent.VK_PLUS;
            case '\t':return KeyEvent.VK_TAB;
            case '\n':return KeyEvent.VK_ENTER;
            case '[': return KeyEvent.VK_OPEN_BRACKET;
            case ']': return KeyEvent.VK_CLOSE_BRACKET;
            case '\\':return KeyEvent.VK_BACK_SLASH;
            case ';': return KeyEvent.VK_SEMICOLON;
            case ':': return KeyEvent.VK_COLON;
            case '\'':return KeyEvent.VK_QUOTE;
            case '"': return KeyEvent.VK_QUOTEDBL;
            case ',': return KeyEvent.VK_COMMA;
            case '<': return KeyEvent.VK_LESS;
            case '.': return KeyEvent.VK_PERIOD;
            case '>': return KeyEvent.VK_GREATER;
            case '/': return KeyEvent.VK_SLASH;
            case ' ': return KeyEvent.VK_SPACE;
            default:
                throw new IllegalArgumentException("Cannot convert character " + character);
        }
    }
}

