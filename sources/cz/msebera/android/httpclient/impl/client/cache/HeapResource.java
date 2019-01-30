package cz.msebera.android.httpclient.impl.client.cache;

import cz.msebera.android.httpclient.annotation.Immutable;
import cz.msebera.android.httpclient.client.cache.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Immutable
public class HeapResource implements Resource {
    private static final long serialVersionUID = -2078599905620463394L;
    /* renamed from: b */
    private final byte[] f117b;

    public HeapResource(byte[] b) {
        this.f117b = b;
    }

    byte[] getByteArray() {
        return this.f117b;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.f117b);
    }

    public long length() {
        return (long) this.f117b.length;
    }

    public void dispose() {
    }
}
