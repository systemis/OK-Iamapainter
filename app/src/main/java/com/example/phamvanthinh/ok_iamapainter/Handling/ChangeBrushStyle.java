package com.example.phamvanthinh.ok_iamapainter.Handling;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.Log;

import java.util.List;

/**
 * Created by nickpham on 12/10/2016.
 */

public class ChangeBrushStyle {

    Paint painttoCustom;
    Context context;

    List<Integer> strokeWidthList, colorList;

    public ChangeBrushStyle(Paint getPaint, Context context, int choice, List<Integer> strokeWidthList, List<Integer> colorList) {

        this.painttoCustom = getPaint;
        this.context = context;
        this.strokeWidthList = strokeWidthList;
        this.colorList = colorList;

        ReturnDefaultProperties();

        switch (choice) {
            case 1:
                setWaterBrushStyle();
                Log.d("ActionWhenChangePaintS", "Water brush");
                break;

            case 2:
                setBigBrushStyle();
                Log.d("ActionWhenChangePaintS", "Big brush");
                break;

            case 3:
                painttoCustom.setAlpha(0x80);
                Log.d("ActionWhenChangePaintS", "Alpha brush");
                break;
        }
    }

    public void setWaterBrushStyle(){
        painttoCustom = new Paint();
        painttoCustom.setAlpha(0x80);
        //painttoCustom.setAntiAlias(true);
        //painttoCustom.setDither(true);
        painttoCustom.setColor(colorList.get(colorList.size() - 1));
        painttoCustom.setStyle(Paint.Style.STROKE);
        painttoCustom.setStrokeJoin(Paint.Join.ROUND);
        painttoCustom.setStrokeCap(Paint.Cap.ROUND);
        painttoCustom.setStrokeWidth(strokeWidthList.get(strokeWidthList.size() - 1));

        Shader shader = new RadialGradient(0, 0, 20,
                Color.argb(255, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.CLAMP);
        //painttoCustom.setShader(shader);
    }


    // Duong ve kieu 40
    public void setBigBrushStyle(){ painttoCustom.setStrokeWidth(40); }

    // Kieu co ve mat dinh !
    public void ReturnDefaultProperties() {

        painttoCustom = new Paint();
        painttoCustom.setAntiAlias(true);
        painttoCustom.setStrokeWidth(strokeWidthList.get(strokeWidthList.size() - 1));
        painttoCustom.setColor(colorList.get(colorList.size() - 1));
        painttoCustom.setStyle(Paint.Style.STROKE);

    }


    public Paint getPainttoCustom() {
        return painttoCustom;
    }

    public void setPainttoCustom(Paint painttoCustom) {
        this.painttoCustom = painttoCustom;
    }
}
