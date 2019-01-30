package com.gemini.play;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyDialog2 extends Dialog {

    public static class Builder {
        private Button cancelButton;
        private View contentView;
        private Context context;
        private String message;
        private TextView messageText;
        private OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private Button okButton;
        private OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private String title;

        /* renamed from: com.gemini.play.MyDialog2$Builder$1 */
        class C03751 implements OnFocusChangeListener {
            C03751() {
            }

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    v.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        }

        /* renamed from: com.gemini.play.MyDialog2$Builder$2 */
        class C03762 implements OnFocusChangeListener {
            C03762() {
            }

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    v.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        }

        /* renamed from: com.gemini.play.MyDialog2$Builder$5 */
        class C03795 implements OnKeyListener {
            C03795() {
            }

            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD");
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            MGplayer.MyPrintln("KeyEvent KEYCODE_DPAD_LEFT");
                            Builder.this.okButton.setFocusable(true);
                            Builder.this.okButton.setFocusableInTouchMode(true);
                            Builder.this.okButton.requestFocus();
                            Builder.this.okButton.requestFocusFromTouch();
                            break;
                    }
                }
                return false;
            }
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) this.context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) this.context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public MyDialog2 create() {
            Typeface typeFace = MGplayer.getFontsType(this.context);
            float rate = MGplayer.getFontsRate();
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            final MyDialog2 dialog = new MyDialog2(this.context, C0216R.style.Dialog);
            View layout = inflater.inflate(C0216R.layout.dialog2, null);
            dialog.addContentView(layout, new LayoutParams(-1, -2));
            this.okButton = (Button) layout.findViewById(C0216R.id.positiveButton);
            this.okButton.setOnFocusChangeListener(new C03751());
            this.cancelButton = (Button) layout.findViewById(C0216R.id.negativeButton);
            this.cancelButton.setOnFocusChangeListener(new C03762());
            if (this.positiveButtonText != null) {
                this.okButton.setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    this.okButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    });
                }
            } else {
                this.okButton.setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                this.cancelButton.setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    this.cancelButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                    });
                }
            } else {
                this.cancelButton.setVisibility(8);
            }
            this.messageText = (TextView) layout.findViewById(C0216R.id.message);
            if (this.message != null) {
                this.messageText.setText(this.message);
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(C0216R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(C0216R.id.content)).addView(this.contentView, new LayoutParams(-1, -1));
            }
            dialog.setContentView(layout);
            this.okButton.setTextSize(7.0f * rate);
            this.okButton.setTypeface(typeFace);
            this.cancelButton.setTextSize(7.0f * rate);
            this.cancelButton.setTypeface(typeFace);
            this.messageText.setTextSize(7.0f * rate);
            this.messageText.setTypeface(typeFace);
            dialog.setOnKeyListener(new C03795());
            return dialog;
        }
    }

    public MyDialog2(Context context) {
        super(context);
    }

    public MyDialog2(Context context, int theme) {
        super(context, theme);
    }
}
