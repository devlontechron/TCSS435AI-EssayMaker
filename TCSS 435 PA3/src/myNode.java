import java.util.LinkedList;

/**
 * simple node class to have an object that keeps track of the word, the count, and a pointer to an array list to hold other nodes
 * @author Devin
 *
 */
public class myNode {
	
	private String word;
	private int count;
	private LinkedList pointer;
	
	/**
	 * constructor
	 * @param NodeWord
	 * @param counter
	 * @param arr
	 */
	public myNode(String NodeWord, int counter, LinkedList arr){
		this.word = NodeWord;
		this.count = counter;
		this.pointer = arr;
	}
	
	/**
	 * over loaded constructor  (for the nested sub words that dont point to other nodes)
	 * @param NodeWord
	 * @param counter
	 */
	public myNode (String NodeWord, int counter){
		this.word = NodeWord;
		this.count = counter;
	}
	

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public LinkedList getPointer() {
		return pointer;
	}

	public void setPointer(LinkedList pointer) {
		this.pointer = pointer;
	}
	
	/**
	 * increments word count for this node
	 */
	public void plusCounter (){
		this.count = this.count + 1;
	}
	
	/**
	 * adds a new myNode to this nodes LinkedList
	 * @param node
	 */
	public void addNode(myNode node){
		pointer.add(node);
	}
	
	

}
