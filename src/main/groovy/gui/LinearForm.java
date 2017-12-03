package gui;

import beans.ResultadosEsperados;
import beans.SistemaMatriz;
import helper.MatrizHelper;
import jacobi.Jacobi;
import sidel.Seidel;
import validations.MatrizValidations;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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
    private JTable resultados_jacobi;
    private JTable resultados_seidel;
    private JScrollPane jscrollpaneJacobi;
    private JScrollPane scroll1;
    private JLabel label1;
    private JTextField guiPrecisao;
    private JComboBox guiArredondamento;
    private JPanel panel_esperados;
    private JTable esperadosTable;
    private JTable jacobi_erros;
    private JTable seidel_erros;
    private JButton buscarResultadoButton;
    private JComboBox parada;
    private JTextField paradaValue;

    private SistemaMatriz sistemaMatriz;
    private MatrizHelper matrizHelper = new MatrizHelper();
    private MatrizTableModel matrizTableModel;
    private EsperadosTableModel esperadoTableModel;
    private SolucoesTableModel jacobiTableModel;
    private SolucoesTableModel seidelTableModel;
    private ErrosTableModel jacobiErrosTM;
    private ErrosTableModel seidelErrosTM;


    private MatrizValidations matrizValidations = new MatrizValidations();

    private Jacobi jacobi;
    private Seidel seidel;
    private ResultadosEsperados resultadosEsperados;

    public LinearForm() {

        guiArredondamento.setModel(new DefaultComboBoxModel(RoundingMode.values()));
        guiArredondamento.setSelectedIndex(4);

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

                iniciarEsperados(nroVariaveis);
                iniciarJacobi(sistemaMatriz);
                iniciarSeidel(sistemaMatriz);

            }
        });
        buttonValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!matrizValidations.isQuadrada(sistemaMatriz)) {
                    JOptionPane.showMessageDialog(null, "A matriz não é quadrada!");
                }

                if (!matrizValidations.isDiagonalmenteDominante(sistemaMatriz)) {
                    int resposta = JOptionPane.showConfirmDialog(null, "A matriz não atenden o critério das linhas (diagonalmente dominante). Deseja aplicar o Pivotamento de Gauss?", "Diagonalmente Dominante", JOptionPane.YES_NO_OPTION);

                    if (resposta == 0) {

                        matrizHelper.pivotamento(sistemaMatriz);
                        if (!matrizValidations.isDiagonalmenteDominante(sistemaMatriz)) {
                            JOptionPane.showMessageDialog(null, "Nenhuma combinação passou no critério de linhas.");
                        }

                        matrizTableModel.fireTableDataChanged();
                    }

                }
            }

        });
        n1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nextSolucao();

            }
        });
        buscarResultadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BigDecimal stop = new BigDecimal(paradaValue.getText());
                int interarJacobi = 1;
                int interarSeidel = 1;

                while (interarJacobi == 1 || interarSeidel == 1) {
                    if(interarJacobi==1){
                        nextJacobi();
                    }
                    if(interarSeidel==1){
                        nextSeidel();
                    }
                    switch (parada.getSelectedIndex()) {

                        case 0:
                            interarJacobi = jacobiErrosTM.getMinVariacao().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMinVariacao().compareTo(stop);
                            break;

                        case 1:
                            interarJacobi = jacobiErrosTM.getMinErroAbsoluto().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMinErroAbsoluto().compareTo(stop);
                            break;

                        case 2:
                            interarJacobi = jacobiErrosTM.getMinErroRelativo().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMinErroRelativo().compareTo(stop);
                            break;
                    }
                }
            }
        });
    }

    private void nextSolucao() {
        nextJacobi();
        nextSeidel();
    }

    private void nextJacobi() {
        Integer precisao = Integer.parseInt(guiPrecisao.getText());
        RoundingMode roundingMode = RoundingMode.valueOf(guiArredondamento.getSelectedIndex());

        jacobi.getNextSolucao(precisao, roundingMode);
        jacobiTableModel.fireTableDataChanged();

        jacobiErrosTM.fireTableDataChanged();
    }

    private void nextSeidel() {
        Integer precisao = Integer.parseInt(guiPrecisao.getText());
        RoundingMode roundingMode = RoundingMode.valueOf(guiArredondamento.getSelectedIndex());

        seidel.getNextSolucao(precisao, roundingMode);
        seidelTableModel.fireTableDataChanged();

        seidelErrosTM.fireTableDataChanged();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LinearForm");
        frame.setContentPane(new LinearForm().panelForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void iniciarJacobi(SistemaMatriz sistemaMatriz) {
        jacobi = new Jacobi(sistemaMatriz);
        jacobiTableModel = new SolucoesTableModel(jacobi.getSolucoes());
        resultados_jacobi.setModel(jacobiTableModel);

        jacobiErrosTM = new ErrosTableModel(jacobi.getSolucoes(), resultadosEsperados);
        jacobi_erros.setModel(jacobiErrosTM);
    }

    private void iniciarSeidel(SistemaMatriz sistemaMatriz) {
        seidel = new Seidel(sistemaMatriz);
        seidelTableModel = new SolucoesTableModel(seidel.getSolucoes());
        resultados_seidel.setModel(seidelTableModel);

        seidelErrosTM = new ErrosTableModel(seidel.getSolucoes(), resultadosEsperados);
        seidel_erros.setModel(seidelErrosTM);
    }

    private void iniciarEsperados(Integer nroVariaveis) {
        resultadosEsperados = new ResultadosEsperados(nroVariaveis);
        esperadoTableModel = new EsperadosTableModel(resultadosEsperados, matrizTableModel);
        esperadosTable.setModel(esperadoTableModel);
    }
}
