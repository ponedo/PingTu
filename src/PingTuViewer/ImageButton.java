package PingTuViewer;

import javax.swing.*;
import java.net.URL;

public class ImageButton extends JButton {
    private int row;
    private int col;
    private int num;
    private String preName;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public ImageButton(int row, int col, int num, String preName) {
        this.row = row;
        this.col = col;
        this.num = num;
        this.preName = preName;

        updateImage(true);
    }

    public void updateImage(boolean isShow) {
        if (isShow) {
            URL url = this.getClass().getResource("/images/subImages/" + preName + "_" + num + ".jpg");
            ImageIcon icon = new ImageIcon(url);
            setIcon(icon);
        } else {
            URL url = this.getClass().getResource("/images/null.jpg");
            ImageIcon icon = new ImageIcon(url);
            setIcon(icon);
        }
    }

}
