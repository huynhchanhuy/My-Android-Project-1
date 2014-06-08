package com.example.doan_didong;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class ChooseAudioInSdcard extends Activity {

	String giaovien;
	String iduser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_file_in_sdcard);
        
        Bundle b = getIntent().getExtras();
		giaovien=b.getString("giaovien");
		iduser=b.getString("iduser");
        
        ListView lv;
        ArrayList<String> FilesInFolder = GetFiles("/sdcard/doan_didong");
        lv = (ListView)findViewById(R.id.listview);

        lv.setAdapter(new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, FilesInFolder));

        lv.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String link=parent.getItemAtPosition(position).toString();
				//Toast.makeText(getBaseContext(),link, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), UploadAAudio.class);
				i.putExtra("giaovien", giaovien);
				i.putExtra("iduser", iduser);
				i.putExtra("link", link);
				startActivity(i);
			}
		});
        
    }
    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++) 
            	if(files[i].getName().indexOf(".mp3")!=-1)
                	MyFiles.add(files[i].getName());
        }

        return MyFiles;
    }

   
}

