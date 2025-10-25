package org.example;

/**
 * Utilities for primes of the form 4k+3 and primality test (6k±1 optimization).
 */
public class PrimeUtils {

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * 1L * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static int next4kPlus3Prime(int n) {
        if (n <= 3) return 3;
        int candidate = n;
        if (candidate % 4 != 3) {
            candidate += (3 - candidate % 4 + 4) % 4;
        }
        while (!isPrime(candidate)) {
            candidate += 4;
        }
        return candidate;
    }
}
