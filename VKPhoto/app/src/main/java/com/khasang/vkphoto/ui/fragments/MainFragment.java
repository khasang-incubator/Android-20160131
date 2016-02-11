package com.khasang.vkphoto.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasang.vkphoto.R;
import com.khasang.vkphoto.domain.adapters.PhotoAlbumsAdapter;
import com.khasang.vkphoto.domain.interfaces.SyncServiceProvider;
import com.khasang.vkphoto.model.album.PhotoAlbum;
import com.khasang.vkphoto.ui.activities.Navigator;
import com.khasang.vkphoto.ui.presenter.MainPresenter;
import com.khasang.vkphoto.ui.presenter.MainPresenterImpl;
import com.khasang.vkphoto.ui.view.MainView;
import com.khasang.vkphoto.util.Logger;
import com.khasang.vkphoto.util.ToastUtils;
import com.vk.sdk.api.model.VKApiPhotoAlbum;

import java.util.List;

public class MainFragment extends Fragment implements MainView {
    public static final String TAG = MainFragment.class.getSimpleName();
    private MainPresenter mainPresdiscrepanciesenter;
    private RecyclerView albumsRecyclerView;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mainPresdiscrepanciesenter = new MainPresenterImpl(this, ((SyncServiceProvider) getActivity()), new Navigator(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView tv_count_of_albums = (TextView) view.findViewById(R.id.tv_count_of_albums);
        albumsRecyclerView = (RecyclerView) view.findViewById(R.id.albums_recycler_view);
        albumsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.findViewById(R.id.start_sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_count_of_albums.getVisibility() == View.INVISIBLE){
                    tv_count_of_albums.setVisibility(View.VISIBLE);
                }
                mainPresdiscrepanciesenter.getAllAlbums();
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void displayVkAlbums(List<VKApiPhotoAlbum> photoAlbumList) {
        for (VKApiPhotoAlbum photoAlbum : photoAlbumList) {
            Logger.d("id " + photoAlbum.id + "\ntitle " + photoAlbum.title + "\ndescription" + photoAlbum.description + "\nPhoto count " + photoAlbum.size + "\nThumb id " + photoAlbum.thumb_id);
        }
        PhotoAlbumsAdapter adapter = new PhotoAlbumsAdapter(photoAlbumList);
        albumsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void displayGalleryAlbums() {

    }

    @Override
    public void showConnectionError() {
        ToastUtils.showError("Error", getContext());
    }

    @Override
    public void showSyncServiceError() {
        ToastUtils.showError("SyncService connecting", getContext());
    }
}
