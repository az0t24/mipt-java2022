import java.util.InputMismatchException;
import java.util.Scanner;

public class task2 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int numPoints = in.nextInt();

            int x1 = 0;
            int x2 = 0;
            int y1 = 0;
            int y2 = 0;

            int square = 0;

            x1 = in.nextInt();
            y1 = in.nextInt();

            int x1Start = x1;
            int y1Start = y1;

            for (int i = 1; i < numPoints; ++i) {
                x2 = in.nextInt();
                y2 = in.nextInt();

                square += (x2 - x1) * (y2 + y1);

                x1 = x2;
                y1 = y2;
            }

            square += (x1Start - x1) * (y1Start + y1);

            System.out.println(0.5 * square);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }
}
