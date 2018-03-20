package com.cs286r.pizzademocracy;

import java.util.ArrayList;

import com.finalproject.pizzademocracy.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PizzaDemocracyActivity extends Activity {
    /** Called when the activity is first created. */
	
	static int selection1 = 0;
	static int selection2 = 0;
	static int selection3 = 0;
	static int selectionLast = 0;
	private ArrayList<Vote> prefProfile = new ArrayList<Vote>();
	private int currentNumberToppings = 0;
	private int currentNumberPizzas = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        RelativeLayout Layout2 = (RelativeLayout)findViewById(R.id.layout2);
        LinearLayout Layout1 = (LinearLayout)findViewById(R.id.layout1);
        Layout1.setVisibility(View.GONE);
        Layout2.setVisibility(View.GONE);
  
        //find all the interesting UI elements in the XML
        Button contButton = (Button) findViewById(R.id.cont);
        Button doneButton = (Button) findViewById(R.id.done);
        Button homeButton = (Button) findViewById(R.id.home);
        Button pizzaVoting = (Button) findViewById(R.id.pizza_voting);
        
        Button plusPizzas = (Button) findViewById(R.id.plus_pizzas);
        Button minusPizzas = (Button) findViewById(R.id.minus_pizzas);
        
        Button plusToppings = (Button) findViewById(R.id.plus_toppings);
        Button minusToppings = (Button) findViewById(R.id.minus_toppings);
        
		EditText editNumberPizzas = (EditText) findViewById(R.id.edit_number_pizzas);
		EditText editNumberToppings = (EditText) findViewById(R.id.edit_number_toppings);
        
        final DropdownManager spinners = new DropdownManager(this);
        
        pizzaVoting.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		EditText editNumberPizzas = (EditText) findViewById(R.id.edit_number_pizzas);
        		EditText editNumberToppings = (EditText) findViewById(R.id.edit_number_toppings);
        		currentNumberToppings = Integer.parseInt(editNumberToppings.getText().toString());
        		currentNumberPizzas = Integer.parseInt(editNumberPizzas.getText().toString());

        		
        		RelativeLayout Layout0 = (RelativeLayout)findViewById(R.id.layout0);
                LinearLayout Layout1 = (LinearLayout)findViewById(R.id.layout1);
                Layout1.setVisibility(View.VISIBLE);
                Layout0.setVisibility(View.GONE);
        	}
        });
        
        plusPizzas.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		EditText editNumberPizzas = (EditText) findViewById(R.id.edit_number_pizzas);
        		int currentNumberPizzas = Integer.parseInt(editNumberPizzas.getText().toString());
        		currentNumberPizzas++;
        		editNumberPizzas.setText(Integer.toString(currentNumberPizzas));
        	}
        });
        
        minusPizzas.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		EditText editNumberPizzas = (EditText) findViewById(R.id.edit_number_pizzas);
        		int currentNumberPizzas = Integer.parseInt(editNumberPizzas.getText().toString());
        		currentNumberPizzas--;
        		editNumberPizzas.setText(Integer.toString(currentNumberPizzas));
        	}
        });
        
        plusToppings.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		EditText editNumberToppings = (EditText) findViewById(R.id.edit_number_toppings);
        		int currentNumberToppings = Integer.parseInt(editNumberToppings.getText().toString());
        		currentNumberToppings++;
        		editNumberToppings.setText(Integer.toString(currentNumberToppings));
        	}
        });
        
        minusToppings.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		EditText editNumberToppings = (EditText) findViewById(R.id.edit_number_toppings);
        		int currentNumberToppings = Integer.parseInt(editNumberToppings.getText().toString());
        		currentNumberToppings--;
        		editNumberToppings.setText(Integer.toString(currentNumberToppings));
        	}
        });
        
        homeButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		RelativeLayout Layout0 = (RelativeLayout)findViewById(R.id.layout0);
                RelativeLayout Layout2 = (RelativeLayout)findViewById(R.id.layout2);
                Layout0.setVisibility(View.VISIBLE);
                Layout2.setVisibility(View.GONE);
                
                //TODO clear previous preferences - do we need this?
                prefProfile = new ArrayList<Vote>();
        	}
        });
        
        contButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		getDropdownValues(spinners);
        	}
        });
        
        doneButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		// grab the last vote
        		getDropdownValues(spinners);

        		Borda bordaElector = new Borda();
        		
        		// select an order based on collected vote data
        		Profile profile = new Profile(prefProfile);        		
        		Order order = bordaElector.chooseOrder(profile, currentNumberPizzas, currentNumberToppings);


        		// print out results for user
        		int numPizzas = order.numPizzas();
        		String message = null;
        		if(numPizzas == 1) {
        			message = "Congratulations, you've got a pizza!\n";
        		}
        		else {
        			message = "Congratulations, you've got " + numPizzas + " pizzas!\n";
        		}

        		Topping[] selectedToppings;
        		for(int i = 0; i < numPizzas; i++) {
        			message += "Pizza " + (i + 1) + "'s toppings are:\n";
        			selectedToppings = order.getPizza(i).getToppings();

        			for(int j = 0; j < selectedToppings.length; j++){
        				message += selectedToppings[j].toString();
        				if(j < selectedToppings.length - 1) {
        					message += ", ";
        				}
        				else
        					message += ".";
        			}

        			if(selectedToppings.length == 0)
        				message += "Cheese... boring. :("; 

        			message += "\n\n";
        		}

        		LinearLayout Layout1 = (LinearLayout)findViewById(R.id.layout1);
        		RelativeLayout Layout2 = (RelativeLayout)findViewById(R.id.layout2);
        		TextView pizzaMessage = (TextView) findViewById(R.id.pizza_congratulations);
        		Layout1.setVisibility(View.GONE);
        		Layout2.setVisibility(View.VISIBLE);
        		pizzaMessage.setText(message);
        	}
        });
        
        
        spinners.choice1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {				
		        spinners.choice1OnSelect(pos);		        
			}
			public void onNothingSelected(AdapterView<?> arg0) {/* do nothin'*/}
        	
        });
        
        spinners.choice2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {				
				spinners.choice2OnSelect(pos);
			}
			public void onNothingSelected(AdapterView<?> arg0) {/* do nothin'*/}
        	
        });   
        
        spinners.choice3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {				
				spinners.choice3OnSelect(pos);
			}
			public void onNothingSelected(AdapterView<?> arg0) {/* do nothin'*/}        	
        }); 
        
        spinners.choiceDeadLast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				spinners.choiceDeadLastOnSelect(pos);
			}
			public void onNothingSelected(AdapterView<?> arg0) {/* do nothin'*/}        	
        }); 
        
    }   
    
    public void getDropdownValues(DropdownManager spinners){
		String toppingString1 = spinners.choice1.getSelectedItem().toString();
		String toppingString2 = spinners.choice2.getSelectedItem().toString();
		String toppingString3 = spinners.choice3.getSelectedItem().toString();
		String toppingStringDeadLast = spinners.choiceDeadLast.getSelectedItem().toString();
		
		Topping topping1 = (toppingString1 == "None") ? null : Topping.valueOf(toppingString1);
		Topping topping2 = (toppingString2 == "None") ? null : Topping.valueOf(toppingString2);
		Topping topping3 = (toppingString3 == "None") ? null : Topping.valueOf(toppingString3);
		Topping toppingDeadLast = (toppingStringDeadLast == "None") ? null : Topping.valueOf(toppingStringDeadLast);
		
		
		Topping[] favoriteToppings = {topping1, topping2, topping3};
		
		Topping[] vetoToppings = {toppingDeadLast};
		
		Vote vote = new Vote(favoriteToppings, vetoToppings);
		prefProfile.add(vote);
		
		spinners.clearDropdowns();
    }
}