package com.example.andreas.studentmanager.models;

/**
 * Created by Andreas on 03.06.2016.
 */
public enum Prio {
    SEHR_HOCH (5),
    HOCH (4),
    NEUTRAL (3),
    GERING (2),
    SEHR_GERING (1);

    private int value;

    private Prio(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Prio makePrio(int newprio){
        Prio returnPrio = null;
        switch (newprio){
            case 1: returnPrio = SEHR_GERING; break;
            case 2: returnPrio = GERING; break;
            case 3: returnPrio = NEUTRAL; break;
            case 4: returnPrio = HOCH; break;
            case 5: returnPrio = SEHR_HOCH; break;
            default: throw new RuntimeException("Unaccpetable priority '" + Integer.toString(newprio) + "' chosen by User. Prio.makePrio()");
        }

        return returnPrio;
    }

    public static int[] getValuesAsIntArray(){
        int[] intArray= {1,2,3,4,5};
        return intArray;
    }

    public static Integer[] getValuesAsIntegerArray(){
        Integer[] integers = {new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5)};
        return integers;
    }
}
