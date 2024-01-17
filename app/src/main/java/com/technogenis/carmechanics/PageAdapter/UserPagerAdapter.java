package com.technogenis.carmechanics.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.technogenis.carmechanics.UserStart.UserLoginFragment;
import com.technogenis.carmechanics.UserStart.UserRegisterFragment;


public class UserPagerAdapter extends FragmentStateAdapter {

    public UserPagerAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new UserLoginFragment();
                break;

            case 1:
                fragment = new UserRegisterFragment();
                break;

        }

        assert fragment != null;
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
