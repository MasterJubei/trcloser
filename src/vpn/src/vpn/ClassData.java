/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package vpn;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jsoup.Jsoup;
import static vpn.TrayIconDemo.createImage;

/**
 *
 * @author jiggly
 */
public class ClassData extends Stuffinput {

    String answer = ""; //Whether or not they want to change the default executable
    String answer2 = ""; //The answer to an alternate webserver
    String exe = "trans"; //The executable name
    String bip = ""; //The ip to close the executable if your wan ip is the same
    String html = ""; //A string of the webpage icanhazip.com v4
    String server = "http://ipv4.icanhazip.com/"; //Theserver to fetch wan ip
    String icanhaz = ""; //String containing the ip obtained from icanhazip.com v4
    Scanner sc = new Scanner(System.in);
    boolean prevcache = false; //To check whether there is a previous text file with the vpn info
    boolean shouldrun = true; //This is for process, it's set to true if it's a valid ip address
    final PopupMenu popup = new PopupMenu();
    final TrayIcon trayIcon = new TrayIcon(createImage("images/taskbar.png", "tray icon"));
    final SystemTray tray = SystemTray.getSystemTray();

    public ClassData() throws IOException, InterruptedException {
        guistart();
        Core();
    }

    public void Core() throws IOException, InterruptedException {
        setResizable(false); //Disable window resizing
        setTitle("TR Closer");
        Image i = ImageIO.read(getClass().getResource("images/taskbar.png"));
        setIconImage(i);

        BufferedReader reader = null; //Read the cache if there is one
        try {
            File file = new File("cache.txt");
            reader = new BufferedReader(new FileReader(file));
            exe = reader.readLine();
            server = reader.readLine();
            bip = reader.readLine();
            prevcache = true;
            reader.close();
            jTextField2.setText(bip);
            jTextField1.setText(exe);
            jTextField3.setText(server);

            try {
                if (isValidIp4(bip, 0)) {
                    tlol.start();
                }

            } catch (Exception exx) {
                Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, exx);
            }
            trayIcon.displayMessage("Note",
                    "TR Closer has been minimized", TrayIcon.MessageType.INFO);

        } catch (IOException e) {
            setVisible(true); //Displays the main window if there is no cache

        }

