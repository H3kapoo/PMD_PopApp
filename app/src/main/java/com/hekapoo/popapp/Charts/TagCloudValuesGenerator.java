package com.hekapoo.popapp.Charts;


import com.anychart.chart.common.dataentry.CategoryValueDataEntry;
import com.anychart.chart.common.dataentry.DataEntry;

import java.util.ArrayList;
import java.util.Random;

public class TagCloudValuesGenerator {

    private static final int LIKES_THRESHOLD_FACTOR = 10;
    private static final int COMMS_THRESHOLD_FACTOR = 5;
    private static final String[] LIKES_STRINGS = new String[]{"So hot!","Good looking!","Marvelous!","Fire!","Unheard of!","OMG","Such body!"};
    private static final String[] COMMS_STRINGS = new String[]{"So talkative!","Many comments!","It's lit!","WOW!","Unheard of!","WOAH","Incredible comments!"};


    public static ArrayList<DataEntry> getValuesArray(int totalPostLikes,int totalPostComments) {
        ArrayList<DataEntry> tagcloudData = new ArrayList<>();

        int initLike=0,initComms=0;

        while(initLike < totalPostLikes){
            int randomLikesStringIndex =  (new Random()).nextInt(7);
            tagcloudData.add(new CategoryValueDataEntry(LIKES_STRINGS[randomLikesStringIndex],"Reactions",totalPostLikes));

            initLike+=LIKES_THRESHOLD_FACTOR;
        }

        while(initComms < totalPostComments){
            int randomCommsStringIndex =  (new Random()).nextInt(7);
            tagcloudData.add(new CategoryValueDataEntry(COMMS_STRINGS[randomCommsStringIndex],"Comments",totalPostComments));

            initComms+=COMMS_THRESHOLD_FACTOR;
        }

        return  tagcloudData;
    }

}
