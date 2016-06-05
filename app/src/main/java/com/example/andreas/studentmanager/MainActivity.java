package com.example.andreas.studentmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.andreas.studentmanager.models.Prio;
import com.example.andreas.studentmanager.models.Termin;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Termin> terminArrayList = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        this.terminArrayList = this.schummeln2(); //--> ändern in importTermineFromFileSystem();


        //Schritt 2
        //2.1
        ArrayAdapter<Termin> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, this.terminArrayList);
        //2.2
        ListView listView = (ListView) findViewById(R.id.dutyListView);
        listView.setAdapter(adapter);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Termin[] schummeln() {
        Termin termin1 = new Termin("CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Termin termin2 = new Termin("SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Termin termin3 = new Termin("MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        Termin[] returnArray = {termin1, termin2, termin3};

        return returnArray;

    }

    private ArrayList<Termin> schummeln2(){
        ArrayList<Termin> returnList = new ArrayList<>();
        Termin termin1 = new Termin("CGE Projekt", 4, 40.5, new LocalDate(2016, 6, 24), new LocalTime(23, 55));
        Termin termin2 = new Termin("SWE Projekt", 5, 80, new LocalDate(2016, 6, 22), new LocalTime(23, 55));
        Termin termin3 = new Termin("MDP Projekt", 3, 30, new LocalDate(2016, 6, 13), new LocalTime(23, 55));

        returnList.add(termin1);
        returnList.add(termin2);
        returnList.add(termin3);

        return returnList;
    }

    private ArrayList<Termin> importTermineFromFileSystem() {
        //TODO : IMPLEMENT THIS
        return null;
    }

    /**
     * Opens new Activity to add a Duty to the list
     */
    public void addDuty(View view) {
        Intent intent = new Intent(this, AddDutyActivity.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        //Retrieve data in the intent
        String name = result.getStringExtra("name");
        String notes = result.getStringExtra("notes");
        final Calendar c = Calendar.getInstance();
        int year = result.getIntExtra("year", c.get(Calendar.YEAR));
        int month = result.getIntExtra("month", 1);
        int day = result.getIntExtra("day", 1);
        int hour = result.getIntExtra("hour", 23);
        int minute = result.getIntExtra("minute", 55);
        double effort = result.getDoubleExtra("effort", 10);
        int priority = result.getIntExtra("prio", Prio.NEUTRAL.getValue());

        Termin newTermin = new Termin(name, priority, effort, new LocalDate(year, month, day), new LocalTime(hour, minute));

        this.terminArrayList.add(newTermin);
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
