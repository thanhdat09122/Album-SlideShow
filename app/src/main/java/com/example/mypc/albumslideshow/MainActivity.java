package com.example.mypc.albumslideshow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    CheckBox checkBox;
    ImageButton imageButton1, imageButton2;

    int currentPosition = -1;
    ArrayList<String> albums;

    Timer timer = null;
    TimerTask timerTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();

    }

    private void addEvents() {
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolveNextImage();
            }
        });
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolvePreviousImage();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    imageButton1.setEnabled(false);
                    imageButton2.setEnabled(false);

                    SolveRandomImage();
                } else {
                    imageButton1.setEnabled(true);
                    imageButton2.setEnabled(true);
                    if(timerTask!=null)
                        timerTask.cancel();
                }
            }
        });
    }

    private void SolveRandomImage() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentPosition++;
                        if(currentPosition == albums.size() - 1)
                            currentPosition = 0;
                        ImageTask imageTask = new ImageTask();
                        imageTask.execute(albums.get(currentPosition));
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);

    }

    private void SolvePreviousImage() {
        currentPosition--;
        if(currentPosition == -1)
            currentPosition = albums.size() -1;
        ImageTask imageTask = new ImageTask();
        imageTask.execute(albums.get(currentPosition));
    }

    private void SolveNextImage() {
        currentPosition++;
        if(currentPosition == albums.size()-1)
            currentPosition = 0;
        ImageTask imageTask = new ImageTask();
        imageTask.execute(albums.get(currentPosition));
    }

    private void addControls() {
        imageView = (ImageView) findViewById(R.id.image);
        checkBox  = (CheckBox) findViewById(R.id.checkBox);
        imageButton1 = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        albums = new ArrayList<>();
        albums.add("https://gazettereview.com/wp-content/uploads/2017/09/2-2.jpg");
//        albums.add("http://www.wallpapers-web.com/data/out/145/5056614-most-popular-hollywood-actresses-wallpapers.jpg");
        albums.add("https://img.zergnet.com/271470_300.jpg");
        albums.add("http://resources.toppik.com.au/assets/Uploads/emma-eyebrows.jpg");
        albums.add("https://img8.androidappsapk.co/300/2/6/1/com.zenith.girls.Wallpaper.png");
        albums.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3NedUSQg9vUkRWvpy_rhX2UgVazNaTdTOyxXaZWVo5jgL7LFbEg");
        albums.add("https://i.pinimg.com/originals/44/51/69/445169d036b22e41382f8b6ff0bc085c.jpg");
        albums.add("http://f9view.com/wp-content/uploads/2013/11/high-definition-backgrounds-for-win-300x300.jpg");
        albums.add("https://vnn-imgs-f.vgcloud.vn/2018/12/03/12/hoa-hau-dam-luu-ly-tiet-lo-chuyen-nghi-lam-tiep-vien-hang-khong-de-sinh-con-2.jpg");
        currentPosition =0;
        ImageTask imageTask = new ImageTask();
        imageTask.execute(albums.get(currentPosition));
    }
    class ImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try{
                String link = strings[0];
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(link).getContent());
                return bitmap;
            }
            catch (Exception ex)
            {
                Log.d("Error", ex.toString());
            }
            return null;
        }
    }
}
