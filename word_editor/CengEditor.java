import enigma.core.Enigma;
import enigma.event.TextMouseEvent;
import enigma.event.TextMouseListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import enigma.console.TextAttributes;
import java.awt.Color;


public class CengEditor {
   public static enigma.console.Console cn = Enigma.getConsole("CENG Editor",85,24,20); // col,row,fontsize,fonttype
   public enigma.console.TextWindow cnt = cn.getTextWindow();
   public static TextAttributes standard = new TextAttributes(Color.white, Color.black);  //foreground, background color
   public static TextAttributes red = new TextAttributes(Color.white, Color.red);  //foreground, background color
   
   public TextMouseListener tmlis; 
   public KeyListener klis; 
   MultiLinkedList mll = new MultiLinkedList();
   public int mode = 1;
   public String selected = ""; 

   // ------ Standard variables for keyboard and mouse 2 --------------------------
   public int keypr;   // key pressed?
   public int rkey;    // key   (for press/release)
   public int rkeymod;      // key modifiers
   public int capslock=0;   // 0:off    1:on
   // -----------------------------------------------------------------------------
   
   
   CengEditor() throws Exception {   // --- Contructor
                 
      // ------ Standard code for keyboard and mouse 2 -------- Do not change -----    
      klis=new KeyListener() {
         public void keyTyped(KeyEvent e) {}
         public void keyPressed(KeyEvent e) {
            if(keypr==0) {
               keypr=1;
               rkey=e.getKeyCode();
               rkeymod=e.getModifiersEx();
               if(rkey == KeyEvent.VK_CAPS_LOCK) {
                 if(capslock==0) capslock=1;
                 else capslock=0;
               }
            }
         }
         public void keyReleased(KeyEvent e) {}
      };
      cn.getTextWindow().addKeyListener(klis);
      // --------------------------------------------------------------------------
      
      int curtype;
      curtype = cnt.getCursorType();   // default:2 (invisible)       0-1:visible
      cnt.setCursorType(1);
      
      
      cn.setTextAttributes(standard);  
      readingFile("screen.txt");        
      
      boolean selection = false;
           
      int startX = 0;
      int startY = 0;
      int endX;
      int endY;
      int counter = 0;

      int nextcounter=0;
      String s=null;
      boolean flag = false;//In order to understand whether the previous action is to delete or not to write.
      int px=1, py=1, py2=1, px2=1, py3=1;
      mll.addRow(py2);//To begin with the first line must be added
      cnt.setCursorPosition(px, py);
      
      //--- main game loop ---
      while(true) {
         if(keypr==1) {// if keyboard button pressed
            if(rkey==KeyEvent.VK_LEFT) {
            	
            	counter = 0;//keeps track of which line the cursor is on
            	for(int i = 1; i < py2; i++) {
            		counter += mll.rowNum(i);
            	} 
            	counter = py - counter;
            	
            	if(px == 1 && counter != 1) {
            		py--; px = 61; cnt.setCursorPosition(px, py);
            	}
            	else if(px == 1 && counter == 1 && py2 > 1) {
            		py2 -= 2; py -= 2; px = 1;
            		px += (mll.size(py2) % 60);
            		cnt.setCursorPosition(px, py);            		
            	}
            	else if( px > 1){//normal left 
            		px--; cnt.setCursorPosition(px, py);
            	}            
            }
                       
            else if(rkey==KeyEvent.VK_RIGHT) {
            	
            	counter = 0;//keeps track of which line the cursor is on
            	for(int i = 1; i < py2; i++) {
            		counter += mll.rowNum(i);
            	} 
            	counter = py - counter;
            	
            	if(px % 61 == 0 && mll.rowNum(py2) > counter) {          		
            		if(selection == true) {             	
                		char temp = mll.letter(px + (counter - 1) * 60, py2);
                		selected += Character.toString(temp);         		
                 		endX = selected.length();
                    	endX += startX; 
                    	mll.SelectionDisplay(startX, startY, endX);                     	
                    	px = 2;
                		py++;                        	
                	} 
            		else if(selection == false) {
            			px = 1; py++;
            		}
            		cnt.setCursorPosition(px, py);
            	}
            	
            	else if(mll.rowNum(py2) == counter && mll.rowSize() > py2 && mll.size(py2) + 1 == (((counter -1) * 60) + px)) {
            		py += 2;
            		py2 += 2;
            		px = 1;
            		cnt.setCursorPosition(px, py);
            	}         	
            	else {//progress in a regular line
            		if(selection == true) {            			
                		char temp = mll.letter(px + (counter-1) * 60, py2);
                		selected += Character.toString(temp);           		
                 		endX = selected.length();
                    	endX += startX; 
                    	mll.SelectionDisplay(startX, startY, endX);                     	
                    	px++;                           	
                	}           	
                	else if(selection == false && mll.check(py2, px + 1 + (counter - 1) * 60)) {
                		px++;               		
                	}
            		cnt.setCursorPosition(px, py);                	
            	}
            }    
            
            else if(rkey == KeyEvent.VK_UP) {     
            	counter = 0;//keeps track of which line the cursor is on
            	for(int i = 1; i < py2; i++) {
            		counter += mll.rowNum(i);
            	} 
            	counter = py - counter;
            	
            	if(py==1) {/*to do nothing for first line*/}          	
            	else if(counter == 1 && 1 != py2) {
            		py2 -= 2; py -= 2; 
            		if(mll.size(py2) % 60 +1 < px) {
            			px = mll.size(py2) % 60 + 1;
            		}
            		cnt.setCursorPosition(px, py);
            	}
            	else {//normal up
            		py--;
            		cnt.setCursorPosition(px, py);
            	}
            }
            
            
            else if(rkey==KeyEvent.VK_DOWN) {    
           	
            	counter = 0;//keeps track of which line the cursor is on
            	for(int i = 1; i < py2; i++) {
            		counter += mll.rowNum(i);
            	} 
            	counter = py - counter;
            	
            	if(py == 20) {/*to do nothing for last line*/}
            	
            	else if(mll.size(py2) / 60  == counter && mll.size(py2) % 60 < px) {
            		py++;
            		px = mll.size(py2) % 60 + 1;
        			cnt.setCursorPosition(px, py);
            	}   
            	else if(mll.rowSize() > py2) {
            		if(mll.size(py2) / 60 + 1 == counter) {//in last line
            			py2 += 2;
                 	   py +=2; 
                 	   if(mll.size(py2) % 60 < px) {
                 		   px = mll.size(py2) % 60 + 1;
                 	   }
            		}
            		else {//not in last line
            			py++;
                		cnt.setCursorPosition(px, py);
            		}         	   
            	   cnt.setCursorPosition(px, py);
            	} 
            	else if(py2 == mll.rowSize() && counter < mll.rowNum(py2)){
            		py++;
            		cnt.setCursorPosition(px, py);
            	}
            }
                                    
            else if(rkey == KeyEvent.VK_ENTER && py < 20) {          	
            	px2 = 1;
            	py2++;
            	mll.addRow(py2);
            	mll.insert(py2,'\n', px2);           	
            	py = py + 2; py2++; counter = 1;
                mll.addRow(py2);
                px = 1; px2 = 1;
                cnt.setCursorPosition(px, py);                     
            }       
            
            else if(rkey==KeyEvent.VK_SPACE) { 
            	if(px < 61) {
            		mll.insert(py2, ' ', px + (counter - 1) * 60);
                	mll.display();
                	px++; px2++;
                	cnt.setCursorPosition(px, py);
            	}
            	else {
            		py++;  px = 1;  counter++;
            	}            	
            }          
           
            else if(rkey == KeyEvent.VK_BACK_SPACE) {
            	if(px == 1 && py == 1) {/*to do nothing*/}            	
            	else if(px > 1) {//delete any letter in a regular line
            		px--; px2--;
                    mll.backSpace(py2, px + (counter - 1) * 60);//It must be linked to py to delete intermediate paragraphs. 
                    mll.display();
                    cnt.setCursorPosition(px, py);
            	} 	
                else if(px == 1 && px2 != 1) {//line missing letters               	
                	if(px2 == 0) {
                		mll.deleteRow(py2);
            			py--; py2--;
                		mll.deleteRow(py2);
                		py--; py2--;
                	}
                	else {
                		py--; flag = true;
                    	px = 61; px2 = 61;
                        cnt.setCursorPosition(px, py);      
                        counter--;
                	}
            		px2 = mll.size(py2);
                    mll.display();
                    cnt.setCursorPosition(px, py);
                    counter = mll.rowNum(py2);
            	}
            	else if(py > 1 && counter > 1) {
            		px = 61; flag = true;
            		py--; counter--;
            		cnt.setCursorPosition(px, py);
            	}
            	else if(px == 1 && px2 == 1) {//deletion stage of a paragraph
            		if(mll.control(py2 - 1) == true) {
            			mll.deleteRow(py2);
            			py--; py2--;
                		mll.deleteRow(py2);
                		py--; py2--;
            		}           
            		px = ((mll.size(py2)) % 60) + 1;
            		px2 = mll.size(py2) + 1;
                    mll.display();
                    cnt.setCursorPosition(px, py);
                    counter = mll.rowNum(py2);
            	}
            }       
            else if(rkey == KeyEvent.VK_DELETE && px < 60) {            	
            	int size = mll.size(py2);             	
            	if(size == 0) {/*To do nothing*/}
            	else if(size >= px) {
            		if(counter > 1) {
            			px2--;
            			mll.delete(py2, px + ((counter - py - 1) * 60));
            			counter = mll.rowNum(py2);           			
            		}else {
            			px2--;
                    	mll.delete(py2, px + (counter - 1) * 60);
            		}         		
                    mll.display();
                    cnt.setCursorPosition(px, py);
            	}            	
            }      
                 
            else if(rkey == KeyEvent.VK_F1) {//Selection Start
            	if(!(selected.equals(""))) {
            		selected = "";
            	}
            	selection = true;
            	startX = px + (counter - 1) * 60; startY = py2;
            }
            else if(rkey == KeyEvent.VK_F2) {//Selection End	
            	selection = false;
            	endX = px + (counter - 1) * 60; endY = py2;
            }                        
            else if(rkey == KeyEvent.VK_F3) {//CUT	
            	
            	for(int i = 0; i < selected.length();i++) {
            		mll.delete(startY, startX);
            	}
            	px = startX;
            	py = startY;
            	mll.display();
            	cnt.setCursorPosition(px, py);
            }
            else if(rkey == KeyEvent.VK_F4) {//COPY   
            	if(selected.length() < 23) {
            		cn.getTextWindow().setCursorPosition(63, 18);
             		System.out.println("Copied: " + selected);    
             		cnt.setCursorPosition(px, py);
            	}
            	else {
            		cn.getTextWindow().setCursorPosition(63, 18);
             		System.out.println("Copied");    
             		cnt.setCursorPosition(px, py);
            	}
            }
            else if(rkey == KeyEvent.VK_F5) {//PASTE    
            	
            	for(int i = 0; i < selected.length();i++) {
            		char ch = selected.charAt(selected.length() - i - 1);
            		mll.insert(py2, ch, px + (counter - 1) * 60);
            	}
            	selected = "";
            	mll.display();
            	cn.getTextWindow().setCursorPosition(63, 18);
         		System.out.println("                            ");
         		cnt.setCursorPosition(px, py);
            }
            else if(rkey == KeyEvent.VK_F6) {//FIND	
            	cn.getTextWindow().setCursorPosition(63, 18);
            	System.out.print("Enter a word:");
            	Scanner scanner = new Scanner(System.in);
            	s = scanner.nextLine();
            	int[][] a = mll.find(s);
            	mll.coloredDisplay(a);
            	cnt.setCursorPosition(px, py);
            }
            else if(rkey == KeyEvent.VK_F7) {//REPLACE	
            	cn.setTextAttributes(standard);
                cn.getTextWindow().setCursorPosition(63, 18);
                System.out.print("Enter a word:");
                Scanner scanner =new Scanner(System.in);
                String x =scanner.nextLine();
                mll.replace(mll.next(nextcounter-1,mll.find(s)),x);
                px=(mll.size(py2)+1)%61;
                py+=(mll.size(py2)+1)/61;
            }
            else if(rkey == KeyEvent.VK_F8) {//NEXT
            	mll.next(nextcounter,mll.find(s));
                nextcounter++;
            }
            else if(rkey == KeyEvent.VK_F9) {//Align Left
                mll.display();
                cn.getTextWindow().setCursorPosition(73, 15);
                 cn.getTextWindow().output("Aligned to ");
                 cn.getTextWindow().setCursorPosition(73, 16);
                 cn.getTextWindow().output("the left");
                 cn.getTextWindow().setCursorPosition(px, py);
            }
            else if(rkey == KeyEvent.VK_F10) {//Justify
                mll.alignment(px, py,py2,px2);
               cn.getTextWindow().setCursorPosition(73, 15);
                cn.getTextWindow().output("Justified          ");
                cn.getTextWindow().setCursorPosition(73, 16);
                cn.getTextWindow().output("          ");
                cn.getTextWindow().setCursorPosition(60, py);
            }
            else if(rkey == KeyEvent.VK_F11) {//LOAD  
            	boolean f = false;
            	if(px == 1 && py == 1)
            		f = true;
            	
            	String str = mll.load(mll, py);
            	String[] a = str.split(";");
            	py = Integer.parseInt(a[0]);
            	px = Integer.parseInt(a[1]);
            	
            	if(f)
            		px2 = px; py2 = py;
            		
            	mll.LoadDisplay();           	
            	cn.setTextAttributes(red);
            	cn.getTextWindow().setCursorPosition(68, 18);
         		System.out.println("LOADED");
         		cn.setTextAttributes(standard);
         		Thread.sleep(1000); 
         		cn.getTextWindow().setCursorPosition(68, 18);
         		System.out.println("        ");
         		cnt.setCursorPosition(px, py);            	           	
            }          
            if(rkey == KeyEvent.VK_F12) {//SAVE     
            	mll.save();
            	cn.setTextAttributes(red);
            	cn.getTextWindow().setCursorPosition(68, 18);
         		System.out.println("SAVED");
         		cn.setTextAttributes(standard);
         		Thread.sleep(1000); 
         		cn.getTextWindow().setCursorPosition(68, 18);
         		System.out.println("      ");
         		cnt.setCursorPosition(px, py);
            }
            
            //mode.1-insert, mode.2-overwrite                      
            else if(rkey == KeyEvent.VK_INSERT) {
            	if(mode == 1) {//switch from insert to overwrite
            		cn.getTextWindow().setCursorPosition(68, 13);
            		System.out.println("Overwrite");
            		mode = 2;
            		cnt.setCursorPosition(px, py);
            	}
            	else {//switch from overwrite to insert
            		cn.getTextWindow().setCursorPosition(68, 13);
            		System.out.println("Insert   ");
            		mode = 1;
            		cnt.setCursorPosition(px, py);
            	}
            }
            
            else if(rkey== KeyEvent.VK_HOME) {//carriage return
            	px = 1;
            	cnt.setCursorPosition(px, py);
            	cn.getTextWindow().setCursorPosition(px, py);
            }
            
            else if(rkey== KeyEvent.VK_END) {//line break              	            	
            	int temp = 0;
            	for(int i = 1; i <= py2; i++) {
            		temp += mll.rowNum(i);
            	}  
            	if(temp > py) {
            		px = 61; flag = true;
            	}
            	else {                 	
                	px = mll.size(py2) - ((counter - 1) * 60)  + 1;  
            	}
            	cnt.setCursorPosition(px, py);
            	cn.getTextWindow().setCursorPosition(px, py);           	
            }
            else if(rkey== KeyEvent.VK_PAGE_UP) {//top of page
            	px = 1; py = 1;
            	cnt.setCursorPosition(px, py);
            	cn.getTextWindow().setCursorPosition(px, py);
            }
            else if(rkey== KeyEvent.VK_PAGE_DOWN) {//page break
            	py = 0;
            	for(int i = 1; i <= py2; i++) {
            		py += mll.rowNum(i);
            	}            	
            	px = mll.size(py2) - ((counter -1) * 60)  + 1;      	
            	cnt.setCursorPosition(px, py);
            	cn.getTextWindow().setCursorPosition(px, py);
            }
                        
            char rckey = (char)rkey;
            
            counter = 0;//paragrafın hangi satırında olduğunu tutar
        	for(int i = 1; i < py2; i++) {
        		counter += mll.rowNum(i);
        	} 
        	counter = py - counter;
            
            
            //        left          right          up            down
            if(rckey=='%' || rckey=='\'' || rckey=='&' || rckey=='(') {    //test without using VK (Virtual Keycode)
              cnt.setCursorPosition(px, py); 
            }
            else {
            	if(px == 61 && flag == false) {
            		px=1;
            		py++;
            		//py3++;
            		counter++;
            	}
            	if(rckey >= '0' && rckey <= '9') {         	  
            		if(mode == 1){//the part added to the multi list
            			mll.insert(py2, rckey, px + (counter - 1) * 60);
            		}else {
            			mll.overwrite(py2, rckey, px + (counter - 1) * 60);
            		}
            		mll.display();
            		px++;
            		px2++;
            		cnt.setCursorPosition(px, py);
            	}
            	else if(rckey>='A' && rckey<='Z') {
            		if(((rkeymod & KeyEvent.SHIFT_DOWN_MASK) > 0) || capslock==1) {
                       	
            			if(mode == 1){//the part added to the multi list
                       		mll.insert(py2, rckey, px + (counter - 1) * 60);
                       	}else {
                       		mll.overwrite(py2, rckey, px + (counter - 1) * 60);
                       	}
                       	mll.display();
                       	px++;
                       	px2++;
                       	cnt.setCursorPosition(px, py);
            		}                	
            		else {
            			
                       	if(mode == 1){//the part added to the multi list
                       		mll.insert(py2, (char)(rckey + 32), px + (counter-1) * 60);
                       	}else {
                       		mll.overwrite(py2, (char)(rckey + 32), px + (counter-1) * 60);
                       	}
                       	mll.display();
                       	px++;
                       	px2++;
                       	cnt.setCursorPosition(px, py);
                    }
            	}
            	if((rkeymod & KeyEvent.SHIFT_DOWN_MASK) == 0) {
            		if(rckey=='.' || rckey==',' || rckey=='-') {  
                       	if(mode == 1){
                       		mll.insert(py2, rckey, px + (counter - 1) * 60);
                       	}else {
                       		mll.overwrite(py2, rckey, px + (counter - 1) * 60);
                       	}
                       	mll.display();
                       	px++;
                       	px2++;
                       	cnt.setCursorPosition(px, py);
            		}                	
            	}
            	else {
            		if(rckey=='.') 
                       	System.out.print(':');
            		if(rckey==',')
                       	System.out.print(';');
            		if(mode == 1){//the part added to the multi list
                   		mll.insert(py2, rckey, px + (counter - 1) * 60);
                   	}else {
                   		mll.overwrite(py2, rckey, px + (counter - 1) * 60);
                   	}
                   	mll.display();
                   	px++;
                   	px2++;
                   	cnt.setCursorPosition(px, py);
            	}            	
            }        
            flag = false;
            keypr = 0;// last action  
         }
         Thread.sleep(20);   
      } //end of game loop      
   }
   
   
   
     
   public static void readingFile(String str) throws IOException, InterruptedException {
	   File file = new File(str);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
		String line = null;		
		line = reader.readLine();
		
		while(line != null) {		
			cn.getTextWindow().output(line + "\n");
			line = reader.readLine();					
		}		
		reader.close();
   }
   
   
   
   
   public static void cleanScreen() {
	   for(int i = 1; i < 61; i++) {
		   for(int j = 1; j < 20; j++) {               
			   cn.getTextWindow().output(i, j, ' ');
		   }
	   }
   }
  
   
   
   
   
   
   
}
