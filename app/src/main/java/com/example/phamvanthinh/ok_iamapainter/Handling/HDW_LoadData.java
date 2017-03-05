package com.example.phamvanthinh.ok_iamapainter.Handling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.example.phamvanthinh.ok_iamapainter.CreateNewScreen;
import com.example.phamvanthinh.ok_iamapainter.R;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;
import com.example.phamvanthinh.ok_iamapainter.ShowProdcuts;

/**
 * Created by nickpham on 08/10/2016.
 */

public class HDW_LoadData  {

    public Context context;
    AppCompatActivity getActivity;

    public int about_art = -1;
    public HDW_LoadData(Context context, int choice){

        this.context = context;
        this.getActivity = (AppCompatActivity) context;

        new startActivity(choice).execute();
    }

    public HDW_LoadData(Context context, int Choice, int About){


        this.context = context;
        this.getActivity = (AppCompatActivity) context;
        this.about_art = About;

        new startActivity(Choice).execute();
    }


    public class startActivity extends AsyncTask<String, String, String>{


        int Choice;
        private ProgressDialog showDialog;

        public startActivity( int Choice) {
            this.Choice = Choice;
            showDialog = new ProgressDialog(context);
            showDialog.setTitle("ProgressDialog");
            showDialog.setMessage("Loading data, please wait ...");

        }

        @Override
        protected void onPreExecute() {
            //showDialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            switch (Choice){
                case 1:
                    getActivity.startActivity(new Intent(context, CreateNewScreen.class));
                    break;
                case 2:
                    getActivity.startActivity(new Intent(context, ShowProdcuts.class));
                    break;

                case 99:

                    final Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.setType("image/jpg");
                    shareIntent.putExtra("image", new FactoryCompriseProduct(context).showProduct().get(about_art).getProductFF());
                    getActivity.startActivity(Intent.createChooser(shareIntent, "Share image using"));

                    break;
                default:
                    break;
            }

            if (about_art == -1 ){

                getActivity.overridePendingTransition(R.anim.start_slide_in_left, R.anim.slide_out_left);
                getActivity.finish();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            showDialog.dismiss();

            super.onPostExecute(s);
        }
    }

}
