package org.example;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * contains a main that provides a command-line interface to DictionaryBuilder
 * Accept a command line argument of a filename to load as the dictionary
 * Allow the user to interact with the dictionary using the following commands:
 * search, add, delete, list, stats, exit
 */
public class DictionaryShell {

    private static void printCommand() {
        System.out.println("Available commands: search <word>, add <word>, delete <word>, list, stats, exit");
    }

    public static void main(String[] args) {
        DictionaryBuilder dictionary;
        if (args.length >= 1) {
            String fileName = args[0];
            // check existing file --> read dictionary from DictionaryBuilder(fileName)
            try {
                dictionary = new DictionaryBuilder(fileName);
            } catch (FileNotFoundException e) {
                System.out.println("File " + fileName +" is not found" + " (it will be started with empty dictionary)");
                dictionary = new DictionaryBuilder(991);
            }
        } else {
            System.out.println("Please input provided file, otherwise starting with empty dictionary.");
            dictionary = new DictionaryBuilder(991);
        }

        System.out.println("Welcome to the Dictionary Builder CLI.");
        printCommand();

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine())
                break;
            String line = in.nextLine().trim();
            if (line.isEmpty())
                continue;

            String[] parts = line.split("\\s+", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                        // get Frequency of word in dictionary
                        // print
                        // if not found --> print error
                    case "search": {
                        if (parts.length < 2) { System.out.println("Usage: search <word>");
                            break;
                        }
                        String wordDictionary = parts[1];
                        int frequency = dictionary.getFrequency(wordDictionary);
                        if (frequency > 0) {
                            System.out.println(frequency + " instance(s) of \"" + wordDictionary.toLowerCase() + "\" found.");
                        }else
                            System.out.println("\"" + wordDictionary.toLowerCase() + "\" not found.");
                        break;
                    }
                        // If word is not existed, add new word
                        // If word is existed, increase count
                    case "add": {
                        if (parts.length < 2) {
                            System.out.println("Usage: add <word>");
                            break;
                        }
                        String wordDictionary = parts[1];
                        int before = dictionary.getFrequency(wordDictionary);
                        dictionary.addWord(wordDictionary);
                        if (before == 0){
                            System.out.println("\"" + wordDictionary.toLowerCase() + "\" added.");
                        }
                        else
                            System.out.println("\"" + wordDictionary.toLowerCase() + "\" count incremented.");
                        break;
                    }
                        // delete word in Dictionary
                        // if that word is not existed, throw and print error
                    case "delete": {
                        if (parts.length < 2) {
                            System.out.println("Usage: delete <word>");
                            break;
                        }
                        String wordDictionary = parts[1];
                        dictionary.removeWord(wordDictionary);
                        System.out.println("\"" + wordDictionary.toLowerCase() + "\" deleted.");
                        break;
                    }
                        // print every word in dictionary
                    case "list": {
                        ArrayList<String> words = dictionary.getAllWords();
                        for (String wordDictionary : words)
                            System.out.println(wordDictionary);
                        break;
                    }
                        // print statistics
                    case "stats": {
                        System.out.println("Total words: " + dictionary.getTotalWords());
                        System.out.println("Total unique words: " + dictionary.getUniqueWords());
                        System.out.printf("Estimated load factor: %.2f%n", dictionary.estimatedLoadFactor());
                        System.out.println("Table capacity (4k+3 prime): " + dictionary.capacity());
                        break;
                    }
                    case "exit":
                        System.out.println("Quitting...");
                        return;
                    default:
                        System.out.println("Unknown command: " + command);
                        printCommand();
                }
            } catch (DictionaryEntryNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
