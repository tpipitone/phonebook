/* NAME THOMAS PIPITONE
RID 82460781
CSSC4068
 */

public class PhoneNumber implements Comparable<PhoneNumber> {
    String phoneNumber;
    // Constructor.  Creates a new PhoneNumber instance.  The parameter
    // is a phone number in the form xxx-xxx-xxxx, which is area code -
    // prefix - number.  The phone number must be validated, and an
    // IllegalArgumentException thrown if it is invalid.

    public PhoneNumber(String pNum){
        phoneNumber = pNum;
        verify(pNum);  //verifies the num is xxx-xxx-xxxx
    }

    // Follows the specifications of the Comparable Interface.
    public int compareTo(PhoneNumber n){
        return phoneNumber.compareTo(n.toString());
    }

    // Returns an int representing the hashCode of the PhoneNumber.
    public int hashCode(){
        return phoneNumber.hashCode();
    }

    // Private method to validate the Phone Number.  Should be called
    // from the constructor.
    private void verify(String n){  //splits into a 3 array at "-" should be three elements of correct length
        String[] splitNum = n.split("-");
        if( (splitNum.length == 3)  && (splitNum[0].length() == 3) && (splitNum[1].length() == 3) && (splitNum[2].length() == 4))
            return;
        throw new IllegalArgumentException();
    }

    // Returns the area code of the Phone Number.
    public String getAreaCode(){
        return phoneNumber.substring(0,2);
    }

    // Returns the prefix of the Phone Number.
    public String getPrefix(){
        return phoneNumber.substring(4,6);
    }

    // Returns the last four digits of the number.
    public String getNumber(){
        return phoneNumber.substring(8,11);
    }

    // Returns the Phone Number.
    public String toString(){
        return phoneNumber;
    }
}
