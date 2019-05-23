package PingTuViewer;

import PingTuDao.HandleDB;
import PingTuEntity.RankingEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RankingDialog extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private MainFrame parent;

    public RankingDialog(MainFrame parent, boolean parentOperatable) {

        super(parent, parentOperatable);

        this.parent=parent;

        setTitle("Top players");
        setBounds(100, 100, 450, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel("Top 5 players");
        lblTitle.setFont(new Font("",Font.BOLD, 20));
        lblTitle.setForeground(Color.RED);
        lblTitle.setBounds(160, 10, 200,25);
        contentPanel.add(lblTitle);

        JTextArea taResult = new JTextArea();
        taResult.setBackground(Color.lightGray);
        taResult.setEditable(false);
        taResult.setBounds(32,47,372,172);
        taResult.setFont(new Font("", Font.BOLD, 12));
        contentPanel.add(taResult);

        HandleDB handleDB = new HandleDB();
        ArrayList<RankingEntry> al = handleDB.selectInfo();
        int length=al.size();
        if (length==0) {
            taResult.setText("No records now, come and play!");
        } else {
            taResult.append("\n\tRank\tName\ttime\n\n");
            for (int i=0; i<length; i++) {
                RankingEntry entry = al.get(i);
                taResult.append("\t" + (i+1) + "\t" + entry.getName() + "\t" + entry.getTime() + "\n");
            }
        }

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);


        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

