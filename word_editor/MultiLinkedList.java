import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import enigma.console.TextAttributes;
import enigma.core.Enigma;


public class MultiLinkedList {

	RowNode head;
	RowNode tail;
	public enigma.console.Console cn = Enigma.getConsole();
	public static TextAttributes classic = new TextAttributes(Color.white, Color.black);  //foreground, background color
	public static TextAttributes green = new TextAttributes(Color.green, Color.black);
	public static TextAttributes magenta = new TextAttributes(Color.white, Color.magenta);
	public static TextAttributes cyan = new TextAttributes(Color.black, Color.cyan);
	public static TextAttributes orange = new TextAttributes(Color.orange, Color.black);
	
	public MultiLinkedList() {
		head = null;
		tail = null;
	}

	
	//add a new line every time enter is pressed
	public void addRow(int dataToAdd) {		
		RowNode newNode = new RowNode(dataToAdd);		
		if(head == null && tail == null) {
			head = newNode;
			tail = newNode;
		}			
		else {
			RowNode temp = head;
			while(temp.getDown() != null)
				temp = temp.getDown();
			temp.setDown(newNode);
			newNode.setUp(temp);
			tail = newNode;
		}		
	}
	
	
	public int rowNum(int py) {//finds the number of lines in a paragraph
		int count = 0;
		int count2 = 0;
		int value = 1;
		RowNode temp = head;
		
		if(head == null)
			System.out.println("List is empty.");
		else {			
			while(count < py) {
				ItemNode temp2 = temp.getRight();
				if(py-count == 1) {
					while(temp2 != null) {
						count2++;
						temp2 = temp2.getNext();
					}
				}				
				temp = temp.getDown();
				count++;
			}
		}		
		value += count2 / 60;		
		return value;
	}
	
	
	
	//selected harfi döndürüyor
	public char letter(int x, int y) {
		
		char letter = 0;
		int count = 1; 
		int count2 = 1;
		RowNode temp = head;

		if(head == null) {
			System.out.println("List is empty.");
		}
		else {
			while(count < y) {											
				temp = temp.getDown();
				count++;
			}	
			ItemNode temp2 = temp.getRight();
			letter = temp2.getItemName();
			while(count2 < x) {					
				count2++;
				temp2 = temp2.getNext();	
				letter = temp2.getItemName();
			}	
		}			
		return letter;
	}
	
	
	
