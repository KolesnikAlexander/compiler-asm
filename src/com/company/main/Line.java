package com.company.main;

import com.company.main.content.Content;

/**
 * Representation of a line in the assembly file inside the compiler.
 * It contains all the information about the line that is contributed and read
 * by many modules.
 */
public class Line {
    private String string;
    private final int id;
    boolean empty;
    boolean error;
    private int byteSize;
    public Content content;
    private int offset;
    private boolean done;
    public String listing;
    private String output;

    public Line(String string, int id) {
        if (string == null)
            throw new RuntimeException("Null string is given");
        this.string = string;
        this.id = id;
        this.empty = false;
        this.error = false;
        this.done = false;
        listing = new String();
        this.output = new String();
    }
    @Override
    public String toString() {
        return "Line{" +
                ", offset=" + offset +
                ", string='" + string + '\'' +
                ", id=" + id +
                ", empty=" + empty +
                ", error=" + error +
                ", done=" + done + "\n" +
                ",      Content:" + content + "\n" +
                ",      Listing:" + listing + "\n" +
                '}'+"\n";
    }
    public boolean isDone() {
        return done;
    }

    public void assignDone() {
        this.done = true;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getString() {
        return string;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isError() {
        return error;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void assignError()
    {
        this.error = true;
    }

}
