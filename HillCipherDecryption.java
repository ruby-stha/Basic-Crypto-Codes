import java.util.Scanner;

/**
 * Created by Ruby on 3/17/2016.
 */
public class HillCipherDecryption {
	
    public static void main(String[] args) {
        Decrypt decrypt=new Decrypt();
        //Try for key: fird
        Scanner in=new Scanner(System.in);
        System.out.print("Enter key: ");
        String key=in.nextLine().toUpperCase();

        System.out.print("Enter encrypted message: ");
        String encrypted=in.nextLine().toUpperCase();

        if (key.length()>4){
            decrypt.setSizeAndInitializeVars(3);
        }else{
            System.out.print("Enter key matrix size you want (2 and 3 allowed): ");
            int s=in.nextInt();
            if (s>3 || s<2){
                System.out.println("Invalid key size----------");
                return;
            }else{
                decrypt.setSizeAndInitializeVars(s);
            }
        }
        decrypt.carryOutDecryption(key, encrypted);

    }
}

class Decrypt{
    private int size;
    private String alphabets="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int [][]keyMatrix;
    private int [][]adj;
    private int [][]KInv;

    void carryOutDecryption(String key, String encrypted){
        key=manageKeySize(key);
        encrypted=manageMsgSize(encrypted);
        createKeyMatrix(keyMatrix, key);
        int determinant=findDeterminant(keyMatrix);
        int determinantInv=determinantInverse(determinant);
        System.out.println("-------Determinant Inverse: ---------\n" + determinantInv);
        findAdjoint(keyMatrix, adj);
        System.out.println("-------ADJOINT---------");
        displayMatrix(adj);
        getKInverse(adj,determinantInv, KInv);
        System.out.println("-------K-INVERSE = Determinant Inverse * Adjoint---------");
        displayMatrix(KInv);
        System.out.println("\n\nDecrypting......\n\nDecrypted Text: " + getDecryptedText(encrypted));

    }

    String manageKeySize(String key){
        if (key.length()<size*size){
            int diff=size*size-key.length();
            for (int i=0; i<diff; i++){
                key+=alphabets.charAt(i);
            }
        }
        return key;
    }

    String manageMsgSize(String msg){
        //changing msg into proper length as per matrix size, by adding X
        if (msg.length()%size!=0){
            int diff=Math.abs(size - msg.length() % size);
            for (int i=0; i<diff; i++){
                msg+='X';
            }
        }
        return msg;
    }

    int findDeterminant(int[][]matrix){
        int det=0;
        if (size==2){
            det=matrix[0][0]*matrix[1][1]-matrix[0][1]*matrix[1][0];
        }else if (size==3){
            int [][]temp=new int[size][size+2];
            int k;
            for (int i=0; i<size; i++){
                for (int j=0; j<size+2; j++){
                    if (j>size-1){
                        k=j-size;
                    }else {
                        k=j;
                    }
                    temp[i][j]=matrix[i][k];
                }
            }

            for(int i=0; i<3; i++){
                int val=1;
                for (int j=0; j<size; j++){
                    val*=temp[j][j+i];
                }
                det+=val;
            }
            for(int i=size-1; i<size+2; i++){
                int val=1;
                for (int j=0; j<size; j++){
                    val*=temp[j][i-j];
                }
                det-=val;
            }
        }
        det=det % 26;
        return (det<0)?det+26:det;
    }

    int determinantInverse(int determinant){
        int x=1;
        while(true){
            if ((determinant*x)%26==1){
                return x;
            }
            x++;
        }
    }

    void findAdjoint(int[][] arr, int[][] adj){
        if (size==2){
            adj[0][0]=arr[1][1];
            adj[1][1]=arr[0][0];
            adj[0][1]=(-arr[0][1])%26;
            adj[0][1]=adj[0][1]<0?adj[0][1]+26:adj[0][1];
            adj[1][0]=(-arr[1][0])%26;
            adj[1][0]=adj[1][0]<0?adj[1][0]+26:adj[1][0];
        }else if (size==3){
            adj[0][0]=((arr[1][1]*arr[2][2]-arr[2][1]*arr[1][2])%26);
            adj[0][1]=(-(arr[1][0]*arr[2][2]-arr[2][0]*arr[1][2]))%26;
            adj[0][2]=(arr[1][0]*arr[2][1]-arr[2][0]*arr[1][1])%26;
            adj[1][0]=(-(arr[0][1]*arr[2][2]-arr[2][1]*arr[0][2]))%26;
            adj[1][1]=(arr[0][0]*arr[2][2]-arr[2][0]*arr[0][2])%26;
            adj[1][2]=(-(arr[0][0]*arr[2][1]-arr[2][0]*arr[0][1]))%26;
            adj[2][0]=(arr[0][1]*arr[1][2]-arr[1][1]*arr[0][2])%26;
            adj[2][1]=(-(arr[0][0]*arr[1][2]-arr[1][0]*arr[0][2]))%26;
            adj[2][2]=(arr[0][0]*arr[1][1]-arr[1][0]*arr[0][1])%26;

            for (int i=0; i<size; i++){
                for (int j=0; j<size; j++){
                    if (adj[i][j]<0){
                        adj[i][j]+=26;
                    }
                }
            }
            getTranspose(adj);
        }

    }

    void getTranspose(int [][] adj){
        int temp;
        for (int i=0; i<size; i++){
            for (int j=i+1; j<size; j++){
                if (i!=j){
                    temp=adj[i][j];
                    adj[i][j]=adj[j][i];
                    adj[j][i]=temp;
                }
            }
        }
    }

    void displayMatrix(int [][] mat){
        for (int i=0; i< size; i++){
            for (int j=0; j<size; j++){
                System.out.print(mat[i][j] + "\t");
            }
            System.out.println();
        }
    }

    void getKInverse(int [][] mat,int determinantInv, int [][] KInv ){
        for (int i=0; i< size; i++){
            for (int j=0; j<size; j++){
                KInv[i][j]=(mat[i][j]*determinantInv)%26;
            }
        }
    }

    void createKeyMatrix(int[][]keyMatrix, String key){
        //Creating matrix of key
        int val=0;
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                keyMatrix[i][j]=alphabets.indexOf(key.charAt(val++));
            }
        }
    }

    String getDecryptedText(String msg){
        msg=msg.toUpperCase();
        int msgLen=msg.length()-size;
        int mul=0;
        String decrypted="";
        int val=0;
        //Encrypting 'size' number of characters at a time till all characters are encrypted by matrix multiplication
        while (msgLen>=0){
            String ch=msg.substring(val, val+size);
            for (int i=0; i<size; i++){
                mul=0;
                for (int j=0; j<size; j++){
                    mul+= KInv[i][j]*alphabets.indexOf(ch.charAt(j));
                }
                mul=mul%26;
                decrypted+=alphabets.charAt(mul);
            }
            val+=size;
            msgLen-=size;
        }
        return decrypted;
    }

    void setSizeAndInitializeVars(int size){
        this.size=size;
        keyMatrix=new int[size][size];
        adj=new int[size][size];
        KInv=new int[size][size];
    }

}