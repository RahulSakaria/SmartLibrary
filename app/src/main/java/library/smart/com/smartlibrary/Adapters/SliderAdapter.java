package library.smart.com.smartlibrary.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import library.smart.com.smartlibrary.R;

/**
 * Created by HP on 06-12-2017.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.eat_icon,
            R.drawable.sleep_icon,
            R.drawable.code_icon
    };

    public String[] slide_heading = {
            "EAT",
            "SLEEP",
            "CODE"
    };

    public String[] slide_desc = {
            "Lorem ipsum sativum. All the basic terminology is taken care of. This is eating area and you can have anything you want",
            "This is sleeping area and you can sleep as much as you can.Lorem ipsum sativum. All the basic terminology is taken care of.",
            "This is coding area and you can create wonders here. Lorem ipsum sativum. All the basic terminology is taken care of."
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideimage = view.findViewById(R.id.imageView1);
        TextView heading = view.findViewById(R.id.slider_heading);
        TextView description = view.findViewById(R.id.slider_description);

        slideimage.setImageResource(slide_images[position]);
        heading.setText(slide_heading[position]);
        description.setText(slide_desc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
