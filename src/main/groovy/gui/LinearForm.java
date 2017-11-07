package gui;

import beans.SistemaMatriz;
import helper.MatrizHelper;
import validations.MatrizValidations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinearForm {
    private JPanel panelForm;
    private JTextField guiNroVariaveis;
    private JButton buttonGerar;
    private JTable guiTable;
    private JButton buttonValidar;
    private JPanel panel1;
    private JPanel panel2;
    private JComboBox guiTipoMatriz;

    private SistemaMatriz sistemaMatriz;
    private MatrizHelper matrizHelper = new MatrizHelper();
    private MatrizTableModel matrizTableModel;

    private MatrizValidations matrizValidations = new MatrizValidations();

    public LinearForm() {
        buttonGerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer nroVariaveis = Integer.parseInt(guiNroVariaveis.getText());

                switch (guiTipoMatriz.getSelectedIndex()){
                    case 0:
                        sistemaMatriz = new SistemaMatriz(nroVariaveis);
                        break;
                    case 1:
                        sistemaMatriz = matrizHelper.getNewMatrizWithRandomValues(nroVariaveis);
                        break;
                    case 2:
                        sistemaMatriz = matrizHelper.getValidMatriz();
                        break;
                    case 3:
                        sistemaMatriz = matrizHelper.getInvalidMatriz();
                        break;
                }

                matrizTableModel = new MatrizTableModel(sistemaMatriz);
                guiTable.setModel(matrizTableModel);

            }
        });
        buttonValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //matrizValidations.allValidations(sistemaMatriz);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LinearForm");
        frame.setContentPane(new LinearForm().panelForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
