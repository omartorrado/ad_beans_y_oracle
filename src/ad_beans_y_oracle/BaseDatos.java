/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_beans_y_oracle;

import beans.Pedido;
import beans.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oracle
 */
public class BaseDatos {

    private Connection conexion;
    private boolean crearConexion = false;
    private String driver;
    private String usuario;
    private String clave;
    private String host;
    private String porto;
    private String sid;

    public BaseDatos() {

    }

    public BaseDatos(String driver, String usuario, String clave, String host, String porto, String sid) {
        this.driver = driver;
        this.usuario = usuario;
        this.clave = clave;
        this.host = host;
        this.porto = porto;
        this.sid = sid;
    }

    public boolean isCrearConexion() {
        return crearConexion;
    }

    public void setCrearConexion() {
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + clave + "@" + host + ":" + porto + ":" + sid;
        try {
            conexion = DriverManager.getConnection(ulrjdbc);
            this.crearConexion = true;
            System.out.println("Conectado");
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrarConexion() {
        try {
            conexion.close();
            crearConexion = false;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Producto> consultaPro(String consulta) {
        ArrayList<Producto> listaProductos = new ArrayList();
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {

                String desc = rs.getString(2);
                int id = rs.getInt(1);
                int stockactual = rs.getInt(3);
                int stockminimo = rs.getInt(4);
                float pvp = rs.getFloat(5);

                Producto p = new Producto(desc, id, stockminimo, pvp, stockactual);

                listaProductos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaProductos;
    }

    public ArrayList<Pedido> consultaPed(String consulta) {
        ArrayList<Pedido> listaPedidos = new ArrayList();
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {

                int numero = rs.getInt(1);
                int idproducto = rs.getInt(2);
                Date fecha = rs.getDate(3);
                int cantidad = rs.getInt(4);

                Pedido p = new Pedido(numero, idproducto, fecha, cantidad);

                listaPedidos.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaPedidos;
    }

    public ArrayList<Venta> consultaVen(String consulta) {
        ArrayList<Venta> listaVentas = new ArrayList();
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {

                Venta v = new Venta(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getInt(4));

                listaVentas.add(v);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaVentas;
    }

    public int obterUltimoID(String taboa) {
        int id = -1;
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from " + taboa);
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public int inxerirVenta(Venta ven) {
        int insertada = 0;
        try {
            Statement st = conexion.createStatement();
            st.executeUpdate("insert into ventas values("+ven.getNumeroventa()+","+ven.getIdproducto()+",date '"+ven.getFechaventa()+"',"+ven.getCantidad()+")");
            insertada=1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertada;
    }

    public Producto consultaUnProducto(int idproducto) {
        Producto prod = null;
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("select * from productos where id=" + idproducto);
            while (rs.next()) {
                prod = new Producto(rs.getString(2), rs.getInt(1), rs.getInt(4), rs.getFloat(5), rs.getInt(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prod;
    }

    public int actualizarStock(Producto producto, int cantidade, Date dataActual) {
        Pedido p=new Pedido();
        producto.addPropertyChangeListener(p);
        
        int nuevoStock=producto.getStockactual()-cantidade;
        
        producto.setStockactual(nuevoStock);
        if(p.isPedir()){
            p.setCantidad(cantidade);
            p.setNumeropedido(obterUltimoID("pedidos")+1);
            p.setIdproducto(producto.getIdproducto());
            p.setFecha(dataActual);
            try {
                Statement st=conexion.createStatement();
                st.executeUpdate("insert into pedidos values("+p.getNumeropedido()+","+p.getIdproducto()+",date '"+p.getFecha()+"',"+p.getCantidad()+")");
            } catch (SQLException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
            return -1;
        }else{
            try{
                Statement st=conexion.createStatement();
                st.executeUpdate("update productos set stockactual="+producto.getStockactual()+" where id="+producto.getIdproducto());
            }catch(SQLException ex) {
                Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
            }            
            return 1;
        }
        
        
    }

}
