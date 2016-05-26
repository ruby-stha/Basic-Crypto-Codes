
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Ruby on 4/6/2016.
 */
//DES Encryption Algorithm Implemented in Java
public class DESImplementation {
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);

        System.out.println("Enter the key (in Hex): ");
        String key=input.nextLine();

        System.out.println("Enter the plain text (in Hex): ");
        String message=input.nextLine();

        DES des=new DES();
        String binaryKey=des.convertToBin(key);
        String binaryMsg=des.convertToBin(message);
        des.convertTo56BitAndGenerateSubKeys(binaryKey);
        System.out.println("Encryption in progress.....");
        System.out.println((des.encrypted(des.encodeMsg(binaryMsg))).toUpperCase());

    }
}

class DES{
    String [] subkeys=new String[16];
    int [] shiftVal={1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
    int v=0;

    String convertToBin(String val){
        String binVal="";
        for (int i=0; i<val.length();i++){
            int hexVal=Integer.parseInt(Character.toString(val.charAt(i)), 16);
            String initial=fourBitBin(hexVal);
            binVal+=initial;
        }
        return  binVal;
    }

    String fourBitBin(int decimal){
        String initial=Integer.toBinaryString(decimal);
        if (initial.length()<4){
            int loop=4-initial.length();
            for (int j=0; j<loop; j++){
                initial=0+initial;
            }
        }
        return initial;
    }

    void convertTo56BitAndGenerateSubKeys(String bin){
        int[] PC1={57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55,47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};
        String newBinVal=pick(bin, PC1);
        String C=newBinVal.substring(0, 28);
        String D=newBinVal.substring(28, newBinVal.length());
        generate16Subkeys(C,D);
    }

    void generate16Subkeys(String C, String D){
        int [] PC2={14, 17, 11, 24, 1, 5, 3, 28, 15, 6,21,10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

        for (int j=0; j<16; j++){
            C=shift(C, shiftVal[v]);
            D=shift(D, shiftVal[v]);
            v++;
            String CD=C+D;
            String k=pick(CD, PC2);
            subkeys[j]=k;
        }
    }

    String shift(String val, int shiftby){
        val=val.substring(shiftby, val.length())+val.substring(0, shiftby);
        return val;
    }

    String encodeMsg(String msg){
        int [] IP={58, 50, 42, 34, 26, 18, 10, 2

                , 60, 52, 44, 36, 28, 20, 12, 4

                , 62, 54, 46, 38, 30, 22, 14, 6

                , 64, 56, 48, 40, 32, 24, 16, 8

                , 57, 49, 41, 33, 25, 17, 9, 1

                , 59, 51, 43, 35, 27, 19, 11, 3

                , 61, 53, 45, 37, 29, 21, 13, 5

                , 63, 55, 47, 39, 31, 23, 15, 7};
        return pick(msg, IP);
    }

    String LnRn(String msg){
        String[] L=new String[17];
        String[] R=new String[17];
        L[0]=msg.substring(0, 32);
        R[0]=msg.substring(32, msg.length());
        for (int i=1; i<=16; i++){
            L[i]=R[i-1];
            R[i]=carryOutXOR(L[i-1], functionF(subkeys[i-1], EOfR(R[i - 1])));
        }
        return R[16]+L[16];
    }

    String encrypted(String msg){
        int [] IPMinus1={40,8,48,16,56,24,64,32

                ,39,7,47,15,55,23,63,31

                ,38,6,46,14,54,22,62,30

                ,37,5,45,13,53,21,61,29

                ,36,4,44,12,52,20,60,28

                ,35,3,43,11,51,19,59,27

                ,34,2,42,10,50,18,58,26

                ,33,1,41,9,49,17,57,25};
        String intdEncryptedVal= pick(LnRn(msg), IPMinus1);
        String encryptedVal="";
        int ln=intdEncryptedVal.length();
        int indx=0;
        while (ln>0){
            encryptedVal+=Integer.toHexString(Integer.parseInt(intdEncryptedVal.substring(indx, indx+4),2));
            indx+=4;
            ln-=4;
        }
        return encryptedVal;
    }



    String EOfR(String R){
        int [] E={32, 1, 2, 3, 4, 5,

                4, 5, 6, 7, 8, 9,

                8, 9, 10, 11, 12, 13,

                12, 13, 14, 15, 16, 17,

                16, 17, 18, 19, 20, 21,

                20, 21, 22, 23, 24, 25,

                24, 25, 26, 27, 28, 29,

                28, 29, 30, 31, 32, 1};
        return pick(R, E);
    }

    String pick(String msg, int[] arr){
        String val="";
        for (int i=0; i<arr.length; i++){
            val+=msg.charAt(arr[i]-1);
        }
        return val;
    }

    String functionF(String kn, String Rn){
        Map sbox=new HashMap();
        sbox.put(1, new int[][]{{14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},{0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},{4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},{15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}});
        sbox.put(2, new int[][]{{15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},{3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},{0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},{13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0,5, 14, 9}});
        sbox.put(3, new int[][]{{10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},{13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},{13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},{1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}});
        sbox.put(4, new int[][]{{7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},{13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},{10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},{3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}});
        sbox.put(5, new int[][]{{2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},{14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6}, {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14}, {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}});
        sbox.put(6, new int[][]{{12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},{10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},{9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},{4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}});
        sbox.put(7, new int[][]{{4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},{13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},{1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},{6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}});
        sbox.put(8, new int[][]{{13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},{1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},{7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},{2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}});

        int [] P={16,7,20,21,
                29,12,28,17,
                1,15,23,26,
                5,18,31,10,
                2,8,24,14,
                32,27,3,9,
                19,13,30,6,
                22,11,4,25};

        int ind=0, row, column;
        String funcVal="";
        int [][] x;
        String val=carryOutXOR(kn, Rn);
        String initialVal="";
        for (int i=1;i<=8; i++){
            initialVal=val.substring(ind, ind+6);
            ind+=6;
            column=Integer.parseInt(initialVal.substring(1,5), 2);
            row=Integer.parseInt(String.valueOf(initialVal.charAt(0))+String.valueOf(initialVal.charAt(5)), 2);
            x=(int[][])sbox.get(i);
            funcVal+=fourBitBin(x[row][column]);
        }

        return pick(funcVal, P);
    }


    String carryOutXOR(String v1, String v2){
        String val="";
        for (int i=0; i<v1.length(); i++){
            val+=v1.charAt(i)^v2.charAt(i);
        }
        return val;
    }

}
