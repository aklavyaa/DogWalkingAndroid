package com.example.dogwalkerandroid;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabsPagerAdapter extends FragmentStateAdapter {
  @StringRes
  private static final int[] TAB_TITLES =
      new int[] { R.string.dogowner, R.string.dogwalker};

  public TabsPagerAdapter(FragmentActivity context) {
    super(context);

  }

//  @Nullable
//  @Override
//  public CharSequence getPageTitle(int position) {
//    return mContext.getResources().getString(TAB_TITLES[position]);
//  }


  @NonNull
  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return DogOwner.newInstance();
      case 1:
        return DogWalker.newInstance();

      default:
        return null;
    }
  }

  @Override
  public int getItemCount() {
    return 2;
  }
}