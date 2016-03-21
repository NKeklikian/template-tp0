package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegExGenerator {
    private int maxLength;
    private Random random = new Random();
    private StringBuffer resultBuffer;
    private static int INVALID_CHAR = 133;
    private StringBuffer charsInToken;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    private void processRepeatingQuantifiers(char charAtIndex) {
        switch (charAtIndex) {
            case '*':
                int totalRepetitions = random.nextInt(maxLength);
                if (totalRepetitions == 0) {
                    resultBuffer.deleteCharAt(resultBuffer.length() - 1);
                } else {
                    for (int repetition = 0; repetition < totalRepetitions; repetition++) {
                        resultBuffer.append(charsInToken.charAt(random.nextInt(charsInToken.length())));
                    }
                }
                return ;
            case '+':
                totalRepetitions = random.nextInt(maxLength);
                for (int repetition = 0; repetition < totalRepetitions; repetition++) {
                    resultBuffer.append(charsInToken.charAt(random.nextInt(charsInToken.length())));
                }
                return ;
            default:
        }
    }

    //Takes care of ?, delegates other quantifiers
    private void processQuantifiers(char charAtIndex) {
        switch (charAtIndex) {
            case '?':
                boolean removeChar = random.nextInt(2) == 1;
                if (removeChar) {
                    resultBuffer.deleteCharAt(resultBuffer.length() - 1);
                }
                return;
            default:
                processRepeatingQuantifiers(charAtIndex);
        }
    }

    //Takes care of . and literals
    private void processSingleChar(char charAtIndex) {
        if (charAtIndex == '+' || charAtIndex == '?' || charAtIndex == '*') {
            processQuantifiers(charAtIndex);
        } else {
            switch (charAtIndex) {
                case '.':
                    int charToPut = INVALID_CHAR;
                    while (charToPut == INVALID_CHAR) {
                        //The first 32 ASCII chars are invalid
                        charToPut = (random.nextInt(256 - 32) + 32);
                    }
                    charsInToken = new StringBuffer("" + (char) charToPut);
                    resultBuffer.append((char) charToPut);
                    return;
                default:
                    charsInToken = new StringBuffer("" + charAtIndex);
                    resultBuffer.append(charAtIndex);
            }
        }
    }

    private String generateOneResult(String regEx) {
        resultBuffer = new StringBuffer();
        int index = 0;
        for (;index < regEx.length(); index++) {
            switch (regEx.charAt(index)) {
                case '\\':
                    index++;
                    resultBuffer.append(regEx.charAt(index));
                    charsInToken = new StringBuffer("" + regEx.charAt(index));
                    break;
                case '[':
                    for (charsInToken = new StringBuffer(); regEx.charAt(index + 1) != ']'; index++) {
                        charsInToken.append(regEx.charAt(index + 1));
                    }
                    resultBuffer.append(charsInToken.charAt(random.nextInt(charsInToken.length())));
                    index++;
                    break;
                default :
                    processSingleChar(regEx.charAt(index));
            }
        }
        return resultBuffer.toString();
    }

    public List<String> generate(String regEx, int numberOfResults) {
        return new ArrayList<String>() {
            {
                for (int resultsAdded = 0; resultsAdded < numberOfResults; resultsAdded++) {
                    add(generateOneResult(regEx));
                }
            }
        };
    }
}