        /* This code was for testing for cmi
         BufferedReader reader = null;

         try {
         File file = new File("cache.txt");
         reader = new BufferedReader(new FileReader(file));
         exe = reader.readLine();
         server = reader.readLine();
         bip = reader.readLine();

         prevcache = true;
         reader.close();
         } catch (IOException e) {

         }

         if (prevcache == false) {
         System.out.println(" By default the program will close transmission-qt, would you like to change it? (y/n)");
         jTextArea1.setText("By default the program will close transmission-qt");

         answer = sc.next();
         if (answer.equals("y")) {

         System.out.println("Enter the name of the executable. This program uses a wildstar, so you only have to enter the first few characters of the executable name.");
         exe = sc.next();
         }

         if (answer.equals("n") || answer.equals("y")) {

         System.out.println("The default ip retrievel server is ipv4.icanhazip.com, prefer an alternate server? (y/n)");
         answer2 = sc.next();
         if (answer2.equals("y")) {
         System.out.println("Enter the full http url of the server, it should display your wan ip address on the page.");
         server = sc.next();
         }
         if (answer2.equals("y") || answer2.equals("n")) {
         do {
         if (answer.equals("n")) {
         System.out.println("Enter the ip that will turn transmission-qt off.");
         } else {
         System.out.println("Enter the ip that will turn " + exe + " off.");
         }

         bip = sc.next();

         } while (isValidIp4(bip, 0) == false);

         Writer writer0 = null;
         try {
         writer0 = new BufferedWriter(new OutputStreamWriter(
         new FileOutputStream("cache.txt"), "utf-8"));
         writer0.write(exe + "\n" + server + "\n" + bip);

         } catch (IOException ex) {

         } finally {
         try {
         writer0.close();
         } catch (Exception ex) {
         }
         }

         while (true) {
         process();
         Thread.sleep(10000);
         }

         } else {
         System.out.println("Enter either y or n, program will now close.");
         Thread.sleep(5000);
         System.exit(0);
         }

         } else {

         System.out.println("Enter either y or n, program will now close.");
         Thread.sleep(5000);
         System.exit(0);
         }
         } else {
         while (true) {
         process();
         Thread.sleep(10000);
         }

         }
         */
    }

    @Override
    public void jButton1ActionPerformed1MOD() {
        //When the apply button is pressed, it will get the info and write to cache.txt
        bip = jTextField2.getText();
        exe = jTextField1.getText();
        server = jTextField3.getText();
        shouldrun = isValidIp4(bip, 0);

        if (shouldrun) {

            Writer writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("cache.txt"), "utf-8"));
                writer.write(exe + "\n" + server + "\n" + bip);

            } catch (IOException ex) {
                System.out.println("It seems I couldn't write the cache");

            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                    System.out.println("It seems I couldn't write the cache");
                }
            }

            try {
                if (!tlol.isAlive()) {

                    // System.out.println("hi");
                    //  tlol.run();
                    //        tlol.interrupt();
                    //      Thread.sleep(1000);
                }
                String state = tlol.getState().toString();
                System.out.println(state);
                if(state.equals("NEW")) {
                    tlol.start();
                }
                
                tlol.notify(); //Wake up the thread so it knows to stop sleeping every 30 seconds

            } catch (Exception exx) {
                Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, exx);
            }
        }

    }
    Thread tlol = new Thread(new Runnable() { //This is for calling Process(), if it wasn't a thread it would lock up the gui

        public void run() {
            if (shouldrun == true) {
                while (true) {

                    try {

                        process();
                        Thread.sleep(10000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            } else {
                try {
                    tlol.wait(); //Invalid settings were put in by the user, waiting until notify()
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    });

    public boolean sanityCheck(String[] octet, int mode) { //Simply checking if ip address numbers are greater than zero or less than 256

        for (int i = 0; i < 4; i++) {
            try {

                if (Integer.parseInt(octet[i]) > 256 || Integer.parseInt(octet[i]) < 0) {

                    if (mode == 0) {
                        jTextArea1.append("\n" + octet[i] + " is not a valid octet.");

                        System.out.println(octet[i] + " is not a valid octet.");
                    }
                    return false;
                }
            } catch (Exception e) {
                if (mode == 0) {
                    jTextArea1.append("\n An invalid ipv4 address was specified");

                    System.out.println("An invalid ipv4 address was specified");
                    return false;
                }
                return false;
            }
        }

        return true;
    }

    public boolean isValidIp4(String localip, int mode) {
//Badly written check if the input is a correct ip address, mode specifies if it should print errors
//Too many breaks...  this could probably be written a lot shorter but w/e
        String[] octet = new String[4];
        for (int i = 0; i < 4; i++) {
            octet[i] = "";
        }
        boolean isvalid = false;
        while (isvalid == false) {
            boolean error = false;

            int ocount = 0;
            for (int i = 0; i < localip.length(); i++) {
                if (localip.charAt(i) != '.' && Character.isDigit(localip.charAt(i))) {
                    octet[ocount] += localip.charAt(i);
                } else if (localip.charAt(i) == '.') {

                    ocount++;

                } else {
                    error = true;
                    break;
                }
            }
            if (error == false) {

                if (sanityCheck(octet, mode) == true) {
                    return true;
                } else {
                    return false;
                }

            }
            return false;
        }
        return false;
    }

    public void httpv4(String webpage) { //Used for checking if the ip address from the webpage is valid
        String pip = "";
        int ocount = 0;
        for (int i = 0; i < webpage.length(); i++) {
            if (webpage.charAt(i) != '.' && Character.isDigit(webpage.charAt(i))) {
                pip += webpage.charAt(i);
            } else if (webpage.charAt(i) == '.') {
                pip += webpage.charAt(i);
            } else {
                if (isValidIp4(pip, 1) == true) {
                    icanhaz = pip;
                    break;
                }

                pip = "";

            }
        }

    }

    public void process() throws InterruptedException, IOException {
        //This method runs as a thread from tlol, and checks if the ip addresses equal eachother

        boolean pass = false;
        while (pass == false) {
            icanhaz = "";
            try {
                html = Jsoup.connect(server).get().html();
                pass = true;
            } catch (Exception e) {

                jTextArea1.setText("Either the webpage is down/invalid or your internet connection is down");
                System.out.println("Either the webpage is down/invalid or your internet connection is down");
                trayIcon.displayMessage("TR Closer",
                        "Webpage is down or your internet connection is offline", TrayIcon.MessageType.WARNING);
                Thread.sleep(5000);
            }
        }

        httpv4(html);

        //Check if there's a 0 in any of the octets for a wildcard, could be written a LOT shorter, there's no need for a string array and an int array
        String octet[] = new String[4];
        String ioctet[] = new String[4];

        for (int i = 0; i < 4; i++) {
            octet[i] = "";
            ioctet[i] = "";
        }
        int octeti[] = new int[4];
        int iocteti[] = new int[4];
        int ocount = 0;
        int iocount = 0;
        for (int i = 0; i < bip.length(); i++) {
            if (bip.charAt(i) != '.') {
                octet[ocount] += bip.charAt(i);
            }
            if (bip.charAt(i) == '.') {
                ocount++;
            }

        }
        for (int i = 0; i < icanhaz.length(); i++) {
            if (icanhaz.charAt(i) != '.') {
                ioctet[iocount] += icanhaz.charAt(i);
            }
            if (icanhaz.charAt(i) == '.') {
                iocount++;
            }

        }
        for (int i = 0; i < 4; i++) {
            octeti[i] = Integer.parseInt(octet[i]);
            iocteti[i] = Integer.parseInt(ioctet[i]);
        }

        for (int i = 0; i < 4; i++) {
            if (octeti[i] == 0) {
                iocteti[i] = 0;
            }
        }

        icanhaz = String.valueOf(iocteti[0]) + '.' + String.valueOf(iocteti[1]) + '.' + String.valueOf(iocteti[2]) + '.' + String.valueOf(iocteti[3]);
        

        if (icanhaz.equals(bip)) {

            trayIcon.displayMessage("TR Closer",
                    "Your VPN appears to be offline", TrayIcon.MessageType.WARNING);
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "taskkill /f /im " + exe + "*");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                jTextArea1.append("\n" + line);
                System.out.println(line);
            }

        } else {

            jTextArea1.append("\n VPN is active");

            System.out.println("VPN is active.");
        }

    }

    public void guistart() {
        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (IOException ex) {
                    Logger.getLogger(ClassData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void createAndShowGUI() throws IOException {
        //     setTitle("TR Closer");

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem cb1 = new MenuItem("Open TR Closer");
        // CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        Menu displayMenu = new Menu("Display");
        MenuItem errorItem = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem = new MenuItem("Info");
        MenuItem noneItem = new MenuItem("None");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        // popup.add(cb2);
        popup.addSeparator();
        //  popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "This dialog box is run from System Tray");
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "\t Version 1.00 \n More info at demthruz.wordpress.com");
            }
        });

        cb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
            }
        });

        /*    cb1.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
         int cb1Id = e.getStateChange();
         if (cb1Id == ItemEvent.SELECTED){
         trayIcon.setImageAutoSize(true);
         } else {
         trayIcon.setImageAutoSize(false);
         }
         }
         });
         
         /*  cb2.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
         int cb2Id = e.getStateChange();
         if (cb2Id == ItemEvent.SELECTED){
         trayIcon.setToolTip("Sun TrayIcon");
         } else {
         trayIcon.setToolTip(null);
         }
         }
         });
         */
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) e.getSource();
                //TrayIcon.MessageType type = null;
                System.out.println(item.getLabel());
                if ("Error".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.ERROR;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an error message", TrayIcon.MessageType.ERROR);

                } else if ("Warning".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.WARNING;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is a warning message", TrayIcon.MessageType.WARNING);

                } else if ("Info".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.INFO;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an info message", TrayIcon.MessageType.INFO);

                } else if ("None".equals(item.getLabel())) {
                    //type = TrayIcon.MessageType.NONE;
                    trayIcon.displayMessage("Sun TrayIcon Demo",
                            "This is an ordinary message", TrayIcon.MessageType.NONE);
                }
            }
        };

        errorItem.addActionListener(listener);
        warningItem.addActionListener(listener);
        infoItem.addActionListener(listener);
        noneItem.addActionListener(listener);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    protected Image createImage(String path, String description) {
        URL imageURL = TrayIconDemo.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

}
