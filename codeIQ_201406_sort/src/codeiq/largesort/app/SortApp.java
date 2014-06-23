package codeiq.largesort.app;

import java.io.File;

import codeiq.largesort.util.Sort;

public class SortApp {

	static final String INPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\input.txt";
	static final String OUTPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\output_1.txt";
	
	//static final String INPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\input-special.txt";
	//static final String OUTPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\output-special_1.txt";
	
	//static final String INPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\random_large.txt";
	//static final String OUTPUT_FILE = "D:\\SelfProject\\git\\codeiq_raftel\\questions\\01\\random_large_sorted.txt";
		
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
