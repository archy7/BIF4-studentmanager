package com.example.andreas.studentmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import com.example.andreas.studentmanager.core.FileHandler;
import com.example.andreas.studentmanager.models.Duty;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener{

    private ArrayList<Duty> dutyArrayList = null;
    private Duty selectedDuty = null;

    private ListView dutyListView;
    private Button exportDutiesButton;
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
        FileHandler.getInstance().setContext(this);

        this.exportDutiesButton = (Button) findViewById(R.id.exportDutiesButton);

        //custom
        /**
         *  Schritte:
         *  1. Termine laden Sollte die App gestartet werden: Aus FileSystem (csv/xml) In der Zwischenzeit: wird die Methode schummeln() benutzt.
         *  Schummeln erzeugt 3 Fake Termine mit denen gearbeitet werden kann.
         *  2. Termine der Liste hinzufügen. Hierzu wird ein Adapter verwendet. Adapter sind common practice um eine ListView dynamisch zu füllen.
         */

        //Schritt 1
        this.dutyArrayList=this.importDutiesFromFileSystem();

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


    private ArrayList<Duty> schummeln2(){
        ArrayList<Duty> returnList = new ArrayList<>();
        Duty duty1 = new Duty(0, "CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Duty duty2 = new Duty(1, "SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Duty duty3 = new Duty(2, "MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));
        Duty duty4 = new Duty(4, "MDP4 Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        returnList.add(duty1);
        returnList.add(duty2);
        returnList.add(duty3);
        returnList.add(duty4);

        FileHandler.getInstance().writeDuty(duty1);
        FileHandler.getInstance().writeDuty(duty2);
        FileHandler.getInstance().writeDuty(duty3);
        FileHandler.getInstance().writeDuty(duty4);

        return returnList;
    }


    public void exportDutiesToFileSystem(View view){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            Log.e("Files", "Keine Permissions");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 9001: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FileHandler.getInstance().exportCSV("test", dutyArrayList);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private ArrayList<Duty> importDutiesFromFileSystem() {
        return FileHandler.getInstance().readDutiesFromFile();
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
        FileHandler.getInstance().writeDuty(newDuty);
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

        this.dutyArrayAdapter.notifyDataSetChanged();

        FileHandler.getInstance().editDuty(this.dutyArrayList.get(index));
    }

    protected void deleteDuty(Intent result){
        this.dutyArrayAdapter.remove(this.selectedDuty);
        FileHandler.getInstance().deleteDuty(this.selectedDuty.getDutyid());
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
