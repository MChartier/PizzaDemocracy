import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVGenerator {
	
	private CSVGenerator(){}
	
	public static void writeCSV(int[] array, String filename){	
		File file = new File(filename);
		try {
			FileWriter writer = new FileWriter(file);			
			writeToFile(array, writer);
			writer.flush();
			writer.close();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeCSV(int[][] array, String filename){
		try {
			FileWriter writer = new FileWriter(filename);
			for(int i = 0; i < array.length; i++){
				writeToFile(array[i], writer);
				writer.append("\n\r");
			}
			writer.flush();
			writer.close();
		}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void writeToFile(int[] array, FileWriter writer){			
		
		try {
			for(int i = 0; i < array.length; i++){
				writer.append(String.valueOf(array[i]));
				
				if(i + 1 != array.length)
					writer.append(',');
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}