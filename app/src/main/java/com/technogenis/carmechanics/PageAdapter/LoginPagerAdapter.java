package com.technogenis.carmechanics.PageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.technogenis.carmechanics.Start.SignInFragment;
import com.technogenis.carmechanics.Start.SignUpFragment;


public class LoginPagerAdapter extends FragmentStateAdapter {

    public LoginPagerAdapter(FragmentActivity fragmentActivity)
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
                fragment = new SignInFragment();
                break;

            case 1:
                fragment = new SignUpFragment();
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
