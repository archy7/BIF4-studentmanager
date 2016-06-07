package com.example.andreas.studentmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class ViewAndEditDutyActivity extends AddDutyActivity {

    public static final int ADD_SUB_DUTY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_edit_duty);

        Intent intent = this.getIntent();

        prioSpinner = (Spinner) findViewById(R.id.VaEPrioSpinner);
        nameEditText = (EditText) findViewById(R.id.VaENameEditText);
        effortEditText = (EditText) findViewById(R.id.VaEEffortEditText);
        dateTextView = (TextView) findViewById(R.id.VaEDateTextView);
        showDatePickerButton = (Button) findViewById(R.id.VaEShowDatePickerButton);
        showTimePickerButton = (Button) findViewById(R.id.VaEShowTimePickerButton);
        timeTextView = (TextView) findViewById(R.id.VaETimeTextView);
        notesEditText = (EditText) findViewById(R.id.VaENotesEditText);

        //populate Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, this.prioSchummeln()); //prioSchummeln() -> hier sollten die Integers aus der Konfiguration geholt werden
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioSpinner.setAdapter(adapter);
        prioSpinner.setOnItemSelectedListener(this);

        //adapt internal fields for Date and Time
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        pickerYear = intent.getIntExtra("year", year);
        pickerMonth = intent.getIntExtra("month", 0);
        pickerDay = intent.getIntExtra("day", 0);
        pickerHour = intent.getIntExtra("hour", 0);
        pickerMin = intent.getIntExtra("minute", 0);

        //set Text of Fields
        this.nameEditText.setText(intent.getStringExtra("name"));
        this.effortEditText.setText(Double.toString(intent.getDoubleExtra("effort",0)));

        ArrayAdapter<Integer> tempadapter = (ArrayAdapter<Integer>) this.prioSpinner.getAdapter();
        int position =  tempadapter.getPosition(intent.getIntExtra("prio", 0));
        this.prioSpinner.setSelection(position);


        this.dateTextView.setText(pickerDay + "." + pickerMonth + "." + pickerYear);

        this.timeTextView.setText(pickerHour + ":" + pickerMin);

        this.notesEditText.setText(intent.getStringExtra("notes"));


    }

    @Override
    public void addNewDuty(View view){
        //I am supposed to do nothing.
    }

    public void editDuty(View view){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", this.nameEditText.getText().toString());
        resultIntent.putExtra("prio", this.prio);
        resultIntent.putExtra("effort", Double.parseDouble(this.effortEditText.getText().toString()));
        resultIntent.putExtra("hour", this.pickerHour);
        resultIntent.putExtra("minute", this.pickerMin);
        resultIntent.putExtra("year", this.pickerYear);
        resultIntent.putExtra("month", this.pickerMonth);
        resultIntent.putExtra("day", this.pickerDay);
        resultIntent.putExtra("notes", this.notesEditText.getText().toString());

        resultIntent.putExtra("mode", MainActivity.EDIT_DUTY); //WICHTIG

        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void deleteDuty(View view){
        Intent resultIntent = new Intent();

        resultIntent.putExtra("mode", MainActivity.DELETE_DUTY); //WICHTIG

        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void addSubDuty(View view){
        //hier sub duties adden
        Intent intent = new Intent(this, AddDutyActivity.class);
        startActivityForResult(intent, ViewAndEditDutyActivity.ADD_SUB_DUTY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ViewAndEditDutyActivity.ADD_SUB_DUTY_REQUEST:{

                }
                default:{
                    //panic
                }
            }

        }
        else if(resultCode == RESULT_CANCELED){
            //do nothing
        }
        else {
            //panic
        }

    }
}
