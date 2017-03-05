package com.example.phamvanthinh.ok_iamapainter.Handling;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.phamvanthinh.ok_iamapainter.ShowProdcuts;

import java.io.File;
import java.util.Calendar;


public class SaveProduct {

    Context context;
    RelativeLayout layoutCPProduct;
    AppCompatActivity getActivity;

    private String ImageName = "bitmapIAM";
    private String mota = "My bitmap created by I am a painter app ";

    int ChoiceToSave = 0;


    public SaveProduct(Context context, RelativeLayout getLayoutCPProduct, int ClientChoicetoSave){

        this.context = context;
        this.layoutCPProduct = getLayoutCPProduct;
        this.ChoiceToSave = ClientChoicetoSave;

        getActivity = (AppCompatActivity)context;
        checkAccepClient().show();

    }

    private Dialog checkAccepClient()
    {

        final AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
        saveDialog.setTitle("Notification !");
        saveDialog.setMessage("Are you sure to save this Producr ");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (CheckPermission()){
                    Log.d("Acti", "Show dialog request save product");
                    SaveImagetoGallery();
                }else { dialotoGuiderClient().show(); }


                //if (ChoiceToSave == 1){ SaveIntoThisApp(); }
                dialog.dismiss();
            }
        });

        saveDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ActionWhenSave", "NO");
                dialog.dismiss();
            }
        });

        return saveDialog.create();
    }


    @TargetApi(Build.VERSION_CODES.M)
    private boolean CheckPermission(){
        Boolean checkPermission = false;
        int checkP = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkP == PackageManager.PERMISSION_GRANTED){
            checkPermission = true;
        }else {
            checkPermission = false;
        }

        return checkPermission;
    }

    // Save Image to Gallery after check permission action
    private void SaveImagetoGallery(){
        layoutCPProduct.setDrawingCacheEnabled(true);

        Bitmap bitmaptoSave = layoutCPProduct.getDrawingCache();
        ContentResolver cr = getActivity.getContentResolver();

        // Will save image with this Name (bitmapIAM + Save Time)
        for (int i = 0;i < getCurrentDateAndTime().length; i++) ImageName = ImageName + "_" + getCurrentDateAndTime()[i];

        String savedURL = MediaStore.Images.Media.insertImage(cr, bitmaptoSave, ImageName, mota);
        Toast.makeText(context, savedURL, Toast.LENGTH_LONG).show();

        File f = new File(savedURL);
        if (f.exists()){
            Log.d("ActionWhenGetImagePath", "Thanh cong");
        }else {
            Log.d("ActionWhenGetImagePath", "That bai");
        }

        getActivity.startActivity(new Intent(context, ShowProdcuts.class));
        getActivity.finish();

        layoutCPProduct.destroyDrawingCache();

    }


    // Hiện thị thông báo hướng dẫn người dùng cấp quyền cho phép truy cập ;

    private Dialog dialotoGuiderClient()
    {

        AlertDialog.Builder nDialog = new AlertDialog.Builder(context);
        nDialog.setTitle("Notification");
        nDialog.setMessage("Bạn chưa ban bố quyền cho app" +
                ", muốn thực hiện chức này thì bạn cần chấp nhận quyền bằng cách vào cài đặt -> ứng dụng -> I am a Painter -> Quyền để bật quyền cho app này");
        nDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return nDialog.create();

    }



    // Lấy thời gian khi người dùng muốn lưu hình ảnh vào thiết bị đang hiện hành
    private String[] getCurrentDateAndTime()
    {
        Calendar nowTime = Calendar.getInstance();
        return new String[]{
                String.valueOf(nowTime.get(Calendar.YEAR)),
                String.valueOf(nowTime.get(Calendar.MONTH)),
                String.valueOf(nowTime.get(Calendar.DAY_OF_MONTH) +  1),
                String.valueOf(nowTime.get(Calendar.HOUR_OF_DAY)),
                String.valueOf(nowTime.get(Calendar.MINUTE))
        };
    }


}