	//delete row
	public void deleteRow(int py) {
		RowNode temp = head;
		RowNode previous = null;
		int count = 1;
		if(head == null) {
			System.out.println("List is empty.");
		}
		else {
			if(py == 1) {//delete the first line
				previous = temp;
				temp = temp.getDown();
				head = temp;
				temp.setRowId(previous.getRowId());
			}
			else {
				while(count < py) {
					previous = temp;
					temp = temp.getDown();
					count++;
				}
				if(temp.getDown() == null) {//delete from the end
					previous.setDown(null);
					temp.setUp(null);
				}
				else {//delete between 2 lines
					temp = temp.getDown();
					previous.setDown(temp);
					temp.setUp(previous);					
				}
			}
			temp = head;
			for(int i = 1; i <= rowSize(); i++) {					
				temp.setRowId(i);
				temp = temp.getDown();
			}
		}		
	}
	
	
	//insert method
	public void insert(int row, char item, int x) {
		ItemNode previous = null;	
		int count = 1;
		int count2 = 1;
		
		if(head == null)
			System.out.println("Add a row before word.");
		else {
			RowNode temp = head;
			
			while(count <= row) {
				
				if(row == temp.getRowId()) {
					ItemNode temp2 = temp.getRight();
					
					if(temp2 == null) {
						temp2 = new ItemNode(item);
						temp.setRight(temp2); 
					}					
					else {
						while(count2 < x) {							
							previous = temp2;
							temp2 = temp2.getNext();
							count2++;
						}			
						ItemNode newNode = new ItemNode(item);
						if(x == 1) {
							newNode.setNext(temp2);
							temp2.setPrevious(newNode);
							temp.setRight(newNode);
						}
						else {
							if(temp2 == null) {//add to the end							
								previous.setNext(newNode);
								newNode.setPrevious(previous);								
							}
							else {//insert between 2 lines
								previous.setNext(newNode);
								newNode.setPrevious(previous);
								temp2.setPrevious(newNode);
								newNode.setNext(temp2);
							}	
						}																
					}			
				}
				temp = temp.getDown();
				count++;
			}
		}
	}

	
	//overwrite method
	public void overwrite(int row, char item, int x ) {
		ItemNode previous = null;	
		int count = 1;
		int count2 = 1;
		//x = x+1;
		
		if(head == null)
			System.out.println("Add a row before word.");
		else {
			RowNode temp = head;
			
			while(count <= row) {
				
				if(row == temp.getRowId()) {

					ItemNode temp2 = temp.getRight();
					
					if(temp2 == null) {
						temp2 = new ItemNode(item);
						temp.setRight(temp2);

					}					
					else {
						while(count2 < x) {							
							previous = temp2;
							temp2 = temp2.getNext();
							count2++;
						}						
												
						temp2.setItemName(item);
					}			
				}
				temp = temp.getDown();
				count++;
			}
		}
	}
	
	
	public boolean control(int py) {//control of the enter in the line
		RowNode temp = head;
		int count = 1;
		
		if(head == null) {
			System.out.println("List is empty.");
			return false;
		}			
		else {
			while(count < py) {
				temp = temp.getDown();
				count++;
			}
			ItemNode temp2 = temp.getRight();
			if(temp2.getItemName() == '\n') {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	
	public void backSpace(int row, int x) {
		ItemNode previous = null;
		int count = 1;
		int count2 = 1;
		
		if(head == null)
			System.out.println("List is emty.");
		else {
			RowNode temp = head;	
			
			while(count <= row) {				
				if(row == temp.getRowId()) {//silinecek harfin satırı ve temp aynı ise
					
					ItemNode temp2 = temp.getRight();	
					
					if(x == 1) {//delete first
						previous = temp2;
						temp2 = temp2.getNext();
						temp.setRight(temp2);
					}
					else {
						while(count2 < x) {
							previous = temp2;
							temp2 = temp2.getNext();
							
							count2++;
						}
						if(temp2.getNext() == null)//delete from end
							previous.setNext(null);
						else {//delete between 2 characters
							temp2 = temp2.getNext();
							previous.setNext(temp2);
							temp2.setPrevious(previous);
						}						
					}								
				}			
				temp = temp.getDown();
				count++;
			}
		}		
	}
	
	
			
	public void delete(int row, int x) {	
		ItemNode previous = null;
		int count = 1;
		int count2 = 1;
		
		if(head == null)
			System.out.println("List is emty.");
		else {
			RowNode temp = head;			
			while(count <= row) {				
				if(row == temp.getRowId()) {
					ItemNode temp2 = temp.getRight();
					
					if(x == 1) {//delete first
						temp2 = temp2.getNext();
						temp2.setPrevious(null);
						temp.setRight(temp2);					
					}
					else {
						while(count2 < x) {
							previous = temp2;
							temp2 = temp2.getNext();
							count2++;
						}
						if(temp2.getNext() == null)//delete from the end
							previous.setNext(null);
						else {//delete between 2 characters
							temp2 = temp2.getNext();
							previous.setNext(temp2);
							temp2.setPrevious(previous);
						}						
					}
				}			
				temp = temp.getDown();
				count++;
			}
		}	
	}
	
	
	
	public int[][] find(String selected) {
		String tempstring="";
		int count=0;
		int count2=0;
		int x=1;
		int y=1;
		int[][]cor =new int[1200][3];
		if(head==null) System.out.println("List is empty.");
		else {
			RowNode temp=head;
			while(temp!=null) {
				ItemNode temp2=temp.getRight();
				while(temp2!=null) {
					tempstring=tempstring+temp2.getItemName();
					count++;
					if(count==selected.length()) {
						
						if(tempstring.equalsIgnoreCase(selected)) {
							cor[count2][0]=(x-count+1)%61;
							cor[count2][1]=x%61;
							cor[count2][2]=y;
							count2++;
							tempstring="";
							count=0;
						}
						else {
							tempstring=tempstring.substring(1,tempstring.length());
							count--;
						}
					}
					
					temp2=temp2.getNext();
					
					x++;
					if(x%61==0) {
						y++;
						x=1;
					}
				}
				
				x=1;
				y++;
				temp=temp.getDown();
				
			}
			
		}
		return cor;
	}
	
	
	
	public void coloredDisplay(int[][] cor) {
		CengEditor.cleanScreen();
		int listcounter=0;
		int rowcount=1;
		int count=1;
		
		if(head==null)System.out.println("List is empty.");
		else {
			RowNode temp=head;
			while(temp!=null) {
				ItemNode temp2=temp.getRight();
				while(temp2!=null) {
					
					if(count==cor[listcounter][0]&&rowcount==cor[listcounter][2]) {
						boolean flag=true;
						while(count<=cor[listcounter][1]&&temp2!=null) {
							cn.getTextWindow().setCursorPosition(count,rowcount);
							cn.setTextAttributes(orange);
							System.out.print(temp2.getItemName());
							if(temp2.getItemName()=='\n')flag=false;
							temp2=temp2.getNext();
							
							count++;
							if(count%61==0) {
								count=1;
								rowcount++;
							}
						}
						if(flag)listcounter++;
							
						
						
					}
					else {
						cn.setTextAttributes(classic);
						cn.getTextWindow().setCursorPosition(count,rowcount);
						System.out.print(temp2.getItemName());
						temp2=temp2.getNext();
						count++;
						if(count%61==0) {
							count=1;
							rowcount++;
						}
					}
				}
				count=1;
				rowcount++;
				temp=temp.getDown();
			}
		}
	}
	
	public int[] next(int count,int[][] cor) {
		int[] ret=new int[3];
		
		ret[0]=cor[count][0];
		ret[1]=cor[count][1];
		ret[2]=cor[count][2];
		CengEditor.cleanScreen();
		
		int countx=1;
		int county=1;
		if(head==null)System.out.println("List is empty.");
		else {
			RowNode temp=head;
			while(temp!=null) {
				ItemNode temp2=temp.getRight();
				while(temp2!=null) {
					if(cor[count][0]==countx&&cor[count][2]==county) {
						while(countx<=cor[count][1]&&temp2!=null) {
							cn.getTextWindow().setCursorPosition(countx,county);
	        				cn.setTextAttributes(orange);
	                        System.out.print(temp2.getItemName());
	                        temp2=temp2.getNext();
	                        countx++;
	                        if(countx%61==0) {
								countx=1;
								county++;
							}
						}
						
					}
					else {
						cn.setTextAttributes(classic);
						cn.getTextWindow().setCursorPosition(countx,county);
						System.out.print(temp2.getItemName());
						temp2=temp2.getNext();
						
						countx++;
						if(countx%61==0) {
							countx=1;
							county++;
						}
					}
					
				}
				county++;
				countx=1;
			 temp=temp.getDown();
			}
		}
		return ret;
	}
	
	
	public void replace(int[] ret,String x) {
		CengEditor.cleanScreen();
    	
		int countx=1;
		int county=1;
		if (head==null)System.out.println("List is empty.");
		else {
			RowNode temp=head;
			while(temp!=null) {
				
				ItemNode temp2=temp.getRight();
				while(temp2!=null) {
					if(countx==ret[0]&&county==ret[2]) {
						ItemNode temp3=null;
						if(ret[0]==1) {
							 temp3=temp2;
						
						}
						else {
							temp3=temp2.getPrevious();

						}
						
						while(countx<=ret[1]&&temp2!=null) {
							temp2=temp2.getNext();
							countx++;
						}
						for (int i=0;i<x.length();i++) {
							ItemNode a =new ItemNode(x.charAt(i));
							a.setPrevious(temp3);
							temp3.setNext(a);
							temp3=temp3.getNext();
							
						}
						temp3.setNext(temp2);
						temp2.setPrevious(temp3);
					}
					else {
						temp2=temp2.getNext();
						countx++;
					}
					
					
				}
				
				countx=1;
				county++;
				temp=temp.getDown();
			}
		}
		display();
	}
	
	
	//standard display method	
	public void display()  {

		CengEditor.cleanScreen();
        int count = 1;
        int count2 = 1;
        if(head == null)
            System.out.println("List is empty.");
        else {
            RowNode temp = head;
            while(temp != null) {
                ItemNode temp2 = temp.getRight();

                while(temp2 != null) {
                    cn.getTextWindow().setCursorPosition(count2, count);

                    if(count2 % 60 == 0) {
                        count++;
                        count2 = 0;
                    }
                    System.out.print(temp2.getItemName());
                    temp2 = temp2.getNext();
                    count2++;
                }
                temp = temp.getDown();
                count2 = 1;
                count++;
            }
        }
    }
	
	
		/*
	//kelimenin alt satıra geçmesi için
	public void display() {
	
		CengEditor.cleanScreen();
        int count = 1;
        int count2 = 1;
        String str = "";
        boolean flag = false;
        int value = 0;
        
        if(head == null)
            System.out.println("List is empty.");
        else {
            RowNode temp = head;
            while(temp != null) {
                ItemNode temp2 = temp.getRight();
                
                while(temp2 != null) {
                    cn.getTextWindow().setCursorPosition(count2, count);
                    str += Character.toString(temp2.getItemName());
                    
                    if(count2 % 60 == 0 && value != 0) {                       
                        if(temp2.getItemName() != ' ') {
                        	for(int i = count2 - str.length(); i <= 60; i++) {
                        		cn.getTextWindow().setCursorPosition(i, count);
                        		System.out.print(" ");
                        		
                        	}
                        	flag = true;
                        	count++;
                        	count2 = 1;
                        	cn.getTextWindow().setCursorPosition(count2, count);
                        	System.out.print(str);
                        	count2 += str.length();
                        	cn.getTextWindow().setCursorPosition(count2, count);
                        }
                        else {                        	
                        	count++;
                            count2 = 0;
                        }
                    }
                    else if(count2 % 60 == 0 && value == 0){
                    	count2 = 0;
                        count++;
                    }
                    if(temp2.getItemName() == ' ') {
                    	str = "";     
                    	value++;
                    }          
                    if(flag == false) {
                    	System.out.print(temp2.getItemName());
                    	count2++;
                    }                    
                    temp2 = temp2.getNext();                    
                    flag = false;
                }
                temp = temp.getDown();
                count2 = 1;
                count++;
            }
        }
        if(value > 0) {
        	cn.getTextWindow().setCursorPosition(count2 + str.length(), count - 1);
        }
	}
	
	*/
	
	public void SelectionDisplay(int startX, int startY, int endX) {
		
		CengEditor.cleanScreen();
		int count = 1;
        int count2 = 1;
        int count3 = 1;
        int countRow = 1;
        
        if(head == null)
            System.out.println("List is empty.");
        else {
        	RowNode temp = head;
        	while(temp != null) {
        		count3 = 1;
        		ItemNode temp2 = temp.getRight();
        		
        		if(startY == countRow) {
        			if(startX == 1) {//ilk harften itibaren ise  
            			while(count2 < endX) {//seçili kısımın renkli yazdırılması
            				cn.getTextWindow().setCursorPosition(count3, count);
            				cn.setTextAttributes(cyan);
                            System.out.print(temp2.getItemName());
                            temp2 = temp2.getNext();
                            count2++;count3++;
                            
                            if(count3 % 61 == 0) {
                                count++; count3 = 1;
                            }
            			}       			
            			while(temp2 != null) {//seçili kısımdan sonra kalan kısmın renkli yazdırılması
            				cn.getTextWindow().setCursorPosition(count3, count);
            				cn.setTextAttributes(classic);
                            System.out.print(temp2.getItemName());
                            temp2 = temp2.getNext();
                            count2++;count3++;
                            
                            if(count3 % 61 == 0) {
                                count++; count3 = 1;
                            }
            			}             			
            		}
            		else {//en baştan değil ise
            			
            			while(count2 < startX) {
            				cn.getTextWindow().setCursorPosition(count3, count);
            				cn.setTextAttributes(classic);
                            System.out.print(temp2.getItemName());
                            temp2 = temp2.getNext();
                            count2++;count3++;
                            
                            if(count3 % 61 == 0) {
                                count++; count3 = 1;
                            }
            			}
            			while(startX <= count2 && count2 < endX) {
            				cn.getTextWindow().setCursorPosition(count3, count);
            				cn.setTextAttributes(cyan);
                            System.out.print(temp2.getItemName());
                            temp2 = temp2.getNext();
                            count2++;count3++;
                            
                            if(count3 % 61 == 0) {
                                count++; count3 = 1;
                            }
            			}
            			while(temp2 != null) {//seçili kısımdan sonra kalan kısmın renkli yazdırılması
            				cn.getTextWindow().setCursorPosition(count3, count);
            				cn.setTextAttributes(classic);
                            System.out.print(temp2.getItemName());
                            temp2 = temp2.getNext();
                            count2++;count3++;
                            
                            if(count3 % 61 == 0) {
                                count++; count3 = 1;
                            }
            			} 
            		}
        		}
        		
        		else {
        			while(temp2 != null) {//seçili kısımdan sonra kalan kısmın renkli yazdırılması
        				cn.getTextWindow().setCursorPosition(count3, count);
        				cn.setTextAttributes(classic);
                        System.out.print(temp2.getItemName());
                        temp2 = temp2.getNext();
                        count3++;
                        
                        if(count3 % 61 == 0) {
                            count++; count3 = 1;
                        }
        			}  
        		}       		       		        		        		
        		count++;
        		temp = temp.getDown();
        		countRow++;
        	}        	
        }				
	}
				
	
	//load operation display method
	public void LoadDisplay() throws InterruptedException {
		cn.setTextAttributes(green);
		CengEditor.cleanScreen();
        int count = 1;
        int count2 = 1;
        if(head == null)
            System.out.println("List is empty.");
        else {
            RowNode temp = head;
            while(temp != null) {
                ItemNode temp2 = temp.getRight();

                while(temp2 != null) {
                    cn.getTextWindow().setCursorPosition(count2, count);

                    if(count2 % 60 == 0) {
                        count++;
                        count2 = 0;
                    }
                    Thread.sleep(60); 
                    System.out.print(temp2.getItemName());
                    temp2 = temp2.getNext();
                    count2++;
                }
                temp = temp.getDown();
                count2 = 1;
                count++;
            }
        }
        cn.setTextAttributes(classic);
    }
	
	
	
	
	public void alignment(int px,int py,int py2,int px2) {
		CengEditor.cleanScreen();
        int count = 1;
        int count2 = 1;
        if(head == null)
            System.out.println("List is empty.");
        else {
        	for(int k = 1; k<=py2;k++) {
        	if(px2-(61*k)==61) {
        		cn.getTextWindow().setCursorPosition(0, k);
        		 RowNode temp = head;
                 while(temp != null) {
                     ItemNode temp2 = temp.getRight();

                     while(temp2 != null) {
                         cn.getTextWindow().setCursorPosition(count2, count);

                         if(count2 % 60 == 0) {
                             count++;
                             count2 = 0;
                         }
                         System.out.print(temp2.getItemName());
                         temp2 = temp2.getNext();
                         count2++;
                     }
                     temp = temp.getDown();
                     count2 = 1;
                     count++;
                 }
        	}
        	if(px<62&&py==1){		
        		cn.getTextWindow().setCursorPosition(62-px, py);
        		int j = 0;
        		for(int i = 1; i<=62-px;i++) {
        		cn.getTextWindow().setCursorPosition(1, py);
        		cn.getTextWindow().output(" ");
        		}
        		
        		RowNode temp = head;
                while(temp != null) {
                    ItemNode temp2 = temp.getRight();

                    while(temp2 != null) {
                        cn.getTextWindow().setCursorPosition(62-px+j, py);
                        j++;

                        if(count2 % 60 == 0) {
                            count++;
                            count2 = 0;
                        }
                        System.out.print(temp2.getItemName());
                        temp2 = temp2.getNext();
                        count2++;
                    }
                    temp = temp.getDown();
                    count2 = 1;
                    count++;
                }
        	}
        	else if(px2-(60*(k))<60) {
        			int rowssize = size(py2)%61;
        			int c = size(py2)/61;
        			c = c+k;
        			if(c==py) {
        			for(int e = 1; e<=c;e++) {
            		if(c==e) {
            			int l = -2 ;
                		cn.getTextWindow().setCursorPosition(1, e);
                		for(int i = 1; i<=61-rowssize;i++) { 
                		cn.getTextWindow().output(" ");
                		}
                		RowNode temp = head;
                        while(temp != null) {
                            ItemNode temp2 = temp.getRight();
                            
                            while(62-rowssize+l!=61) {
                                cn.getTextWindow().setCursorPosition(62-rowssize+l, e);
                                l++;
                                
                                if(count2 % 60 == 0) {
                                    count++;
                                    count2 = 0;
                                }
                                System.out.print(temp2.getItemName());
                                temp2 = temp2.getNext();
                                count2++;
                            }
                            temp = temp.getDown();
                            count2 = 1;
                            count++;
                        }
            		}
            		else {
            	        if(head == null)
            	            System.out.println("List is empty.");
            	        else {
            	            RowNode temp = head;
            	            while(temp != null) {
            	                ItemNode temp2 = temp.getRight();

            	                while(temp2 != null) {
            	                    cn.getTextWindow().setCursorPosition(count2, count);

            	                    if(count2 % 60 == 0) {
            	                        count++;
            	                        count2 = 0;
            	                    }
            	                    System.out.print(temp2.getItemName());
            	                    temp2 = temp2.getNext();
            	                    count2++;
            	                }
            	                temp = temp.getDown();
            	                count2 = 1;
            	                count++;
            	            }
            	        }
            		}
        				}
        		}
        			else {
                		RowNode temp = head;
                        while(temp != null) {
                            ItemNode temp2 = temp.getRight();
                            cn.getTextWindow().setCursorPosition(0, k);
                            while(temp2 != null) {
                                if(count2 % 60 == 0) {
                                    count++;
                                    count2 = 0;
                                }
                                System.out.print(temp2.getItemName());
                                temp2 = temp2.getNext();
                                count2++;
                            }
                            temp = temp.getDown();
                            count2 = 1;
                            count++;
                        }
        			}
        		}
        	}
        }
      }	
	
	
	
	
	//to find the line length
	public int size(int py){
		RowNode x = null;
		int count1 = 1;
		int count2 = 0;
		if(head == null)
			System.out.println("List is empty.");
		else {
			RowNode temp = head;
			while(temp != null) {
				if(count1==py) {
					x=temp;
					break;
				}
				temp = temp.getDown();				
				count1++;			
			}
			ItemNode temp2 = temp.getRight();
			while(temp2 != null) {
				if(temp2.getItemName() == '\n')//to not count the enter key
					break;
				else
					count2++;				
				temp2 = temp2.getNext();
			}			
		}
		x.setSize(count2);
		return count2;
	}	
	
	
	//how many lines are in a paragraph		
	public int howManyRow(int x) {
		int a = x / 60;		
		return a+1;
	}
	

	//load method
	public String load(MultiLinkedList mll, int row) throws IOException {
		File file = new File("text.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
		String line = null;
		String rt="";
		line = reader.readLine();
		int count = 1;
		int a=1;
		int px=1;
		while(line != null) {	
			if(a!=1) {
				row++;
			}
			px=1;
			if(row != 1)
				mll.addRow(row);
			
			for (int i = 1; i < line.length() + 1; i++){
				if(count < 60) {
					mll.insert(row, line.charAt(i-1), i);
					count++;
					px++;
				}							
			}
			line = reader.readLine();
			count = 1;
			a++;
		}
		rt = rt+String.valueOf(row)+";"+String.valueOf(px);
		reader.close();
		return rt;
	}

	
	
	//save method
	public void save() throws IOException {	
		BufferedWriter writer = new BufferedWriter(new FileWriter("text.txt"));
		int count=0;
		String text = "";
		if(head == null)
			System.out.println("List is empty.");
		else {
			RowNode temp = head;
			while(temp != null) {
				ItemNode temp2 = temp.getRight();
				
				while(temp2 != null) {
					if(count%60==0&&count!=0) {
						text=text+"\n";
					}
					text += temp2.getItemName();
					temp2 = temp2.getNext();
					
					count++;
				}				
				writer.write(text);				
				text = "";
				temp = temp.getDown();			
				count=0;
			}
		}
		writer.close();
	}
	

	
	//returns how many rows
	public int rowSize() {
		int count = 0;
		if(head == null) {
			System.out.println("List is empty.");
		}
		else {
			RowNode temp = head;
			while(temp != null) {
				count++;
				temp = temp.getDown();
			}
		}			
		return count;
	}
	
	
	
	//related to cursor control
	public boolean check(int row, int x) {
		ItemNode previous = null;
		boolean flag = true;
		int count = 1;
		int count2 = 1;
		
		if(head == null)
			System.out.println("List is emty.");
		else {
			RowNode temp = head;	
			
			while(count <= row) {				
				if(row == temp.getRowId()) {
					
					ItemNode temp2 = temp.getRight();	
					
					if(temp2 == null) {
						flag = false;
						break;
					}
					else {
						while(count2 < x-1 && temp2 != null) {
							previous = temp2;
							temp2 = temp2.getNext();
							
							count2++;
						}
						if(temp2 == null){
							flag=false;
							break;
						}					
					}								
				}			
				temp = temp.getDown();
				count++;
			}
		}
		return flag;
	}
	
	
	
	public boolean checkRow(int row) {
		ItemNode previous = null;
		boolean flag=true;
		int count = 1;
		
		if(head == null)
			System.out.println("List is emty.");
		else {
			RowNode temp = head;	
			if (row==1) {
				flag=true;
			}
			else {
				while(count < row) {				
					temp = temp.getDown();
					count++;
			    }
				if (temp==null) {
					flag=false;
				}
			}		
		}		
		return flag;
	}
	

	
}
