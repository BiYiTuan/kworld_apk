package com.google.android.exoplayer.upstream.cache;

import android.net.Uri;
import android.util.Log;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.upstream.DataSink;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DataSpec;
import com.google.android.exoplayer.upstream.FileDataSource;
import com.google.android.exoplayer.upstream.TeeDataSource;
import com.google.android.exoplayer.upstream.cache.CacheDataSink.CacheDataSinkException;
import java.io.IOException;
import java.io.InterruptedIOException;

public final class CacheDataSource implements DataSource {
    private static final String TAG = "CacheDataSource";
    private final boolean blockOnCache;
    private long bytesRemaining;
    private final Cache cache;
    private final DataSource cacheReadDataSource;
    private final DataSource cacheWriteDataSource;
    private DataSource currentDataSource;
    private final EventListener eventListener;
    private int flags;
    private boolean ignoreCache;
    private final boolean ignoreCacheOnError;
    private String key;
    private CacheSpan lockedSpan;
    private long readPosition;
    private long totalCachedBytesRead;
    private final DataSource upstreamDataSource;
    private Uri uri;

    public interface EventListener {
        void onCachedBytesRead(long j, long j2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void closeCurrentSource() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0005 in list [B:6:0x0012]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/915416632.run(Unknown Source)
*/
        /*
        r4 = this;
        r3 = 0;
        r0 = r4.currentDataSource;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        r0 = r4.currentDataSource;	 Catch:{ all -> 0x001c }
        r0.close();	 Catch:{ all -> 0x001c }
        r0 = 0;	 Catch:{ all -> 0x001c }
        r4.currentDataSource = r0;	 Catch:{ all -> 0x001c }
        r0 = r4.lockedSpan;
        if (r0 == 0) goto L_0x0005;
    L_0x0012:
        r0 = r4.cache;
        r1 = r4.lockedSpan;
        r0.releaseHoleSpan(r1);
        r4.lockedSpan = r3;
        goto L_0x0005;
    L_0x001c:
        r0 = move-exception;
        r1 = r4.lockedSpan;
        if (r1 == 0) goto L_0x002a;
    L_0x0021:
        r1 = r4.cache;
        r2 = r4.lockedSpan;
        r1.releaseHoleSpan(r2);
        r4.lockedSpan = r3;
    L_0x002a:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.upstream.cache.CacheDataSource.closeCurrentSource():void");
    }

    public CacheDataSource(Cache cache, DataSource upstream, boolean blockOnCache, boolean ignoreCacheOnError) {
        this(cache, upstream, blockOnCache, ignoreCacheOnError, MediaFormat.OFFSET_SAMPLE_RELATIVE);
    }

    public CacheDataSource(Cache cache, DataSource upstream, boolean blockOnCache, boolean ignoreCacheOnError, long maxCacheFileSize) {
        this(cache, upstream, new FileDataSource(), new CacheDataSink(cache, maxCacheFileSize), blockOnCache, ignoreCacheOnError, null);
    }

    public CacheDataSource(Cache cache, DataSource upstream, DataSource cacheReadDataSource, DataSink cacheWriteDataSink, boolean blockOnCache, boolean ignoreCacheOnError, EventListener eventListener) {
        this.cache = cache;
        this.cacheReadDataSource = cacheReadDataSource;
        this.blockOnCache = blockOnCache;
        this.ignoreCacheOnError = ignoreCacheOnError;
        this.upstreamDataSource = upstream;
        if (cacheWriteDataSink != null) {
            this.cacheWriteDataSource = new TeeDataSource(upstream, cacheWriteDataSink);
        } else {
            this.cacheWriteDataSource = null;
        }
        this.eventListener = eventListener;
    }

    public long open(DataSpec dataSpec) throws IOException {
        try {
            this.uri = dataSpec.uri;
            this.flags = dataSpec.flags;
            this.key = dataSpec.key;
            this.readPosition = dataSpec.position;
            this.bytesRemaining = dataSpec.length;
            openNextSource();
            return dataSpec.length;
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    public int read(byte[] buffer, int offset, int max) throws IOException {
        try {
            int bytesRead = this.currentDataSource.read(buffer, offset, max);
            if (bytesRead >= 0) {
                if (this.currentDataSource == this.cacheReadDataSource) {
                    this.totalCachedBytesRead += (long) bytesRead;
                }
                this.readPosition += (long) bytesRead;
                if (this.bytesRemaining != -1) {
                    this.bytesRemaining -= (long) bytesRead;
                }
            } else {
                closeCurrentSource();
                if (this.bytesRemaining > 0 && this.bytesRemaining != -1) {
                    openNextSource();
                    bytesRead = read(buffer, offset, max);
                }
            }
            return bytesRead;
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    public void close() throws IOException {
        notifyBytesRead();
        try {
            closeCurrentSource();
        } catch (IOException e) {
            handleBeforeThrow(e);
            throw e;
        }
    }

    private void openNextSource() throws IOException {
        CacheSpan span;
        DataSpec dataSpec;
        if (this.ignoreCache) {
            span = null;
        } else if (this.bytesRemaining == -1) {
            Log.w(TAG, "Cache bypassed due to unbounded length.");
            span = null;
        } else if (this.blockOnCache) {
            try {
                span = this.cache.startReadWrite(this.key, this.readPosition);
            } catch (InterruptedException e) {
                throw new InterruptedIOException();
            }
        } else {
            span = this.cache.startReadWriteNonBlocking(this.key, this.readPosition);
        }
        if (span == null) {
            this.currentDataSource = this.upstreamDataSource;
            dataSpec = new DataSpec(this.uri, this.readPosition, this.bytesRemaining, this.key, this.flags);
        } else if (span.isCached) {
            long filePosition = this.readPosition - span.position;
            dataSpec = new DataSpec(Uri.fromFile(span.file), this.readPosition, filePosition, Math.min(span.length - filePosition, this.bytesRemaining), this.key, this.flags);
            this.currentDataSource = this.cacheReadDataSource;
        } else {
            this.lockedSpan = span;
            DataSpec dataSpec2 = new DataSpec(this.uri, this.readPosition, span.isOpenEnded() ? this.bytesRemaining : Math.min(span.length, this.bytesRemaining), this.key, this.flags);
            this.currentDataSource = this.cacheWriteDataSource != null ? this.cacheWriteDataSource : this.upstreamDataSource;
        }
        this.currentDataSource.open(dataSpec);
    }

    private void handleBeforeThrow(IOException exception) {
        if (!this.ignoreCacheOnError) {
            return;
        }
        if (this.currentDataSource == this.cacheReadDataSource || (exception instanceof CacheDataSinkException)) {
            this.ignoreCache = true;
        }
    }

    private void notifyBytesRead() {
        if (this.eventListener != null && this.totalCachedBytesRead > 0) {
            this.eventListener.onCachedBytesRead(this.cache.getCacheSpace(), this.totalCachedBytesRead);
            this.totalCachedBytesRead = 0;
        }
    }
}
