package org.example;

/**
 * Check isPrime
 * Find prime number 4k+3 (next4kPlus3Prime)
 */
public class PrimeUtils {

    // check a which is a number whether it is a prime number
    //  means: is a natural number greater than 1 that has exactly two positive divisors: 1 and itself.
    public static boolean isPrime(int a) {
        if (a <= 1)
            return false;
        if (a <= 3)
            return true;
        if (a % 2 == 0 || a % 3 == 0) // if divide either of them --> a is not prime number
            return false;
        for (int i = 5; i * 1L * i <= a; i += 6) {
            if (a % i == 0 || a % (i + 2) == 0) return false;
        }
        return true;
    }
        // find prime number >= n of the form 4k+3
    public static int next4kPlus3Prime(int n) {
        if (n <= 3)
            return 3; // return the 1st 4k+3 prime number
        int candidate = n;
        if (candidate % 4 != 3) {
            candidate += (3 - candidate % 4 + 4) % 4;

            // candidate = 14
            // 14 % 4 = 2 --> not 4k+3
            // (3 - 2 + 4) % 4 = 5 % 4 = 1
            // candidate +=1 --> 15
            // check 15 % 4 = 3

        }
        while (!isPrime(candidate)) {
            candidate += 4;
        }
        return candidate;
    }
}
