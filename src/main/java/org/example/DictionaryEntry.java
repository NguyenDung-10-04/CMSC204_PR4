
package org.example;

/**
 * One word and its frequency
 * (Lower-case; punctuation-stripped)
 */
public class DictionaryEntry {
    private final String word;
    private int count; // its frequency

    // constructor
    public DictionaryEntry(String word) {
        if (word == null) throw new IllegalArgumentException("words are null");
        this.word = word;
        this.count = 1;
    }
    // methods:
    public String getWord() { return word; }
    public int getCount() { return count; }
    public void increment() { count++; }
    public void decrement() { if (count > 0) count--; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DictionaryEntry)) return false;
        return word.equals(((DictionaryEntry) o).word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public String toString() {
        return word + ":" + count;
    }
}
