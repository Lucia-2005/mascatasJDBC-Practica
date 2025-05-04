package t8_ex_mascotas;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mascota {

	private JFrame frame;
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtEdad;
	private JTextField txtInicio;
	private JTextField txtFinal;
	private JTextField txtNacimiento;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mascota window = new Mascota();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	static boolean comprobarExpReg(String cadena, String patron) {
		Pattern pat=Pattern.compile(patron);
		Matcher mat=pat.matcher(cadena);
		
		if(mat.matches()) {
			return true;
		}else {
			return false;
		}
	}
	
	

	/**
	 * Create the application.
	 */
	public Mascota() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 692, 371);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(28, 21, 70, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Nombre:");
		lblNewLabel_1.setBounds(28, 46, 70, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Edad:");
		lblNewLabel_2.setBounds(28, 75, 70, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Fecha Inicio:");
		lblNewLabel_3.setBounds(28, 125, 70, 14);
		frame.getContentPane().add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Fecha Final:");
		lblNewLabel_4.setBounds(28, 150, 70, 14);
		frame.getContentPane().add(lblNewLabel_4);
		JLabel lblNewLabel_5 = new JLabel("Fecha Nacimiento:");
		lblNewLabel_5.setBounds(28, 100, 70, 14);
		frame.getContentPane().add(lblNewLabel_5);
		
		txtNacimiento = new JTextField();
		txtNacimiento.setBounds(108, 97, 96, 20);
		frame.getContentPane().add(txtNacimiento);
		txtNacimiento.setColumns(10);
		
		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setBounds(108, 18, 96, 20);
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(108, 43, 96, 20);
		frame.getContentPane().add(txtNombre);
		txtNombre.setColumns(10);
		
		txtEdad = new JTextField();
		txtEdad.setBounds(108, 72, 96, 20);
		frame.getContentPane().add(txtEdad);
		txtEdad.setColumns(10);
		
		txtInicio = new JTextField();
		txtInicio.setBounds(108, 122, 96, 20);
		frame.getContentPane().add(txtInicio);
		txtInicio.setColumns(10);
		
		txtFinal = new JTextField();
		txtFinal.setBounds(108, 147, 96, 20);
		frame.getContentPane().add(txtFinal);
		txtFinal.setColumns(10);
		
		//MODELO
		DefaultTableModel modelo= new DefaultTableModel();
		modelo.addColumn("ID");
		modelo.addColumn("Nombre");
		modelo.addColumn("Edad");
		modelo.addColumn("Fecha");
		modelo.addColumn("Inicial-Final");
		
		//AÑADE LAS FILAS A LA TABLA
			//si esta entre parentesis no hace falta cerrarlo
		refrescarTabla(modelo);
		
		table = new JTable(modelo);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TableModel model=table.getModel();
				int index=table.getSelectedRow();
				//coger id de la tabla para pasarlo a txtID
				String id=model.getValueAt(index, 0).toString();
				
				txtId.setText(id);
				txtNombre.setText(model.getValueAt(index, 1).toString());
				txtEdad.setText(model.getValueAt(index, 2).toString());
				txtNacimiento.setText(model.getValueAt(index, 3).toString());
				
				try(Connection con =Conexion.getConnection();){
					PreparedStatement selPstmt=con.prepareStatement("SELECT * FROM mascota WHERE idmascota=?");
					selPstmt.setInt(1, Integer.parseInt(id));
					ResultSet rs=selPstmt.executeQuery();
					while(rs.next()) {
						int diaI=rs.getInt("diaInicial");
						int diaF=rs.getInt("diaFinal");
						txtInicio.setText(Integer.toString(diaI));
						txtFinal.setText(Integer.toString(diaF));
					}
					
					rs.close();
					selPstmt.close();
				}catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		table.setBounds(301, 41, 291, 149);
				
		JScrollPane scrollPane= new JScrollPane(table);
		scrollPane.setBounds(299, 41, 293, 149);
		frame.getContentPane().add(scrollPane);
		
		//AÑADIR
		JButton btnAdd = new JButton("Añadir");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//recoger datos
				String nombre=txtNombre.getText();
				int edad=Integer.parseInt( txtEdad.getText());
				LocalDate fechaN=LocalDate.parse(txtNacimiento.getText());
				int dinicio=Integer.parseInt( txtInicio.getText());
				int dfinal =Integer.parseInt( txtFinal.getText());
				
				if(nombre.isEmpty()) {
					JOptionPane.showMessageDialog(null, "El nombre está vacio");
				}else if(txtEdad.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "La edad está vacia");
				}else if(txtNacimiento.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "La fecha está vacia");
				}else if(txtInicio.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "el dia está vacio");
				}else if(txtFinal.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "el dia está vacio");
				}else if(!comprobarExpReg(txtEdad.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else if(!comprobarExpReg(txtNacimiento.getText(), "^[12]\\d{3}\\-[01][0-9]\\-[0-3][0-9]$")) {
					JOptionPane.showMessageDialog(null, "ingresa una fecha en formato YYYY-MM-DD");
				}else if(!comprobarExpReg(txtInicio.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else if(!comprobarExpReg(txtFinal.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else {
					//comenzar la consulta
					try(Connection con =Conexion.getConnection();){
						//hacer la consulta
						PreparedStatement addPstmt=con.prepareStatement("INSERT INTO mascota(nombre, edad, fechaNacimiento, diaInicial, diaFinal)VALUES(?, ?, ?, ?, ?)");
						
						
						addPstmt.setString(1, nombre);
						addPstmt.setInt(2, edad);
						addPstmt.setObject(3, fechaN);
						addPstmt.setInt(4, dinicio);
						addPstmt.setInt(5, dfinal);
						
						//guardar en la base de datos
						addPstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "Mascota añadida");
						refrescarTabla(modelo);
						//cerrar lo que hemos abierto
						addPstmt.close();
					}catch(SQLException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "error al conectar con la base de datos");
					}
				}	
			}
		});
		btnAdd.setBounds(9, 178, 89, 23);
		frame.getContentPane().add(btnAdd);
		
		//ACTUALIZAR
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//recoger datos
				int id=Integer.parseInt(txtId.getText());
				String nombre=txtNombre.getText();
				int edad=Integer.parseInt( txtEdad.getText());
				LocalDate fechaN=LocalDate.parse(txtNacimiento.getText());
				int dinicio=Integer.parseInt( txtInicio.getText());
				int dfinal =Integer.parseInt( txtFinal.getText());
				
				if(nombre.isEmpty()) {
					JOptionPane.showMessageDialog(null, "El nombre está vacio");
				}else if(txtEdad.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "La edad está vacia");
				}else if(txtNacimiento.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "La fecha está vacia");
				}else if(txtInicio.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "el dia está vacio");
				}else if(txtFinal.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "el dia está vacio");
				}else if(!comprobarExpReg(txtEdad.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else if(!comprobarExpReg(txtNacimiento.getText(), "^[12]\\d{3}\\-[01][0-9]\\-[0-3][0-9]$")) {
					JOptionPane.showMessageDialog(null, "ingresa una fecha en formato YYYY-MM-DD");
				}else if(!comprobarExpReg(txtInicio.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else if(!comprobarExpReg(txtFinal.getText(), "^\\d{1,2}")) {
					JOptionPane.showMessageDialog(null, "ingresa un numero de hasta dos cifras");
				}else {
					//comenzar la consulta
					try(Connection con =Conexion.getConnection();){
						//hacer la consulta
						PreparedStatement updPstmt=con.prepareStatement("UPDATE mascota SET nombre=?, edad=?, fechaNacimiento=?, diaInicial=?, diaFinal=? WHERE idmascota=?");
						
						//recoger datos para rellenar interrogante
						updPstmt.setString(1, nombre);
						updPstmt.setInt(2, edad);
						updPstmt.setObject(3, fechaN);
						updPstmt.setInt(4, dinicio);
						updPstmt.setInt(5, dfinal);	
						updPstmt.setInt(6, id);
						
						//guardar en la base de datos
						updPstmt.executeUpdate();
						JOptionPane.showMessageDialog(null, "Mascota actualizada");
						refrescarTabla(modelo);
						//cerrar lo que hemos abierto
						updPstmt.close();
					}catch(SQLException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "error al conectar con la base de datos");
					}
				}
			}
		});
		btnActualizar.setBounds(118, 178, 89, 23);
		frame.getContentPane().add(btnActualizar);
		
		JButton btnNewButton = new JButton("Borrar");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//recoger datos
				int id=Integer.parseInt(txtId.getText());
				
				try(Connection con =Conexion.getConnection();){
					PreparedStatement delPstmt=con.prepareStatement("DELETE FROM mascota WHERE idmascota=?");
					
					delPstmt.setInt(1, id);
					
					//necesario para que se guarde en la base de datos
					delPstmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Mascota eliminada");
					refrescarTabla(modelo);
					
					delPstmt.close();
				}catch(SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "error al conectar con la base de datos");
				}
				
			}
		});
		btnNewButton.setBounds(56, 218, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		
		
	}//acaba la clase

	private void refrescarTabla(DefaultTableModel modelo) {
		try(Connection con =Conexion.getConnection();
				Statement stmt=con.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT * FROM mascota");){
			modelo.setRowCount(0);
			while(rs.next()) {
				Object row[]=new Object[5];
				row[0]=rs.getInt("idmascota");
				row[1]=rs.getString("nombre");
				row[2]=rs.getInt("edad");
				row[3]=rs.getDate("fechaNacimiento");
				row[4]=rs.getInt("diaInicial")+"-"+rs.getInt("diaFinal");
				modelo.addRow(row);
			}
			
		}catch(SQLException ex) {
			JOptionPane.showMessageDialog(null, "error al conectar con la base de datos");
		}
	}
}
