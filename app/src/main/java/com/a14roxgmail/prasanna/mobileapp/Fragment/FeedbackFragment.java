package com.a14roxgmail.prasanna.mobileapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a14roxgmail.prasanna.mobileapp.R;

import java.util.zip.Inflater;

/**
 * Created by Prasanna Deshappriya on 1/17/2017.
 */
public class FeedbackFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback,container,false);
        init(view);
        return view;
    }

    private void init(View view) {

    }
}
