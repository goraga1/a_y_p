package com.radioaypfm.aypfm.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.radioaypfm.aypfm.R;
import com.radioaypfm.aypfm.model.Video;
import com.radioaypfm.aypfm.util.Constants;
import com.radioaypfm.aypfm.util.Utilities;

@SuppressLint("DefaultLocale")
public class VideoAdapter extends BaseAdapter {
  Context c;
  private final LayoutInflater mLayoutInflater;
  ArrayList<Video> videos;
  Typeface md;
  Typeface roman;

  public VideoAdapter(Context context, ArrayList<Video> allVideos) {
    this.c = context;
    this.mLayoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.videos = allVideos;
    this.md = Typeface.createFromAsset(c.getAssets(), Constants.FONT_HELVETICA_MD);
    this.roman = Typeface.createFromAsset(c.getAssets(), Constants.FONT_HELVETICA_ROMAN);
  }

  @Override
  public View getView(int position, View view, ViewGroup viewGroup) {
    ViewHolder holder;

    if (view == null) {
      holder = new ViewHolder();
      view = mLayoutInflater.inflate(R.layout.list_item_videos, viewGroup, false);
      holder.image = (ImageView) view.findViewById(R.id.videoImage);
      holder.title = (TextView) view.findViewById(R.id.videoTitle);
      holder.desc = (TextView) view.findViewById(R.id.videoDesc);
      holder.date = (TextView) view.findViewById(R.id.videoDate);
      Utilities.setListviewItemTypeface(md, holder.title, c);
      Utilities.setListviewItemTypeface(roman, holder.desc, c);
      Utilities.setListviewItemTypeface(roman, holder.date, c);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    Video v = videos.get(position);
    AQuery aq = new AQuery(view);
    aq.id(holder.image).image(v.getThumbnail(), true, true, 0, 0, null, AQuery.FADE_IN, 1.0f);
    aq.id(holder.title).text(v.getTitle());
    aq.id(holder.desc).text(v.getDesc());
    aq.id(holder.date).text("Date : " + v.getDate().substring(0, 10));

    return view;

  }

  private static class ViewHolder {
    protected ImageView image;
    protected TextView title;
    protected TextView desc;
    protected TextView date;

  }

  @Override
  public int getCount() {
    return videos.size();
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

}
