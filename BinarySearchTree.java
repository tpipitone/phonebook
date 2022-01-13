package data_structures;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/* NAME THOMAS PIPITONE
RID 82460781
CSSC4068

Help Credit ALAN RIGGINS COURSE READER AND BINARY SEARCH TREE LECTURE VIDEO
 */

public class BinarySearchTree<K extends Comparable<K>, V> implements DictionaryADT<K, V>{

    private Node<K,V> root;
    private int currSize;
    private int modiCount;

    public BinarySearchTree(){ // no args
        root = null;
        modiCount = 0;
        currSize = 0;
    }

    class Node<K,V> {  //node class with parent, left, right
        K key;
        V value;
        Node<K,V> leftChild;
        Node<K,V> rightChild;
        Node<K,V> parent;

        public Node(K k, V v){
            key = k;
            value = v;
            leftChild = rightChild = parent = null;
        }
    }

    @Override
    public boolean contains(K key) {
        return getValue(key) != null;
    }

    private void insert(K key, V val, Node<K,V> node, Node<K,V> parent, boolean wasLeft){
        if(node == null){  //traverses through the tree, recursively calling itself based on wheater it is going left or right according to bst properties
            if(wasLeft) {  // reaches this if at the end of tree (child node == null)
                parent.leftChild = new Node<K, V>(key, val);
            } else {
                parent.rightChild = new Node<K,V>(key,val);
            }


        } else if (((Comparable<K>)key).compareTo((K)node.key) < 0 ) {
            insert(key, val, node.leftChild, node, true);  // recursion until a null child node is reached, this is our insertion point
        } else {
            insert(key,val, node.rightChild,node, false);
        }
    }

    @Override
    public boolean add(K key, V value) { // calls our insert method described above
        if(contains(key))
            return false;
        if(root == null)
            root = new Node<K,V>(key,value);
        else
            insert(key,value,root,null,false);
        currSize++;
        modiCount++;
        return true;
    }

    @Override
    public boolean delete(K key) { //unfinished
        return deleteHelper(root, key);
    }

    private boolean deleteHelper(Node<K,V> n, K key){ //unfinished
        modiCount++;
        return false;
    }

    @Override
    public V getValue(K key) {
        return findVal(key,root);
    }

    private Node<K,V> findNode(K key, Node<K,V>node){
        if(node == null)
            return null;
        if(((Comparable<K>)key).compareTo(node.key) < 0)
            return findNode(key, node.leftChild);
        if(((Comparable<K>)key).compareTo(node.key) > 0 )
            return findNode(key, node.rightChild);
        return node;
    }

    private V findVal(K key, Node<K,V> node){
        if(node == null)
            return null;
        if(((Comparable<K>)key).compareTo(node.key) < 0)  // loops through based on wheather our key is less than or greater than the current node
            return findVal(key, node.leftChild);
        if(((Comparable<K>)key).compareTo(node.key) > 0 )
            return findVal(key, node.rightChild);
        return node.value;
    }

    @Override
    public K getKey(V value) {
        return findKey(value,root);
    }

    @SuppressWarnings("unchecked")
    private K findKey(V val, Node<K,V> node){
        if(node == null)
            return null;
        if(((Comparable<V>)val).compareTo(node.value) < 0)
            return findKey(val, node.leftChild);
        if(((Comparable<V>)val).compareTo(node.value) > 0 )
            return findKey(val, node.rightChild);
        return node.key;
    }

    @Override
    public int size() {
        return currSize;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return currSize == 0;
    }

    @Override
    public void clear() {
        currSize = 0;
        modiCount = 0;
        root = null;
    }

    @SuppressWarnings("unchecked")
    private class valIteratorHelper<Q> implements Iterator<Q>{  // adds each value to a generic array based on in order traversal, itrater then loops through this array
        Q[] vals;
        int pos;
        int index;
        int iterModiCount;

        public valIteratorHelper(){
            index = 0;
            pos = 0;
            iterModiCount = modiCount;
            vals = (Q[]) new Object[currSize];
            inOrderVal(root);
        }

        private void inOrderVal(Node<K,V> node){  // in order recursive helper, fills array with values from nodes in order.
            if(node != null){
                inOrderVal(node.leftChild);
                vals[index++] = (Q)node.value;
                inOrderVal(node.rightChild);
            }
        }

        @Override
        public boolean hasNext() {
            if(iterModiCount != modiCount)
                throw new ConcurrentModificationException();
            return pos < vals.length;
        }

        @Override
        public Q next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return vals[pos++];
        }
    }

    LinearList<K> keyList = new LinearList<>();
    private void inOrderKey(Node<K,V> node){  // key iterator helper, creates in order linear list so we dont have to worry about sorting
        if(node != null){
                inOrderKey(node.leftChild);
                keyList.addLast(node.key);
                inOrderKey(node.rightChild);
            }
        }


    @Override
    public Iterator keys() {  // creates a linearList of keys in order and uses LinearList's given iterator
        inOrderKey(root);
        return keyList.iterator();
    }

    @Override
    public Iterator values() {
        return new valIteratorHelper();
    }

}
