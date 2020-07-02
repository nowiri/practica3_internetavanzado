
import encapsulations.Controladora;
import encapsulations.Producto;
import encapsulations.Usuario;
import encapsulations.VentaProductos;

import javax.naming.ldap.Control;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataBaseServices {

    private static DataBaseServices instancia;
    private String URL = "jdbc:h2:tcp://localhost/~/carritoDB"; //Modo Server...

    private DataBaseServices() {
        registrarDriver();
    }

    public static DataBaseServices getInstancia() {
        if (instancia == null) {
            instancia = new DataBaseServices();
        }
        return instancia;
    }

    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {

        }
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {

        }
        return con;
    }

    public void testConexion() {
        try {
            getConexion().close();
            System.out.println("Conexión realizado con exito...");
        } catch (SQLException ex) {

        }
    }

    public ArrayList<Usuario> llenarUsuarios(){

        ArrayList<Usuario> lista = new ArrayList<>();
        Connection con = null;
        try {

            String query = "select * from USUARIO ";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();

            while(rs.next()){
                Usuario us = new Usuario();
                us.setName(rs.getString("NOMBRE"));
                us.setUser(rs.getString("USUARIO"));
                us.setPassw(rs.getString("PASSWORD"));

                lista.add(us);
            }

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return lista;
    }

    public ArrayList<Producto> llenarProductos(){

        ArrayList<Producto> lista = new ArrayList<>();
        Connection con = null;
        try {

            String query = "select * from PRODUCTO ";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();

            while(rs.next()){
                Producto prod = new Producto();
                prod.setId(rs.getInt("IDPROD"));
                prod.setNombre(rs.getString("NOMBRE"));
                prod.setPrecio(rs.getFloat("PRECIO"));

                lista.add(prod);
            }

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return lista;
    }

    public ArrayList<VentaProductos> llenarHistorialVentas() throws SQLException{

        ArrayList<VentaProductos> lista = new ArrayList<>();
        Connection con = null;

        try {

            String query = "select * from VENTA ";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();

            while(rs.next()){
                VentaProductos vp = new VentaProductos();
                ArrayList<Producto> ps = new ArrayList<>();

                vp.setId(rs.getString("IDVENTA"));
                vp.setFechaCompra(rs.getString("FECHA"));
                vp.setTotal(rs.getFloat("TOTAL"));
                vp.setNombreCliente(rs.getString("NOMBRECLIENTE"));

                String query2 = "select * from VENTAPRODUCTO where IDVENTA = ?";
                PreparedStatement prep2 = con.prepareStatement(query2);
                prep2.setString(1,vp.getId());
                ResultSet rs2 = prep2.executeQuery();

                while(rs2.next()) {
                    Producto p = Controladora.getInstance().buscarProducto(rs2.getInt("IDPROD"));
                    ps.add(p);
                }

                vp.setListaProductos(ps);

                lista.add(vp);
            }

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return lista;
    }

    public boolean addUsuario(Usuario us){
        boolean ok =false;
        Connection con = null;

        try {

            String query = "insert into USUARIO(USUARIO, NOMBRE, PASSWORD) values(?,?,?)";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            //PARAMETROS ANTES DE EJECUTAR
            prepareStatement.setString(1, us.getUser());
            prepareStatement.setString(2, us.getName());
            prepareStatement.setString(3, us.getPassw());

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;
    }

    public boolean addProducto(Producto prod){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "insert into PRODUCTO(IDPROD, NOMBRE, PRECIO) values(?,?,?)";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, prod.getId());
            prepareStatement.setString(2, prod.getNombre());
            prepareStatement.setFloat(3, prod.getPrecio());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;
    }

    public boolean deleteProducto(int id){
        boolean ok = false;

        Connection con = null;
        try {

            String query = "delete from PRODUCTO where IDPROD = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, id);;
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;

    }

    public boolean updateProducto(Producto prod, int id){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update PRODUCTO set IDPROD=?, NOMBRE=?, PRECIO=? where IDPROD = ?";
            con = DataBaseServices.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, prod.getId());
            prepareStatement.setString(2, prod.getNombre());
            prepareStatement.setFloat(3, prod.getPrecio());

            prepareStatement.setInt(4, id);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;
    }

    public boolean addVenta(VentaProductos v){
        boolean ok =false;
        Connection con = null;

        try {

            String query = "insert into VENTA(IDVENTA, FECHA, NOMBRECLIENTE, TOTAL) values(?,?,?,?)";
            con = DataBaseServices.getInstancia().getConexion();

            PreparedStatement prepareStatement = con.prepareStatement(query);
            //PARAMETROS ANTES DE EJECUTAR
            prepareStatement.setString(1, v.getId());
            prepareStatement.setString(2, v.getFechaCompra());
            prepareStatement.setString(3, v.getNombreCliente());
            prepareStatement.setFloat(4, v.getTotal());

            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;
    }

    public boolean addProdctosVendidos(String idVenta, ArrayList<Producto> ps){
        boolean ok =false;
        Connection con = null;

        try {

            con = DataBaseServices.getInstancia().getConexion();

            for (Producto p: ps) {

                String query = "insert into VENTAPRODUCTO(IDVENTA, IDPROD) values(?,?)";
                //
                PreparedStatement prepareStatement = con.prepareStatement(query);
                //Antes de ejecutar seteo los parametros.
                prepareStatement.setString(1, idVenta);
                prepareStatement.setInt(2, p.getId());
                //
                int fila = prepareStatement.executeUpdate();
                ok = fila > 0 ;

            }


        } catch (SQLException ex) {

        } finally{
            try {
                con.close();
            } catch (SQLException ex) {

            }
        }

        return ok;
    }
}
