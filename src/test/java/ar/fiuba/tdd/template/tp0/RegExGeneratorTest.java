package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) {
        RegExGenerator generator = new RegExGenerator(5);
        List<String> results = generator.generate(regEx, numberOfResults);
        System.out.println(results.get(0));
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() {
        assertTrue(validate(".", 1));
    }

    @Test
    public void testMultipleCharacters() {
        assertTrue(validate("...", 1));
    }

    @Test
    public void testLiteral() {
        assertTrue(validate("\\@", 1));
    }

    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }

    @Test
    public void testLiteralBracket(){
        assertTrue(validate("\\]",1));
    }

    @Test
    public void testLiteralBackslash(){
        assertTrue(validate("\\\\", 1));
    }

    //Test if any invalid chars are returned
    @Test
    public void testTwentyDots(){
        assertTrue(validate("....................", 1));
    }

    @Test
    public void testDigits(){
        assertTrue(validate("1356789420",1));
    }

    @Test
    public void testMultipleQuantifiers(){
        assertTrue(validate(".*a+[guante]?[xkcd]+1024?n*s*", 1));
    }
}
