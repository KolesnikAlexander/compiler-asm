package com.company.main;

import java.io.*;

/**
 * Reads the line from file and represents it as a <code>Line</code> instance.
 */
public class LineReader {
    BufferedReader br;
    int counter;

    public LineReader(String filePath) throws FileNotFoundException {
        this.br = new BufferedReader(new FileReader(filePath));
        this.counter = 0;
    }
    /**
     * @return <code>String</code> or null if
     * the end of the stream has been reached.
     * Skips empty strings.
     */
    public Line read() throws IOException {
        String str = null;
       if ((str = br.readLine()) == null)
           return null;
        return new Line(str, counter++);
    }
}
