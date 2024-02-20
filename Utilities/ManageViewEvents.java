package com.irrelevxnce.jblgroundscare.Utilities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class ManageViewEvents extends AppCompatActivity {
        public static void manageSetOnClickListener(View view, Context context, Class classForIntent) {
                context.startActivity(new Intent(context, classForIntent));
        }

        public static void manageSetOnClickListener(View view, Context context, Class classForIntent, String name, String value) {
                Intent intent = new Intent(context, classForIntent);
                intent.putExtra(name, value);
                context.startActivity(intent);
        }

}
