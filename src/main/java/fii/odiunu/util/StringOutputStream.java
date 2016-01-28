package fii.odiunu.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ojrobert on 1/28/2016.
 */
public class StringOutputStream extends OutputStream {
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void write(int b) throws IOException {
        stringBuilder.append((char) b);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
