package wao.flutter.application.project.android_gallery_picker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;

public class GalleryActivity extends Activity {
    /** The images. */
    private ArrayList<String> images;
    private ArrayList<String> multiImagesPicked;
    MethodChannel.Result methodChannelResult;
    Button send_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(UtilProject.INSTANCE.getTitleAppBar());
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(UtilProject.INSTANCE.getColorAppBar())));
        final GridView gallery = (GridView) findViewById(R.id.galleryGridView);
        gallery.setNumColumns(4);
        gallery.setPadding(0,0,0,0);
        gallery.setHorizontalSpacing(5);
        gallery.setVerticalSpacing(5);
        final ImageAdapter adapter = new ImageAdapter(this);
        gallery.setAdapter(adapter);

        send_button = (Button)findViewById(R.id.sendButton);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilProject.INSTANCE.getResult().success(multiImagesPicked);
                UtilProject.INSTANCE.onDestroy();
                finish();
            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if(UtilProject.INSTANCE.getMultiPick().equals("false")) {
                    UtilProject.INSTANCE.getResult().success(images.get(position));
                    UtilProject.INSTANCE.onDestroy();
                    finish();
                }
                else {
                    if(multiImagesPicked == null) {
                        multiImagesPicked = new ArrayList<String>();
                    }
                    if(multiImagesPicked.contains(images.get(position))) {
                        multiImagesPicked.remove(images.get(position));
                        adapter.notifyDataSetChanged();
                    }
                    else if (multiImagesPicked.size() < UtilProject.INSTANCE.getLimitMultiPick()){
                        multiImagesPicked.add(images.get(position));
                        adapter.notifyDataSetChanged();
                    }
                    if(multiImagesPicked.size() == 0) {
                        send_button.setVisibility(View.GONE);
                    }
                    else {
                        send_button.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            if(multiImagesPicked == null) {
                multiImagesPicked = new ArrayList<String>();
            }
            if(multiImagesPicked.contains(images.get(position))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    picturesView.setBackground(context.getDrawable(R.drawable.image_border));
                }else{
                    picturesView.setBackgroundResource(R.drawable.image_border);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    picturesView.setForeground(context.getDrawable(R.drawable.image_overlay));
                }
            }
            else {
                picturesView.setBackground(null);
                picturesView.setPadding(0,0,0,0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    picturesView.setForeground(null);
                }
            }
                Glide.with(context).load(images.get(position))
                        .placeholder(R.drawable.launch_background
                        ).centerCrop()
                        .into(picturesView);
            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_folder_name, column_index_data;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
            };
            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, String.format("%s DESC", MediaStore.Images.Media.DATE_ADDED));
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        UtilProject.INSTANCE.getResult().success(null);
        UtilProject.INSTANCE.onDestroy();
        finish();
    }
}
