package kpi.iff.utils;

import java.awt.*;
import java.util.Random;

/**
 * Created by cullycross on 2/10/15.
 */
public class AppLogic {

    private static final int MATRIX_WIDTH = 400;
    private static final int MATRIX_HEIGHT = 300;
    private static final int MATRIX_HALF = 200;

    private static final int MARGIN = 25;

    private static final int GOLD = 1;
    private static final int SILVER = -1;

    private static final int POINT_SIZE = 1;

    //private static final int CROSS_CHANCE = 104;
    //private static final int XY_CHANCE = 146;
    private static final int FULL_CHANCE = 1000;

    public volatile static int sTime;

    private int [][] mMatrix = new int[MATRIX_HEIGHT][MATRIX_WIDTH];
    private static int sCurrentConcentration = 0;
    private volatile int [] mDuplicates = new int[MATRIX_WIDTH];
    private Random mRandom = new Random();

    private static AppLogic mInstance;

    public static AppLogic getInstance() {
        if (mInstance == null)
            mInstance = new AppLogic();
        return mInstance;
    }

    private AppLogic() {

        for(int i = 0; i < MATRIX_HEIGHT; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                mMatrix[i][j] = j <= MATRIX_HALF ? GOLD : SILVER;
            }
        }
    }

    public void start(final Graphics g) {
        update();
        render(g);
    }

    /**
     * swap two elements
     */

    private void swapElements(int iFrom,
                              int jFrom,
                              int iTo,
                              int jTo) {
        int temp = mMatrix[iFrom][jFrom];
        mMatrix[iFrom][jFrom] = mMatrix[iTo][jTo];
        mMatrix[iTo][jTo] = temp;
    }

    /**
     * Random swapping elements
     * @param i array index row
     * @param j array index column
     */
    private void swap(int i, int j) {

        int chance = mRandom.nextInt(FULL_CHANCE);

        if(chance < 416) {
            if(chance < 104 && i > 0 && j < MATRIX_WIDTH - 2) { //North-East

                swapElements(i, j, i - 1, j + 1);
            } else if(chance < 208 && i < MATRIX_HEIGHT - 2 && j < MATRIX_WIDTH - 2) { //South-East

                swapElements(i, j, i + 1, j + 1);
            } else if(chance < 312 && i < MATRIX_HEIGHT - 2 && j > 0) { //South-West

                swapElements(i, j, i + 1, j - 1);
            } else if (i > 0 && j > 0){ //North-West

                swapElements(i, j, i - 1, j - 1);
            }
        } else if(chance >= 416) {
            if(chance < 562 && i > 0) { //North

                swapElements(i, j, i - 1, j);
            } else if(chance < 708 && j < MATRIX_WIDTH - 2) { //East

                swapElements(i, j, i, j + 1);
            } else if(chance < 854 && i < MATRIX_HEIGHT - 2) { //South

                swapElements(i, j, i + 1, j);
            } else if(j > 0){ //West

                swapElements(i, j, i, j - 1);
            }
        }
    }

    private void update() {

        int counter = 0;
        for(int i = 0; i < MATRIX_HEIGHT; i++) {
            for(int j = 0; j < MATRIX_WIDTH; j++) {
                swap(i,j);
            }
        }
        for(int i = 0; i < MATRIX_WIDTH; i++) {
            int c = 0;
            for(int j = 0; j < MATRIX_HEIGHT; j++) {
                if(i > MATRIX_HALF + 1 &&
                        mMatrix[j][i] == GOLD){
                    counter++;
                }
                if(mMatrix[j][i] == GOLD) c++;
            }
            mDuplicates[i] = c;
        }
        sCurrentConcentration = counter;
    }

    private void render(Graphics g) {

        for(int i = 0; i < MATRIX_HEIGHT; i++) {
            for(int j = 0; j < MATRIX_WIDTH; j++) {
                g.setColor(
                        mMatrix[i][j]==GOLD?Color.ORANGE:Color.GRAY
                );

                g.drawRect(
                        POINT_SIZE*j + MARGIN,
                        POINT_SIZE*i + MARGIN,
                        POINT_SIZE,
                        POINT_SIZE
                );
            }
        }

        String diff = Integer.toString(sTime++) + " iter.";
        g.setColor(Color.BLACK);

        g.setFont(new Font("SanSerif", Font.PLAIN, 20));
        g.drawString(diff, 65, 100);

        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(sCurrentConcentration), 325, 100);
    }

    public void renderChart(Graphics g) {
        for(int i = 0; i < MATRIX_WIDTH; i++) {
            for(int j = 0; j < MATRIX_HEIGHT; j++) {
                g.setColor(
                        j < mDuplicates[i]?
                                Color.ORANGE:Color.GRAY
                );

                g.drawRect(
                        POINT_SIZE*i + MARGIN,
                        POINT_SIZE*(MATRIX_HEIGHT - j) + MARGIN,
                        POINT_SIZE,
                        POINT_SIZE
                );
            }
        }

        g.setColor(Color.WHITE);

        g.setFont(new Font("SanSerif", Font.PLAIN, 20));
        g.drawString("Silver", 345, 100);

        g.setColor(Color.BLACK);
        g.drawString("Gold", 55, 275);

        g.setFont(new Font("SanSerif", Font.PLAIN, 12));

        //x Axis
        g.drawLine(MARGIN + 100, MARGIN + MATRIX_HEIGHT + 1, MARGIN + 100, MARGIN + MATRIX_HEIGHT - 10);
        g.drawLine(MARGIN + 200, MARGIN + MATRIX_HEIGHT + 1, MARGIN + 200, MARGIN + MATRIX_HEIGHT - 10);
        g.drawLine(MARGIN + 300, MARGIN + MATRIX_HEIGHT + 1, MARGIN + 300, MARGIN + MATRIX_HEIGHT - 10);
        g.drawLine(MARGIN + 400, MARGIN + MATRIX_HEIGHT + 1, MARGIN + 400, MARGIN + MATRIX_HEIGHT - 10);
        g.drawString("0", MARGIN, 13 + MARGIN + MATRIX_HEIGHT);
        g.drawString("100", MARGIN + 100, 13 + MARGIN + MATRIX_HEIGHT);
        g.drawString("200", MARGIN + 200, 13 + MARGIN + MATRIX_HEIGHT);
        g.drawString("300", MARGIN + 300, 13 + MARGIN + MATRIX_HEIGHT);
        g.drawString("400", MARGIN + 400, 13 + MARGIN + MATRIX_HEIGHT);

        //y Axis
        g.drawLine(MARGIN, MARGIN + 76, MARGIN + 13, MARGIN + 76);
        g.drawLine(MARGIN, MARGIN + 151, MARGIN + 13, MARGIN + 151);
        g.drawLine(MARGIN, MARGIN + 226, MARGIN + 13, MARGIN + 226);
        g.drawLine(MARGIN, MARGIN + 1, MARGIN + 13, MARGIN + 1);
        g.drawString("100%", MARGIN, MARGIN + 14);
        g.drawString("75%", MARGIN, MARGIN + 89);
        g.drawString("50%", MARGIN, MARGIN + 164);
        g.drawString("25%", MARGIN, MARGIN + 239);
    }
}
