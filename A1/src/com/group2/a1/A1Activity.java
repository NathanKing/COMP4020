package com.group2.a1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class A1Activity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.startActivity(new Intent(this, Experiment.class));
    }
}