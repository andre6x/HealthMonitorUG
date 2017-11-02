package com.grupocisc.healthmonitor.Settings.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.grupocisc.healthmonitor.R;
import com.grupocisc.healthmonitor.Utils.Utils;

public class TutorialActivityV2 extends AppCompatActivity {
public static final String TAG="TutorialActivityV2";

	private ViewPager vp;
	private LinearLayout linear_indicator;
	private vpAdapter miAdapter;
	private String[] imagenes;
	private String[] textos;
	private String[] titles_textos;
	private final int id_generator = 1666;
	private TextView footer , title_view;
	private String title ="TUTORIAL";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v2j_tutorial_main_layout);
		Utils.SetStyleToolbarTitle2(this, title);
		linear_indicator = (LinearLayout) findViewById(R.id.linear_indicator);

		footer = (TextView) findViewById(R.id.tv_view_pager);
		title_view = (TextView) findViewById(R.id.title_view);

		imagenes = getResources().getStringArray(R.array.images_tutorial_v2);
		textos = getResources().getStringArray(R.array.footer_tutorial_v2);
		titles_textos = getResources().getStringArray(R.array.titles_tutorial_v2);
		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setOffscreenPageLimit(imagenes.length);
        miAdapter = new vpAdapter();
        vp.setAdapter(miAdapter);
        vp.setCurrentItem(0);
		title_view.setText(titles_textos[0]);
		footer.setText(textos[0]);

		final float scale = getResources().getDisplayMetrics().density;
		int p = (int) (3 * scale + 0.5f);
		int size_ = (int) (10 * scale + 0.5f);
        for(int x = 0 ; x < imagenes.length ; x++ )
        {
        	View circle = new View(this);
        	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( size_ ,size_ );
        	params.setMargins(p, p, p, p);
        	circle.setLayoutParams(params);
        	if(x == 0)
        		circle.setBackgroundResource(R.drawable.cicler_indicator_select);
        	else
        		circle.setBackgroundResource(R.drawable.cicler_indicator_not_select);
        	circle.setId(id_generator+x);
        	linear_indicator.addView(circle);
        }
        
        vp.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				for(int y = 0 ; y < imagenes.length ; y ++ )
				{
					if(y == position)
						findViewById(id_generator+y).setBackgroundResource(R.drawable.cicler_indicator_select);
		        	else
		        		findViewById(id_generator+y).setBackgroundResource(R.drawable.cicler_indicator_not_select);
				}
				title_view.setText(titles_textos[position]);
				footer.setText(textos[position]);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	//se ejecuta al seleccionar el icon back del toolbar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			callFinish();
		return super.onOptionsItemSelected(item);
	}

	//se ejecuta al presionar boton back del movil
	@Override
	public void onBackPressed() {
		callFinish();
	}

	public void callFinish() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	/**********************************************VIEW PAGER***********************************************************************/
	private class vpAdapter extends PagerAdapter {
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imagenes.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == ((LinearLayout) object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			TextView footer;
			ImageView imagen;
	        
			LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.v2j_view_pager_layout, container,false);
			String mDrawableName;
            mDrawableName = imagenes[position];
            int resID = container.getContext().getResources().getIdentifier(mDrawableName, "mipmap",container.getContext().getPackageName());
			imagen = (ImageView) itemView.findViewById(R.id.img_view_pager);
			imagen.setImageDrawable(getResources().getDrawable(resID));
			((ViewPager) container).addView(itemView);
			//Toast.makeText(getBaseContext(), "View Pager: "+position, Toast.LENGTH_SHORT).show();
	        return itemView;
		}
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
		}
    }
	/***************************************************************************************************************************/
	


}