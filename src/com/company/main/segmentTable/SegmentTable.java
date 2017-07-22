package com.company.main.segmentTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex6 on 18.05.2017.
 */
public class SegmentTable {
    private List<Segment> table;

    public SegmentTable() {
        this.table = new ArrayList<>(5);
    }

    public void addSegment(String name){
            table.add(new Segment(name));
    }
    public boolean containsSegment(Segment segment){
            return table.contains(segment);
    }
    public boolean contains(String name){
        Segment forCompare = new Segment(name);
        return table.contains(forCompare);
    }


    @Override
    public String toString() {
        return "SegmentTable: \n"+ table;
    }

    public String toListing(){
        String result = "           SEGMENT TABLE \n";
        for (Segment segment:this.table){
            result+=segment.toString();
        }
        return result;
    }
    public Segment getSegmentByName(String name){
        for (Segment segment :table) {
            if (segment.equals(new Segment(name)))
                return segment;
        }
        return null;
    }
}
