package com.example.andreas.studentmanager.models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andreas on 03.06.2016.
 */
public class Duty implements Serializable{

    static final long serialVersionUID =1L;

    private int dutyid;
    private String betreff;
    private String bemerkung;
    private int prio;
    private double aufwand;

    private transient ArrayList<Duty> zwischenTermine;

    private LocalDate eingabeTag;
    private LocalTime eingabeZeit;

    private LocalDate abgabeTag;
    private LocalTime abgabeZeit;

    public Duty(){

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

    public Duty(int dutyid, String betreff, int newprio, double aufwand, LocalDate abgabeTag, LocalTime abgabeZeit){
        this.dutyid=dutyid;
        this.betreff = betreff;
        this.prio = newprio;
        this.aufwand = aufwand;
        this.abgabeTag = abgabeTag;
        this.abgabeZeit = abgabeZeit;

        this.eingabeTag = LocalDate.now();
        this.eingabeZeit = LocalTime.now();
    }

    public int getDutyid() { return dutyid; }

    public void setDutyid(int dutyid) { this.dutyid = dutyid; }

    public String getBetreff() { return betreff; }

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
                + "Priorität: " +Integer.toString(this.prio) + " Geschätzter Aufwand: " + Double.toString(this.aufwand) + " Stunden";
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Duty) {
            Duty d = (Duty)o;
            return (this.dutyid == d.dutyid);
        }
        return false;
    }

}
