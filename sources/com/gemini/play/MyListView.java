package com.gemini.play;

import android.widget.ImageView;

public class MyListView {
    private MyListView1 listview1 = null;
    private MyListView2 listview2 = null;
    private int used_id = 1;

    public MyListView(Object listview, int used) {
        this.used_id = used;
        if (this.used_id == 1) {
            this.listview1 = (MyListView1) listview;
        } else if (this.used_id == 2) {
            this.listview2 = (MyListView2) listview;
        }
    }

    public void setVisibility(int gone) {
        if (this.used_id == 1) {
            this.listview1.setVisibility(gone);
        } else if (this.used_id == 2) {
            this.listview2.setVisibility(gone);
        }
    }

    public void showPlayList(int value) {
        if (this.used_id == 1) {
            this.listview1.showPlayList(value);
        } else if (this.used_id == 2) {
            this.listview2.showPlayList(value);
        }
    }

    public void showPlayList(String currentType, boolean b) {
        if (this.used_id == 1) {
            this.listview1.showPlayList(0, currentType, b);
        } else if (this.used_id == 2) {
            this.listview2.showPlayList(currentType, b);
        }
    }

    public String getCurrentID() {
        if (this.used_id == 1) {
            return this.listview1.getCurrentID();
        }
        if (this.used_id == 2) {
            return this.listview2.getCurrentID();
        }
        return null;
    }

    public void setCurrentID(String num) {
        if (this.used_id == 1) {
            this.listview1.setCurrentID(num);
        } else if (this.used_id == 2) {
            this.listview2.setCurrentID(num);
        }
    }

    public boolean isShown() {
        if (this.used_id == 1) {
            return this.listview1.isShown();
        }
        if (this.used_id == 2) {
            return this.listview2.isShown();
        }
        return false;
    }

    public void hidePlayList() {
        if (this.used_id == 1) {
            this.listview1.hidePlayList();
        } else if (this.used_id == 2) {
            this.listview2.hidePlayList();
        }
    }

    public void showPlayList(String data) {
        if (this.used_id == 1) {
            this.listview1.showPlayList(data);
        } else if (this.used_id == 2) {
            this.listview2.showPlayList(data);
        }
    }

    public void showFindPlayList(String data) {
        if (this.used_id == 1) {
            this.listview1.showPlayList(1, data, false);
        }
    }

    public void listFocus() {
        if (this.used_id == 1) {
            this.listview1.listFocus();
        } else if (this.used_id == 2) {
            this.listview2.listFocus();
        }
    }

    public void showViewNoTimeout() {
        if (this.used_id == 1) {
            this.listview1.showViewNoTimeout();
        } else if (this.used_id == 2) {
            this.listview2.showViewNoTimeout();
        }
    }

    public void showViewTimeout() {
        if (this.used_id == 1) {
            this.listview1.showViewTimeout();
        } else if (this.used_id == 2) {
            this.listview2.showViewTimeout();
        }
    }

    public void setCurrentID(int id) {
        if (this.used_id == 1) {
            this.listview1.setCurrentID(id);
        } else if (this.used_id == 2) {
            this.listview2.setCurrentID(id);
        }
    }

    public void setInterface(ListViewInterface onLivePressed) {
        if (this.used_id == 1) {
            this.listview1.setInterface(onLivePressed);
        } else if (this.used_id == 2) {
            this.listview2.setInterface(onLivePressed);
        }
    }

    public void set_ad_Image(ImageView livelist_ad_image) {
        if (this.used_id == 1) {
            this.listview1.set_ad_Image(livelist_ad_image);
        } else if (this.used_id == 2) {
            this.listview2.set_ad_Image(livelist_ad_image);
        }
    }

    public String currentID() {
        if (this.used_id == 1) {
            MyListView1 myListView1 = this.listview1;
            return MyListView1.currentID;
        } else if (this.used_id != 2) {
            return null;
        } else {
            MyListView2 myListView2 = this.listview2;
            return MyListView2.currentID;
        }
    }

    public void set_currentID(String value) {
        if (this.used_id == 1) {
            MyListView1 myListView1 = this.listview1;
            MyListView1.currentID = value;
        } else if (this.used_id == 2) {
            MyListView2 myListView2 = this.listview2;
            MyListView2.currentID = value;
        }
    }
}
