package com.technogenis.carmechanics.UserAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.technogenis.carmechanics.UserPanal.CancelFragment;
import com.technogenis.carmechanics.UserPanal.PendingFragment;


public class BookPagerAdapter extends FragmentStateAdapter
{
    public BookPagerAdapter(@NonNull FragmentActivity fragmentActivity)
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
                fragment = new PendingFragment();
                break;

            case 1:
                fragment = new CancelFragment();
                break;


        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
