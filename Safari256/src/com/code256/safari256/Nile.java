

package com.code256.safari256;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

/**
 * DON'T REMOVE THIS
 * Created by CODE+256 on 2/9/14.
 * Mr.sentio henry
 * codeuganda@yahoo.com
 */
public class Nile extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nile);



    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.text1:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, Animals.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}	