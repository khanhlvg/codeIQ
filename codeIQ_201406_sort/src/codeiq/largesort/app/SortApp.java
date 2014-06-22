package codeiq.largesort.app;

import java.io.File;

import codeiq.largesort.util.Sort;

public class SortApp {

	static final String INPUT_FILE = "D:\\SelfProject\\CodeIQ\\test data set\\input.txt";
	static final String OUTPUT_FILE = "D:\\SelfProject\\CodeIQ\\test data set\\output.txt";
	
	//static final String INPUT_FILE = "D:\\SelfProject\\CodeIQ\\test data set\\random_large.txt";
	//static final String OUTPUT_FILE = "D:\\SelfProject\\CodeIQ\\test data set\\random_large.txt_sorted.txt";
		
	public static void main(String[] args) {
		
		/*
		//pause to start resource monitoring
		System.out.println("Press Enter to continue");  
		try{System.in.read();}  
		catch(Exception e){}*/  
		
		long tStart = System.currentTimeMillis();
		
		try {
			Sort obj = new Sort();
			File input = new File(INPUT_FILE);
			File output = new File(OUTPUT_FILE);
			
			obj.executeWithUnique(input,output);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long tEnd = System.currentTimeMillis();
		System.out.println("*** Total time elapsed = " + (tEnd - tStart) + "ms." );
				
	}

}
