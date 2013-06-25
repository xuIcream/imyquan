package com.imyquan.test.view;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import com.imyquan.test.R;
/**
 * @author xubc
 * 
 */
public class ImageItemView extends ImageView {

	private BitmapDrawable image;
	private static final String TAG = "ImageItemView";
	private String imageName;
	
	public static final int MAXMEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
	public static final int CACHESIZE = MAXMEMORY / 6;
	
	public static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(CACHESIZE);
	public static Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	public static List<String> downLoadingIMG = new ArrayList<String>();
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public enum ImageType{
	    UserIv, StatusIv;
	}
	
	public ImageItemView(Context context) {
		super(context);
		init(context);
	}

	public ImageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	void init(Context context) {
		//defaultDrawable = context.getResources().getDrawable(R.drawable.detail_pic_loading);
		//setImageDrawable(defaultDrawable);
	}

	/**
	 * image  to download
	 * @param url
	 */
	public void toDownLoad(String url) {
	    // if is downloading  don't  download again
	    if (downLoadingIMG.contains(url)) {
            return;
        }else{
            downLoadingIMG.add(url);
            new DownLoadTask(ImageItemView.this).execute(url);
        }
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	
	public static Bitmap getBitmap(String url){
		Bitmap bitmap = lruCache.get(url);
		if (bitmap != null) {
			Log.i(TAG, "has image in lrucache"  + url);
			return bitmap;
		}
		return null;
	} 
	public Drawable getDefaultDrawable(){
		return image;
	}
	
	
	class DownLoadTask extends AsyncTask<String, Void, Bitmap> {

	    private static final String TAG = "DownLoadTask";
	    private String mImagename;
	    
	    private ImageItemView imageItemView;
	    
	    public DownLoadTask(ImageItemView image) {
	        this.imageItemView = image;
	    }

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }

	    @Override
	    protected Bitmap doInBackground(String... params) {
	        Log.i(TAG, "doInBackground " +params[0]);
	        Bitmap bitmap;
	        try {
	            URL url = new URL(params[0]);
	            InputStream in = url.openStream();
	            mImagename = params[0];
	            bitmap = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.i(TAG, "DownLoadTask has bug  "+ e.getMessage()+ ""+ e.getStackTrace());
	            return null;
	        }
	        return bitmap;
	    }

	    @Override
	    protected void onPostExecute(Bitmap result) {
	        Log.i(TAG, "onPostExecute "+ imageItemView  + "dImagename" +mImagename);
	        // when has download put it in the lrucache
	        if (TextUtils.isEmpty(mImagename) || result == null) {
	            return;
	        }else{
	            if (imageItemView.getImageName().equals(mImagename)) {
	                imageItemView.setImageBitmap(result);
	            }
	            ImageItemView.lruCache.put(mImagename, result);
	        }
	    }
	}

}

