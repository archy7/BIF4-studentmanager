package com.example.andreas.studentmanager.models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;

/**
 * Created by Andreas on 03.06.2016.
 */
public class Duty {

    private String betreff;
    private String bemerkung;
    private int prio;
    private double aufwand;

    private ArrayList<Duty> zwischenTermine;

    private LocalDate eingabeTag;
    private LocalTime eingabeZeit;

    private LocalDate abgabeTag;
    private LocalTime abgabeZeit;

    private Duty(){

    }

    public Duty(String betreff, int newprio, double aufwand, LocalDate abgabeTag, LocalTime abgabeZeit){
        this.betreff = betreff;
        this.prio = newprio;
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

    public int getPrio(){
        return prio;
    }

    public void setPrio(int newprio){
        this.prio = newprio;
    }

    public double getAufwand(){
        return aufwand;
    }

    public void setAufwand(double aufwand){
        this.aufwand = aufwand;
    }

    public ArrayList<Duty> getZwischenTermine() {
        return zwischenTermine;
    }

    public void setZwischenTermine(ArrayList<Duty> zwischenTermine) {
        this.zwischenTermine = zwischenTermine;
    }

    public LocalDate getAbgabeTag() {
        return abgabeTag;
    }

    public void setAbgabeTag(LocalDate abgabeTag) {
        this.abgabeTag = abgabeTag;
    }

    public LocalTime getAbgabeZeit() {
        return abgabeZeit;
    }

    public void setAbgabeZeit(LocalTime abgabeZeit) {
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
                + "Priorität: " +Integer.toString(this.prio) + " Geschätzter Aufwand: " + Double.toString(this.aufwand) + " Stunden";
    }
}
