
package org.example;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/**
 * Command-line interface:
 *   search <word> | add <word> | delete <word> | list | stats | exit
 * If args[0] is provided, load file on startup (root project folder).
 *
 * Example:
 *   java DictionaryShell sample.txt
 */


public class DictionaryShell {

    private static void printHelp() {
        System.out.println("Available commands: search <word>, add <word>, delete <word>, list, stats, exit");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        DictionaryBuilder dict;
        if (args.length >= 1) {
            String filename = args[0];
            try {
                dict = new DictionaryBuilder(filename);
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + filename + " (starting with empty dictionary)");
                dict = new DictionaryBuilder(101);
            }
        } else {
            System.out.println("No input file provided; starting with empty dictionary.");
            dict = new DictionaryBuilder(101);
        }

        System.out.println("Welcome to the Dictionary Builder CLI.");
        printHelp();

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) break;
            String line = in.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();

            try {
                switch (cmd) {
                    case "search": {
                        if (parts.length < 2) { System.out.println("Usage: search <word>"); break; }
                        String w = parts[1];
                        int f = dict.getFrequency(w);
                        if (f > 0) System.out.println(f + " instance(s) of \"" + w.toLowerCase() + "\" found.");
                        else System.out.println("\"" + w.toLowerCase() + "\" not found.");
                        break;
                    }
                    case "add": {
                        if (parts.length < 2) { System.out.println("Usage: add <word>"); break; }
                        String w = parts[1];
                        int before = dict.getFrequency(w);
                        dict.addWord(w);
                        if (before == 0) System.out.println("\"" + w.toLowerCase() + "\" added.");
                        else System.out.println("\"" + w.toLowerCase() + "\" count incremented.");
                        break;
                    }
                    case "delete": {
                        if (parts.length < 2) { System.out.println("Usage: delete <word>"); break; }
                        String w = parts[1];
                        dict.removeWord(w);
                        System.out.println("\"" + w.toLowerCase() + "\" deleted.");
                        break;
                    }
                    case "list": {
                        ArrayList<String> words = dict.getAllWords();
                        for (String w : words) System.out.println(w);
                        break;
                    }
                    case "stats": {
                        System.out.println("Total words: " + dict.getTotalWords());
                        System.out.println("Total unique words: " + dict.getUniqueWords());
                        System.out.printf(Locale.US, "Estimated load factor: %.2f%n", dict.estimatedLoadFactor());
                        System.out.println("Table capacity (4k+3 prime): " + dict.capacity());
                        break;
                    }
                    case "exit":
                        System.out.println("Quitting...");
                        return;
                    default:
                        System.out.println("Unknown command: " + cmd);
                        printHelp();
                }
            } catch (DictionaryEntryNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
