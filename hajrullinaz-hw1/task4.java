import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class task4 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int peopleInCircle = in.nextInt();
            int excluded = in.nextInt();
            int survivor = 0;

            for (int i = 1; i <= peopleInCircle; ++i) {
                survivor = (survivor + excluded) % i;
            }

            System.out.println(survivor + 1);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }
}
