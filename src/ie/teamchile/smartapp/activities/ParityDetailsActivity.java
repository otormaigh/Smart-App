package ie.teamchile.smartapp.activities;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.utility.ServiceUserSingleton;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ParityDetailsActivity extends MenuInheritActivity {
	private BaseAdapter adapter;
	private TextView name, parity, babyName;
	private String patientName, patientParity;
	private String sex_male = "ale", sex_female = "emale";
	
	ServiceUserActivity ab = new ServiceUserActivity();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parity_details);
		
		switch(getScreenOrientation()){
		case 1:				//ORIENTATION_PORTRAIT
			portraitCode();
			break;
		case 2:				//ORIENTATION_LANDSCAPE
			landscapeCode();
			break;
		}		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
	
	private int getScreenOrientation() {
	    Display getOrient = this.getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}
	
	private void portraitCode(){
		ListView parityList = (ListView)findViewById(R.id.parity_list);
		babyName = (TextView)findViewById(R.id.baby_name_parity);
		parity =(TextView)findViewById(R.id.parity_info);
		
		patientName = ServiceUserSingleton.getInstance().getUserName().get(0);
		patientParity = ServiceUserSingleton.getInstance().getUserParity().get(0);
		setTitle(patientName);
		parity.setText(patientParity);
		
		List<String> nameBaby = ServiceUserSingleton.getInstance().getBabyName();
		List<String> hospitalNumber = ServiceUserSingleton.getInstance().getBabyHospitalNumber();
        
		List<String> dobBaby = ServiceUserSingleton.getInstance().getBabyDeliveryDateTime();
		List<String> genderBaby = ServiceUserSingleton.getInstance().getBabyGender();
		List<String> gestationBaby = ServiceUserSingleton.getInstance().getPregnancyGestation();
		
		List<String> weightBaby = ServiceUserSingleton.getInstance().getBabyWeight();
		Log.d("bugs", "weightBaby" + weightBaby);
	
		List<String> birthMode = ServiceUserSingleton.getInstance().getPregnancyBirthMode();
		List<String> birthOutcome = ServiceUserSingleton.getInstance().getBabyBirthOutcome();
        
	    ArrayList<String> dobStr = new ArrayList<String>();
	   
		for(int i = 0; i < dobBaby.size(); i++){
			dobStr.add(dobBaby.get(i) + "\n");
		}
		  
		for(int i = 0; i < weightBaby.size(); i ++){
			String kg = getGramsToKg(Integer.parseInt(weightBaby.get(i)));
			weightBaby.set(i, kg);
		}
		
        //textBabyDOB.setText(dobStr);
		adapter = new ListElementAdapter(ParityDetailsActivity.this, nameBaby, hospitalNumber,
				 dobStr, genderBaby, gestationBaby
				,weightBaby, birthMode, birthOutcome);
		parityList.setAdapter(adapter);
        //listView.setOnItemClickListener(new OnItemListener());		
	}
	
	private void landscapeCode(){
		
	}
	
    public String getGramsToKg(int grams){
    	double kg = 0.0;
    	kg = grams/1000.0;
    	return String.valueOf(kg);
    }
	
	private class ListElementAdapter extends BaseAdapter {
		Context context;
		int position;
		LayoutInflater layoutInflater;
		List<String> babyInformation, hospitalNumber, babyGender, gestation,
				 	 weight, name, birthMode, birthOutcome; 	

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		public ListElementAdapter(Context context, List<String> name, List<String>hospitalNumber, 
				List<String>babyInformation, List<String>babyGender, List<String>gestation,
				List<String>weight, List<String>birthMode, List<String>birthOutcome) {
			super();
			
			this.context = context;
			this.babyInformation = babyInformation;
			this.hospitalNumber= hospitalNumber;
			this.name=name;
			this.babyGender = babyGender;
			this.gestation= gestation;
			this.weight=weight;
			this.birthMode=birthMode;
			this.birthOutcome=birthOutcome;
			layoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return name.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			this.position = position;
			convertView = layoutInflater.inflate(R.layout.parity_list, parent, false);
			TextView nameOfBaby = (TextView)convertView.findViewById(R.id.baby_name_parity);
			TextView hospitalNo = (TextView) convertView.findViewById(R.id.hospital_number_parity);
			TextView dobText = (TextView) convertView.findViewById(R.id.dob_parity);
			TextView genderText = (TextView) convertView.findViewById(R.id.gender_parity);
			TextView gestationText = (TextView) convertView.findViewById(R.id.gestation_parity);
			TextView weightText = (TextView) convertView.findViewById(R.id.weight_parity);
			TextView modeText = (TextView) convertView.findViewById(R.id.mode_of_birth_parity);
			TextView birthOutcomeParity = (TextView) convertView.findViewById(R.id.birth_outcome_parity);
			
		    nameOfBaby.setText(name.get(position));
			dobText.setText(ab.getDeliveryDate(babyInformation.get(position)));
			hospitalNo.setText(hospitalNumber.get(position));
			if(babyGender.get(position).equalsIgnoreCase("M")){
				genderText.setText(babyGender.get(position) + sex_male);
			}
			else if (babyGender.get(position).equalsIgnoreCase("F")){
				genderText.setText(babyGender.get(position) + sex_female);
			}	
			gestationText.setText(gestation.get(position));
	
			weightText.setText(weight.get(position));
			modeText.setText(formatArrayString(birthMode.get(position)));
			birthOutcomeParity.setText(birthOutcome.get(position));
			//gestText.setText(gestationStr.get(position)); 
			return convertView;			
		}
	}
	
    private String formatArrayString(String toBeFormatted){
    	String formatedString = toBeFormatted
    		    .replace(",", "")  //remove the commas
    		    .replace("[", "")  //remove the right bracket
    		    .replace("]", "")  //remove the left bracket
    		    .replace("\"", "")
    		    .trim(); 
    	return formatedString;
    }
}
