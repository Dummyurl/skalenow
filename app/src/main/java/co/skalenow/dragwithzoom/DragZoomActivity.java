package co.skalenow.dragwithzoom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import co.skalenow.R;


public class DragZoomActivity extends Activity implements View.OnTouchListener {

    private ImageView imgjwellery,imgphoto;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    Button btn;
    String Url="";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Bitmap myBitmap;
    private String userChoosenTask, selectedPhoto;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_drag);
//        findViewById(R.id.myimage1).setOnTouchListener(new MyTouchListener());
//        findViewById(R.id.myimage2).setOnTouchListener(new MyTouchListener());
//        findViewById(R.id.myimage3).setOnTouchListener(new MyTouchListener());
//        findViewById(R.id.myimage4).setOnTouchListener(new MyTouchListener());
//        findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
//        findViewById(R.id.topright).setOnDragListener(new MyDragListener());
//        findViewById(R.id.bottomleft).setOnDragListener(new MyDragListener());
//        findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());
        mRrootLayout = (ViewGroup) findViewById(R.id.root);
        imgjwellery = (ImageView) mRrootLayout.findViewById(R.id.imgjwellery);
        imgphoto = (ImageView) mRrootLayout.findViewById(R.id.imgphoto);

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
//        imgjwellery.setLayoutParams(layoutParams);
//        imgphoto.setLayoutParams(layoutParams2);
//        mImageView.setOnTouchListener(this);
        imgphoto.setOnTouchListener(this);

        btn=(Button)findViewById(R.id.btnCapture);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                cameraIntent();
                selectImage();
            }
        });
        Intent in=getIntent();
         byte[] Url=in.getByteArrayExtra("imageurl");
//        if(Url.equalsIgnoreCase(""))
//        {
//        }
//        else
//        {
//            Bitmap bitmap = (Bitmap) in.getParcelableExtra("BitmapImage");
//            imgjwellery.setImageBitmap(bitmap);
//        }
//        if (Url.equalsIgnoreCase("")) {
//
//        } else {
//            byte[] encodeByte = Base64.decode(Url, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(Url, 0,Url.length);
            imgjwellery.setImageBitmap(bitmap);

//            new Progress().execute();
//        }



        imgjwellery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                System.out.println("matrix=" + savedMatrix.toString());
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        startPoint.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(midPoint, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            }
                        }
                        break;
                }
                view.setImageMatrix(matrix);
                return true;
            }

            @SuppressLint("FloatMath")
            private float spacing(MotionEvent event) {
                float x = event.getX(0) - event.getX(1);
                float y = event.getY(0) - event.getY(1);
//                return FloatMath.sqrt(x * x + y * y);
                return (x * x + y * y);
            }

            private void midPoint(PointF point, MotionEvent event) {
                float x = event.getX(0) + event.getX(1);
                float y = event.getY(0) + event.getY(1);
                point.set(x / 2, y / 2);
            }
        });
    }

//    private final class MyTouchListener implements OnTouchListener {
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData data = ClipData.newPlainText("", "");
//                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
//                        view);
//                view.startDrag(data, shadowBuilder, view, 0);
//                view.setVisibility(View.INVISIBLE);
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }
//
//    class MyDragListener implements OnDragListener {
//        Drawable enterShape = getResources().getDrawable(
//                R.drawable.shape_droptarget);
//        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
//
//        @Override
//        public boolean onDrag(View v, DragEvent event) {
//            int action = event.getAction();
//            switch (event.getAction()) {
//                case DragEvent.ACTION_DRAG_STARTED:
//                    // do nothing
//                    break;
//                case DragEvent.ACTION_DRAG_ENTERED:
//                    v.setBackgroundDrawable(enterShape);
//                    break;
//                case DragEvent.ACTION_DRAG_EXITED:
//                    v.setBackgroundDrawable(normalShape);
//                    break;
//                case DragEvent.ACTION_DROP:
//                    // Dropped, reassign View to ViewGroup
//                    View view = (View) event.getLocalState();
//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    owner.removeView(view);
//                    RelativeLayout container = (RelativeLayout) v;
//                    container.addView(view);
//                    view.setVisibility(View.VISIBLE);
//                    break;
//                case DragEvent.ACTION_DRAG_ENDED:
//                    v.setBackgroundDrawable(normalShape);
//                default:
//                    break;
//            }
//            return true;
//        }
//    }
public boolean onTouch(View view, MotionEvent event) {
    final int X = (int) event.getRawX();
    final int Y = (int) event.getRawY();
    switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            _xDelta = X - lParams.leftMargin;
            _yDelta = Y - lParams.topMargin;
            break;
        case MotionEvent.ACTION_UP:
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            break;
        case MotionEvent.ACTION_POINTER_UP:
            break;
        case MotionEvent.ACTION_MOVE:
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            layoutParams.leftMargin = X - _xDelta;
            layoutParams.topMargin = Y - _yDelta;
            layoutParams.rightMargin = (0-250);
            layoutParams.bottomMargin = (0-250);
            view.setLayoutParams(layoutParams);
            break;
    }
    mRrootLayout.invalidate();
    return true;
}
    private void selectImage() {
        final CharSequence[] items = {"Take Picture", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DragZoomActivity.this);
//        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Picture")) {
                    userChoosenTask = "Take Picture";
                        cameraIntent();

                } else if (items[item].equals("Select from Gallery")) {
                    userChoosenTask = "Select from Gallery";
                        galleryIntent();

                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                // String selectedPhoto contains the path of selected Image
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPhoto = cursor.getString(columnIndex);
                cursor.close();

            }
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgphoto.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgphoto.setImageBitmap(thumbnail);
    }
    public class Progress extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            try {
                InputStream input = null;
                URL url = new URL(Url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.connect();
                input = connection.getInputStream();

                BitmapFactory.Options opts = new BitmapFactory.Options();
                // opts.inJustDecodeBounds = true;
                opts.inSampleSize = 2;
                myBitmap = BitmapFactory.decodeStream(input, null, opts);

                //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                //myBitmap.recycle();

                //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                connection.disconnect();
            } catch (IOException e) {

            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imgjwellery.setImageBitmap(myBitmap);
        }
    }
}