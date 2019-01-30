package org.videolan.libvlc;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;

public abstract class Dialog {
    public static final int TYPE_ERROR = 0;
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_PROGRESS = 3;
    public static final int TYPE_QUESTION = 2;
    private static Callbacks sCallbacks = null;
    private static Handler sHandler = null;
    private Object mContext;
    protected String mText;
    private final String mTitle;
    protected final int mType;

    public interface Callbacks {
        @MainThread
        void onCanceled(Dialog dialog);

        @MainThread
        void onDisplay(ErrorMessage errorMessage);

        @MainThread
        void onDisplay(LoginDialog loginDialog);

        @MainThread
        void onDisplay(ProgressDialog progressDialog);

        @MainThread
        void onDisplay(QuestionDialog questionDialog);

        @MainThread
        void onProgressUpdate(ProgressDialog progressDialog);
    }

    public static class ErrorMessage extends Dialog {
        private ErrorMessage(String title, String text) {
            super(0, title, text);
        }
    }

    protected static abstract class IdDialog extends Dialog {
        protected long mId;

        private native void nativeDismiss(long j);

        protected IdDialog(long id, int type, String title, String text) {
            super(type, title, text);
            this.mId = id;
        }

        @MainThread
        public void dismiss() {
            if (this.mId != 0) {
                nativeDismiss(this.mId);
                this.mId = 0;
            }
        }
    }

    public static class LoginDialog extends IdDialog {
        private final boolean mAskStore;
        private final String mDefaultUsername;

        private native void nativePostLogin(long j, String str, String str2, boolean z);

        @MainThread
        public /* bridge */ /* synthetic */ void dismiss() {
            super.dismiss();
        }

        private LoginDialog(long id, String title, String text, String defaultUsername, boolean askStore) {
            super(id, 1, title, text);
            this.mDefaultUsername = defaultUsername;
            this.mAskStore = askStore;
        }

        @MainThread
        public String getDefaultUsername() {
            return this.mDefaultUsername;
        }

        @MainThread
        public boolean asksStore() {
            return this.mAskStore;
        }

        @MainThread
        public void postLogin(String username, String password, boolean store) {
            if (this.mId != 0) {
                nativePostLogin(this.mId, username, password, store);
                this.mId = 0;
            }
        }
    }

    public static class ProgressDialog extends IdDialog {
        private final String mCancelText;
        private final boolean mIndeterminate;
        private float mPosition;

        @MainThread
        public /* bridge */ /* synthetic */ void dismiss() {
            super.dismiss();
        }

        private ProgressDialog(long id, String title, String text, boolean indeterminate, float position, String cancelText) {
            super(id, 3, title, text);
            this.mIndeterminate = indeterminate;
            this.mPosition = position;
            this.mCancelText = cancelText;
        }

        @MainThread
        public boolean isIndeterminate() {
            return this.mIndeterminate;
        }

        @MainThread
        public boolean isCancelable() {
            return this.mCancelText != null;
        }

        @MainThread
        public float getPosition() {
            return this.mPosition;
        }

        @MainThread
        public String getCancelText() {
            return this.mCancelText;
        }

        private void update(float position, String text) {
            this.mPosition = position;
            this.mText = text;
        }
    }

    public static class QuestionDialog extends IdDialog {
        public static final int TYPE_ERROR = 2;
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_WARNING = 1;
        private final String mAction1Text;
        private final String mAction2Text;
        private final String mCancelText;
        private final int mQuestionType;

        private native void nativePostAction(long j, int i);

        @MainThread
        public /* bridge */ /* synthetic */ void dismiss() {
            super.dismiss();
        }

        private QuestionDialog(long id, String title, String text, int type, String cancelText, String action1Text, String action2Text) {
            super(id, 2, title, text);
            this.mQuestionType = type;
            this.mCancelText = cancelText;
            this.mAction1Text = action1Text;
            this.mAction2Text = action2Text;
        }

