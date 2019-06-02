package com.yodlee.iae.health.util;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class ShingleBased {

    private static final int DEFAULT_K = 3;

    private final int k;

    /**
     * Pattern for finding multiple following spaces.
     */
    private static final Pattern SPACE_REG = Pattern.compile("\\s+");

    /**
     *
     * @param k
     * @throws IllegalArgumentException if k is &lt;= 0
     */
    public ShingleBased(final int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("k should be positive!");
        }
        this.k = k;
    }

    /**
     *
     */
    ShingleBased() {
        this(DEFAULT_K);
    }
 
    public final int getK() {
        return k;
    }

   
    public final Map<String, Integer> getProfile(final String string) {
        HashMap<String, Integer> shingles = new HashMap<String, Integer>();

        String string_no_space = SPACE_REG.matcher(string).replaceAll(" ");
        for (int i = 0; i < (string_no_space.length() - k + 1); i++) {
            String shingle = string_no_space.substring(i, i + k);
            Integer old = shingles.get(shingle);
            if (old != null) {
                shingles.put(shingle, old + 1);
            } else {
                shingles.put(shingle, 1);
            }
        }

        return Collections.unmodifiableMap(shingles);
    }
}