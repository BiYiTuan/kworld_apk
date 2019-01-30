package com.memo.cable;

import java.util.List;

public final class ShellUtils {

    public static class CommandResult {
        public String errorMsg;
        public int result;
        public String successMsg;

        public CommandResult(int i, String str, String str2) {
            this.result = i;
            this.successMsg = str;
            this.errorMsg = str2;
        }
    }

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static CommandResult execCmd(String str, boolean z) {
        return execCmd(new String[]{str}, z, true);
    }

    public static CommandResult execCmd(String str, boolean z, boolean z2) {
        return execCmd(new String[]{str}, z, z2);
    }

    public static CommandResult execCmd(List<String> list, boolean z) {
        return execCmd(list == null ? null : (String[]) list.toArray(new String[0]), z, true);
    }

    public static CommandResult execCmd(List<String> list, boolean z, boolean z2) {
        return execCmd(list == null ? null : (String[]) list.toArray(new String[0]), z, z2);
    }

    public static CommandResult execCmd(String[] strArr, boolean z) {
        return execCmd(strArr, z, true);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.memo.cable.ShellUtils.CommandResult execCmd(java.lang.String[] r13, boolean r14, boolean r15) {
        /*
        r9 = -1;
        if (r13 == 0) goto L_0x0006;
    L_0x0003:
        r0 = r13.length;
        if (r0 != 0) goto L_0x000e;
    L_0x0006:
        r0 = new com.memo.cable.ShellUtils$CommandResult;
        r1 = 0;
        r2 = 0;
        r0.<init>(r9, r1, r2);
    L_0x000d:
        return r0;
    L_0x000e:
        r6 = 0;
        r5 = 0;
        r3 = 0;
        r0 = 0;
        r1 = 0;
        r4 = 0;
        r7 = java.lang.Runtime.getRuntime();	 Catch:{ Exception -> 0x011a, all -> 0x0108 }
        if (r14 == 0) goto L_0x0034;
    L_0x001a:
        r2 = "su";
    L_0x001c:
        r8 = r7.exec(r2);	 Catch:{ Exception -> 0x011a, all -> 0x0108 }
        r7 = new java.io.DataOutputStream;	 Catch:{ Exception -> 0x0125, all -> 0x010c }
        r2 = r8.getOutputStream();	 Catch:{ Exception -> 0x0125, all -> 0x010c }
        r7.<init>(r2);	 Catch:{ Exception -> 0x0125, all -> 0x010c }
        r4 = r13.length;	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r2 = 0;
    L_0x002b:
        if (r2 >= r4) goto L_0x0075;
    L_0x002d:
        r6 = r13[r2];	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        if (r6 != 0) goto L_0x0037;
    L_0x0031:
        r2 = r2 + 1;
        goto L_0x002b;
    L_0x0034:
        r2 = "sh";
        goto L_0x001c;
    L_0x0037:
        r6 = r6.getBytes();	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r7.write(r6);	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r6 = "\n";
        r7.writeBytes(r6);	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r7.flush();	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        goto L_0x0031;
    L_0x0047:
        r2 = move-exception;
        r4 = r7;
        r6 = r5;
        r5 = r3;
        r7 = r8;
        r3 = r2;
        r2 = r9;
        r11 = r0;
        r0 = r1;
        r1 = r11;
    L_0x0051:
        r3.printStackTrace();	 Catch:{ all -> 0x0114 }
        r3 = 3;
        r3 = new java.io.Closeable[r3];
        r8 = 0;
        r3[r8] = r4;
        r4 = 1;
        r3[r4] = r6;
        r4 = 2;
        r3[r4] = r5;
        com.memo.cable.CloseUtils.closeIO(r3);
        if (r7 == 0) goto L_0x0068;
    L_0x0065:
        r7.destroy();
    L_0x0068:
        r3 = new com.memo.cable.ShellUtils$CommandResult;
        if (r1 != 0) goto L_0x00fc;
    L_0x006c:
        r1 = 0;
    L_0x006d:
        if (r0 != 0) goto L_0x0102;
    L_0x006f:
        r0 = 0;
    L_0x0070:
        r3.<init>(r2, r1, r0);
        r0 = r3;
        goto L_0x000d;
    L_0x0075:
        r2 = "exit\n";
        r7.writeBytes(r2);	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r7.flush();	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        r6 = r8.waitFor();	 Catch:{ Exception -> 0x0047, all -> 0x010f }
        if (r15 == 0) goto L_0x0161;
    L_0x0083:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0130, all -> 0x010f }
        r2.<init>();	 Catch:{ Exception -> 0x0130, all -> 0x010f }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x013d, all -> 0x010f }
        r0.<init>();	 Catch:{ Exception -> 0x013d, all -> 0x010f }
        r4 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0149, all -> 0x010f }
        r1 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0149, all -> 0x010f }
        r9 = r8.getInputStream();	 Catch:{ Exception -> 0x0149, all -> 0x010f }
        r10 = "UTF-8";
        r1.<init>(r9, r10);	 Catch:{ Exception -> 0x0149, all -> 0x010f }
        r4.<init>(r1);	 Catch:{ Exception -> 0x0149, all -> 0x010f }
        r1 = new java.io.BufferedReader;	 Catch:{ Exception -> 0x0154, all -> 0x0111 }
        r5 = new java.io.InputStreamReader;	 Catch:{ Exception -> 0x0154, all -> 0x0111 }
        r9 = r8.getErrorStream();	 Catch:{ Exception -> 0x0154, all -> 0x0111 }
        r10 = "UTF-8";
        r5.<init>(r9, r10);	 Catch:{ Exception -> 0x0154, all -> 0x0111 }
        r1.<init>(r5);	 Catch:{ Exception -> 0x0154, all -> 0x0111 }
    L_0x00ad:
        r3 = r4.readLine();	 Catch:{ Exception -> 0x00b7, all -> 0x00c9 }
        if (r3 == 0) goto L_0x00bf;
    L_0x00b3:
        r2.append(r3);	 Catch:{ Exception -> 0x00b7, all -> 0x00c9 }
        goto L_0x00ad;
    L_0x00b7:
        r3 = move-exception;
        r5 = r1;
        r1 = r2;
        r2 = r6;
        r6 = r4;
        r4 = r7;
        r7 = r8;
        goto L_0x0051;
    L_0x00bf:
        r3 = r1.readLine();	 Catch:{ Exception -> 0x00b7, all -> 0x00c9 }
        if (r3 == 0) goto L_0x00e1;
    L_0x00c5:
        r0.append(r3);	 Catch:{ Exception -> 0x00b7, all -> 0x00c9 }
        goto L_0x00bf;
    L_0x00c9:
        r0 = move-exception;
        r3 = r1;
        r5 = r4;
    L_0x00cc:
        r1 = 3;
        r1 = new java.io.Closeable[r1];
        r2 = 0;
        r1[r2] = r7;
        r2 = 1;
        r1[r2] = r5;
        r2 = 2;
        r1[r2] = r3;
        com.memo.cable.CloseUtils.closeIO(r1);
        if (r8 == 0) goto L_0x00e0;
    L_0x00dd:
        r8.destroy();
    L_0x00e0:
        throw r0;
    L_0x00e1:
        r3 = r4;
        r11 = r1;
        r1 = r2;
        r2 = r11;
    L_0x00e5:
        r4 = 3;
        r4 = new java.io.Closeable[r4];
        r5 = 0;
        r4[r5] = r7;
        r5 = 1;
        r4[r5] = r3;
        r3 = 2;
        r4[r3] = r2;
        com.memo.cable.CloseUtils.closeIO(r4);
        if (r8 == 0) goto L_0x015e;
    L_0x00f6:
        r8.destroy();
        r2 = r6;
        goto L_0x0068;
    L_0x00fc:
        r1 = r1.toString();
        goto L_0x006d;
    L_0x0102:
        r0 = r0.toString();
        goto L_0x0070;
    L_0x0108:
        r0 = move-exception;
        r7 = r4;
        r8 = r6;
        goto L_0x00cc;
    L_0x010c:
        r0 = move-exception;
        r7 = r4;
        goto L_0x00cc;
    L_0x010f:
        r0 = move-exception;
        goto L_0x00cc;
    L_0x0111:
        r0 = move-exception;
        r5 = r4;
        goto L_0x00cc;
    L_0x0114:
        r0 = move-exception;
        r3 = r5;
        r8 = r7;
        r5 = r6;
        r7 = r4;
        goto L_0x00cc;
    L_0x011a:
        r2 = move-exception;
        r7 = r6;
        r6 = r5;
        r5 = r3;
        r3 = r2;
        r2 = r9;
        r11 = r0;
        r0 = r1;
        r1 = r11;
        goto L_0x0051;
    L_0x0125:
        r2 = move-exception;
        r6 = r5;
        r7 = r8;
        r5 = r3;
        r3 = r2;
        r2 = r9;
        r11 = r1;
        r1 = r0;
        r0 = r11;
        goto L_0x0051;
    L_0x0130:
        r2 = move-exception;
        r4 = r7;
        r7 = r8;
        r11 = r1;
        r1 = r0;
        r0 = r11;
        r12 = r3;
        r3 = r2;
        r2 = r6;
        r6 = r5;
        r5 = r12;
        goto L_0x0051;
    L_0x013d:
        r0 = move-exception;
        r4 = r7;
        r7 = r8;
        r11 = r1;
        r1 = r2;
        r2 = r6;
        r6 = r5;
        r5 = r3;
        r3 = r0;
        r0 = r11;
        goto L_0x0051;
    L_0x0149:
        r1 = move-exception;
        r4 = r7;
        r7 = r8;
        r11 = r2;
        r2 = r6;
        r6 = r5;
        r5 = r3;
        r3 = r1;
        r1 = r11;
        goto L_0x0051;
    L_0x0154:
        r1 = move-exception;
        r5 = r3;
        r3 = r1;
        r1 = r2;
        r2 = r6;
        r6 = r4;
        r4 = r7;
        r7 = r8;
        goto L_0x0051;
    L_0x015e:
        r2 = r6;
        goto L_0x0068;
    L_0x0161:
        r2 = r3;
        r3 = r5;
        r11 = r1;
        r1 = r0;
        r0 = r11;
        goto L_0x00e5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.memo.cable.ShellUtils.execCmd(java.lang.String[], boolean, boolean):com.memo.cable.ShellUtils$CommandResult");
    }
}
