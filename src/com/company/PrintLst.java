package com.company;

import com.company.program.Line;
import com.company.program.LinesManager;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Printing the listing to the file
 */
public class PrintLst {

    public static void print(String lstPath, LinkedList<Line> lines){
        int errorCounter = 0;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(lstPath);
        } catch (FileNotFoundException e) {
            System.out.println("Listing file not found");
            return;
        }
        // Console output
        System.out.println("Successfully translated");
        for (Line line : lines) {
            if (line.isEmpty()){
                writer.println();
                continue;
            }
            writer.print(String.format("%"+4+"s", Integer.toHexString(line.getOffset()).toUpperCase()).replace(' ', '0'));
            writer.print("  ");
                String lst = String.format("%1$-" + 15 + "s", line.getListing());
            writer.print(lst.toUpperCase());
                String string = String.format("%1$-" + 40 + "s", line.getString());//line.getString();//String.format("%1$-" + 15 + "s", line.getString());
            writer.print(string);
            if(line.isError()){
                errorCounter++;
                writer.print("ERROR");
            }
            writer.println();
        }
        writer.println();
        writer.print(LinesManager.segmentTable.toListing());
        writer.println();
        writer.print(LinesManager.usrIdTable.toListing());
        writer.println();
        writer.println("ERRORS: "+errorCounter);
        writer.println();
        writer.println("Created by Alexander Kolesnik, group: KV-51, 2017");
        writer.flush();
        System.out.println(errorCounter+" Severe Errors"); // Console output
    }
}
