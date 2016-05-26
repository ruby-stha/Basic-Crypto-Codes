import java.util.Scanner;

/**
 * Created by Ruby on 3/20/2016.
 */
public class VigenereCipher {
    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);

        System.out.print("Enter key: ");
        String key=in.nextLine();

        System.out.print("Enter message: ");
        String msg=in.nextLine();

        System.out.print("The encrypted text: " + encrypt(msg, key));


    }

    static String encrypt(String msg, String key){
        String encrypted="";
        String alphabets="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int val=0;

        msg=msg.toUpperCase();
        key=key.replaceAll(" ","").toUpperCase();

        for (int i=0; i<msg.length(); i++){
            if (msg.charAt(i)!=' '){
                int index=(alphabets.indexOf(msg.charAt(i))+alphabets.indexOf(key.charAt(val)))%26;
                encrypted+=alphabets.charAt(index);
                if (val==key.length()-1)
                    val=0;
                else
                    val++;
            }else{
                encrypted+='#';
            }

        }
        return encrypted;
    }
}

