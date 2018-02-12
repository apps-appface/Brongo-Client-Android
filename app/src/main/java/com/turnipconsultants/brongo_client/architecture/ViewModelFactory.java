package com.turnipconsultants.brongo_client.architecture;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by mohit on 19-01-2018.
 */

public class ViewModelFactory implements ViewModelProvider.Factory {
    private BuyLandViewModel buyLandViewModel;

    @Inject
    public ViewModelFactory(BuyLandViewModel buyLandViewModel) {
        this.buyLandViewModel = buyLandViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(BuyLandViewModel.class)) {
            return (T) buyLandViewModel;
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
