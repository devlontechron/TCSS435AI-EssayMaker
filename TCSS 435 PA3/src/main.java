/*
 * Devin Durham
 * TCSS 435
 * Sakpal
 * PA3
 * 6/4/17
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;



/**
 * Program reads in specified txt files and splits them into words
 * those words are parsed using a trigram into a hash table, then an array list, then another nested array list
 * frequency of words and trigram are kept
 * production of a file in the printer() method reads the data structure and randomly selects a seed, 
 * -randomly selects a node in the array list, then chooses a word from the nested list based off of probability
 * @author Devin
 *
 */
public class main {

	static Hashtable<String, LinkedList> wordsHash = new Hashtable<String, LinkedList>();
	static LinkedList LL1 = new LinkedList();
	static LinkedList LL2 = new LinkedList();
	static LinkedList<myNode> LLholder = new LinkedList<myNode>();
	static StringBuffer sb = new StringBuffer();

	static int f = 0;

	public static void main(String[] args) throws IOException {

		System.out.println("Reading book 1");
		reader("../doyle-27.txt");
		System.out.println("Reading book 2");
		reader("../doyle-case-27.txt");
		System.out.println("Reading book 3");
		reader("../alice-27.txt");
		System.out.println("Reading book 4");
		reader("../london-call-27.txt");
		System.out.println("Reading book 5");
		reader("../melville-billy-27.txt");
		System.out.println("Reading book 6");
		reader("../twain-adventures-27.txt");
		System.out.println("Generating new story");
		
		//test();
		
		makeStory();
		System.out.println("Complete. GeneratedStory.txt has been made.");

	}

	/**
	 * reader method takes in a file and parses the file into words then inserts the words intot the data structure
	 * checks for words already in structure and increments counter
	 * makes new nodes and inserts into structure if new word is discovered 
	 * @param file path (type: .txt)
	 * @throws IOException
	 */
	public static void reader(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		StringBuilder sbb = new StringBuilder();
		String line = br.readLine();
		int t = 0;

		//reads the entire txt file into one large string to allow consistent trigram operations
		while (line != null) {
			sbb.append(line);
			line = br.readLine();
		}

		line = sbb.toString();

		//the text is changed to lower case, checks for periods to make them their own word, and trims white space
		line = line.toLowerCase();
		line = line.replace(".", " .");
		line = line.replaceAll("\\s{2,}", " ");

		//splits string into separate words into an array
		String[] BufferWords = line.split(" ");

		//reads each word and places into data structure
		for (int i = 0; BufferWords.length - 2 > i; i++) {
			if (!BufferWords[i].equals("") || !BufferWords[i].equals(" ")) {

				//checks if word is already present in hash table
				if (wordsHash.containsKey(BufferWords[i])) {
					LLholder = wordsHash.get(BufferWords[i]);
					myNode theOne = null;
					
					//checks if word+1 is already a sub word (increments word counter if true)
					for (int y = 0; y < LLholder.size() && theOne == null; y++) {
						myNode dummy = LLholder.get(y);
						if (dummy.getWord().equals(BufferWords[i + 1])) {
							theOne = dummy;
							theOne.plusCounter();
							LinkedList<myNode> dummyLL2 = theOne.getPointer();

							myNode dummyNode2 = null;
							myNode theOtherOne = null;
							for (int u = 0; u < dummyLL2.size() && theOtherOne == null; u++) {
								dummyNode2 = dummyLL2.get(u);
								
								//updates word count if nested sub word is found
								if (dummyNode2.getWord().equals(BufferWords[i + 2])) {
									dummyNode2.plusCounter();
									theOtherOne = dummyNode2;
								}
							}
							//creates a new nested subword node if does not exist 
							if (theOtherOne == null) {
								myNode DN2 = new myNode(BufferWords[i + 2], 1);
								theOne.addNode(DN2);
							}
						}

					}
					//creates a new subword node if does not exist 
					if (theOne == null) {

						LinkedList<myNode> newLL = new LinkedList<myNode>();
						myNode newNode2 = new myNode(BufferWords[i + 2], 1);
						newLL.add(newNode2);
						myNode newNode = new myNode(BufferWords[i + 1], 1, newLL);
						LLholder.add(newNode);
						wordsHash.put(BufferWords[i], LLholder);
					}

					//if word is not found in hash, make a new nested sub word, a new sub word, and a new hash entry
				} else {
					LinkedList<myNode> LLnew = new LinkedList<myNode>();
					LinkedList<myNode> LL2new = new LinkedList<myNode>();

					myNode N2holder = new myNode(BufferWords[i + 2], 1);

					LL2new.add(N2holder);

					myNode Nholder = new myNode(BufferWords[i + 1], 1, LL2new);

					LLnew.add(Nholder);

					wordsHash.put(BufferWords[i], LLnew);

				}

			}

		}
		br.close();

	}

