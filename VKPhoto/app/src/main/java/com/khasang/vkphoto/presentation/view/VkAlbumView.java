package com.khasang.vkphoto.presentation.view;

import com.khasang.vkphoto.presentation.model.Photo;

import java.util.List;

public interface VkAlbumView extends VkView {
    void displayVkPhotos(List<Photo> photos);

    List<Photo> getPhotoList();
}
