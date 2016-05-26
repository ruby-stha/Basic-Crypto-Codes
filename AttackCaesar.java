import java.sql.*;


/**
 * Created by Ruby on 2/22/2016.
 */
public class AttackCaesar {
    public static void main(String[] args) {
        Attack atk1=new Attack();
        atk1.calculateSi("KHOOR ZRUOG");
        System.out.println("Processing probable words...Please wait...");
        atk1.decrpt(atk1.findGreatestSiIndex());
    }
}

class Attack{

    private String plainTxt=null;
    private String decryptedTxt=null;
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String URL = "jdbc:mysql://localhost/dictionary";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    static Connection conn = null;
    static PreparedStatement stSQL=null;

    String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private double [] pVal={0.080, 0.015, 0.030, 0.040, 0.130, 0.020, 0.015, 0.060, 0.065, 0.005, 0.005, 0.035, 0.030, 0.070, 0.080, 0.020, 0.002, 0.065, 0.060, 0.090, 0.030, 0.010, 0.015, 0.005, 0.020, 0.002};
    private double [] siarr=new double[26];
    private double reqFreq;

    void calculateSi(String plainTxt){
        this.plainTxt=plainTxt;
        int intermediateVal=0;
        int lenWithoutSpace=plainTxt.replaceAll(" ", "").length();
        reqFreq=1/(double)lenWithoutSpace;
        System.out.println(reqFreq);
        for (int i=0; i<26; i++){
            for (int j=0; j<plainTxt.length(); j++) {
                if (plainTxt.charAt(j)!=' '){
                    intermediateVal=characters.indexOf(plainTxt.charAt(j))-i;
                    siarr[i]+=reqFreq*pVal[(intermediateVal+26)%26];
                }
            }
        }
    }

    int findGreatestSiIndex(){
        int grtIndex=0;
        double greatest=siarr[0];
        for (int i=0; i<siarr.length; i++){
            if (siarr[i]>greatest){
                greatest=siarr[i];
                grtIndex=i;
            }
        }
        return grtIndex;
    }

    void decrpt(int index){
        StringBuilder decrypted=new StringBuilder(plainTxt);
        char ch;
        for (int i=0; i<plainTxt.length(); i++){
            ch= plainTxt.charAt(i);
            if (ch!=' '){
                decrypted.setCharAt(i, characters.charAt(((characters.indexOf(ch) - index) + 26) % 26));
            }
        }
        this.decryptedTxt=decrypted.toString();
        System.out.println(this.decryptedTxt);
        searchAvailability(this.decryptedTxt);
    }

    void searchAvailability(String decrypted){
        String [] words;
        words=decrypted.split(" ");
        boolean available=true;
        for (int i=0; i<words.length; i++){
            available=checkAvailability(words[i]);
            if (!available){
                break;
            }
        }
        if (!available){
            siarr[findGreatestSiIndex()]=0;
            decrpt(findGreatestSiIndex());
        }else{
            System.out.println("\nThe decrypted text is:" + this.decryptedTxt);
        }
    }

    boolean checkAvailability(String word){
        boolean avail=true;
        //Load Driver Class
        try {
            Class.forName(JDBC_DRIVER);
        }
        catch(ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            ex.printStackTrace();
            System.exit(1);
        }
        try{
            conn=DriverManager.getConnection(URL, USER, PASS);
            String str="select * from words where strings LIKE'"+ word.toLowerCase() + "%'";
            stSQL=conn.prepareStatement(str);
            ResultSet rec=stSQL.executeQuery();
            if (!rec.next()){
                avail=false;
            }else{
                avail=true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return avail;
    }


//    void getUniqueCh(String plainTxt){
//        plainTxt=plainTxt.replaceAll(" ", "");
//        int len=plainTxt.length();
//        uniqueCh=new char[len];
//        uniqueCh[0]=plainTxt.charAt(0);
//        for (int i=0; i<len; i++){
//            for (int j=0; j<uniqueCh.length; j++){
//                if ()
//            }
//        }
//    }
//
//   void findFrequency(String plainTxt){
//
//       int [] charCount=new int[len];
//       double [] freq=new double[len];
//
//       for (int i=0; i<len; i++){
//           for (int j=0; j<len; j++){
//               if (plainTxt.charAt(j)==plainTxt.charAt(i)){
//                   charCount[i]++;
//               }
//           }
//           freq[i]=(double)charCount[i]/len;
//           System.out.println(freq[i]);
//       }
//    }

}
