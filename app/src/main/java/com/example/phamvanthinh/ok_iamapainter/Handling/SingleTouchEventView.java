package com.example.phamvanthinh.ok_iamapainter.Handling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.phamvanthinh.ok_iamapainter.CreateNewScreen;
import com.example.phamvanthinh.ok_iamapainter.R;

import java.util.ArrayList;
import java.util.List;


public class SingleTouchEventView extends View {

    public Context context;
    public AppCompatActivity getActivity;
    public Paint paint = new Paint();
    public Path path = new Path();
    public Path pathtoClear;
    public List<ChillOfPoint> ppList = new ArrayList<>();
    public List<Paint> paintList = new ArrayList<Paint>();
    public List<Integer> colorList = new ArrayList<Integer>();
    public List<Integer> strokeWidthList = new ArrayList<Integer>();
    public boolean changeColorC = false, closeChangeColorC = false, firstPickImage = false, selectBitmap = false;
    public boolean isWaterBrush = false, isBigBrush = false, isAlphaBrush = false;
    public int indextOfPosition = -1, painColor = Color.BLACK, painStrokeWidth = 12, thisBackground, brushAlpha = -1;//Color.WHITE;
    public Bitmap getBitmap = null;

    public SingleTouchEventView(Context context) {
        super(context);

        this.context = context;
        this.getActivity = (AppCompatActivity) context;
        this.thisBackground = getActivity.getResources().getColor(R.color.backgroundofViewAboutProduct);

        colorList.add(painColor);
        strokeWidthList.add(painStrokeWidth);

        this.setBackgroundColor(thisBackground);
    }

    /**
     * Khởi tạo thuộc tính cõ vẽ theo tuỳ chọn của người dùng
     */
    public Paint customPaintoDraw()
    {
        Paint painttocustomPaintoDraw = new Paint();

        int stylePaintMode = 0;

        if (isWaterBrush) { stylePaintMode = 1 ; } else { Log.d("ActionProperties: ", "not Water"); }
        if (isBigBrush  ) { stylePaintMode = 2 ; } else { Log.d("ActionProperties: ", "not Big");   }
        if (isAlphaBrush) { stylePaintMode = 3 ; } else { Log.d("ActionProperties: ", "not alpha");   }

        ChangeBrushStyle brushStyle  = new ChangeBrushStyle(painttocustomPaintoDraw, context, stylePaintMode, strokeWidthList, colorList);
        painttocustomPaintoDraw = brushStyle.getPainttoCustom();

        return painttocustomPaintoDraw;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (CreateNewScreen._custom_stroke_width.isShowing())
        {
            CreateNewScreen._custom_stroke_width.dismiss();

        }

        if (this.getBitmap != null)
        {

            if (firstPickImage)
            {

                ReturnAllNull();
                firstPickImage = false;

            }

            canvas.drawBitmap(getBitmap, 0, 0, null);

        }

        if (ppList.size() > 0)
        {

            for (ChillOfPoint ofPoint : ppList)
            {

                canvas.drawPath(ofPoint.getPathToDraw(), paintList.get(ppList.indexOf(ofPoint)));

            }

        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // Set value for path to draw !!
                //if (changeColorC) { path = new Path(); }

                if (selectBitmap || changeColorC)
                {

                    path = new Path();

                }
                path.moveTo(event.getX(), event.getY());

                path.lineTo(event.getX(), event.getY());

            case MotionEvent.ACTION_MOVE:

                path.lineTo(event.getX(), event.getY());

                hanldingForPaintProperties(event.getX(), event.getY(), 2);

                break;

            default:

                break;

        }

        changeColorC = false;
        closeChangeColorC = false;
        selectBitmap = false;
        invalidate();
        return true;
    }

    public void hanldingForPaintProperties(float x, float y, int Choice) {
        if (Choice != 10)
        {

            Paint mPaint = customPaintoDraw();


            ChillOfPoint ofPoint = new ChillOfPoint();
            ofPoint.setPathToDraw(path);
            ofPoint.setPaintForPaint(mPaint);

            if (ppList.size() > 0) {

                if (changeColorC) {
                    // Nếu có sự thay đổi thuộc tính cỏ vẽ từ phía người dùng thì sẽ thêm một ChillOfPoint vào ppList và sẽ thêm một cõ vẽ (Paint) vào paintList.
                    ppList.add(ofPoint);
                    paintList.add(mPaint);

                } else {
                    // Nếu không có sự thay đổi nào từ
                    if (indextOfPosition == -1) {

                        ppList.set(ppList.size() - 1, ofPoint);
                        paintList.set(paintList.size() - 1, ofPoint.getPaintForPaint());

                    } else {
                        ppList.set(indextOfPosition, ofPoint);
                    }
                }

            } else
            {

                // Nếu người dùng chưa vẽ đường nét nào trên giấy đang mở thì sẽ tạo mới một đường vẽ và cõ vẽ khi người dùng thao tác trên màn hình, sau đó sẽ thêm vào ChillOfPoint.
                ppList.add(ofPoint);
                paintList.add(mPaint);

            }

        }

    }


