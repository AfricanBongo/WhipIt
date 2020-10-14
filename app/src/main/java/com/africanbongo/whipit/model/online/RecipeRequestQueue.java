package com.africanbongo.whipit.model.online;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RecipeRequestQueue {
    private static RecipeRequestQueue instance;
    private RequestQueue requestQueue;
    private Context context;

    private RecipeRequestQueue(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RecipeRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRequestQueue(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

        return requestQueue;
    }
}
