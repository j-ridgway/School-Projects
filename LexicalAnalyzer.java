/* Justin Ridgway CMPSCI 4250 Project 1.
This program is converted from c language from chapter 4.2 of Concepts of Programming Languages. I have altered
certain parts of the original program, as they seemed to break the logic of the program. Instead of an array for
lexeme, I used an ArrayList. All of the return statements for the non-main functions have been eliminated as they
had no use in the original program. The program reads an expression from a file, then parses the expression for
each token and lexeme, then displays them to the console.
*/
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Character.*;

public class LexicalAnalyzer {
    // Global declarations
    // Variables
    static charClasses  charClass;
    static ArrayList<Character> lexeme = new ArrayList<>();
    static char nextChar;
    static int lexLen = 0;
    static tokenCodes nextToken;
    static int i = 0;
    static String line;





    // Token codes
    public enum tokenCodes{
        INT_LIT(10), IDENT(11), ASSIGN_OP(20), ADD_OP(21), SUB_OP(22), MULT_OP(23),
        DIV_OP(24), LEFT_PAREN(25), RIGHT_PAREN(26), EOF(-1);

        // Values to represent token codes
        private final int value;

        // Constructor to give enumerated types integer values
        tokenCodes(final int newValue) {
            value = newValue;
        }

        // Getter function to get integer type.
        public int getValue(){
            return value;
        }
    }

    // Character classes
    public enum charClasses{
        LETTER, DIGIT, UNKNOWN, EOF
    }



    public static void main(String[] args) throws FileNotFoundException{

        // Opens file for reading.
        FileReader fileIn = new FileReader("fileIn.txt");

        // Transfers file object to scanner object.
        Scanner scanner = new Scanner(fileIn);

        // Reads entire expression as a string into a string variable.
        line = scanner.nextLine();
        do {
            lex();
        } while (nextToken != tokenCodes.EOF);
        }


        public static void lookup ( char ch){
            switch (ch) {
                case '(':
                    addChar();
                    nextToken = tokenCodes.LEFT_PAREN;
                    break;

                case ')':
                    addChar();
                    nextToken = tokenCodes.RIGHT_PAREN;
                    break;

                case '+':
                    addChar();
                    nextToken = tokenCodes.ADD_OP;
                    break;

                case '-':
                    addChar();
                    nextToken = tokenCodes.SUB_OP;
                    break;

                case '*':
                    addChar();
                    nextToken = tokenCodes.MULT_OP;
                    break;

                case '/':
                    addChar();
                    nextToken = tokenCodes.DIV_OP;
                    break;
                case '=':
                    addChar();
                    nextToken = tokenCodes.ASSIGN_OP;
                    break;

                default:
                    addChar();
                    nextToken = tokenCodes.EOF;

            }
        }

        public static void addChar () {
            if (lexLen <= 98) {
                // Appends next character to Array list
                lexeme.add(nextChar);
            } else {
                System.out.println("Error - lexeme is too long. \n");
            }
        }

        public static void getChar(){

            if(i < line.length()) {
                nextChar = line.charAt(i);

                // Replaces getNonBlank function as it broke the logic of the program. Instead, if whitespace
                // is detected focus is moved to the next character in the string and getChar is called recursively.
                if (isSpaceChar(nextChar)){
                    i++;
                    getChar();
                }
                else if (isLetter(nextChar)) {
                    charClass = charClasses.LETTER;
                } else if (isDigit(nextChar)) {
                    charClass = charClasses.DIGIT;
                } else{
                    charClass = charClasses.UNKNOWN;
                }
            }
            else {
                charClass = charClasses.EOF;
            }
            i++;
        }


        public static void lex(){

            lexLen = 0;

            // Only call getChar here when this is the first time lex() has been called. Otherwise the first character
            // in each lexeme is skipped.
            if (charClass == null) {
                getChar();
            }
            switch (charClass){

                case LETTER:
                    addChar();
                    getChar();
                    while (charClass == charClasses.LETTER || charClass == charClasses.DIGIT ){
                        addChar();
                        getChar();
                    }
                    nextToken = tokenCodes.IDENT;
                    break;

                case DIGIT:
                    addChar();
                    getChar();
                    while (charClass == charClasses.DIGIT){
                        addChar();
                        getChar();
                    }
                    nextToken = tokenCodes.INT_LIT;
                    break;

                case UNKNOWN:
                    lookup(nextChar);
                    getChar();
                    break;

                case EOF:
                    nextToken = tokenCodes.EOF;
                    lexeme.add('E');
                    lexeme.add('O');
                    lexeme.add('F');
                    break;
            }

            // Build a string object from the lexeme array list. Allows formatted output, i.e. no commas or brackets.
            StringBuilder builder = new StringBuilder();
            for (Character value : lexeme){
                builder.append(value);
            }
            // Convert string builder object to string for display in print call.
            String text = builder.toString();

            System.out.println("Next token is: " + nextToken.getValue() + " Next lexeme is " + text);

            // Clears the previous array list to store new lexeme.
            lexeme.clear();
        }
}

