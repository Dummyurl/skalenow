package co.skalenow.newrequirments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import co.skalenow.R;

/**
 * Created by ADMIN on 27-Mar-17.
 */

public class ImageZoom extends AppCompatActivity {
    ImageView imageDetail;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    Toolbar toolbar;
    String Url = "";
    Bitmap myBitmap;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_zoom_image_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageDetail = (ImageView) findViewById(R.id.imgzoom); /** * set on touch listner on image */
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent in = getIntent();
//        Url = in.getStringExtra("imageurl");
        byte[] Url=in.getByteArrayExtra("imageurl");
//        if (Url.equalsIgnoreCase("")) {
//
//        } else {
//            byte[] encodeByte = Base64.decode(Url, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(Url, 0, Url.length);
            imageDetail.setImageBitmap(bitmap);
//            new Progress().execute();
//        }


//        if (Url.equalsIgnoreCase("")) {
//            imageDetail.setBackgroundResource(R.drawable.header);
//        } else {
//            Bitmap bitmap = (Bitmap) in.getParcelableExtra("BitmapImage");
//            imageDetail.setImageBitmap(bitmap);
//
//        }


        imageDetail.setOnTouchListener(new View.OnTouchListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }
    public class Progress extends AsyncTask<String,String,String>
    {  private ProgressDialog pdia;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(ImageZoom.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }
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
            pdia.dismiss();
            pdia = null;
            imageDetail.setImageBitmap(myBitmap);
        }
    }
}

