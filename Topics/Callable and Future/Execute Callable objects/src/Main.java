import java.util.List;
import java.util.concurrent.*;


class FutureUtils {

    public static int executeCallableObjects(List<Future<Callable<Integer>>> items) {
        int sum = 0;
        for (int i = items.size() - 1; i >= 0; i--) {
            try {
                sum += items.get(i).get().call();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
        }
        return sum;
    }

}