    public void changePaintSingle(int color, int Width) {

        Log.d("SizeOfLists", String.valueOf("ppList : " + ppList.size()
                + " colorList: " + colorList.size()
                + " StrokeWidthList: " + strokeWidthList.size()
                + " PaintList: " + paintList.size()));

        if (color != colorList.get(colorList.size() - 1) || Width != strokeWidthList.get(strokeWidthList.size() - 1) || checkIsHaveModeStyle() == false) {


            if (ppList.size() < 1) {
                Log.d("ResultChangePainP", "Change Paint Properties when first start");

                colorList.set(0, color);
                strokeWidthList.set(0, Width);

                 customPaintoDraw();



                indextOfPosition = -1;
            } else {
                strokeWidthList.add(Width);
                colorList.add(color);
                indextOfPosition = -1;
            }

            if (indextOfPosition == -1) changeColorC = true;

            Log.d("ResultOfChangeAll : ", String.valueOf(changeColorC + " - ViTri: " + indextOfPosition));
        } else {
            Log.d("ActionWhenChange: ", "Sorry you are choosing this properties !");
            indextOfPosition = -1;
        }
    }


    public void clearAll() {
        // Không vẽ hình ảnh đã chọn
        getBitmap = null;

        if (ppList.size() > 0) {
            ReturnAllNull();
        } else {
            Log.d("ResultWhenChoice", "That bai ");
        }
    }

    // Dua tat ca ve so 0
    public int ReturnAllNull() {

        ppList = new ArrayList<ChillOfPoint>();

        path = new Path();

        colorList = new ArrayList<Integer>();
        colorList.add(painColor);

        strokeWidthList = new ArrayList<>();
        strokeWidthList.add(painStrokeWidth);


        paintList = new ArrayList<Paint>();

        invalidate();

        // Hiện thị cho người dùng biết là màu hiện tại của cọ vẽ là màu đen bằng cách set màu của khung viền button màu đen từ màu đen sang màu đỏ
        CreateNewScreen.btnChangePaintSimpleColor[0].setBorderColor(Color.RED);

        return 0;
    }

    public void setImageFromGallery(Bitmap getBitmap)
    {

        if (this.getBitmap != getBitmap || this.getBitmap == null)
        {

            this.getBitmap = getBitmap;
            firstPickImage = true;
            selectBitmap   = true;

        }

    }


    public class ChillOfPoint
    {

        Path pathToDraw;
        Paint paintForPaint;

        public Paint getPaintForPaint() {
            return paintForPaint;
        }

        public void setPaintForPaint(Paint paintForPaint) {
            this.paintForPaint = paintForPaint;
        }

        public Path getPathToDraw() {
            return pathToDraw;
        }

        public void setPathToDraw(Path pathToDraw) {
            this.pathToDraw = pathToDraw;
        }

    }

    public void returnPainProperties(){

        isWaterBrush = false;
        isBigBrush   = false;
        isAlphaBrush = false;

    }
    // Lam mo co
    public void setPaintAlpha() {

        if (isAlphaBrush != true)
        {
            returnPainProperties();
            if (paintList.size() > 0){
                if (paintList.get(paintList.size() - 1).getAlpha() != 0x80){
                    changeColorC = true;
                }
            }

            isAlphaBrush = true;
        }

    }

    public void setBigBrush(boolean bigBrush) {
        if (this.isBigBrush != true)
        {
            returnPainProperties();

            if (paintList.size() < 1){
                changeColorC = true;
            }else {
                if (paintList.get(paintList.size() - 1).getStrokeWidth() != 30) {
                    changeColorC = true;
                    Log.d("BigW", "Big brush");
                }
            }

            isBigBrush = bigBrush;
            Log.d("is", String.valueOf(isBigBrush));
        }else if(bigBrush) {
            returnPainProperties();
        }
    }

    // Mau nuoc
    public void setIsWaterBrush(boolean isWaterBrush) {
        returnPainProperties();
        if (this.isWaterBrush != true)
        {

            this.isWaterBrush = isWaterBrush;
            changeColorC = true;

        } else isWaterBrush = true;
    }

    public boolean checkIsHaveModeStyle(){
        boolean result = false;
        if (paintList.size() > 1){

            if (isBigBrush) {
                if(paintList.get(paintList.size() - 1).getStrokeWidth() == 30) { result = true; }
            }else if( isAlphaBrush ) {
                if(paintList.get(paintList.size() - 1).getAlpha() == 0x80) { result = true; }
            }else if ( isWaterBrush ) {

            }

        }

        return result;
    }

    public void setIsDefaultBrush(){ returnPainProperties(); changeColorC = true; }

    public boolean getIsWaterBrush() {
        return isWaterBrush;
    }

    public boolean getIsBigBrush() {
        return isBigBrush;
    }



    public void showAll(){
        Log.d("List", "pp: " + getValue(ppList.size()) + " paintList: "
                + getValue(paintList.size()) + " colorList: " + getValue(colorList.size()) + " strokeList: " + getValue(strokeWidthList.size()));
    }

    public String getValue(int a){
        return  String.valueOf(a);
    }
}