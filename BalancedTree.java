package data_structures;
import java.util.*;

/* NAME THOMAS PIPITONE
RID 82460781
CSSC4068

HELP CREDIT JAVADOCS / ORACLE
 */


public class BalancedTree<K extends Comparable<K>, V> implements DictionaryADT<K,V>{

    private TreeMap<K,V> BRtree;

    public BalancedTree() {
        BRtree = new TreeMap<>();
    }

    @Override
    public boolean contains(K key) {
        return BRtree.containsKey(key);
    }

    @Override
    public boolean add(K key, V value) { // uses treemap's put() method to add to it
        if(contains(key))
            return false;
        BRtree.put(key,value);
        return true;
    }

    @Override
    public boolean delete(K key) {
        BRtree.remove(key);
        return true;
    }

    @Override
    public V getValue(K key) {
        return BRtree.get(key);
    }

    @Override
    public K getKey(V value) {  // loops through the entire treeMap until the value is found, rets null if nto
        for(Map.Entry<K,V>node : BRtree.entrySet()){
            if(node.getValue().equals(value)){
                return node.getKey();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return BRtree.size();
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return BRtree.size() == 0;
    }

    @Override
    public void clear() {
        BRtree.clear();
    }


    @Override
    public Iterator keys() {
       Set keySet = BRtree.keySet();
       return keySet.iterator();
    }

    class valIteratorHelper implements Iterator<V>{  //creates a set of values and uses java sets given iterator to iterate through, credit oracle javadocs

        Set valSet;
        Iterator valIt;

        public valIteratorHelper(){
            valSet = BRtree.entrySet();
            valIt = valSet.iterator();
        }

        @Override
        public boolean hasNext() {
            while(valIt.hasNext()){
                return true;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public V next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            Map.Entry entry = (Map.Entry)valIt.next();
            return (V)entry.getValue();
        }
    }

    @Override
    public Iterator values() {
        return new valIteratorHelper();
    }


}
