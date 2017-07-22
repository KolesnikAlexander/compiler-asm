package com.company.main.lstGen;

/**
 * Created by alex6 on 10.06.2017.
 */

import java.util.Hashtable;

/**
 * Register table
 */

public class RegTable {
   private static Hashtable<String, Integer> registers;

    public RegTable() {
        registers = new Hashtable<>(16);
        registers.put("AL", 0);
        registers.put("EAX", 0);

        registers.put("CL", 1);
        registers.put("ECX", 1);

        registers.put("DL", 2);
        registers.put("EDX", 2);

        registers.put("BL", 3);
        registers.put("EBX", 3);

        registers.put("AH", 4);
        registers.put("ESP", 4);

        registers.put("CH", 5);
        registers.put("EBP", 5);

        registers.put("DH", 6);
        registers.put("ESI", 6);

        registers.put("BH", 7);
        registers.put("EDI", 7);
    }

    public static int number(String reg){
        return registers.get(new String(reg.toUpperCase()));

    }


}
