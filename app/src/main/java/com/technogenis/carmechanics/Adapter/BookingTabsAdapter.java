package com.technogenis.carmechanics.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.technogenis.carmechanics.AdminPanal.fragment.CancelBookingFrag;
import com.technogenis.carmechanics.AdminPanal.fragment.PendingBookingFrag;


public class BookingTabsAdapter extends FragmentStateAdapter
{
    public BookingTabsAdapter(@NonNull FragmentActivity fragmentActivity)
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
                fragment = new PendingBookingFrag();
                break;

            case 1:
                fragment = new CancelBookingFrag();
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
