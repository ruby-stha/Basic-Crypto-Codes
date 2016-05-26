import java.util.Scanner;

/**
 * Created by Ruby on 3/16/2016.
 */
public class HillCipherEncryption{
    public static void main(String[] args){

        String alphabets="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Scanner in=new Scanner(System.in);

        System.out.println("Enter key: ");
        String key=in.nextLine();

        System.out.println("Enter Message: ");
        String msg=in.nextLine();

        key=key.replaceAll(" ", "").toUpperCase();
        msg=msg.toUpperCase();

        System.out.println("Enter the size of Key Array: ");
        int size=in.nextInt();
        int[][] keyMatrix=new int[size][size];

        //changing key into proper length as per matrix size
        if (key.length()<size*size){
            int diff=size*size-key.length();
            for (int i=0; i<diff; i++){
                key+=alphabets.charAt(i);
            }
        }

        //changing msg into proper length as per matrix size, by adding X
        if (msg.length()%size!=0){
            int diff=Math.abs(size - msg.length() % size);
            for (int i=0; i<diff; i++){
                msg+='X';
            }
        }

        int val=0;

        //Creating matrix of key
        System.out.println("Matrix of Key:");
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                keyMatrix[i][j]=alphabets.indexOf(key.charAt(val++));
                System.out.print(keyMatrix[i][j] + "\t");
            }
            System.out.println();
        }

        String encrypted="";
        val=0;
        int msgLen=msg.length()-size;
        int mul=0;

        //Encrypting 'size' number of characters at a time till all characters are encrypted by matrix multiplication
        while (msgLen>=0){
            String ch=msg.substring(val, val+size);
            for (int i=0; i<size; i++){
                for (int j=0; j<size; j++){
                       mul+= keyMatrix[i][j]*alphabets.indexOf(ch.charAt(j));
                }
                mul=mul%26;
                encrypted+=alphabets.charAt(mul);
            }
            val+=size;
            msgLen-=size;
        }

        System.out.println("encrypted-----------" + encrypted);
    }
}
