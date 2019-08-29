package com.example.gymmyapplication.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.gymmyapplication.R;

public class Exercise_Fragment extends Fragment {
    EditText editRip,editRec,editEserc;
    public String rip,rec,eserc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment, container, false);
        editEserc=v.findViewById(R.id.editTextnome);
        editRec=v.findViewById(R.id.editTextRec);
        editRip=v.findViewById(R.id.editTextRip);
        editEserc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                eserc="";
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eserc=editEserc.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editRec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rec="";
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rec=editRec.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editRip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rip="";
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rip=editRip.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
}
