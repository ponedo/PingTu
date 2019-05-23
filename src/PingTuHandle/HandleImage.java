package PingTuHandle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class HandleImage {

    public String getImageDir() {
        String pathName = new String();
        try {
            pathName = getClass().getResource("/images/").toURI().getPath();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return pathName;
    }

    public void deleteAll() {
        String pathName = getImageDir() + "subImages/";
        File file = new File(pathName);
        if (file.isDirectory()) {
            String fileArr[] = file.list();
            if (fileArr.length>0) {
                for (int i=0; i<fileArr.length;i++) {
                    String fileName = pathName + fileArr[i];
                    File f = new File(fileName);
                    if (f.isFile()) {
                        f.delete();
                    }
                }
            }
        }
    }

    public void cutImage(int size, int row, int col, String preName, String imageName) {
        String filename = getImageDir() + imageName;
        File file = new File(filename);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedImage bufferedImage = ImageIO.read(fileInputStream);
            fileInputStream.close();

            for (int i=0; i<row; i++) {
                for (int j=0; j<col; j++) {
                    int index = col*i + j + 1;
                    String path = getImageDir() + "subImages/" + preName + "_" + index + ".jpg";
                    FileOutputStream fileOutputStream = new FileOutputStream(path);
                    BufferedImage subImage = bufferedImage.getSubimage(j*size, i*size, size, size);
                    ImageIO.write(subImage, "jpg", fileOutputStream);
                    fileOutputStream.close();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        HandleImage hi = new HandleImage();
        hi.deleteAll();
        hi.cutImage(600/3, 3, 3, "Ahri", "Ahri.jpg");

    }
}
