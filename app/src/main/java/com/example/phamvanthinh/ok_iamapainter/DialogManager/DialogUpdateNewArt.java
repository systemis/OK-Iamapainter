package com.example.phamvanthinh.ok_iamapainter.DialogManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.example.phamvanthinh.ok_iamapainter.GAS.ChillOfAProduct;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;
import com.example.phamvanthinh.ok_iamapainter.ShowProdcuts;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by nickpham on 10/10/2016.
 */

public class DialogUpdateNewArt {


    Context context;
    RelativeLayout rlCP_Product;
    AppCompatActivity getActivity;
    int isUpdate;

    public DialogUpdateNewArt(Context context, RelativeLayout lrCP, int isUpdate) {

        this.context = context;
        this.rlCP_Product = lrCP;
        this.isUpdate = isUpdate;
        this.getActivity = (AppCompatActivity) context;

        RemindClientAboutUpdate().show();
    }


    public AlertDialog RemindClientAboutUpdate(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("Update ?");
        mDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new When_UpdateArt(context, "Updating ... please wait ").execute();
            }
        });
        mDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GotoProductScreen();
            }
        });
        return mDialog.create();
    }


    public int EditProduct() {
        rlCP_Product.setDrawingCacheEnabled(true);

        if (isUpdate != -1) {
            FactoryCompriseProduct compriseProduct = new FactoryCompriseProduct(context);
            List<ChillOfAProduct> getOldProduct = compriseProduct.showProduct();
            String productName = getOldProduct.get(isUpdate).getProductName();
            if (getNewProduct() != null) {
                if (compriseProduct.EditProduct(productName, getNewProduct())) {

                }
            }
        }

        rlCP_Product.destroyDrawingCache();


        return 0;
    }

    public byte[] getNewProduct() {
        byte[] newImage = null;

        Bitmap newBitmap = rlCP_Product.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 78, stream);

        newImage = stream.toByteArray();

        return newImage;
    }


    public class When_UpdateArt extends AsyncTask<String, String, String> {
        private Context context;
        private String geM;
        private ProgressDialog progressDialog;
        public When_UpdateArt(Context context, String Message) {

            this.context = context;
            this.geM = Message;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("ProgressDialog");
            progressDialog.setMessage(geM);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            EditProduct();
            //Do your loading here
            return "finish";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //Start other Activity or do whatever you want
            GotoProductScreen();
        }
    }

    public void GotoProductScreen(){
        //Start other Activity or do whatever you want
        getActivity.startActivity(new Intent(context, ShowProdcuts.class));
        getActivity.finish();
    }
}
