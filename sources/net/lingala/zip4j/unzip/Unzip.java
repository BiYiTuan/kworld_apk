package net.lingala.zip4j.unzip;

import java.io.File;
import java.util.ArrayList;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.UnzipParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class Unzip {
    private ZipModel zipModel;

    /* renamed from: net.lingala.zip4j.unzip.Unzip$1 */
    class C07581 extends Thread {
        final Unzip this$0;
        private final ArrayList val$fileHeaders;
        private final String val$outPath;
        private final ProgressMonitor val$progressMonitor;
        private final UnzipParameters val$unzipParameters;

        C07581(Unzip unzip, String $anonymous0, ArrayList arrayList, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String str) {
            super($anonymous0);
            this.this$0 = unzip;
            this.val$fileHeaders = arrayList;
            this.val$unzipParameters = unzipParameters;
            this.val$progressMonitor = progressMonitor;
            this.val$outPath = str;
        }

        public void run() {
            try {
                Unzip.access$0(this.this$0, this.val$fileHeaders, this.val$unzipParameters, this.val$progressMonitor, this.val$outPath);
                this.val$progressMonitor.endProgressMonitorSuccess();
            } catch (ZipException e) {
            }
        }
    }

    /* renamed from: net.lingala.zip4j.unzip.Unzip$2 */
    class C07592 extends Thread {
        final Unzip this$0;
        private final FileHeader val$fileHeader;
        private final String val$newFileName;
        private final String val$outPath;
        private final ProgressMonitor val$progressMonitor;
        private final UnzipParameters val$unzipParameters;

        C07592(Unzip unzip, String $anonymous0, FileHeader fileHeader, String str, UnzipParameters unzipParameters, String str2, ProgressMonitor progressMonitor) {
            super($anonymous0);
            this.this$0 = unzip;
            this.val$fileHeader = fileHeader;
            this.val$outPath = str;
            this.val$unzipParameters = unzipParameters;
            this.val$newFileName = str2;
            this.val$progressMonitor = progressMonitor;
        }

        public void run() {
            try {
                Unzip.access$1(this.this$0, this.val$fileHeader, this.val$outPath, this.val$unzipParameters, this.val$newFileName, this.val$progressMonitor);
                this.val$progressMonitor.endProgressMonitorSuccess();
            } catch (ZipException e) {
            }
        }
    }

    public Unzip(ZipModel zipModel) throws ZipException {
        if (zipModel == null) {
            throw new ZipException("ZipModel is null");
        }
        this.zipModel = zipModel;
    }

    public void extractAll(UnzipParameters unzipParameters, String outPath, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        CentralDirectory centralDirectory = this.zipModel.getCentralDirectory();
        if (centralDirectory == null || centralDirectory.getFileHeaders() == null) {
            throw new ZipException("invalid central directory in zipModel");
        }
        ArrayList fileHeaders = centralDirectory.getFileHeaders();
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(calculateTotalWork(fileHeaders));
        progressMonitor.setState(1);
        if (runInThread) {
            new C07581(this, InternalZipConstants.THREAD_NAME, fileHeaders, unzipParameters, progressMonitor, outPath).start();
        } else {
            initExtractAll(fileHeaders, unzipParameters, progressMonitor, outPath);
        }
    }

    static void access$0(Unzip unzip, ArrayList arrayList, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String str) throws ZipException {
        unzip.initExtractAll(arrayList, unzipParameters, progressMonitor, str);
    }

    private void initExtractAll(ArrayList fileHeaders, UnzipParameters unzipParameters, ProgressMonitor progressMonitor, String outPath) throws ZipException {
        for (int i = 0; i < fileHeaders.size(); i++) {
            initExtractFile((FileHeader) fileHeaders.get(i), outPath, unzipParameters, null, progressMonitor);
            if (progressMonitor.isCancelAllTasks()) {
                progressMonitor.setResult(3);
                progressMonitor.setState(0);
                return;
            }
        }
    }

    public void extractFile(FileHeader fileHeader, String outPath, UnzipParameters unzipParameters, String newFileName, ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        progressMonitor.setCurrentOperation(1);
        progressMonitor.setTotalWork(fileHeader.getCompressedSize());
        progressMonitor.setState(1);
        progressMonitor.setPercentDone(0);
        progressMonitor.setFileName(fileHeader.getFileName());
        if (runInThread) {
            new C07592(this, InternalZipConstants.THREAD_NAME, fileHeader, outPath, unzipParameters, newFileName, progressMonitor).start();
            return;
        }
        initExtractFile(fileHeader, outPath, unzipParameters, newFileName, progressMonitor);
        progressMonitor.endProgressMonitorSuccess();
    }

    static void access$1(Unzip unzip, FileHeader fileHeader, String str, UnzipParameters unzipParameters, String str2, ProgressMonitor progressMonitor) throws ZipException {
        unzip.initExtractFile(fileHeader, str, unzipParameters, str2, progressMonitor);
    }

    private void initExtractFile(FileHeader fileHeader, String outPath, UnzipParameters unzipParameters, String newFileName, ProgressMonitor progressMonitor) throws ZipException {
        if (fileHeader == null) {
            throw new ZipException("fileHeader is null");
        }
        try {
            progressMonitor.setFileName(fileHeader.getFileName());
            if (!outPath.endsWith(InternalZipConstants.FILE_SEPARATOR)) {
                outPath = new StringBuffer(String.valueOf(outPath)).append(InternalZipConstants.FILE_SEPARATOR).toString();
            }
            if (fileHeader.isDirectory()) {
                String fileName = fileHeader.getFileName();
                if (Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
                    File file = new File(new StringBuffer(String.valueOf(outPath)).append(fileName).toString());
                    if (!file.exists()) {
                        file.mkdirs();
                        return;
                    }
                    return;
                }
                return;
            }
            checkOutputDirectoryStructure(fileHeader, outPath, newFileName);
            new UnzipEngine(this.zipModel, fileHeader).unzipFile(progressMonitor, outPath, newFileName, unzipParameters);
        } catch (Throwable e) {
            progressMonitor.endProgressMonitorError(e);
            throw new ZipException(e);
        } catch (ZipException e2) {
            progressMonitor.endProgressMonitorError(e2);
            throw e2;
        } catch (Throwable e3) {
            progressMonitor.endProgressMonitorError(e3);
            throw new ZipException(e3);
        } catch (Throwable e32) {
            progressMonitor.endProgressMonitorError(e32);
            throw new ZipException(e32);
        }
    }

    public ZipInputStream getInputStream(FileHeader fileHeader) throws ZipException {
        return new UnzipEngine(this.zipModel, fileHeader).getInputStream();
    }

    private void checkOutputDirectoryStructure(FileHeader fileHeader, String outPath, String newFileName) throws ZipException {
        if (fileHeader == null || !Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
            throw new ZipException("Cannot check output directory structure...one of the parameters was null");
        }
        String fileName = fileHeader.getFileName();
        if (Zip4jUtil.isStringNotNullAndNotEmpty(newFileName)) {
            fileName = newFileName;
        }
        if (Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
            try {
                File parentDirFile = new File(new File(new StringBuffer(String.valueOf(outPath)).append(fileName).toString()).getParent());
                if (!parentDirFile.exists()) {
                    parentDirFile.mkdirs();
                }
            } catch (Throwable e) {
                throw new ZipException(e);
            }
        }
    }

    private long calculateTotalWork(ArrayList fileHeaders) throws ZipException {
        if (fileHeaders == null) {
            throw new ZipException("fileHeaders is null, cannot calculate total work");
        }
        long totalWork = 0;
        for (int i = 0; i < fileHeaders.size(); i++) {
            FileHeader fileHeader = (FileHeader) fileHeaders.get(i);
            if (fileHeader.getZip64ExtendedInfo() == null || fileHeader.getZip64ExtendedInfo().getUnCompressedSize() <= 0) {
                totalWork += fileHeader.getCompressedSize();
            } else {
                totalWork += fileHeader.getZip64ExtendedInfo().getCompressedSize();
            }
        }
        return totalWork;
    }
}
