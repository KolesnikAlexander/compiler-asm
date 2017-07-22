package com.company.main;

import com.company.main.check.KeyWordMatch;
import com.company.main.lstGen.InstrSizeGen;
import com.company.main.lstGen.InstrSizeGen2;
import com.company.main.lstGen.RegTable;
import com.company.main.segmentTable.SegmentTable;
import com.company.main.semanticAnalyzer.Semantic;
import com.company.main.usrIdTable.UsrIdTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by alex6 on 18.05.2017.
 */
public class LinesManager {
    private static LinkedList<Line> lines = new LinkedList<>();
    public static SegmentTable segmentTable = new SegmentTable();
    public static UsrIdTable usrIdTable = new UsrIdTable();


    public static void start(String filePath){
        new RegTable();
        LineReader reader;
        try {
            reader = new LineReader(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        }

        Line curLine;
        try {
            while ((curLine = reader.read()) != null){
                if(KeyWordMatch.isEnd(curLine.getString())){
                    lines.add(curLine);
                    break;
                }
                LineHandler.handle(curLine);
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                Semantic.analyze(curLine); // transfer to the 2nd pass. All ids should be identified before analyzing
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                InstrSizeGen.generateSize(curLine);
                    if (curLine.isError()) {
                        lines.add(curLine);
                        continue;
                    }
                lines.add(curLine);
            }
            for (Line line :lines) {
                InstrSizeGen2.generateSize(line);
            }
        } catch (IOException e) {
            System.out.println("Cannot read from file");
            return;
        }

    }
    public static LinkedList<Line> getLines() {
        return lines;
    }
}
