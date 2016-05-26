import java.util.Scanner;

/**
 * Created by Ruby on 3/10/2016.
 */
public class Vernam {

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);

        System.out.print("Enter key: ");
        String key=in.nextLine();

        System.out.print("Enter msg: ");
        String msg=in.nextLine();

        System.out.println("Encrypting.....");
        System.out.print("The encrypted text: " + getEncrypted(msg, key));

    }

    static String getEncrypted(String plaintext, String key){
       String alphabets="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int decimal1, decimal2, XORResult;
        String binary1, binary2, encryptedText="";

        plaintext=plaintext.replaceAll(" ","").toUpperCase();
        key=key.toUpperCase();
        if (key.length()<plaintext.length()){
            key=manageKeyLength(key, plaintext.length());
        }

        for (int i=0; i<plaintext.length(); i++){
            decimal1=alphabets.indexOf(plaintext.charAt(i));
            binary1=Integer.toBinaryString(decimal1);
            if (binary1.length()<5){ binary1=increaseLength(binary1);}

            decimal2=alphabets.indexOf(key.charAt(i));
            binary2=Integer.toBinaryString(decimal2);
            if (binary2.length()<5){ binary2=increaseLength(binary2);}

            XORResult=Integer.parseInt(performXOR(binary1,binary2), 2);

            if (XORResult>25){
                XORResult=XORResult%26;
            }
            encryptedText+=alphabets.charAt(XORResult);
        }
       return encryptedText;
    }

    static String increaseLength(String binary){
            int diff=5-binary.length();
            for (int i=0; i<diff; i++){
                binary=0+ binary;
            }
            return binary;
    }

    static String performXOR(String binary1, String binary2){
        String newBinary="";
        for (int i=0; i<5; i++){
            if (binary1.charAt(i)==binary2.charAt(i)){
                newBinary=newBinary+0;
            }else{
                newBinary=newBinary+1;
            }
        }
        return newBinary;
    }

    static String manageKeyLength(String key, int reqLen){
        int val=reqLen-key.length();
        int j=0;
        for (int i=val; i>0; i--){
            key+=key.charAt(j++);
            if (j==key.length()-1){
                j=0;
            }
        }
        return key;
    }
}
