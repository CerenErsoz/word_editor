public class RowNode {

	public int rowId;
	private RowNode up;
	private ItemNode right;
	private RowNode down;
	public int size;
	
	public RowNode(int dataToAdd) {
		rowId = dataToAdd;		
		up = null;
		right = null;
		down = null;
	}

	
	public int getSize() {return size;}
	public void setSize(int size) {this.size = size;}
	public int getRowId() {return rowId;}
	public void setRowId(int rowId) {this.rowId = rowId;}
	public RowNode getDown() {return down;}
	public void setDown(RowNode down) {this.down = down;}
	public RowNode getUp() {return up;}
	public void setUp(RowNode up) {this.up = up;}
	public ItemNode getRight() {return right;}
	public void setRight(ItemNode right) {this.right = right;}
	
}
