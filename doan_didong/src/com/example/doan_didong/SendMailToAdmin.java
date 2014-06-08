package com.example.doan_didong;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendMailToAdmin extends Activity
{
	EditText subject;
	EditText message;
	Button sendmail_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmailtoadmin);
		subject = (EditText)findViewById(R.id.sendmail_subject);
		message = (EditText)findViewById(R.id.sendmail_message);
		sendmail_btn = (Button)findViewById(R.id.sendmail_btn);
		sendmail_btn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if("".equals(subject.getText().toString()) || "".equals(message.getText().toString()))
				{
					Toast.makeText(getApplicationContext(), "Nhập vào tiêu đề và nội dung tin nhắn", Toast.LENGTH_SHORT).show();
				}
				else
				{
					try {
					   	Uri URI = null;
				            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				            emailIntent.setType("message/rfc822") ;
	//			            emailIntent.setType("plain/text");
				            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "tranvokhoinguyen@gmail.com" });
				            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject.getText().toString());
				            if (URI != null) 
				            {
				                   emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
				            }
			            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,message.getText().toString());
			            startActivity(Intent.createChooser(emailIntent,"Send email..."));

				      } catch (Throwable t) 
				      {
				            Toast.makeText(getApplicationContext(),
				                          "Không gửi được email " + t.toString(), Toast.LENGTH_SHORT).show();
				      }
				}
			}
		});
	}
}
