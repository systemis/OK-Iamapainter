package com.example.phamvanthinh.ok_iamapainter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.phamvanthinh.ok_iamapainter.Handling.Animation.Show_A_Hide_manager_product;
import com.example.phamvanthinh.ok_iamapainter.Handling.HDW_LoadData;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.tag;

public class ShowProdcuts extends AppCompatActivity implements OnClickListener {

    RelativeLayout activity_show_prodcuts;
    LinearLayout lshowProdcuts, managet_tools_art, managet_tools_all_art;
    ImageView imageTestShowProdcuct;
    List<ImageView> ViewtoShowProductsList = new ArrayList<ImageView>();
    ImageButton delete_art, share_art, edit_art, AAP_new_art, AAP_remove_all;
    public FactoryCompriseProduct fCP;

    public static String intNameIT = "positionInSQLITE";
    public static String bunlderNameIT = "DataFromShowFactory";

    private int aboutArt = -1;
    boolean isShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prodcuts);

        //setupActionbar();

        fCP = new FactoryCompriseProduct(this);

        Anhxa();

        AddAll();

    }

    public void Anhxa(){
        activity_show_prodcuts     = (RelativeLayout) this.findViewById(R.id.activity_show_prodcuts);
        lshowProdcuts              = (LinearLayout)   this.findViewById(R.id.showProdcuts);
        managet_tools_art          = (LinearLayout)   this.findViewById(R.id.managet_tools_art);
        managet_tools_all_art      = (LinearLayout)   this.findViewById(R.id.managet_tools_all_art);

        imageTestShowProdcuct = (ImageView) this.findViewById(R.id.imageTestShowProdcuct);

        delete_art = (ImageButton) this.findViewById(R.id.delete_art);
        share_art  = (ImageButton) this.findViewById(R.id.share_art);
        edit_art   = (ImageButton) this.findViewById(R.id.edit_art);

        AAP_new_art = (ImageButton) this.findViewById(R.id.AAP_new_art);
        AAP_remove_all = (ImageButton) this.findViewById(R.id.AAP_remove_art);

        activity_show_prodcuts.removeView(imageTestShowProdcuct);

        managet_tools_all_art.setVisibility(View.VISIBLE);

        ListennerAboutArt();
        ListennerAboutAllArt();
        activity_show_prodcuts.setOnClickListener(this);

    }

    public void AddAll(){
        lshowProdcuts.removeAllViews();

        ViewtoShowProductsList = new ArrayList<ImageView>();

        if (fCP.showProduct() != null){
            for (int i = 0; i < fCP.showProduct().size(); i++){
                lshowProdcuts.addView(customImageView(fCP.showProduct().get(i).getProductFF(), i));
            }
        }else{
            // do thí thing when not art

            Button btnShowNotNull = new Button(ShowProdcuts.this);
            btnShowNotNull.setLayoutParams(imageTestShowProdcuct.getLayoutParams());
            btnShowNotNull.setBackgroundColor(android.R.color.transparent);
            btnShowNotNull.setText("Bạn chưa tạo bản vẽ nào cả ");
            btnShowNotNull.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            btnShowNotNull.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CreateNewArt();
                }
            });

            lshowProdcuts.addView(btnShowNotNull);
        }


    }

    public ImageView customImageView(byte[] getImage, int tag)
    {

        Bitmap bitmaptoSet = BitmapFactory.decodeByteArray(getImage, 0, getImage.length);
        ImageView createImage = new ImageView(this);

        createImage.setLayoutParams(imageTestShowProdcuct.getLayoutParams());
        createImage.setScaleType(ImageView.ScaleType.FIT_XY);
        createImage.setImageBitmap(bitmaptoSet);

        // set id for image view
        createImage.setTag(tag);
        createImage.setOnClickListener(ActionAboutAProduct);
        ViewtoShowProductsList.add(createImage);

        return createImage;
    }

    public OnClickListener ActionAboutAProduct = new OnClickListener() {
        @Override
        public void onClick(View v)
        {

            if(managet_tools_art.getVisibility() == View.VISIBLE)
            {
                SAH_ManangerTool(11);
            }else
            {
                SAH_ManangerTool(2);
            }

            ImageView bt = (ImageView) v;
            aboutArt = ViewtoShowProductsList.indexOf(bt);

        }
    };


    // Do something when client click imageview
    public void ListennerAboutArt(){
        OnClickListener listenerCustomAboutArtCheck = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.delete_art:
                        RemoveArtFromFactory();
                        AddAll();
                        break;
                    case R.id.share_art:
                        new HDW_LoadData(ShowProdcuts.this, 99, aboutArt);
                        break;
                    case R.id.edit_art:
                        EditArt();
                        break;
                }

            }
        };

        delete_art.setOnClickListener(listenerCustomAboutArtCheck);
        share_art.setOnClickListener(listenerCustomAboutArtCheck);
        edit_art.setOnClickListener(listenerCustomAboutArtCheck);
    }

    // Do something when client click parent layout
    public void ListennerAboutAllArt() {
        OnClickListener listenerAboutAllArt = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.AAP_new_art:
                        CreateNewArt();
                        break;

                    case R.id.AAP_remove_art:

                        fCP.DeleteAllProduct();
                        AddAll();
                        break;
                }
            }
        };

        AAP_new_art.setOnClickListener(listenerAboutAllArt);
        AAP_remove_all.setOnClickListener(listenerAboutAllArt);

    }

    // Sua ban ve
    public void EditArt()
    {

        Intent MoreAboutProduct = new Intent(ShowProdcuts.this, CreateNewScreen.class);
        Bundle dataToGetProduct = new Bundle();
        dataToGetProduct.putInt(intNameIT, aboutArt);
        MoreAboutProduct.putExtra(bunlderNameIT, dataToGetProduct);
        startActivity(MoreAboutProduct);
        animationStartActivity();
        finish();
    }

    // Xoa ban ve
    public void RemoveArtFromFactory(){
        String productName = fCP.showProduct().get(aboutArt).getProductName();
        fCP.DeleteProduct(productName);
    }


    public void CreateNewArt(){ new HDW_LoadData(ShowProdcuts.this, 1); }

    public void SAH_ManangerTool(int Choice){ new Show_A_Hide_manager_product(ShowProdcuts.this, Choice, managet_tools_art, managet_tools_all_art); }

    public void animationStartActivity() { overridePendingTransition(R.anim.start_slide_in_left, R.anim.slide_out_left); }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.activity_show_prodcuts:

                SAH_ManangerTool(1);

                break;
        }
    }




}
