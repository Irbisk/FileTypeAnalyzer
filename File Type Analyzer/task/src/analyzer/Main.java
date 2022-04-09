package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Main {
    final static int a = 3;
    final static int m = 11;


    public static void main(String[] args) throws InterruptedException {

        File inputFolder = new File(args[0]);
        File patternFile = new File(args[1]);

        File[] files = inputFolder.listFiles();

        List<PatternType> patterns = new ArrayList<>();

        try (Scanner scanner = new Scanner(patternFile)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] type = line.replaceAll("\"", "").split(";");
                int priority = Integer.parseInt(type[0]);
                String pattern = type[1];
                String printType = type[2];
                patterns.add(new PatternType(priority, pattern, printType));
            }
            Collections.sort(patterns);
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + args[1]);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < files.length; i++) {
            int finalI = i;
            executorService.submit(() -> {
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    byte[] fileBytes = Files.readAllBytes(Paths.get(String.valueOf(files[finalI])));
                    for (byte b : fileBytes) {
                        stringBuilder.append((char) b);
                    }
                    String t = stringBuilder.toString();
                    runProgram(t, patterns, files[finalI].getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);
    }

    public static void runProgram(String t, List<PatternType> patterns, String fileName) {

        boolean found = false;

        for (int i = 0; i < patterns.size(); i++) {
            System.out.println(i);
            String s = patterns.get(i).getPattern();
            if (s.length() > t.length()) {
                continue;
            }
            int hash = hash(s, 0, 0);
            if (algorithmRK(t, s, hash)) {
                System.out.println(fileName + ": "+ patterns.get(i).getPrintType());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println(fileName + ": Unknown file type");
        }

    }

    public static boolean algorithmRK(String t, String s, int hash) {


        boolean found = false;

        String check = t.substring(t.length() - s.length());
        int checkHash = hash(s, 0, 0);
        if (checkHash == hash) {
            boolean equals = true;
            for (int i = 0; i < check.length(); i++) {
                if (check.charAt(i) != s.charAt(i)) {
                    equals = false;
                    break;
                }
            }
            if (equals) {
                found = true;
            }
        }

        if (!found && t.length() > s.length()) {
            return algorithmRK(t.substring(0, t.length() - 1), s, hash);
        }

        return found;
    }

    public static int hash(String s, int result, int pow) {
        if (pow == 0) {
            result += s.substring(0, 1).hashCode() * Math.pow(a, pow) + s.substring(1, 2).hashCode() * Math.pow(a, pow + 1);
            return hash(s.substring(2), result, pow + 2);
        } else if (s.length() > 1) {
            result += s.substring(0, 1).hashCode() * Math.pow(a, pow);
            return hash(s.substring(1), result, pow + 1);
        } else {
            result += s.hashCode() * Math.pow(a, pow);
            result = Math.floorMod(result, m);
        }
        return result;
    }

    public static void naive(String t, String s, String printType) {
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(t);
        if (matcher.find()) {
            System.out.println(printType);
        } else {
            System.out.println("Unknown file type");
        }
    }


    public static void algorythmKMP(String t, List<PatternType> patterns, String fileName) {
        String s = "";
        String printType = "";
        boolean found = false;
        for (int k = 0; k < patterns.size(); k++) {
            s = patterns.get(k).getPattern();
            printType = patterns.get(k).getPrintType();

            int M = s.length();
            int N = t.length();

            int lps[] = prefixFunc(s);
            int j = 0;

            int i = 0;
            while (i < N) {
                if (s.charAt(j) == t.charAt(i)) {
                    j++;
                    i++;
                }
                if (j == M) {
                    System.out.println(fileName + ": " + printType);
                    found = true;
                    j = lps[j - 1];
                }

                else if (i < N && s.charAt(j) != t.charAt(i)) {

                    if (j != 0)
                        j = lps[j - 1];
                    else
                        i = i + 1;
                }

            }
        }
        if (!found) {
            System.out.println(fileName + ": Unknown file type");
        }

    }

    public static int[] prefixFunc(String s) {
        String[] argsArray = s.split("");
        int[] prefixFunc = new int[s.length()];
        prefixFunc[0] = 0;
        for (int i = 1; i < argsArray.length; i++) {
            int j = prefixFunc[i - 1];
            int k = i - 1;
            while (true) {
                if (argsArray[i].equals(argsArray[j])) {
                    prefixFunc[i] = prefixFunc[k] + 1;
                    break;
                } else {
                    if (j == 0) {
                        prefixFunc[i] = 0;
                        break;
                    }
                    k = j - 1;
                    j = prefixFunc[j - 1];

                }
            }
        }
        return prefixFunc;
    }
}
