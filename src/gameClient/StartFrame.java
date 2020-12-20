package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class StartFrame extends JFrame implements ActionListener {
        // JTextField
        static JTextField t;
        static JFrame f;
        static JButton b;
        static JLabel l;
        static JTextField t2;
        static JLabel l2;
        private Ex2 G;
        private static int lvl;
        private static long id;


        public static int getLvl()
        {
            return lvl;
        }

        public static long getId()
        {
           return id;
        }

        public void actionPerformed(ActionEvent e)
        {
            String s = e.getActionCommand();
            if (s.equals("submit")) {
                // set the text of the label to the text of the field
                this.lvl = Integer.parseInt(t.getText());
                this.id = Long.parseLong(t2.getText());
                f.setVisible(false);
                Thread client = new Thread(new Ex2());
                client.start();

            }
        }
}
