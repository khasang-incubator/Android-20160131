package com.khasang.vkphoto.data.vk;

import com.khasang.vkphoto.data.RequestMaker;
import com.khasang.vkphoto.domain.events.ErrorEvent;
import com.khasang.vkphoto.domain.events.GetVKPhotosEvent;
import com.khasang.vkphoto.presentation.model.Photo;
import com.khasang.vkphoto.presentation.model.PhotoAlbum;
import com.khasang.vkphoto.util.JsonUtils;
import com.khasang.vkphoto.util.Logger;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class VKPhotoSource {

    public void savePhotoToAlbum(File file, PhotoAlbum photoAlbum) {
        if (file.exists()) {
            RequestMaker.uploadPhoto(file, photoAlbum, new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    Logger.d(response.responseString);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                }
            });
        }
    }

    public void savePhotos() {

    }

    public void updatePhoto() {

    }

    public void deletePhoto() {

    }

    public void deletePhotos() {

    }

    public void getPhotoById() {

    }


    public void getPhotosByAlbumId(int albumId) {
        RequestMaker.getVkPhotosByAlbumId(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final List<Photo> photoList;
                try {
                    photoList = JsonUtils.getItems(response.json, Photo.class);
                    Logger.d("Got VKPhoto successfully");
                    EventBus.getDefault().postSticky(new GetVKPhotosEvent(photoList));
                } catch (Exception e) {
                    sendError(e.toString());
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                sendError(error.toString());
            }

        }, albumId);
    }


    public void getAllPhotos() {

    }

    void sendError(String s) {
        EventBus.getDefault().postSticky(new ErrorEvent(s));
    }
}