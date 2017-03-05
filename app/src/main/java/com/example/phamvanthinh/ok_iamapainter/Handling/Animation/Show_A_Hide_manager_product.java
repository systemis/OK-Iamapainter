package com.example.phamvanthinh.ok_iamapainter.Handling.Animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.phamvanthinh.ok_iamapainter.R;

/**
 * Created by nickpham on 08/10/2016.
 */

public class Show_A_Hide_manager_product {

    public Show_A_Hide_manager_product(final Context context, int Choice, final LinearLayout managet_tools_art, final LinearLayout managet_tools_all_art){

        final Animation a_hide = AnimationUtils.loadAnimation(context, R.anim.view_hide);
        final Animation a_show = AnimationUtils.loadAnimation(context, R.anim.view_show);

        android.os.Handler dn = new android.os.Handler();

        if(Choice == 1){


            if (managet_tools_all_art.getVisibility() == View.INVISIBLE){

                dn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        managet_tools_all_art.setVisibility(View.VISIBLE);
                        managet_tools_all_art.startAnimation(a_show);

                    }
                }, 100);

                managet_tools_art.startAnimation(a_hide);
                managet_tools_art.setVisibility(View.INVISIBLE);
            }


        }else if (Choice == 2){

            if (managet_tools_art.getVisibility() == View.INVISIBLE){

                dn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        managet_tools_art.setVisibility(View.VISIBLE);
                        managet_tools_art.startAnimation(a_show);

                    }
                }, 100);

                managet_tools_all_art.startAnimation(a_hide);
                managet_tools_all_art.setVisibility(View.INVISIBLE);

            }
        }

        if(Choice == 11)
        {

            dn.postDelayed(new Runnable() {
                @Override
                public void run() {
                    managet_tools_art.setVisibility(View.VISIBLE);
                    managet_tools_art.startAnimation(a_show);

                }
            }, 100);

            managet_tools_art.startAnimation(a_hide);
            managet_tools_art.setVisibility(View.INVISIBLE);

        }
    }


}
