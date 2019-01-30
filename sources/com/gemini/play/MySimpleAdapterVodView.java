package com.gemini.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;
import cz.msebera.android.httpclient.HttpStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MySimpleAdapterVodView extends SimpleAdapter {
    private Context _this;
    private int clickTemp = -1;
    private int current_index = 0;
    private ArrayList<HashMap<String, Object>> list;
    final Handler pHandler = new C04521();
    private float rate;
    private int selectedPosition = -1;
    private Typeface typeFace;

    /* renamed from: com.gemini.play.MySimpleAdapterVodView$1 */
    class C04521 extends Handler {
        C04521() {
        }

        public void handleMessage(Message msg) {
            ViewHolder viewHolder;
            switch (msg.what) {
                case 0:
                    MyViewHolder my_viewhold = (MyViewHolder) msg.getData().getSerializable("viewholder");
                    viewHolder = my_viewhold.getViewHolder();
                    String url = msg.getData().getString("url");
                    String imageurl = my_viewhold.getImageUrl();
                    if (viewHolder != null && url.equals(imageurl)) {
                        Bitmap b = VODplayer.listbitmapCache.getBitmap(url);
                        if (b != null) {
                            viewHolder.ItemView.setImageBitmap(b);
                            return;
                        } else {
                            viewHolder.ItemView.setImageResource(C0216R.mipmap.vdef);
                            return;
                        }
                    }
                    return;
                case 1:
                    viewHolder = ((MyViewHolder) msg.getData().getSerializable("viewholder")).getViewHolder();
                    if (viewHolder != null) {
                        viewHolder.ItemView.setImageResource(C0216R.mipmap.vdef);
                        return;
                    }
                    return;
                case 2:
                    MySimpleAdapterVodView.this.notifyDataSetChanged();
                    return;
                default:
                    return;
            }
        }
    }

    public class MyViewHolder implements Serializable {
        private static final long serialVersionUID = 123456;
        private String url;
        private ViewHolder viewHolder;

        public void setViewHolder(ViewHolder v) {
            this.viewHolder = v;
        }

        public ViewHolder getViewHolder() {
            return this.viewHolder;
        }

        public String getImageUrl() {
            return this.url;
        }

        public void setImageUrl(String u) {
            this.url = u;
        }
    }

    private class ViewHolder {
        private TextView ItemInfo;
        private TextView ItemTitle;
        private ImageView ItemView;

        private ViewHolder() {
        }
    }

    public MySimpleAdapterVodView(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this._this = context;
        this.list = (ArrayList) data;
        init();
    }

    private void init() {
        this.typeFace = MGplayer.getFontsType(this._this);
        this.rate = MGplayer.getFontsRate();
    }

    public void setSeclection(int position) {
        this.clickTemp = position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder2;
        if (convertView == null) {
            convertView = ((LayoutInflater) this._this.getSystemService("layout_inflater")).inflate(C0216R.layout.voditem, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ItemView = (ImageView) convertView.findViewById(C0216R.id.ItemView);
            viewHolder.ItemInfo = (TextView) convertView.findViewById(C0216R.id.ItemInfo);
            viewHolder.ItemTitle = (TextView) convertView.findViewById(C0216R.id.ItemTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder2 = (ViewHolder) convertView.getTag();
        }
        ViewHolder viewHolder3 = viewHolder2;
        TextView vh = viewHolder3.ItemInfo;
        TextView vt = viewHolder3.ItemTitle;
        ImageView vi = viewHolder3.ItemView;
        vh.setTextSize(this.rate * 6.0f);
        vh.setTypeface(this.typeFace);
        vh.setTextColor(Color.rgb(255, HttpStatus.SC_CREATED, 14));
        if (MGplayer.custom().equals("msiptv")) {
            vh.setVisibility(8);
        }
        vt.setTextSize(this.rate * 8.0f);
        vt.setTypeface(this.typeFace);
        vt.setTextColor(Color.rgb(255, 255, 255));
        if (position == this.clickTemp) {
            vt.setBackgroundColor(Color.rgb(0, 162, 232));
        } else {
            vt.setBackgroundColor(Color.rgb(0, 0, 0));
        }
        LayoutParams layoutParams = (LayoutParams) vi.getLayoutParams();
        layoutParams.height = (int) (((double) MGplayer.screenHeight) / 2.8d);
        vi.setLayoutParams(layoutParams);
        if (this.clickTemp == position) {
            convertView.setBackgroundResource(C0216R.drawable.gradient9);
        } else {
            convertView.setBackgroundResource(C0216R.drawable.gradient3);
        }
        final HashMap<String, Object> map = (HashMap) this.list.get(position);
        int ItemHaveLoadTmp = 0;
        if (((Integer) map.get("ItemHaveLoad")).intValue() == 1) {
            ItemHaveLoadTmp = 1;
        }
        map.remove("ItemHaveLoad");
        map.put("ItemHaveLoad", Integer.valueOf(1));
        this.list.set(position, map);
        final int ItemHaveLoad = ItemHaveLoadTmp;
        viewHolder3.ItemInfo.setText(String.valueOf(map.get("ItemInfo")));
        viewHolder3.ItemTitle.setText((String) map.get("ItemTitle"));
        Bitmap bimage2 = (Bitmap) map.get("ItemImageBit");
        if (bimage2 != null) {
            viewHolder3.ItemView.setImageBitmap(bimage2);
        } else {
            Bitmap b = VODplayer.listbitmapCache.getBitmap((String) map.get("ItemImageUrl"));
            if (b != null) {
                viewHolder3.ItemView.setImageBitmap(b);
            } else {
                viewHolder3.ItemView.setImageResource(C0216R.mipmap.vdef);
                final Bitmap bimage = bimage2;
                MyViewHolder my_viewhold = new MyViewHolder();
                my_viewhold.setViewHolder(viewHolder3);
                String imageurl = (String) map.get("ItemImageUrl");
                if (!(imageurl == null || imageurl.endsWith("null"))) {
                    my_viewhold.setImageUrl(imageurl);
                    MGplayer.MyPrintln("ItemImageUrl:" + ((String) map.get("ItemImageUrl")));
                }
                new Thread() {
                    public void run() {
                        String url = (String) map.get("ItemImageUrl");
                        if (bimage != null) {
                            url = String.valueOf(map.get("ItemId"));
                            VODplayer.listbitmapCache.putBitmap(url, bimage);
                        }
                        if (VODplayer.listbitmapCache.getBitmap(url) == null && ItemHaveLoad == 0 && url != null && !url.endsWith("null")) {
                            if (url.toLowerCase().endsWith("jpg") || url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("gif") || url.toLowerCase().endsWith("bmp")) {
                                Bitmap b1 = MGplayer.getHttpBitmap(url, 10000);
                                if (b1 == null) {
                                    MGplayer.MyPrintln("ItemImageUrl b1 null");
                                    return;
                                }
                                VODplayer.listbitmapCache.putBitmap(url, b1);
                                Message msg = new Message();
                                msg.what = 2;
                                if (MySimpleAdapterVodView.this.pHandler.hasMessages(msg.what)) {
                                    MySimpleAdapterVodView.this.pHandler.removeMessages(msg.what);
                                }
                                MySimpleAdapterVodView.this.pHandler.sendMessageDelayed(msg, 1000);
                            }
                        }
                    }
                }.start();
            }
        }
        return convertView;
    }
}
