package com.company.main;

import com.company.main.check.KeyWordMatch;
import com.company.main.lstGen.FirstPathProcessor;
import com.company.main.lstGen.SecondPathProcessor;
import com.company.main.lstGen.RegTable;
import com.company.main.segmentTable.SegmentTable;
import com.company.main.semanticAnalyzer.PrimaryFilters;
import com.company.main.usrIdTable.UsrIdTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by alex6 on 18.05.2017.
 * The main module that manages all other modules.
 * Provides sequential processing of the .asm file lines:
 *      First path: reading, handling, and generating opcodes
 *          for the commands that don't contain a direct address.
 *      Second path: generating opcodes for the commands that have direct address.
 */
public class LinesManager {
    private static LinkedList<Line> lines = new LinkedList<>(); //global lines storage
    public static SegmentTable segmentTable = new SegmentTable();// global segment table
    public static UsrIdTable usrIdTable = new UsrIdTable(); // global user identifiers table

    public static void start(String filePath){
        new RegTable(); //creating a register table
        LineReader reader;
        try {
            reader = new LineReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }
        Line curLine;
        try {
            while ((curLine = reader.read()) != null){ //first pass
                if(end(curLine)){ //stop reading when END directive reached
                    lines.add(curLine); //save line to a lines storage
                    break;
                }
                LineHandler.handle(curLine);
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                PrimaryFilters.filter(curLine);
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                FirstPathProcessor.generateOpcode(curLine);
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                lines.add(curLine);
            }
            for (Line line :lines) { //second pass
                SecondPathProcessor.generateOpcode(line);
            }
        } catch (IOException e) {
            System.out.println("Cannot read from file");
            return;
        }
    }
    private static boolean end(Line curLine){
        return KeyWordMatch.isEnd(curLine.getString());
    }
    public static LinkedList<Line> getLines() {
        return lines;
    }
}
