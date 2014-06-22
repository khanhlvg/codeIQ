package codeiq.largesort.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class CreateLargeFile {

	static final String INPUT_FILE = "D:\\readme.txt";
	static final String OUTPUT_FILE = "D:\\random_large.txt";
	static final int LOOP_TIMES = 10000000;
	
	public static void duplicateFile() throws IOException{
		// create output stream
		File file = new File(OUTPUT_FILE);
		
	    if (!file.exists()) {
			file.createNewFile();
		}

		Writer bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));
		
		// create duplicate files
		FileInputStream inputStream;
		Scanner sc;
		
		for (int i=0;i<LOOP_TIMES;i++) {
			
			inputStream = new FileInputStream(INPUT_FILE);
		    sc = new Scanner(inputStream, "UTF8");
		    while (sc.hasNextLine()) {
		    	String line = sc.nextLine();
		    	bw.append(line).append("\n");
		    }
		    
		    sc.close();
	    
		}
		
		bw.close();

	}
	
	public static void randomMethod() throws IOException {
		// create output stream
		File file = new File(OUTPUT_FILE);
		
	    if (!file.exists()) {
			file.createNewFile();
		}

		Writer bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));
		
		//Random randomGenerator = new Random();
		
		for (int i=0; i<LOOP_TIMES; i++) {
			/*int rdmRepeat = randomGenerator.nextInt(10); 
			for (int j=0; j<rdmRepeat; j++) {
				bw.append(UUID.randomUUID().toString());
			}*/
			bw.append(UUID.randomUUID().toString());
			bw.append("\n");
			if ((i % (LOOP_TIMES/10)) == 0) {
				System.out.println((i*100/LOOP_TIMES) + "%");
			}
		}
		
		bw.close();

	}
	
	public static void main(String[] args) throws IOException {
		
		randomMethod();
		
	}

}
