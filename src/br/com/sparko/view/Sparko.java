/*
 * Copyright (C) 2022 Kevin Fernando de Moura Moises
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.sparko.view;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Classe principal do software.
 *
 * @author Kevin Fernando de Moura Moises
 * @version 1.1x
 */
public class Sparko extends javax.swing.JFrame {
    private boolean logCriado = false;
    
    public Sparko() {
        initComponents();
        initUi();
    }
    
    private void initUi() {
        FlatIntelliJLaf.setup();
        FlatIntelliJLaf.updateUI();
        this.setTitle("SparkoEncrypter");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        senha.putClientProperty("JComponent.roundRect", true);
        boxCodificacao.putClientProperty("JComponent.roundRect", true);
        btnEncriptar.putClientProperty("JButton.buttonType", 
                "roundRect");
        btnDesencriptar.putClientProperty("JButton.buttonType", 
                "roundRect");
        btnLimpar.putClientProperty("JButton.buttonType", "roundRect");
        btnCaixaBaixa.putClientProperty("JButton.buttonType", 
                "roundRect");
        btnCaixaAlta.putClientProperty("JButton.buttonType", 
                "roundRect");
        btnExportarLogAcoes.putClientProperty("JButton.buttonType", 
                "roundRect");
        btnCopiar.putClientProperty("JButton.buttonType", "roundRect");
        console.setForeground(Color.white);
        console.setBackground(Color.black);
    }
    
    private void abrirGitHub() {
        try {
            URI link = new URI("www.github.com/Kevin-Moises/SparkoEncrypter"
                    + ".git");
            Desktop.getDesktop().browse(link);
        } 
        catch (IOException | URISyntaxException e) {
            console.setText("Falha ao lançar navegador web:\n" + e);
        }
    }
    
    private void testeCriptografia() {
        final String str = new String(senha.getPassword());
        
        if(str.isEmpty())
            JOptionPane.showMessageDialog(null, 
                    "Não é possível converter sem uma senha!", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        else if(boxCodificacao.getSelectedItem().equals(" "))
            JOptionPane.showMessageDialog(null, 
                    "Não é possível converter sem um método de conversão!", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        else {
            if(boxCodificacao.getSelectedItem().toString().equals(
                "SHA-256")) {
            Object[] option = {"Sim", "Não"};
            
            if(JOptionPane.showOptionDialog(null, 
                    "O método selecionado foi SHA-256!\nApós criptografar não "
                            + "será possível reverter o processo!\nDeseja "
                            + "continuar?", 
                    "Atenção", JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.WARNING_MESSAGE, null, 
                    option, option[0]) == 0) {
                criptografar(str);
            }
            else
                limparCampos();
        } else 
            criptografar(str);
        }
    }
    
    private void criptografar(String str) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance(
                    boxCodificacao.getSelectedItem().toString());
            byte messageDigest[] = algorithm.digest(str.getBytes(
                        "UTF-8"));

            StringBuilder hexString = new StringBuilder();

            for(byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }

            final String senhaSegura = hexString.toString();
            
            if(!logCriado)
                logAcoes();
            else
                editLogAcoes();
            
            console.setText(senhaSegura);
        } 
        catch (NoSuchAlgorithmException | UnsupportedEncodingException  e) {
        }
    }
    
    private void descriptografar(String str) {
        //A ser implementado junto a métodos de criptografia bidirecionais.
    }
    
    private void exportarLogAcoes() {
        //A ser implementado.
    }
    
    private void logAcoes() {
        Date data = new Date();
        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        
        try {
            try (FileWriter fw = new FileWriter(
                    "C:\\Users\\kevin.moises\\Documents\\NetBeansProjects"
                            + "\\SparkoCripto\\logs\\logs.txt")) {
                PrintWriter gravar = new PrintWriter(fw);
                
                gravar.printf("Ação de encriptação realizada em " +
                        format.format(data));
                logCriado = true;
            }
        } 
        catch (IOException e) {
            System.out.println("Erro ao criar log de atividade:\n" + e);
        }
    }
    
