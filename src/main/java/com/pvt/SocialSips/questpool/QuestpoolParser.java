package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.Trivia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class QuestpoolParser {

    private final static String PATH = "src/main/resources/questpools";
    private final static File DIR = new File(PATH);

    public static Set<Questpool> getAllStandardQuestpools(){
        Set<Questpool> standardQuestpools = new HashSet<>();

        if (!DIR.isDirectory())
            throw new IllegalStateException(DIR + " was not a directory!");

        for (File file : Objects.requireNonNull(DIR.listFiles())){
            if (!file.isFile()){
                System.out.println(file + " was not a file!");
                continue;
            }
            if (file.getName().split(" ")[0].equalsIgnoreCase("Trivia"))
                standardQuestpools.add(parseTrivia(file));
            else
                standardQuestpools.add(parseIcebreaker(file));

        }

        return standardQuestpools;
    }

    private static Questpool parseIcebreaker(File file){
        try{
            Scanner scanner = new Scanner(file);

            String name = scanner.nextLine();

            Set<Quest> quests = new HashSet<>();

            while(scanner.hasNext()){
                quests.add(new Icebreaker(scanner.nextLine()));
            }

            scanner.close();
            return new Questpool(name, QuestpoolType.ICEBREAKER, quests);

        } catch (FileNotFoundException e){
            System.err.println(file + " not found!");
            return null;
        }
    }

    private static Questpool parseTrivia(File file) {
        try{
            Scanner scanner = new Scanner(file);

            String name = scanner.nextLine();

            Set<Quest> quests = new HashSet<>();

            while(scanner.hasNext()){
                String prompt = scanner.nextLine();
                if (!scanner.hasNext()){
                    System.err.println("Bad format: " + prompt + " was the last line able to be read");
                    break;
                }

                String answers = scanner.nextLine();
                if (answers.split(";").length != 4){
                    System.err.println("answer string:" + answers + " doesnt have 4 options!");
                    continue;
                }
                quests.add(new Trivia(prompt, answers));
            }

            scanner.close();
            return new Questpool(name, QuestpoolType.TRIVIA, quests);

        } catch (FileNotFoundException e){
            System.err.println(file + " not found!");
            return null;
        }
    }

}
