import java.util.Scanner;
import java.util.concurrent.Callable;

class CallableUtil {
    public static Callable<String> getCallable() {
        Scanner scanner = new Scanner(System.in);
        return  = () -> scanner.next();

    }
}