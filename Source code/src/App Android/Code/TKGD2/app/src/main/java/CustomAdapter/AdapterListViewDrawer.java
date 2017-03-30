package CustomAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nghia.tkgd.R;

import java.util.ArrayList;
import java.util.List;

import DT0.MenuDrawer;

/**
 * Created by Nghia on 23/03/2017.
 */

public class AdapterListViewDrawer extends ArrayAdapter<MenuDrawer> {
    Context context;
    int resID;
    List<MenuDrawer> objects;
    public AdapterListViewDrawer(Context context,int resID,List<MenuDrawer> objects)
    {
        super(context,resID,objects);
        this.context= context;
        this.objects= objects;
        this.resID = resID;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, resID, null);
        ImageView hinhanh = (ImageView) view.findViewById(R.id.iconDrawer);
        TextView noidung = (TextView) view.findViewById(R.id.contentDrawer);
        MenuDrawer item = objects.get(position);
        hinhanh.setImageResource(item.getHinhanh());
        noidung.setText(item.getTenmenu());
        return view;
    }
}
