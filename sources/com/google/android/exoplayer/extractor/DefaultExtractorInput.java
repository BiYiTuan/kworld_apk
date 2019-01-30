package com.google.android.exoplayer.extractor;

import com.google.android.exoplayer.upstream.DataSource;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

public final class DefaultExtractorInput implements ExtractorInput {
    private static final byte[] SCRATCH_SPACE = new byte[4096];
    private final DataSource dataSource;
    private byte[] peekBuffer = new byte[8192];
    private int peekBufferLength;
    private int peekBufferPosition;
    private long position;
    private final long streamLength;

    public DefaultExtractorInput(DataSource dataSource, long position, long length) {
        this.dataSource = dataSource;
        this.position = position;
        this.streamLength = length;
    }

    public int read(byte[] target, int offset, int length) throws IOException, InterruptedException {
        int bytesRead = 0;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int peekBytes = Math.min(this.peekBufferLength, length);
        System.arraycopy(this.peekBuffer, 0, target, offset, peekBytes);
        offset += peekBytes;
        length -= peekBytes;
        if (length != 0) {
            bytesRead = this.dataSource.read(target, offset, length);
        }
        if (bytesRead == -1) {
            return -1;
        }
        updatePeekBuffer(peekBytes);
        bytesRead += peekBytes;
        this.position += (long) bytesRead;
        return bytesRead;
    }

    public boolean readFully(byte[] target, int offset, int length, boolean allowEndOfInput) throws IOException, InterruptedException {
        int peekBytes = Math.min(this.peekBufferLength, length);
        System.arraycopy(this.peekBuffer, 0, target, offset, peekBytes);
        offset += peekBytes;
        int remaining = length - peekBytes;
        while (remaining > 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            int bytesRead = this.dataSource.read(target, offset, remaining);
            if (bytesRead != -1) {
                offset += bytesRead;
                remaining -= bytesRead;
            } else if (allowEndOfInput && remaining == length) {
                return false;
            } else {
                throw new EOFException();
            }
        }
        updatePeekBuffer(peekBytes);
        this.position += (long) length;
        return true;
    }

    public void readFully(byte[] target, int offset, int length) throws IOException, InterruptedException {
        readFully(target, offset, length, false);
    }

    public void skipFully(int length) throws IOException, InterruptedException {
        int peekBytes = Math.min(this.peekBufferLength, length);
        int remaining = length - peekBytes;
        while (remaining > 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            int bytesRead = this.dataSource.read(SCRATCH_SPACE, 0, Math.min(SCRATCH_SPACE.length, remaining));
            if (bytesRead == -1) {
                throw new EOFException();
            }
            remaining -= bytesRead;
        }
        updatePeekBuffer(peekBytes);
        this.position += (long) length;
    }

    public void peekFully(byte[] target, int offset, int length) throws IOException, InterruptedException {
        ensureSpaceForPeek(length);
        int peekBytes = Math.min(this.peekBufferLength - this.peekBufferPosition, length);
        System.arraycopy(this.peekBuffer, this.peekBufferPosition, target, offset, peekBytes);
        offset += peekBytes;
        int fillBytes = length - peekBytes;
        int remaining = fillBytes;
        int writePosition = this.peekBufferLength;
        while (remaining > 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            int bytesRead = this.dataSource.read(this.peekBuffer, writePosition, remaining);
            if (bytesRead == -1) {
                throw new EOFException();
            }
            System.arraycopy(this.peekBuffer, writePosition, target, offset, bytesRead);
            remaining -= bytesRead;
            writePosition += bytesRead;
            offset += bytesRead;
        }
        this.peekBufferPosition += length;
        this.peekBufferLength += fillBytes;
    }

    public void advancePeekPosition(int length) throws IOException, InterruptedException {
        ensureSpaceForPeek(length);
        int fillBytes = length - Math.min(this.peekBufferLength - this.peekBufferPosition, length);
        int remaining = fillBytes;
        int writePosition = this.peekBufferLength;
        while (remaining > 0) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            int bytesRead = this.dataSource.read(this.peekBuffer, writePosition, remaining);
            if (bytesRead == -1) {
                throw new EOFException();
            }
            remaining -= bytesRead;
            writePosition += bytesRead;
        }
        this.peekBufferPosition += length;
        this.peekBufferLength += fillBytes;
    }

    public void resetPeekPosition() {
        this.peekBufferPosition = 0;
    }

    public long getPosition() {
        return this.position;
    }

    public long getLength() {
        return this.streamLength;
    }

    private void ensureSpaceForPeek(int length) {
        int requiredLength = this.peekBufferPosition + length;
        if (requiredLength > this.peekBuffer.length) {
            this.peekBuffer = Arrays.copyOf(this.peekBuffer, Math.max(this.peekBuffer.length * 2, requiredLength));
        }
    }

    private void updatePeekBuffer(int bytesConsumed) {
        this.peekBufferLength -= bytesConsumed;
        this.peekBufferPosition = 0;
        System.arraycopy(this.peekBuffer, bytesConsumed, this.peekBuffer, 0, this.peekBufferLength);
    }
}
