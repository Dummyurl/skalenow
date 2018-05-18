package co.skalenow.newrequirments;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ADMIN on 10-Apr-17.
 */

public class ProductObject extends Application implements Constants, Parcelable {

    String Product_id, Product_name, Product_price, Product_quantity, Product_image, Product_weight, Product_description, Product_code;
    Bitmap bitmap;
    String ImageUrl = "";

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getProduct_code() {
        return Product_code;
    }

    public void setProduct_code(String product_code) {
        Product_code = product_code;
    }

    public String getProduct_id() {
        return Product_id;
    }

    public void setProduct_id(String product_id) {
        Product_id = product_id;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(String product_price) {
        Product_price = product_price;
    }

    public String getProduct_quantity() {
        return Product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        Product_quantity = product_quantity;
    }

    public String getProduct_image() {
        return Product_image;
    }

    public void setProduct_image(String product_image) {
        Product_image = product_image;
    }

    public String getProduct_weight() {
        return Product_weight;
    }

    public void setProduct_weight(String product_weight) {
        Product_weight = product_weight;
    }

    public String getProduct_description() {
        return Product_description;
    }

    public void setProduct_description(String product_description) {
        Product_description = product_description;
    }

    public ProductObject() {

    }

    public ProductObject(JSONObject jsonData) {

        try {
            InputStream input = null;
//            if (jsonData.getBoolean("error") == false) {

//                this.setId(jsonData.getLong("id"));
//                this.setToUserId(jsonData.getLong("toUserId"));
//                this.setFromUserId(jsonData.getLong("fromUserId"));
//                this.setFromUserFullname(jsonData.getString("fromUserFullname"));
//                this.setFromAccount(jsonData.getBoolean("fromAccount"));
//                this.setType(jsonData.getInt("questionType"));
//                this.setText(jsonData.getString("question"));
//                this.setAddedToListAt(jsonData.getInt("addedToListAt"));
//                this.setCreateAt(jsonData.getInt("createAt"));

            this.setProduct_id(jsonData.getString("id"));
            this.setProduct_name(jsonData.getString("name"));
            this.setProduct_image(jsonData.getString("image"));
            this.setProduct_code(jsonData.getString("pcode"));
            this.setProduct_weight(jsonData.getString("weight"));
            this.setProduct_description(jsonData.getString("dscptn"));
            ImageUrl = jsonData.getString("image");
            new ImageConvert().execute();
//            this.setBitmap();
        } catch (Throwable t) {

            Log.e("Question", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Question", jsonData.toString());
        }
    }
    class ImageConvert extends AsyncTask<String, String, String> {
        @Override
        public String doInBackground(String... params) {
            Bitmap myBitmap = null;
            InputStream input = null;
            try {
                if (ImageUrl.equalsIgnoreCase("")) {

                } else {

                    URL url = new URL(ImageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    input = connection.getInputStream();

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // opts.inJustDecodeBounds = true;
                    opts.inSampleSize = 4;
                    myBitmap = BitmapFactory.decodeStream(input, null, opts);
                    //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                    //myBitmap.recycle();

                    //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    setBitmap(myBitmap);
//                    connection.disconnect();
                }
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator CREATOR = new Creator() {

        public ProductObject createFromParcel(Parcel in) {

            return new ProductObject();
        }

        public ProductObject[] newArray(int size) {
            return new ProductObject[size];
        }
    };
}