	/**
	 * this actually generates the story based off of the data structure
	 * makes a file, randomly choose a seed word from the hash table
	 * calls printer to generate text
	 * writes text to file
	 */
	public static void makeStory() {
		File newFile = new File("GeneratedStory.txt");
		try {
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile);
			BufferedWriter bw = new BufferedWriter(fw);
			int t = 0;
			Object[] keys = wordsHash.keySet().toArray();
			String seed = (String) keys[new Random().nextInt(keys.length)];
			System.out.println("\nSEED: " + seed);
			sb.append(seed + " ");
			f++;
			printer(seed);
			System.out.println(sb.toString());
			bw.write(sb.toString());
			bw.flush();
			bw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	/**
	 * this is a simple test method to visually see the data structure after a txt file has been read and parsed
	 * includes hash root, subWord count, subWord word, & subWord nestedArray list
	 * also includes the nested subWord nodes and their word and count
	 */
	public static void test() {
		Object[] keys = wordsHash.keySet().toArray();
		for (int k = 0; k < keys.length; k++) {
			System.out.println("Root: " + keys[k]);

			LinkedList testLL = wordsHash.get(keys[k]);
			for (int p = 0; p < testLL.size(); p++) {
				myNode testN = (myNode) testLL.get(p);
				System.out.println("   Node:" + testN.getWord() + " " + testN.getCount());

				LinkedList testLL2 = testN.getPointer();
				for (int w = 0; w < testLL2.size(); w++) {
					myNode testN2 = (myNode) testLL2.get(w);
					System.out.println("      Node:" + testN2.getWord() + " " + testN2.getCount());
				}

			}
		}
	}

	/**
	 * printer takes in a key from the hash table and randomly selects it sub word then selects the nested subWord based off of probability
	 * generates 1000 words
	 * @param seed
	 */
	public static void printer(String seed) {
		while (f < 1000) {
			LinkedList<myNode> nextList = wordsHash.get(seed);
			myNode subWord = nextList.get(new Random().nextInt(nextList.size()));
			//appends word to string builder and increments word count f
			sb.append(subWord.getWord() + " ");
			f++;
			
			//gets the nested sub word from the subword list
			LinkedList<myNode> subList = subWord.getPointer();
			myNode currMax = subList.get(0);
			//compares the probability of nested word to subWord
			float currProb = (float) currMax.getCount() / subWord.getCount();

			//checks all other nested subWords in list to see for higher probability
			for (int q = 1; q < subList.size(); q++) {
				myNode newMax = subList.get(q);
				float newProb = (float) newMax.getCount() / subWord.getCount();
				if (newProb > currProb) {
					currProb = newProb;
					currMax = newMax;

					//in the event of a tie, randomly selects to stay with current word or switch to new word
				} else if (newProb == currProb) {
					int rando = new Random().nextInt(2);
					if (rando == 0) {
						currMax = newMax;
					}
				}
			}
			//checks the word count again and adds max probability word to string
			if (f < 1000) {
				sb.append(currMax.getWord() + " ");
				f++;
				printer(currMax.getWord());
			}
		}
	}
}
