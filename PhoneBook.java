/* NAME THOMAS PIPITONE
RID 82460781
CSSC4068
 */

import data_structures.DictionaryADT;
import data_structures.Hashtable;
import java.util.Iterator;
import java.io.*;

public class PhoneBook {
    int maxSize;
    Hashtable dictionary;

    public PhoneBook(int maxSize){
        this.maxSize = maxSize;
        dictionary = new Hashtable<>(this.maxSize);
    }

    // Reads PhoneBook data from a text file and loads the data into
    // the PhoneBook.  Data is in the form "key=value" where a phoneNumber
    // is the key and a name in the format "Last, First" is the value.
    public void load(String filename){
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader((filename)));  //uses buffered reader to read the file line by line
            String kvPair = reader.readLine();
            while( kvPair != null){
                String[] kvSpl = kvPair.split("=");  // splits into a string array
                PhoneNumber num = new PhoneNumber(kvSpl[0]); // asigns the pnone number to Object PhoneNumber
                dictionary.add(num,kvSpl[1]); // adds to dictionary (phoneNum, name)
                kvPair = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e){
            System.out.println("IO Exception @ file reader");
        }
    }

    // Returns the name associated with the given PhoneNumber, if it is
    // in the PhoneBook, null if it is not.
    public String numberLookup(PhoneNumber number){
        if(dictionary.getValue(number) == null)
            return null;
        return dictionary.getValue(number).toString();
    }

    // Returns the PhoneNumber associated with the given name value.
    // There may be duplicate values, return the first one found.
    // Return null if the name is not in the PhoneBook.
    public PhoneNumber nameLookup(String name){
        return (PhoneNumber)dictionary.getKey(name);
    }

    // Adds a new PhoneNumber = name pair to the PhoneBook.  All
    // names should be in the form "Last, First".
    // Duplicate entries are *not* allowed.  Return true if the
    // insertion succeeds otherwise false (PhoneBook is full or
    // the new record is a duplicate).  Does not change the datafile on disk.
    public boolean addEntry(PhoneNumber number, String name){
        return dictionary.add(number,name);
    }

    // Deletes the record associated with the PhoneNumber if it is
    // in the PhoneBook.  Returns true if the number was found and
    // its record deleted, otherwise false.  Does not change the datafile on disk.
    public boolean deleteEntry(PhoneNumber number){
        return dictionary.delete(number);
    }

    // Prints a directory of all PhoneNumbers with their associated
    // names, in sorted order (ordered by PhoneNumber).
    public void printAll(){ //creates an instance of hashtable iterater keys()
        for (Iterator it = dictionary.keys(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }

    // Prints all records with the given Area Code in ordered
    // sorted by PhoneNumber.
    public void printByAreaCode(String code){ //creates an instance of hashtable iterater keys(), substring to find first three chars
        for (Iterator it = dictionary.keys(); it.hasNext(); ) {
            System.out.println(it.next().toString().substring(0,2));
        }
    }

    // Prints all of the names in the directory, in sorted order (by name,
    // not by number).  There may be duplicates as these are the values.       
    public void printNames(){ //creates an instance of hashtable iterater keys()
        for (Iterator it = dictionary.values(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }
}