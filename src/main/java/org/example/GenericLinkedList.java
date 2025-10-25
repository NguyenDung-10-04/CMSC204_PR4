package org.example;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Use singly linked list (generic) for chaining buckets.
 * Use simple linked list
 * Support for DictionaryBuilder class
 * @param <E> is the type of elements stored in the list
 */
public class GenericLinkedList<E> implements Iterable<E> {

    private static class Node<E>{
        E data;
        Node<E> next;
        Node(E d) {
            data = d;
            next = null;
        }
    }

    private Node<E> head;
    private int size = 0;
    // construct
    public GenericLinkedList(){
        head = null;
        size = 0;
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }

    public void addFirst(E element){
        Node<E> node = new Node<>(element);
        node.next = head;
        head = node;
        size++;
    }

    public E find(E element){
        for(Node<E> current = head; current != null; current = current.next){
            if((element == null && current.data == null) || element != null &&  element.equals(current.data)){
                return current.data;
            }
        }

        // Run for loop from head to last
        // if seeing node == value --> return value
        // otherwise return null
        return null;
    }
    // remove first node (ELEMENT) that == element(desired) in linked list
    // head -> [3] -> [5] -> [7] -> [5] -> null
    // remove(5)
    // --> [3]: NO --> [5]: YES
    // --> head -> [3] -> [7] -> [5] -> null
    public E remove(E element) {
        Node<E> previous = null;

        // Run for loop from head (which is current) to the last node of list)
        for(Node<E> current = head; current != null; current = current.next){
            if((element == null && current.data == null) || (element != null && element.equals(current.data))){
                if(previous == null){
                    // when node is at the beginning
                    head = current.next;
                }else
                    // when node is in the middle or at the end
                    previous.next = current.next;
                size--;
                /* head → [A] → [B] → [C] → null
                head
                 ↓
                [A] → [B] → [C] → null
                 ^
                cur
                prev = null
                --------------------------------
                         remove("B");
                         prev = current
                [A] → [B] → [C] → null
                 ↑     ^
                 prev  cur
                 prev.next(A) = current.next(C)
                --------------------------------
                [A] → [C] → null
                 ↑
                prev
                 */
                return current.data;
            }
            previous = current;
        }
        return null;
    }

    @FunctionalInterface
    public interface Visitor<E> { void accept(E e); }

    public void forEach(Visitor<E> v) {
        Node<E> cur = head;
        while (cur != null) { v.accept(cur.data); cur = cur.next; }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) throw new NoSuchElementException();
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
