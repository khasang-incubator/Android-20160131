package com.khasang.vkphoto.data.vk;

import android.content.Context;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.khasang.vkphoto.data.RequestMaker;
import com.khasang.vkphoto.data.local.LocalAlbumSource;
import com.khasang.vkphoto.domain.events.GetVKPhotoEvent;
import com.khasang.vkphoto.domain.events.GetVKPhotosEvent;
import com.khasang.vkphoto.presentation.model.MyVkRequestListener;
import com.khasang.vkphoto.presentation.model.Photo;
import com.khasang.vkphoto.util.ErrorUtils;
import com.khasang.vkphoto.util.JsonUtils;
import com.khasang.vkphoto.util.Logger;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class VKPhotoSource {

    /**
     * Добавляет фото на сервер ВК
     *
     * @param file
     * @param idPhotoAlbum
     * @param localAlbumSource
     */
    public void savePhotoToAlbum(final File file, final long idPhotoAlbum, final LocalAlbumSource localAlbumSource) {
        if (file.exists()) {
            RequestMaker.uploadPhoto(file, idPhotoAlbum, new MyVkRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    Logger.d("savePhotoToAlbum: " + response.responseString);
                }
            });
        }
    }

    /**
     * Добавляет список фотографий на сервер ВК и в альбом на устройсте

     */
    public void savePhotos(final MultiSelector multiSelector, final List<Photo> photoList, final long idPhotoAlbum, final Context context) {
//        multiSelector.clearSelections();
//        multiSelector.getSelectedPositions().clear();
//        getPhotosByAlbumId(idPhotoAlbum);
    }

    public void updatePhoto() {

    }

    public void deletePhoto(int photoId) {
        RequestMaker.deleteVkPhotoById(new MyVkRequestListener() {
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Logger.d("Delete VKPhoto successfully");
            }
        }, photoId);
    }

    public void deletePhotos() {

    }

    public void getPhotoById(int photoId) {
        RequestMaker.getPhotoById(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    List<Photo> photos = JsonUtils.getPhotos(response.json, Photo.class);
                    Logger.d(String.valueOf(photos.size()));
                    EventBus.getDefault().post(new GetVKPhotoEvent(photos.get(0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                System.out.println(error.errorMessage);
            }
        },photoId);

    }


    public void getPhotosByAlbumId(int albumId) {
        RequestMaker.getVkPhotosByAlbumId(new MyVkRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final List<Photo> photoList;
                try {
                    photoList = JsonUtils.getItems(response.json, Photo.class);
                    Logger.d("Got VKPhoto successfully");
                    EventBus.getDefault().postSticky(new GetVKPhotosEvent(photoList));
                } catch (Exception e) {
                    sendError(ErrorUtils.JSON_PARSE_FAILED);
                }
            }
        }, albumId);
    }
}
