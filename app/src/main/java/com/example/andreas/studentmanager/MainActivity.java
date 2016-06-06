package com.example.andreas.studentmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andreas.studentmanager.core.FileHandler;
import com.example.andreas.studentmanager.models.Duty;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Duty> dutyArrayList = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public final static int ADD_DUTY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileHandler.getInstance().setContext(this);

        //custom
        /**
         *  Schritte:
         *  1. Termine laden Sollte die App gestartet werden: Aus FileSystem (csv/xml) In der Zwischenzeit: wird die Methode schummeln() benutzt.
         *  Schummeln erzeugt 3 Fake Termine mit denen gearbeitet werden kann.
         *  2. Termine der Liste hinzufügen. Hierzu wird ein Adapter verwendet. Adapter sind common practice um eine ListView dynamisch zu füllen.
         */

        //Schritt 1
        //this.dutyArrayList = this.schummeln2(); //--> ändern in importTermineFromFileSystem();
        this.dutyArrayList=FileHandler.getInstance().readDutiesFromFile();

        //Schritt 2
        //2.1
        ArrayAdapter<Duty> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, this.dutyArrayList);
        //2.2
        ListView listView = (ListView) findViewById(R.id.dutyListView);
        listView.setAdapter(adapter);
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
        Duty duty1 = new Duty(0, "CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Duty duty2 = new Duty(1, "SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Duty duty3 = new Duty(2, "MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        returnList.add(duty1);
        returnList.add(duty2);
        returnList.add(duty3);

        FileHandler.getInstance().writeDuty(duty1);
        FileHandler.getInstance().writeDuty(duty2);
        FileHandler.getInstance().writeDuty(duty3);

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

    private void exportDutiesToFileSystem(){
        //TODO: implement this

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if(resultCode == RESULT_OK){
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

            this.dutyArrayList.add(newDuty);
            FileHandler.getInstance().writeDuty(newDuty);
        }
        else if(resultCode == RESULT_CANCELED){
            //do nothing
        }
        else {
            //panic
        }
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
}