        @MainThread
        public int getQuestionType() {
            return this.mQuestionType;
        }

        @MainThread
        public String getCancelText() {
            return this.mCancelText;
        }

        @MainThread
        public String getAction1Text() {
            return this.mAction1Text;
        }

        @MainThread
        public String getAction2Text() {
            return this.mAction2Text;
        }

        @MainThread
        public void postAction(int action) {
            if (this.mId != 0) {
                nativePostAction(this.mId, action);
                this.mId = 0;
            }
        }
    }

    private static native void nativeSetCallbacks(LibVLC libVLC, boolean z);

    protected Dialog(int type, String title, String text) {
        this.mType = type;
        this.mTitle = title;
        this.mText = text;
    }

    @MainThread
    public int getType() {
        return this.mType;
    }

    @MainThread
    public String getTitle() {
        return this.mTitle;
    }

    @MainThread
    public String getText() {
        return this.mText;
    }

    @MainThread
    public void setContext(Object context) {
        this.mContext = context;
    }

    @MainThread
    public Object getContext() {
        return this.mContext;
    }

    @MainThread
    public void dismiss() {
    }

    @MainThread
    public static void setCallbacks(LibVLC libVLC, Callbacks callbacks) {
        if (callbacks != null && sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
        sCallbacks = callbacks;
        nativeSetCallbacks(libVLC, callbacks != null);
    }

    private static void displayErrorFromNative(String title, String text) {
        final ErrorMessage dialog = new ErrorMessage(title, text);
        sHandler.post(new Runnable() {
            public void run() {
                if (Dialog.sCallbacks != null) {
                    Dialog.sCallbacks.onDisplay(dialog);
                }
            }
        });
    }

    private static Dialog displayLoginFromNative(long id, String title, String text, String defaultUsername, boolean askStore) {
        final LoginDialog dialog = new LoginDialog(id, title, text, defaultUsername, askStore);
        sHandler.post(new Runnable() {
            public void run() {
                if (Dialog.sCallbacks != null) {
                    Dialog.sCallbacks.onDisplay(dialog);
                }
            }
        });
        return dialog;
    }

    private static Dialog displayQuestionFromNative(long id, String title, String text, int type, String cancelText, String action1Text, String action2Text) {
        final QuestionDialog dialog = new QuestionDialog(id, title, text, type, cancelText, action1Text, action2Text);
        sHandler.post(new Runnable() {
            public void run() {
                if (Dialog.sCallbacks != null) {
                    Dialog.sCallbacks.onDisplay(dialog);
                }
            }
        });
        return dialog;
    }

    private static Dialog displayProgressFromNative(long id, String title, String text, boolean indeterminate, float position, String cancelText) {
        final ProgressDialog dialog = new ProgressDialog(id, title, text, indeterminate, position, cancelText);
        sHandler.post(new Runnable() {
            public void run() {
                if (Dialog.sCallbacks != null) {
                    Dialog.sCallbacks.onDisplay(dialog);
                }
            }
        });
        return dialog;
    }

    private static void cancelFromNative(final Dialog dialog) {
        sHandler.post(new Runnable() {
            public void run() {
                if (dialog instanceof IdDialog) {
                    ((IdDialog) dialog).dismiss();
                }
                if (Dialog.sCallbacks != null && dialog != null) {
                    Dialog.sCallbacks.onCanceled(dialog);
                }
            }
        });
    }

    private static void updateProgressFromNative(final Dialog dialog, final float position, final String text) {
        sHandler.post(new Runnable() {
            public void run() {
                if (dialog.getType() != 3) {
                    throw new IllegalArgumentException("dialog is not a progress dialog");
                }
                ProgressDialog progressDialog = dialog;
                progressDialog.update(position, text);
                if (Dialog.sCallbacks != null) {
                    Dialog.sCallbacks.onProgressUpdate(progressDialog);
                }
            }
        });
    }
}
