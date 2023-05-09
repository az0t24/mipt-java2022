import java.util.InputMismatchException;
import java.util.Scanner;

public class task1 {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int len = in.nextInt();

            int[] arrayA = new int[len];
            int[] arrayB = new int[len];

            for (int i = 0; i < len; ++i) {
                arrayA[i] = in.nextInt();
            }

            for (int i = 0; i < len; ++i) {
                arrayB[i] = in.nextInt();
            }

            int[] maxPos = new int[len];
            int maxValue = Integer.MIN_VALUE;
            int maxIdx = 0;

            for (int i = 0; i < len; ++i) {
                if (arrayA[i] > maxValue) {
                    maxValue = arrayA[i];
                    maxIdx = i;
                }

                maxPos[i] = maxIdx;
            }

            int iMin = 0;
            int jMin = 0;
            maxValue = Integer.MIN_VALUE;
            int currentMax = 0;

            for (int i = 0; i < len; ++i) {
                currentMax = arrayB[i] + arrayA[maxPos[i]];
                if (currentMax > maxValue) {
                    maxValue = currentMax;
                    iMin = maxPos[i];
                    jMin = i;
                }
            }

            System.out.println(iMin);
            System.out.println(jMin);
        } catch (InputMismatchException e) {
            e.printStackTrace();
        }
    }
}
