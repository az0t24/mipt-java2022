import java.util.InputMismatchException;
import java.util.Scanner;

public class task3 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int lenA = in.nextInt();
            int[] arrayA = new int[lenA];
            for (int i = 0; i < lenA; ++i) {
                arrayA[i] = in.nextInt();
            }

            int lenB = in.nextInt();
            int[] arrayB = new int[lenB];
            for (int i = 0; i < lenB; ++i) {
                arrayB[i] = in.nextInt();
            }

            int requiredSum = in.nextInt();

            int i = 0;
            int j = lenB - 1;
            int counter = 0;
            while (i < lenA && j >= 0) {
                if (arrayA[i] + arrayB[j] > requiredSum) {
                    --j;
                } else if (arrayA[i] + arrayB[j] < requiredSum) {
                    ++i;
                } else {
                    ++counter;
                    ++i;
                    --j;
                }
            }

            System.out.println(counter);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }
}
