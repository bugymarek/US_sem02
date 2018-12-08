/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import appCore.Core;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import static constants.CommonConstants.REALTY_CADASTER_NAME_MAX_LENGTH;
import static constants.CommonConstants.REALTY_DESC_MAX_LENGTH;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Bugy
 */
public class MainApp extends javax.swing.JDialog {

    private Core core;
    private ManagerHTML managerHTML;

    /**
     * Creates new form MainApp
     */
    public MainApp(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        configTextPane();
        managerHTML = new ManagerHTML();

        int result = JOptionPane.showConfirmDialog(this,
                "Chcete načítať exitstujúcu databázú dát",
                "Uložisko dát",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            DialogFileNames dialogFileNames = new DialogFileNames(parent, true);
            if (dialogFileNames.isCancel()) {
                String message = "************************************************************************************************\n"
                        + "                             Nepodarilo sa načítať databázu zo súborov" + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.ERR);
                return;
            }

            String fileMainNeme = dialogFileNames.getTextFieldID();
            String fileAdditionName = dialogFileNames.geteTxtFieldIDAndCadasterName();
            this.core = new Core(fileMainNeme, fileAdditionName);
            dynamicHashingCore.DynamicHashing dh1 = core.getDynamicHashingRealty();
            dynamicHashingCore.DynamicHashing dh2 = core.getDynamicHashingRealtyInCadaster();

            if (dh1 == null || dh2 == null) {
                String message = "************* Nepodarilo sa načítať databázu zo súborov*****************************************\n"
                        + " Prefix súboru nehnuteľnosti  podľa id: " + fileMainNeme + "\n"
                        + " Prefix súboru nehnuteľnosti podľa súp. čísla a názvu katastra: " + fileAdditionName + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.ERR);
            } else {
                String message = "************************************************************************************************\n"
                        + "                             Úspešné načítanie databázy" + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.SUC);
            }

        }

        if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION) {
            DialogNewFiles dialogNewFiles = new DialogNewFiles(parent, true);
            if (dialogNewFiles.isCancel()) {
                String message = "************************************************************************************************\n"
                        + "                             Nepodarilo sa vytvoriť novú databázu" + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.ERR);
                return;
            }
            this.core = new Core(dialogNewFiles.getTextFieldID(),
                    dialogNewFiles.geteTxtFieldIDAndCadasterName(),
                    dialogNewFiles.getMainFactor(),
                    dialogNewFiles.getAdditionFactor(),
                    dialogNewFiles.getMainFactor()
            );
            dynamicHashingCore.DynamicHashing dh1 = core.getDynamicHashingRealty();
            dynamicHashingCore.DynamicHashing dh2 = core.getDynamicHashingRealtyInCadaster();

            if (dh1 == null || dh2 == null || !core.saveToConfigFiles()) {
                String message = "*************       Nepodarilo sa vytvoriť novú databázu    *****************************************\n"
                        + " Prefix súboru nehnuteľnosti  podľa id: " + dialogNewFiles.getTextFieldID() + "\n"
                        + " Prefix súboru nehnuteľnosti podľa súp. čísla a názvu katastra: " + dialogNewFiles.geteTxtFieldIDAndCadasterName() + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.ERR);
            } else {
                String message = "************************************************************************************************\n"
                        + "                             Úspešné vytvorenie databázy" + "\n"
                        + "*******************************************************************************************************";
                addToConsole(message, State.SUC);
            }

        }

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                core.saveToConfigFiles();
                System.exit(0);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneConsole = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldIDRealtyInCadaster = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldNameCadaster = new javax.swing.JTextField();
        jTextFieldDescription = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldIDRealty = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldRegistrationNumberRealtyFind = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldIDFind = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jTextFieldCadasterNameFind = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldEditIDRealty = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jTextFieldEditDescriptionRealty = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jTextFieldGeneratorRealtyCount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(jTextPaneConsole);

        jButton1.setText("Vyčisti konzolu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Súpisné číslo *");

        jLabel2.setText("Názov katastrálneho územia *");

        jLabel3.setText("Popis *");

        jLabel4.setText("Identifikačné číslo nehnuteľnosti *");

        jButton3.setText("Vlož");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldIDRealty)
                    .addComponent(jTextFieldDescription)
                    .addComponent(jTextFieldNameCadaster)
                    .addComponent(jTextFieldIDRealtyInCadaster)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(0, 142, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldIDRealtyInCadaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNameCadaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldIDRealty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 238, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Pridaj", jPanel5);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Podľa identifikačné čísla nehnuteľnosti"));

        jLabel6.setText("Identifikačné číslo nehnuteľnosti *");

        jButton5.setText("Nájdi");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldRegistrationNumberRealtyFind)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 120, Short.MAX_VALUE))
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldRegistrationNumberRealtyFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Podľa súp. čísla nehnuteľnosti a názvu katastra"));

        jLabel7.setText("Súpisné číslo *");

        jButton6.setText("Nájdi");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel8.setText("Názov katastrálneho územia *");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldIDFind)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCadasterNameFind)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldIDFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCadasterNameFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Vyhľadaj", jPanel3);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Vyhľadanie nehnuteľnosti podľa klúča"));

        jLabel9.setText("Identifikačné číslo nehnuteľnost *");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 123, Short.MAX_VALUE))
                    .addComponent(jTextFieldEditIDRealty))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEditIDRealty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Editované položky"));

        jLabel10.setText("Popis");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldEditDescriptionRealty)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldEditDescriptionRealty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(283, Short.MAX_VALUE))
        );

        jButton12.setText("Edituj");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton12)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Zmeň", jPanel6);

        jButton4.setText("Generuj");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Počet nehnuteľností *");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 212, Short.MAX_VALUE))
                    .addComponent(jTextFieldGeneratorRealtyCount))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(jTextFieldGeneratorRealtyCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 440, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Generátor", jPanel4);

        jButton7.setText("Vypíš neutriedený súbor");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Nehnuteľnosti podľa identifikačného čísla"));

        jButton8.setText("Vypíš hlavný súbor");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Vypíš preplňovací súbor");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Nehnuteľnosti podľa sup. čísla a názvu katastra"));

        jButton10.setText("Vypíš hlavný súbor");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Vypíš preplňovací súbor");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jButton7)
                .addGap(49, 49, 49)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Výpis", jPanel9);

        jButton2.setText("Ulož do súboru");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextPaneConsole.setText("");
        configTextPane();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (core.saveToConfigFiles()) {
            String message = "************************************************************************************************\n"
                    + "                             Úspešné uloženie databázy do súboru" + "\n"
                    + "*******************************************************************************************************";
            addToConsole(message, State.SUC);
        } else {
            String message = "************************************************************************************************\n"
                    + "                             Neúspešné uloženie databázy do súboru" + "\n"
                    + "*******************************************************************************************************";
            addToConsole(message, State.ERR);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String idRealtyInCadaster = jTextFieldIDRealtyInCadaster.getText();
        String nameCadaster = jTextFieldNameCadaster.getText();
        String desc = jTextFieldDescription.getText();
        String id = jTextFieldIDRealty.getText();
        if (isEmptyTextField(idRealtyInCadaster)
                || isEmptyTextField(nameCadaster)
                || isEmptyTextField(desc)
                || isEmptyTextField(id)) {
            JOptionPane.showMessageDialog(this,
                    "Vyplnte všetky polička označene hviezdičkou.(*)",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (nameCadaster.length() > REALTY_CADASTER_NAME_MAX_LENGTH ) {
            JOptionPane.showMessageDialog(this,
                    "Prekročili ste povolenú dížku názovu katastrálneho územia. Povolená dlžka je " + REALTY_CADASTER_NAME_MAX_LENGTH + " znakov.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (desc.length() > REALTY_DESC_MAX_LENGTH ) {
            JOptionPane.showMessageDialog(this,
                    "Prekročili ste povolenú dížku popisu. Povolená dlžka je " + REALTY_DESC_MAX_LENGTH + " znakov.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = tryParseToInteger(idRealtyInCadaster);
        String result1 = tryParseToInteger(id);
        if (result != null || result1 != null) {
            JOptionPane.showMessageDialog(this,
                    "Zadali ste text do poľa určeného pre číslo.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int addedResult = core.addRealty(getInt(id), getInt(idRealtyInCadaster), nameCadaster, desc);
        String message = new String();
        switch (addedResult) {
            case 0:
                message = "Úspešne vloženie nehnuteľnosti.\n";
                break;
            case -1:
                message = "Neúspešne vloženie nehnuteľnosti. Nehnuteľnosť s identifikačným číslom sa v databáze už nachádza. \n";
                break;
            case -2:
                message = "Neúspešne vloženie nehnuteľnosti. Nehnuteľnosť podľa súpisného čísla a názvu katastra sa v databáze už nachádza.\n";
                break;
            case -3:
                message = "Neúspešne vloženie nehnuteľnosti. Nepodarilo sa vložiť nehnuteľnosť do neusporiadaného súboru.";
                break;
            case -4:
                message = "Neúspešne vloženie nehnuteľnosti do dynamického hašu podľa identifikačného čísla. Ale podarilo sa vložiť do neusporiadaného súboru.\n";
                break;
            case -5:
                message = "Neúspešne vloženie nehnuteľnosti do dynamického hašu podľa sup. čísla a názvu katastu. "
                        + "Ale podarilo sa vložiť do neusporiadaného súboru a do dynamického hašu podľa identifikačného čísla.\n";
                break;
        }
        message += "******************************************************\n"
                + " identifikačné číslo nehnuteľnosti: " + id + "\n"
                + " súpisné číslo: " + idRealtyInCadaster + "\n"
                + " názov katastru: " + nameCadaster + "\n"
                + " popis: " + desc + "\n"
                + "******************************************************";
        if (addedResult == 0) {
            addToConsole(message, State.SUC);
        } else {
            addToConsole(message, State.ERR);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (isEmptyTextField(jTextFieldGeneratorRealtyCount.getText())) {
            JOptionPane.showMessageDialog(this,
                    "Vyplnte všetky polička označene hviezdičkou.(*)",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = MainApp.tryParseToInteger(jTextFieldGeneratorRealtyCount.getText());
        if (result != null) {
            JOptionPane.showMessageDialog(this,
                    "Zadali ste text do poľa určeného pre číslo.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resultGenerate = core.generateRealties(getInt(jTextFieldGeneratorRealtyCount.getText()));

        if (resultGenerate == 0) {
            String message = "************************************************************************************************\n"
                    + "                             Úspešné vygenerovanie dát" + "\n"
                    + "*******************************************************************************************************";
            addToConsole(message, State.SUC);
        } else {
            String message = "************************************************************************************************\n"
                    + "                             Počet neúspešne vygenerovaných záznamov" + resultGenerate + "\n"
                    + "*******************************************************************************************************";
            addToConsole(message, State.NON);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String id = jTextFieldRegistrationNumberRealtyFind.getText();
        if (isEmptyTextField(id)) {
            JOptionPane.showMessageDialog(this,
                    "Vyplnte všetky polička označene hviezdičkou.(*)",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = MainApp.tryParseToInteger(id);
        if (result != null) {
            JOptionPane.showMessageDialog(this,
                    "Zadali ste text do poľa určeného pre číslo.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JsonObject resultObject = core.findRealtyByID(getInt(id));
        if (resultObject.get("err") != null) {
            String message = resultObject.get("err").getAsString() + "\n";
            message += "******************************************************\n"
                    + " identifikačné číslo: " + id + "\n"
                    + "******************************************************";
            addToConsole(message, State.ERR);
            return;
        }

        String message = "******************************************************\n"
                + " identifikačné číslo nehnuteľnosti: " + resultObject.get("id").getAsString() + "\n"
                + " súpisné číslo: " + resultObject.get("idRealty").getAsString() + "\n"
                + " názov katastru: " + resultObject.get("cadasterName").getAsString() + "\n"
                + " popis: " + resultObject.get("desc").getAsString() + "\n"
                + "******************************************************";
        addToConsole(message, State.SUC);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String id = jTextFieldIDFind.getText();
        String cadasterName = jTextFieldCadasterNameFind.getText();
        if (isEmptyTextField(id)
                || isEmptyTextField(cadasterName)) {
            JOptionPane.showMessageDialog(this,
                    "Vyplnte všetky polička označene hviezdičkou.(*)",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cadasterName.length() > REALTY_CADASTER_NAME_MAX_LENGTH ) {
            JOptionPane.showMessageDialog(this,
                    "Prekročili ste povolenú dížku názovu katastrálneho územia. Povolená dlžka je " + REALTY_CADASTER_NAME_MAX_LENGTH + " znakov.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = MainApp.tryParseToInteger(id);
        if (result != null) {
            JOptionPane.showMessageDialog(this,
                    "Zadali ste text do poľa určeného pre číslo.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JsonObject resultObject = core.findRealtyByRegistrationNumberAndCadasterName(getInt(id), cadasterName);
        if (resultObject.get("err") != null) {
            String message = resultObject.get("err").getAsString() + "\n";
            message += "******************************************************\n"
                    + " súpisné číslo: " + id + "\n"
                    + " názov katastru: " + id + "\n"
                    + "******************************************************";
            addToConsole(message, State.ERR);
            return;
        }

        String message = "******************************************************\n"
                + " identifikačné číslo nehnuteľnosti: " + resultObject.get("id").getAsString() + "\n"
                + " súpisné číslo: " + resultObject.get("idRealty").getAsString() + "\n"
                + " názov katastru: " + resultObject.get("cadasterName").getAsString() + "\n"
                + " popis: " + resultObject.get("desc").getAsString() + "\n"
                + "******************************************************";
        addToConsole(message, State.SUC);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        JsonArray result = core.getBlocksRealtyByIdFromMainFile();

        if (result == null) {
            addToConsole("V súbore niesu žiadne bloky", State.NON);
        } else {
            for (JsonElement jsonElement : result) {
                JsonObject block = (JsonObject) jsonElement;

                String message = "******************************************************\n"
                        + " stav bloku: " + (block.get("isValid").getAsBoolean() ? "platný" : "neplatný") + "\n"
                        + " adresa: " + block.get("address").getAsString() + "\n"
                        + " adresa nasledujúceho zreťazeného bloku: " + block.get("addressNextBlock").getAsString() + "\n"
                        + " faktor: " + block.get("factor").getAsString() + "\n"
                        + " počet platných záznamov: " + block.get("validCount").getAsString() + "\n"
                        + "******************************************************";
                addToConsole(message, State.SUC);
                String rowsHtml = "";
                for (JsonElement jsonElementRecord : block.get("records").getAsJsonArray()) {
                    JsonObject record = (JsonObject) jsonElementRecord;

                    ArrayList<String> dataArr = new ArrayList<>();
                    dataArr.add(record.get("isValid").getAsBoolean() ? "platný" : "neplatný");
                    dataArr.add(record.get("id").getAsString());
                    dataArr.add(record.get("address").getAsString());

                    rowsHtml += "\n";
                    rowsHtml += managerHTML.createTableRow(dataArr);
                }

                String[] headerArr = {"Stav záznamu", "Identifikačné číslo", "adresa"};
                String headerHtml = managerHTML.createTableHeader(headerArr);
                String tableHtml = managerHTML.createTable(headerHtml, rowsHtml);
                addHtmlComponent(tableHtml);
                addHtmlComponent("<br>");
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        JsonArray result = core.getBlocksRealtyByIdInCadasterAndCadasterNameFromMainFile();

        if (result == null) {
            addToConsole("V súbore niesu žiadne bloky", State.NON);
        } else {
            for (JsonElement jsonElement : result) {
                JsonObject block = (JsonObject) jsonElement;

                String message = "******************************************************\n"
                        + " stav bloku: " + (block.get("isValid").getAsBoolean() ? "platný" : "neplatný") + "\n"
                        + " adresa: " + block.get("address").getAsString() + "\n"
                        + " adresa nasledujúceho zreťazeného bloku: " + block.get("addressNextBlock").getAsString() + "\n"
                        + " faktor: " + block.get("factor").getAsString() + "\n"
                        + " počet platných záznamov: " + block.get("validCount").getAsString() + "\n"
                        + "******************************************************";
                addToConsole(message, State.SUC);
                String rowsHtml = "";
                for (JsonElement jsonElementRecord : block.get("records").getAsJsonArray()) {
                    JsonObject record = (JsonObject) jsonElementRecord;

                    ArrayList<String> dataArr = new ArrayList<>();
                    dataArr.add(record.get("isValid").getAsBoolean() ? "platný" : "neplatný");
                    dataArr.add(record.get("registerNumber").getAsString());
                    dataArr.add(record.get("cadasterName").getAsString());
                    dataArr.add(record.get("address").getAsString());

                    rowsHtml += "\n";
                    rowsHtml += managerHTML.createTableRow(dataArr);
                }

                String[] headerArr = {"Stav záznamu", "Supisné číslo", "Názov katastrálneho územia", "adresa"};
                String headerHtml = managerHTML.createTableHeader(headerArr);
                String tableHtml = managerHTML.createTable(headerHtml, rowsHtml);
                addHtmlComponent(tableHtml);
                addHtmlComponent("<br>");
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        JsonArray result = core.getBlocksRealtyByIdFromAdditionFile();

        if (result == null) {
            addToConsole("V súbore niesu žiadne bloky", State.NON);
        } else {
            for (JsonElement jsonElement : result) {
                JsonObject block = (JsonObject) jsonElement;

                String message = "******************************************************\n"
                        + " adresa: " + block.get("address").getAsString() + "\n"
                        + " adresa nasledujúceho zreťazeného bloku: " + block.get("addressNextBlock").getAsString() + "\n"
                        + " faktor: " + block.get("factor").getAsString() + "\n"
                        + " počet platných záznamov: " + block.get("validCount").getAsString() + "\n"
                        + "******************************************************";
                addToConsole(message, State.SUC);
                String rowsHtml = "";
                for (JsonElement jsonElementRecord : block.get("records").getAsJsonArray()) {
                    JsonObject record = (JsonObject) jsonElementRecord;

                    ArrayList<String> dataArr = new ArrayList<>();
                    dataArr.add(record.get("isValid").getAsBoolean() ? "platný" : "neplatný");
                    dataArr.add(record.get("id").getAsString());
                    dataArr.add(record.get("address").getAsString());

                    rowsHtml += "\n";
                    rowsHtml += managerHTML.createTableRow(dataArr);
                }

                String[] headerArr = {"Stav záznamu", "Identifikačné číslo", "adresa"};
                String headerHtml = managerHTML.createTableHeader(headerArr);
                String tableHtml = managerHTML.createTable(headerHtml, rowsHtml);
                addHtmlComponent(tableHtml);
                addHtmlComponent("<br>");
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        JsonArray result = core.getBlocksRealtyByIdInCadasterAndCadasterNameFromAdditionFile();

        if (result == null) {
            addToConsole("V súbore niesu žiadne bloky", State.NON);
        } else {
            for (JsonElement jsonElement : result) {
                JsonObject block = (JsonObject) jsonElement;

                String message = "******************************************************\n"
                        + " adresa: " + block.get("address").getAsString() + "\n"
                        + " adresa nasledujúceho zreťazeného bloku: " + block.get("addressNextBlock").getAsString() + "\n"
                        + " faktor: " + block.get("factor").getAsString() + "\n"
                        + " počet platných záznamov: " + block.get("validCount").getAsString() + "\n"
                        + "******************************************************";
                addToConsole(message, State.SUC);
                String rowsHtml = "";
                for (JsonElement jsonElementRecord : block.get("records").getAsJsonArray()) {
                    JsonObject record = (JsonObject) jsonElementRecord;

                    ArrayList<String> dataArr = new ArrayList<>();
                    dataArr.add(record.get("isValid").getAsBoolean() ? "platný" : "neplatný");
                    dataArr.add(record.get("registerNumber").getAsString());
                    dataArr.add(record.get("cadasterName").getAsString());
                    dataArr.add(record.get("address").getAsString());

                    rowsHtml += "\n";
                    rowsHtml += managerHTML.createTableRow(dataArr);
                }

                String[] headerArr = {"Stav záznamu", "Supisné číslo", "Názov katastrálneho územia", "adresa"};
                String headerHtml = managerHTML.createTableHeader(headerArr);
                String tableHtml = managerHTML.createTable(headerHtml, rowsHtml);
                addHtmlComponent(tableHtml);
                addHtmlComponent("<br>");
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        JsonArray result = core.getBlocksRealtyDataFromUnsortedFile();

        if (result == null) {
            addToConsole("V súbore niesu žiadne bloky", State.NON);
        } else {
            for (JsonElement jsonElement : result) {
                JsonObject block = (JsonObject) jsonElement;

                String message = "******************************************************\n"
                        + " adresa: " + block.get("address").getAsString() + "\n"
                        + " faktor: " + block.get("factor").getAsString() + "\n"
                        + " počet platných záznamov: " + block.get("validCount").getAsString() + "\n"
                        + "******************************************************";
                addToConsole(message, State.SUC);
                String rowsHtml = "";
                for (JsonElement jsonElementRecord : block.get("records").getAsJsonArray()) {
                    JsonObject record = (JsonObject) jsonElementRecord;

                    ArrayList<String> dataArr = new ArrayList<>();
                    dataArr.add(record.get("isValid").getAsBoolean() ? "platný" : "neplatný");
                    dataArr.add(record.get("id").getAsString());
                    dataArr.add(record.get("registerNumber").getAsString());
                    dataArr.add(record.get("cadasterName").getAsString());
                    dataArr.add(record.get("desc").getAsString());

                    rowsHtml += "\n";
                    rowsHtml += managerHTML.createTableRow(dataArr);
                }

                String[] headerArr = {"Stav záznamu", "Identifikačné číslo", "Supisné číslo", "Názov katastru", "Popis"};
                String headerHtml = managerHTML.createTableHeader(headerArr);
                String tableHtml = managerHTML.createTable(headerHtml, rowsHtml);
                addHtmlComponent(tableHtml);
                addHtmlComponent("<br>");
            }
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        String id = jTextFieldEditIDRealty.getText();
        String desc = jTextFieldEditDescriptionRealty.getText();
        if (isEmptyTextField(id)
                || isEmptyTextField(desc)) {
            JOptionPane.showMessageDialog(this,
                    "Vyplnte všetky polička označene hviezdičkou.(*)",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (desc.length() > REALTY_DESC_MAX_LENGTH ) {
            JOptionPane.showMessageDialog(this,
                    "Prekročili ste povolenú dížku popisu. Povolená dlžka je " + REALTY_DESC_MAX_LENGTH + " znakov.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = MainApp.tryParseToInteger(id);
        if (result != null) {
            JOptionPane.showMessageDialog(this,
                    "Zadali ste text do poľa určeného pre číslo.",
                    "Pozor",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JsonObject resultObject = core.editRealty(getInt(id), desc);
        if (resultObject.get("err") != null) {
            String message = resultObject.get("err").getAsString() + "\n";
            message += "******************************************************\n"
                    + " identifikačné číslo: " + id + "\n"
                    + " popis: " + desc + "\n"
                    + "******************************************************";
            addToConsole(message, State.ERR);
            return;
        }

        String message = "******************************************************\n"
                + " identifikačné číslo nehnuteľnosti: " + resultObject.get("id").getAsString() + "\n"
                + " súpisné číslo: " + resultObject.get("idRealty").getAsString() + "\n"
                + " názov katastru: " + resultObject.get("cadasterName").getAsString() + "\n"
                + " popis: " + resultObject.get("desc").getAsString() + "\n"
                + "******************************************************";
        addToConsole(message, State.SUC);
    }//GEN-LAST:event_jButton12ActionPerformed

    public static String tryParseToInteger(String term) {
        try {
            Integer.parseInt(term);
            return null;
        } catch (NumberFormatException e) {
            return "Zadali ste text do poľa určeného pre číslo.";
        }
    }

    public static boolean isEmptyTextField(String input) {
        if (input == null || input.isEmpty() || input.trim().equals("")) {
            return true;
        }
        return false;
    }

    private int getInt(String term) {
        return Integer.parseInt(term);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainApp dialog = new MainApp(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }

    private void configTextPane() {
        jTextPaneConsole.setContentType("text/html");
        Font f = new Font(Font.SANS_SERIF, 3, 13);
        jTextPaneConsole.setFont(f);
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = new HTMLDocument();
        jTextPaneConsole.setEditorKit(kit);
        jTextPaneConsole.setDocument(doc);
        jTextPaneConsole.setText("<html><head><style>" + Styles.CSS + "</style></head>");
    }

    private void addHtmlComponent(String component) {
        HTMLEditorKit kit = (HTMLEditorKit) jTextPaneConsole.getEditorKit();
        HTMLDocument doc = (HTMLDocument) jTextPaneConsole.getDocument();
        try {
            kit.insertHTML(doc, doc.getLength(), component, 0, 0, null);

        } catch (BadLocationException ex) {
            Logger.getLogger(MainApp.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldCadasterNameFind;
    private javax.swing.JTextField jTextFieldDescription;
    private javax.swing.JTextField jTextFieldEditDescriptionRealty;
    private javax.swing.JTextField jTextFieldEditIDRealty;
    private javax.swing.JTextField jTextFieldGeneratorRealtyCount;
    private javax.swing.JTextField jTextFieldIDFind;
    private javax.swing.JTextField jTextFieldIDRealty;
    private javax.swing.JTextField jTextFieldIDRealtyInCadaster;
    private javax.swing.JTextField jTextFieldNameCadaster;
    private javax.swing.JTextField jTextFieldRegistrationNumberRealtyFind;
    private javax.swing.JTextPane jTextPaneConsole;
    // End of variables declaration//GEN-END:variables

    public enum State {
        ERR,
        SUC,
        NON
    }

    private void addToConsole(String value, State state) {
        switch (state) {
            case ERR:
                addColoredTextRow(value, Color.RED);
                break;
            case SUC:
                addColoredTextRow(value, new Color(0, 153, 0));
                break;
            case NON:
                addColoredTextRow(value, Color.BLACK);
                break;
        }
        addColoredTextRow("=============================================================================================", Color.BLACK);

    }

    private void addColoredTextRow(String text, Color color) {
        StyledDocument doc = jTextPaneConsole.getStyledDocument();

        Style style = jTextPaneConsole.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);

        try {
            doc.insertString(doc.getLength(), text + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
//        String rowHtml = "<pre style=\"color:" + color + "\">" + text + "</pre>";
//        addHtmlComponent(rowHtml);

    }
}
