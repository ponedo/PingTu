package PingTuViewer;

import PingTuDao.HandleDB;
import PingTuHandle.HandleImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

import static java.lang.Math.abs;

public class MainFrame extends JFrame implements ActionListener {

    /*Components*/
    private JPanel contentPane;
    private JLabel lblShow = new JLabel("");
    private JPanel imagePanel = new JPanel();
    private JButton btnRanking = new JButton("Ranking");
    private JButton btnMusic = new JButton("Music On");
    private JButton btnInstruction = new JButton("Instruction");
    private JButton btnStart = new JButton("Start");
    private JLabel lblLeftTime = new JLabel();
    private JComboBox<String> imgSelect = null;
    private JComboBox<String> difSelect = null;

    /*Variables for initialization*/
    private int nums = 3; //difficulty
    private String imageName = "Ahri.jpg";
    private String preName = "Ahri3x3";
    private boolean isRun = false;
    private int num_arr[][] = null;//for initNumArr

    /*Dynamic variables*/
    private ImageButton img_btn_array[][] = null;
    private int blankRow = nums - 1;
    private int blankCol = nums - 1;
    private Timer timer = new Timer();
    private int timeLeft=1200;

    /*Static variables*/
    private AudioClip sound = null;
    private HandleDB handleDB = new HandleDB();
    private String strImage[] = {"Image Select", "Ahri", "NagatoYuki", "Summoner'sCup", "Obba","Developer"};

    /*Inner class of counter*/
    public class MyTask extends TimerTask {
        @Override
        public void run() {
            timeLeft--;
            lblLeftTime.setText(timeLeft+"");
            if (timeLeft==0) {
                lblLeftTime.setText("0");
                JOptionPane.showMessageDialog(null, "Time up!");
                this.cancel();
            }
        }
    }

