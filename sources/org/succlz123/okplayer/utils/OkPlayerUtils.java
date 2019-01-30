package org.succlz123.okplayer.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.exoplayer.util.Util;
import org.succlz123.okplayer.builder.ExtractorRendererBuilder;
import org.succlz123.okplayer.builder.HlsRendererBuilder;
import org.succlz123.okplayer.builder.RendererBuilder;

public class OkPlayerUtils {
    public static final String CONTENT_EXT_EXTRA = "type";
    public static final String CONTENT_TYPE = "content_type";
    public static final String EXT_DASH = ".mpd";
    public static final String EXT_HLS = ".m3u8";
    public static final String EXT_SS = ".ism";
    public static final String TYPE = "type";
    public static final int TYPE_DASH = 0;
    public static final int TYPE_HLS = 2;
    public static final int TYPE_OTHER = 3;
    public static final int TYPE_SS = 1;

    public static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension : uri.getLastPathSegment();
        if (lastPathSegment == null) {
            return 3;
        }
        if (lastPathSegment.endsWith(EXT_DASH)) {
            return 0;
        }
        if (lastPathSegment.endsWith(EXT_SS)) {
            return 1;
        }
        if (lastPathSegment.endsWith(EXT_HLS)) {
            return 2;
        }
        return 3;
    }

    public static RendererBuilder getRendererBuilder(Context context, Uri uri, int contentType) {
        String userAgent = Util.getUserAgent(context, "OkPlayer");
        switch (contentType) {
            case 2:
                return new HlsRendererBuilder(context, userAgent, uri.toString());
            case 3:
                return new ExtractorRendererBuilder(context, userAgent, uri);
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }
}
