

/**
 * Name: Xuefeng REN
 * Surname: XUEFENGR
 * Student ID: 1011257
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MyWindowListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e){
        super.windowClosing(e);
        System.exit(0);
    }


}
