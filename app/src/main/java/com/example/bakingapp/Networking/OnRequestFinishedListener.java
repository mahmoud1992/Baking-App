package com.example.bakingapp.Networking;

import com.example.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Response;

public interface OnRequestFinishedListener {

    void onFailure(String message);

    void onResponse(Response<List<Recipe>> response);
}
