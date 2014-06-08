package com.code256.safari256;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * DON'T REMOVE THIS
 * Created by CODE+256 on 2/9/14.
 * Mr.sentio henry
 * codeuganda@yahoo.com
 */
public class CameraActivity extends Activity {

    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
iv= (ImageView)findViewById(R.id.imageButton);
Button camera =(Button)findViewById(R.id.button);

 camera.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
startActivityForResult(intent,0);



     }
 });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bm=(  Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(bm);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.text1:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, LayoutChangesActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}