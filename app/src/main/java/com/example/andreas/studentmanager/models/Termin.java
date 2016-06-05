package com.example.andreas.studentmanager.models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

/**
 * Created by Andreas on 03.06.2016.
 */
public class Termin {

    private String betreff;
    private String bemerkung;
    private Prio prio;
    private double aufwand;

    private ArrayList<Termin> zwischenTermine;

    private LocalDate eingabeTag;
    private LocalTime eingabeZeit;

    private LocalDate abgabeTag;
    private LocalTime abgabeZeit;

    private Termin(){

    }

    public Termin(String betreff, int newprio, double aufwand, LocalDate abgabeTag, LocalTime abgabeZeit){
        this.betreff = betreff;
        this.setPrio(newprio);
        this.aufwand = aufwand;
        this.abgabeTag = abgabeTag;
        this.abgabeZeit = abgabeZeit;

        this.eingabeTag = LocalDate.now();
        this.eingabeZeit = LocalTime.now();
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public Prio getPrio(){
        return prio;
    }

    public void setPrio(Prio newprio){
        this.prio = newprio;
    }

    public void setPrio(int newprio){
        for(Prio prio : Prio.values()){
            if(prio.getValue() == newprio){
                this.prio = prio;
            }
        }
    }

    public double getAufwand(){
        return aufwand;
    }

    public void setAufwand(double aufwand){
        this.aufwand = aufwand;
    }

    public ArrayList<Termin> getZwischenTermine() {
        return zwischenTermine;
    }

    public void setZwischenTermine(ArrayList<Termin> zwischenTermine) {
        this.zwischenTermine = zwischenTermine;
    }

    public LocalDate getAbgabeTag() {
        return abgabeTag;
    }

    public void setAbgabeTag(LocalDate abgabeTag) {
        this.abgabeTag = abgabeTag;
    }

    public LocalTime getTime() {
        return abgabeZeit;
    }

    public void setTime(LocalTime abgabeZeit) {
        this.abgabeZeit = abgabeZeit;
    }

    public LocalDate getEingabeTag(){
        return this.eingabeTag;
    }

    public LocalTime getEingabeZeit(){
        return this.eingabeZeit;
    }

    @Override
    public String toString(){
        return this.betreff + " fällig am " + this.abgabeTag.toString() + ", um " + this.abgabeZeit + "\n"
                + "Priorität: " +Integer.toString(this.prio.getValue()) + " Geschätzter Aufwand: " + Double.toString(this.aufwand) + " Stunden";
    }
}
