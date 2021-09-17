/**
 * @author Lance Choong
 *
 */

import java.io.*;
import java.util.*;

/**
 * HuffmanCompressor class which compresses a text file into encoded inputs 
 * Console should give a list of frequencies and the codes for each character as well as
 * the amount of bits saved and if the program finished
 * */
public class HuffmanCompressor {

	//field for input file's path 
	private String inputFile;
	//field for output file's path
	private String outputFile;
	
	/**
	 * ArrayList that represents the Huffman Tree nodes 
	 * I used ArrayLists instead LinkedLists because ArrayLists have instant element access 
	 * and ArrayLists can resize themselves automatically.
	 * */	
	private ArrayList<HuffmanNode> huffmanList = new ArrayList<HuffmanNode>();
	
	//HashMap that keeps the number of occurrences of each character
	private HashMap<Character, Integer> occurTable = new HashMap<Character,Integer>();
	
	//HashMap that maps the characters to their encoded outputs
	private HashMap<Character, String> encodedMap = new HashMap<Character, String>();
	
	
	/*
	 * Default constructor
	 * @param inputFile's name 
	 * @param outputFile's name after it has been encoded
	 */
	public HuffmanCompressor(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}
	
	/**
	 * Method that scans the input text file to make a list of Huffman nodes and matches the 
	 * amount of occurrences to each character in a table (hashmap) which stores the occurrences. 
	 * @throws IOException
	 * */
	public void scanAndSave() throws IOException {
		
		//checks if the input file exists if not throw an exception if it cant be found
		 File input = new File(inputFile);
			if(input.exists() != true) {
				throw new IOException("File does not exist!");
				
			}
			
			BufferedReader inputCharacters = new BufferedReader(new FileReader(input));
			
			//current index that the loop is working on
			int index; 
			//traversal variable for the while loop 
			char currentChar;		
			
			while((index = inputCharacters.read()) != -1) {
				//moves the loop to next character 
				currentChar = (char)index;
						
				//if the table does not contain the character yet add it into the table with the frequency of 1
				if(occurTable.containsKey(currentChar) == false) {
					occurTable.put(currentChar, 1);
					
				} //else add the frequency of the current character by 1
				else {
					occurTable.put(currentChar, occurTable.get(currentChar) + 1);
					
				}
			}
			
			inputCharacters.close();
			
			//makes a array containing all keys in the table 
			Character[] keyList = occurTable.keySet().toArray(new Character[0]);
			
			//puts the keys and frequencies into an ArrayList
			for(int j = 0; j < keyList.length; j++){
				huffmanList.add(j, new HuffmanNode(occurTable.get(keyList[j]), keyList[j], null, null));  
			}

			/**Sorts the 'nodes' in the ArrayList from lowest to highest according to frequency
			 * The comparator being used is of type HuffmanNode and a lambda expression is used to make the comparator
			 * (an anonymous class can be used as well but I chose to use a lambda expression)
			 */
			Collections.sort(huffmanList, ((HuffmanNode h1, HuffmanNode h2) -> h1.compareTo(h2)));				
		
	}
	
	
	/**
	 * Method that merges the smallest HuffmanNodes to construct the Huffman Tree. 
	 * Since the Huffman ArrayList should be sorted after scanning we can assume that the values are sorted lowest to highest
	 * */
	public void mergeAndBuildTree() {
		
		//while the list has at least 1 pair of nodes to merge 
		while(huffmanList.size() > 1) {
			
			//removes the first 2 nodes which are the 2 smallest nodes since the ArrayList is sorted
			HuffmanNode h1 = huffmanList.remove(0);
			HuffmanNode h2 = huffmanList.remove(0);
			//combine the first 2 nodes since they are the smallest and add it back into the end of the list
			HuffmanNode parent = new HuffmanNode(h1.frequency() + h2.frequency(),null, h1, h2);
			huffmanList.add(parent);
		}
		
	}
	
	/**
	 * Method which traverses the Huffman tree to obtain the encoding for each character in the tree 
	 * @param currentNode current node which the tree needs to traverse through
	 * @param currEncodedNum current encoded combination of numbers which the node is at
	 * */
	public void huffTraversalDown(HuffmanNode currentNode, String currEncodedNum) {
		//String to keep encoded number for each character 
		String encodedNum = currEncodedNum;
		
		
		/**adds a character if there is no children*/
		if(currentNode.getLeft() == null && currentNode.getRight() == null) {
			encodedMap.put(currentNode.inChar(), encodedNum);
			
		//recursively encodes all parts of the tree	
		}else {
			if (currentNode.getLeft() != null) {
				huffTraversalDown(currentNode.getLeft(), encodedNum + "0");
			
		}
		if (currentNode.getRight() != null) {
			huffTraversalDown(currentNode.getRight(), encodedNum + "1");
			
		}
		
		}
	}
	
	
	
