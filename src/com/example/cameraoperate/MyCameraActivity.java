package com.example.cameraoperate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyCameraActivity extends Activity implements SurfaceHolder.Callback {
	
	private Button but_obtain_img;
	private SurfaceView mSurfaceView;
	
	private SurfaceHolder mSurfaceHolder;
	
	private Camera mCamera;
	
	private String filePath;
	
	private Camera.PictureCallback mPictureCallback=new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			filePath=Environment.getExternalStorageDirectory().getPath();
			filePath=filePath+"/"+"temp.png";
			File file=new File(filePath);
			
			FileOutputStream fo=null;
			try {
				fo=new FileOutputStream(file);
				fo.write(data);
				
				Intent intent=new Intent(MyCameraActivity.this,ImgActivity.class);
				intent.putExtra("imgpath", filePath);
				startActivity(intent);
				MyCameraActivity.this.finish();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally
			{
				try {
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		
		initComponent();
		
		initOnClick();
		
		mSurfaceHolder=mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		
		mSurfaceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCamera.autoFocus(null);
			}
		});
	}

	private void initOnClick() {
		but_obtain_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Camera.Parameters parameters=mCamera.getParameters();
				parameters.setPictureFormat(ImageFormat.JPEG);
				parameters.setPreviewSize(800, 400);
				parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
				mCamera.autoFocus(new AutoFocusCallback() {
					
					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						if(success)
						{
							mCamera.takePicture(null, null, mPictureCallback);
						}
					}
				});
			}
		});
	}
	
	/*
	 * 获取camera
	 */
	private Camera getCamera()
	{
		Camera camera;
		try {
			camera=Camera.open();
		} catch (Exception e) {
			return null;
			//e.printStackTrace();
		}
		return camera;
	}
	
	/*
	 * 开始预览相机内容
	 */
	private void setStartPreview(Camera camera,SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			camera.setDisplayOrientation(90);
			camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 释放相机资源
	 */
	private void releaseCamera() {
		if(mCamera!=null)
		{
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera=null;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mCamera==null)
		{
			mCamera=getCamera();
			if(mSurfaceHolder!=null)
			{
				setStartPreview(mCamera,mSurfaceHolder);
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCamera();
	}
	
	private void initComponent() {
		but_obtain_img=(Button) findViewById(R.id.but_obtain_img);
		mSurfaceView=(SurfaceView) findViewById(R.id.sf_camera_original);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mCamera.stopPreview();
		setStartPreview(mCamera,mSurfaceHolder);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setStartPreview(mCamera,mSurfaceHolder);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		releaseCamera();
	}
}
