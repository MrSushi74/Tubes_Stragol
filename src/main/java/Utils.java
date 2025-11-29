import java.util.Scanner;

public class Utils {
    private static Scanner sc;

    static {
        sc = new Scanner(System.in);
    }

    public static int getInt(){
        return sc.nextInt();
    }

    public static String getString(){
        return sc.nextLine();
    }
}
