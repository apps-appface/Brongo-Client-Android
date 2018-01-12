package com.turnipconsultants.brongo_client;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Pankaj on 14-11-2017.
 */

public class PageTransformerTest implements ViewPager.PageTransformer {
    private float mScale = 0.8f;
    @Override
    public void transformPage(View view, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        view.setScaleX(normalizedposition / 2 + 0.5f);
        view.setScaleY(normalizedposition / 2 + 0.5f);


       /* if(position==0){
            ViewCompat.setScaleX(view, 1);
            ViewCompat.setScaleY(view, 1);
        }
        else{
            ViewCompat.setScaleX(view, mScale);
            ViewCompat.setScaleY(view, mScale);
        }*/
    }
}