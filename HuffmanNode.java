import java.util.Comparator;

public class HuffmanNode extends Object implements Comparable<HuffmanNode> {
	
	//a field that stores the number of times the character occurs
	private int numOccur;
	//a field that stores the character value of the node
	private Character c;
	//a field that stores the left child of the node	
	private HuffmanNode left;
	//a field that stores the right child of the node	
	private HuffmanNode right;
		
		/**
		 * Default Constructor
		 * @param numOccur number of occurrences a character has in the node
		 * @param c the character value in the node
		 * @param left child of the node
		 * @param right child of the node
		 * */
		public HuffmanNode( int numOccur, Character c, HuffmanNode left, HuffmanNode right) {
			this.numOccur = numOccur;
			this.c = c;
			this.left = left;
			this.right = right;
		}
		
		
		/**
		 * A method that returns the character inside the node if it is empty it returns null
		 * @return character value of the node
		 * */
		public Character inChar() {
			return c; 
		}

		/**
		 * A method that gives the frequency of the amount of times the character appears in the tree
		 * @return the number of times the character value occurs
		 * */
		public int frequency() {
			return numOccur;
		}
		
		/**
		 * Getter method for the left child of the node
		 * @return left child node
		 * */
		public HuffmanNode getLeft() {
			return left;
		}
		
		/**
		 * Setter method for the left child of the node
		 * @param newLeft the new left child node
		 * */
		public void setLeft(HuffmanNode newLeft) {
			left = newLeft;
		}
		
		/**
		 * Getter method for the right child of the node
		 * @return right child node
		 * */
		public HuffmanNode getRight() {
			return right;
		}
		
		/**
		 * Setter method for the right child of the node
		 * @param newRight the new right child node
		 * */
		public void setRight(HuffmanNode newRight) {
			right = newRight;
		}
		
		/**
		 * The compareTo method to order nodes by frequency 
		 * @param node HuffmanNode I am comparing this other HuffmanNode to
		 * @return < 0 if this HuffmanNode is smaller, > 0 if the HuffmanNode is bigger
		 */
	    public int compareTo(HuffmanNode node) {
			return this.frequency() - node.frequency();
	    }
		
	    /**
	     * Return a Comparator that compares two HuffmanNodes by their frequency
	     * @return a comparator that compares by frequency
	     */
	 
	    public static Comparator<HuffmanNode> compareByFrequency() {
	      return (h1, h2) -> ((h1.frequency() - h2.frequency()));
	    }

}
	


	
	

