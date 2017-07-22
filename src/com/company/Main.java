package com.company;

import com.company.main.LinesManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Assembly translator ");
        System.out.println("Created by Kolesnik Alexander ");
        System.out.println("Group: KV-51");
        System.out.println("2017 ");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.println(".ASM file: "); //assembly file
        String filePath = scanner.next();

        System.out.println(".LST file: ");// listing file. It will be created if it doesn't exist
        String lstPath = scanner.next();

        //Start compiler and print listing if no IO problems occurred
        if(LinesManager.start(filePath))
            PrintLst.print(lstPath, LinesManager.getLines());
    }
}
