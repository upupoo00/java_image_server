package org.example.test;

import java.io.*;

public class FileCopyTest {

    public static void main(String[] args) throws IOException {
        InputStream is = new FileInputStream("C:\\Users\\CLearLove\\Desktop\\JavaWeb17\\image\\dog.png");

        FileOutputStream fos  = new FileOutputStream("D:/1.png");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = is.read(bytes)) != -1) {
            fos.write(bytes,0, len);
        }
        is.close();
        fos.close();
    }
}
