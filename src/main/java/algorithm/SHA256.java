package algorithm;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* <h1> SHA256 Algorithm </h1>
*
* <a href="https://www.geeksforgeeks.org/sha-256-hash-in-java/"> Source Code </a>
*
* <p> I add instance variable, constructor, setValue and changed
*         getValue from Source Code. </p>
*/


public class SHA256 {
    private String value;


    public SHA256(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() throws NoSuchAlgorithmException {
        return toHexString(getSHA(this.value));
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}