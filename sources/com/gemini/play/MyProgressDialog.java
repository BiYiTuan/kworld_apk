package com.gemini.play;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gemini.kvod2.C0216R;

public class MyProgressDialog extends Dialog {

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
        private ProgressBar update_progressBar;

        /* renamed from: com.gemini.play.MyProgressDialog$Builder$1 */
        class C04471 implements OnFocusChangeListener {
            C04471() {
            }

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    v.setBackgroundResource(C0216R.mipmap.bof);
                }
            }
        }

        /* renamed from: com.gemini.play.MyProgressDialog$Builder$2 */
        class C04482 implements OnFocusChangeListener {
            C04482() {
            }

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(C0216R.mipmap.bf);
                } else {
                    v.setBackgroundResource(C0216R.mipmap.bof);
                }
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

        public MyProgressDialog create() {
            Typeface typeFace = MGplayer.getFontsType(this.context);
            float rate = MGplayer.getFontsRate();
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            final MyProgressDialog dialog = new MyProgressDialog(this.context, C0216R.style.Dialog);
            View layout = inflater.inflate(C0216R.layout.progressdialog, null);
            dialog.addContentView(layout, new LayoutParams(-1, -2));
            this.okButton = (Button) layout.findViewById(C0216R.id.positiveButton);
            this.okButton.setOnFocusChangeListener(new C04471());
            this.cancelButton = (Button) layout.findViewById(C0216R.id.negativeButton);
            this.cancelButton.setOnFocusChangeListener(new C04482());
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
            this.update_progressBar = (ProgressBar) layout.findViewById(C0216R.id.update_progressBar);
            dialog.setContentView(layout);
            this.okButton.setTextSize(7.0f * rate);
            this.okButton.setTypeface(typeFace);
            this.cancelButton.setTextSize(7.0f * rate);
            this.cancelButton.setTypeface(typeFace);
            this.messageText.setTextSize(8.0f * rate);
            this.messageText.setTypeface(typeFace);
            return dialog;
        }

        public void setProgress(int i) {
            if (this.update_progressBar != null) {
                this.update_progressBar.setProgress(i);
            }
        }
    }

    public MyProgressDialog(Context context) {
        super(context);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }
}
