package com.example.phamvanthinh.ok_iamapainter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ViewFlipper;

import com.example.phamvanthinh.ok_iamapainter.DialogManager.DialogRemindSaveto_SQLite_Screen;
import com.example.phamvanthinh.ok_iamapainter.DialogManager.DialogShowWhatIsLocationtoSave;
import com.example.phamvanthinh.ok_iamapainter.DialogManager.DialogUpdateNewArt;
import com.example.phamvanthinh.ok_iamapainter.Handling.SaveProduct;
import com.example.phamvanthinh.ok_iamapainter.Handling.SingleTouchEventView;
import com.example.phamvanthinh.ok_iamapainter.SQLiteManager.FactoryCompriseProduct;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.larswerkman.holocolorpicker.ColorPicker;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class CreateNewScreen extends AppCompatActivity {

    public int position_SQLITE = -1;

    public RelativeLayout locationtoDraw, ViewGroup_CPTools;
    public SingleTouchEventView layoutToDraw;
    public ViewFlipper ManagerSupportToDraw;
    public static CircleImageView[] btnChangePaintSimpleColor;
    private static CircleImageView showColorNow;
    public ImageButton btnChangePaintColor, btnClearCanvas;
    public ImageButton imgSetPaintAphal, btnWaterBrush, btnBigBrush, btnDefaultBrush;
    ImageView btnShowNetx, btnShowPrevious;
    public static SeekBar sbChangePainWidthStroke;
    public int StrokeWidth = 12, PaintColor = Color.BLACK;
    private Animation slide_in_left, slide_in_right, slide_out_left, slide_out_right;

    boolean isShowManagerAllSupporttoDraw = true;
    public View.OnClickListener actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_new_screen);
        // get data from show product screen
        getBundle();

        CustomActionBar();

        custom_popup_stroke_width();

        // Tao moi View hoac bien toan bo (public, private)
        CreateNewVG();

        layoutToDraw = new SingleTouchEventView(this);
        locationtoDraw.addView(layoutToDraw);

        if (position_SQLITE != -1)
        {

            FactoryCompriseProduct fcp = new FactoryCompriseProduct(this);
            Bitmap bpToDraw = BitmapFactory.decodeByteArray
                    (fcp.showProduct().get(position_SQLITE).getProductFF(), 0, fcp.showProduct().get(position_SQLITE).getProductFF().length);
            layoutToDraw.setImageFromGallery(bpToDraw);

        }else {

        }
    }

    public int getBundle()
    {

        if(getIntent().getBundleExtra("DataFromShowFactory") != null)
        {

            Bundle getData = getIntent().getBundleExtra("DataFromShowFactory");
            position_SQLITE = getData.getInt("positionInSQLITE");

        }else {

        }
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int CustomActionBar()
    {
        Toolbar toolbarThis = (Toolbar) this.findViewById(R.id.actionbarCreateNewScreen);

        this.setSupportActionBar(toolbarThis);
        this.getSupportActionBar().setTitle("Create New");
        this.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        return 0;
    }

    public void CreateNewVG() {
        Listerner_Change_PR_Paint();

        locationtoDraw          = (RelativeLayout) this.findViewById(R.id.ve                        );
        ViewGroup_CPTools       = (RelativeLayout) this.findViewById(R.id.ViewGroup_CPTools         );
        ManagerSupportToDraw    = (ViewFlipper   ) this.findViewById(R.id.ToolManagerToDraw         );
        btnChangePaintColor     = (ImageButton   ) this.findViewById(R.id.changePaintColor          );
        btnClearCanvas          = (ImageButton   ) this.findViewById(R.id.clearCanvas               );
        btnDefaultBrush         = (ImageButton   ) this.findViewById(R.id.btnDefaultBrush           );
        imgSetPaintAphal        = (ImageButton   ) this.findViewById(R.id.imgSetPaintAphal          );
        btnBigBrush             = (ImageButton   ) this.findViewById(R.id.btnBigBrush               );
        btnShowNetx             = (ImageView     ) this.findViewById(R.id.btnShowNextViewFlipper    );
        btnShowPrevious         = (ImageView     ) this.findViewById(R.id.btnShowPerviousViewFlipper);

        //btnWaterBrush           = (ImageButton   ) this.findViewById(R.id.btnWaterBrush             );
        //btnWaterBrush      .setOnClickListener(actionButton);

        // Button này đảm nhận nhiệm vụ hiện thì bảng màu đầy đủ cho người dùng chọn màu cho cõ vẽ
        btnChangePaintColor.setOnClickListener(actionButton);
        btnShowNetx        .setOnClickListener(actionButton);
        btnShowPrevious    .setOnClickListener(actionButton);
        btnDefaultBrush    .setOnClickListener(actionButton);
        imgSetPaintAphal   .setOnClickListener(actionButton);
        btnBigBrush        .setOnClickListener(actionButton);

        // Mảng button bao gồm những button chọn màu cho cõ vẽ (màu đỏ, màu đen, màu đỏ, màu vàng , màu cam );
        btnChangePaintSimpleColor = new CircleImageView[]{
                (CircleImageView) CreateNewScreen.this.findViewById(R.id.defaultPainColor),
                (CircleImageView) CreateNewScreen.this.findViewById(R.id.RedtPainColor),
                (CircleImageView) CreateNewScreen.this.findViewById(R.id.YellowPaintColor),
                (CircleImageView) CreateNewScreen.this.findViewById(R.id.OrangePainColor)
        };
        showColorNow = (CircleImageView) this.findViewById(R.id.showColorNow);

        AboutSupportManagertoDraw();

        //sbChangePainWidthStroke = (SeekBar       ) this.findViewById(R.id.seekBartoCustomWidthStroke);
        //AboutSeekBarChangeWidthStroke();

        AboutViewGroupListenner();

    }

    // Click to show popupwindow(custom Stroke Width)
    public void Show_Custom_Stroke_Width(View view)
    {
        if (_custom_stroke_width.isShowing() == false)
        {

            _custom_stroke_width.showAtLocation(view, Gravity.TOP, 90, 20);

        }else
        {

            _custom_stroke_width.dismiss();

        }
    }

    public void AboutViewGroupListenner(){
        // Khi người dùng giữa tay lâu lên ký hiệu tẩy thì sẽ xoá tất cả những gì đã vẽ bao gồm hình vẽ đã lấy từ thư viện
        btnClearCanvas.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                for (int i = 0; i < btnChangePaintSimpleColor.length; i++)
                {

                    btnChangePaintSimpleColor[i].setBorderColor(Color.BLACK);

                }

                layoutToDraw.clearAll();

                return true;
            }
        });


    }

    public static PopupWindow _custom_stroke_width;
    public void custom_popup_stroke_width()
    {
        _custom_stroke_width = new PopupWindow(CreateNewScreen.this);

        View vcn = View.inflate(this, R.layout.popup_custom_stroke_width_layout, null);

        _custom_stroke_width.setContentView(vcn);

        sbChangePainWidthStroke = (SeekBar) _custom_stroke_width.getContentView().findViewById(R.id.seekBartoCustomWidthStroke);
        AboutSeekBarChangeWidthStroke();

    }

    public void AboutSupportManagertoDraw() {
        //create animations for ViewFlipper
        slide_in_left   = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_in_right  = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_out_left  = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
    }

    public void AboutSeekBarChangeWidthStroke() {
        sbChangePainWidthStroke.setProgress(StrokeWidth / 4);

        sbChangePainWidthStroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

                StrokeWidth = progress * 4;

                layoutToDraw.changePaintSingle(PaintColor, StrokeWidth);

                Log.d("ResultWhenChangeSB", String.valueOf(StrokeWidth));

            }
            // Not use
            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            } @Override  public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
    }

    // Action of buttons change paint properties
    public void Listerner_Change_PR_Paint()
    {
        actionButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() != R.id.changePaintColor && v.getId() != R.id.btnShowNextViewFlipper && v.getId() != R.id.btnShowPerviousViewFlipper )
                {
                    layoutToDraw.returnPainProperties();
                    layoutToDraw.showAll();
                }

                switch (v.getId()) {
                    case R.id.changePaintColor:

                        Log.d("ActionFromActivity", "You are changing paint color, good luck !");

                        new pickerDialog();

                        break;
                    case R.id.btnShowNextViewFlipper:

                        SoSanhForSupportToDraw(0);

                        break;

                    case R.id.btnShowPerviousViewFlipper:

                        SoSanhForSupportToDraw(1);

                        break;
                    case R.id.btnDefaultBrush:

                        layoutToDraw.setIsDefaultBrush();

                        break;
                    case R.id.imgSetPaintAphal:

                        if(layoutToDraw.isAlphaBrush != true)
                        {
                            layoutToDraw.setPaintAlpha();
                        }

                        break;
                    /**
                     * case R.id.btnWaterBrush:

                        // Co mau nuoc
                        if (layoutToDraw.getIsWaterBrush() != true )
                        {
                            layoutToDraw.setIsWaterBrush(true);
                        }

                        break;

                     */

                    case R.id.btnBigBrush:
                        if (layoutToDraw.getIsBigBrush() != true)
                        {
                            layoutToDraw.setBigBrush(true);
                        }
                        // Duong ve to
                        break;

                }
            }
        };
    }


    // Action of button change color paint with simple dialog color
    public void btnPaintChangColorSimple(View view)
    {
        int ViewId = view.getId();
        ComparevsSetPainSimpleColor(ViewId);

        for (int i = 0; i < btnChangePaintSimpleColor.length; i++) {
            if (ViewId == btnChangePaintSimpleColor[i].getId()) {
                // Vien button se chuyen sang mau do khi duoc cho
                btnChangePaintSimpleColor[i].setBorderColor(Color.RED);
            } else {
                // Vien button se chuyen sang mau den khi khong duoc chon
                btnChangePaintSimpleColor[i].setBorderColor(Color.BLACK);
            }
        }
    }

    public void ComparevsSetPainSimpleColor(int ViewId)
    {

        /** Kiểm tra coi thử người dùng muốn chọn màu đơn giản nào cho cõ vẽ  */
        switch (ViewId) {
            case R.id.clearCanvas:
                layoutToDraw.changePaintSingle(getResources().getColor(R.color.backgroundofViewAboutProduct), 20);
                PaintColor = getResources().getColor(R.color.backgroundofViewAboutProduct); // Co ve mau trang ( tuong duoc voi viec tay )
                break;
            case R.id.defaultPainColor:
                layoutToDraw.changePaintSingle(Color.BLACK, StrokeWidth);
                PaintColor = Color.BLACK; // Co ve mau den
                break;
            case R.id.RedtPainColor:
                layoutToDraw.changePaintSingle(Color.RED, StrokeWidth);
                PaintColor = Color.RED;// Co ve mau do
                break;
            case R.id.YellowPaintColor:
                layoutToDraw.changePaintSingle(Color.YELLOW, StrokeWidth);
                PaintColor = Color.YELLOW; // Co ve mau vang
                break;
            case R.id.OrangePainColor:
                PaintColor = getResources().getColor(R.color.PaintOrangeColor); // Co ve mau cam
                layoutToDraw.changePaintSingle(getResources().getColor(R.color.PaintOrangeColor), StrokeWidth);
                break;
        }
    }

    // Animation when touch left manager tools
    public void SoSanhForSupportToDraw(int Choice)
    {

        ManagerSupportToDraw.setOutAnimation(slide_out_right);
        ManagerSupportToDraw.setInAnimation(slide_in_left);


        if (Choice == 1){
            Log.d("ActionWhenTouch: --> ", "ShowNext");
            ManagerSupportToDraw.showPrevious();
        }
        if (Choice == 0) {
            Log.d("ActionWhenTouch: --> ", "ShowPrevious");

            ManagerSupportToDraw.setInAnimation(slide_in_right);
            ManagerSupportToDraw.setOutAnimation(slide_out_left);

            ManagerSupportToDraw.showNext();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.menu_create_new_screen, menu);
        return true;

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (position_SQLITE != -1)
                {
                    // Show dialog remind if this product is showing have been created
                    new DialogUpdateNewArt(CreateNewScreen.this, locationtoDraw, position_SQLITE);

                }else
                {

                    // Show dialog remind location to save this product is showing ( only save )
                    new DialogRemindSaveto_SQLite_Screen(CreateNewScreen.this, locationtoDraw, 1);

                }


                break;

            case R.id.saveProduct:
                Log.d("ActionMenu: ", "Save product ");
                new DialogShowWhatIsLocationtoSave(CreateNewScreen.this, locationtoDraw, position_SQLITE);
                break;

            case R.id.actiontoPickImageFromGallery:

                Log.d("ActionMenu: ","Pick Iamge from gallery");
                CheckPermissionToPickImageFromGallery();
                break;

        }


        return true;
    }

    public void HAS_ManagerSupport()
    {

        if (isShowManagerAllSupporttoDraw)
        {

            ViewGroup_CPTools.setVisibility(View.INVISIBLE);
            isShowManagerAllSupporttoDraw = false;

        }else
        {

            ViewGroup_CPTools.setVisibility(View.VISIBLE);
            isShowManagerAllSupporttoDraw = true;

        }

    }

    private static final int SELECT_PHOTO = 100;
    private String IntentType = "image/*";
    public void CheckPermissionToPickImageFromGallery()
    {

        int checkPermission = ContextCompat.checkSelfPermission(CreateNewScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(checkPermission == PackageManager.PERMISSION_GRANTED)
        {

            Log.d("ActionPick", "OK");
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType(IntentType);
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }else
        {

            Log.d("ActionPick", "Chua duoc dau !");

        }

    }





    // Nhiem vu cua class pickerDialog la dung de hien thi bang mau cho nguoi dung thay doi mau cua co ve theo so thich cua minh
    public class pickerDialog
    {

        public pickerDialog() { changePaintColorDialog(); }

        public void changePaintColorDialog() {
            final int[] ChoicePaintColor = {PaintColor};
            ColorPickerDialogBuilder
                    .with(CreateNewScreen.this)
                    .setTitle("Choose color")
                    .initialColor(PaintColor)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            ChoicePaintColor[0] = selectedColor;
                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            if (ChoicePaintColor[0] != PaintColor){
                                PaintColor = ChoicePaintColor[0];
                                layoutToDraw.changePaintSingle(PaintColor, StrokeWidth);
                            }else
                            {
                                Log.d("ResultWhenChangeColor ", "Khong hop ly");
                            }

                            for (int i = 0; i < btnChangePaintSimpleColor.length; i ++) btnChangePaintSimpleColor[i].setBorderColor(Color.BLACK);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();

        }

    }


    // Get image from garrely to pick for canvas
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PHOTO)
        {

            if (resultCode == RESULT_OK)
            {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                layoutToDraw.setImageFromGallery(BitmapFactory.decodeFile(picturePath));


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void show_tools_manager(View view)
    {

        ImageView cov_img = (ImageView) view;

        HAS_ManagerSupport();
        if(isShowManagerAllSupporttoDraw)
        {
            cov_img.setImageResource(R.mipmap.down_arrow_icon);
        }else
        {
            cov_img.setImageResource(R.mipmap.up_arrow_icon);
        }
    }
}


