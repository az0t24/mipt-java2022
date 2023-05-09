package ru.khairullin_ar;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

public final class Board {
    private static final int BOARD_SIZE = 8;
    private Position[][] board;
    private int numberWhites;
    private int numberBlacks;
    private Set<Checker> whites;
    private Set<Checker> blacks;

    public Board() {
        numberBlacks = 0;
        numberWhites = 0;
        board = new Position[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                boolean isBlack = (i + j) % 2 == 0;
                board[i][j] = new Position(isBlack);
            }
        }

        whites = new HashSet<>();
        blacks = new HashSet<>();
    }

    public int getNumberWhites() {
        return numberWhites;
    }

    public int getNumberBlacks() {
        return numberBlacks;
    }

    /**
     * Sets new checker on board
     * @param xCoord x coordinate of checker
     * @param yCoord y coordinate of checker
     * @param color true if checker is black or white else
     * @param isKing true if checker is king
     */
    public void setCheckerOnPosition(int xCoord, int yCoord, boolean color, boolean isKing) {
        if (board[xCoord][yCoord].isWhiteCell()) {
            throw new WhiteCellException();
        }

        if (!board[xCoord][yCoord].isEmpty()) {
            throw new BusyCellException();
        }

        Checker newChecker;

        if (!color) {
            newChecker = new Checker(xCoord, yCoord, false, isKing);
            whites.add(newChecker);
            ++numberWhites;
        } else {
            newChecker = new Checker(xCoord, yCoord, true, isKing);
            blacks.add(newChecker);
            ++numberBlacks;
        }

        board[xCoord][yCoord].makeNotEmpty(newChecker);
    }

    /**
     * Move checker on board
     * @param xFromCoord old x coordinate of checker
     * @param yFromCoord old y coordinate of checker
     * @param xToCoord new x coordinate of checker
     * @param yToCoord new y coordinate of checker
     */
    public void moveChecker(int xFromCoord, int yFromCoord, int xToCoord, int yToCoord) {
        int Delta = xFromCoord - xToCoord;

        checkCoords(xFromCoord, yFromCoord, xToCoord, yToCoord);

        Checker checker = board[xFromCoord][yFromCoord].getChecker();

        if (checker == null) {
            throw new GeneralException();
        }

        if (checker.isCanBeat()) {
            throw new InvalidMoveException();
        } else {
            if (checker.isBlackChecker()) {
                for (Checker ch : blacks) {
                    if (ch.isCanBeat()) {
                        throw new InvalidMoveException();
                    }
                }
            } else {
                for (Checker ch : whites) {
                    if (ch.isCanBeat()) {
                        throw  new InvalidMoveException();
                    }
                }
            }
        }

        if (Delta > 1 && !checker.isKing()) {
            throw new GeneralException();
        }

        checker.move(xToCoord, yToCoord);

        board[xFromCoord][yFromCoord].makeEmpty();
        board[xToCoord][yToCoord].makeNotEmpty(checker);

        renewIsCanBeat();
    }

    /**
     * Move checker on board and beat another checker
     * @param xFromCoord old x coordinate of checker
     * @param yFromCoord old y coordinate of checker
     * @param xToCoord new x coordinate of checker
     * @param yToCoord new y coordinate of checker
     */
    public boolean beatAnother(int xFromCoord, int yFromCoord, int xToCoord, int yToCoord) {
        int xDelta = xFromCoord - xToCoord;
        int yDelta = yFromCoord - yToCoord;

        checkCoords(xFromCoord, yFromCoord, xToCoord, yToCoord);

        Checker beatingChecker = board[xFromCoord][yFromCoord].getChecker();

        if (beatingChecker == null) {
            throw new GeneralException();
        }

        if (xDelta > 2 && !beatingChecker.isKing()) {
            throw new GeneralException();
        }

        Checker beatableChecker = getCheckerBetween(xFromCoord, yFromCoord, xToCoord, yToCoord);

        if (beatableChecker.isBlackChecker() == beatingChecker.isBlackChecker()) {
            throw new GeneralException();
        }

        beatingChecker.move(xToCoord, yToCoord);

        board[xFromCoord][yFromCoord].makeEmpty();
        board[xToCoord][yToCoord].makeNotEmpty(beatingChecker);

        board[beatableChecker.getX()][beatableChecker.getY()].makeEmpty();

        if (beatableChecker.isBlackChecker()) {
            blacks.remove(beatableChecker);
            --numberBlacks;
        } else {
            whites.remove(beatableChecker);
            --numberWhites;
        }

        renewIsCanBeat();

        return beatingChecker.isKing() ? findNeighboursToBeat(beatingChecker, xDelta, yDelta)
                : beatingChecker.isCanBeat();
    }

    private Checker getCheckerBetween(int xFromCoord, int yFromCoord, int xToCoord, int yToCoord) {
        int xDelta = (xFromCoord - xToCoord > 0) ? -1 : 1;
        int yDelta = (yFromCoord - yToCoord > 0) ? -1 : 1;

        int i = 1;
        while (board[xFromCoord + i * xDelta][yFromCoord + i * yDelta].isEmpty()) {
            ++i;
        }

        return board[xFromCoord + i * xDelta][yFromCoord + i * yDelta].getChecker();
    }

    private void checkCoords(int xFromCoord, int yFromCoord, int xToCoord, int yToCoord) {
        int xDelta = xFromCoord - xToCoord;
        int yDelta = yFromCoord - yToCoord;

        if (board[xToCoord][yToCoord].isWhiteCell()) {
            throw new WhiteCellException();
        }

        if (!board[xToCoord][yToCoord].isEmpty()) {
            throw new BusyCellException();
        }

        if (Math.abs(xDelta) != Math.abs(yDelta)) {
            throw new GeneralException();
        }

        if (board[xFromCoord][yFromCoord].isEmpty()) {
            throw new GeneralException();
        }
    }

    private void deleteBeating() {
        for (Checker checker : whites) {
            checker.makeNotBeating();
        }

        for (Checker checker : blacks) {
            checker.makeNotBeating();
        }
    }

    private boolean findNeighboursToBeat(Checker checker, int xRealDelta, int yRealDelta) {
        int x = checker.getX();
        int y = checker.getY();

        int[] iValues = new int[2];
        final int step = 1;
        iValues[0] = -step;
        iValues[1] = step;
        for (int iCopy : iValues) {
            for (int jCopy : iValues) {
                if (iCopy * xRealDelta > 0 && jCopy * yRealDelta > 0) {
                    continue;
                }

                int i = iCopy;
                int j = jCopy;
                while (isInInterval(x + i, y + j)) {
                    if (!board[x + i][y + j].isEmpty()) {
                        Checker neighbour = board[x + i][y + j].getChecker();
                        if (neighbour == null) {
                            throw new GeneralException();
                        }

                        if (neighbour.isBlackChecker() != checker.isBlackChecker()) {
                            int xNeighbour = neighbour.getX();
                            int yNeighbour = neighbour.getY();
                            int xDelta = (x - xNeighbour > 0) ? 1 : -1;
                            int yDelta = (y - yNeighbour > 0) ? 1 : -1;

                            if (xNeighbour - xDelta >= 0 && xNeighbour - xDelta < BOARD_SIZE
                                    && yNeighbour - yDelta >= 0 && yNeighbour - yDelta < BOARD_SIZE) {
                                if (board[xNeighbour - xDelta][yNeighbour - yDelta].isEmpty()) {
                                    return true;
                                }
                            }
                        } else {
                            i *= BOARD_SIZE;
                            j *= BOARD_SIZE;
                        }
                    }
                    if (i > 0) {
                        ++i;
                    } else {
                        --i;
                    }
                    if (j > 0) {
                        ++j;
                    } else {
                        --j;
                    }
                }
            }
        }

        return false;
    }

    private void renewIsCanBeat() {
        deleteBeating();
        final int step = 2;

        for (Checker checker : whites) {
            int x = checker.getX();
            int y = checker.getY();

            ArrayList<Position> neighbours = getNeighbours(x, y);

            for (Position pos : neighbours) {
                if (!pos.isEmpty()) {
                    Checker neighbour = pos.getChecker();
                    if (neighbour == null) {
                        throw new GeneralException();
                    }

                    if (neighbour.isBlackChecker() != checker.isBlackChecker()) {
                        int xNeighbour = neighbour.getX();
                        int yNeighbour = neighbour.getY();
                        int xDelta = x - xNeighbour;
                        int yDelta = y - yNeighbour;

                        if (xNeighbour - xDelta >= 0 && xNeighbour - xDelta < BOARD_SIZE
                            && yNeighbour - yDelta >= 0 && yNeighbour - yDelta < BOARD_SIZE) {
                            if (board[xNeighbour - xDelta][yNeighbour - yDelta].isEmpty()) {
                                checker.makeBeating();
                            }
                        }

                        if (x + xDelta >= 0 && x + xDelta < BOARD_SIZE
                            && y + yDelta >= 0 && y + yDelta < BOARD_SIZE) {
                            if (board[x + xDelta][y + yDelta].isEmpty()) {
                                neighbour.makeBeating();
                            }
                        }
                    }
                }
            }
        }

        for (Checker checker : whites) {
            if (!checker.isKing() || checker.isCanBeat()) {
                continue;
            }
            int x = checker.getX();
            int y = checker.getY();

            int[] iValues = new int[2];
            iValues[0] = -step;
            iValues[1] = step;
            for (int iCopy : iValues) {
                if (checker.isCanBeat()) {
                    break;
                }
                for (int jCopy : iValues) {
                    if (checker.isCanBeat()) {
                        break;
                    }

                    int i = iCopy;
                    int j = jCopy;
                    while (isInInterval(x + i, y + j)) {
                        if (!board[x + i][y + j].isEmpty()) {
                            Checker neighbour = board[x + i][y + j].getChecker();
                            if (neighbour == null) {
                                throw new GeneralException();
                            }

                            if (neighbour.isBlackChecker() != checker.isBlackChecker()) {
                                int xNeighbour = neighbour.getX();
                                int yNeighbour = neighbour.getY();
                                int xDelta = (x - xNeighbour > 0) ? 1 : -1;
                                int yDelta = (y - yNeighbour > 0) ? 1 : -1;

                                if (xNeighbour - xDelta >= 0 && xNeighbour - xDelta < BOARD_SIZE
                                        && yNeighbour - yDelta >= 0 && yNeighbour - yDelta < BOARD_SIZE) {
                                    if (board[xNeighbour - xDelta][yNeighbour - yDelta].isEmpty()) {
                                        checker.makeBeating();
                                        break;
                                    }
                                }
                            } else {
                                i *= BOARD_SIZE;
                                j *= BOARD_SIZE;
                            }
                        }

                        if (i > 0) {
                            ++i;
                        } else {
                            --i;
                        }
                        if (j > 0) {
                            ++j;
                        } else {
                            --j;
                        }
                    }
                }
            }
        }

        for (Checker checker : blacks) {
            if (!checker.isKing() || checker.isCanBeat()) {
                continue;
            }
            int x = checker.getX();
            int y = checker.getY();

            int[] iValues = new int[2];
            iValues[0] = -step;
            iValues[1] = step;
            for (int iCopy : iValues) {
                for (int jCopy : iValues) {
                    int i = iCopy;
                    int j = jCopy;
                    while (isInInterval(x + i, y + j)) {
                        if (!board[x + i][y + j].isEmpty()) {
                            Checker neighbour = board[x + i][y + j].getChecker();
                            if (neighbour == null) {
                                throw new GeneralException();
                            }

                            if (neighbour.isBlackChecker() != checker.isBlackChecker()) {
                                int xNeighbour = neighbour.getX();
                                int yNeighbour = neighbour.getY();

                                int xDelta = x - xNeighbour;
                                int yDelta = y - yNeighbour;

                                if (xNeighbour - xDelta >= 0 && xNeighbour - xDelta < BOARD_SIZE
                                        && yNeighbour - yDelta >= 0 && yNeighbour - yDelta < BOARD_SIZE) {
                                    if (board[xNeighbour - xDelta][yNeighbour - yDelta].isEmpty()) {
                                        checker.makeBeating();
                                    }
                                }
                            } else {
                                i = -BOARD_SIZE;
                                j = -BOARD_SIZE;
                            }
                        }

                        if (i > 0) {
                            ++i;
                        } else {
                            --i;
                        }

                        if (j > 0) {
                            ++j;
                        } else {
                            --j;
                        }
                    }
                }
            }
        }
    }

    private boolean isInInterval(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    private ArrayList<Position> getNeighbours(int x, int y) {
        ArrayList<Position> neighbours = new ArrayList<Position>();

        int[] delta = new int[2];
        delta[0] = -1;
        delta[1] = 1;
        for (int i : delta) {
            for (int j : delta) {
                if (isInInterval(x + i, y + j)) {
                    neighbours.add(board[x + i][y + j]);
                }
            }
        }

        return neighbours;
    }

    private void sortCheckersForOutput(StringBuilder string, ArrayList<Checker> sorted) {
        for (Checker white : sorted) {
            int delta = 'A';

            if (!white.isKing()) {
                delta = 'a';
            }

            String coords = new String(Character.toString(white.getX() + delta)
                    + Integer.toString(white.getY() + 1));
            string.append(coords);
            string.append(" ");
        }
    }

    /**
     * Prints checkers from the board
     * @return string with sorted list of checkers
     */
    public String print() {
        ArrayList<Checker> sortedWhites = new ArrayList<>(whites);
        Collections.sort(sortedWhites);

        StringBuilder stringAnswer = new StringBuilder();
        sortCheckersForOutput(stringAnswer, sortedWhites);

        stringAnswer.append('\n');

        ArrayList<Checker> sortedBlacks = new ArrayList<>(blacks);
        Collections.sort(sortedBlacks);
        sortCheckersForOutput(stringAnswer, sortedBlacks);

        return stringAnswer.toString();
    }

    /**
     * Updates info about beat opportunities of checkers
     */
    public void boardUpdate() {
        renewIsCanBeat();
    }

    /**
     * Creates new board
     */
    public void clean() {
        numberBlacks = 0;
        numberWhites = 0;
        board = new Position[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                boolean isBlack = (i + j) % 2 == 0;
                board[i][j] = new Position(isBlack);
            }
        }

        whites = new HashSet<>();
        blacks = new HashSet<>();
    }
}
