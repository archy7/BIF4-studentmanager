package com.example.andreas.studentmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.andreas.studentmanager.pickers.DatePickerFragment;
import com.example.andreas.studentmanager.pickers.TimePickerFragment;
import com.example.andreas.studentmanager.core.NotificationPublisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Credit: http://stackoverflow.com/questions/15027987/how-to-read-timepicker-chosen-values
 */
public class AddDutyActivity extends FragmentActivity implements TimePickerDialog.OnTimeSetListener,
                                                                DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener{//, AdapterView.OnItemSelectedListener {

    protected Spinner prioSpinner;
    protected EditText nameEditText;
    protected EditText effortEditText;
    protected TextView dateTextView;
    protected Button showDatePickerButton;
    protected Button showTimePickerButton;
    protected TextView timeTextView;
    protected EditText notesEditText;

    protected String name;
    protected String notes;
    protected double effort;
    protected int prio;
    //Time
    protected int pickerHour = 0;
    protected int pickerMin = 0;
    //Date
    protected int pickerDay = 0;
    protected int pickerMonth = 0;
    protected int pickerYear = 0;

    //Notification data
    protected Calendar alarmStartTime = Calendar.getInstance();
    int settings_prio;
    int settings_reminder_first;
    int settings_reminder_repeat;
    Random randomGenerator = new Random();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duty);

        //Get the settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        settings_prio = Integer.parseInt(prefs.getString("PREF_PRIO", "5"));
        settings_reminder_first = Integer.parseInt(prefs.getString("PREF_REMINDERFIRST", "7"));
        settings_reminder_repeat = Integer.parseInt(prefs.getString("PREF_REMINDERREPEAT", "2"));

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
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, this.prioSchummeln()); //prioSchummeln() -> hier sollten die Integers aus der Konfiguration geholt werden
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioSpinner.setAdapter(adapter);
        prioSpinner.setOnItemSelectedListener(this);

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

    protected ArrayList<Integer> prioSchummeln(){
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for(int i=1; i <= settings_prio; i++){
            integerArrayList.add(i);
        }

        return integerArrayList;
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
        resultIntent.putExtra("prio", this.prio);
        resultIntent.putExtra("effort", Double.parseDouble(this.effortEditText.getText().toString()));
        resultIntent.putExtra("hour", this.pickerHour);
        resultIntent.putExtra("minute", this.pickerMin);
        resultIntent.putExtra("year", this.pickerYear);
        resultIntent.putExtra("month", this.pickerMonth);
        resultIntent.putExtra("day", this.pickerDay);
        resultIntent.putExtra("notes", this.notesEditText.getText().toString());

        setResult(RESULT_OK,resultIntent);

        //Making the notification
        String title = this.nameEditText.getText().toString();
        StringBuilder mybuilder = new StringBuilder();
        mybuilder.append("fällig am ");
        mybuilder.append(this.pickerYear);
        mybuilder.append("-");
        mybuilder.append(this.pickerMonth);
        mybuilder.append("-");
        mybuilder.append(this.pickerDay);
        mybuilder.append(", um ");
        mybuilder.append(this.pickerHour);
        mybuilder.append(":");
        mybuilder.append(this.pickerMin);
        scheduleNotification(getNotification(title, mybuilder.toString()));
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
    public void onBackPressed() {
        Intent resultIntent = new Intent();

        setResult(RESULT_CANCELED,resultIntent);
        finish();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        pickerHour = hourOfDay;
        pickerMin = minute;

        StringBuilder sb = new StringBuilder();
        if(pickerHour < 10){
            sb.append(0);
        }
        sb.append(pickerHour);
        sb.append(":");
        if(pickerMin < 10){
            sb.append(0);
        }
        sb.append(pickerMin);

        this.timeTextView.setText(sb.toString());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        pickerDay = day;
        pickerMonth = month;
        pickerYear = year;

        StringBuilder sb = new StringBuilder();
        if(pickerDay < 10){
            sb.append(0);
        }
        sb.append(pickerDay);
        sb.append(".");
        if(pickerMonth < 10){
            sb.append(0);
        }
        sb.append(pickerMonth);
        sb.append(".");
        sb.append(pickerYear);

        this.dateTextView.setText(sb.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Integer integer = (Integer) parent.getItemAtPosition(position);
        this.prio = integer;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing
    }

    private void scheduleNotification(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        int randomInt = randomGenerator.nextInt(100000);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, randomInt);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmStartTime.set(this.pickerYear, this.pickerMonth, this.pickerDay, this.pickerHour, this.pickerMin);
        alarmStartTime.add(Calendar.DAY_OF_MONTH, settings_reminder_first*(-1));
        int intervall = settings_reminder_repeat *24*60*60*1000;
        //int intervall = 5000;
        //Proof that notification is set for the right day
        //System.out.println(alarmStartTime.get(Calendar.YEAR) +"-"+ alarmStartTime.get(Calendar.MONTH)+"-"+ alarmStartTime.get(Calendar.DAY_OF_MONTH));

        alarmManager.setRepeating(AlarmManager.RTC, alarmStartTime.getTimeInMillis(), intervall, pendingIntent);
    }

    private Notification getNotification(String title, String content) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(android.R.drawable.ic_menu_my_calendar);
        builder.setContentIntent(resultPendingIntent);
        return builder.build();
    }
}


/*new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        WelcomeActivity.super.onBackPressed();
                    }
                }).create().show();*/