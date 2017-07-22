package com.company.program.usrIdTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.company.program.check.KeyWords.*;

/**
 * Created by alex6 on 05.06.2017.
 */
public class UsrIdTable {
    private List<Id> table;

    public UsrIdTable() {
        this.table = new ArrayList<>(5);
    }

    public void addId(String name, String type, int value, String attr){
        table.add(new Id(name, type, value, attr));
    }
    public boolean nameIsForbidden(String name) {
        return ( this.contains(name) || isAKeyWord(name));
    }
    public boolean contains(String name){
        return table.contains(new Id(name,null,0,null));
    }
    private boolean isAKeyWord(String name){
        Pattern usrIdPattern = Pattern.compile("^(?:"+RESERVED_WORDS+")$",
                Pattern.CASE_INSENSITIVE);
        return  usrIdPattern.matcher(name).find();

    }
    public int getValueByName(String name){
        for (Id id : table) {
            if (id.name.toUpperCase().equals(name.toUpperCase()))
                return id.value;
        }
        return -1;
    }
    public Id getIdByName(String name){
        for (Id id : table) {
            if (id.name.toUpperCase().equals(name.toUpperCase()))
                return id;
        }
        return null;
    }


    @Override
    public String toString() {
        return "UsrIdTable: \n"+ table;
    }

    public String toListing(){
        String result = "           USER IDENTIFIER TABLE \n";
        for (Id id:this.table){
            result+=id.toString();
        }
        return result;
    }
}

