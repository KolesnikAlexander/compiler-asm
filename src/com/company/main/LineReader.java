package com.company.main;

import java.io.*;

/**
 * Created by alex6 on 18.05.2017.
 */
public class LineReader {
    BufferedReader br;
    int counter;

    public LineReader(String filePath) throws FileNotFoundException {
        this.br = new BufferedReader(new FileReader(filePath));
        this.counter = 0;
    }

    /**
     *
     * @return String or null if the end of the stream has been reached
     *
     * Skips empty strings
     */
    //BUG TO FIX: IF 1st STING IS EMPTY IT WILL RETURN IT!!!
    public Line read() throws IOException {
        String str = null;
       if ((str = br.readLine()) == null)
           return null;

       /* if((str.equals(""))||str.trim().length() <= 0)    // enable to skip empty strings
           return read();
       */
        return new Line(str, counter++);

    }
    private static boolean isSegDir(String string) {
        return true;
    }
}
