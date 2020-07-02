

import org.h2.tools.Server;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

    private static Server tcp; //SERVER CONECTION ATTR

    /**
     *
     * @throws SQLException
     */
    public static void startDb() throws SQLException {
        tcp = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers", "-ifNotExists").start();
    }

    public static void stopDb() throws SQLException {
        tcp.stop();
    }

    public static void crearTablas() throws  SQLException{
        crearTablaUsuario();
        crearTablaProducto();
        crearTablaVenta();
        crearTablaVentaProd();
    }

    private static void crearTablaUsuario() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS USUARIO\n" +
                "(\n" +
                "  USUARIO VARCHAR(30) PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PASSWORD VARCHAR(100) NOT NULL\n" +
                ");";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
    }

    private static void crearTablaProducto() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCTO\n" +
                "(\n" +
                "  IDPROD INTEGER PRIMARY KEY NOT NULL,\n" +
                "  NOMBRE VARCHAR(100) NOT NULL,\n" +
                "  PRECIO FLOAT NOT NULL\n" +
                ");";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
    }

    private static void crearTablaVenta() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS VENTA\n" +
                "(\n" +
                "  IDVENTA VARCHAR(250) PRIMARY KEY NOT NULL,\n" +
                "  FECHA VARCHAR(100) NOT NULL,\n" +
                "  NOMBRECLIENTE VARCHAR(100) NOT NULL,\n" +
                "  TOTAL FLOAT NOT NULL\n" +
                ");";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
    }

    private static void crearTablaVentaProd() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS VENTAPRODUCTO\n" +
                "(\n" +
                "  IDVENTA VARCHAR(250) NOT NULL,\n" +
                "  IDPROD INTEGER NOT NULL\n" +
                ");";
        Connection con = DataBaseServices.getInstancia().getConexion();
        Statement statement = con.createStatement();
        statement.execute(sql);
        statement.close();
        con.close();
    }
}
