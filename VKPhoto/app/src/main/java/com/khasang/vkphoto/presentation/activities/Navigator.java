package com.khasang.vkphoto.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.khasang.vkphoto.R;
import com.khasang.vkphoto.presentation.fragments.AlbumFragment;
import com.khasang.vkphoto.presentation.fragments.LocalAlbumFragment;
import com.khasang.vkphoto.presentation.fragments.PhotoViewPagerFragment;
import com.khasang.vkphoto.presentation.fragments.VKCommentsFragment;
import com.khasang.vkphoto.presentation.model.Photo;
import com.khasang.vkphoto.presentation.model.PhotoAlbum;
import com.khasang.vkphoto.util.Constants;

import java.util.List;

public class Navigator {

    private static int mode = Constants.START;
    private static int tabPosition = 0;
    private static String tabTag = "";

    private static FragmentManager getFragmentManager(Context context) {
        return ((FragmentActivity) context).getSupportFragmentManager();
    }

    private static void startActivity(Intent intent, Context context) {
        context.startActivity(intent);
    }

    private static boolean isFragmentAvailable(Fragment fragment) {
        return fragment != null && fragment.isAdded();
    }

    public static void navigateToVKAlbumFragment(Context context, PhotoAlbum photoAlbum) {
        navigateToFragmentWithBackStack(context, AlbumFragment.newInstance(photoAlbum), AlbumFragment.TAG);
    }

    public static void navigateToPhotoViewPagerFragment(Context context, List<Photo> photoList, int position) {
        getFragmentManager(context).beginTransaction().replace(R.id.fragment_container, PhotoViewPagerFragment.newInstance(photoList, position), PhotoViewPagerFragment.TAG).addToBackStack(PhotoViewPagerFragment.TAG).commit();
    }

    public static void navigateToVKCommentsFragment(Context context, Photo photo) {
        getFragmentManager(context).beginTransaction().replace(R.id.fragment_container, VKCommentsFragment.newInstance(photo), VKCommentsFragment.TAG).addToBackStack(VKCommentsFragment.TAG).commit();
    }

    public static void navigateToLocalAlbumFragment(Context context, PhotoAlbum photoAlbum) {
        navigateToFragmentWithBackStack(context, LocalAlbumFragment.newInstance(photoAlbum), photoAlbum.title);
    }

    public static void navigateToLocalAlbumFragmentWithReplace(Context context, PhotoAlbum selectedLocalPhotoAlbum, long idVKPhotoAlbum) {
        navigateToFragmentWithBackStackWithReplace(context, LocalAlbumFragment.newInstance(selectedLocalPhotoAlbum, idVKPhotoAlbum), LocalAlbumFragment.TAG);
    }

    private static void navigateToFragment(Context context, Fragment fragment, String tag) {
        getFragmentManager(context).beginTransaction().add(R.id.fragment_container, fragment, tag).commit();

    }

    private static void navigateToFragmentWithBackStack(Context context, Fragment fragment, String title) {
        AppCompatActivity activity = (AppCompatActivity) context;
        changeActionBarTitle(activity, title);
        changeViewPagerVisibility(activity, false);
        getFragmentManager(context).beginTransaction().add(R.id.fragment_container, fragment, title).addToBackStack(title).commit();
    }

    private static void changeActionBarTitle(AppCompatActivity activity, String title) {
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    private static void navigateToFragmentWithBackStackWithReplace(Context context, Fragment fragment, String tag) {
        changeViewPagerVisibility(((Activity) context), false);
        getFragmentManager(context).beginTransaction().replace(R.id.fragment_container, fragment, tag).addToBackStack(tag).commit();
    }

    public static void navigateBack(Context context) {
        FragmentManager fragmentManager = getFragmentManager(context);
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Fragment fragment;
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            fragmentManager.popBackStack();
            if (backStackEntryCount == 1) {
                changeViewPagerVisibility((Activity) context, true);
                fragment = fragmentManager.findFragmentByTag(tabTag);
                ActionBar supportActionBar = ((AppCompatActivity) context).getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.setDisplayHomeAsUpEnabled(false);
                }
            } else {
                FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
                String str = backEntry.getName();
                fragment = fragmentManager.findFragmentByTag(str);
            }
            fragment.onResume();
        } else {
            ((Activity) context).finish();
        }
    }

    public static void changeViewPagerVisibility(Activity activity, boolean visibility) {
        View tabLayout = activity.findViewById(R.id.tabs);
        View fragmentContainer = activity.findViewById(R.id.fragment_container);
        View viewPager = activity.findViewById(R.id.viewpager);
        if (visibility) {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
        } else {
            if (viewPager.getVisibility() == View.VISIBLE) {
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                fragmentContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    public static String getTabTag() {
        return tabTag;
    }

    public static void setTabTag(String tabTag) {
        Navigator.tabTag = tabTag;
    }

    public static void initToolbar(Toolbar toolbar) {

    }
}
