package com.example.phamvanthinh.ok_iamapainter.DialogManager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.phamvanthinh.ok_iamapainter.GAS.ChillOfAProduct;
import com.example.phamvanthinh.ok_iamapainter.Handling.SaveProduct;
import com.example.phamvanthinh.ok_iamapainter.R;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class DialogShowWhatIsLocationtoSave {

    Context context;
    RelativeLayout rlCP_Product;
    int isUpdate;

    AppCompatActivity getActivity;
    Dialog thisMDialog;

    CheckBox CH_toThisApp, CH_toGallery;

    RadioGroup selectGroup;

    Button Save_Product, Cancel_saveProduct;
    public DialogShowWhatIsLocationtoSave(Context context, RelativeLayout rlCP_Product, int isUpdate){

        this.context = context;
        this.rlCP_Product = rlCP_Product;
        this.isUpdate  = isUpdate;

        getActivity = (AppCompatActivity)  context;

        getChoiceofClientIntoSave().show();

    }


    public Dialog getChoiceofClientIntoSave(){
        thisMDialog = new Dialog(context);

        thisMDialog.setContentView(R.layout.layout_dialog_whai_is_location_to_save);
        thisMDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AnhxaThis();

        return thisMDialog;
    }


    public void AnhxaThis(){
        selectGroup = (RadioGroup) thisMDialog.findViewById(R.id.groupForSLSelect);

        Save_Product       = (Button) thisMDialog.findViewById(R.id.btnSaveProduct_WidthChoice);
        Cancel_saveProduct = (Button) thisMDialog.findViewById(R.id.btnCancel_SaveProduct);

        View.OnClickListener listenerDialog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSaveProduct_WidthChoice:
                        int IdButtonSelect = selectGroup.getCheckedRadioButtonId();
                        /*if (CH_toThisApp.isChecked()) { DialogRemindSaveto_SQLite_Screen savetoSqLiteScreen = new DialogRemindSaveto_SQLite_Screen(context,  rlCP_Product); }
                        if (CH_toGallery.isChecked()) { SaveProduct saveProductCheck = new SaveProduct(context, rlCP_Product,0); }*/

                        if (IdButtonSelect == R.id.rbSavetoApp)      { DialogRemindSaveto_SQLite_Screen savetoSqLiteScreen = new DialogRemindSaveto_SQLite_Screen(context,  rlCP_Product, 2);}
                        if (IdButtonSelect == R.id.rbSavetoGallery)  { SaveProduct saveProductCheck = new SaveProduct(context, rlCP_Product,0);  }
                        thisMDialog.dismiss();

                        break;

                    case R.id.btnCancel_SaveProduct:
                        thisMDialog.dismiss();
                        break;
                }
            }
        };


        Save_Product.setOnClickListener(listenerDialog);
        Cancel_saveProduct.setOnClickListener(listenerDialog);


    }


    public int EditProduct(){
        rlCP_Product.setDrawingCacheEnabled(true);
        FactoryCompriseProduct compriseProduct = new FactoryCompriseProduct(context);

        List<ChillOfAProduct> getOldProduct = compriseProduct.showProduct();
        if (isUpdate != -1){

            String productName = getOldProduct.get(isUpdate).getProductName();
            if (getNewProduct() != null){
                if (compriseProduct.EditProduct(productName, getNewProduct())) {

                }
            }
        }

        rlCP_Product.destroyDrawingCache();


        return 0;
    }

    public byte[] getNewProduct(){
        byte[] newImage = null;

        Bitmap newBitmap = rlCP_Product.getDrawingCache();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.PNG, 78, stream);

        newImage = stream.toByteArray();


        return newImage;
    }
}
