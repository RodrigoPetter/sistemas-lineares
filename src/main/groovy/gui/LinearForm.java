package gui;

import beans.SistemaMatriz;
import helper.MatrizHelper;
import jacobi.Jacobi;
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
    private JPanel panel_resultados;
    private JPanel panel_jacobi;
    private JPanel btn_resultados;
    private JButton n1Button;
    private JButton resultadoFinalButton;
    private JTable resultados_jacobi;
    private JTable resultados_seidel;
    private JScrollPane jscrollpaneJacobi;
    private JScrollPane scroll1;
    private JLabel label1;

    private SistemaMatriz sistemaMatriz;
    private MatrizHelper matrizHelper = new MatrizHelper();
    private MatrizTableModel matrizTableModel;
    private SolucoesTableModel jacobiTableModel;

    private MatrizValidations matrizValidations = new MatrizValidations();

    private Jacobi jacobi;

    public LinearForm() {
        buttonGerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer nroVariaveis = Integer.parseInt(guiNroVariaveis.getText());

                switch (guiTipoMatriz.getSelectedIndex()) {
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

                ininiarJacobi(sistemaMatriz);
            }
        });
        buttonValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //matrizValidations.allValidations(sistemaMatriz);
            }
        });
        n1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jacobi.getNextSolucao();
                jacobiTableModel.fireTableDataChanged();
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

    private void ininiarJacobi(SistemaMatriz sistemaMatriz) {
        jacobi = new Jacobi(sistemaMatriz);
        jacobiTableModel = new SolucoesTableModel(jacobi.getSolucoes());
        resultados_jacobi.setModel(jacobiTableModel);
    }
}
