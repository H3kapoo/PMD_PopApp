package com.hekapoo.popapp.homeStatus;

import android.util.Log;

import com.anychart.chart.common.dataentry.CategoryValueDataEntry;

import java.util.Random;

public class HomeStatusUpdater {

    private static final int GOOD_THRESHOLD = 340;
    private static final int NEUTRAL_THRESHOLD = 140;


    private static final int[] IMAGES_ID = new int[]{0, 1, 2}; //sustitute with R.drawable. bad -> good
    private static final String[] TEXT_STRINGS_POSITIVE = new String[]{"Fans are very talkative!", "You're doing very well!"};
    private static final String[] TEXT_STRINGS_NEUTRAL = new String[]{"You're doing ok buddy!", "They appreciate you"};
    private static final String[] TEXT_STRINGS_NEGATIVE = new String[]{"This is bad..", "Maybe change style?"};

    public static final int TEXT_STRINGS_LENGTH = TEXT_STRINGS_POSITIVE.length;

    public static int getSuitableImageResource(int totalLikes, int totalComments) {
        int total = totalLikes * 3 + totalComments * 7;
        Log.d("homestatus", "getSuitableTextString: " + total);

        if (total >= GOOD_THRESHOLD)
            return IMAGES_ID[2];
         else if (total >= NEUTRAL_THRESHOLD && total < GOOD_THRESHOLD)
            return IMAGES_ID[1];
         else
            return IMAGES_ID[0];

    }

    public static String getSuitableTextString(int totalLikes, int totalComments) {

        int total = totalLikes * 3 + totalComments * 7;
        Log.d("homestatus", "getSuitableTextString: " + total);

        if (total >= GOOD_THRESHOLD) {

            int randomStringIndex = (new Random()).nextInt(TEXT_STRINGS_LENGTH);
            return TEXT_STRINGS_POSITIVE[randomStringIndex];

        } else if (total >= NEUTRAL_THRESHOLD && total < GOOD_THRESHOLD) {

            int randomStringIndex = (new Random()).nextInt(TEXT_STRINGS_LENGTH);
            return TEXT_STRINGS_NEUTRAL[randomStringIndex];

        } else {

            int randomStringIndex = (new Random()).nextInt(TEXT_STRINGS_LENGTH);
            return TEXT_STRINGS_NEGATIVE[randomStringIndex];
        }
    }
}
