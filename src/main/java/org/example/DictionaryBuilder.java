
package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 DictionaryBuilder– manages the hash table, implements methods:
 DictionaryBuilder(int estimatedEntries)- Constructor that creates a hash table appropriate
 for the estimated entries. Must consider load factor and 4k+3 prime table size.
 DictionaryBuilder(String filename)- Constructor that reads a file and adds all the words to
 the DictionaryBuilder
 Methods:
 1/ addWord(String word)
 2/ getCount(String word)
 3/ removeWord(String word)
 4/ getAllWords()– return sorted ArrayList of all the words in the dictionary
 */


public class DictionaryBuilder {

    private final GenericLinkedList<DictionaryEntry>[] table;
    // Array of class MyLinkedLists (hash table buckets)
    private final int capacity;
    // Number of buckets
    private int totalWords = 0;
    // including duplicate
    private int uniqueWords = 0;


    public DictionaryBuilder(int estimatedEntries) {
        // use load factor (0.6) and find the nearest 4k+3 prime to estimate table size;


        int base = (int)Math.ceil(estimatedEntries / 0.6);
        // check how big is the hash table
        // only get 60% of full hash table
        // --> give a large number of buckets

        int cap = PrimeUtils.next4kPlus3Prime(Math.max(3, base));
        // check when it is 4k+3 prime
        this.capacity = cap;

        // create new empty linked list
        this.table = (GenericLinkedList<DictionaryEntry>[]) new GenericLinkedList[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new GenericLinkedList<>();
        // run for loop from 0 to capacity,
        // initialize each ele in hash table to empty each linked list
    }


    public DictionaryBuilder(String filename) throws FileNotFoundException { // constructor
        this(estimateEntriesFromFile(filename));
        // estimate words in file --> create enough spaces in hash table
        loadFile(filename);
        // read file and add words into hash table
    }

    // estimate words in file
    private static int estimateEntriesFromFile(String filename) {
        File f = new File(filename);
        int bytes = (int) f.length();
        // get length of file by bytes
        int estUnique = Math.max(1, bytes / 100);
        // estimate unique word from the beginning
        int tableNeed = (int) Math.ceil(estUnique / 0.6);
        // calculate hash table based on load factor (=0.6) and the prime number 4k+3
        PrimeUtils.next4kPlus3Prime(Math.min(Integer.MAX_VALUE, tableNeed));
        return estUnique;
    }

    // get index in Hash
    private static int indexForHash(int hash, int capacity) {
        int h = hash ^ (hash >>> 16);
        if (h < 0) h = -h;
        return h % capacity;
    }

    // covert words into normal words (Lowercase - Remove - trim space)
    private static String normalize(String w) {
        if (w == null) return "";
        w = w.toLowerCase();
        w = w.replaceAll("[^a-z0-9']+", " ").trim();
        return w;
    }


    // read file and add all words into that file
    private void loadFile(String filename) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String cleaned = normalize(line);
                if (cleaned.isEmpty()) continue;
                String[] tokens = cleaned.split("\\s+");
                for (String t : tokens) {
                    if (t.isEmpty() || t.equals("'")) continue;
                    addWord(t);
                }
            }
        }
    }

    public void addWord(String word) {
        if (word == null) return;
        String newWord = normalize(word);
        // normalize word

        if (newWord.isEmpty()) return;

        int index = indexForHash(newWord.hashCode(), capacity);
        // get hash code of newWord after normalizing --> index

        GenericLinkedList<DictionaryEntry> bucket = table[index];
        // get the bucket at that index

        DictionaryEntry probe = new DictionaryEntry(newWord);
        DictionaryEntry existing = bucket.find(probe);

        // check probe whether it is in bucket

        // case 1: (addFirst)
        if (existing == null) {
            bucket.addFirst(probe);
            uniqueWords++;
            //case 2: increase existing
        } else {
            existing.increment();
        }
        totalWords++;
    }
    // check how many times does that word appear
    public int getFrequency(String word) {
        if (word == null) return 0;
        String newWord = normalize(word);
        if (newWord.isEmpty()) return 0;

        int index = indexForHash(newWord.hashCode(), capacity);
        DictionaryEntry found = table[index].find(new DictionaryEntry(newWord));
        if (found == null) return 0;
        return found.getCount();
    }

    public void removeWord(String word) throws DictionaryEntryNotFoundException {
        if (word == null) throw new DictionaryEntryNotFoundException("The word cannot be null." );
        String newWord = normalize(word);
        if (newWord.isEmpty()) throw new DictionaryEntryNotFoundException("The word cannot be empty.");

        int index = indexForHash(newWord.hashCode(), capacity);
        // check index bucket of hash table that word will be there
        DictionaryEntry removed = table[index].remove(new DictionaryEntry(newWord));
        // find and remove "newWord" in DictionaryEntry

        if (removed == null) {
            throw new DictionaryEntryNotFoundException("\"" + word + "\" not found.");
        } else {
            totalWords -= removed.getCount();
            uniqueWords--;
        }
    }

    public ArrayList<String> getAllWords() {
        // create new list to store all words with the capacity of uniqueWords
        ArrayList<String> allWords = new ArrayList<>(uniqueWords);
        for (int i = 0; i < capacity; i++) {
            // check each bucket in table
            for(DictionaryEntry entry : table[i]){
                // check all DictionaryEntry in table[i]
                allWords.add(entry.getWord());
            }
        }
        Collections.sort(allWords);
        return allWords;
    }

    public int getTotalWords(){
        return totalWords;
    }
    public int getUniqueWords(){
        return uniqueWords;
    }
    public int capacity(){
        return capacity;
    }
    public double estimatedLoadFactor(){
        return capacity == 0 ? 0.0 : (uniqueWords * 1.0) / capacity;
    }

    public void loadFromFile(String filename) throws FileNotFoundException {
        loadFile(filename);
    }
}
