package com.example.yourfood.ui.spesa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;

public class SpesaFragment extends Fragment {

    private SpesaViewModel spesaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        spesaViewModel =
                ViewModelProviders.of(this).get(SpesaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_spesa, container, false);
        final TextView textView = root.findViewById(R.id.text_spesa);
        spesaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}