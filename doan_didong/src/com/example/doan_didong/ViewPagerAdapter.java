package com.example.doan_didong;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 10;

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {

			// Open FragmentTab1.java
		case 0:
			Tab1_Home tab1 = new Tab1_Home();
			return tab1;
		case 1:
			Tab2_ThongTinCaNhan tab2 = new Tab2_ThongTinCaNhan();
			return tab2;
		case 2:
			Tab3_TuDien tab3 = new Tab3_TuDien();
			return tab3;
		case 3:
			Tab4_ThanhNgu tab4 = new Tab4_ThanhNgu();
			return tab4;
		case 4:
			Tab5_NguPhap tab5 = new Tab5_NguPhap();
			return tab5;
		case 5:
			Tab6_Audio tab6 = new Tab6_Audio();
			return tab6;
		case 6:
			Tab7_Video tab7 = new Tab7_Video();
			return tab7;
		case 7:
			Tab8_ThiTracNghiem a = new Tab8_ThiTracNghiem();
			return a;
		case 8:
			Tab9_YeuThich b = new Tab9_YeuThich();
			return b;
		case 9:
			Tab10_KetQua c = new Tab10_KetQua();
			return c;
		/*default:
			Tab1_home tab = new Tab1_home();
			return tab;*/
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}
