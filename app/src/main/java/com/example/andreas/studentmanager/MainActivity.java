package com.example.andreas.studentmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andreas.studentmanager.models.Duty;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener{

    private ArrayList<Duty> dutyArrayList = null;
    private Duty selectedDuty = null;

    private ListView dutyListView;
    private ArrayAdapter<Duty> dutyArrayAdapter;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public final static int ADD_DUTY_REQUEST = 1;
    public final static int VIEW_AND_EDIT_DUTY_REQUEST = 2;

    public final static int EDIT_DUTY = 1;
    public final static int DELETE_DUTY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //custom
        /**
         *  Schritte:
         *  1. Termine laden Sollte die App gestartet werden: Aus FileSystem (csv/xml) In der Zwischenzeit: wird die Methode schummeln() benutzt.
         *  Schummeln erzeugt 3 Fake Termine mit denen gearbeitet werden kann.
         *  2. Termine der Liste hinzufügen. Hierzu wird ein Adapter verwendet. Adapter sind common practice um eine ListView dynamisch zu füllen.
         */

        //Schritt 1
        this.dutyArrayList = this.schummeln2(); //--> ändern in importTermineFromFileSystem();


        //Schritt 2
        //2.1
        this.dutyArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, this.dutyArrayList);
        //2.2
        this.dutyListView = (ListView) findViewById(R.id.dutyListView);
        this.dutyListView.setAdapter(this.dutyArrayAdapter);
        this.dutyListView.setOnItemClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Duty[] schummeln() {
        Duty duty1 = new Duty("CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Duty duty2 = new Duty("SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Duty duty3 = new Duty("MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        Duty[] returnArray = {duty1, duty2, duty3};

        return returnArray;

    }

    private ArrayList<Duty> schummeln2(){
        ArrayList<Duty> returnList = new ArrayList<>();
        Duty duty1 = new Duty("CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Duty duty2 = new Duty("SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Duty duty3 = new Duty("MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        returnList.add(duty1);
        returnList.add(duty2);
        returnList.add(duty3);

        return returnList;
    }

    private ArrayList<Duty> importDutiesFromFileSystem() {
        //TODO : IMPLEMENT THIS
        return null;
    }

    /**
     * Opens new Activity to add a Duty to the list
     */
    public void addDuty(View view) {
        Intent intent = new Intent(this, AddDutyActivity.class);
        startActivityForResult(intent, MainActivity.ADD_DUTY_REQUEST);
    }

    public void openSettingsActivity(View view) {
        //für Andi und seine Settings Dinge
    }

    private void exportDutiesToFileSystem(){
        //TODO: implement this

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case MainActivity.ADD_DUTY_REQUEST:{
                    this.addResultDutyToList(result);
                }
                case MainActivity.VIEW_AND_EDIT_DUTY_REQUEST:{
                    int mode = result.getIntExtra("mode", 0);
                    if(mode == MainActivity.DELETE_DUTY){
                        this.deleteDuty(result);
                    }
                    else if(mode == MainActivity.EDIT_DUTY){
                        this.replaceExisitingDutyWithResultDuty(result);
                    }
                    else{
                        //panic
                    }
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

    protected void addResultDutyToList(Intent result){
        String name = result.getStringExtra("name");
        String notes = result.getStringExtra("notes");
        final Calendar c = Calendar.getInstance();
        int year = result.getIntExtra("year", c.get(Calendar.YEAR));
        int month = result.getIntExtra("month", 1);
        int day = result.getIntExtra("day", 1);
        int hour = result.getIntExtra("hour", 23);
        int minute = result.getIntExtra("minute", 55);
        double effort = result.getDoubleExtra("effort", 10);
        int priority = result.getIntExtra("prio", 3);

        Duty newDuty = new Duty(name, priority, effort, new LocalDate(year, month, day), new LocalTime(hour, minute));

        if(notes != null && !notes.isEmpty()){
            newDuty.setBemerkung(notes);
        }

        this.dutyArrayList.add(newDuty);
    }

    protected void replaceExisitingDutyWithResultDuty(Intent result){
        //the currenly selected Duty is stored in a separate reference
        //Anmerkung: Was hier echt gut wäre, wäre das List<T> Interface selbst zu implementieren (DutyList), die Methode wie findByID() oder replaceByID() anbieten würde

        int index = this.dutyArrayList.indexOf(this.selectedDuty);
        this.dutyArrayList.get(index).setBetreff(result.getStringExtra("name"));
        this.dutyArrayList.get(index).setPrio(result.getIntExtra("prio", 0));
        this.dutyArrayList.get(index).setAufwand(result.getDoubleExtra("effort", 0));
        this.dutyArrayList.get(index).setAbgabeTag(new LocalDate(result.getIntExtra("year", Calendar.getInstance().get(Calendar.YEAR)), result.getIntExtra("month", 0), result.getIntExtra("day", 0)));
        this.dutyArrayList.get(index).setAbgabeZeit(new LocalTime(result.getIntExtra("hour", 0), result.getIntExtra("minute", 0)));
        this.dutyArrayList.get(index).setBemerkung(result.getStringExtra("notes"));


    }

    protected void deleteDuty(Intent result){
        this.dutyArrayAdapter.remove(this.selectedDuty);
        this.selectedDuty = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.andreas.studentmanager/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.andreas.studentmanager/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.selectedDuty = (Duty) parent.getItemAtPosition(position);

        //build intent
        Intent intent = new Intent(this, ViewAndEditDutyActivity.class);
        intent.putExtra("name", selectedDuty.getBetreff());
        intent.putExtra("prio", selectedDuty.getPrio());
        intent.putExtra("effort", selectedDuty.getAufwand());
        intent.putExtra("hour", selectedDuty.getAbgabeZeit().get(DateTimeFieldType.hourOfDay()));
        intent.putExtra("minute", selectedDuty.getAbgabeZeit().get(DateTimeFieldType.minuteOfHour()));
        intent.putExtra("year", selectedDuty.getAbgabeTag().get(DateTimeFieldType.year()));
        intent.putExtra("month", selectedDuty.getAbgabeTag().get(DateTimeFieldType.monthOfYear()));
        intent.putExtra("day", selectedDuty.getAbgabeTag().get(DateTimeFieldType.dayOfMonth()));
        intent.putExtra("notes", selectedDuty.getBemerkung());

        //start Activity
        startActivityForResult(intent, MainActivity.VIEW_AND_EDIT_DUTY_REQUEST);
    }

    /*@Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing
    }*/
}
