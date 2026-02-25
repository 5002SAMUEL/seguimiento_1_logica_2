// ================= IMPORTACIONES =================

// Librería para crear interfaces gráficas (ventanas, botones, tablas, etc.)
import javax.swing.*;

// Modelo que permite manejar los datos de la tabla
import javax.swing.table.DefaultTableModel;

// Permite usar layouts como BorderLayout y GridLayout
import java.awt.*;

// Permite manejar eventos como clic en botones
import java.awt.event.*;


// ================= CLASE PRINCIPAL =================

// La clase se llama InterfazDevolverDinero
// Extiende JFrame, lo que significa que esta clase ES una ventana
public class InterfazDevolverDinero extends JFrame {

    // ================= ATRIBUTOS (VARIABLES GLOBALES DE LA CLASE) =================
    // Estas variables son globales dentro de la clase.
    // Se pueden usar en cualquier método.

    // Arreglo que guarda las denominaciones de billetes y monedas
    private Integer[] denominaciones = {50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};

    // Arreglo que guarda cuántos billetes/monedas existen en la caja
    private int[] existencia = new int[10];

    // Arreglo que guarda cuántos billetes/monedas se devuelven como cambio
    private int[] devuelta = new int[10];

    // Componentes gráficos
    private JComboBox<Integer> cmbDenominacion; // Lista desplegable de denominaciones
    private JTextField txtExistencia;           // Campo para escribir cantidad a agregar
    private JButton btnActualizar;              // Botón para actualizar existencia

    private JTextField txtValorDevolver;        // Campo para escribir valor a devolver
    private JButton btnCalcular;                // Botón para calcular cambio

    private JTable tabla;                       // Tabla visual
    private DefaultTableModel modelo;           // Modelo que maneja los datos de la tabla

    private JTextArea txtMensaje;               // Área donde se muestra el resultado


    // ================= CONSTRUCTOR =================
    // Este método se ejecuta cuando se crea la ventana

    public InterfazDevolverDinero() {

        // Título de la ventana
        setTitle("Caja Registradora");

        // Tamaño de la ventana
        setSize(700, 600);

        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);

        // Hace que el programa termine cuando se cierre la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define el layout principal
        setLayout(new BorderLayout());


        // ================= PANEL SUPERIOR =================

        // Panel que contiene los dos subpaneles
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));

        // -------- PANEL ACTUALIZAR EXISTENCIA --------

        JPanel panelActualizar = new JPanel();

        // ComboBox con las denominaciones
        cmbDenominacion = new JComboBox<>(denominaciones);

        // Campo para escribir cantidad
        txtExistencia = new JTextField(8);

        // Botón actualizar
        btnActualizar = new JButton("Actualizar");

        // Se agregan los componentes al panel
        panelActualizar.add(new JLabel("Denominación:"));
        panelActualizar.add(cmbDenominacion);
        panelActualizar.add(new JLabel("Existencia:"));
        panelActualizar.add(txtExistencia);
        panelActualizar.add(btnActualizar);

        // -------- PANEL CALCULAR DEVUELTA --------

        JPanel panelCalcular = new JPanel();

        txtValorDevolver = new JTextField(10);
        btnCalcular = new JButton("Calcular");

        panelCalcular.add(new JLabel("Valor a devolver:"));
        panelCalcular.add(txtValorDevolver);
        panelCalcular.add(btnCalcular);

        // Se agregan los dos paneles al panel superior
        panelSuperior.add(panelActualizar);
        panelSuperior.add(panelCalcular);

        // Se agrega el panel superior a la parte norte de la ventana
        add(panelSuperior, BorderLayout.NORTH);


        // ================= TABLA =================

        // Se crea el modelo con las columnas
        modelo = new DefaultTableModel(
                new Object[]{"Denominación", "Existencia", "Devuelta"}, 0) {

            // Este método evita que las celdas se puedan editar
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Se crea la tabla con el modelo
        tabla = new JTable(modelo);

        // Se llena la tabla por primera vez
        actualizarTabla();

        // Se agrega la tabla al centro de la ventana
        add(new JScrollPane(tabla), BorderLayout.CENTER);


        // ================= ÁREA DE MENSAJE =================

        txtMensaje = new JTextArea(6, 50);

        // Evita que el usuario pueda escribir
        txtMensaje.setEditable(false);

        add(new JScrollPane(txtMensaje), BorderLayout.SOUTH);


        // ================= EVENTOS =================

        // Cuando se presiona el botón actualizar
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarExistencia();
            }
        });

        // Cuando se presiona el botón calcular
        btnCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calcularDevuelta();
            }
        });
    }


    // ================= MÉTODO ACTUALIZAR EXISTENCIA =================
    // Este método agrega dinero a la caja

    private void actualizarExistencia() {

        // Verifica que el campo no esté vacío
        if (!txtExistencia.getText().isEmpty()) {

            // Convierte el texto a número
            int cantidad = Integer.parseInt(txtExistencia.getText());

            // Solo permite cantidades positivas
            if (cantidad >= 0) {

                // Obtiene la posición seleccionada en el combo
                int indice = cmbDenominacion.getSelectedIndex();

                // Suma la cantidad a la existencia actual
                existencia[indice] += cantidad;

                // Actualiza la tabla
                actualizarTabla();

                // Limpia el campo
                txtExistencia.setText("");
            }
        }
    }


    // ================= MÉTODO CALCULAR DEVUELTA =================
    // Calcula el cambio usando un algoritmo voraz (greedy)

    private void calcularDevuelta() {

        // Limpia el mensaje anterior
        txtMensaje.setText("");

        // Verifica que el campo no esté vacío
        if (!txtValorDevolver.getText().isEmpty()) {

            int valor = Integer.parseInt(txtValorDevolver.getText());

            // Reinicia el arreglo devuelta
            for (int i = 0; i < devuelta.length; i++) {
                devuelta[i] = 0;
            }

            int restante = valor;

            // Recorre todas las denominaciones
            for (int i = 0; i < denominaciones.length; i++) {

                if (restante > 0) {

                    // Calcula el máximo posible de billetes
                    int maxBilletes = restante / denominaciones[i];

                    if (maxBilletes > 0) {

                        // Si hay suficiente existencia
                        if (existencia[i] >= maxBilletes) {
                            devuelta[i] = maxBilletes;
                        } else {
                            // Si no hay suficiente, usa lo que haya
                            devuelta[i] = existencia[i];
                        }

                        // Resta el dinero entregado
                        restante -= devuelta[i] * denominaciones[i];

                        // Reduce la existencia en caja
                        existencia[i] -= devuelta[i];
                    }
                }
            }

            // Actualiza la tabla
            actualizarTabla();

            // Si el cambio fue exacto
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


    // ================= MÉTODO ACTUALIZAR TABLA =================
    // Recarga la tabla con los valores actuales

    private void actualizarTabla() {

        // Borra todas las filas
        modelo.setRowCount(0);

        // Vuelve a llenar la tabla
        for (int i = 0; i < denominaciones.length; i++) {
            modelo.addRow(new Object[]{
                    denominaciones[i],
                    existencia[i],
                    devuelta[i]
            });
        }
    }


    // ================= MÉTODO MAIN =================
    // Punto de inicio del programa

    public static void main(String[] args) {

        // Crea la ventana y la hace visible
        new InterfazDevolverDinero().setVisible(true);
    }
}