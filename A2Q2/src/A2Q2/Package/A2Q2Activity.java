package A2Q2.Package;

import A2Q2.Package.MyCanvas;

import android.app.Activity;
import android.os.Bundle;

public class A2Q2Activity extends Activity {
    /** Called when the activity is first created. */
	MyCanvas canvasView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        canvasView = new MyCanvas(this);
        setContentView(canvasView);
        canvasView.requestFocus();
    }
}