package gui;

import beans.ResultadosEsperados;
import beans.SistemaMatriz;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import helper.MatrizHelper;
import jacobi.Jacobi;
import sidel.Seidel;
import validations.MatrizValidations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private JButton zerarResultadosButton;
    private JTextField max_interacoes;

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
                    return;
                }

                if (!matrizValidations.isDiagonalmenteDominante(sistemaMatriz)) {
                    int resposta = JOptionPane.showConfirmDialog(null, "A matriz não atende o critério das linhas (diagonalmente dominante). Deseja aplicar o Pivotamento de Gauss?", "Diagonalmente Dominante", JOptionPane.YES_NO_OPTION);

                    if (resposta == 0) {

                        matrizHelper.pivotamento(sistemaMatriz);
                        if (!matrizValidations.isDiagonalmenteDominante(sistemaMatriz)) {
                            JOptionPane.showMessageDialog(null, "Nenhuma combinação passou no critério de linhas.");
                            return;
                        }

                        matrizTableModel.fireTableDataChanged();
                    }

                }

                JOptionPane.showMessageDialog(null, "A matriz passou em todas as validações! (Quadrada e diagonalmente dominante)");
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
                int maxInteracoes = Integer.parseInt(max_interacoes.getText());
                int interarJacobi = 1;
                int interarSeidel = 1;

                while (maxInteracoes > 0 && (interarJacobi == 1 || interarSeidel == 1)) {
                    maxInteracoes--;

                    if (interarJacobi == 1) {
                        nextJacobi();
                    }
                    if (interarSeidel == 1) {
                        nextSeidel();
                    }
                    switch (parada.getSelectedIndex()) {

                        case 0:
                            interarJacobi = jacobiErrosTM.getMaxVariacao().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMaxVariacao().compareTo(stop);
                            break;

                        case 1:
                            interarJacobi = jacobiErrosTM.getMaxErroAbsoluto().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMaxErroAbsoluto().compareTo(stop);
                            break;

                        case 2:
                            interarJacobi = jacobiErrosTM.getMaxErroRelativo().compareTo(stop);
                            interarSeidel = seidelErrosTM.getMaxErroRelativo().compareTo(stop);
                            break;
                    }
                }
            }
        });
        zerarResultadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJacobi(sistemaMatriz);
                iniciarSeidel(sistemaMatriz);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelForm = new JPanel();
        panelForm.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.setName("panelForm");
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        label1 = new JLabel();
        label1.setText("Número de variáveis:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guiNroVariaveis = new JTextField();
        guiNroVariaveis.setText("3");
        panel1.add(guiNroVariaveis, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonGerar = new JButton();
        buttonGerar.setLabel("Gerar Matriz");
        buttonGerar.setText("Gerar Matriz");
        panel1.add(buttonGerar, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonValidar = new JButton();
        buttonValidar.setLabel("Validar Matriz");
        buttonValidar.setText("Validar Matriz");
        panel1.add(buttonValidar, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guiTipoMatriz = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Zerada");
        defaultComboBoxModel1.addElement("Aleatória");
        defaultComboBoxModel1.addElement("Válida");
        defaultComboBoxModel1.addElement("Inválida");
        guiTipoMatriz.setModel(defaultComboBoxModel1);
        panel1.add(guiTipoMatriz, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, new Dimension(-1, 185), 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Sistema de funções "));
        scroll1 = new JScrollPane();
        panel2.add(scroll1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(453, 185), null, 0, false));
        guiTable = new JTable();
        guiTable.setEditingColumn(-1);
        scroll1.setViewportView(guiTable);
        panel_resultados = new JPanel();
        panel_resultados.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(panel_resultados, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel_jacobi = new JPanel();
        panel_jacobi.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel_resultados.add(panel_jacobi, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel_jacobi.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Gauss-Jacobi"));
        jscrollpaneJacobi = new JScrollPane();
        panel_jacobi.add(jscrollpaneJacobi, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 180), null, 0, false));
        resultados_jacobi = new JTable();
        jscrollpaneJacobi.setViewportView(resultados_jacobi);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel_jacobi.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 80), null, 0, false));
        jacobi_erros = new JTable();
        scrollPane1.setViewportView(jacobi_erros);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel_resultados.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Gauss-Seidel"));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel3.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 180), null, 0, false));
        resultados_seidel = new JTable();
        scrollPane2.setViewportView(resultados_seidel);
        final JScrollPane scrollPane3 = new JScrollPane();
        panel3.add(scrollPane3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 80), null, 0, false));
        seidel_erros = new JTable();
        scrollPane3.setViewportView(seidel_erros);
        btn_resultados = new JPanel();
        btn_resultados.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(btn_resultados, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        n1Button = new JButton();
        n1Button.setText("N+1");
        btn_resultados.add(n1Button, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        btn_resultados.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Precisão e arredondamento:");
        btn_resultados.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guiPrecisao = new JTextField();
        guiPrecisao.setText("4");
        btn_resultados.add(guiPrecisao, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(25, -1), null, 0, false));
        guiArredondamento = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        guiArredondamento.setModel(defaultComboBoxModel2);
        btn_resultados.add(guiArredondamento, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zerarResultadosButton = new JButton();
        zerarResultadosButton.setText("Zerar resultados");
        btn_resultados.add(zerarResultadosButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel_esperados = new JPanel();
        panel_esperados.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(panel_esperados, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 63), null, 0, false));
        panel_esperados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Resultados esperados"));
        final JScrollPane scrollPane4 = new JScrollPane();
        panel_esperados.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 40), null, 0, false));
        esperadosTable = new JTable();
        scrollPane4.setViewportView(esperadosTable);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panelForm.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buscarResultadoButton = new JButton();
        buscarResultadoButton.setText("Buscar resultado");
        panel4.add(buscarResultadoButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Critério de parada:");
        panel4.add(label3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        parada = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("Variação");
        defaultComboBoxModel3.addElement("Erro absoluto");
        defaultComboBoxModel3.addElement("Erro relativo");
        parada.setModel(defaultComboBoxModel3);
        panel4.add(parada, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paradaValue = new JTextField();
        paradaValue.setText("0.0001");
        panel4.add(paradaValue, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, -1), new Dimension(75, -1), 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Máximo de interações:");
        panel4.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        max_interacoes = new JTextField();
        max_interacoes.setText("50");
        panel4.add(max_interacoes, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), new Dimension(30, -1), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelForm;
    }
}
