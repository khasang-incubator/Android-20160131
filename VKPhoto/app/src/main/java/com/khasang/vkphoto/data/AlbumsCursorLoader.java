package com.khasang.vkphoto.data;

import android.content.Context;
import android.database.Cursor;

import com.khasang.vkphoto.data.local.LocalAlbumSource;

public class AlbumsCursorLoader extends android.support.v4.content.CursorLoader {
    private LocalAlbumSource localAlbumSource;

    public AlbumsCursorLoader(Context context, LocalAlbumSource localAlbumSource) {
        super(context);
        this.localAlbumSource = localAlbumSource;
    }

    @Override
    public Cursor loadInBackground() {
        return localAlbumSource.getAllSynchronizedAlbumsCursor();
    }
}
