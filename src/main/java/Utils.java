import java.util.Scanner;

public class Utils {
    private static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    public static int getInt(String message){
        System.out.println(message);
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    public static String getString(String message){
        System.out.println(message);
        return sc.nextLine();
    }
}
