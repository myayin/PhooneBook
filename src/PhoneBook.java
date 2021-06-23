import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

//javac *.java
//jar -cvfm phonebook.jar manifest.MF *.class
//java -jar phonebook.jar delete "merve"
//java -jar phonebook.jar search "merve"
//java -jar phonebook.jar create "merve" "123"

public class PhoneBook {

    public static void main(String[] args) {
        int argumentLength = args.length;
        if (argumentLength == 0) {
            System.out.println("Arguments are missing...");
        } else {
            boolean isArgumentNameValid = isArgumentNameValid(args);
            boolean isArgumentLengthValid = isArgumentLengthValid(args);

            if (!isArgumentNameValid) {
                System.out.println("Argument name is not valid... Try with create, delete or search...");
            } else if (!isArgumentLengthValid) {
                System.out.println("Argument length is not valid...");
            } else {
                action(args);
            }
        }
    }

    private static void action(String[] args) {
        String firstArgument = args[0];
        if (firstArgument.equals(ArgumentType.CREATE.getValue())) {
            String key = args[1];
            List<PhoneBookItem> phoneBookItemList = readFile();
            boolean hasUser = false;
            for (PhoneBookItem phoneBookItem : phoneBookItemList) {
                if (phoneBookItem.getName().trim().equals(key)) {
                    hasUser = true;
                    break;
                }
            }
            if (hasUser) {
                System.out.println("The user exists. Please try with another keywords...");
            } else {
                create(args);
            }
        } else if (firstArgument.equals(ArgumentType.DELETE.getValue())) {
            String result = delete(args);
            System.out.println(result);
        } else if (firstArgument.equals(ArgumentType.SEARCH.getValue())) {
            String result = search(args);
            if (result.isEmpty()) {
                System.out.println("No data found...");
            } else {
                System.out.println(result);
            }
        } else {
            System.out.println("Try with create, delete or search...");
        }
    }

    private static void create(String[] args) {
        File file = new File("C:\\Users\\Merve\\Desktop\\PhoneBook\\src\\phonebook-db.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fileWriter);
            br.write("\n" + args[1] + " " + args[2]);
            br.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String search(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        String searchKey = args[1];
        List<PhoneBookItem> phoneBookItemList = readFile();
        for (PhoneBookItem phoneBookItem : phoneBookItemList) {
            if (phoneBookItem.getName().contains(searchKey)) {
                stringBuilder
                        .append(phoneBookItem.getName())
                        .append(" ")
                        .append(phoneBookItem.getPhone())
                        .append("\n");
            }
        }
        return stringBuilder.toString();

    }

    private static void removeLine(String lineContent) throws IOException {
        File file = new File("C:\\Users\\Merve\\Desktop\\PhoneBook\\src\\phonebook-db.txt");
        List<String> out = Files.lines(file.toPath())
                .filter(line -> !line.contains(lineContent))
                .collect(Collectors.toList());
        Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String delete(String[] args) {
        String key = args[1];
        String result = "There is no user named " + key;
        List<PhoneBookItem> phoneBookItemList = readFile();
        for (PhoneBookItem phoneBookItem : phoneBookItemList) {
            if (phoneBookItem.getName().trim().equals(key)) {
                try {
                    removeLine(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = phoneBookItem.getName() + " REMOVED";
                break;
            }
        }
        return result;
    }

    private static boolean isArgumentNameValid(String[] args) {
        String firstArgument = args[0];
        for (ArgumentType argumentType : ArgumentType.values()) {
            if (argumentType.getValue().equals(firstArgument)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isArgumentLengthValid(String[] args) {
        String firstArgument = args[0];
        int argumentLength = args.length;
        if (firstArgument.equals(ArgumentType.CREATE.getValue()) && argumentLength == 3) {
            return true;
        } else return (firstArgument.equals(ArgumentType.DELETE.getValue()) ||
                firstArgument.equals(ArgumentType.SEARCH.getValue())) && argumentLength == 2;
    }

    private static List<PhoneBookItem> readFile() {
        List<PhoneBookItem> phoneBookItemList = new ArrayList<>();
        try {
            File f = new File("C:\\Users\\Merve\\Desktop\\PhoneBook\\src\\phonebook-db.txt");
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (Character.isDigit(chars[i])) {
                        phoneBookItemList.add(new PhoneBookItem(
                                line.substring(0, i), line.substring(i, chars.length)
                        ));
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return phoneBookItemList;
    }
}
