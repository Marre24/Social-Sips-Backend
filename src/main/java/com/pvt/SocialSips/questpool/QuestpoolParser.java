package com.pvt.SocialSips.questpool;

import com.pvt.SocialSips.quest.Icebreaker;
import com.pvt.SocialSips.quest.Quest;
import com.pvt.SocialSips.quest.Trivia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QuestpoolParser {

    private final static Questpool ICE_BREAKER_MISC = new Questpool("Misc", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(
            new Icebreaker("If you could have a conversation with anyone, living or dead, who would you choose and why?"),
            new Icebreaker("If you could instantly master any skill or hobby, what would it be and why?"),
            new Icebreaker("If your life were a movie, what would the title be?"),
            new Icebreaker("If you could live inside any movie or TV show for a week, which one would you pick?"),
            new Icebreaker("If your personality had a theme song, what would it be?"),
            new Icebreaker("If someone gave you a one-way ticket to anywhere in the world right now, where would you go—and why?"),
            new Icebreaker("If your day had a rating like a movie (G, PG, R…), what would today be and why?"),
            new Icebreaker("If animals could talk, which one do you think would be the rudest?"),
            new Icebreaker("If your name had to be a sound effect (like boing or zap), what would it be?"),
            new Icebreaker("If you opened a restaurant with zero cooking skills, what would you name it?"),
            new Icebreaker("If you could ask your future self one yes/no question, what would it be?"),
            new Icebreaker("Is there a quote or phrase you think about more often than you'd admit?"),
            new Icebreaker("If your phone had a “most used thought” app, what would it say you think about the most?"),
            new Icebreaker("If you could pause time for 24 hours, what’s the first thing you’d do?"),
            new Icebreaker("If your mood today were a color or a weather forecast, what would it be?"),
            new Icebreaker("If your life had background music, what genre would be playing this week?"),
            new Icebreaker("If you could swap lives with a fictional character for a day, who would it be—and what would you do?"),
            new Icebreaker("What's the biggest most dangerous animal that you are convinced that you could take on in a fight?"),
            new Icebreaker("What’s the most random or unusual thing you’ve ever Googled?"),
            new Icebreaker("What’s a small thing that instantly makes your day better?"),
            new Icebreaker("What's the dumbest thing you’ve ever done?"),
            new Icebreaker("What’s a food you wish you liked but just can’t get into?"),
            new Icebreaker("What’s the weirdest compliment you’ve ever received?"),
            new Icebreaker("What’s something totally random that you know a lot about?"),
            new Icebreaker("What’s the most \"you\" item in your bag or pocket right now?"),
            new Icebreaker("What’s your most irrational fear that you secretly know is ridiculous?"),
            new Icebreaker("What’s a conspiracy theory you don’t believe but still love talking about?"),
            new Icebreaker("What’s a simple moment from this year that made you unexpectedly happy?"),
            new Icebreaker("What do people often assume about you that’s totally wrong?"),
            new Icebreaker("What’s something you’ve always wanted to try, but haven’t yet?"),
            new Icebreaker("What’s something you believed as a kid that still makes you laugh now?"),
            new Icebreaker("What’s your go-to \"fun fact\" when you need to introduce yourself?"),
            new Icebreaker("What’s a really specific smell that instantly brings back a memory for you?"),
            new Icebreaker("What’s something totally ordinary that you find oddly satisfying?"),
            new Icebreaker("What’s a \"little win\" recently that made you feel way prouder than it should have?")
    )));

    private final static Questpool ICE_BREAKER_WOULD_YOU_RATHER = new Questpool("Would you rather", QuestpoolType.ICEBREAKER, new HashSet<>(List.of(
            new Icebreaker("Would you rather be chronically overdressed or underdressed?"),
            new Icebreaker("Would you rather never eat cheese again or never eat chocolate again?"),
            new Icebreaker("Would you rather remember everything you read, or have the ability to forget exactly what you want?"),
            new Icebreaker("Would you rather never get too cold, or never get too hot?"),
            new Icebreaker("Would you rather walk on all fours wherever you go or hop on one leg wherever you go?"),
            new Icebreaker("Would you rather be a genius that nobody believes or be an idiot that everybody believes?"),
            new Icebreaker("Would you rather every food you eat is too salty, or every drink you drink is too sweet?"),
            new Icebreaker("Would you rather be isolated with your favorite person for 1 year, or be isolated with your most hated person for 1 month?"),
            new Icebreaker("Would you rather go on a sun vacation or ski vacation?"),
            new Icebreaker("Would you rather bite your cheek every day or stub your toe every day?"),
            new Icebreaker("Would you rather never eat vegetables again or never eat fruit again?"),
            new Icebreaker("Would you rather have a good job with awful colleagues or a boring job with the best colleagues?"),
            new Icebreaker("Would you rather be a superhero or a supervillain?"),
            new Icebreaker("Would you rather have to always tell the truth or always have to lie?"),
            new Icebreaker("Would you rather be able to talk every language or talk to all animals?"),
            new Icebreaker("Would you rather always feel hungry or always feel thirsty?"),
            new Icebreaker("Would you rather live in a world without internet, or live in a world without music?"),
            new Icebreaker("Would you rather find out when you’re going to die or how you’re going to die?"),
            new Icebreaker("Would you rather have face blindness or name blindness?"),
            new Icebreaker("Would you rather master every musical instrument, or master every sport?")
    )));

    private final static Questpool TRIVIA_HISTORY = new Questpool("History", QuestpoolType.TRIVIA, new HashSet<>(List.of(
            new Trivia("Who was the first emperor of China?", "Qin Shi Huang;Liu Bang;Sun Yat-sen;Kublai Khan"),
            new Trivia("In what year did the Titanic sink?", "1912;1905;1920;1898"),
            new Trivia("What was the name of the ship that carried the Pilgrims to America in 1620?", "The Mayflower;The Discovery;The Santa Maria;The Endeavour"),
            new Trivia("Who was the British Prime Minister during World War II?", "Winston Churchill;Neville Chamberlain;Margaret Thatcher;Tony Blair"),
            new Trivia("Which empire was ruled by Julius Caesar?", "The Roman Empire;The Byzantine Empire;The Ottoman Empire;The Holy Roman Empire"),
            new Trivia("Who was the first woman to fly solo across the Atlantic Ocean?", "Amelia Earhart;Bessie Coleman;Jacqueline Cochran;Harriet Quimby"),
            new Trivia("The Berlin Wall fell in which year?", "1989;1980;1991;1978"),
            new Trivia("What was the name of the first successful English colony in America?", "Jamestown;Plymouth;Roanoke;New Amsterdam"),
            new Trivia("Who was the first president of the United States?", "George Washington;Thomas Jefferson;John Adams;James Madison"),
            new Trivia("In which year did World War I begin?", "1914;1912;1916;1918"),
            new Trivia("The Great Fire of London occurred in which year?", "1666;1588;1750;1801"),
            new Trivia("Who was the famous queen of ancient Egypt known for her relationships with Julius Caesar and Mark Antony?", "Cleopatra;Nefertiti;Hatshepsut;Tiy"),
            new Trivia("What was the name of the ship that carried the first successful English settlers to Virginia in 1607?", "The Susan Constant;The Mayflower;The Discovery;The Godspeed"),
            new Trivia("Who discovered America in 1492?", "Christopher Columbus;John Cabot;Hernán Cortés;Ferdinand Magellan"),
            new Trivia("The Cold War primarily involved which two superpowers?", "United States and Soviet Union;United States and Germany;United States and China;Soviet Union and Japan"),
            new Trivia("What was the name of the famous battle in 1066 where William the Conqueror defeated King Harold II of England?", "Battle of Hastings;Battle of Agincourt;Battle of Waterloo;Battle of Bosworth Field"),
            new Trivia("What year did the United States declare independence from Great Britain?", "1776;1775;1783;1791"),
            new Trivia("Who was the first woman to win a Nobel Prize?", "Marie Curie;Rosa Parks;Florence Nightingale;Ada Lovelace"),
            new Trivia("Which war ended with the signing of the Treaty of Versailles in 1919?", "World War I;World War II;The American Civil War;The Napoleonic Wars"),
            new Trivia("Who was the leader of the Soviet Union during World War II?", "Joseph Stalin;Vladimir Lenin;Leon Trotsky;Nikita Khrushchev"),
            new Trivia("What is the name of the empire that ruled the Middle East from 883 to 627 BC?", "The Assyrian Empire;The Babylonian Empire;The Merovingian Empire;The Persian Empire"),
            new Trivia("Whose adopted son was Emperor Augustus?", "Julius Caesar;Diogenes;Nero;Mark Antony"),
            new Trivia("Which major city put the first urban tram line into service in 1832?", "New York;London;Berlin;Vienna"),
            new Trivia("In what year did the reunification of Germany take place?", "1990;1989;1991;1992"),
            new Trivia("Which was the last country to join the European Union in the year 2013?", "Croatia;Romania;Bulgaria;Estonia")
    )));

    private final static Questpool TRIVIA_MISC = new Questpool("Misc", QuestpoolType.TRIVIA, new HashSet<>(List.of(
            // Culture
            new Trivia("In literature, what do you call a poem or text where the graphic arrangement of words on the page forms a picture or design?", "Calligram;Ambigram;Tercet;Sonnet"),
            new Trivia("Which painter made Campbell’s soup cans famous?", "Andy Warhol;Roy Lichtenstein;David Hockney;Edward Hopper"),
            new Trivia("How many keys are there on a piano keyboard?", "88;80;100;62"),
            new Trivia("Which famous movie star starred in the classic films \"Roman Holiday,\" \"Breakfast at Tiffany's\", \"Sabrina\", and “War and Peace”?", "Audrey Hepburn;Lauren Bacall;Sophia Loren;Vivien Leigh"),
            new Trivia("Who wrote the novel \"1984\"?", "George Orwell;Aldous Huxley;Ray Bradbury;J.R.R. Tolkien"),
            new Trivia("Which famous artist is known for his work featuring \"drip\" painting, including \"No. 5, 1948\"?", "Jackson Pollock;Pablo Picasso;Vincent van Gogh;Andy Warhol"),

            // Geography
            new Trivia("Which river is the longest in the world?", "Nile River;Amazon River;Mississippi River;Yangtze River"),
            new Trivia("What is the largest country in the world by land area?", "Russia;China;United States;Canada"),
            new Trivia("Which country is known as the Land of the Rising Sun?", "Japan;China;South Korea;Thailand"),
            new Trivia("Which two countries share the longest international border in the world?", "United States and Canada;Russia and China;Brazil and Argentina;India and China"),
            new Trivia("Which country has the most official languages?", "Bolivia;India;Switzerland;South Africa"),
            new Trivia("Which is the only country in the world to have a flag that is not rectangular?", "Nepal;Switzerland;Vatican City;Japan"),
            new Trivia("What is the name of the largest island in the Mediterranean Sea?", "Sicily;Corsica;Cyprus;Sardinia"),
            new Trivia("What is the longest river in Asia?", "Yangtze River;Mekong River;Ganges River;Brahmaputra River"),
            new Trivia("Which country is home to the famous historical site Petra?", "Jordan;Egypt;Iraq;Saudi Arabia"),
            new Trivia("What is the capital city of Canada?", "Ottawa;Toronto;Vancouver;Montreal"),
            new Trivia("In which country would you find the ancient city of Timbuktu?", "Mali;Sudan;Egypt;Chad"),
            new Trivia("Which city is known as the \"City of a Hundred Spires\"?", "Prague;Vienna;Brussels;Berlin"),
            new Trivia("What is the official language of Brazil?", "Portuguese;Spanish;French;English"),
            new Trivia("What is the largest city by population in the world?", "Tokyo;New York;Shanghai;Beijing"),
            new Trivia("Which city in the country of India is the largest in terms of population?", "Mumbai;Delhi;Kolkata;Jaipur"),

            // Pop Culture
            new Trivia("In the TV show Stranger Things, what is the name of the alternate dimension?", "The Upside Down;The Flipside;The Downworld;The Undershadow"),
            new Trivia("Who played Barbie in the 2023 Barbie movie?", "Margot Robbie;Emma Stone;Florence Pugh;Blake Lively"),
            new Trivia("Who famously interrupted Taylor Swift’s 2009 VMA speech?", "Kanye West;Drake;Justin Timberlake;Eminem"),

            // Science
            new Trivia("What planet is known for having a giant red storm?", "Jupiter;Mars;Neptune;Mercury"),
            new Trivia("What part of the cell is known as the “powerhouse”?", "Mitochondria;Nucleus;Ribosome;Golgi apparatus"),
            new Trivia("How many bones does an adult human typically have?", "206;198;212;182"),
            new Trivia("What is the chemical symbol for gold?", "Au;Ag;Gd;Go"),
            new Trivia("Which organ is primarily responsible for filtering blood in the human body?", "Kidneys;Liver;Heart;Lungs"),
            new Trivia("What is the fourth state of matter called, after the states of gas, liquid and solid?", "Plasma;Jelly;Ether;Molt"),
            new Trivia("By what name is the patella more generally called?", "Kneecap;Paella;Spread;Shoulder blade"),
            new Trivia("Which gas planet is the largest planet in the Solar System?", "Jupiter;Saturn;Uranus;Neptune"),
            new Trivia("What is the common, more popular name for an orca?", "Killer whale;White shark;Calf;Beluga"),

            // Additional
            new Trivia("Which country drinks the most amount of coffee per person?", "Finland;Italy;Colombia;Sweden"),
            new Trivia("What is the collective name for a group of unicorns?", "A blessing;A sparkle;A spell;A hoard"),
            new Trivia("How many times per day does the average American open their fridge?", "33;5;22;15"),
            new Trivia("What color is an airplane’s famous black box?", "Orange;Red;Black;White"),
            new Trivia("What is the name of a duel with three people involved?", "A truel;A triage;A tryst;A Triad")
    )));


    public static Set<Questpool> getAllStandardQuestpools(){
        Set<Questpool> standardQuestpools = new HashSet<>();

        standardQuestpools.add(ICE_BREAKER_MISC);
        standardQuestpools.add(ICE_BREAKER_WOULD_YOU_RATHER);
        standardQuestpools.add(TRIVIA_HISTORY);
        standardQuestpools.add(TRIVIA_MISC);


        return standardQuestpools;
    }

}
