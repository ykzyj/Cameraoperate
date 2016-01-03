package com.example.cameraoperate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private Button but_open_camera;
	private Button but_open_camera_original;
	private Button but_my_camera;
	private ImageView img_camera_obtain;
	
	private static final int obtain=100;
	private static final int obtain_original=101;
	
	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		filePath=Environment.getExternalStorageDirectory().getPath();
		filePath=filePath+"/"+"temp.png";
		
		initComponent();
		
		initOnClick();
		
	}

	private void initOnClick() {
		but_open_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, obtain);
			}
		});
		but_open_camera_original.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri photoUri=Uri.fromFile(new File(filePath));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(intent, obtain_original);
			}
		});
		but_my_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,MyCameraActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			switch (requestCode) {
			case obtain:
				Bundle bundle=data.getExtras();
				Bitmap bitmap=(Bitmap) bundle.get("data");
				img_camera_obtain.setImageBitmap(bitmap);
				break;
			case obtain_original:
				FileInputStream inStream = null;
				try {
					inStream = new FileInputStream(new File(filePath));
					Bitmap bitmap_in=(Bitmap) BitmapFactory.decodeStream(inStream);
					img_camera_obtain.setImageBitmap(bitmap_in);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				finally
				{
					try {
						inStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				/*File file = new File(filePath); 
				Uri uri = Uri.fromFile(file); 
				img_camera_obtain.setImageURI(uri);*/
				
				break;

			default:
				break;
			}
		}
	}

	private void initComponent() {
		but_open_camera=(Button) findViewById(R.id.but_open_camera);
		but_open_camera_original=(Button) findViewById(R.id.but_open_camera_original);
		but_my_camera=(Button) findViewById(R.id.but_my_camera);
		img_camera_obtain=(ImageView) findViewById(R.id.img_camera_obtain);
	}
}
