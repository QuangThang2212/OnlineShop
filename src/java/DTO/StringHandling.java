/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author PQT2212
 */
public class StringHandling {

    public static String upperFirstLetter(String input) {
        if (input == null) {
            return "";
        }
        char[] charArray = input.toCharArray();
        boolean foundSpace = true;

        for (int i = 0; i < charArray.length; i++) {
            if (Character.isLetter(charArray[i])) {
                if (foundSpace) {
                    charArray[i] = Character.toUpperCase(charArray[i]);
                    foundSpace = false;
                } else {
                    charArray[i] = Character.toLowerCase(charArray[i]);
                }
            } else if (Character.isDigit(charArray[i])) {
                continue;
            } else {
                foundSpace = true;
            }
        }
        input = String.valueOf(charArray);

        return input;
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

    public static String HashMapToString(HashMap<Integer, Integer> productsCart) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : productsCart.entrySet()) {
            if (count == 0) {
                builder.append(entry.getKey()).append("&").append(entry.getValue());
                count++;
            } else {
                builder.append("-").append(entry.getKey()).append("&").append(entry.getValue());
            }
            count++;
        }
        return builder.toString();
    }

    public static HashMap<Integer, Integer> StringToHashMap(String hashmap) {
        HashMap<Integer, Integer> productsCart = new HashMap<>();
        String hashmapArray[] = hashmap.split("-");
        for (String hashmapArray1 : hashmapArray) {
            String hashmapArrayEmlement[] = hashmapArray1.split("&");
            try {
                int key = Integer.parseInt(hashmapArrayEmlement[0]);
                int value = Integer.parseInt(hashmapArrayEmlement[1]);
                productsCart.put(key, value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return productsCart;
    }
}
