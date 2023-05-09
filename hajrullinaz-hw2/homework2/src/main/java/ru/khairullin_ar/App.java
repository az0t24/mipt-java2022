package ru.khairullin_ar;

import java.util.Scanner;

public final class App {
    private static final Board BOARD;
    static {
        BOARD = new Board();
    }

    private App() {
    }

    private static void setCheckers(String[] positions, boolean isBlack) {
        for (String pos : positions) {
            int xCoord = pos.charAt(0) - 'a';
            int yCoord = pos.charAt(1) - '1';
            boolean isKing = false;
            if (xCoord < 0) {
                isKing = true;
                xCoord -= 'A' - 'a';
            }
            BOARD.setCheckerOnPosition(xCoord, yCoord, isBlack, isKing);
        }
    }

    private static void makeStep(String step) {
        int xFrom = step.charAt(0) - 'a';
        if (xFrom < 0) {
            xFrom -= 'A' - 'a';
        }
        int yFrom = step.charAt(1) - '1';
        boolean isBeat = step.charAt(2) == ':';

        int xTo = step.charAt(3) - 'a';
        if (xTo < 0) {
            xTo -= 'A' - 'a';
        }
        int yTo = step.charAt(4) - '1';

        int xDelta = xFrom - xTo;
        int yDelta = yFrom - yTo;

        if (isBeat) {
            boolean check = BOARD.beatAnother(xFrom, yFrom, xTo, yTo);
            int i = 5;
            while (i < step.length()) {
                if (!check) {
                    throw new GeneralException();
                }

                xFrom = xTo;
                yFrom = yTo;

                xTo = step.charAt(i + 1) - 'a';
                if (xTo < 0) {
                    xTo -= 'A' - 'a';
                }
                yTo = step.charAt(i + 2) - '1';

                if (xFrom - xTo == -xDelta && yFrom - yTo == -yDelta) {
                    throw new GeneralException();
                }

                xDelta = xFrom - xTo;
                yDelta = yFrom - yTo;

                check = BOARD.beatAnother(xFrom, yFrom, xTo, yTo);

                i += 3;
            }

            if (check) {
                throw new InvalidMoveException();
            }
        } else {
            BOARD.moveChecker(xFrom, yFrom, xTo, yTo);
        }
    }

    public static String play(String[] data) {
        try {
            BOARD.clean();

            String[] whitePos = data[0].split(" ");
            String[] blackPos = data[1].split(" ");

            setCheckers(whitePos, false);
            setCheckers(blackPos, true);

            BOARD.boardUpdate();

            for (int i = 2; i < data.length; ++i) {
                if (BOARD.getNumberWhites() == 0) {
                    throw new GeneralException();
                }

                String[] steps = data[i].split(" ");
                makeStep(steps[0]);

                if (BOARD.getNumberBlacks() == 0 || steps.length == 1) {
                    break;
                }

                makeStep(steps[1]);
            }

            return BOARD.print();
        } catch (BusyCellException exp) {
            return "busy cell";
        } catch (WhiteCellException exp) {
            return "white cell";
        } catch (InvalidMoveException exp) {
            return "invalid move";
        } catch (GeneralException exp) {
            return "general error";
        } catch (RuntimeException exp) {
            return "another runtime error";
        }
    }

    private static void play() {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        String[] whitePos = str.split(" ");
        str = in.nextLine();
        String[] blackPos = str.split(" ");

        setCheckers(whitePos, false);
        setCheckers(blackPos, true);

        BOARD.boardUpdate();

        while (in.hasNextLine()) {
            str = in.nextLine();
            if (BOARD.getNumberWhites() == 0) {
                throw new GeneralException();
            }

            String[] steps = str.split(" ");
            makeStep(steps[0]);

            if (BOARD.getNumberBlacks() == 0 || steps.length == 1) {
                break;
            }

            makeStep(steps[1]);
        }

        System.out.println(BOARD.print());
    }

    public static void main(String[] args) {
        try {
            play();
        } catch (BusyCellException exp) {
            System.out.println("busy cell");
        } catch (WhiteCellException exp) {
            System.out.println("white cell");
        } catch (InvalidMoveException exp) {
            System.out.println("invalid move");
        } catch (GeneralException exp) {
            System.out.println("general error");
        } catch (RuntimeException exp) {
            System.out.println("another runtime error");
        }
    }
}
