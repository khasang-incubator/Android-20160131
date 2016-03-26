package com.khasang.vkphoto.presentation.presenter.album;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.khasang.vkphoto.presentation.presenter.Presenter;

public interface AlbumPresenter extends Presenter {
    void getPhotosByAlbumId(int albumId);

    void deleteSelectedPhotos(MultiSelector multiSelector);

    void selectPhoto(MultiSelector multiSelector, final AppCompatActivity activity);

    void checkActionModeFinish(MultiSelector multiSelector);

    void uploadPhotos(MultiSelector multiSelector, final long idPhotoAlbum, final AppCompatActivity activity);

    void hideActionModeItem(MultiSelector multiSelector, MenuItem menuItem);
}
