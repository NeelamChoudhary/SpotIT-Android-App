package com.example.tanyasanjay.giveandtake;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Sellerpics extends AppCompatActivity {
    String latitude,longitude;
    String location, price, description, itemName;
    String sessionToken = "";
    private ImageView imageView;
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellerpics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        latitude = getIntent().getExtras().getString("latitude");
        longitude = getIntent().getExtras().getString("longitude");
        location = getIntent().getExtras().getString("location");
        price = getIntent().getExtras().getString("price");
        description = getIntent().getExtras().getString("description");
        itemName = getIntent().getExtras().getString("itemName");

        Log.d("testing", latitude + longitude + location + price + description + itemName);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            sessionToken = extras.getString("sessionToken");
        }

        imageView = (ImageView) findViewById(R.id.img_view);
        Button clickpics = (Button) findViewById(R.id.click_pics);
        clickpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
        });

        Button choosepics = (Button) findViewById(R.id.choose_pics);
        choosepics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE);*/
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        Button save = (Button) findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postlDetail("ItemCollection");


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        // final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        // Log.d("image uri path",imageUri.getPath());
                        //File f = new File(imageUri.getPath());
                        String s = getRealPathFromURI(imageUri);
                        Bitmap bitmap = null;
                        Uri uriFromPath = Uri.fromFile(new File(s));
                        Log.d("uriFromPath", uriFromPath.getPath());
                        try {
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
                            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                            imageView.setImageBitmap(scaled);

                            // imageView.setImageBitmap(bitmap);
                        /*Log.d("input Stream",imageStream.toString());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        Log.d("Bitmap",selectedImage.toString());
                        int nh = (int) ( selectedImage.getHeight() * (512.0 / selectedImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(selectedImage, 512,nh, true);
                        imageView.setImageBitmap(scaled);*/
                            // imageView.setImageBitmap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void postlDetail(String tablename) throws JSONException, UnsupportedEncodingException {

        RequestParams params = new RequestParams();
        JSONObject json = new JSONObject();


        json.put("itemType", description);
        json.put("itemPrice", price);
        json.put("itemName", itemName);
        json.put("itemSeller", sessionToken);


        //File[] files = { new File("pic.jpg"), new File("pic1.jpg") };


        Log.d("Details", json.toString());

        StringEntity entity = new StringEntity(json.toString());

        RestClient.post(this.getApplicationContext(), tablename, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    insertIntoPinCollection();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("response", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("response", response.toString());
            }
        });
    }

    public void insertIntoPinCollection() throws JSONException, UnsupportedEncodingException {

        JSONObject json = new JSONObject();

        json.put("item", itemName);
        json.put("pinLatitude", latitude);
        json.put("pinLongitude", longitude);

        Log.d("Details", json.toString());

        StringEntity entity = new StringEntity(json.toString());

        RestClient.post(this.getApplicationContext(), "PinCollection", entity, "application/json", new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("response", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                Log.d("response", response.toString());
            }
        });

    }
    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}