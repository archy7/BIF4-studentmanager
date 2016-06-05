package com.example.andreas.studentmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.andreas.studentmanager.models.Prio;
import com.example.andreas.studentmanager.pickers.DatePickerFragment;
import com.example.andreas.studentmanager.pickers.TimePickerFragment;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Credit: http://stackoverflow.com/questions/15027987/how-to-read-timepicker-chosen-values
 */
public class AddDutyActivity extends FragmentActivity implements TimePickerDialog.OnTimeSetListener,
                                                                DatePickerDialog.OnDateSetListener,
                                                                AdapterView.OnItemSelectedListener {

    private Spinner prioSpinner;
    private EditText nameEditText;
    private EditText effortEditText;
    private TextView dateTextView;
    private Button showDatePickerButton;
    private Button showTimePickerButton;
    private TextView timeTextView;
    private EditText notesEditText;

    private String name;
    private String notes;
    private double effort;
    private Prio prio;
    //Time
    private int pickerHour = 0;
    private int pickerMin = 0;
    //Date
    private int pickerDay = 0;
    private int pickerMonth = 0;
    private int pickerYear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty);

        prioSpinner = (Spinner) findViewById(R.id.prioSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        effortEditText = (EditText) findViewById(R.id.effortEditText);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        showDatePickerButton = (Button) findViewById(R.id.showDatePickerButton);
        showTimePickerButton = (Button) findViewById(R.id.showTimePickerButton);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        notesEditText = (EditText) findViewById(R.id.notesEditText);

        //populate Spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, Prio.getValuesAsIntegerArray());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioSpinner.setAdapter(adapter);

        //set text of TextViews for Time and Date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        this.dateTextView.setText("01.01." + Integer.toString(year));

        this.timeTextView.setText("00:00");

        //set Change Listeners for EditTexts
        //this.setChangeListeners();

    }


    public void showTimePickerDialog(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }



    public void addNewDuty(View view){
        /**
         *  Hier gibt es 2 Möglichkeiten die Activity an die MainActivity zurückzuschicken (im Rahmen von Intents)
         *  Man schickt die Bausteine der neuen Duty einzeln:
         *  intent.putExtra("day", this.day); etc.
         *
         *  ODER
         *
         *  intent.putExtra("duty", new Duty(parameters...);
         *  Hierbei müsste Duty aber das Interface Parcelable impelementieren
         *  Aus Gründen der "Einfachheit" wird die erste Version verwendet.
         */

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", this.nameEditText.getText().toString());
        resultIntent.putExtra("prio", this.prio.getValue());
        resultIntent.putExtra("effort", Double.parseDouble(this.effortEditText.getText().toString()));
        resultIntent.putExtra("hour", this.pickerHour);
        resultIntent.putExtra("minute", this.pickerMin);
        resultIntent.putExtra("year", this.pickerYear);
        resultIntent.putExtra("month", this.pickerMonth);
        resultIntent.putExtra("day", this.pickerDay);
        resultIntent.putExtra("notes", this.notesEditText.getText().toString());

        setResult(0,resultIntent);
        finish();

    }

    /**
     * vorerst verworfen: kleines Problem: Wie bekomm ich den neuen Text Wert in die Activity?
     * keine sofortige Lösung gefunden
     */
    /*private void setChangeListeners(){
        this.effortEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //empty not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //empty not needed
            }
        });
    }*/

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        pickerHour = hourOfDay;
        pickerMin = minute;

        //TODO: set text values to represent the chosen values
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        pickerDay = day;
        pickerMonth = month;
        pickerYear = year;

        //TODO: set text values to represent the chosen values
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Enum aus Prio fischen
        Integer integer = (Integer) parent.getItemAtPosition(position);
        this.prio = Prio.makePrio(integer);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing
    }
}
