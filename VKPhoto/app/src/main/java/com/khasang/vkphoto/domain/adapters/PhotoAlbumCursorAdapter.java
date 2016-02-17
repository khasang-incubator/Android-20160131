package com.khasang.vkphoto.domain.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khasang.vkphoto.R;
import com.khasang.vkphoto.data.RequestMaker;
import com.khasang.vkphoto.domain.DownloadFileAsyncTask;
import com.khasang.vkphoto.domain.entities.Photo;
import com.khasang.vkphoto.domain.entities.PhotoAlbum;
import com.khasang.vkphoto.domain.events.ErrorEvent;
import com.khasang.vkphoto.util.Constants;
import com.khasang.vkphoto.util.JsonUtils;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.greenrobot.eventbus.EventBus;

public class PhotoAlbumCursorAdapter extends CursorRecyclerViewAdapter<PhotoAlbumCursorAdapter.ViewHolder> {

    public PhotoAlbumCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.bindPhotoAlbum(new PhotoAlbum(cursor));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photoalbum_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView albumThumbImageView;
        final TextView albumTitleTextView;
        final TextView albumPhotoCountTextView;
        PhotoAlbum photoAlbum;

        public ViewHolder(View itemView) {
            super(itemView);
            albumThumbImageView = (ImageView) itemView.findViewById(R.id.album_thumb);
            albumTitleTextView = (TextView) itemView.findViewById(R.id.album_title);
            albumPhotoCountTextView = (TextView) itemView.findViewById(R.id.tv_count_of_albums);
        }

        public void bindPhotoAlbum(final PhotoAlbum photoAlbum) {
            this.photoAlbum = photoAlbum;
            albumTitleTextView.setText(photoAlbum.title);
            String photoCount = albumPhotoCountTextView.getContext().getString(R.string.count_of_photos_in_album, photoAlbum.size);
            albumPhotoCountTextView.setText(photoCount);
            if (photoAlbum.thumb_id != 0) {
                loadThumb(photoAlbum);
            }
        }

        private void loadThumb(final PhotoAlbum photoAlbum) {
            if (photoAlbum.thumb_id != Constants.NULL) {
                RequestMaker.getPhotoAlbumThumb(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        try {
                            Photo photo = JsonUtils.getItems(response.json, Photo.class).get(0);
                            new DownloadFileAsyncTask(albumThumbImageView, photo, photoAlbum).execute(photo.getUrlToMaxPhoto());
                        } catch (Exception e) {
                            sendError(e.toString());
                        }
                    }

                    @Override
                    public void onError(VKError error) {
                        super.onError(error);
                        sendError(error.toString());
                    }

                    public void sendError(String s) {
                        EventBus.getDefault().postSticky(new ErrorEvent(s));
                    }
                }, photoAlbum);
            } else {
                Picasso.with(albumThumbImageView.getContext()).load(R.drawable.vk_gray_transparent_shape);
            }
        }
    }
}