package com.example.stelios.songle;



public class StringSimilarity {

        // Calculates the similarity (a double between 0 and 1) between two strings
        public static double similarity(String s1, String s2) {

            // Taking the two strings
            String longer = s1,
                    shorter = s2;

            // Longer should always have greater length
            if (s1.length() < s2.length()) {
                longer = s2;
                shorter = s1;
            }

            // Take longer string size
            int longerLength = longer.length();

            // Case where both strings are of zero length
            if (longerLength == 0) {
                return 1.0; }

            // Return Length of Longer String minus Levenshtein Edit Distance over the Length of Longer String
            return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
        }

        // Levenshtein Edit Distance Algorithm
        // Reference:  http://rosettacode.org/wiki/Levenshtein_distance#Java
        public static int editDistance(String s1, String s2) {
            s1 = s1.toLowerCase();
            s2 = s2.toLowerCase();

            int[] costs = new int[s2.length() + 1];
            for (int i = 0; i <= s1.length(); i++) {
                int lastValue = i;
                for (int j = 0; j <= s2.length(); j++) {
                    if (i == 0)
                        costs[j] = j;
                    else {
                        if (j > 0) {
                            int newValue = costs[j - 1];
                            if (s1.charAt(i - 1) != s2.charAt(j - 1))
                                newValue = Math.min(Math.min(newValue, lastValue),
                                        costs[j]) + 1;
                            costs[j - 1] = lastValue;
                            lastValue = newValue;
                        }
                    }
                }
                if (i > 0)
                    costs[s2.length()] = lastValue;
            }
            return costs[s2.length()];
        }

        // Print similarity of two Strings
        public static void printSimilarity(String s, String t) {
            System.out.println(String.format(
                    "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
        }
}
