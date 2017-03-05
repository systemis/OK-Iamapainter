package com.example.phamvanthinh.ok_iamapainter.DialogManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.phamvanthinh.ok_iamapainter.GAS.ChillOfAProduct;
import com.example.phamvanthinh.ok_iamapainter.Handling.HDW_LoadData;
import com.example.phamvanthinh.ok_iamapainter.R;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;
import com.example.phamvanthinh.ok_iamapainter.ShowProdcuts;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by nickpham on 06/10/2016.
 */

public class DialogRemindSaveto_SQLite_Screen {

    Context context;
    RelativeLayout layoutCP_Product;
    AppCompatActivity getActivity;
    Dialog remindDialog;
    Button btnSave, btnCancel;
    EditText importProdutctName_Splite;
    int Choice;

    public DialogRemindSaveto_SQLite_Screen(Context context, RelativeLayout getProductLayout, int getChoice){

        this.context = context;
        this.layoutCP_Product = getProductLayout;
        this.Choice = getChoice;

        getActivity = (AppCompatActivity) context;

        show().show();
    }

    public Dialog show(){
        remindDialog = new Dialog(context);

        remindDialog.setContentView(R.layout.layout_dialog_remind_save_product_into_sql);
        remindDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AnhXaDialog();
        return remindDialog;
    }


    public void AnhXaDialog()
    {
        btnSave   = (Button) remindDialog.findViewById(R.id.btnSaveProduct_Sqlite);
        btnCancel = (Button) remindDialog.findViewById(R.id.btnCancelSaveProduct_Sqlite);

        importProdutctName_Splite = (EditText) remindDialog.findViewById(R.id.importProdutctName_Splite);

        View.OnClickListener listener_Sqlite_Product = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSaveProduct_Sqlite:
                        String PN_sqlite = importProdutctName_Splite.getText().toString();
                        if (PN_sqlite.isEmpty() != true){

                            Log.d("ActionOnDialog", "Saveing into sqlite ");

                            if (SaveIntoThisApp(importProdutctName_Splite.getText().toString()) != -1)
                            {

                                remindDialog.dismiss();
                                new HDW_LoadData(context, 2);

                            }else
                            {

                                Toast.makeText(context, "Bản vẽ đã tồn tại", Toast.LENGTH_SHORT).show();

                            }

                        } else { importProdutctName_Splite.setError("Xin mời nhập tên bản vẻ "); }
                        break;

                    case R.id.btnCancelSaveProduct_Sqlite:
                        Log.d("ActionOnDialog", "Dismiss Save into sqlite ");
                        if (Choice == 1){
                            getActivity.startActivity(new Intent(context, ShowProdcuts.class));
                            getActivity.finish();
                        }else if (Choice == 2) remindDialog.dismiss();
                        break;
                }
            }
        };

        btnSave.setOnClickListener(listener_Sqlite_Product);
        btnCancel.setOnClickListener(listener_Sqlite_Product);
    }



    private int SaveIntoThisApp(String ImageName){

        int result = -1;

        layoutCP_Product.setDrawingCacheEnabled(true);

        Bitmap bitmaptoSave = layoutCP_Product.getDrawingCache();

        try {

            result = SaveToSQlite(ImageName, bitmaptoSave);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        layoutCP_Product.destroyDrawingCache();

        return result;
    }

    public byte[] getImage(Bitmap getBitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public int SaveToSQlite(String TitleName, Bitmap btSave) throws UnsupportedEncodingException {

        byte[] Image = getImage(btSave);

        FactoryCompriseProduct factory = new FactoryCompriseProduct(context);
        ChillOfAProduct aProduct = new ChillOfAProduct();
        aProduct.setProductName(TitleName);
        aProduct.setProductFF(Image);

        return factory.ImportProduct(aProduct);
    }


}
