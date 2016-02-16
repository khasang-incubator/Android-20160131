package com.khasang.vkphoto.model.data.vk;

import com.khasang.vkphoto.domain.RequestMaker;
import com.khasang.vkphoto.model.PhotoAlbum;
import com.khasang.vkphoto.model.events.ErrorEvent;
import com.khasang.vkphoto.model.events.GetVKAlbumsEvent;
import com.khasang.vkphoto.model.events.GetVkSaveAlbumEvent;
import com.khasang.vkphoto.util.JsonUtils;
import com.khasang.vkphoto.util.Logger;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class VKAlbumSource {

    public void saveAlbum() {
        RequestMaker.createEmptyAlbum(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete (VKResponse response) {
                super.onComplete(response);
                final PhotoAlbum photoAlbum;
                try {
                    photoAlbum = JsonUtils.getJsonTest(response.json, PhotoAlbum.class);
                    Logger.d("Got VKAlbums successfully");
                    EventBus.getDefault().postSticky(new GetVkSaveAlbumEvent(photoAlbum));
                } catch (Exception e) {
                    sendError(e.toString());
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                sendError(error.toString());
            }

            void sendError(String s) {
                EventBus.getDefault().postSticky(new ErrorEvent(s));
            }
        }, "Test 16.02", "test album", "all", "all");
    }

    public void updateAlbum() {

    }

    public void deleteAlbum() {

    }

    public void deleteAlbums() {

    }

    public void getAlbumById(int albumId) {
        RequestMaker.getVkAlbum(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final PhotoAlbum photoAlbum;
//                try {
//                    photoAlbumList = JsonUtils(response.json, PhotoAlbum.class);
//                    Logger.d("Got VKAlbums successfully");
//                    EventBus.getDefault().postSticky(new GetVKAlbumsEvent(photoAlbumList));
//                } catch (Exception e) {
//                    sendError(e.toString());
//                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                sendError(error.toString());
            }

            void sendError(String s) {
                EventBus.getDefault().postSticky(new ErrorEvent(s));
            }
        }, albumId);
    }

    public void getAllAlbums() {
        RequestMaker.getAllVkAlbums(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                final List<PhotoAlbum> photoAlbumList;
                try {
                    photoAlbumList = JsonUtils.getItems(response.json, PhotoAlbum.class);
                    Logger.d("Got VKAlbums successfully");
                    EventBus.getDefault().postSticky(new GetVKAlbumsEvent(photoAlbumList));
                } catch (Exception e) {
                    sendError(e.toString());
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                sendError(error.toString());
            }

            void sendError(String s) {
                EventBus.getDefault().postSticky(new ErrorEvent(s));
            }
        });
    }
}
