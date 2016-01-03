package com.example.cameraoperate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImgActivity extends Activity {
	
	private ImageView img_camera_obtain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showimg);
		
		initComponent();
		
		Intent intent=getIntent();
		String filePath=intent.getStringExtra("imgpath");
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(new File(filePath));
			Bitmap bitmap_in=(Bitmap) BitmapFactory.decodeStream(inStream);
			Matrix matrix=new Matrix();
			matrix.setRotate(90);
			bitmap_in=Bitmap.createBitmap(bitmap_in, 0, 0, bitmap_in.getWidth(), 
					bitmap_in.getHeight(), matrix, true);
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
		
	}

	private void initComponent() {
		img_camera_obtain=(ImageView) findViewById(R.id.img_camera_obtain);
	}
}
