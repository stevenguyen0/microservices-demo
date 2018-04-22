
package com.demo.ms.tinyurlservice.business;

public class StringShortener {
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
    private static final int    BASE     = ALPHABET.length();

    /*
     * get remainder by dividing the BASE, map to a character in ALPHABET
     * keep doing this until remainder reaches 0
     */
    public static String encode(long num) {
        StringBuilder result = new StringBuilder();
        while ( num > 0 ) {
        	result.append(getCharFromArray(getRemainder(num)));
            num /= BASE;
        }
        return result.reverse().toString();   
    }
    
    private static int getRemainder(long num) {
    	return (int)num % BASE;
    }
    private static char getCharFromArray(int index) {
    	return ALPHABET_ARRAY[index];
    }

    public static long decode(String str) {
        long num = 0;
        for ( int i = 0; i < str.length(); i++ )
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        return num;
    } 
}
