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
        private GameOn G;
        private static int lvl;
        private static int id;


        public static int getLvl()
        {
            return lvl;
        }

        public static int getId()
        {
           return id;
        }


        // if the vutton is pressed
        public void actionPerformed(ActionEvent e)
        {
            String s = e.getActionCommand();
            if (s.equals("submit")) {
                // set the text of the label to the text of the field
                this.lvl = Integer.parseInt(t.getText());
                System.out.println(this.lvl);
                this.id = Integer.parseInt(t2.getText());
                System.out.println(this.id);
                f.setVisible(false);
                Thread client = new Thread(new GameOn());
                client.start();

            }
        }
}