	/**
	 * Method which makes the HuffmanEncodings for each character and prints encodings for all characters. 
	 * @throws FileNotFoundException 
	 * */
	public void huffmanEncoder() throws FileNotFoundException{
		
		//traverses down to get all the encoded versions of the characters
		huffTraversalDown(huffmanList.get(0), "");
		
		//checks if the inputFile exists and if not throw and exception that it was not found
			File input = new File(inputFile);
				if (input.exists() != true) {
					throw new FileNotFoundException("File cannot be found!");
				}
			
			//loops through all ASCII characters 
			for(int i = 0; i <= 127; i++) {
				
				Character current = ((char)i);
				char normChars = ((char)i);
				//if it is a regular alphabetical character print out how many times it occurs and how it is encoded
				if(i >= 32 && i < 127) {
					System.out.println(current + " Frequency: " + occurTable.get(current) + " Code : " + encodedMap.get(normChars));
				}
				//else it is a special character and we must use the escapeSpecialCharacter method to get the code for it
				else {
					
				System.out.println(current + " Frequency: " + occurTable.get(current) + " Code :" + HuffmanCompressor.escapeSpecialCharacter(current.toString()));
				
				}
			}
	}
	
	
	/**
	 * Method that outputs encoded text into a text file and computes and shows savings
	 * @throws IOException
	 * */
	public void outputCodedTextAndSavings(){
		
			try {

				//a temporary variable to access the input file
				File input = new File(inputFile);
				//a temporary variable to access the output file
				File output = new File(outputFile);
				
				
				//reader to read the inputs to encode
				BufferedReader bReader = new BufferedReader(new FileReader(input));
				//writer that writes the encoded inputs onto the output file
				BufferedWriter bWriter = new BufferedWriter(new FileWriter(output));
				
				//if an output file does not exist make a new one 
				if(output.exists() != true) {
					output.createNewFile();
				}
				
				
			
				//current character that the loop is working on 
				int currChar;
				//counter that counts how many bits would have been used before the compression
				int befEncodeCount = 0;
				//counter that counts how many bits used after compression
				int aftEncodeCount = 0;
				
				while ((currChar = bReader.read()) != -1) {
					//moves the loop to next character 
					char c = ((char) currChar);
					
					//each character before being encoded will take 8 bits
					befEncodeCount += 8;
					/**the amount of bits used in the encoded file will depend on how many bits are stored in the encodedMap which
					 * contains the code values for the characters
					 */
					aftEncodeCount += String.valueOf(encodedMap.get(c)).length(); 
					
					//buffered writer writes the encoded output onto the output text file
					bWriter.write(encodedMap.get(c) + "\n");
					
				}
				//prints out the amount of bits saved in the console
				System.out.println(" Encoding has saved " + (befEncodeCount - aftEncodeCount) + " bits!");
				
				
				bReader.close();
				bWriter.close();
				
			} catch (IOException e) {
				
				System.out.println("Please make an output file!");
			}
		
		
		
	}
	
	

	
	/**
	 * (TA given code we were given to avoid escape special characters, credit to Neo Huang))
	 * Method that adds characters normally to a string builder and escapes special characters 
	 * @param x current string that the method is working on
	 * @return a string version of 
	 * */
	public static String escapeSpecialCharacter(String x) {
		  StringBuilder sb = new StringBuilder();
		  for (char c : x.toCharArray()) {
		    if (c >= 32 && c < 127) 
		    	sb.append(c);
		    else sb.append(" [0x" + Integer.toOctalString(c) + "]");
		  }
		  return sb.toString();
		}
	
	
	/**
	 * Method which scans the input file, builds the Huffman tree and encodes the input text file  
	 * @param inputFileName input file's name
	 * @param outputFileName output file's name
	 * @return if the method completed 
	 * @throws IOException 
	 * */
	public static String huffmanCoder(String inputFileName, String outputFileName) throws IOException {
		
		HuffmanCompressor compressor = new HuffmanCompressor(inputFileName, outputFileName);
		
		try {
			compressor.scanAndSave();
		} catch(IOException e) {
			System.out.print("File not found!");
		}
		
		//build the tree if we scanned and saved our inputs
		compressor.mergeAndBuildTree();
		
		//make encoded outputs
		compressor.huffmanEncoder();

		//write encoded outputs onto a text file and output the savings 
		compressor.outputCodedTextAndSavings();
		
		
		return "finished";
	}

	/**
	 * Main method which runs huffmanCoder which compresses the file using Huffman encoding
	 * Please take note the pathfile names is needed to run the program properly you can find the 
	 * file location of the txt files by right clicking on the files and copying and adding 
	 * \\inputFileName for the input file and \\outputFileName for the output file at the end 
	 * e.g my file pathnames were for the input and output respectively
	 * C:\\Users\\lance\\OneDrive\\Documents\\mobydick\\MobyDick.txt
	 * C:\\Users\\lance\\OneDrive\\Documents\\mobydick\\MobyDickCompressed.txt
	 * @param args the file pathnames for input and output file
	 */
	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.out.print("Only 2 file names can be entered! Please put an input file name and an output file's name");
		}
		else
			try {
				System.out.print(huffmanCoder(args[0], args[1]));
				
			} catch (IOException e) {
				
				System.out.print("Files not found try again!");
			}

	}

}

