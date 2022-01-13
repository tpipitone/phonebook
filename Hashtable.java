package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/* NAME THOMAS PIPITONE
RID 82460781
CSSC4068

METHODS/CONSTRUCTOR help credit ROB EDWARDS YouTube playlist "Data Structures"
 */

public class Hashtable<K extends Comparable<K>, V> implements DictionaryADT<K,V>{
    int modiCount;
    int currSize,tableSize;
    double maxLoadFactor;
    private LinearList<DictionaryNode<K,V>>[] hashArr;

    class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>{ //doint use.equasls
        K key;
        V value;

        public DictionaryNode(K key, V value){ // node constructor, no need for left or rightchild
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(DictionaryNode<K, V> o) {
            return (((Comparable<K>)this.key).compareTo(o.key));
        }
    }

    @SuppressWarnings("unchecked")
    public Hashtable(int tableSize){
        this.tableSize = tableSize;
        hashArr = (LinearList<DictionaryNode<K,V>>[]) new LinearList[tableSize];

        for(int i = 0; i < tableSize; i++){
            hashArr[i] = new LinearList<DictionaryNode<K,V>>();
        }
        maxLoadFactor = 0.75;
        currSize = 0;
        modiCount = 0;

    }

    @Override
    public boolean contains(K key) {  // maps the hashvalue to find the index of the element being searched fopre using %
        int hashval = (key.hashCode() & 0x7FFFFFFF) % tableSize;
        for(DictionaryNode<K,V>he : hashArr[hashval]){
            if(((Comparable<K>)key).compareTo(he.key) == 0){
                return true;
            }
        }
        return false;
    }

    private double loadFactor(){
        return (currSize / tableSize);
    }

    @Override
    public boolean add(K key, V value) {  // mods hashcode to find an index to add the element
        if(contains(key))
            return false;
        if(loadFactor() > maxLoadFactor ){ // checks the loadfactor to know if arr needs to be resized
            resize(tableSize*2);
        }
        DictionaryNode<K,V> element = new DictionaryNode(key,value);
        int hashVal = key.hashCode();
        hashVal = hashVal & 0x7FFFFFFF;
        hashVal = hashVal % tableSize; // our index to add the element
        hashArr[hashVal].addLast(element);
        currSize++;
        modiCount++;
        return true;
    }

    @Override
    public boolean delete(K key) {  // same idea as contains, but nullifies element once found.
        int hashVal = key.hashCode();
        hashVal = hashVal & 0x7FFFFFFF;
        hashVal = hashVal % tableSize; // our index to rem the element

        DictionaryNode<K,V> remElement = new DictionaryNode(key,getValue(key));
        hashArr[hashVal].remove(remElement);
        currSize--;
        modiCount++;
        return true;

    }

    @Override
    public V getValue(K key) {  // uses the key's hashcode to find the index of the value we're looking for
        int hashval = (key.hashCode() & 0x7FFFFFFF) % tableSize;

        for(DictionaryNode<K,V>he : hashArr[hashval]){

            if( he != null && ((Comparable<K>)key).compareTo(he.key) == 0){
                return he.value;
            }
        }
        return null;
    }

    private void resize(int newSize){
        LinearList<DictionaryNode<K,V>>[] newArr = (LinearList<DictionaryNode<K,V>>[]) new LinearList[newSize];
        for(int i = 0; i < newSize; i++){
            newArr[i] = new LinearList<DictionaryNode<K,V>>();
        }
        for(LinearList<DictionaryNode<K,V>>linList: hashArr){  // re adds each element to a new array of a resized length
            for(DictionaryNode<K,V>elem : linList){
                int hashVal = elem.key.hashCode();
                hashVal = hashVal & 0x7FFFFFFF;
                hashVal = hashVal % newSize;
                newArr[hashVal].addLast(elem); // might haveto add an element num incriment here
            }
        }
        hashArr = newArr;
        tableSize = newSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K getKey(V value) {
        for(int i = 0; i < hashArr.length; i++){  // nested forloop, needs to loop through the entire table to find the key at a given value
            for(DictionaryNode<K,V>node : hashArr[i]){
                if(((Comparable<V>)value).compareTo(node.value) == 0){
                    return node.key;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return currSize;
    }

    @Override
    public boolean isFull() { // never full due to our resize method
        return false;
    }

    @Override
    public boolean isEmpty() {
        return currSize == 0;
    }

    @Override
    public void clear() {
        currSize = 0;
    }

    @SuppressWarnings("unchecked")
    class KeyIteratorHelper<T> implements Iterator<T>{
        T[] keys;
        int pos;
        int iterModiCount;
        public KeyIteratorHelper(){ // converts the hashtable into a generic array, to be iterated through one by one.
            keys = (T[])new Object[currSize];
            iterModiCount = modiCount;
            int p = 0;
            for(int i = 0; i < tableSize; i++){
                LinearList<DictionaryNode<K,V>> list = hashArr[i];
                for(DictionaryNode<K,V> h : list){
                    keys[p++] = (T)h.key;
                }
            }
            quickSort(0,keys.length-1);
            pos = 0;
        }

        private void quickSort(int from, int to){  // sorts our array so keys are in order, // credit Rob Edwards youtube
            if(from >= to)
                return;
            T key = keys[to];
            int counter = from;
            for(int i = from; i < to; i++){
                if(((Comparable<T>)keys[i]).compareTo(key) <= 0){
                    swap(i, counter);
                    counter++;
                }
            }
            swap(counter,to);
            quickSort(from, counter-1);
            quickSort(counter+1, to);
        }

        private void swap(int from, int to){ // sort helper
            T temp = keys[from];
            keys[from] = keys[to];
            keys[to] = temp;
        }


        public boolean hasNext(){
            if(iterModiCount != modiCount)
                throw new ConcurrentModificationException();
            return pos < keys.length;
        }
        public T next(){
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return keys[pos++];
        }
    }

    @SuppressWarnings("unchecked")
    class ValIteratorHelper<Q> implements Iterator<Q>{ // same idea as our keyIterator, just without a sorting method
        Q[] vals;
        int pos;
        int iterModiCount;
        public ValIteratorHelper(){
            vals = (Q[])new Object[currSize];
            iterModiCount = modiCount;
            int p = 0;
            for(int i = 0; i < tableSize; i++){
                LinearList<DictionaryNode<K,V>> list = hashArr[i];
                for(DictionaryNode<K,V> h : list){
                    vals[p++] = (Q)h.value;
                }
            }
            pos = 0;
        }

        public boolean hasNext(){
            if(iterModiCount != modiCount)
                throw new ConcurrentModificationException();
            return pos < vals.length;
        }
        public Q next(){
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return vals[pos++];
        }
    }

    @Override
    public Iterator keys() {
        return new KeyIteratorHelper();
    }

    @Override
    public Iterator values() {
        return new ValIteratorHelper();
    }
}

