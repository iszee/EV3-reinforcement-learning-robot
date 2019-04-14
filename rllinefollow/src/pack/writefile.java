package pack;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class writefile {
	private String path;
	private boolean append_to_file = false;

	public writefile(String file_path){
		path = file_path;
	}
	public writefile(String file_path,boolean append_value){
		path = file_path;
		append_to_file = append_value;
		File f = new File(file_path);
		if (f.exists()){
			f.delete();
		}
	}
	public void writeToFile(String textLine) throws IOException{
		FileWriter writer = new FileWriter(path,append_to_file);
		PrintWriter printline = new PrintWriter(writer);
		printline.printf("%s", textLine);
		printline.close();
		
	}
	
//	public java.io.File File(String file_path) {
//		File f = new File(file_path);
//		return f;
//		
//	}

}
