package com.cs286r.pizzademocracy;

import com.finalproject.pizzademocracy.R;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DropdownManager {
	
	static int selection1 = 0;
	static int selection2 = 0;
	static int selection3 = 0;
	static int selectionLast = 0;
	final String[] toppingsArray;
	final public Spinner choice1;
	final public Spinner choice2;
	final public Spinner choice3;
	final public Spinner choiceDeadLast;
	private Activity context;
	
	//constructor
	public DropdownManager(Activity context){
		
		this.context = context;
		
        //populate a list of toppings from the enum
        toppingsArray = new String[Topping.values().length + 1];
        Topping[] unconvertedToppings = Topping.values();
        
        toppingsArray[0] = "None";
        for(int i= 0; i < Topping.values().length; i++){
        	toppingsArray[i+1] = unconvertedToppings[i].toString();        	
        }
        
        //find all the interesting UI elements in the XML
        choice1 = (Spinner) context.findViewById(R.id.choice1);
        choice2 = (Spinner) context.findViewById(R.id.choice2);
        choice3 = (Spinner) context.findViewById(R.id.choice3);
        choiceDeadLast = (Spinner) context.findViewById(R.id.choice_dead_last);
        
        //create a special array of options and pass it to all the spinner menus
        ArrayAdapter<String> newArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, toppingsArray);       
        
        
        choice1.setAdapter(newArrayAdapter);
        choice2.setAdapter(newArrayAdapter);
        choice3.setAdapter(newArrayAdapter);
        choiceDeadLast.setAdapter(newArrayAdapter);
	}
	
	public void clearDropdowns(){
		selection1 = 0;
		selection2 = 0;
		selection3 = 0;
		selectionLast = 0;
		
		ArrayAdapter<String> clearedArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, toppingsArray);	
		choice1.setAdapter(clearedArrayAdapter);
	    choice2.setAdapter(clearedArrayAdapter);
	    choice3.setAdapter(clearedArrayAdapter);
	    choiceDeadLast.setAdapter(clearedArrayAdapter);
	}
	
	public void choice1OnSelect(int pos){
		//pos is the position of the selection within the given spinner's list;
        //we need to convert it to a position in the global master list of toppings
		int newSelection1 = 0;
		if(pos != 0){
			for(int i = 1; i < toppingsArray.length; i++){
				if(selection2 != i && selection3 != i && selectionLast != i){
					pos--;
				}
				if(pos == 0){
					newSelection1 = i;
					break;
				}
			}
		}
		else{
			newSelection1 = 0;
		}
		
		//if we're at the same position we were before, no need to run the rest of this code
		if(newSelection1 == selection1){
			return;
		}
		else{
			selection1 = newSelection1;
		}
		
		//number of options to remove
		int toRemove2 = 0;
		int toRemove3 = 0;
		int toRemoveLast = 0;
		if(newSelection1 != 0){
			toRemove2++;
			toRemove3++;
			toRemoveLast++;
		}
		if(selection2 != 0){
			toRemove3++;
			toRemoveLast++;
		}
		if(selection3 != 0){
			toRemove2++;
			toRemoveLast++;
		}
		if(selectionLast != 0){					
			toRemove2++;
			toRemove3++;
		}
		
		//create an array of the size that corresponds to the number of available options
		String[] newToppingsArray2 = new String[toppingsArray.length - toRemove2];
		String[] newToppingsArray3 = new String[toppingsArray.length - toRemove3];
		String[] newToppingsArrayLast = new String[toppingsArray.length - toRemoveLast];
		int skip2 = 0;
		int skip3 = 0;
		int skipLast = 0;
		
		//relative positions of selections within new lists
		int relativeSelection2 = 0;
		int relativeSelection3 = 0;
		int relativeSelectionLast = 0;
		
        for(int i= 0; i < toppingsArray.length; i++){
        	if(i != 0 && i == newSelection1){
        		skip2++; 
        		skip3++;
        		skipLast++;
        	}
        	else if(i != 0 && i == selection2){
        		skip3++;
        		skipLast++;
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		relativeSelection2 = i - skip2;
        	}
        	else if(i != 0 && i == selection3){
        		skip2++;
        		skipLast++;
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		relativeSelection3 = i - skip3;
        	}
        	else if(i != 0 && i == selectionLast){
        		skip2++;
        		skip3++;
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        		relativeSelectionLast = i - skipLast;
        	}
        	else{
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        	}
        }
        
        ArrayAdapter<String> choice2ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray2);        
        ArrayAdapter<String> choice3ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray3);
        ArrayAdapter<String> choiceDeadLastArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArrayLast);        
	       
        choice2.setAdapter(choice2ArrayAdapter);
        choice3.setAdapter(choice3ArrayAdapter);
        choiceDeadLast.setAdapter(choiceDeadLastArrayAdapter);	
        
        choice2.setSelection(relativeSelection2);
        choice3.setSelection(relativeSelection3);
        choiceDeadLast.setSelection(relativeSelectionLast);
        
        choice2.setSelected(true);
        choice3.setSelected(true);
        choiceDeadLast.setSelected(true);
	}
	
	public void choice2OnSelect(int pos){
		//pos is the position of the selection within the given spinner's list;
        //we need to convert it to a position in the global master list of toppings
		int newSelection2 = 0;
		if(pos != 0){
			for(int i = 1; i < toppingsArray.length; i++){
				if(selection1 != i && selection3 != i && selectionLast != i){
					pos--;
				}
				if(pos == 0){
					newSelection2 = i;
					break;
				}
			}
		}
		else{
			newSelection2 = 0;
		}
		
		//if we're at the same position we were before, no need to run the rest of this code
		if(newSelection2 == selection2){
			return;
		}
		else{
			selection2 = newSelection2;
		}
		
		//number of options to remove
		int toRemove1 = 0;
		int toRemove3 = 0;
		int toRemoveLast = 0;
		if(newSelection2 != 0){
			toRemove1++;
			toRemove3++;
			toRemoveLast++;
		}
		if(selection1 != 0){
			toRemove3++;
			toRemoveLast++;
		}
		if(selection3 != 0){
			toRemove1++;
			toRemoveLast++;
		}
		if(selectionLast != 0){
			toRemove1++;
			toRemove3++;
		}
			
		//create an array of the size that corresponds to the number of available options
		String[] newToppingsArray1 = new String[toppingsArray.length - toRemove1];
		String[] newToppingsArray3 = new String[toppingsArray.length - toRemove3];
		String[] newToppingsArrayLast = new String[toppingsArray.length - toRemoveLast];
		int skip1 = 0;
		int skip3 = 0;
		int skipLast = 0;
		
		//relative positions of selections within new lists
		int relativeSelection1 = 0;
		int relativeSelection3 = 0;
		int relativeSelectionLast = 0;
		
        for(int i= 0; i < toppingsArray.length; i++){
        	if(i != 0 && i == newSelection2){
        		skip1++; 
        		skip3++;
        		skipLast++;
        	}
        	else if(i != 0 && i == selection1){
        		skip3++;
        		skipLast++;
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        		relativeSelection1 = i - skip1;
        	}
        	else if(i != 0 && i == selection3){
        		skip1++;
        		skipLast++;
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		relativeSelection3 = i - skip3;
        	}
        	else if(i != 0 && i == selectionLast){
        		skip1++;
        		skip3++;
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        		relativeSelectionLast = i - skipLast;
        	}
        	else{
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        	}
        }
        
        ArrayAdapter<String> choice1ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray1);        
        ArrayAdapter<String> choice3ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray3);
        ArrayAdapter<String> choiceDeadLastArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArrayLast);        
	       
        choice1.setAdapter(choice1ArrayAdapter);
        choice3.setAdapter(choice3ArrayAdapter);
        choiceDeadLast.setAdapter(choiceDeadLastArrayAdapter);
        
        choice1.setSelection(relativeSelection1);
        choice3.setSelection(relativeSelection3);
        choiceDeadLast.setSelection(relativeSelectionLast);
        
        choice2.setSelected(true);
        choice3.setSelected(true);
        choiceDeadLast.setSelected(true);
	}
	
	
	public void choice3OnSelect(int pos){
		//pos is the position of the selection within the given spinner's list;
        //we need to convert it to a position in the global master list of toppings
		int newSelection3 = 0;
		if(pos != 0){
			for(int i = 1; i < toppingsArray.length; i++){
				if(selection2 != i && selection1 != i && selectionLast != i){
					pos--;
				}
				if(pos == 0){
					newSelection3 = i;
					break;
				}
			}
		}
		else{
			newSelection3 = 0;
		}
		
		//if we're at the same position we were before, no need to run the rest of this code
		if(newSelection3 == selection3){
			return;
		}
		else{
			selection3 = newSelection3;
		}
		
		//number of options to remove
		int toRemove2 = 0;
		int toRemove1 = 0;
		int toRemoveLast = 0;
		if(newSelection3 != 0){
			toRemove2++;
			toRemove1++;
			toRemoveLast++;
		}
		if(selection2 != 0){
			toRemove1++;
			toRemoveLast++;
		}
		if(selection1 != 0){
			toRemove2++;
			toRemoveLast++;
		}
		if(selectionLast != 0){
			toRemove2++;
			toRemove1++;
		}
						
		//create an array of the size that corresponds to the number of available options
		String[] newToppingsArray2 = new String[toppingsArray.length - toRemove2];
		String[] newToppingsArray1 = new String[toppingsArray.length - toRemove1];
		String[] newToppingsArrayLast = new String[toppingsArray.length - toRemoveLast];
		int skip2 = 0;
		int skip1 = 0;
		int skipLast = 0;
		
		//relative positions of selections within new lists
		int relativeSelection1 = 0;
		int relativeSelection2 = 0;
		int relativeSelectionLast = 0;
		
        for(int i= 0; i < toppingsArray.length; i++){
        	if(i != 0 && i == newSelection3){
        		skip2++; 
        		skip1++;
        		skipLast++;
        	}
        	else if(i != 0 && i == selection2){
        		skip1++;
        		skipLast++;
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		relativeSelection2 = i - skip2;
        	}
        	else if(i != 0 && i == selection1){
        		skip2++;
        		skipLast++;
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        		relativeSelection1 = i - skip1;
        	}
        	else if(i != 0 && i == selectionLast){
        		skip2++;
        		skip1++;
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        		relativeSelectionLast = i - skipLast;
        	}
        	else{
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        		newToppingsArrayLast[i - skipLast] = toppingsArray[i];
        	}
        }
        
        ArrayAdapter<String> choice2ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray2);        
        ArrayAdapter<String> choice1ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray1);
        ArrayAdapter<String> choiceDeadLastArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArrayLast);        
	       
        choice2.setAdapter(choice2ArrayAdapter);
        choice1.setAdapter(choice1ArrayAdapter);
        choiceDeadLast.setAdapter(choiceDeadLastArrayAdapter);
        
        choice1.setSelection(relativeSelection1);
        choice2.setSelection(relativeSelection2);
        choiceDeadLast.setSelection(relativeSelectionLast);
        
        choice2.setSelected(true);
        choice1.setSelected(true);
        choiceDeadLast.setSelected(true);
	}
	
	public void choiceDeadLastOnSelect(int pos){
		 //pos is the position of the selection within the given spinner's list;
        //we need to convert it to a position in the global master list of toppings
		int newSelectionLast = 0;
		if(pos != 0){
			for(int i = 1; i < toppingsArray.length; i++){
				if(selection2 != i && selection3 != i && selection1 != i){
					pos--;
				}
				if(pos == 0){
					newSelectionLast = i;
					break;
				}
			}
		}
		else{
			newSelectionLast = 0;
		}
		
		//if we're at the same position we were before, no need to run the rest of this code
		if(newSelectionLast == selectionLast){
			return;
		}
		else{
			selectionLast = newSelectionLast;
		}
		
		//number of options to remove
		int toRemove2 = 0;
		int toRemove3 = 0;
		int toRemove1 = 0;
		if(newSelectionLast != 0){
			toRemove2++;
			toRemove3++;
			toRemove1++;
		}
		if(selection2 != 0){
			toRemove3++;
			toRemove1++;
		}
		if(selection3 != 0){
			toRemove2++;
			toRemove1++;
		}
		if(selection1 != 0){
			toRemove2++;
			toRemove3++;	
		}
		
		//create an array of the size that corresponds to the number of available options
		String[] newToppingsArray2 = new String[toppingsArray.length - toRemove2];
		String[] newToppingsArray3 = new String[toppingsArray.length - toRemove3];
		String[] newToppingsArray1 = new String[toppingsArray.length - toRemove1];
		int skip2 = 0;
		int skip3 = 0;
		int skip1 = 0;
		
		//relative positions of selections within new lists
		int relativeSelection1 = 0;
		int relativeSelection2 = 0;
		int relativeSelection3 = 0;
		
        for(int i= 0; i < toppingsArray.length; i++){
        	if(i != 0 && i == newSelectionLast){
        		skip2++; 
        		skip3++;
        		skip1++;
        	}
        	else if(i != 0 && i == selection2){
        		skip3++;
        		skip1++;
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		relativeSelection2 = i - skip2;
        	}
        	else if(i != 0 && i == selection3){
        		skip2++;
        		skip1++;
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		relativeSelection3 = i - skip3;
        	}
        	else if(i != 0 && i == selection1){
        		skip2++;
        		skip3++;
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        		relativeSelection1 = i - skip1;
        	}
        	else{
        		newToppingsArray2[i - skip2] = toppingsArray[i];
        		newToppingsArray3[i - skip3] = toppingsArray[i];
        		newToppingsArray1[i - skip1] = toppingsArray[i];
        	}
        }
        
        ArrayAdapter<String> choice2ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray2);        
        ArrayAdapter<String> choice3ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray3);
        ArrayAdapter<String> choice1ArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, newToppingsArray1);        
	       
        choice2.setAdapter(choice2ArrayAdapter);
        choice3.setAdapter(choice3ArrayAdapter);
        choice1.setAdapter(choice1ArrayAdapter);	
        
        choice1.setSelection(relativeSelection1);
        choice2.setSelection(relativeSelection2);
        choice3.setSelection(relativeSelection3);
        
        choice2.setSelected(true);
        choice1.setSelected(true);
        choice3.setSelected(true);
	}
}
