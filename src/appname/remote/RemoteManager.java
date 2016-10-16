package appname.remote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//

/**
 * Created by yusiang on 11/4/14.
 */
public class RemoteManager implements Runnable {
    ScheduledExecutorService ExecService;
    final JButton toiletButton;
    int state = 0b00000100; //0b00000CBA C=Uninit B=Local A=1out

    public RemoteManager(JButton button) {
        this.toiletButton =button;
        SwingUtilities.invokeLater(new Runnable() { //Do we really need this? Invokation is guaranteed from swing.

            public void run() {
                toiletButton.setBackground(new Color(154, 154, 154));
                toiletButton.setFont(new Font("Sans",Font.PLAIN,40));
                toiletButton.setHorizontalAlignment(SwingConstants.LEFT);
                toiletButton.setFocusPainted(false);
                toiletButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getButton() == MouseEvent.BUTTON1) {
                            state &= 0b11111011; //Set to manual mode
                            state |= 0b00000010; //Set to manual mode
                            state ^= 0b00000001; //Toggle current display

                        }else if(e.getButton()==MouseEvent.BUTTON2){
                            state = 0b00000100; //Set to auto, uninitialized mode

                        }

                        updateButton(state);

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });

            }
        });
        ExecService = Executors.newSingleThreadScheduledExecutor();
        ExecService.scheduleWithFixedDelay(this, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        //TODO receive, parse, decode UDP packet. Update state.
        if((state&=0b00000010)!=0) return; //Manual mode. No touchie!
        else updateButton(state); //We update button
    }

    private void updateButton(int xstate){
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if((xstate&0b00000100)!=0){
                    toiletButton.setBackground(new Color(154, 154, 154));
                    toiletButton.setText("<html><p>Searching</p><p style=\"font-size: 16\">Remote control</p></html>");
                }
                else {
                    String s;
                    if ((xstate & 0b00000001) != 0) {
                        toiletButton.setBackground(new Color(255, 134, 137));
                        s="<html><p>ONE OUT</p>";
                    } else {
                        toiletButton.setBackground(new Color(134, 255, 136));
                        s="<html><p>ALL IN</p>";
                    }
                    if((xstate&0b00000010) != 0 ){
                        s+="<p style=\"font-size: 16\">Local control</p></html>";
                    }else{
                        s+="<p style=\"font-size: 16\">Remote control</p></html>";
                    }
                    toiletButton.setText(s);
                }

            }
        });
    }

    public void shutdown() {
        ExecService.shutdown();
    }

}
/*
Left click  sets to manual mode
Right click sets to remote mode
Default is remote mode with grey back until recv
 */