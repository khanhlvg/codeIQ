package codeiq.largesort.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Sort {
    /**
     * `sort -u`相当の処理を行うメソッドです
     * 
     * @param input 入力ファイル
     * @param output 結果を書き出す出力ファイル
     * @throws IOException
     */
	
	// use only 80% of free memory for a chunk
	// as we use UTF8 for encoding, each character will take 4 bytes.
	//final long CHUNK_SIZE_LIMIT = (Runtime.getRuntime().freeMemory() / 4) * 8 / 10 ;
	final long CHUNK_SIZE_LIMIT = 20 ;
	final String TEMP_FILENAME_PATTERN = "sort_chunk";
	final String ENCODING = "UTF-8";
	
	//BOM character
	final String UTF8_BOM = "\uFEFF";
	
	// a subclass in charge of merging a list of sorted chunk files into one output file
	private class ChunkHolder {
		
		private List<String> chunkFileList;
		private Map<String,Scanner> chunkStreamReaderMap;
		
		private TreeMap<String,String> queue;
		
		private String outputFileName;
		private Writer outputWriter;
		
		//object constructor
		public ChunkHolder (List<String> chunkFileList, String outputFilename) throws IOException {
			//initialize chunk file list
			this.chunkFileList = new ArrayList<String>();
			this.chunkFileList.addAll(chunkFileList);
			this.outputFileName = outputFilename;
			
			//initialize necessary object to start merge sort
			initStreamReaderMap();
			initQueue();
			
			//create output stream
			initOutputStream();
		}
		
		//main method: merge sorted trunk into one output file
		public void mergeSortedTrunkIntoOutput() throws IOException {
			while (queue.size() > 0) {
				Map.Entry<String,String> qItem = queue.firstEntry();
				
				//write top item in queue to output file
				outputWriter.append(qItem.getKey()).append(System.getProperty("line.separator"));
				
				//remove that item and add new item
				queue.remove(qItem.getKey());
				Scanner sc = chunkStreamReaderMap.get(qItem.getValue());
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					if (!queue.containsKey(line)) {
						queue.put(line, qItem.getValue());
						break;
					}
				} 
			}
			
			//remove trunk files and close output write
			destroy();
		}
		
		// =========== class's sub-method ============
		private void initStreamReaderMap() throws IOException {
			chunkStreamReaderMap = new HashMap<String,Scanner>();
			for (String filename : chunkFileList) {
				FileInputStream inputStream = new FileInputStream(filename);
				Scanner sc = new Scanner(inputStream, ENCODING);
				chunkStreamReaderMap.put(filename,sc);
			}
		}
		
		private void initQueue() {
			queue = new TreeMap<String,String>();
			for (String filename : chunkFileList) {
				Scanner sc = chunkStreamReaderMap.get(filename);
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					if (!queue.containsKey(line)) {
						queue.put(line, filename);
						break;
					}
				}
			}
			//System.out.println("Queue size = " + queue.size());
		}
		
		private void initOutputStream() throws IOException {
			File file = new File(outputFileName);
			if (!file.exists()) {
				file.createNewFile();
			}
 
			outputWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), ENCODING));
		}
		
		private void destroy() throws IOException{
			// close output file as all process finished 
			outputWriter.close();
			
			//close all chunk scanner delete chunk files
			for (String filename : chunkFileList) {
				chunkStreamReaderMap.get(filename).close();
				File file = new File(filename);
				file.delete();
			}	
		}
	}
		
	void writeCollectionToFile(Collection<String> collection, File file) throws IOException {
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			Writer bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), ENCODING));
			for (String content : collection) {
				bw.append(content).append(System.getProperty("line.separator"));
			}
			bw.close();
			
	}
	
	List<String> splitLargeFileAndSort(String filename) throws IOException{
		FileInputStream inputStream = null;
		Scanner sc = null;
		
		//count total length of each chunk
		long totalLengthCount = 0;
		SortedSet<String> chunkContent = new TreeSet<String>();
		
		//list of chunk filenames
		List<String> ret = new ArrayList<String>();
		
		try {
		    inputStream = new FileInputStream(filename);
		    sc = new Scanner(inputStream, ENCODING);
		    
		    //handle UTF-8 BOM character
		    boolean firstLine = true;
		    
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        
		        //remove first character of first line if it is UTF-8 BOM character
		        if (firstLine) {
		        	if (line.startsWith(UTF8_BOM)) {
		                line = line.substring(1);
		            }
		        	firstLine = false;
		        }
		        
		        // add current line to chunk
		        if (!chunkContent.contains(line)) {
		        	totalLengthCount+= line.length();
		        	chunkContent.add(line);
		        }
		        
		        // if current chunk exceed memory limit or finished reading input file, 
		        // then export it to chunk file and reset chunk object
		        if ((totalLengthCount > CHUNK_SIZE_LIMIT)||(!sc.hasNextLine())) {
		        	//export chunk content to file
		        	File file = File.createTempFile(TEMP_FILENAME_PATTERN, ".tmp"); 
		        	writeCollectionToFile(chunkContent, file);
		        	ret.add(file.getAbsolutePath());
		        	
		        	//start new chunk
		        	chunkContent = new TreeSet<String>();
		        	totalLengthCount = 0;
		        	
		        	//logging
		        	System.out.println("Exported chunk file : " + file.getAbsolutePath());
		        }
		        
		    }
		    
		    // as Scanner suppresses exceptions, we have to throw it manually
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		    chunkContent = null;
		}
		
		return ret;
	}
	
    public void executeWithUnique(File input, File output) throws IOException{
    	//logging
    	System.out.println("Started creating chunk files...");
    	
        //split input file into multiple sorted chunk files
    	String filename = input.getAbsolutePath();
    	List<String> chunkFileList = splitLargeFileAndSort(filename);
    	
    	//logging
    	System.out.println("Total: " + chunkFileList.size() + " files");
    	System.out.println("Started merging...");
    	
    	//merge chunks to one output file
    	ChunkHolder chunkholder = new ChunkHolder(chunkFileList, output.getAbsolutePath());
    	chunkholder.mergeSortedTrunkIntoOutput();

    	//logging
    	System.out.println("Sort finished.");
    }
}
