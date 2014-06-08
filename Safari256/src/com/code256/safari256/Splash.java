package com.code256.safari256;
/**
 * DON'T REMOVE THIS
 * Created by CODE+256 on 2/10/14.
 * Mr.sentio henry
 * codeuganda@yahoo.com
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

public class Splash extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		Thread timer = new Thread(){
			
			public void run(){
				try{
					final ViewAnimator image = (ViewAnimator) findViewById(R.id.splash);
					image.setOnClickListener(new OnClickListener() {
						
							@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Animation anim = AnimationUtils.loadAnimation(Splash.this,R.anim.animation_splash );
							image.startAnimation(anim);
                                Intent gotStart = new Intent(Splash.this, Animals.class);
                                startActivity( gotStart);
						}
					});
					
					sleep(4000);
				}catch(InterruptedException e){
					e.printStackTrace();
					Log.e("log_tag","Error in splash!" + e.toString());;       
				}finally{
					Intent gotoStart = new Intent(Splash.this,  Animals.class);
                    startActivity( gotoStart);
				}
			}
		};timer.start();
	}

	

}
