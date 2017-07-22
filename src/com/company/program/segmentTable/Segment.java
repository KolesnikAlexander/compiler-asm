package com.company.program.segmentTable;

public class Segment {
        private String name;
        private String size;
        private int length;
        private String align;
        private String combClass;

        public Segment(String name) {
            this.name = name;
            this.align = "PARA";
            this.combClass = "NONE";
            this.size = "32 Bit";
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Segment segment = (Segment) o;

        return name.toUpperCase().equals(segment.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void setLength(int length) {
            this.length = length;
        }

        @Override
        public String toString() {
            String hexLength =
                    String.format("%"+4+"s", Integer.toHexString(length).toUpperCase())
                            .replace(' ', '0');
            return String.format("%1$-" + 4 + "s",  name) +"  ..............   " +
                    String.format("%1$-" + 6 + "s",  size) + "   "+
                    String.format("%1$-" + 4 + "s",  hexLength) +"  "+
                    align+"   "+
                    combClass + "\n";
        }
}
