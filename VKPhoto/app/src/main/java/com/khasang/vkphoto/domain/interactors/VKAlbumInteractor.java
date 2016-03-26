package com.khasang.vkphoto.domain.interactors;


import com.bignerdranch.android.multiselector.MultiSelector;
import com.khasang.vkphoto.presentation.model.Photo;

import java.util.List;

/**
 * Created by Anton on 21.02.2016.
 */
public interface VKAlbumInteractor {
    void getPhotosByAlbumId(int albumId);

    void getAllLocalAlbums();

    void deleteSelectedVkPhotos(MultiSelector multiSelector, List<Photo> photoList);
}
