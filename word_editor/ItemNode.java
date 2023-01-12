
public class ItemNode {

	private char ItemName;
	private ItemNode next;
	private ItemNode previous;
	private int ItemID;
	
	public ItemNode(char dataToAdd) {
		ItemName = dataToAdd;
		next = null;
		previous = null;
	}
	
	public char getItemName() {return ItemName;}
	public void setItemName(char itemName) {ItemName = itemName;}
	public ItemNode getNext() {return next;}
	public void setNext(ItemNode next) {this.next = next;}
	public ItemNode getPrevious() {return previous;}
	public void setPrevious(ItemNode previous) {this.previous = previous;}
	
}
