package software.carter;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;

public interface BoofPack extends Library {
    BoofPack INSTANCE = (BoofPack) Native.load("BoofPacked", BoofPack.class);

    boolean asyncKeystate(int key);

    String getActiveWindowTitle();

    PointerByReference getHWNDByTitle(String windowTitle);

    String getTitleByHWND(PointerByReference hwnd);

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowpos
    void setWindowLoc(PointerByReference hwnd, int x, int y, int cx, int cy);

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setwindowtexta
    void setWindowText(PointerByReference hwnd, String text);

    // https://docs.microsoft.com/en-us/windows/win32/api/processthreadsapi/nf-processthreadsapi-createprocessa
    void spawnProcess(String executablePath);

    // https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-setfocus
    void setFgWindow(PointerByReference hwnd);
}
