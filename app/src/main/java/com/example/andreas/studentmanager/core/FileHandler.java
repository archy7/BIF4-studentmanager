package com.example.andreas.studentmanager.core;

import android.content.Context;
import android.util.Log;

import com.example.andreas.studentmanager.models.Duty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by MK on 06.06.2016.
 */
public class FileHandler {
    private static FileHandler instance=null;
    private int newId=0;
    private String path="/duties";
    private Context context;

    public static FileHandler getInstance(){
        if(instance==null)
            instance=new FileHandler();
        return instance;
    }

    private FileHandler(){
    }

    public void setContext(Context context){
        this.context=context;
        File dir=new File(context.getFilesDir()+path);
        dir.mkdir();
    }

    public ArrayList<Duty> readDutiesFromFile(){

        Log.d("Files", context+path);

        ArrayList<Duty> duties=new ArrayList<Duty>();

        File f = new File(context.getFilesDir()+path);
        File file[] = f.listFiles();
        if(file!=null)
            for (int i=0; i < file.length; i++)
            {
                try {
                    ObjectInputStream input;
                    input = new ObjectInputStream(new FileInputStream(file[i]));
                    Duty d=(Duty) input.readObject();
                    duties.add(d);
                    if(d.getDutyid()>=newId)
                        newId=d.getDutyid();
                    input.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                //Log.d("Files", "FileName:" + file[i].getName());

            }

        return duties;
    }

    public void writeDuty(Duty duty){

        newId++;

        String filename = "duty"+newId+".srl";
        duty.setDutyid(newId);


        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir()+path,"")+ File.separator+filename));
            out.writeObject(duty);
            out.close();
            Log.d("Duties", "Duty:" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editDuty(Duty duty){
        /*
         * Aktuelles File leeren
         */
        String filename = "duty"+duty.getDutyid()+".srl";
        File f=new File(context.getFilesDir()+path, "");
        PrintWriter pw= null;
        try {
            pw = new PrintWriter(f);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*
         * File neu schreiben
         */
        writeDuty(duty);
    }

    public void deleteDuty(int dutyid){
        String filename = "duty"+dutyid+".srl";
        File f=new File(context.getFilesDir()+path, "");
        f.delete();
    }

    public void exportCSV(){

    }

}