    public void startThread() {
        if (timer!=null) {
            timer.cancel();
        }
        timeLeft = 1200;
        timer = new Timer();
        timer.schedule(new MyTask(), 1000, 1000);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnMusic) {
            musicSwitch(ae);
        } else if (ae.getSource() == btnStart) {
            if (isRun) {
                int option = JOptionPane.showConfirmDialog(this, "Are you sure to start again?");
                if (option == 0) {
                    init();
                    startThread();
                }
            } else {
                init();
                isRun=true;
                btnStart.setText("Restart");
                startThread();
            }
        } else if (ae.getSource() == btnInstruction) {
            JOptionPane.showMessageDialog(this, "This game is a homework of my java course.");
        } else if (ae.getSource() == btnRanking) {
            new RankingDialog(this, true);
        } else {
            ImageButton img_btn = (ImageButton) ae.getSource();

            update(img_btn);

            if (finished()) {
                JOptionPane.showMessageDialog(this, "Congratulations!");
                timer.cancel();
                int leftTime = Integer.parseInt(lblLeftTime.getText());
                int time = 1200 - leftTime;
                String name = JOptionPane.showInputDialog(this, "Please input your name: ");
                if (name==null || "".equals(name.trim())) {
                    name = "Mysterious player ";
                }
                handleDB.insertInfo(name, time);
            }
        }
    }

    public MainFrame() {

        this.setTitle("PingTu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 915, 670);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        imagePanel.setBackground(Color.lightGray);
        imagePanel.setBounds(15, 15, 600, 600);
        contentPane.add(imagePanel);

        lblShow.setForeground(Color.GRAY);
        lblShow.setBackground(Color.MAGENTA);
        lblShow.setBounds(640, 20, 250, 260);
        lblShow.setBackground(Color.MAGENTA);
        lblShow.setBorder(new TitledBorder(null, "Original Image"));
        contentPane.add(lblShow);

        btnRanking.setBounds(700, 438, 120, 25);
        contentPane.add(btnRanking);
        btnRanking.addActionListener(this);

        btnMusic.setBounds(700, 482, 120, 25);
        contentPane.add(btnMusic);
        btnMusic.addActionListener(this);

        btnInstruction.setBounds(700, 526, 120, 25);
        contentPane.add(btnInstruction);
        btnInstruction.addActionListener(this);

        btnStart.setBounds(700, 570, 120, 25);
        contentPane.add(btnStart);
        btnStart.addActionListener(this);

        JLabel lbl1 = new JLabel();
        lbl1.setBounds(670, 300, 150, 25);
        lbl1.setFont(new Font("", Font.BOLD, 16));
        lbl1.setText("Time remained:");
        contentPane.add(lbl1);

        lblLeftTime.setBounds(800, 300, 150, 25);
        lblLeftTime.setFont(new Font("", Font.BOLD, 16));
        lblLeftTime.setForeground(Color.RED);
        lblLeftTime.setText("1200");
        contentPane.add(lblLeftTime);

        imgSelect = new JComboBox<String>(strImage);
        imgSelect.setBounds(700, 350, 120, 25);
        contentPane.add(imgSelect);

        String strDifficulty[] = {"Difficulty Select", "3x3", "4x4", "5x5","6x6"};
        difSelect = new JComboBox<String>(strDifficulty);
        difSelect.setBounds(700, 394, 120, 25);
        contentPane.add(difSelect);

        URL urlSound = getClass().getResource("/music/sound.wav");
        sound = Applet.newAudioClip(urlSound);
        isRun=false;
    }

    public void init() {

        initDifficulty();
        initImage();
        initNumArr();

        imagePanel.removeAll();
        imagePanel.setLayout(new GridLayout(nums, nums));

        img_btn_array = new ImageButton[nums][nums];
        HandleImage handleImage = new HandleImage();
        handleImage.deleteAll();
        handleImage.cutImage(600/nums, nums, nums, preName, imageName);

        for (int i = 0; i < nums; i++) {
            for (int j = 0; j < nums; j++) {
                ImageButton btn = new ImageButton(i, j, num_arr[i][j], preName);
                img_btn_array[i][j] = btn;
                btn.addActionListener(this);
                imagePanel.add(btn);
            }
        }

        img_btn_array[blankRow][blankCol].updateImage(false);
    }

    public void initNumArr() {

        Random rand = new Random();

        int move_direction;
        int move_times;
        int record[] = new int[4];

        blankRow = nums - 1;
        blankCol = nums - 1;

        //normal order
        num_arr = new int[nums][nums];
        for (int i = 0; i < nums; i++) {
            for (int j = 0; j < nums; j++)
                num_arr[i][j] = nums * i + j + 1;
        }

        move_times = 0;

        //move for move_times
        while (move_times < 30*nums) {

            move_direction = rand.nextInt(4);

            switch (move_direction) {
                //upOK
                case 0:
                    if ((blankRow - 1 >= 0)) {
                        swap(blankRow, blankCol, --blankRow, blankCol);
                        move_times++;
                    }
                    break;
                //downOK
                case 1:
                    if (blankRow + 1 <= nums - 1) {
                        swap(blankRow, blankCol, ++blankRow, blankCol);
                        move_times++;
                    }
                    break;
                //leftOK
                case 2:
                    if (blankCol - 1 >= 0) {
                        swap(blankRow, blankCol, blankRow, --blankCol);
                        move_times++;
                    }
                    break;
                //rightOK
                case 3:
                    if (blankCol + 1 <= nums - 1) {
                        swap(blankRow, blankCol, blankRow, ++blankCol);
                        move_times++;
                    }
                    break;
            }
        }
    }

    public void initDifficulty() {
        int dif = difSelect.getSelectedIndex();
        if (dif==0 || dif==1) {
            nums = 3;
        } else {
            nums = dif + 2;
        }
    }

    public void initImage() {
        int index = imgSelect.getSelectedIndex();
        if (index==0) {
            index = 1;
        }
        imageName = strImage[index] + ".jpg";
        preName = strImage[index] + nums + "x" + nums;
        URL url = getClass().getResource("/images/" + imageName);
        ImageIcon icon = new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(240,240, Image.SCALE_DEFAULT));
        lblShow.setIcon(icon);
    }

    public void update(ImageButton img_btn) {

        if (abs(blankRow - img_btn.getRow()) + abs(blankCol - img_btn.getCol()) == 1) { //if swapOK

            int temp = img_btn_array[blankRow][blankCol].getNum();

            //set new icon to old blank button
            img_btn_array[blankRow][blankCol].setNum(img_btn.getNum());
            img_btn_array[blankRow][blankCol].updateImage(true);

            //set img_btn to new blank button
            blankRow = img_btn.getRow();
            blankCol = img_btn.getCol();
            img_btn_array[blankRow][blankCol].setNum(temp);
            img_btn_array[blankRow][blankCol].updateImage(false);
        }
    }

    public void musicSwitch(ActionEvent ae) {
        JButton btn = (JButton) ae.getSource();
        if ("Music On".equals(btn.getText().trim())) {
            sound.loop();
            btn.setText("Music Off");
        } else {
            sound.stop();
            btn.setText("Music On");
        }
    }

    public void swap(int x_row, int x_col, int y_row, int y_col) {
        int temp = num_arr[x_row][x_col];
        num_arr[x_row][x_col] = num_arr[y_row][y_col];
        num_arr[y_row][y_col] = temp;
    }

    public boolean finished() {

        for (int i = 0; i < nums; i++) {
            for (int j = 0; j < nums; j++) {
                if (img_btn_array[i][j].getNum() != nums*i + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }
}