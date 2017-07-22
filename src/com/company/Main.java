package com.company;

import com.company.main.LineHandler;
import com.company.main.LineReader;
import com.company.main.LinesManager;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Assembly translator ");
        System.out.println("Created by Kolesnik Alexander ");
        System.out.println("Group: KV-51");
        System.out.println("2017 ");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.println(".ASM file: ");
        String filePath = scanner.next();

        System.out.println(".LST file: ");
        String lstPath = scanner.next();

        LinesManager.start(filePath);
        PrintLst.print(lstPath, LinesManager.getLines());
    }
}
