package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.exifinterface.media.ExifInterface;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.apache.commons.io.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class MainActivity<ImageCapture> extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int GALLERY_CODE = 1002;
    Uri image_uri;
    ImageView imageViewlogoapp, backgroundImageView;
    ZoomableImageView firstImageView;
    ZoomableImageView secondImageView;
    Bitmap image2save;
    Bitmap bitmap;
    SeekBar rotationBar;
    ImageView rotationImage;
    String imageString="";
    PaintView paintView;
    RelativeLayout canvasLayout;
    FloatingActionButton glorification, camera, gallery, saveImage, clearDraw;
    ProgressBar progressb;
    boolean imgAdded= false;
    boolean finishedPythonTask = false;
    RadioButton firstRadioButton;
    RadioButton secondRadioButton;
    RadioGroup radio;

    Rect firstImageRect, secondImageRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.imageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(firstRadioButton.isChecked())
                    firstImageView.onTouch(view,motionEvent);
                else
                    secondImageView.onTouch(view,motionEvent);
                return true;
            }
        });


        glorification = findViewById(R.id.fab);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);
        saveImage = findViewById(R.id.save_image);
        clearDraw = findViewById(R.id.clear_draw);

        firstImageView = findViewById(R.id.imageView);
        secondImageView = findViewById(R.id.imageView2);
        backgroundImageView = findViewById(R.id.backgroundImageView);
        imageViewlogoapp = findViewById(R.id.imageViewlogoapp);
        canvasLayout = findViewById(R.id.canvasLyt);
        progressb = findViewById(R.id.progress_bar);
        paintView = new PaintView(this);
        rotationBar = findViewById(R.id.rotationBar);
        rotationImage = findViewById(R.id.rotate_image);
        radio=findViewById(R.id.radio);
        firstRadioButton=findViewById(R.id.firstRadio);
        secondRadioButton=findViewById(R.id.secondRadio);
        rotationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(firstRadioButton.isChecked())
                    firstImageView.setRotation(i);
                else
                    secondImageView.setRotation(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED) {
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), GALLERY_CODE);

            }
        });

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        glorification.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (paintView == null || bitmap == null) {
                    Toast.makeText(MainActivity.this, "First select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!paintView.firstObjectMarked){
                    Toast.makeText(MainActivity.this, "Please mark an object", Toast.LENGTH_SHORT).show();
                    return;
                }
                firstImageRect = new Rect(paintView.getMinx(), paintView.getMiny(), paintView.getMaxx(), paintView.getMaxy());
                secondImageRect = null;

                if (paintView.secondObjectMarked)
                    secondImageRect = new Rect(paintView.getMinx2(), paintView.getMiny2(), paintView.getMaxx2(), paintView.getMaxy2());
                PythonTask pythonTask = new PythonTask(MainActivity.this, firstImageRect, secondImageRect);
                imageString = getStringImage(bitmap);
                pythonTask.execute(imageString);
                            }
        });

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        saveImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                save2gallery();
            }
        });

        clearDraw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearDraw();
                rotationBar.setVisibility(View.INVISIBLE);
                rotationImage.setVisibility(View.INVISIBLE);
                radio.setVisibility(View.INVISIBLE);
                firstRadioButton.setChecked(true);
                rotationBar.setProgress(0);
                showFABMenu();
            }
        });
    }

    private void clearDraw(){
        if (!paintView.firstObjectMarked){
            Toast.makeText(MainActivity.this, "Nothing to clear", Toast.LENGTH_SHORT).show();
            return;
        }

        create_canvas();

    }

    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }//w ww  . j a v a2 s .co  m
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) / 2, null);
        canvas.save();
        canvas.restore();
        return newmap;
    }

    private void save2gallery(){
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                image2save,
                String.format("%d.jpeg", System.currentTimeMillis()),
                ""
        );

        Toast.makeText(this, "Saved: " + savedImageURL, Toast.LENGTH_SHORT).show();
    }

    private void enabled_buttons(boolean enabled){
        glorification.setEnabled(enabled);
        clearDraw.setEnabled(enabled);
        saveImage.setEnabled(enabled);
        gallery.setEnabled(enabled);
        camera.setEnabled(enabled);
        glorification.setClickable(enabled);
        clearDraw.setClickable(enabled);
        saveImage.setClickable(enabled);
        gallery.setClickable(enabled);
        camera.setClickable(enabled);
        if(!enabled){
            progressb.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            progressb.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openCamera();
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            if (requestCode == GALLERY_CODE) {
                // Get the url of the image from data
                image_uri = data.getData();
            }
            //when activity result is on, create canvas will be played
            create_canvas();
            Toast.makeText(MainActivity.this, "Please mark an object", Toast.LENGTH_SHORT).show();
        }
        if(!imgAdded) {
            showCLEARMenu();
            showFABMenu();
            showSAVEMenu();
            rotationBar.setVisibility(View.INVISIBLE);
            rotationImage.setVisibility(View.INVISIBLE);
            radio.setVisibility(View.INVISIBLE);
            rotationBar.setProgress(0);
        }
    }

    private static int getOrientation(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ExifInterface ei = new ExifInterface(bais);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        return orientation;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void create_canvas(){
        byte[] bytes;
        firstImageView.setVisibility(View.INVISIBLE);
        secondImageView.setVisibility(View.INVISIBLE);
        backgroundImageView.setVisibility(View.INVISIBLE);
        try {
            InputStream is = getContentResolver().openInputStream(image_uri);
            bytes = IOUtils.toByteArray(is);
            bitmap = uriToBitmap(bytes);
            int orientation = getOrientation(bytes);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d("TAG", "90");
                    bitmap = rotateBitmap(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d("TAG", "180");
                    bitmap = rotateBitmap(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d("TAG", "270");
                    bitmap = rotateBitmap(bitmap, 270);
                    break;
                default:
                    Log.d("TAG", "0");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageViewlogoapp.setVisibility(View.GONE);
        firstImageView.setImageBitmap(bitmap);
		bitmap = ImageResizer.reduceBitmapSize(bitmap, 240000);
        canvasLayout.removeAllViews();
        paintView.setBitmap(bitmap);
        canvasLayout.addView(paintView);

        canvasLayout.setVisibility(View.VISIBLE);
        paintView.firstObjectMarked=false;
        paintView.secondObjectMarked=false;
    }

    private Bitmap uriToBitmap( byte[] bytes) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return image;
    }

    private void showFABMenu(){
        imgAdded = true;
        glorification.setVisibility(View.VISIBLE);
        glorification.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
    }

    private void closeFABMenu(){
        imgAdded=false;
        glorification.setVisibility(View.INVISIBLE);
        glorification.animate().translationY(0);
    }

    private void showCLEARMenu(){
        imgAdded = true;
        clearDraw.setVisibility(View.VISIBLE);
    }

    private void closeCLEARMenu(){
        imgAdded=false;
        clearDraw.animate().translationY(0);
    }


    private void showSAVEMenu(){
        imgAdded = true;
        saveImage.setVisibility(View.VISIBLE);
    }

    private void closeSAVEMenu(){
        imgAdded=false;
        saveImage.animate().translationY(0);
    }


    private String getStringImage(Bitmap bitamp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    class PythonTask extends AsyncTask<String,Void,String>{
        ProgressDialog pd;
        Rect firstRect;
        Rect secondRect;

        public PythonTask(Activity activity, Rect recti, Rect recti2) {
            pd=new ProgressDialog(activity);
            firstRect=recti;
            secondRect=recti2;
            firstImageView.setRect(firstRect);
            secondImageView.setRect(secondRect);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enabled_buttons(false);
        }

        @Override
        protected void onPostExecute(String s) {

            String[] separated=s.split("separate");
            super.onPostExecute(separated[0]);

            byte[] firstObjectData = android.util.Base64.decode(separated[0], Base64.DEFAULT);
            Bitmap firstObjectBitmap = BitmapFactory.decodeByteArray(firstObjectData, 0, firstObjectData.length);

            byte[] backgroundData = android.util.Base64.decode(separated[2], Base64.DEFAULT);
            Bitmap backgroundBitmap = BitmapFactory.decodeByteArray(backgroundData, 0, backgroundData.length);

            firstImageView.setImageBitmap(firstObjectBitmap);
            backgroundImageView.setImageBitmap(backgroundBitmap);
            image2save = combineBitmap(backgroundBitmap, firstObjectBitmap);

            canvasLayout.setVisibility(View.INVISIBLE);
            firstImageView.setVisibility(View.VISIBLE);
            backgroundImageView.setVisibility(View.VISIBLE);

            if (secondRect!=null){
                byte[] secondObjectData = android.util.Base64.decode(separated[1], Base64.DEFAULT);
                Bitmap secondObjectBitmap = BitmapFactory.decodeByteArray(secondObjectData, 0, secondObjectData.length);
                secondImageView.setImageBitmap(secondObjectBitmap);
                secondImageView.setVisibility(View.VISIBLE);
                radio.setVisibility(View.VISIBLE);
                image2save = combineBitmap(image2save, secondObjectBitmap);
            }

            enabled_buttons(true);
            closeFABMenu();
            rotationBar.setVisibility(View.VISIBLE);
            rotationImage.setVisibility(View.VISIBLE);
            finishedPythonTask= true;
        }

        @Override
        protected String doInBackground(String... strings) {
            String firstObjectString = "";
            String sedondObjectString = "";
            String backgroundString = "";

            try{
                final Python py = Python.getInstance();
                PyObject pyobj = py.getModule("grabcut");

                PyObject firstObject = pyobj.callAttr("edit_pic", strings[0], firstRect.left,firstRect.top, firstRect.right, firstRect.bottom);

                if (secondRect!=null){
                    PyObject secondObject = pyobj.callAttr("edit_pic", strings[0], secondRect.left,secondRect.top, secondRect.right, secondRect.bottom);
                    sedondObjectString=secondObject.toString();
                }

                PyObject backgroundObject = pyobj.callAttr("get_background", strings[0]);

                firstObjectString = firstObject.toString();
                backgroundString = backgroundObject.toString();

            }catch (Exception e){
                e.printStackTrace();
            }
            String res=firstObjectString+"separate"+sedondObjectString+"separate"+backgroundString;
            return res;
        }
    }
}