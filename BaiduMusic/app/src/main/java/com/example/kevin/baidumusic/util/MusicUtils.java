package com.example.kevin.baidumusic.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.List;

/**
 * Created by kevin on 16/6/4.
 */
public class MusicUtils {

    public static void scanMusic(Context context, List<LocalMusic> musicList){
        musicList.clear();
        Cursor cursor=context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor!=null){
            while (cursor.moveToNext()){
                int isMusic=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if ((isMusic==0)){
                    continue;
                }
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String unknown = "未知";
                artist = artist.equals("<unknown>") ? unknown : artist;
                String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                LocalMusic music = new LocalMusic();
                music.setId(id);
                music.setTitle(title);
                music.setArtist(artist);
                music.setAlbum(album);
                music.setDuration(duration);
                music.setUri(url);
                music.setFileName(fileName);
                musicList.add(music);
            }
            cursor.close();
        }
    }

}