    private void editLogAcoes() {
        Date data = new Date();
        DateFormat format = DateFormat.getDateInstance(DateFormat.DEFAULT);
        
        try {
            try (FileWriter fw = new FileWriter("C:\\Users\\kevin.moises\\Documents"
                    + "\\NetBeansProjects\\SparkoCripto\\logs\\logs.txt",
                    true)) {
                fw.write("\nAção de encriptação realizada em " +
                        format.format(data));
            }
        } catch (IOException e) {
            System.out.println("Erro ao criar log de atividade:\n" + e);
        }
    }
    
    private void converterCaixaBaixa(String str) {
        if(str.isEmpty())
            JOptionPane.showMessageDialog(null, 
                    "Primero é necessário converter a sua senha!", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        else
            console.setText(str.toLowerCase());
    }
    
    private void converterCaixaAlta(String str) {
        if(str.isEmpty())
            JOptionPane.showMessageDialog(null, 
                    "Primero é necessário converter a sua senha!", 
                    "Atenção", JOptionPane.WARNING_MESSAGE);
        else
            console.setText(str.toUpperCase());
    }
    
    private void copiar() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        final String text = console.getText();
        
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
        
        JOptionPane.showMessageDialog(null, 
                "Copiado para a Área de Transferência!", 
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparCampos() {
        senha.setText("");
        boxCodificacao.setSelectedItem(" ");
        console.setText("");
        senha.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelInfo1 = new javax.swing.JLabel();
        labelInfo2 = new javax.swing.JLabel();
        labelInfo3 = new javax.swing.JLabel();
        labelInfo4 = new javax.swing.JLabel();
        labelInfo5 = new javax.swing.JLabel();
        labelInfo6 = new javax.swing.JLabel();
        labelInfo7 = new javax.swing.JLabel();
        labelInfo8 = new javax.swing.JLabel();
        labelInfo9 = new javax.swing.JLabel();
        labelAbrirGitHub = new javax.swing.JLabel();
        labelInfo11 = new javax.swing.JLabel();
        senha = new javax.swing.JPasswordField();
        labelInfo12 = new javax.swing.JLabel();
        boxCodificacao = new javax.swing.JComboBox<>();
        labelInfo13 = new javax.swing.JLabel();
        btnEncriptar = new javax.swing.JButton();
        labelInfo14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        btnLimpar = new javax.swing.JButton();
        btnCaixaBaixa = new javax.swing.JButton();
        btnCopiar = new javax.swing.JButton();
        btnCaixaAlta = new javax.swing.JButton();
        btnDesencriptar = new javax.swing.JButton();
        btnExportarLogAcoes = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelInfo1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        labelInfo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInfo1.setText("SparkoEncrypter");

        labelInfo2.setText("Olá, seja bem vindo ao Sparko!");

        labelInfo3.setText("A função deste software é encriptar suas senhas, tornando-as mais seguras");

        labelInfo4.setText("para o seu uso. Portanto, siga as instruções abaixo:");

        labelInfo5.setText("1. Insira a senha na caixa de texto abaixo.");

        labelInfo6.setText("3. Clique no botão Encriptar.");

        labelInfo7.setText("4. Observe o resultado no console abaixo.");

        labelInfo8.setText("É pronto! É só isso, para mais funções e detalhes sobre o software visite o ");

        labelInfo9.setText("repositório do GitHub clicando");

        labelAbrirGitHub.setForeground(new java.awt.Color(0, 0, 255));
        labelAbrirGitHub.setText("Aqui!");
        labelAbrirGitHub.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelAbrirGitHubMouseClicked(evt);
            }
        });

        labelInfo11.setText("Insira a sua senha:");

        labelInfo12.setText("Selecione o método de codificação:");

        boxCodificacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "SHA-256" }));

        labelInfo13.setText("2. Selecione o método de codificação.");

        btnEncriptar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnEncriptar.setText("Encriptar!");
        btnEncriptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncriptarActionPerformed(evt);
            }
        });

        labelInfo14.setText("Resultado:");

        console.setColumns(20);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        btnLimpar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnCaixaBaixa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sparko/img/seta-curva-para-baixo.png"))); // NOI18N
        btnCaixaBaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaixaBaixaActionPerformed(evt);
            }
        });

        btnCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sparko/img/interface.png"))); // NOI18N
        btnCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopiarActionPerformed(evt);
            }
        });

        btnCaixaAlta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sparko/img/seta-curva-para-cima.png"))); // NOI18N
        btnCaixaAlta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaixaAltaActionPerformed(evt);
            }
        });

        btnDesencriptar.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        btnDesencriptar.setText("Desencriptar!");

        btnExportarLogAcoes.setText("Exportar log de ações");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelInfo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(labelInfo8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDesencriptar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEncriptar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelInfo11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(senha))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelInfo12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(boxCodificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelInfo2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(labelInfo4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelInfo3, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(labelInfo5)
                            .addComponent(labelInfo6)
                            .addComponent(labelInfo7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelInfo9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelAbrirGitHub))
                            .addComponent(labelInfo13)
                            .addComponent(labelInfo14))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCaixaBaixa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCaixaAlta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExportarLogAcoes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCopiar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelInfo1)
                .addGap(18, 18, 18)
                .addComponent(labelInfo2)
                .addGap(18, 18, 18)
                .addComponent(labelInfo3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfo4)
                .addGap(18, 18, 18)
                .addComponent(labelInfo5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfo13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfo6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInfo7)
                .addGap(18, 18, 18)
                .addComponent(labelInfo8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelInfo9)
                    .addComponent(labelAbrirGitHub))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelInfo11)
                    .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelInfo12)
                    .addComponent(boxCodificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEncriptar)
                    .addComponent(btnLimpar)
                    .addComponent(btnDesencriptar))
                .addGap(12, 12, 12)
                .addComponent(labelInfo14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCopiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCaixaAlta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnExportarLogAcoes))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCaixaBaixa)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void labelAbrirGitHubMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelAbrirGitHubMouseClicked
        abrirGitHub();
    }//GEN-LAST:event_labelAbrirGitHubMouseClicked

    private void btnEncriptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncriptarActionPerformed
        testeCriptografia();
    }//GEN-LAST:event_btnEncriptarActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limparCampos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnCaixaBaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCaixaBaixaActionPerformed
        converterCaixaBaixa(console.getText());
    }//GEN-LAST:event_btnCaixaBaixaActionPerformed

    private void btnCaixaAltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCaixaAltaActionPerformed
        converterCaixaAlta(console.getText());
    }//GEN-LAST:event_btnCaixaAltaActionPerformed

    private void btnCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopiarActionPerformed
        copiar();
    }//GEN-LAST:event_btnCopiarActionPerformed

    public static void main(String args[]) {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sparko.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(() -> {
            new Sparko().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> boxCodificacao;
    private javax.swing.JButton btnCaixaAlta;
    private javax.swing.JButton btnCaixaBaixa;
    private javax.swing.JButton btnCopiar;
    private javax.swing.JButton btnDesencriptar;
    private javax.swing.JButton btnEncriptar;
    private javax.swing.JButton btnExportarLogAcoes;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JTextArea console;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAbrirGitHub;
    private javax.swing.JLabel labelInfo1;
    private javax.swing.JLabel labelInfo11;
    private javax.swing.JLabel labelInfo12;
    private javax.swing.JLabel labelInfo13;
    private javax.swing.JLabel labelInfo14;
    private javax.swing.JLabel labelInfo2;
    private javax.swing.JLabel labelInfo3;
    private javax.swing.JLabel labelInfo4;
    private javax.swing.JLabel labelInfo5;
    private javax.swing.JLabel labelInfo6;
    private javax.swing.JLabel labelInfo7;
    private javax.swing.JLabel labelInfo8;
    private javax.swing.JLabel labelInfo9;
    private javax.swing.JPasswordField senha;
    // End of variables declaration//GEN-END:variables
}