package com.company.program.usrIdTable;

/**
 * Created by alex6 on 05.06.2017.
 */
public class Id {
    String name;
    String type;
    int value; // offset from the beginning of the segment. It = -1 at 1st pass
    String attr;

    public Id(String name, String type, int value, String attr) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.attr = attr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id id = (Id) o;

        return name.toUpperCase().equals(id.name.toUpperCase());
    }

    @Override
    public String toString() {
//        return name +"  .......   " +
//                type + "   "+
//                value +"  "+
//                attr +"\n";
        String hexValue =
                String.format("%"+4+"s", Integer.toHexString(value).toUpperCase())
                        .replace(' ', '0');
        return String.format("%1$-" + 4 + "s",  name) +"  ..............   " +
                String.format("%1$-" + 7 + "s",  type) + "   "+
                String.format("%1$-" + 4 + "s",  hexValue) +"  "+
                String.format("%1$-" + 4 + "s",  attr) +"\n";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String getAttr() {
        return attr;
    }
}
