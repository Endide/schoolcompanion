package fr.endide.application.data.generator;

import org.apache.commons.text.RandomStringGenerator;

public class passwordGenerator {
    public static String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(length);
    }
}
