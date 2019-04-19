package com.example.prince.multiscreen.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prince.multiscreen.Adapters.SongAdapter;
import com.example.prince.multiscreen.MusicService;
import com.example.prince.multiscreen.R;
import com.example.prince.multiscreen.SeekbarProgress;
import com.example.prince.multiscreen.SongList;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {
    final ArrayList<SongList> list = new ArrayList<>();
    ListView listView;
    SongAdapter itemsAdapter;
    public static  SongList songlist;
    SeekBar seeekbar;
    Intent i;
    /**
     * Handles playback of all the sound files
     */

    /**
     * Handles audio focus when playing a sound file
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */



    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_numbers, container, false);
        // Create and setup the {@link AudioManager} to request audio focus
        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        i=new Intent(getActivity(),MusicService.class);
        itemsAdapter =
                new SongAdapter(getActivity(), list);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        listView = rootView.findViewById(R.id.list_View);


        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Release the media player if it currently exists because we are about to
                // play a different sound file
                // Get the {@link Word} object at the given position the user clicked on


                Intent intent=new Intent(getActivity(), SeekbarProgress.class);
                startActivity(intent);

                MusicService.releaseMediaPlayer();

                if(i!=null)
                {
                    getActivity().stopService(i);
                }
                getActivity().startService(i);
                songlist = list.get(position);
                TextView textViewSong=getActivity().findViewById(R.id.textViewSong);
                String k=songlist.getMsongName();
                String x=k.substring(0,k.length()-4);
                textViewSong.setText(x);
                textViewSong.setSelected(true);
                ImageView imageViewSong=getActivity().findViewById(R.id.imageViewSong);
                android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(songlist.getSongImage());
                try{
                    byte data[] =mmr.getEmbeddedPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageViewSong.setImageBitmap(bitmap);
                    imageViewSong.setAdjustViewBounds(true);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    imageViewSong.setImageResource(R.drawable.cover_art);

                }
                // Request audio focus so in order to play the audio file. The app needs to play a
                // short audio file, so we will request audio focus with a short amount of time
                // with AUDIOFOCUS_GAIN_TRANSIENT.
            }
        });
        checkUserPermission();

        return rootView;
    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        loadSongs();
    }

    @Override
    public void onDestroy() {
        if(i!=null)
        {
            getActivity().stopService(i);
        }        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    loadSongs();
                } else {
                    Toast.makeText(this.getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }


    public void loadSongs() {
        byte[] data=null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                    String x=cursor.getString(column_index);
                    final String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    String time = convertDuration(Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))));
                    list.add(new SongList(name, artist, time,url,x));

                } while (cursor.moveToNext());
            }

            cursor.close();
            // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
            // {@link ListView} will display list items for each word in the list of words.
            // Do this by calling the setAdapter method on the {@link ListView} object and pass in
            // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
            //  listView.setAdapter(itemsAdapter);
            listView.setAdapter(itemsAdapter);
        }
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours = 0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }
        return out;

    }


}
