public class App {
    public static void main(String[] args) throws Exception {
       import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class InterfazDevolverDinero extends JFrame {

    private Integer[] denominaciones = {50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};
    private int[] existencia = new int[10];
    private int[] devuelta = new int[10];

    private JComboBox<Integer> cmbDenominacion;
    private JTextField txtExistencia;
    private JButton btnActualizar;

    private JTextField txtValorDevolver;
    private JButton btnCalcular;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextArea txtMensaje;

    public InterfazDevolverDinero() {

        setTitle("Caja Registradora");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));

        JPanel panelActualizar = new JPanel();
        cmbDenominacion = new JComboBox<>(denominaciones);
        txtExistencia = new JTextField(8);
        btnActualizar = new JButton("Actualizar");

        panelActualizar.add(new JLabel("Denominación:"));
        panelActualizar.add(cmbDenominacion);
        panelActualizar.add(new JLabel("Existencia:"));
        panelActualizar.add(txtExistencia);
        panelActualizar.add(btnActualizar);

        JPanel panelCalcular = new JPanel();
        txtValorDevolver = new JTextField(10);
        btnCalcular = new JButton("Calcular");

        panelCalcular.add(new JLabel("Valor a devolver:"));
        panelCalcular.add(txtValorDevolver);
        panelCalcular.add(btnCalcular);

        panelSuperior.add(panelActualizar);
        panelSuperior.add(panelCalcular);

        add(panelSuperior, BorderLayout.NORTH);

        // TABLA
        modelo = new DefaultTableModel(
                new Object[]{"Denominación", "Existencia", "Devuelta"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        actualizarTabla();

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // MENSAJE
        txtMensaje = new JTextArea(6, 50);
        txtMensaje.setEditable(false);
        add(new JScrollPane(txtMensaje), BorderLayout.SOUTH);

        // EVENTOS
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarExistencia();
            }
        });

        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularDevuelta();
            }
        });
    }

    private void actualizarExistencia() {

        if (!txtExistencia.getText().isEmpty()) {

            int cantidad = Integer.parseInt(txtExistencia.getText());

            if (cantidad >= 0) {
                int indice = cmbDenominacion.getSelectedIndex();
                existencia[indice] += cantidad;
                actualizarTabla();
                txtExistencia.setText("");
            }
        }
    }

    private void calcularDevuelta() {

        txtMensaje.setText("");

        if (!txtValorDevolver.getText().isEmpty()) {

            int valor = Integer.parseInt(txtValorDevolver.getText());

            for (int i = 0; i < devuelta.length; i++) {
                devuelta[i] = 0;
            }

            int restante = valor;

            for (int i = 0; i < denominaciones.length; i++) {

                if (restante > 0) {

                    int maxBilletes = restante / denominaciones[i];

                    if (maxBilletes > 0) {

                        if (existencia[i] >= maxBilletes) {
                            devuelta[i] = maxBilletes;
                        } else {
                            devuelta[i] = existencia[i];
                        }

                        restante -= devuelta[i] * denominaciones[i];
                        existencia[i] -= devuelta[i];
                    }
                }
            }

            actualizarTabla();

            if (restante == 0) {

                txtMensaje.append("La devuelta se compone de:\n");

                for (int i = 0; i < denominaciones.length; i++) {

                    if (devuelta[i] > 0) {

                        if (denominaciones[i] >= 1000) {
                            txtMensaje.append(devuelta[i] + " billete(s) de " + denominaciones[i] + "\n");
                        } else {
                            txtMensaje.append(devuelta[i] + " moneda(s) de " + denominaciones[i] + "\n");
                        }
                    }
                }

            } else {
                txtMensaje.setText("No hay suficiente dinero para completar la devuelta.");
            }
        }
    }

    private void actualizarTabla() {

        modelo.setRowCount(0);

        for (int i = 0; i < denominaciones.length; i++) {
            modelo.addRow(new Object[]{
                    denominaciones[i],
                    existencia[i],
                    devuelta[i]
            });
        }
    }

    public static void main(String[] args) {
        new InterfazDevolverDinero().setVisible(true);
    }
}

    }
}
