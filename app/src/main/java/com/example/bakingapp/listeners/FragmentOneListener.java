package com.example.bakingapp.listeners;

import com.example.bakingapp.models.Step;

import java.util.ArrayList;

public interface FragmentOneListener {
    void setStep(int index , ArrayList<Step> steps);


    void setCurrent(int index);
}
