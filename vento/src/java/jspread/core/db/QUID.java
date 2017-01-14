package jspread.core.db;

/**
 *
 * @author JeanPaul
 */
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import jspread.core.models.Transporter;
import jspread.core.util.PatronUtil;
import jspread.core.util.StringUtil;
import jspread.core.util.UTime;
import jspread.core.util.WebUtil;
import jspread.core.util.security.JHash;
import oracle.jdbc.OraclePreparedStatement;

/**
 *
 * @author desarrollowe
 */
public final class QUID {
//utiliza el siguiente renglon para poder hacer tracer, je je je tambien se puede usar en otras clases
//JOptionPane.showMessageDialog(null, "tracer: "+"alguna desc u obejto", "tracer", JOptionPane.INFORMATION_MESSAGE);    

    private HttpServletRequest request = null;
    private int statementTimeOut = 17; //esta varaible esta en segundos
    private int reportStatementTimeOut = 600; //esta varaible esta en segundos y equivale a 10 min

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private void endConnection(Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void endConnection(PreparedStatement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void endConnection(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void endConnection(JSpreadConnectionPool jscp, Connection connection, Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            jscp.returnConnectionToPool(connection);
        }
    }

    private void endConnection(JSpreadConnectionPool jscp, Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            jscp.returnConnectionToPool(connection);
        }
    }

    private void endConnection(JSpreadConnectionPool jscp, Connection connection) {
        if (connection != null) {
            jscp.returnConnectionToPool(connection);
        }
    }

    private void endConnection(Connection connection, Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void endConnection(Connection connection, PreparedStatement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ex) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Query">
    public final LinkedList logIn(String user, String password) {
        LinkedList infoUser = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT ID_Usuario, nombreCompleto, tipoRol, FK_ID_plantel"
                    + " FROM fichas.usuario"
                    + " WHERE BINARY usuario = ? "
                    + " AND password =  MD5(?)"
                    + " AND status COLLATE SQL_Latin1_General_CP1_CI_AI =  'Activo'";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, user);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                infoUser = new LinkedList();
                infoUser.add(rs.getString(1));
                infoUser.add(rs.getString(2));
                infoUser.add(rs.getString(3));
                infoUser.add(rs.getInt(4));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            infoUser = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return infoUser;
    }

    public final LinkedList select_idPlantel(String nombrePlantel) {
        LinkedList idPlantel = new LinkedList();
        Statement st = null;
        ResultSet rs = null;
        Connection conn = null;
        String LocalSQLSentence = null;
        PreparedStatement pstmt = null;
        JSpreadConnectionPool jscp = null;

        try {
            LocalSQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " FROM PLANTEL"
                    + " WHERE"
                    + " nombre = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(LocalSQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombrePlantel);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getInt(1));
                idPlantel.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            idPlantel = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idPlantel;
    }

    public final LinkedList select_idPlantelXCCT(String cct) {
        LinkedList idPlantel = new LinkedList();
        Statement st = null;
        ResultSet rs = null;
        Connection conn = null;
        String LocalSQLSentence = null;
        PreparedStatement pstmt = null;
        JSpreadConnectionPool jscp = null;

        try {
            LocalSQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " FROM PLANTEL"
                    + " WHERE"
                    + " claveCentroTrabajo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(LocalSQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, cct);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getInt(1));
                idPlantel.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            idPlantel = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idPlantel;
    }

    public final int getRowForeignKeysID(
            int ID_Carga_Academica,
            int FK_ID_Especialidad,
            int FK_ID_Asignatura) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalRegistros = -1;
        try {
            SQLSentence = " SELECT"
                    + " FK_ID_Especialidad"
                    + " ,FK_ID_Asignatura"
                    + " FROM CARGA_ACADEMICA"
                    + " WHERE"
                    + " FK_ID_Especialidad=?"
                    + " AND FK_ID_Asignatura=?"
                    + " AND ID_Carga_Academica =?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Carga_Academica);
            pstmt.setInt(2, FK_ID_Especialidad);
            pstmt.setInt(3, FK_ID_Asignatura);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                totalRegistros = (Integer) rs.getInt(1);
            }
        } catch (Exception ex) {

            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }

        return totalRegistros;
    }

    public final LinkedList select_Proveedor() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Proveedor"
                    + " , nombreProveedor"
                    + " , claveProveedor"
                    + " , telefono"
                    + " , correo"
                    + " , calle"
                    + " , localidad"
                    + " , colonia"
                    + " , codigoPostal"
                    + " FROM PROVEEDOR"
                    + " ORDER BY nombreProveedor ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Proveedor(String ID_Proveedor) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + "  nombreProveedor"
                    + " , claveProveedor"
                    + " , telefono"
                    + " , correo"
                    + " , calle"
                    + " , localidad"
                    + " , colonia"
                    + " , codigoPostal"
                    + " , noExterior"
                    + " FROM PROVEEDOR"
                    + " WHERE"
                    + " ID_Proveedor = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Proveedor);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_TipoProveedor() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Tipo_Proveedor"
                    + " , nombreTipo"
                    + " FROM TIPO_PROVEEDOR"
                    + " ORDER BY nombreTipo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_TipoProveedor(String ID_Tipo_Proveedor) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " nombreTipo"
                    + " FROM TIPO_PROVEEDOR"
                    + " WHERE"
                    + " ID_Tipo_Proveedor = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Tipo_Proveedor);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Detalle() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Detalle"
                    + " , nombreDetalle"
                    + " FROM DETALLE"
                    + " ORDER BY nombreDetalle ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Detalle(String ID_Detalle) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " nombreDetalle"
                    + " FROM DETALLE"
                    + " WHERE"
                    + " ID_Detalle = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Detalle);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Valor4Detalle(String ID_Detalle) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " V.ID_Valor"
                    + " , V.Valor"
                    + " , D.nombreDetalle"
                    + " FROM DETALLE AS D"
                    + " , VALOR AS V"
                    + " WHERE"
                    + " V.FK_ID_Detalle = D.ID_Detalle"
                    + " AND D.ID_Detalle = ?"
                    + " ORDER BY V.Valor ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setString(1, ID_Detalle);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Planteles(String idPlantel, Boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " ,P.nombre"
                    + " ,P.direccion"
                    + " ,P.claveCentroTrabajo"
                    + " ,P.telefono"
                    + " ,P.correo"
                    + " FROM PLANTEL AS P";
            if (!seeAll) {
                SQLSentence += " WHERE P.ID_Plantel = ?";
            }
            SQLSentence += " ORDER BY P.nombre ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, idPlantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_rowsPermiso(String nombrePermiso) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Permiso)"
                    + " FROM "
                    + " PERMISO"
                    + " WHERE nombrePermiso = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombrePermiso);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsMarca(String nombreMarca) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Marca)"
                    + " FROM "
                    + " MARCA"
                    + " WHERE marca = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_filasTipo_Software(String tipo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Tipo_Software)"
                    + " FROM "
                    + " TIPO_SOFTWARE"
                    + " WHERE tipo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsTipo_Software(String tipo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Tipo_Software)"
                    + " FROM "
                    + " TIPO_SOFTWARE"
                    + " WHERE tipo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsLicencia(String licencia) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Licencia)"
                    + " FROM "
                    + " LICENCIA"
                    + " WHERE licencia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, licencia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsTipoGarantia(String garantia) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Tipo_Garantia)"
                    + " FROM "
                    + " TIPO_GARANTIA"
                    + " WHERE garantia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, garantia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsTipo_Compra(String nombreMarca) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Tipo_Compra)"
                    + " FROM "
                    + " TIPO_COMPRA"
                    + " WHERE compra = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsTipo_Garantia(String garantia) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Tipo_Garantia)"
                    + " FROM "
                    + " TIPO_GARANTIA"
                    + " WHERE garantia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, garantia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsModelo(String FK_ID_Marca, String modelo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Modelo)"
                    + " FROM"
                    + " MODELO"
                    + " WHERE modelo = ?"
                    + " AND FK_ID_Marca = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, modelo);
            pstmt.setString(2, FK_ID_Marca);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsModelo(String FK_ID_Marca, String ID_Modelo, String modelo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Modelo)"
                    + " FROM"
                    + " MODELO"
                    + " WHERE modelo = ?"
                    + " AND FK_ID_Marca = ?"
                    + " AND ID_Modelo <> ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, modelo);
            pstmt.setString(2, FK_ID_Marca);
            pstmt.setString(3, ID_Modelo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_Permisos() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Permiso"
                    + " ,P.nombrePermiso"
                    + " ,P.tipoPermiso"
                    + " ,P.descripcion"
                    + " FROM"
                    + " PERMISO P"
                    + " ORDER BY P.nombrePermiso asc ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Marca() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " M.ID_Marca "
                    + " ,M.marca"
                    + " FROM MARCA  M"
                    + " ORDER BY M.marca ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Categoria() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " C.ID_Categoria "
                    + " ,C.nombreCategoria"
                    + " FROM CATEGORIA  C"
                    + " ORDER BY C.nombreCategoria ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Tipo_Software() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " T.ID_Tipo_Software "
                    + " ,T.tipo"
                    + " FROM TIPO_SOFTWARE  T"
                    + " ORDER BY T.tipo ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Licencia() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " L.ID_Licencia "
                    + " ,L.licencia"
                    + " FROM LICENCIA  L"
                    + " ORDER BY L.licencia ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Tipo_Garantia() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " G.ID_Tipo_Garantia "
                    + " ,G.garantia"
                    + " FROM TIPO_GARANTIA  G"
                    + " ORDER BY G.garantia ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_Tipo_Garantia(String ID_TipoGarantia) {
        String nombreGarantia = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " G.garantia"
                    + " FROM TIPO_GARANTIA  G"
                    + " WHERE"
                    + " G.ID_Tipo_Garantia = ?"
                    + " ORDER BY G.garantia ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_TipoGarantia);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                nombreGarantia = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombreGarantia;
    }

    public final LinkedList select_Tipo_Compra() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " T.ID_Tipo_Compra "
                    + " ,T.compra"
                    + " FROM TIPO_COMPRA  T"
                    + " ORDER BY T.compra ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Modelo() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " MO.ID_Modelo"
                    + " ,M.ID_Marca"
                    + " ,M.marca"
                    + " ,MO.FK_ID_Marca "
                    + " ,MO.descripcion"
                    + " ,MO.modelo "
                    + " FROM MARCA  M "
                    + " , MODELO  MO "
                    + " WHERE "
                    + " MO.FK_ID_Marca = M.ID_Marca"
                    + " ORDER BY M.marca ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getInt(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int getRowsPlantelinPlantelPersonal(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(FK_ID_Plantel)"
                    + " FROM "
                    + " PERSONAL_PLANTEL"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_PlantelXID(int ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"//0
                    + " ,P.nombre"//2
                    + " ,P.direccion"//3
                    + " ,P.claveCentroTrabajo"//6
                    + " ,P.telefono"//10
                    + " ,P.correo"//11
                    + " FROM PLANTEL  P"
                    + " WHERE"
                    + " P.ID_Plantel = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_IDNombrePlantel() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " ,nombre"
                    + " FROM "
                    + " PLANTEL"
                    + " ORDER BY nombre ASC";

            //+ " WHERE ID_Plantel <> 1";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_NombrePlantelID(String FK_ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " ,nombre"
                    + " FROM "
                    + " PLANTEL";
            if (!seeAll) {
                SQLSentence = SQLSentence + " WHERE ID_Plantel = ? ";
            }
            SQLSentence += " ORDER BY nombre ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, FK_ID_Plantel);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_PlantelXCampo(String campo, String ID_Plantel) {
        String resultado = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " " + campo
                    + " FROM "
                    + " PLANTEL"
                    + " WHERE ID_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            resultado = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public final LinkedList select_PlantelIDs() {
        LinkedList listosend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " FROM "
                    + " PLANTEL";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            listosend = new LinkedList();
            while (rs.next()) {
                listosend.add(rs.getString(1));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listosend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listosend;
    }

    public final String select_NomprePersonalCargo(String FK_ID_Plantel, String cargo) {
        String resultado = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.nombreCompleto"
                    + " FROM "
                    + " PERSONAL  P"
                    + " , PERSONAL_PLANTEL  PP"
                    + " WHERE"
                    + " PP.FK_ID_Personal=P.ID_Personal"
                    + " AND"
                    + " PP.FK_ID_Plantel=?"
                    + " AND"
                    + " PP.cargo=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, cargo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            resultado = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public final LinkedList select_PlantelesXPersonal(String FK_ID_Personal) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " ,P.nombre"
                    + " FROM"
                    + " PLANTEL  P"
                    + " , PERSONAL_PLANTEL  PP"
                    + " WHERE"
                    + " PP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND"
                    + " PP.FK_ID_Personal = ?"
                    + " ORDER BY P.nombre ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Personal);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_NombrePlantel(String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Plantel"
                    + " ,nombre"
                    + " FROM "
                    + " PLANTEL"
                    + " WHERE ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String getClavePlantel(String ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String clavePlantel = "-1";

        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " clavePlantel"
                    + " FROM "
                    + " PLANTEL"
                    + " WHERE ID_PLANTEL = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                clavePlantel = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            clavePlantel = "-1";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clavePlantel;
    }

    public final int select_rowsSubCategoria(String nombreSubCategoria, String FK_ID_Categoria) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_SubCategoria)"
                    + " FROM "
                    + " SUBCATEGORIA"
                    + " WHERE nombreSubCategoria = ?"
                    + " AND FK_ID_Categoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSubCategoria);
            pstmt.setString(2, FK_ID_Categoria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    /**
     * Obtiene el numero de registros en los que se encuentra IDGRUPO en la
     * tabla ASIGNATURA_GRUPO
     *
     * @param IDGrupo
     * @return numero de filas que contienen el IDGRUPO en la tabla
     * ASIGNATURA_GRUPO, -1 si ocurre algun error
     */
    public final int getRowCurpPersonal(String curp) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalRegistros = -1;
        try {
            SQLSentence = " SELECT"
                    + " COUNT(curp) "
                    + " FROM PERSONAL"
                    + " WHERE"
                    + " curp=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, curp);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalRegistros = (Integer) rs.getInt(1);
            }
        } catch (Exception ex) {

            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }

        return totalRegistros;
    }

    public final int getRowPersonalPlantel(String FK_ID_Personal, String FK_ID_Plantel, String cargo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int totalRegistros = -1;
        try {
            SQLSentence = " SELECT"
                    + " COUNT(FK_ID_Personal) "
                    + " FROM PERSONAL_PLANTEL"
                    + " WHERE"
                    + " FK_ID_Personal=?"
                    + " AND"
                    + " FK_ID_Plantel=?"
                    + " AND"
                    + " cargo=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Personal);
            pstmt.setString(2, FK_ID_Plantel);
            pstmt.setString(3, cargo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalRegistros = (Integer) rs.getInt(1);
            }
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }

        return totalRegistros;
    }

    public final String getIdPersonal(String curp) {
        String ID_Personal = "-1";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " ID_Personal"
                    + " FROM"
                    + " PERSONAL"
                    + " WHERE"
                    + " curp=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, curp);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                ID_Personal = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ID_Personal;
    }

    /**
     * Obtiene todos los permisos registrados
     *
     * @param
     * @return LinkedList con los registros obtenidos
     */
    public final LinkedList select_getPermisos() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Permiso"
                    + " ,descripcion "
                    + " FROM "
                    + " PERMISO"
                    + " ORDER BY descripcion ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            //pstmt.setFetchSize(50000);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Usuarios() {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList listToSend = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " U.ID_Usuario"
                    + " ,U.usuario"
                    + " ,U.password"
                    + " ,U.correo"
                    + " ,U.nombreCompleto"
                    + " ,U.status"
                    + " ,U.FK_ID_Plantel"
                    + " ,P.nombre"
                    + " ,R.nombreRol"
                    + " ,RU.ID_Rol_Usuario"
                    + " FROM "
                    + " USUARIO U"
                    + " , PLANTEL P"
                    + " , ROL  R"
                    + " , ROL_USUARIO  RU"
                    + " WHERE"
                    + " RU.FK_ID_Usuario=U.ID_Usuario"
                    + " AND U.FK_ID_Plantel=P.ID_Plantel"
                    + " AND RU.FK_ID_Rol=R.ID_Rol"
                    + " ORDER BY U.usuario ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            LinkedList aux = null;
            while (rs.next()) {
                aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                listToSend.add(aux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {

            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_searchPersonalNamesNCurp(String busqueda) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;
        try {
            //busqueda = StringUtil.quitarPreposicionesCurp(busqueda.trim());
            busqueda = busqueda.trim();
            busqueda = busqueda.replaceAll("\\s\\s", "\\s");
            String parametrosBusqueda[] = busqueda.split("\\s");
            String paramsType[] = new String[parametrosBusqueda.length];
            for (int i = 0; i < paramsType.length; i++) {
                if (StringUtil.hasNumber(parametrosBusqueda[i])) {
                    paramsType[i] = "num";
                } else {
                    paramsType[i] = "string";
                }
            }
            SQLSentence = ""
                    + " SELECT"
                    + " TOP 15"
                    + " P.ID_Personal"
                    + " ,(P.nombre + ' ' + P.aPaterno + ' ' + P.aMaterno) info"
                    + " FROM PERSONAL  P"
                    + " WHERE";

            SQLSentence = SQLSentence + " (P.nombre + ' ' + P.aPaterno + ' ' + P.aMaterno) COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?";

            SQLSentence = SQLSentence + " AND P.nombre<>'OTRO USUARIO'"
                    + " ORDER BY P.aPaterno, P.aMaterno, P.nombre ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "%" + busqueda + "%");

            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add("");
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2).replaceAll("'", ""));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_permisosPorUsuarios(String ID_Usuario) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.nombrePermiso"
                    + " FROM PERMISO P"
                    + " , ROL_PERMISO RP"
                    + " , ROL R"
                    + " , ROL_USUARIO RU"
                    + " WHERE RP.FK_ID_Permiso = P.ID_Permiso"
                    + " AND RP.FK_ID_Rol = R.ID_Rol"
                    + " AND RU.FK_ID_Rol = R.ID_Rol"
                    + " AND RU.FK_ID_Usuario = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Usuario));

            rs = pstmt.executeQuery();

            listToSend = new LinkedList();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_permisosPorNombreRol(String nombreRol) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    //+ " P.ID_Permiso"
                    + "  P.nombrePermiso"
                    //+ " , P.descripcion"
                    //+ " , P.tipoPermiso"
                    + " FROM"
                    + " ROL  R"
                    + " , ROL_PERMISO  RP"
                    + " , PERMISO  P"
                    + " WHERE"
                    + " RP.FK_ID_Permiso=P.ID_Permiso"
                    + " AND RP.FK_ID_Rol=R.ID_Rol"
                    + " AND R.nombreRol=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreRol);
            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_permisosPorRol(String ID_Rol) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Permiso"
                    + " ,P.descripcion"
                    + " FROM PERMISO  P"
                    + " , ROL_PERMISO  RP"
                    + " , ROL  R"
                    + " WHERE RP.FK_ID_Permiso = P.ID_Permiso"
                    + " AND RP.FK_ID_Rol = R.ID_Rol"
                    + " AND RP.FK_ID_Rol = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Rol));

            rs = pstmt.executeQuery();

            listToSend = new LinkedList();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final boolean select_existpermisoPorRol(String ID_Rol, String ID_Permiso) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean existePermiso = false;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Rol_Permiso "
                    + " FROM ROL_PERMISO "
                    + " WHERE FK_ID_Rol = ? "
                    + " AND FK_ID_Permiso = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Rol));
            pstmt.setInt(2, Integer.parseInt(ID_Permiso));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                existePermiso = true;
            }

            endConnection(jscp, conn, pstmt, rs);

        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existePermiso;
    }

    public final boolean select_rolOcupado(String ID_Rol) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean ocupado = false;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Rol_Usuario "
                    + " FROM ROL_USUARIO "
                    + " WHERE FK_ID_Rol = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Rol));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                ocupado = true;
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ocupado;
    }

    public final LinkedList select_idUsuario(String usuario, String password, String status) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " SELECT"
                    + " U.ID_Usuario"
                    + " , U.nombreCompleto"
                    + " , U.FK_ID_Plantel"
                    + " , R.nombreRol"
                    + " FROM USUARIO U"
                    + " , ROL_USUARIO RU"
                    + " , ROL R"
                    + " WHERE"
                    + " RU.FK_ID_Usuario=U.ID_Usuario"
                    + " AND RU.FK_ID_Rol=R.ID_Rol"
                    + " AND U.status = ?"
                    + " AND U.usuario = ?"
                    + " AND U.password = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, status);
            pstmt.setString(2, usuario);
            pstmt.setString(3, JHash.getStringMessageDigest(password, JHash.MD5));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                listToSend = new LinkedList();
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PersonalLogin(String correo, String password) {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Personal"
                    + " , P.nombreCompleto"
                    + " , PP.FK_ID_Plantel"
                    + " FROM"
                    + " PERSONAL  P"
                    + " , PERSONAL_PLANTEL  PP"
                    + " WHERE"
                    + " PP.FK_ID_Personal=P.ID_Personal"
                    + " AND P.correo=?"
                    + " AND P.password=?"
                    + " AND PP.situacionActual='Activo'";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, correo);
            pstmt.setString(2, JHash.getStringMessageDigest(password, JHash.MD5));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend = new LinkedList();
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Usuario(String ID_Usuario) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList listToSend = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " U.usuario"
                    + " ,U.correo"
                    + " ,U.nombreCompleto"
                    + " ,U.status"
                    + " ,U.FK_ID_Plantel"
                    + " ,RU.FK_ID_Rol"
                    + " FROM "
                    + " USUARIO  U"
                    + " ,ROL_USUARIO  RU"
                    + " WHERE"
                    + " U.ID_Usuario=RU.FK_ID_Usuario"
                    + " AND"
                    + " U.ID_Usuario=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Usuario);

            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            if (rs.next()) {
                listToSend.add(null);
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_IDUsuario(String usuario, String nombreCompleto, String FK_ID_Plantel) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id = "";

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Usuario"
                    + " FROM "
                    + " USUARIO"
                    + " WHERE"
                    + " usuario=?"
                    + " AND nombreCompleto=?"
                    + " AND FK_ID_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, usuario);
            pstmt.setString(2, nombreCompleto);
            pstmt.setString(3, FK_ID_Plantel);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            id = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public final String select_PasswordUsuario(String ID_Usuario) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String password = "";

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " password"
                    + " FROM "
                    + " USUARIO"
                    + " WHERE"
                    + " ID_Usuario=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Usuario);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                password = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            password = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }

    public final String select_PasswordPersonal(String ID_Personal) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String password = "";

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " password"
                    + " FROM "
                    + " PERSONAL"
                    + " WHERE"
                    + " ID_Personal=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                password = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            password = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }

    public final LinkedList select_PlantelInfo(String ID_Plantel) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList listToSend = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.clavePlantel"
                    + " ,P.nombre"
                    + " ,P.direccion"
                    + " ,P.FK_ID_Municipio"
                    + " ,M.municipio"
                    + " ,P.FK_ID_Estado"
                    + " , E.estado"
                    + " ,P.claveCentroTrabajo"
                    + " ,P.claveEstudios"
                    + " ,P.fechaAlta"
                    + " ,P.zona"
                    + " ,P.telefono"
                    + " ,P.correo"
                    + " ,P.fax"
                    + " ,P.anioCreacion"
                    + " FROM "
                    + " PLANTEL  P"
                    + " ,ESTADO  E"
                    + " ,MUNICIPIO  M"
                    + " WHERE"
                    + " P.FK_ID_Estado=E.ID_Estado"
                    + " AND"
                    + " P.FK_ID_Municipio=M.ID_Municipio"
                    + " AND"
                    + " P.ID_Plantel=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Plantel);

            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PlantelUsuario(String ID_Usuario) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList plantel = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " ,P.nombre"
                    + " ,P.claveCentroTrabajo"
                    + " FROM "
                    + " PLANTEL  P"
                    + " , USUARIO  U"
                    + " WHERE"
                    + " U.FK_ID_Plantel=P.ID_Plantel"
                    + " AND"
                    + " ID_Usuario=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Usuario);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                plantel = new LinkedList();
                plantel.add(rs.getString(1));
                plantel.add(rs.getString(2));
                plantel.add(rs.getString(3));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            plantel = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return plantel;
    }

    public final LinkedList select_Roles() {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList listToSend = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Rol"
                    + " ,nombreRol"
                    + " ,descripcion"
                    + " FROM "
                    + " ROL";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            LinkedList aux = null;
            while (rs.next()) {
                aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                listToSend.add(aux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Roles(boolean seeAll) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList listToSend = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Rol"
                    + " ,nombreRol"
                    + " ,descripcion"
                    + " FROM "
                    + " ROL";
            if (!seeAll) {
                SQLSentence += " WHERE nombreRol <> 'root'";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            listToSend = new LinkedList();
            LinkedList aux = null;
            while (rs.next()) {
                aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                listToSend.add(aux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_rolesUsuario() {
        LinkedList listToSend = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Rol"
                    + " ,nombreRol "
                    + " ,descripcion "
                    + " FROM ROL"
                    + " ORDER BY nombreRol ASC"
                    + "";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            listToSend = new LinkedList();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Personal(String FK_ID_Plantel, Boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String SQLWhere = "";
        try {
            if (!seeAll) {
                SQLWhere = " WHERE PP.FK_ID_Plantel = ?";
            }
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Personal"
                    + " ,P.nombre"
                    + " ,P.telefono"
                    + " ,P.titulo"
                    + " ,P.correo"
                    + " ,PP.FK_ID_Plantel"
                    + " ,PLA.nombre"
                    + " ,PP.cargo"
                    + " ,PP.estatus"
                    + " ,PP.ID_Personal_Plantel"
                    + " ,P.aPaterno"
                    + " ,P.aMaterno"
                    + " ,P.siglasTitulo"
                    + " ,P.curp"
                    + " ,CASE WHEN PP.fechaAsignacionCargo IS NULL THEN '0000-00-00' ELSE PP.fechaAsignacionCargo END"
                    + " FROM PERSONAL_PLANTEL  PP "
                    + " LEFT JOIN PERSONAL  P ON PP.FK_ID_Personal = P.ID_Personal "
                    + " LEFT JOIN PLANTEL  PLA ON PP.FK_ID_Plantel = PLA.ID_Plantel ";

            if (!seeAll) {
                SQLSentence += SQLWhere;
            }
            SQLSentence += " WHERE P.nombre<>'OTRO USUARIO'"
                    + " ORDER BY P.aPaterno ASC, P.aMaterno ASC, P.nombre ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getString(14));
                llaux.add(rs.getString(15));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_InfoPersonal(String ID_Personal) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " nombre"
                    + " ,telefono"
                    + " ,correo"
                    + " ,fechaAlta"
                    + " ,titulo"
                    + " ,siglasTitulo"
                    + " ,aPaterno"
                    + " ,aMaterno"
                    + " ,curp"
                    + " FROM "
                    + " PERSONAL"
                    + " WHERE"
                    + " ID_Personal=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_InfoPersonalPlantel(String ID_Personal_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " PP.estatus"
                    + ", PP.cargo"
                    + ", P.nombre"
                    + ", P.aPaterno"
                    + ", P.aMaterno"
                    + " FROM"
                    + " PERSONAL  P"
                    + " ,PERSONAL_PLANTEL  PP"
                    + " WHERE"
                    + " PP.FK_ID_Personal=p.ID_Personal"
                    + " AND"
                    + " PP.ID_Personal_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal_Plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PlantelXPersonal(String ID_Personal) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " PL.ID_Plantel"
                    + " ,PL.nombre"
                    + " ,PL.nombreCompletoDirector"
                    + " ,PL.nombreCompletoSubDirector"
                    + " FROM"
                    + " PERSONAL  P"
                    + " ,PERSONAL_PLANTEL  PP"
                    + " ,PLANTEL  PL"
                    + " WHERE"
                    + " PP.FK_ID_Personal=P.ID_Personal"
                    + " AND PP.FK_ID_Plantel=PL.ID_Plantel"
                    + " AND P.ID_Personal=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_obsPersonalXPlantel(String FK_ID_Plantel, String ID_Personal, String situacion) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " disponibilidad"
                    + " ,observaciones "
                    + " ,P.ID_Personal"
                    + " ,PP.FK_ID_Plantel"
                    + " FROM PERSONAL_PLANTEL  PP "
                    + " LEFT JOIN PERSONAL  P ON PP.FK_ID_Personal = P.ID_Personal "
                    + " WHERE PP.cargo = 'Docente' AND PP.situacionActual = 'Activo' "
                    + " AND PP.FK_ID_Plantel = ? "
                    + " AND P.ID_Personal = ? "
                    + " AND PP.situacionActual=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            pstmt.setInt(2, Integer.parseInt(ID_Personal));
            pstmt.setString(3, situacion);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getInt(3));
                listToSend.add(rs.getInt(4));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, " Verifique log: " + " " + ex);
        }
        return listToSend;
    }

    public final LinkedList select_InfoPersonalXPlantel(String FK_ID_Plantel, String ID_Personal) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " disponibilidad"
                    + " ,observaciones "
                    + " ,P.ID_Personal"
                    + " ,PP.FK_ID_Plantel"
                    + " FROM PERSONAL_PLANTEL  PP "
                    + " LEFT JOIN PERSONAL  P ON PP.FK_ID_Personal = P.ID_Personal "
                    + " WHERE PP.cargo = 'Docente' AND PP.situacionActual = 'Activo' "
                    + " AND PP.FK_ID_Plantel = ? "
                    + " AND P.ID_Personal = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            pstmt.setInt(2, Integer.parseInt(ID_Personal));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getInt(3));
                listToSend.add(rs.getInt(4));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, " Verifique log: " + " " + ex);
        }
        return listToSend;
    }

    public final LinkedList select_PersonalXPlantel(String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Personal"
                    + " ,(curp, + ' ' + ,P.aPaterno, + ' ' + ,P.aMaterno, + ' ' + ,P.nombre)  Personal"
                    + " FROM PERSONAL_PLANTEL  PP"
                    + " LEFT JOIN PERSONAL  P ON PP.FK_ID_Personal = P.ID_Personal"
                    + " WHERE PP.cargo = 'Docente' "
                    + " AND PP.situacionActual = 'Activo'"
                    + " AND PP.FK_ID_Plantel = ?"
                    + " ORDER BY P.aPaterno, P.aMaterno, P.nombre ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, " Verifique log: " + " " + ex);
        }
        return listToSend;
    }

    public final LinkedList select_Nacionalidad() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " N.ID_Nacionalidad"
                    + " ,N.nacionalidad"
                    + " FROM"
                    + " NACIONALIDAD  N"
                    + " ORDER BY N.ID_Nacionalidad ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SubCategoria() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " S.ID_SubCategoria"
                    + " ,C.ID_Categoria"
                    + " ,C.nombreCategoria"
                    + " ,S.nombreSubCategoria"
                    + " ,S.FK_ID_Categoria"
                    + " FROM SUBCATEGORIA  S "
                    + " , CATEGORIA  C "
                    + " Where "
                    + " S.FK_ID_Categoria = C.ID_Categoria"
                    + " ORDER BY C.nombreCategoria ASC, S.nombreSubCategoria ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getInt(5));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SubCategoria(String idSucategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " S.nombreSubCategoria"
                    + " FROM SUBCATEGORIA  S "
                    + " Where "
                    + " S.ID_Subcategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, idSucategoria);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_infoEstado() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Estado"
                    + " ,estado "
                    + " FROM "
                    + " ESTADO"
                    + " ORDER BY estado ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2).toUpperCase());
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int getRowsMarcainMarcaModelo(int ID_Marca) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Modelo)"
                    + " FROM "
                    + " MODELO"
                    + " WHERE FK_ID_Marca = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Marca);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsTipoGarantiainTipoGarantiaBien(int FK_ID_Tipo_Garantia) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Tipo_Compra = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, FK_ID_Tipo_Garantia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsTipoSoftwareinTipoTipoSoftwareSoftware(int FK_ID_Tipo_Software) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE FK_ID_Tipo_Software = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, FK_ID_Tipo_Software);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsTipoComprainTipoCompraSoftware(int ID_Tipo_Compra) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE FK_ID_Tipo_Compra = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Tipo_Compra);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsTipoComprainTipoCompraBien(int ID_Tipo_Compra) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Tipo_Compra = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Tipo_Compra);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_ModeloXID(int ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " MO.ID_Modelo"
                    + " ,M.ID_Marca"
                    + " ,M.marca"
                    + " ,MO.FK_ID_Marca "
                    + " ,MO.descripcion"
                    + " ,MO.modelo "
                    + " FROM MARCA  M "
                    + " , MODELO  MO "
                    + " Where "
                    + " MO.FK_ID_Marca = M.ID_Marca"
                    + " AND ID_Modelo = ?"
                    + " ORDER BY M.marca ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Modelo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getInt(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Modelo4ID(String ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " MO.ID_Modelo"
                    + " ,M.ID_Marca"
                    + " ,M.marca"
                    + " ,MO.FK_ID_Marca "
                    + " ,MO.descripcion"
                    + " ,MO.modelo "
                    + " FROM MARCA  M "
                    + " , MODELO  MO "
                    + " Where "
                    + " MO.FK_ID_Marca = M.ID_Marca"
                    + " AND ID_Modelo = ?"
                    + " ORDER BY M.marca ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Modelo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                listToSend.add(rs.getInt(1));
                listToSend.add(rs.getInt(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getInt(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Modelo(String ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " MO.ID_Modelo"
                    + " ,M.ID_Marca"
                    + " ,M.marca"
                    + " ,MO.FK_ID_Marca "
                    + " ,MO.descripcion"
                    + " ,MO.modelo "
                    + " FROM MARCA  M "
                    + " , MODELO  MO "
                    + " Where "
                    + " MO.FK_ID_Marca = M.ID_Marca"
                    + " AND ID_Modelo = ?"
                    + " ORDER BY M.marca ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Modelo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int getRowsModeloinModeloBien(int ID_Modelo) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Modelo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsLicenciainLicenciaSoftware(int ID_Licencia) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE FK_ID_Licencia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Licencia);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsCategoria(String nombreCategoria) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Categoria)"
                    + " FROM "
                    + " CATEGORIA"
                    + " WHERE nombreCategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreCategoria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsCategoriainCategoriaSubCategoria(int ID_Categoria) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_SubCategoria)"
                    + " FROM "
                    + " SUBCATEGORIA"
                    + " WHERE FK_ID_Categoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Categoria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_getRowsSubCategoria() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " C.nombreCategoria"
                    + " ,C.ID_Categoria"
                    + " FROM CATEGORIA  C "
                    + " ORDER BY C.nombreCategoria ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getInt(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SubCategoriaXID(int ID_SubCategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " S.ID_SubCategoria"
                    + " ,S.nombreSubCategoria"
                    + " ,S.FK_ID_Categoria"
                    + " ,C.nombreCategoria"
                    + " FROM SUBCATEGORIA  S "
                    + " , CATEGORIA  C "
                    + " Where "
                    + " S.FK_ID_Categoria = C.ID_Categoria"
                    + " AND S.ID_SubCategoria = ?"
                    + " ORDER BY S.nombreSubCategoria ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_SubCategoria);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getInt(3));
                llaux.add(rs.getString(4));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Departamento_PlantelXID(String idPlantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " DP.ID_Departamento_Plantel"
                    + " ,DP.FK_ID_Plantel"
                    + " ,DP.FK_ID_Departamento"
                    + " ,D.nombreDepartamento"
                    + " ,P.nombre"
                    + " FROM DEPARTAMENTO_PLANTEL  DP"
                    + " ,DEPARTAMENTO  D"
                    + " ,PLANTEL  P"
                    + " WHERE"
                    + " DP.FK_ID_Departamento = D.ID_Departamento"
                    + " AND DP.FK_ID_Plantel = P.ID_Plantel";

            if (!idPlantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?"
                        + " ORDER BY D.nombreDepartamento ASC";
            } else {
                SQLSentence += " ORDER BY P.nombre ASC ";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            if (!idPlantel.equalsIgnoreCase("todos")) {
                pstmt.setString(1, idPlantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getInt(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_DepartamentoPlantel(String ID_DepartamentoPlantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " DP.ID_Departamento_Plantel"
                    + " ,DP.FK_ID_Plantel"
                    + " ,DP.FK_ID_Departamento"
                    + " ,D.nombreDepartamento"
                    + " ,P.nombre"
                    + " FROM DEPARTAMENTO_PLANTEL  DP"
                    + " ,DEPARTAMENTO  D"
                    + " ,PLANTEL  P"
                    + " WHERE"
                    + " DP.FK_ID_Departamento = D.ID_Departamento"
                    + " AND DP.FK_ID_Plantel = P.ID_Plantel"
                    + " AND DP.ID_Departamento_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_DepartamentoPlantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getInt(1));
                listToSend.add(rs.getInt(2));
                listToSend.add(rs.getInt(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Departamento() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " ID_Departamento"
                    + " ,nombreDepartamento"
                    + " FROM DEPARTAMENTO"
                    + " ORDER BY nombreDepartamento";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Departamento(String ID_Departamento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " ID_Departamento"
                    + " ,nombreDepartamento"
                    + " FROM DEPARTAMENTO"
                    + " WHERE ID_Departamento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Departamento);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getInt(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Departamento_Plantel(String ID_Plantel, Boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " DP.ID_Departamento_Plantel"
                    + " ,DP.FK_ID_Plantel"
                    + " ,DP.FK_ID_Departamento"
                    + " ,D.nombreDepartamento"
                    + " ,P.nombre"
                    + " FROM DEPARTAMENTO_PLANTEL  DP"
                    + " ,DEPARTAMENTO  D"
                    + " ,PLANTEL  P"
                    + " WHERE"
                    + " DP.FK_ID_Departamento = D.ID_Departamento"
                    + " AND DP.FK_ID_Plantel = P.ID_Plantel";

            if (!seeAll) {
                SQLSentence = SQLSentence + " AND DP.FK_ID_Plantel = '" + ID_Plantel + "' ";
            }
            SQLSentence = SQLSentence + " ORDER BY P.nombre ASC, D.nombreDepartamento ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getInt(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_DepartamentoPlantelXID(String tipoRol, String ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.nombre"
                    + " ,P.ID_Plantel"
                    + " FROM PLANTEL  P";
            if (tipoRol.equalsIgnoreCase("root") == false) {
                SQLSentence = SQLSentence + " WHERE P.ID_Plantel = '" + ID_Plantel + "' ";
            }
            SQLSentence = SQLSentence + " ORDER BY P.nombre ASC, P.ID_Plantel";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getInt(2));
                listToSend.add(llaux);

            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Departamentos() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Departamento"
                    + " ,nombreDepartamento"
                    + " FROM DEPARTAMENTO "
                    + " ORDER BY nombreDepartamento ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));

                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final boolean select_existdepartamentoPorPlantel(String ID_Plantel, String ID_Departamento) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean existePermiso = false;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Departamento_Plantel "
                    + " FROM DEPARTAMENTO_PLANTEL "
                    + " WHERE FK_ID_Plantel = ? "
                    + " AND FK_ID_Departamento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_Departamento);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                existePermiso = true;
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existePermiso;
    }

    public final LinkedList select_Grupo() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " G.ID_Grupo "
                    + " ,G.nombreGrupo"
                    + " ,G.fechaAlta"
                    + " FROM GRUPO  G"
                    + " ORDER BY G.nombreGrupo ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_rowsGrupo(String nombreGrupo) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Grupo)"
                    + " FROM "
                    + " GRUPO"
                    + " WHERE nombreGrupo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreGrupo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsGrupoinGrupoRelacion_PDG(int ID_Grupo) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_PDG)"
                    + " FROM "
                    + " Relacion_PDG"
                    + " WHERE FK_ID_Grupo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Grupo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsGrupoinGrupo_Bien(String ID_Grupo) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Grupo_Bien)"
                    + " FROM "
                    + " GRUPO_BIEN"
                    + " WHERE FK_ID_Grupo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Grupo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_getRowsTipo_Software() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Tipo_Software"
                    + " ,tipo"
                    + " FROM "
                    + " TIPO_SOFTWARE";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_getRowsLicencia() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Licencia"
                    + " ,licencia"
                    + " FROM"
                    + " LICENCIA";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_getRowsTipo_Compra() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Licencia"
                    + " ,licencia"
                    + " FROM"
                    + " LICENCIA";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_rowsSoftware(String nombreSoftware) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE nombreSoftware = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSoftware);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_SoftwareXID(int ID_Software) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " S.ID_Software"
                    + " ,S.nombreSoftware"
                    + " ,S.version"
                    + " ,S.serial"
                    + " ,S.fechaVencimiento"
                    + " ,S.fechaAdquisicion"
                    + " ,S.noDictamen"
                    + " ,S.noLicencias"
                    + " ,S.hddRequerido"
                    + " ,S.ramRequerida"
                    + " ,S.soRequerido"
                    + " ,S.soporteTecnico"
                    + " ,S.emailSoporteTecnico"
                    + " ,S.telefonoSoporte"
                    + " ,S.FK_ID_Tipo_Software"
                    + " ,S.FK_ID_Licencia"
                    + " ,S.FK_ID_Tipo_Compra"
                    + " ,S.observaciones"
                    + " ,S.nombreArchivo"
                    + " ,TS.ID_Tipo_Software"
                    + " ,TS.tipo"
                    + " ,L.ID_Licencia"
                    + " ,L.licencia"
                    + " ,TC.ID_Tipo_Compra"
                    + " ,TC.compra"
                    + " FROM SOFTWARE  S"
                    + " ,TIPO_SOFTWARE  TS"
                    + " ,LICENCIA  L"
                    + " ,TIPO_COMPRA  TC"
                    + " WHERE"
                    + " S.FK_ID_Tipo_Software = TS.ID_Tipo_Software"
                    + " AND S.FK_ID_Licencia = L.ID_Licencia"
                    + " AND S.FK_ID_Tipo_Compra = TC.ID_Tipo_Compra"
                    + " AND S.ID_Software = ?"
                    + " ORDER BY S.nombreSoftware ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Software);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getString(14));
                llaux.add(rs.getInt(15));
                llaux.add(rs.getInt(16));
                llaux.add(rs.getInt(17));
                llaux.add(rs.getString(18));
                llaux.add(rs.getString(19));
                llaux.add(rs.getInt(20));
                llaux.add(rs.getString(21));
                llaux.add(rs.getInt(22));
                llaux.add(rs.getString(23));
                llaux.add(rs.getInt(24));
                llaux.add(rs.getString(25));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int getRowsSubCategoriainSubCategoriaBien(int ID_SubCategoria) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_SubCategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_SubCategoria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsSubCategoriainSubCategoriaDetalleSubCategoria(int ID_SubCategoria) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Detalle_SubCategoria)"
                    + " FROM "
                    + " DETALLE_SUBCATEGORIA"
                    + " WHERE FK_ID_SubCategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_SubCategoria);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_getRowsPermisoinPermisoRol(int ID_Permiso) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Rol_Permiso)"
                    + " FROM "
                    + " ROL_PERMISO"
                    + " WHERE FK_ID_Permiso = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Permiso);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsDepartamento(String nombreDepartamento) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Departamento)"
                    + " FROM "
                    + " DEPARTAMENTO"
                    + " WHERE nombreDepartamento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreDepartamento);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsDepartamentoinDepartamentoDepartamentoPlantel(int ID_Departamento) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Departamento_Plantel)"
                    + " FROM "
                    + " DEPARTAMENTO_PLANTEL"
                    + " WHERE FK_ID_Departamento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Departamento);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsDepartamentoinDepartamentoBien(int ID_Departamento) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Departamento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Departamento);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelBien(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelDepartamentoPlantel(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Departamento_Plantel)"
                    + " FROM "
                    + " DEPARTAMENTO_PLANTEL"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelPersonalPlantel(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Personal_Plantel)"
                    + " FROM "
                    + " PERSONAL_PLANTEL"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelSoftwarePlantel(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software_Plantel)"
                    + " FROM "
                    + " SOFTWARE_PLANTEL"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelTranspasoBien(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Traspaso_Bien)"
                    + " FROM "
                    + " TRASPASO_BIEN"
                    + " WHERE FK_ID_Plantel_Destino = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsPlantelinPlantelUsuario(int ID_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Usuario)"
                    + " FROM "
                    + " USUARIO"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList speedTest() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " * "
                    + " FROM CALIFICACION"
                    + " WHERE ROWNUM <= 50000";
//            SQLSentence = ""
//                    + " SELECT TOP 50000"
//                    + " * "
//                    + " FROM calificacion"
//                   ;
            System.out.println(UTime.getTimeMilis());
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            //pstmt.setFetchSize(1000);

            pstmt.setQueryTimeout(statementTimeOut);
            //System.out.println(""+UTime.getTimeMilis());
            //for (int i = 0; i < 2; i++) {
            //System.out.println(UTime.getTimeMilis());
            rs = pstmt.executeQuery();
//int i=0;
            //System.out.println("--"+UTime.getTimeMilis());

            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getInt(3));
                listToSend.add(llaux);
                //  i+=1;
            }
//System.out.println(i+": "+UTime.getTimeMilis());
            //}
            System.out.println(UTime.getTimeMilis());
            rs.close();
            pstmt.close();
            conn.close();
//endConnection(jscp, conn, pstmt, rs);

        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_AllDetalle4SubCategoria(String idSucategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " D.ID_Detalle"
                    + " , D.nombredetalle"
                    + " , DS.FK_ID_Subcategoria"
                    + " , DS.nombresubcategoria"
                    + " , DS.ID_Detalle_Subcategoria"
                    + " FROM"
                    + " DETALLE D LEFT JOIN"
                    + " (SELECT "
                    + " DT.FK_ID_Detalle"
                    + " , DT.ID_Detalle_Subcategoria"
                    + " , DT.FK_ID_Subcategoria"
                    + " , S.nombresubcategoria"
                    + " FROM"
                    + " DETALLE_SUBCATEGORIA DT"
                    + " ,  SUBCATEGORIA S"
                    + " WHERE "
                    + " DT.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " AND FK_ID_Subcategoria = ?) DS ON DS.FK_ID_Detalle=D.ID_Detalle"
                    + " ORDER BY D.nombredetalle ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, idSucategoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Detalle_SubCategoria(String FK_ID_Sucategoria, String FK_ID_Detalle) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Detalle_Subcategoria"
                    + " FROM"
                    + " DETALLE_SUBCATEGORIA"
                    + " WHERE"
                    + " FK_ID_Detalle = ?"
                    + " AND FK_ID_Subcategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Detalle);
            pstmt.setString(2, FK_ID_Sucategoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Grupos(String ID_Plantel, Boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , D.ID_Departamento"
                    + " , D.nombreDepartamento"
                    + " , G.ID_Grupo"
                    + " , G.nombreGrupo"
                    + " , PDG.ID_PDG"
                    + " FROM"
                    + " GRUPO G"
                    + " ,  RELACION_PDG PDG"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " WHERE"
                    + " PDG.FK_ID_Grupo=G.ID_Grupo"
                    + " AND PDG.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento";
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " ORDER BY P.nombre ASC, D.nombredepartamento ASC, G.nombregrupo ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int getRowsBieninBienDepartamentoPlantel(String ID_Departamento_Plantel) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE FK_ID_Departamento_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Departamento_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int getRowsNombreSoftwareinNombreSoftwareSoftware(int ID_Nombre_Software) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE FK_ID_NombreSoftware = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Nombre_Software);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsNombreSoftware(String nombreSoftware) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Nombre_Software)"
                    + " FROM "
                    + " NOMBRE_SOFTWARE"
                    + " WHERE nombre = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSoftware);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_nombreSoftware() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " S.ID_Nombre_Software "
                    + " ,S.nombre"
                    + " FROM NOMBRE_SOFTWARE  S"
                    + " ORDER BY S.nombre ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Grupo(String ID_Grupo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , D.ID_Departamento"
                    + " , D.nombredepartamento"
                    + " , G.nombregrupo"
                    + " , PDG.ID_PDG"
                    + " , DP.ID_Departamento_Plantel"
                    + " FROM"
                    + " GRUPO G"
                    + " ,  RELACION_PDG PDG"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " WHERE"
                    + " PDG.FK_ID_Grupo=G.ID_Grupo"
                    + " AND PDG.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND G.ID_grupo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Grupo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_GrupoBien(String ID_Grupo_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " TOP 1"
                    + " GB.FK_ID_Grupo"
                    + " , GB.FK_ID_Bien"
                    + " FROM"
                    + " GRUPO_BIEN GB"
                    + " WHERE"
                    + " GB.ID_Grupo_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Grupo_Bien);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_GrupoBien4Bien(String FK_ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " GB.FK_ID_Grupo"
                    + " , GB.ID_Grupo_Bien"
                    + " FROM"
                    + " GRUPO_BIEN GB"
                    + " WHERE"
                    + " GB.FK_ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bien(String ID_Plantel, boolean seeAll, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , MR.ID_Marca"
                    + " , MR.Marca"
                    + " , M.ID_Modelo"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.fechaCompra"
                    + " , B.fechaAlta"
                    + " , B.status"
                    + " , B.observaciones"
                    + " , D.nombreDepartamento"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " LEFT JOIN DEPARTAMENTO D ON DP.FK_ID_Departamento=D.ID_Departamento AND DP.FK_ID_Plantel=P.ID_Plantel";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " WHERE B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " WHERE B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!checkStatus && !seeAll) {
                SQLSentence += " WHERE P.ID_Plantel = ?";
            } else if (checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (checkStatus) {
                pstmt.setString(1, estatus);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(1, ID_Plantel);
            } else if (checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bien4Subcategoria(String ID_Plantel, boolean seeAll, String estatus, boolean igual, boolean checkStatus, String[] idSubcategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , MR.ID_Marca"
                    + " , MR.Marca"
                    + " , M.ID_Modelo"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.fechaCompra"
                    + " , B.fechaAlta"
                    + " , B.status"
                    + " , B.observaciones"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " WHERE B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " WHERE B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!checkStatus && !seeAll) {
                SQLSentence += " WHERE P.ID_Plantel = ?";
            } else if (checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " AND ( ";
            for (int i = 0; i < idSubcategoria.length; i++) {
                SQLSentence += " S.ID_SubCategoria = ?";
                if (i + 1 < idSubcategoria.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) "
                    + " ORDER BY S.nombreSubcategoria ASC, MR.marca ASC, M.modelo ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int paramIndex = 0;
            if (checkStatus) {
                pstmt.setString(1, estatus);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(1, ID_Plantel);
                paramIndex = 2;
            } else if (checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
                paramIndex = 3;
            }
            for (int i = 0; i < idSubcategoria.length; i++) {
                pstmt.setString(paramIndex, idSubcategoria[i]);
                paramIndex += 1;
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bien4Depto4Subcategoria(String ID_Plantel, boolean seeAll, String estatus, boolean igual, boolean checkStatus, String[] idSubcategoria, String ID_DepartamentoPlatel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , MR.ID_Marca"
                    + " , MR.Marca"
                    + " , M.ID_Modelo"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.fechaCompra"
                    + " , B.fechaAlta"
                    + " , B.status"
                    + " , B.observaciones"
                    + " , D.ID_Departamento"
                    + " , D.nombreDepartamento"
                    + " , DP.ID_Departamento_Plantel"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " INNER JOIN DEPARTAMENTO_PLANTEL DP ON DP.FK_ID_Plantel=P.ID_Plantel"
                    + " INNER JOIN DEPARTAMENTO D ON DP.FK_ID_Departamento=D.ID_Departamento AND B.FK_ID_Departamento_Plantel = DP.ID_Departamento_Plantel"
                    + " WHERE DP.ID_Departamento_Plantel = ?";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            } else if (checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " AND ( ";
            for (int i = 0; i < idSubcategoria.length; i++) {
                SQLSentence += " S.ID_SubCategoria = ?";
                if (i + 1 < idSubcategoria.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) "
                    + " ORDER BY S.nombreSubcategoria ASC, MR.marca ASC, M.modelo ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int paramIndex = 0;
            pstmt.setString(1, ID_DepartamentoPlatel);
            if (checkStatus) {
                pstmt.setString(2, estatus);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
                paramIndex = 3;
            } else if (checkStatus && !seeAll) {
                pstmt.setString(3, ID_Plantel);
                paramIndex = 4;
            }
            for (int i = 0; i < idSubcategoria.length; i++) {
                pstmt.setString(paramIndex, idSubcategoria[i]);
                paramIndex += 1;
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , MR.ID_Marca"
                    + " , MR.Marca"
                    + " , M.ID_Modelo"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.fechaCompra"
                    + " , B.fechaAlta"
                    + " , B.status"
                    + " , B.observaciones"
                    + " , DP.ID_Departamento_Plantel"
                    + " , D.ID_Departamento"
                    + " , D.nombreDepartamento"
                    + " , B.FK_ID_Tipo_Compra"
                    + " , G.ID_Grupo"
                    + "  , G.nombreGrupo"
                    + "  , GB.ID_Grupo_Bien"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " LEFT JOIN DEPARTAMENTO D ON DP.FK_ID_Departamento=D.ID_Departamento AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN GRUPO_BIEN GB ON GB.FK_ID_Bien = B.ID_Bien"
                    + " LEFT JOIN GRUPO G ON GB.FK_ID_Grupo = G.ID_Grupo"
                    + " WHERE B.ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
                listToSend.add(rs.getString(19));
                listToSend.add(rs.getString(20));
                listToSend.add(rs.getString(21));
                listToSend.add(rs.getString(22));
                listToSend.add(rs.getString(23));
                listToSend.add(rs.getString(24));
                listToSend.add(rs.getString(25));
                listToSend.add(rs.getString(26));
                listToSend.add(rs.getString(27));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_BienXID_ModeloXSubCategoria(String ID_Modelo, String ID_Subcategoria, String status, boolean statusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " B.ID_Bien"
                    + " ,B.noSerie"
                    + " ,B.noInventario"
                    + " ,B.status"
                    + " FROM "
                    + " BIEN B"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " WHERE"
                    + " B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND SC.ID_SubCategoria = ?"
                    + " AND MD.ID_Modelo = ?";
            if (statusEqual) {
                SQLSentence += " AND B.status = ? ";
            } else {
                SQLSentence += " AND B.status <> ? ";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Subcategoria);
            pstmt.setString(2, ID_Modelo);
            pstmt.setString(3, status);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_BienXID_ModeloXSubCategoriaXDepartamento(
            String ID_Modelo,
            LinkedList ID_Subcategoria,
            String ID_Plantel,
            String ID_Departamento,
            String status,
            boolean statusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " B.ID_Bien"
                    + " ,B.noSerie"
                    + " ,B.noInventario"
                    + " ,B.status"
                    + " FROM "
                    + " BIEN B"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , PLANTEL P"
                    + " , DEPARTAMENTO D"
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " WHERE"
                    + " B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND MD.ID_Modelo = ?"
                    + " AND DP.ID_Departamento_Plantel = ?";
            if (statusEqual) {
                SQLSentence += " AND B.status = ? ";
            } else {
                SQLSentence += " AND B.status <> ? ";
            }
            SQLSentence += " AND (";
            for (int i = 0; i < ID_Subcategoria.size(); i++) {
                SQLSentence += "  SC.ID_SubCategoria = ?";
                if (i + 1 < ID_Subcategoria.size()) {
                    SQLSentence += " OR ";
                }

            }
            SQLSentence += ")";
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ? ";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Modelo);
            pstmt.setString(2, ID_Departamento);
            pstmt.setString(3, status);
            int i = 4;
            for (; i - 4 < ID_Subcategoria.size(); i++) {
                pstmt.setString(i, ID_Subcategoria.get(i - 4).toString());
            }
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(i, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_BienSinGrupo(String ID_Plantel, boolean seeAll, String statusBien, boolean igualar, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , MR.ID_Marca"
                    + " , MR.Marca"
                    + " , M.ID_Modelo"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.fechaCompra"
                    + " , B.fechaAlta"
                    + " , B.status"
                    + " , B.observaciones"
                    + " , DP.ID_Departamento_Plantel"
                    + " , D.ID_Departamento"
                    + " , D.nombreDepartamento"
                    + " , B.FK_ID_Tipo_Compra"
                    + " , G.ID_Grupo"
                    + "  , G.nombreGrupo"
                    + "  , GB.ID_Grupo_Bien"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " LEFT JOIN DEPARTAMENTO D ON DP.FK_ID_Departamento=D.ID_Departamento AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN GRUPO_BIEN GB ON GB.FK_ID_Bien = B.ID_Bien"
                    + " LEFT JOIN GRUPO G ON GB.FK_ID_Grupo = G.ID_Grupo"
                    + " WHERE G.ID_Grupo IS NULL ";
//            if (!seeAll) {
//                SQLSentence += " AND P.ID_Plantel = ? ";
//            }
            if (checkStatus) {
                if (igualar) {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            } else if (checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
//            if (!seeAll) {
//                pstmt.setString(1, ID_Plantel);
//            }
            if (checkStatus) {
                pstmt.setString(1, statusBien);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(1, ID_Plantel);
            } else if (checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                aux.add(rs.getString(26));
                aux.add(rs.getString(27));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SubCategoria4Categoria(String ID_Categoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " S.ID_SubCategoria"
                    + " ,S.nombreSubCategoria"
                    + " ,C.nombreCategoria"
                    + " FROM SUBCATEGORIA  S "
                    + " , CATEGORIA  C "
                    + " Where "
                    + " S.FK_ID_Categoria = C.ID_Categoria"
                    + " AND C.ID_Categoria = ?"
                    + " ORDER BY S.nombreSubCategoria ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Categoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Modelo4Marca(String ID_Marca) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + "SELECT"
                    + " MO.ID_Modelo"
                    + " ,MO.modelo"
                    + " ,MO.descripcion"
                    + " ,M.marca"
                    + " FROM MARCA  M "
                    + " , MODELO  MO "
                    + " Where "
                    + " MO.FK_ID_Marca = M.ID_Marca";

            if (!ID_Marca.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND M.ID_Marca = ?";
            }

            SQLSentence += " ORDER BY MO.modelo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!ID_Marca.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, ID_Marca);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Detalle4Subcategoria(String ID_Subcategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " D.ID_Detalle"
                    + " , D.nombreDetalle"
                    + " , S.ID_Subcategoria"
                    + " , S.nombreSubcategoria"
                    + " , S.FK_ID_Categoria"
                    + " , DS.ID_Detalle_Subcategoria"
                    + " FROM DETALLE D"
                    + " ,  SUBCATEGORIA S"
                    + " ,  DETALLE_SUBCATEGORIA DS"
                    + " WHERE"
                    + " DS.FK_ID_Detalle=D.ID_Detalle"
                    + " AND DS.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " AND S.ID_Subcategoria = ?"
                    + " ORDER BY D.nombreDetalle ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Subcategoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_Bien4Campo(String nombreCampo, String ID_Bien) {
        String valorCampo = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + nombreCampo
                    + " FROM BIEN"
                    + " WHERE"
                    + " ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                valorCampo = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valorCampo;
    }

    public final int select_CountBien4Campo(String nombreCampo, String valor) {
        int count = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(" + nombreCampo + ")"
                    + " FROM BIEN"
                    + " WHERE "
                    + " " + nombreCampo + " = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, valor);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final LinkedList select_Detalle4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " BDS.ID_Relacion_BDS"
                    + " , BDS.FK_ID_Valor"
                    + " , BDS.FK_ID_Detalle_SubCategoria"
                    + " , D.ID_Detalle"
                    + " , D.nombreDetalle"
                    + " , V.valor"
                    + " FROM"
                    + " RELACION_BDS BDS"
                    + " ,  DETALLE_SUBCATEGORIA DS"
                    + " ,  DETALLE D"
                    + " ,  VALOR V"
                    + " , BIEN B"
                    + " WHERE"
                    + " BDS.FK_ID_Bien=B.ID_Bien"
                    + " AND BDS.FK_ID_Detalle_Subcategoria=DS.ID_Detalle_SubCategoria"
                    + " AND BDS.FK_ID_Valor=V.ID_Valor"
                    + " AND DS.FK_ID_Detalle=D.ID_Detalle"
                    + " AND V.FK_ID_Detalle=D.ID_Detalle"
                    + " AND B.ID_Bien = ?"
                    + " ORDER BY D.nombreDetalle ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_RelacionBDS(String ID_Bien, String FK_ID_DetalleSubcategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " BDS.FK_ID_Valor"
                    + " FROM"
                    + " RELACION_BDS BDS"
                    + " WHERE"
                    + " BDS.FK_ID_Bien=?"
                    + " AND BDS.FK_ID_Detalle_SubCategoria=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Bien);
            pstmt.setString(2, FK_ID_DetalleSubcategoria);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Grupo4DeptoPlantel(String ID_DepartamentoPlantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " G.ID_grupo"
                    + " ,G.nombreGrupo"
                    + " ,PDG.ID_PDG"
                    + " FROM RELACION_PDG PDG"
                    + " ,GRUPO G"
                    + " WHERE"
                    + " PDG.FK_ID_Grupo=G.ID_Grupo"
                    + " AND PDG.FK_ID_Departamento_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_DepartamentoPlantel);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Grupo4PDG(String ID_PDG) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " G.ID_grupo"
                    + " ,G.nombreGrupo"
                    + " FROM RELACION_PDG PDG"
                    + " ,GRUPO G"
                    + " ,DEPARTAMENTO_PLANTEL AS DP"
                    + " WHERE"
                    + " PDG.FK_ID_Grupo=G.ID_Grupo"
                    + " AND PDG.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND PDG.ID_PDG=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PDG);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bien4Grupo(String ID_Grupo, String statusBien, boolean igualar, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " B.ID_Bien"
                    + " , B.DescripcionBien"
                    + " , B.noSerie"
                    + " , B.noDictamen"
                    + " , B.noFactura"
                    + " , B.noInventario"
                    + " , B.status"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " , C.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , G.ID_Grupo"
                    + " , G.nombreGrupo"
                    + " , GB.ID_Grupo_Bien"
                    + " FROM"
                    + " GRUPO_BIEN GB"
                    + " , GRUPO G"
                    + " , BIEN B"
                    + " , MARCA MR"
                    + " ,  MODELO M"
                    + " ,  CATEGORIA C"
                    + " ,  SUBCATEGORIA SC"
                    + " WHERE"
                    + " GB.FK_ID_Grupo=G.ID_Grupo"
                    + " AND GB.FK_ID_Bien=B.ID_Bien"
                    + " AND B.FK_ID_Subcategoria = SC.ID_Subcategoria"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Modelo=M.ID_Modelo"
                    + " AND M.FK_ID_Marca=MR.ID_Marca"
                    + " AND G.ID_Grupo=?";
            if (checkStatus) {
                if (igualar) {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Grupo);
            if (checkStatus) {
                pstmt.setString(2, statusBien);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getString(14));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PBT(String ID_Bien, String ID_TipoProveedor) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Proveedor"
                    + " ,P.nombreProveedor"
                    + " ,P.telefono"
                    + " ,P.correo"
                    + " ,TP.ID_Tipo_Proveedor"
                    + " ,TP.nombreTipo"
                    + " ,B.ID_Bien"
                    + " ,PBT.ID_PBT"
                    + " FROM"
                    + " BIEN B"
                    + " , PROVEEDOR P"
                    + " , TIPO_PROVEEDOR TP"
                    + " , RELACION_PBT PBT"
                    + " WHERE"
                    + " PBT.FK_ID_Proveedor=P.ID_Proveedor"
                    + " AND PBT.FK_ID_Tipo_Proveedor=TP.ID_Tipo_Proveedor"
                    + " AND PBT.FK_ID_Bien=B.ID_Bien"
                    + " AND B.ID_Bien = ?"
                    + " AND TP.ID_Tipo_Proveedor = ?"
                    + " ORDER BY P.nombreProveedor ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            pstmt.setString(2, ID_TipoProveedor);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PBT(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Proveedor"
                    + " ,P.nombreProveedor"
                    + " ,P.telefono"
                    + " ,P.correo"
                    + " ,TP.ID_Tipo_Proveedor"
                    + " ,TP.nombreTipo"
                    + " ,B.ID_Bien"
                    + " ,PBT.ID_PBT"
                    + " FROM"
                    + " BIEN B"
                    + " , PROVEEDOR P"
                    + " , TIPO_PROVEEDOR TP"
                    + " , RELACION_PBT PBT"
                    + " WHERE"
                    + " PBT.FK_ID_Proveedor=P.ID_Proveedor"
                    + " AND PBT.FK_ID_Tipo_Proveedor=TP.ID_Tipo_Proveedor"
                    + " AND PBT.FK_ID_Bien=B.ID_Bien"
                    + " AND B.ID_Bien = ?"
                    + " ORDER BY P.nombreProveedor ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Garantia4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " B.ID_Bien"
                    + " ,B.noSerie"
                    + " ,M.Marca"
                    + " ,MD.Modelo"
                    + " ,C.nombreCategoria"
                    + " ,SC.nombreSubcategoria"
                    + " ,TG.Garantia"
                    + " ,G.ID_Garantia"
                    + " ,G.fechaInicio"
                    + " ,G.fechaFin"
                    + " FROM"
                    + " BIEN B"
                    + " , GARANTIA G"
                    + " , TIPO_GARANTIA TG"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " WHERE"
                    + " MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Subcategoria=SC.ID_Subcategoria"
                    + " AND G.FK_ID_Bien=B.ID_Bien"
                    + " AND G.FK_ID_Tipo_Garantia=TG.ID_Tipo_Garantia"
                    + " AND B.ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountGarantia(String ID_Bien, String FK_ID_Tipo) {
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(B.ID_Bien)"
                    + " FROM"
                    + " BIEN B"
                    + " , GARANTIA G"
                    + " , TIPO_GARANTIA TG"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " WHERE"
                    + " MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Subcategoria=SC.ID_Subcategoria"
                    + " AND G.FK_ID_Bien=B.ID_Bien"
                    + " AND G.FK_ID_Tipo_Garantia=TG.ID_Tipo_Garantia"
                    + " AND B.ID_Bien = ?"
                    + " AND TG.ID_Tipo_Garantia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            pstmt.setString(2, FK_ID_Tipo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_Garantia(String ID_Garantia) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " B.ID_Bien"
                    + " ,B.noSerie"
                    + " ,M.Marca"
                    + " ,MD.Modelo"
                    + " ,C.nombreCategoria"
                    + " ,SC.nombreSubcategoria"
                    + " ,TG.Garantia"
                    + " ,G.ID_Garantia"
                    + " ,G.fechaInicio"
                    + " ,G.fechaFin"
                    + " ,TG.ID_Tipo_Garantia"
                    + " FROM"
                    + " BIEN B"
                    + " , GARANTIA G"
                    + " , TIPO_GARANTIA TG"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " WHERE"
                    + " MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Subcategoria=SC.ID_Subcategoria"
                    + " AND G.FK_ID_Bien=B.ID_Bien"
                    + " AND G.FK_ID_Tipo_Garantia=TG.ID_Tipo_Garantia"
                    + " AND G.ID_Garantia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Garantia);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Solicitud(String FK_ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " S.ID_Solicitud"
                    + " ,S.numeroOficio"
                    + " ,S.nombreSolicitante"
                    + " ,S.fechaSolicitud"
                    + " ,S.nombreResponsable"
                    + " ,S.status"
                    + " ,S.observaciones"
                    + " ,S.FK_ID_Plantel"
                    + " ,P.nombre"
                    + " FROM"
                    + " SOLICITUD S"
                    + " ,PLANTEL P"
                    + " WHERE"
                    + " S.FK_ID_Plantel=P.ID_Plantel"
                    + " AND S.tipoSolicitud = ?";
            if (!seeAll) {
                SQLSentence += " AND S.FK_ID_Plantel = ? ";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, "BAJA");
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(2, FK_ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Solicitud(String ID_Solicitud) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " S.numeroOficio"
                    + " ,S.nombreSolicitante"
                    + " ,S.fechaSolicitud"
                    + " ,S.nombreResponsable"
                    + " ,S.status"
                    + " ,S.observaciones"
                    + " ,S.FK_ID_Plantel"
                    + " ,P.nombre"
                    + " ,P.ID_Plantel"
                    + " FROM"
                    + " SOLICITUD S"
                    + " ,PLANTEL P"
                    + " WHERE"
                    + " S.FK_ID_Plantel=P.ID_Plantel"
                    + " AND S.ID_Solicitud = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Solicitud);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SolicitudBaja(String ID_Solicitud) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " P.ID_Plantel"
                    + " , P.Nombre"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , SC.ID_Subcategoria"
                    + " , SC.nombreSubcategoria"
                    + " , M.ID_Marca"
                    + " , M.Marca"
                    + " , MD.ID_Modelo"
                    + " , MD.modelo"
                    + " , B.ID_Bien"
                    + " , B.descripcionBien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , B.status"
                    + " , BJ.ID_Baja"
                    + " , BJ.motivoBaja"
                    + " , SB.ID_Solicitud_Baja"
                    + " FROM"
                    + " BIEN B"
                    + " ,MARCA M"
                    + " ,MODELO MD"
                    + " ,CATEGORIA C"
                    + " ,SUBCATEGORIA SC"
                    + " ,BAJA BJ"
                    + " ,SOLICITUD S"
                    + " ,SOLICITUD_BAJA SB"
                    + " ,PLANTEL P"
                    + " WHERE"
                    + " MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Subcategoria=SC.ID_Subcategoria"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND BJ.FK_ID_Bien=B.ID_Bien"
                    + " AND SB.FK_ID_Baja=BJ.ID_Baja"
                    + " AND SB.FK_ID_Solicitud=S.ID_Solicitud"
                    + " AND S.FK_ID_Plantel=P.ID_Plantel"
                    + " AND S.ID_Solicitud = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Solicitud);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_searchBien(String idPlantel, String busqueda, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;
        try {
            busqueda = busqueda.trim();
            busqueda = busqueda.replaceAll("\\s\\s", "\\s");
            String parametrosBusqueda[] = busqueda.split("\\s");

            SQLSentence = " SELECT TOP 15"
                    + " B.ID_Bien"
                    + " , (S.nombreSubcategoria + ' '"
                    + " + MR.Marca + ' '"
                    + " + M.modelo + ' '"
                    + " + 'No Serie: '"
                    + " + B.noSerie)"
                    + " FROM"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " WHERE  (";

            for (int i = 0; i < parametrosBusqueda.length;) {
                SQLSentence += "  B.noSerie LIKE ? OR B.noInventario LIKE ? OR M.modelo COLLATE SQL_Latin1_General_CP1_CI_AI like ?";
                i += 1;
                if (i < parametrosBusqueda.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence = SQLSentence + ") AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> 'Baja'";
            if (!seeAll) {
                SQLSentence = SQLSentence + " AND B.FK_ID_Plantel = ?";
            }
            SQLSentence = SQLSentence + " ORDER BY M.modelo ASC, B.noSerie ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int noParam = 0;
            for (int i = 0; i < parametrosBusqueda.length; i++) {
                pstmt.setString(noParam + 1, "%" + parametrosBusqueda[i].toUpperCase() + "%");
                pstmt.setString(noParam + 2, "%" + parametrosBusqueda[i].toUpperCase() + "%");
                pstmt.setString(noParam + 3, "%" + parametrosBusqueda[i].toUpperCase() + "%");
                noParam += 3;
            }
            if (!seeAll) {
                pstmt.setString(parametrosBusqueda.length * 3 + 1, idPlantel);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add("");
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2).replaceAll("'", ""));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_searchConsumible(String idPlantel, String busqueda, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;
        try {
            busqueda = busqueda.trim();
            busqueda = busqueda.replaceAll("\\s\\s", "\\s");
            String parametrosBusqueda[] = busqueda.split("\\s");

            SQLSentence = ""
                    + " SELECT TOP 15 "
                    + " C.ID_Consumible"
                    + " ,(C.clave+' '"
                    + " + C.descripcion+' '"
                    + " + '('+M.medida+')'"
                    + " )"
                    + " FROM"
                    + " CONSUMIBLE C"
                    + " , PLANTEL P"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA CT"
                    + " , MEDIDA M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria = CT.ID_Categoria"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND (";

            for (int i = 0; i < parametrosBusqueda.length;) {
                SQLSentence += "  C.clave LIKE ? OR C.descripcion COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ? ";
                i += 1;
                if (i < parametrosBusqueda.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence = SQLSentence + ")";
            if (!seeAll) {
                SQLSentence = SQLSentence + " AND P.ID_Plantel = ?";
            }
            SQLSentence = SQLSentence + " ORDER BY C.descripcion ASC";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int noParam = 0;
            for (int i = 0; i < parametrosBusqueda.length; i++) {
                pstmt.setString(noParam + 1, "%" + parametrosBusqueda[i].toUpperCase() + "%");
                pstmt.setString(noParam + 2, "%" + parametrosBusqueda[i].toUpperCase() + "%");
                noParam += 2;
            }
            if (!seeAll) {
                pstmt.setString(parametrosBusqueda.length * 2 + 1, idPlantel);
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add("");
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2).replaceAll("'", ""));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Baja4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " fechaBaja"
                    + " ,noOficio"
                    + " ,observaciones"
                    + " ,motivoBaja"
                    + " ,ID_Baja"
                    + " FROM BAJA"
                    + " WHERE"
                    + " FK_ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ID_Baja4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " ID_Baja"
                    + " FROM BAJA"
                    + " WHERE"
                    + " FK_ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Garantia4TipoGarantia(
            String ID_Tipo_Garantia,
            LinkedList ID_Subcategoria,
            String estatus,
            boolean statusEqual,
            String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " M.marca"
                    + " ,MD.modelo"
                    + " ,G.fechaInicio"
                    + " ,G.fechaFin"
                    + " ,B.noFactura"
                    + " ,PV.nombreProveedor"
                    + " ,TP.nombreTipo"
                    + " ,TG.garantia"
                    + " ,COUNT(MD.modelo)"
                    + " FROM"
                    + " BIEN B"
                    + " ,MARCA M"
                    + " ,MODELO MD"
                    + " ,PROVEEDOR PV"
                    + " ,TIPO_PROVEEDOR TP"
                    + " ,RELACION_PBT PBT"
                    + " ,PLANTEL P"
                    + " ,GARANTIA G"
                    + " ,SUBCATEGORIA S"
                    + " ,CATEGORIA C"
                    + " ,TIPO_GARANTIA TG"
                    + " WHERE"
                    + " MD.FK_ID_MARCA=M.ID_MARCA"
                    + " AND B.FK_ID_MODELO=MD.ID_MODELO"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND S.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " AND G.FK_ID_Bien=B.ID_Bien"
                    + " AND G.FK_ID_Tipo_Garantia=TG.ID_Tipo_Garantia"
                    + " AND PBT.FK_ID_Bien=B.ID_Bien"
                    + " AND PBT.FK_ID_Proveedor=PV.ID_Proveedor"
                    + " AND PBT.FK_ID_Tipo_Proveedor=TP.ID_Tipo_Proveedor"
                    + " AND TP.nombreTipo COLLATE SQL_Latin1_General_CP1_CI_AI LIKE '%GARANTIA%'"
                    + " AND TG.ID_Tipo_Garantia = ?";

            if (statusEqual) {
                SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
            } else {
                SQLSentence += " AND B.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
            }
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ? ";
            }
            SQLSentence += " AND (";
            for (int i = 0; i < ID_Subcategoria.size(); i++) {
                if (i > 0) {
                    SQLSentence += " OR ";
                }
                SQLSentence += " S.ID_Subcategoria = ? ";
            }

            SQLSentence += " ) GROUP BY M.marca"
                    + " ,MD.modelo"
                    + " ,G.fechaInicio"
                    + " ,G.fechaFin"
                    + " ,B.noFactura"
                    + " ,PV.nombreProveedor"
                    + " ,TP.nombreTipo"
                    + " ,TG.garantia";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Tipo_Garantia);
            pstmt.setString(2, estatus);
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(3, FK_ID_Plantel);
            }
            int index = !FK_ID_Plantel.equalsIgnoreCase("todos") ? 4 : 3;
            for (int i = 0; i < ID_Subcategoria.size(); i++) {
                pstmt.setString(index, ID_Subcategoria.get(i).toString());
                index += 1;
            }

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_rowsSoftwareID(String noFactura,
            String FK_ID_Proveedor,
            String FK_ID_Software,
            String version,
            String ID_Plantel) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE S"
                    + " , SOFTWARE_PLANTEL SP"
                    + " , PLANTEL P"
                    + " WHERE "
                    + " SP.FK_ID_Plantel = P.ID_Plantel"
                    + " AND SP.FK_ID_Software = S.ID_Software"
                    + " AND S.noFactura = ?"
                    + " AND S.FK_ID_Proveedor = ?"
                    + " AND S.FK_ID_NombreSoftware = ?"
                    + " AND S.version = ?"
                    + " AND P.ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, noFactura);
            pstmt.setString(2, FK_ID_Proveedor);
            pstmt.setString(3, FK_ID_Software);
            pstmt.setString(4, version);
            pstmt.setString(5, ID_Plantel);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final int select_rowsSoftware(String ID_Software,
            String noFactura,
            String FK_ID_Proveedor,
            String FK_ID_NombreSoftware,
            String version
    ) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int total_filas = -1;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(ID_Software)"
                    + " FROM "
                    + " SOFTWARE"
                    + " WHERE ID_Software = ?"
                    + " AND noFactura = ?"
                    + " AND FK_ID_Proveedor = ?"
                    + " AND FK_ID_NombreSoftware = ?"
                    + " AND version = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Software);
            pstmt.setString(2, noFactura);
            pstmt.setString(3, FK_ID_Proveedor);
            pstmt.setString(4, FK_ID_NombreSoftware);
            pstmt.setString(5, version);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total_filas = rs.getInt(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total_filas = -1;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total_filas;
    }

    public final LinkedList select_idSoftware(String noDictamen) {
        LinkedList idSoftware = new LinkedList();
        Statement st = null;
        ResultSet rs = null;
        Connection conn = null;
        String LocalSQLSentence = null;
        PreparedStatement pstmt = null;
        JSpreadConnectionPool jscp = null;

        try {
            LocalSQLSentence = ""
                    + " SELECT"
                    + " ID_Software"
                    + " FROM SOFTWARE"
                    + " WHERE"
                    + " noDictamen = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(LocalSQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, noDictamen);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getInt(1));
                idSoftware.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            idSoftware = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idSoftware;
    }

    public final LinkedList select_SoftwareXID(String ID_Software) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " S.ID_Software"
                    + " ,S.FK_ID_NombreSoftware"
                    + " ,S.version"
                    + " ,S.serial"
                    + " ,S.fechaVencimiento"
                    + " ,S.fechaAdquisicion"
                    + " ,S.noDictamen"
                    + " ,S.noLicencias"
                    + " ,S.hddRequerido"
                    + " ,S.ramRequerida"
                    + " ,S.soRequerido"
                    + " ,S.soporteTecnico"
                    + " ,S.emailSoporteTecnico"
                    + " ,S.telefonoSoporte"
                    + " ,S.FK_ID_Tipo_Software"
                    + " ,S.FK_ID_Licencia"
                    + " ,S.FK_ID_Tipo_Compra"
                    + " ,S.observaciones"
                    + " ,S.nombreArchivo"
                    + " ,TS.ID_Tipo_Software"
                    + " ,TS.tipo"
                    + " ,L.ID_Licencia"
                    + " ,L.licencia"
                    + " ,TC.ID_Tipo_Compra"
                    + " ,TC.compra"
                    + " ,NS.nombre"
                    + " ,NS.ID_Nombre_Software"
                    + " ,S.status"
                    + " ,SP.noLicenciasAsignadas"
                    + " ,P.nombre"
                    + " ,P.ID_Plantel"
                    + " ,SP.FK_ID_Plantel"
                    + " ,SP.nombreResponsable"
                    + " ,SP.ID_Software_Plantel"
                    + " ,PR.ID_Proveedor"
                    + " ,PR.nombreProveedor"
                    + " ,S.noFactura"
                    + " ,S.noContrato"
                    + " ,S.noAutorizacion"
                    + " ,S.fechaInstalacion"
                    + " ,S.aniosLicencia"
                    + " ,S.noActualizacionesPermitidas"
                    + " ,S.upgrade"
                    + " ,S.degrade"
                    + " FROM SOFTWARE  S"
                    + " ,TIPO_SOFTWARE  TS"
                    + " ,LICENCIA  L"
                    + " ,TIPO_COMPRA  TC"
                    + " ,NOMBRE_SOFTWARE NS"
                    + " ,PLANTEL P"
                    + " ,SOFTWARE_PLANTEL SP"
                    + " ,PROVEEDOR PR"
                    + " WHERE"
                    + "   SP.FK_ID_Software = S.ID_Software"
                    + " AND S.FK_ID_Tipo_Software= TS.ID_Tipo_Software"
                    + " AND S.FK_ID_Licencia= L.ID_Licencia"
                    + " AND S.FK_ID_Tipo_Compra = TC.ID_Tipo_Compra"
                    + " AND S.FK_ID_NombreSoftware=NS.ID_Nombre_Software"
                    + " AND SP.FK_ID_Plantel = P.ID_Plantel "
                    + " AND S.FK_ID_Proveedor = PR.ID_Proveedor"
                    + " AND S.ID_Software = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Software);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getInt(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getInt(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getInt(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getString(14));
                llaux.add(rs.getInt(15));
                llaux.add(rs.getInt(16));
                llaux.add(rs.getInt(17));
                llaux.add(rs.getString(18));
                llaux.add(rs.getString(19));
                llaux.add(rs.getInt(20));
                llaux.add(rs.getString(21));
                llaux.add(rs.getInt(22));
                llaux.add(rs.getString(23));
                llaux.add(rs.getInt(24));
                llaux.add(rs.getString(25));
                llaux.add(rs.getString(26));
                llaux.add(rs.getInt(27));
                llaux.add(rs.getString(28));
                llaux.add(rs.getString(29));
                llaux.add(rs.getString(30));
                llaux.add(rs.getString(31));
                llaux.add(rs.getInt(32));
                llaux.add(rs.getString(33));
                llaux.add(rs.getString(34));
                llaux.add(rs.getInt(35));
                llaux.add(rs.getString(36));
                llaux.add(rs.getString(37));
                llaux.add(rs.getString(38));
                llaux.add(rs.getString(39));
                llaux.add(rs.getString(40));
                llaux.add(rs.getString(41));
                llaux.add(rs.getString(42));
                llaux.add(rs.getString(43));
                llaux.add(rs.getString(44));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_getRowsNombreSoftware() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Nombre_Software"
                    + " ,nombre"
                    + " FROM "
                    + " NOMBRE_SOFTWARE";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Software(String idPlantel, Boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " S.ID_Software"
                    + " ,S.version"
                    + " ,S.serial"
                    + " ,S.fechaVencimiento"
                    + " ,S.fechaAdquisicion"
                    + " ,S.noDictamen"
                    + " ,S.noLicencias"
                    + " ,S.hddRequerido"
                    + " ,S.ramRequerida"
                    + " ,S.soRequerido"
                    + " ,S.soporteTecnico"
                    + " ,S.emailSoporteTecnico"
                    + " ,S.telefonoSoporte"
                    + " ,S.FK_ID_Tipo_Software"
                    + " ,S.FK_ID_Licencia"
                    + " ,S.FK_ID_Tipo_Compra"
                    + " ,S.observaciones"
                    + " ,S.nombreArchivo"
                    + " ,TS.ID_Tipo_Software"
                    + " ,TS.tipo"
                    + " ,L.ID_Licencia"
                    + " ,L.licencia"
                    + " ,TC.ID_Tipo_Compra"
                    + " ,TC.compra"
                    + " ,NS.ID_Nombre_Software"
                    + " ,NS.nombre"
                    + " ,SP.ID_Software_Plantel"
                    + " ,P.nombre"
                    + " ,PR.nombreProveedor"
                    + " ,S.fechaInstalacion"
                    + " FROM SOFTWARE  S"
                    + " ,TIPO_SOFTWARE  TS"
                    + " ,LICENCIA  L"
                    + " ,TIPO_COMPRA  TC"
                    + " ,NOMBRE_SOFTWARE NS"
                    + " ,SOFTWARE_PLANTEL SP"
                    + " ,PROVEEDOR PR"
                    + " ,PLANTEL P"
                    + " WHERE"
                    + " S.FK_ID_NombreSoftware = NS.ID_Nombre_Software"
                    + " AND S.FK_ID_Tipo_Software = TS.ID_Tipo_Software"
                    + " AND S.FK_ID_Licencia = L.ID_Licencia"
                    + " AND S.FK_ID_Tipo_Compra = TC.ID_Tipo_Compra"
                    + " AND  SP.FK_ID_Software = S.ID_Software"
                    + " AND SP.FK_ID_Plantel = P.ID_Plantel"
                    + " AND S.FK_ID_Proveedor = PR.ID_Proveedor";
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " ORDER BY P.nombre ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, idPlantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getInt(14));
                llaux.add(rs.getInt(15));
                llaux.add(rs.getInt(16));
                llaux.add(rs.getString(17));
                llaux.add(rs.getString(18));
                llaux.add(rs.getInt(19));
                llaux.add(rs.getString(20));
                llaux.add(rs.getInt(21));
                llaux.add(rs.getString(22));
                llaux.add(rs.getInt(23));
                llaux.add(rs.getString(24));
                llaux.add(rs.getInt(25));
                llaux.add(rs.getString(26));
                llaux.add(rs.getInt(27));
                llaux.add(rs.getString(28));
                llaux.add(rs.getString(29));
                llaux.add(rs.getString(30));

                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountID_BitacoraIncidente() {
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " COUNT(ID_Bitacora_Incidente)"
                    + " FROM"
                    + " BITACORA_INCIDENTE";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_NombreEncargadoPlantel(String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Personal"
                    + " ,P.nombre"
                    + " ,P.aPaterno"
                    + " ,P.aMaterno"
                    + " FROM "
                    + " PERSONAL P"
                    + " ,PERSONAL_PLANTEL PP"
                    + " ,PLANTEL PL"
                    + " WHERE PP.FK_ID_Personal = P.ID_Personal"
                    + " AND PP.FK_ID_Plantel = PL.ID_Plantel"
                    + " AND PP.FK_ID_Plantel = ?"
                    + " AND PP.estatus COLLATE SQL_Latin1_General_CP1_CI_AI ='Activo'"
                    + " AND (PP.cargo = 'Coordinador de Plantel'"
                    + " OR PP.cargo = 'Director de Plantel'"
                    + " OR PP.cargo = 'Subdirector de Plantel')";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Personal4Plantel(String FK_ID_Plantel, String[] cargo, String status, boolean checkStatus, boolean statusEqual, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Personal"
                    + " ,P.nombre"
                    + " ,P.aPaterno"
                    + " ,P.aMaterno"
                    + " ,P.telefono"
                    + " ,P.correo"
                    + " ,P.titulo"
                    + " ,P.curp"
                    + " ,P.fechaAlta"
                    + " ,P.siglasTitulo"
                    + " ,PP.cargo"
                    + " ,PP.estatus"
                    + " ,PL.ID_Plantel"
                    + " ,PL.nombre"
                    + " ,PP.ID_Personal_Plantel"
                    + " FROM "
                    + " PERSONAL P"
                    + " ,PERSONAL_PLANTEL PP"
                    + " ,PLANTEL PL"
                    + " WHERE "
                    + " PP.FK_ID_Personal = P.ID_Personal"
                    + " AND PP.FK_ID_Plantel = PL.ID_Plantel";
            if (checkStatus) {
                if (statusEqual) {
                    SQLSentence += " AND PP.estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND PP.estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!seeAll) {
                SQLSentence += " AND PL.ID_Plantel = ?";
            }
            if (cargo.length > 0) {
                SQLSentence += " AND ( ";
                for (int i = 0; i < cargo.length; i++) {
                    SQLSentence += " PP.cargo = ? ";
                    if (i + 1 < cargo.length) {
                        SQLSentence += " OR ";
                    }
                }
                SQLSentence += " ) ";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int parameterIndex = 1;
            if (checkStatus) {
                pstmt.setString(1, status);
                parameterIndex = 2;
                if (!seeAll) {
                    pstmt.setString(2, FK_ID_Plantel);
                    parameterIndex = 3;
                }
            } else if (!seeAll) {
                pstmt.setString(1, FK_ID_Plantel);
                parameterIndex = 2;
            }
            for (int i = 0; i < cargo.length; i++) {
                pstmt.setString(parameterIndex, cargo[i]);
                parameterIndex += 1;
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                llaux.add(rs.getString(10));
                llaux.add(rs.getString(11));
                llaux.add(rs.getString(12));
                llaux.add(rs.getString(13));
                llaux.add(rs.getString(14));
                llaux.add(rs.getString(15));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_CheckList() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " ID_CheckList"
                    + " ,descripcion"
                    + " FROM CHECKLIST";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_CheckList(String ID_CheckList) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " descripcion"
                    + " FROM CHECKLIST"
                    + " WHERE "
                    + " ID_CheckList = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_CheckList);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Rubro4CheckList(String ID_CheckList) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " C.descripcion"
                    + " , R.ID_Rubro"
                    + " , R.rubro"
                    + " , CR.categoria"
                    + " , CR.ID_CheckList_Rubro"
                    + " FROM"
                    + " CHECKLIST C"
                    + " , RUBRO R"
                    + " , CHECKLIST_RUBRO CR"
                    + " WHERE"
                    + " CR.FK_ID_Rubro=R.ID_Rubro"
                    + " AND CR.FK_ID_CheckList=C.ID_CheckList"
                    + " AND C.ID_CheckList = ?"
                    + " ORDER BY CR.categoria ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_CheckList);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_CheckList4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " DISTINCT(CL.ID_CHECKLIST)"
                    + " , CL.DESCRIPCION"
                    + " FROM"
                    + " RELACION_CRB CRB"
                    + " ,  CHECKLIST_RUBRO CR"
                    + " ,  CHECKLIST CL"
                    + " WHERE"
                    + " CRB.FK_ID_CHECKLIST_RUBRO=CR.ID_CHECKLIST_RUBRO"
                    + " AND CR.FK_ID_CHECKLIST=CL.ID_CHECKLIST"
                    + " AND CRB.FK_ID_BIEN = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Bien);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Rubro4Bien(String ID_Bien, String ID_CheckList) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " RBR.RUBRO"
                    + " , CHK.ID_CRB"
                    + " , RBR.ID_CheckList_Rubro"
                    + " , RBR.categoria"
                    + " FROM"
                    + " ("
                    + " SELECT"
                    + " RB.ID_Rubro"
                    + " , RB.rubro"
                    + " , CHKR.ID_CheckList_Rubro"
                    + " , CHKR.categoria"
                    + " FROM"
                    + " RUBRO RB"
                    + " ,  CHECKLIST_RUBRO CHKR"
                    + " ,  CHECKLIST CLT"
                    + " WHERE"
                    + " CHKR.FK_ID_CheckList=CLT.ID_CheckList"
                    + " AND CHKR.FK_ID_Rubro=RB.ID_Rubro"
                    + " AND CLT.ID_CheckList = ?) RBR LEFT JOIN"
                    + " (SELECT"
                    + " R.ID_Rubro"
                    + " , CL.ID_CheckList"
                    + " , CR.ID_CheckList_Rubro"
                    + " , CRB.ID_CRB"
                    + " , R.Rubro"
                    + " , B.ID_Bien"
                    + " , CR.FK_ID_Rubro"
                    + " FROM"
                    + " RELACION_CRB CRB"
                    + " ,  CHECKLIST_RUBRO CR"
                    + " ,  RUBRO R"
                    + " ,  CHECKLIST CL"
                    + " ,  BIEN B"
                    + " WHERE"
                    + " CRB.FK_ID_Bien=B.ID_Bien"
                    + " AND CRB.FK_ID_CheckList_Rubro=CR.ID_CheckList_Rubro"
                    + " AND CR.FK_ID_Rubro=R.ID_Rubro"
                    + " AND CR.FK_ID_CheckList=CL.ID_CheckList"
                    + " AND B.ID_Bien = ? AND CL.ID_CheckList = ?) CHK ON CHK.FK_ID_Rubro=RBR.ID_Rubro"
                    + " ORDER BY RBR.categoria ASC, RBR.rubro ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_CheckList);
            pstmt.setString(2, ID_Bien);
            pstmt.setString(3, ID_CheckList);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_CheckListRubro4CheckLIist(String ID_CheckList) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " CR.ID_CheckList_Rubro"
                    + " , CR.FK_ID_Rubro"
                    + " , CR.categoria"
                    + " FROM"
                    + " CHECKLIST_RUBRO CR"
                    + " , CHECKLIST C"
                    + " WHERE"
                    + " CR.FK_ID_CheckList=C.ID_CheckList"
                    + " AND CR.FK_ID_CheckList = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_CheckList);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_CheckListNotBien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " CL.ID_CheckList"
                    + " , CL.descripcion"
                    + " FROM"
                    + " CHECKLIST CL"
                    + " WHERE NOT EXISTS"
                    + " (SELECT"
                    + " * "
                    + " FROM"
                    + " RELACION_CRB CRB"
                    + " ,   CHECKLIST_RUBRO CR"
                    + " WHERE"
                    + " CRB.FK_ID_CheckList_Rubro=CR.ID_CheckList_Rubro"
                    + " AND CR.FK_ID_CheckList=CL.ID_CheckList"
                    + " AND CRB.FK_ID_Bien = ?) ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Bien);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Incidente() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " ID_Incidente"
                    + " ,incidente"
                    + " FROM INCIDENTE";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bitacora_Incidente(String ID_Plantel, boolean seeAll, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " BI.ID_Bitacora_Incidente"
                    + " , P.nombre"
                    + " , D.nombreDepartamento"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , BI.noReporte"
                    + " , BI.fechaCreacion"
                    + " , BI.fechaAtencion"
                    + " , BI.observaciones"
                    + " , BI.accion"
                    + " , BI.FK_ID_Incidente"
                    + " , BI.status"
                    + " , BI.prioridad"
                    + " , I.incidente"
                    + " FROM"
                    + " BIEN B"
                    + " ,  MARCA M"
                    + " ,  MODELO MD"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  INCIDENTE I"
                    + " ,  BITACORA_INCIDENTE BI"
                    + " WHERE"
                    + " B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND BI.FK_ID_Incidente=I.ID_Incidente"
                    + " AND BI.FK_ID_Bien=B.ID_Bien";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND BI.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND BI.status COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            } else if (checkStatus && !seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            if (checkStatus) {
                pstmt.setString(1, estatus);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(1, ID_Plantel);
            } else if (checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bitacora_Incidente4Bien(String ID_Bien, String estatus, String fechaCreacion, String fehchaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " BI.ID_Bitacora_Incidente"
                    + " , P.nombre"
                    + " , D.nombreDepartamento"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , BI.noReporte"
                    + " , BI.fechaCreacion"
                    + " , BI.fechaAtencion"
                    + " , BI.observaciones"
                    + " , BI.accion"
                    + " , BI.FK_ID_Incidente"
                    + " , BI.status"
                    + " , BI.prioridad"
                    + " , I.incidente"
                    + " FROM"
                    + " BIEN B"
                    + " ,  MARCA M"
                    + " ,  MODELO MD"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  INCIDENTE I"
                    + " ,  BITACORA_INCIDENTE BI"
                    + " WHERE"
                    + " B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND BI.FK_ID_Incidente=I.ID_Incidente"
                    + " AND BI.FK_ID_Bien=B.ID_Bien"
                    + " AND B.ID_Bien = ?"
                    + " AND BI.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?"
                    + " AND BI.fechaCreacion >= ? AND BI.fechaCreacion <= ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Bien);
            pstmt.setString(2, estatus);
            pstmt.setString(3, fechaCreacion);
            pstmt.setString(4, fehchaFin);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bitacora_Incidente(String ID_BitacoraIncidente) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + "  P.nombre"
                    + " , D.nombreDepartamento"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , BI.noReporte"
                    + " , BI.fechaCreacion"
                    + " , BI.fechaAtencion"
                    + " , BI.observaciones"
                    + " , BI.accion"
                    + " , BI.FK_ID_Incidente"
                    + " , BI.status"
                    + " , BI.prioridad"
                    + " , I.incidente"
                    + " , C.ID_Categoria"
                    + " , C.nombreCategoria"
                    + " , SC.ID_SubCategoria"
                    + " , SC.nombreSubCategoria"
                    + " FROM"
                    + " BIEN B"
                    + " ,  MARCA M"
                    + " ,  MODELO MD"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  INCIDENTE I"
                    + " ,  BITACORA_INCIDENTE BI"
                    + " , CATEGORIA C"
                    + " , SUBCATEGORIA SC"
                    + " WHERE"
                    + " B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND BI.FK_ID_Incidente=I.ID_Incidente"
                    + " AND BI.FK_ID_Bien=B.ID_Bien"
                    + " AND BI.ID_Bitacora_Incidente = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_BitacoraIncidente);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
                listToSend.add(rs.getString(19));
                listToSend.add(rs.getString(20));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Bitacora_Incidente(String ID_Plantel, String fechaInicio, String fechaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " BI.ID_Bitacora_Incidente"
                    + " , P.nombre"
                    + " , D.nombreDepartamento"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , BI.noReporte"
                    + " , BI.fechaCreacion"
                    + " , BI.fechaAtencion"
                    + " , BI.observaciones"
                    + " , BI.accion"
                    + " , BI.FK_ID_Incidente"
                    + " , BI.status"
                    + " , BI.prioridad"
                    + " , I.incidente"
                    + " FROM"
                    + " BIEN B"
                    + " ,  MARCA M"
                    + " ,  MODELO MD"
                    + " ,  PLANTEL P"
                    + " ,  DEPARTAMENTO D"
                    + " ,  DEPARTAMENTO_PLANTEL DP"
                    + " ,  INCIDENTE I"
                    + " ,  BITACORA_INCIDENTE BI"
                    + " WHERE"
                    + " B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND BI.FK_ID_Incidente=I.ID_Incidente"
                    + " AND BI.FK_ID_Bien=B.ID_Bien "
                    + " AND BI.fechaCreacion >= ? "
                    + " AND BI.fechaCreacion <= ?";

            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, fechaInicio);
            pstmt.setString(2, fechaFin);
            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                pstmt.setString(3, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_infoPlantel() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        LinkedList llaux = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Plantel"
                    + " ,nombre "
                    + " FROM "
                    + " Plantel"
                    + " ORDER BY nombre ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            listToSend = null;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Traspaso() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " T.ID_Traspaso"
                    + " ,T.fechaEmision"
                    + " ,T.motivoTraspaso"
                    + " ,T.estatusTraspaso"
                    + " ,P.nombre"
                    + " ,T.FK_ID_Plantel"
                    + " FROM TRASPASO  T"
                    + " ,PLANTEL P"
                    + " WHERE"
                    + " T.FK_ID_Plantel = P.ID_Plantel"
                    + " ORDER BY P.nombre ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_TraspasoBien(String ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " TB.ID_Traspaso_Bien"
                    + " ,S.nombreSubcategoria"
                    + " ,MR.marca"
                    + " ,M.modelo"
                    + " ,B.noSerie"
                    + " ,B.noInventario"
                    + " ,P.nombre"
                    + " ,TB.FK_ID_Plantel_Destino"
                    + " ,T.FK_ID_Plantel"
                    + " FROM TRASPASO_BIEN  TB"
                    + " ,BIEN B"
                    + " ,PLANTEL P"
                    + " ,SUBCATEGORIA S"
                    + " ,MODELO M"
                    + " ,MARCA MR"
                    + " WHERE"
                    + " BIEN B LEFT JOIN SUBCATEGORIA S ON B.FK_ID_Subcategoria=S.ID_Subcategoria"
                    + " LEFT JOIN CATEGORIA C ON S.FK_ID_Categoria=C.ID_Categoria"
                    + " LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " LEFT JOIN TIPO_COMPRA TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " ORDER BY P.nombre ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Resguardo(String ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " PL.NOMBRE"
                    + " , P.ID_Personal"
                    + " ,  (P.nombre  + ' ' +  P.aPaterno  + ' ' +  P.aMaterno)"
                    + " , PP.ID_Personal_Plantel"
                    + " , PP.cargo"
                    + " , R.ID_Resguardo"
                    + " , R.observaciones"
                    + " , R.fechaAsignacion"
                    + " , R.fechaCambioSituacion"
                    + " , R.status"
                    + " , R.noTarjetaResguardo"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , PL.ID_Plantel"
                    + " FROM"
                    + " (PERSONAL P INNER JOIN PERSONAL_PLANTEL PP ON PP.FK_ID_PERSONAL=P.ID_PERSONAL"
                    + " INNER JOIN PLANTEL PL ON PP.FK_ID_PLANTEL=PL.ID_PLANTEL  AND PP.estatus COLLATE SQL_Latin1_General_CP1_CI_AI ='Activo')"
                    + " LEFT JOIN RESGUARDO R ON R.FK_ID_PERSONAL_PLANTEL=PP.ID_PERSONAL_PLANTEL AND R.STATUS='Activo'"
                    + " LEFT JOIN BIEN B ON R.FK_ID_BIEN=B.ID_BIEN"
                    + " LEFT JOIN MODELO M ON B.FK_ID_MODELO=M.ID_MODELO"
                    + " LEFT JOIN MARCA MR ON M.FK_ID_MARCA=MR.ID_MARCA";

            if (!seeAll) {
                SQLSentence += " WHERE PL.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Resguardo(String ID_PersonalPlantel, String status) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " PL.NOMBRE"
                    + " , P.ID_Personal"
                    + " , (P.nombre  +' '+ P.aPaterno + ' ' + P.aMaterno)"
                    + " , PP.ID_Personal_Plantel"
                    + " , PP.cargo"
                    + " , R.ID_Resguardo"
                    + " , R.observaciones"
                    + " , R.fechaAsignacion"
                    + " , R.fechaCambioSituacion"
                    + " , R.status"
                    + " , R.noTarjetaResguardo"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , PL.ID_Plantel"
                    + " FROM"
                    + " PERSONAL P"
                    + " , PLANTEL PL"
                    + " , PERSONAL_PLANTEL PP"
                    + " , RESGUARDO R"
                    + " , BIEN B"
                    + " , MODELO M"
                    + " , MARCA MR"
                    + " WHERE "
                    + " PP.FK_ID_PERSONAL=P.ID_PERSONAL"
                    + " AND PP.FK_ID_PLANTEL=PL.ID_PLANTEL"
                    + " AND R.FK_ID_PERSONAL_PLANTEL=PP.ID_PERSONAL_PLANTEL"
                    + " AND R.FK_ID_BIEN=B.ID_BIEN"
                    + " AND B.FK_ID_MODELO=M.ID_MODELO"
                    + " AND M.FK_ID_MARCA=MR.ID_MARCA"
                    + " AND R.FK_ID_PERSONAL_PLANTEL = ?"
                    + " AND R.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PersonalPlantel);
            pstmt.setString(2, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PersonalXPersonalPlantel(String ID_PersonalPlantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Personal"
                    + " , (P.aPaterno + ' ' + P.aMaterno + ' ' + P.nombre)"
                    + " FROM "
                    + "  PERSONAL_PLANTEL  PP"
                    + " , PERSONAL  P"
                    + " WHERE "
                    + " PP.FK_ID_Personal = P.ID_Personal"
                    + " AND PP.ID_Personal_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PersonalPlantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {

            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, " Verifique log: " + " " + ex);
        }
        return listToSend;
    }

    public final LinkedList select_Resguardo4Bien(String ID_Bien, String status) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " PL.NOMBRE"
                    + " , P.ID_Personal"
                    + " ,  (P.nombre  + ' ' +  P.aPaterno  + ' ' +  P.aMaterno)"
                    + " , PP.ID_Personal_Plantel"
                    + " , PP.cargo"
                    + " , R.ID_Resguardo"
                    + " , R.observaciones"
                    + " , R.fechaAsignacion"
                    + " , R.fechaCambioSituacion"
                    + " , R.status"
                    + " , R.noTarjetaResguardo"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , PL.ID_Plantel"
                    + " FROM"
                    + " PERSONAL P"
                    + " , PLANTEL PL"
                    + " , PERSONAL_PLANTEL PP"
                    + " , RESGUARDO R"
                    + " , BIEN B"
                    + " , MODELO M"
                    + " , MARCA MR"
                    + " WHERE "
                    + " PP.FK_ID_PERSONAL=P.ID_PERSONAL"
                    + " AND PP.FK_ID_PLANTEL=PL.ID_PLANTEL"
                    + " AND R.FK_ID_PERSONAL_PLANTEL=PP.ID_PERSONAL_PLANTEL"
                    + " AND R.FK_ID_BIEN=B.ID_BIEN"
                    + " AND B.FK_ID_MODELO=M.ID_MODELO"
                    + " AND M.FK_ID_MARCA=MR.ID_MARCA"
                    + " AND R.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?"
                    + " AND R.FK_ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, status);
            pstmt.setString(2, ID_Bien);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Resguardo4Personal(String ID_Personal, String status) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " PL.NOMBRE"
                    + " , P.ID_Personal"
                    + " ,  (P.nombre  + ' ' +  P.aPaterno  + ' ' +  P.aMaterno)"
                    + " , PP.ID_Personal_Plantel"
                    + " , PP.cargo"
                    + " , R.ID_Resguardo"
                    + " , R.observaciones"
                    + " , R.fechaAsignacion"
                    + " , R.fechaCambioSituacion"
                    + " , R.status"
                    + " , R.noTarjetaResguardo"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " , B.ID_Bien"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " , PL.ID_Plantel"
                    + " FROM"
                    + " PERSONAL P"
                    + " , PLANTEL PL"
                    + " , PERSONAL_PLANTEL PP"
                    + " , RESGUARDO R"
                    + " , BIEN B"
                    + " , MODELO M"
                    + " , MARCA MR"
                    + " WHERE "
                    + " PP.FK_ID_PERSONAL=P.ID_PERSONAL"
                    + " AND PP.FK_ID_PLANTEL=PL.ID_PLANTEL"
                    + " AND R.FK_ID_PERSONAL_PLANTEL=PP.ID_PERSONAL_PLANTEL"
                    + " AND R.FK_ID_BIEN=B.ID_BIEN"
                    + " AND B.FK_ID_MODELO=M.ID_MODELO"
                    + " AND M.FK_ID_MARCA=MR.ID_MARCA"
                    + " AND PP.FK_ID_PERSONAL = ?"
                    + " AND R.status COLLATE SQL_Latin1_General_CP1_CI_AI = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal);
            pstmt.setString(2, status);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ResumeBien(String ID_Plantel, String ID_Subcategoria, String status, String[] detalles, boolean estatusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " nombre AS PLANTEL"
                    + " , ID_Bien"
                    + " , nombreSubCategoria"
                    + " , marca"
                    + " , modelo"
                    + " , noSerie"
                    + " , noInventario"
                    + " , nombreDepartamento"
                    + " , compra"
                    + " , CASE WHEN fechaFin IS NULL THEN ''ELSE fechaFin END AS GARANTIA";

            for (int i = 0; i < detalles.length; i++) {
                SQLSentence += " , CASE WHEN [" + detalles[i] + "] IS NULL THEN ''ELSE([" + detalles[i] + "]) END AS [" + detalles[i] + "]";
            }
            SQLSentence += " FROM"
                    + " (SELECT"
                    + " P.nombre"
                    + " ,  B.ID_Bien"
                    + " , B.FK_ID_Departamento_Plantel"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " ,  B.noSerie"
                    + " , B.noInventario"
                    + " , DT.nombreDepartamento"
                    + " , TC.compra"
                    + " , G.fechaFin"
                    + " ,  SC.nombreSubCategoria"
                    + " ,  D.nombreDetalle"
                    + " ,  VR.valor"
                    + " FROM"
                    + " (BIEN B INNER JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo "
                    + " INNER JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " INNER JOIN SUBCATEGORIA SC ON B.FK_ID_SubCategoria=SC.ID_SubCategoria "
                    + " INNER JOIN CATEGORIA C ON SC.FK_ID_Categoria=C.ID_Categoria"
                    + " INNER JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL AS DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " INNER JOIN DEPARTAMENTO AS DT ON DP.FK_ID_Departamento=DT.ID_Departamento AND DP.FK_ID_Plantel = P.ID_Plantel"
                    + " INNER JOIN TIPO_COMPRA AS TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN GARANTIA AS G ON G.FK_ID_Bien=B.ID_Bien)"
                    + " LEFT JOIN RELACION_BDS BDS ON BDS.FK_ID_Bien=B.ID_Bien"
                    + " LEFT JOIN DETALLE_SUBCATEGORIA DS ON BDS.FK_ID_Detalle_SubCategoria=DS.ID_Detalle_SubCategoria"
                    + " LEFT JOIN VALOR VR ON BDS.FK_ID_Valor=VR.ID_Valor"
                    + " LEFT JOIN DETALLE D ON VR.FK_ID_Detalle=D.ID_Detalle"
                    + " WHERE "
                    + " SC.ID_SubCategoria= ? ";
            if (estatusEqual) {
                SQLSentence += " AND B.status = ?";
            } else {
                SQLSentence += " AND B.status <> ?";
            }

            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            SQLSentence += " GROUP BY "
                    + " P.nombre"
                    + " , MR.marca"
                    + " , M.modelo"
                    + " ,  B.noSerie"
                    + " , B.noInventario"
                    + " , DT.nombreDepartamento"
                    + " , TC.compra"
                    + " , G.fechaFin"
                    + " ,  SC.nombreSubCategoria"
                    + " ,  D.nombreDetalle"
                    + " ,  VR.valor"
                    + " , B.FK_ID_Departamento_Plantel"
                    + " , B.ID_Bien) AS SOURCE"
                    + " PIVOT (MAX([valor]) FOR nombreDetalle IN (";
            for (int i = 0; i < detalles.length; i++) {
                SQLSentence += "[" + detalles[i] + "]";
                if (i + 1 < detalles.length) {
                    SQLSentence += " , ";
                }
            }

            SQLSentence += ")) as PIVOTE"
                    + " GROUP BY "
                    + " nombre"
                    + " , nombreSubCategoria"
                    + " , marca"
                    + " , modelo"
                    + " , noSerie"
                    + " , noInventario"
                    + " , nombreDepartamento"
                    + " , compra"
                    + " , fechaFin"
                    + " , ID_Bien";
            for (int i = 0; i < detalles.length; i++) {
                SQLSentence += " ,[" + detalles[i] + "]";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Subcategoria);
            pstmt.setString(2, status);
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(3, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                for (int i = 0; i < detalles.length; i++) {
                    aux.add(rs.getString(i + 11));
                }
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Totales4Subcategoria(String ID_Plantel, String status, String[] subcategorias, boolean estatusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + "  SELECT"
                    + "  nombre";
            SQLSentence += " ,";
            for (int i = 0; i < subcategorias.length; i++) {
                SQLSentence += " SUM(CASE WHEN [" + subcategorias[i] + "] IS NULL THEN 0 ELSE ([" + subcategorias[i] + "]) END)";
                if (i + 1 < subcategorias.length) {
                    SQLSentence += " + ";
                }
            }
            SQLSentence += "  AS TOTAL";
            for (int i = 0; i < subcategorias.length; i++) {
                SQLSentence += " , CASE WHEN [" + subcategorias[i] + "] IS NULL THEN 0 ELSE ([" + subcategorias[i] + "]) END AS [" + subcategorias[i] + "]";
            }
            SQLSentence += "  FROM"
                    + "  (SELECT"
                    + "  P.nombre"
                    + " ,   SC.nombreSubCategoria"
                    + " , COUNT( SC.nombreSubCategoria  ) AS conteo "
                    + "  FROM BIEN B LEFT JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo  "
                    + "  LEFT JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca "
                    + "  LEFT JOIN SUBCATEGORIA SC ON B.FK_ID_SubCategoria=SC.ID_SubCategoria  "
                    + "  LEFT JOIN CATEGORIA C ON SC.FK_ID_Categoria=C.ID_Categoria"
                    + "  LEFT JOIN PLANTEL P ON P.ID_Plantel=B.FK_ID_Plantel"
                    + "  WHERE  ";
            if (estatusEqual) {
                SQLSentence += " B.status = ?";
            } else {
                SQLSentence += " B.status <> ?";
            }
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " GROUP BY"
                    + "  P.nombre"
                    + " , SC.nombreSubCategoria) AS SOURCE PIVOT (MAX(conteo) FOR nombreSubCategoria IN ( ";
            for (int i = 0; i < subcategorias.length; i++) {
                SQLSentence += "[" + subcategorias[i] + "]";
                if (i + 1 < subcategorias.length) {
                    SQLSentence += " , ";
                }
            }
            SQLSentence += ")) AS PIVOTE"
                    + " GROUP BY"
                    + " nombre";
            for (int i = 0; i < subcategorias.length; i++) {
                SQLSentence += " , [" + subcategorias[i] + "]";
            }

            SQLSentence += " ORDER BY nombre ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, status);
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(2, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                for (int i = 0; i < subcategorias.length; i++) {
                    aux.add(rs.getString(i + 3));
                }
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_Totales4Modelo(String ID_Plantel, String status, String ID_Modelo, String[] ID_SubCategoria, boolean estatusEqual) {
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT COUNT(B.ID_Bien)"
                    + " FROM "
                    + " BIEN B"
                    + " ,  PLANTEL P"
                    + " ,  MARCA M"
                    + " ,  MODELO MD"
                    + " ,  CATEGORIA C"
                    + " ,  SUBCATEGORIA SC"
                    + " WHERE "
                    + " B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND MD.ID_Modelo=?"
                    + " AND P.ID_Plantel=?";
            if (estatusEqual) {
                SQLSentence += " AND B.status = ?";
            } else {
                SQLSentence += " AND B.status <> ?";
            }
            SQLSentence += " AND (";
            for (int i = 0; i < ID_SubCategoria.length; i++) {
                SQLSentence += " SC.ID_Subcategoria = ?";
                if (i + 1 < ID_SubCategoria.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Modelo);
            pstmt.setString(2, ID_Plantel);
            pstmt.setString(3, status);
            for (int i = 0; i < ID_SubCategoria.length; i++) {
                pstmt.setString(i + 4, ID_SubCategoria[i]);
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_Tipo_Archivo() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT "
                    + " ID_Tipo_Archivo"
                    + " ,nombreTipo"
                    + " FROM TIPO_ARCHIVO";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Archivo4Bien(String ID_Bien) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " A.ID_Archivo"
                    + " , A.nombreArchivo"
                    + " , A.descripcion"
                    + " , A.ubicacionFisica"
                    + " , A.extension"
                    + " , A.fechaActualizacion"
                    + " , TA.nombreTipo"
                    + " , TA.ID_Tipo_Archivo"
                    + " , BA.ID_Bien_Archivo"
                    + " FROM"
                    + " BIEN B"
                    + " ,  ARCHIVO A"
                    + " ,  BIEN_ARCHIVO BA"
                    + " ,  TIPO_ARCHIVO TA"
                    + " WHERE"
                    + " BA.FK_ID_Archivo=A.ID_Archivo"
                    + " AND BA.FK_ID_Bien=B.ID_Bien"
                    + " AND A.FK_ID_Tipo_Archivo=TA.ID_Tipo_Archivo"
                    + " AND B.ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Bien);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Archivo4Software(String ID_Software) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " SELECT"
                    + " A.ID_Archivo"
                    + " , A.nombreArchivo"
                    + " , A.descripcion"
                    + " , A.ubicacionFisica"
                    + " , A.extension"
                    + " , A.fechaActualizacion"
                    + " , TA.nombreTipo"
                    + " , TA.ID_Tipo_Archivo"
                    + " , SA.ID_Software_Archivo"
                    + " FROM"
                    + " SOFTWARE S"
                    + " ,  ARCHIVO A"
                    + " ,  SOFTWARE_ARCHIVO SA"
                    + " ,  TIPO_ARCHIVO TA"
                    + " WHERE"
                    + " SA.FK_ID_Archivo=A.ID_Archivo"
                    + " AND SA.FK_ID_Software=S.ID_Software"
                    + " AND A.FK_ID_Tipo_Archivo=TA.ID_Tipo_Archivo"
                    + " AND S.ID_Software = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Software);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Archivo(String ID_Archivo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " A.nombreArchivo"
                    + " , A.descripcion"
                    + " , A.ubicacionFisica"
                    + " , A.extension"
                    + " , A.fechaActualizacion"
                    + " , A.FK_ID_Tipo_Archivo"
                    + " , CASE WHEN A.tamanio IS NULL THEN ' ' ELSE (A.tamanio/1048576) END"
                    + " , A.keywords"
                    + " , A.hashName"
                    + " , A.FK_ID_Plantel"
                    + " , TA.nombreTipo"
                    + " , A.tipoAcceso"
                    + " , A.FK_ID_Plantel"
                    + " FROM"
                    + " ARCHIVO AS A"
                    + " , TIPO_ARCHIVO AS TA"
                    + " WHERE "
                    + " A.FK_ID_Tipo_Archivo=TA.ID_Tipo_Archivo"
                    + " AND A.ID_Archivo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, ID_Archivo);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ResumenGeneral(String ID_Plantel, String[] ID_Subcategoria, String status, boolean estatusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.nombre"
                    + " ,   B.ID_Bien"
                    + " ,  B.FK_ID_Departamento_Plantel"
                    + " ,  MR.marca"
                    + " ,  M.modelo"
                    + " ,   B.noSerie"
                    + " ,  B.noInventario"
                    + " ,  DT.nombreDepartamento"
                    + " ,  TC.compra"
                    + " ,  CASE WHEN G.fechaFin IS NULL THEN '' ELSE  G.fechaFin END AS FECHAFIN"
                    + " ,   SC.nombreSubCategoria"
                    + " FROM"
                    + " BIEN B INNER JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo "
                    + " INNER JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " INNER JOIN SUBCATEGORIA SC ON B.FK_ID_SubCategoria=SC.ID_SubCategoria "
                    + " INNER JOIN CATEGORIA C ON SC.FK_ID_Categoria=C.ID_Categoria"
                    + " INNER JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL AS DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " INNER JOIN DEPARTAMENTO AS DT ON DP.FK_ID_Departamento=DT.ID_Departamento AND DP.FK_ID_Plantel = P.ID_Plantel"
                    + " INNER JOIN TIPO_COMPRA AS TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN GARANTIA AS G ON G.FK_ID_Bien=B.ID_Bien"
                    + " WHERE ";
            if (estatusEqual) {
                SQLSentence += " B.status = ?";
            } else {
                SQLSentence += " B.status <> ?";
            }
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += "  AND (";
            for (int i = 0; i < ID_Subcategoria.length; i++) {
                SQLSentence += " SC.ID_SubCategoria = ?";
                if (i + 1 < ID_Subcategoria.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) ORDER BY SC.nombreSubCategoria ASC, MR.marca ASC, M.modelo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            int index = 2;
            pstmt.setString(1, status);
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(2, ID_Plantel);
                index += 1;
            }
            for (int i = 0; i < ID_Subcategoria.length; i++) {
                pstmt.setString(index, ID_Subcategoria[i]);
                index += 1;
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_BienesSinNoInventario(String ID_Plantel, String[] ID_Subcategoria, String status, boolean estatusEqual) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.nombre"
                    + " ,   B.ID_Bien"
                    + " ,  B.FK_ID_Departamento_Plantel"
                    + " ,  MR.marca"
                    + " ,  M.modelo"
                    + " ,   B.noSerie"
                    + " ,  B.noInventario"
                    + " ,  DT.nombreDepartamento"
                    + " ,  TC.compra"
                    + " ,  CASE WHEN G.fechaFin IS NULL THEN '' ELSE  G.fechaFin END AS FECHAFIN"
                    + " ,   SC.nombreSubCategoria"
                    + " FROM"
                    + " BIEN B INNER JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo "
                    + " INNER JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca"
                    + " INNER JOIN SUBCATEGORIA SC ON B.FK_ID_SubCategoria=SC.ID_SubCategoria "
                    + " INNER JOIN CATEGORIA C ON SC.FK_ID_Categoria=C.ID_Categoria"
                    + " INNER JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel"
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL AS DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " INNER JOIN DEPARTAMENTO AS DT ON DP.FK_ID_Departamento=DT.ID_Departamento AND DP.FK_ID_Plantel = P.ID_Plantel"
                    + " INNER JOIN TIPO_COMPRA AS TC ON B.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " LEFT JOIN GARANTIA AS G ON G.FK_ID_Bien=B.ID_Bien"
                    + " WHERE "
                    + " (B.noInventario='' OR B.noInventario='00000')";
            if (estatusEqual) {
                SQLSentence += " AND B.status = ?";
            } else {
                SQLSentence += " AND B.status <> ?";
            }
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += "  AND (";
            for (int i = 0; i < ID_Subcategoria.length; i++) {
                SQLSentence += " SC.ID_SubCategoria = ?";
                if (i + 1 < ID_Subcategoria.length) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) ORDER BY SC.nombreSubCategoria ASC, MR.marca ASC, M.modelo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);

            pstmt.setQueryTimeout(statementTimeOut);
            int index = 2;
            pstmt.setString(1, status);
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(2, ID_Plantel);
                index += 1;
            }
            for (int i = 0; i < ID_Subcategoria.length; i++) {
                pstmt.setString(index, ID_Subcategoria[i]);
                index += 1;
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Consumible(String ID_Plantel, boolean seeAll, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " C.ID_Consumible"
                    + " ,C.clave"
                    + " ,C.descripcion"
                    + " ,C.estatus"
                    + " ,C.noReferencia"
                    + " ,C.fechaActualizacion"
                    + " ,C.fechaAlta"
                    + " ,C.observaciones"
                    + " ,C.precioActual"
                    + " ,C.total"
                    + " ,SC.ID_SubCategoria"
                    + " ,SC.nombreSubCategoria"
                    + " ,M.ID_Medida"
                    + " ,M.medida"
                    + " ,P.ID_Plantel"
                    + " ,P.nombre"
                    + " ,CT.ID_Categoria"
                    + " ,CT.nombreCategoria"
                    + " FROM"
                    + " CONSUMIBLE C"
                    + " , PLANTEL P"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA CT"
                    + " , MEDIDA M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND C.estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND C.estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " ORDER BY P.nombre ASC, CT.nombreCategoria ASC, SC.nombreSubCategoria ASC,C.descripcion ASC ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (checkStatus) {
                pstmt.setString(1, estatus);
            }
            if (!checkStatus && !seeAll) {
                pstmt.setString(1, ID_Plantel);
            } else if (checkStatus && !seeAll) {
                pstmt.setString(2, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Consumible(String ID_Consumible) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " C.ID_Consumible"
                    + " ,C.clave"
                    + " ,C.descripcion"
                    + " ,C.estatus"
                    + " ,C.noReferencia"
                    + " ,C.fechaActualizacion"
                    + " ,C.fechaAlta"
                    + " ,C.observaciones"
                    + " ,C.precioActual"
                    + " ,C.total"
                    + " ,SC.ID_SubCategoria"
                    + " ,SC.nombreSubCategoria"
                    + " ,M.ID_Medida"
                    + " ,M.medida"
                    + " ,P.ID_Plantel"
                    + " ,P.nombre"
                    + " ,CT.ID_Categoria"
                    + " ,CT.nombreCategoria"
                    + " FROM"
                    + " CONSUMIBLE C"
                    + " , PLANTEL P"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA CT"
                    + " , MEDIDA M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND C.ID_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Consumible);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Consumible4Clave(String clave) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " C.ID_Consumible"
                    + " ,C.clave"
                    + " ,C.descripcion"
                    + " ,C.estatus"
                    + " ,C.noReferencia"
                    + " ,C.fechaActualizacion"
                    + " ,C.fechaAlta"
                    + " ,C.observaciones"
                    + " ,C.precioActual"
                    + " ,C.total"
                    + " ,SC.ID_SubCategoria"
                    + " ,SC.nombreSubCategoria"
                    + " ,M.ID_Medida"
                    + " ,M.medida"
                    + " ,P.ID_Plantel"
                    + " ,P.nombre"
                    + " ,CT.ID_Categoria"
                    + " ,CT.nombreCategoria"
                    + " FROM"
                    + " CONSUMIBLE C"
                    + " , PLANTEL P"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA CT"
                    + " , MEDIDA M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND C.clave = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, clave);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_TipoMovimiento(String movimiento) {
        int id = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT TOP 1"
                    + " ID_Tipo_Movimiento"
                    + " FROM"
                    + " TIPO_MOVIMIENTO"
                    + " WHERE "
                    + " tipoMovimiento COLLATE SQL_Latin1_General_CP1_CI_AI = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, movimiento);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);

            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public final LinkedList select_Movimiento4ID(String ID_Movimiento, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " M.ID_Movimiento"
                    + " , M.FK_ID_Plantel"
                    + " , M.folio"
                    + " , M.fechaMovimiento"
                    + " , M.fechaActualizacion"
                    + " , M.noFactura"
                    + " , M.noReferencia"
                    + " , M.estatus"
                    + " , M.observaciones"
                    + " , M.iva"
                    + " , M.FK_ID_Tipo_Movimiento"
                    + " , FK_ID_Usuario"
                    + " , M.motivoMovimiento"
                    + " , P.ID_Proveedor"
                    + " , P.nombreProveedor"
                    + " , TC.ID_Tipo_Compra"
                    + " , TC.compra"
                    + " , MP.ID_Movimiento_Proveedor"
                    + " , CASE WHEN M.fechaFactura IS NULL THEN '0000-00-00' ELSE M.fechaFactura END"
                    + " FROM MOVIMIENTO M"
                    + " , PROVEEDOR P"
                    + " , MOVIMIENTO_PROVEEDOR MP"
                    + " , TIPO_COMPRA TC"
                    + " WHERE"
                    + " MP.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MP.FK_ID_Proveedor=P.ID_Proveedor"
                    + " AND MP.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
                SQLSentence += " AND ID_Movimiento = ?";
            } else {
                SQLSentence += " AND ID_Movimiento = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (checkStatus) {
                pstmt.setString(1, estatus);
                pstmt.setString(2, ID_Movimiento);
            } else {
                pstmt.setString(1, ID_Movimiento);
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
                listToSend.add(rs.getString(19));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_MovimientoSalida4ID(String ID_Movimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Movimiento"
                    + " , FK_ID_Plantel"
                    + " , folio"
                    + " , fechaMovimiento"
                    + " , fechaActualizacion"
                    + " , noFactura"
                    + " , noReferencia"
                    + " , estatus"
                    + " , observaciones"
                    + " , iva"
                    + " , FK_ID_Tipo_Movimiento"
                    + " , FK_ID_Usuario"
                    + " , motivoMovimiento"
                    + " , noTurno"
                    + " FROM MOVIMIENTO M"
                    + " WHERE"
                    + " ID_Movimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Movimiento);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Medida() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " ID_Medida"
                    + " , medida"
                    + " FROM"
                    + " MEDIDA"
                    + " ORDER BY medida ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Movimiento(String ID_Plantel, String ID_TipoMovimiento, boolean seeAll, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " M.ID_Movimiento"
                    + " ,M.FK_ID_Plantel"
                    + " ,M.folio"
                    + " ,M.fechaMovimiento"
                    + " ,M.fechaActualizacion"
                    + " ,M.noFactura"
                    + " ,M.estatus"
                    + " ,M.observaciones"
                    + " ,M.iva"
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario"
                    + " ,M.motivoMovimiento"
                    + " ,P.nombre"
                    + " ,TM.tipoMovimiento"
                    + " ,PV.ID_Proveedor"
                    + " ,PV.nombreProveedor"
                    + " ,TC.ID_Tipo_Compra"
                    + " ,TC.compra"
                    + " ,CASE WHEN M.fechaFactura IS NULL THEN '0000-00-00' ELSE M.fechaFactura END"
                    + " FROM"
                    + " MOVIMIENTO M"
                    + " , TIPO_MOVIMIENTO TM"
                    + " , MOVIMIENTO_PROVEEDOR MP"
                    + " , TIPO_COMPRA TC"
                    + " , PLANTEL P"
                    + " , PROVEEDOR PV"
                    + " WHERE"
                    + " M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento"
                    + " AND MP.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MP.FK_ID_Proveedor=PV.ID_Proveedor"
                    + " AND MP.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra"
                    + " AND TM.ID_Tipo_Movimiento = ?";
            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoMovimiento);
            if (checkStatus) {
                pstmt.setString(2, estatus);
                if (!seeAll) {
                    pstmt.setString(3, ID_Plantel);
                }
            } else if (!seeAll) {
                pstmt.setString(2, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_MovimientoConsumible(String ID_Movimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " C.ID_Consumible"
                    + " ,C.FK_ID_Plantel"
                    + " ,C.FK_ID_Medida"
                    + " ,C.FK_ID_Subcategoria"
                    + " ,C.clave"
                    + " ,C.descripcion"
                    + " ,C.estatus"
                    + " ,C.fechaActualizacion"
                    + " ,C.fechaAlta"
                    + " ,C.noReferencia"
                    + " ,C.total"
                    + " ,C.precioActual"
                    + " ,C.observaciones"
                    + " ,M.ID_Movimiento"
                    + " ,M.folio"
                    + " ,M.fechaMovimiento"
                    + " ,M.noFactura"
                    + " ,M.noReferencia"
                    + " ,M.fechaActualizacion"
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario"
                    + " ,M.FK_ID_Plantel"
                    + " ,MD.medida"
                    + " ,MC.cantidad"
                    + " ,MC.precioUnitario"
                    + " ,MC.ID_Movimiento_Consumible"
                    + " FROM MOVIMIENTO M"
                    + " , CONSUMIBLE C"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " , MEDIDA AS MD"
                    + " WHERE"
                    + " MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND C.FK_ID_Medida=MD.ID_Medida"
                    + " AND M.ID_Movimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Movimiento);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                aux.add(rs.getString(26));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_Consumible4Campo(String campo, String ID_Consumible) {
        String resultado = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " " + campo
                    + " FROM "
                    + " CONSUMIBLE"
                    + " WHERE ID_Consumible = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Consumible);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            resultado = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public final String select_Movimiento4Campo(String campo, String ID_Movimiento) {
        String resultado = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " " + campo
                    + " FROM "
                    + " MOVIMIENTO"
                    + " WHERE ID_Movimiento = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Movimiento);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            resultado = "";
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }

    public final LinkedList select_Movimiento_Consumible(String ID_MovimientoConsumible) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " FK_ID_Movimiento"
                    + " ,FK_ID_Consumible"
                    + " ,cantidad"
                    + " ,precioUnitario"
                    + " FROM MOVIMIENTO_CONSUMIBLE"
                    + " WHERE"
                    + " ID_Movimiento_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_MovimientoConsumible);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountMovimiento(String ID_TipoMovimiento) {
        int totalMovimientos = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(ID_Movimiento)"
                    + " FROM MOVIMIENTO"
                    + " WHERE FK_ID_Tipo_Movimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoMovimiento);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalMovimientos = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            totalMovimientos = 0;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalMovimientos;
    }

    public final LinkedList select_MovimientoSalida(String ID_Plantel, String ID_TipoMovimiento, boolean seeAll, String estatus, boolean igual, boolean checkStatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " MOV.ID_Movimiento"
                    + " ,MOV.FK_ID_Plantel "
                    + " ,CAST(MOV.folio AS INT) "
                    + " ,MOV.fechaMovimiento "
                    + " ,MOV.fechaActualizacion "
                    + " ,MOV.noFactura"
                    + " ,MOV.estatus "
                    + " ,MOV.observaciones "
                    + " ,MOV.iva"
                    + " ,MOV.FK_ID_Tipo_Movimiento"
                    + " ,MOV.FK_ID_Usuario "
                    + " ,MOV.motivoMovimiento "
                    + " ,MOV.nombre"
                    + " ,MOV.tipoMovimiento                     "
                    + " ,MOV.FK_ID_Departamento_Plantel"
                    + " ,MOV.FK_ID_Personal_Plantel"
                    + " ,MOV.FK_ID_Departamento"
                    + " ,MOV.FK_ID_Personal"
                    + " ,D.nombreDepartamento"
                    + " ,PER.aPaterno"
                    + " ,PER.aMaterno"
                    + " ,PER.nombre"
                    + " ,PTL.nombre"
                    + " ,PTL.ID_Plantel"
                    + " , MOV.noTurno"
                    + " FROM"
                    + " (SELECT "
                    + " M.ID_Movimiento"
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura"
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva"
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " ,CASE WHEN M.noTurno IS NULL THEN -1 ELSE M.noTurno END AS noTurno "
                    + " ,P.nombre"
                    + " ,TM.tipoMovimiento                     "
                    + " ,T.FK_ID_Departamento_Plantel"
                    + " ,T.FK_ID_Personal_Plantel"
                    + " ,DP.FK_ID_Departamento"
                    + " ,PP.FK_ID_Personal"
                    + " ,DP.FK_ID_Plantel AS destino"
                    + " FROM "
                    + " MOVIMIENTO M "
                    + " , TIPO_MOVIMIENTO TM "
                    + " , PLANTEL P "
                    + " , TRASLADO T  "
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " , PERSONAL_PLANTEL PP "
                    + " WHERE T.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND T.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND T.FK_ID_Personal_Plantel=PP.ID_Personal_Plantel"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento"
                    + " AND TM.ID_Tipo_Movimiento = ?";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            SQLSentence += " ) AS MOV"
                    + " ,DEPARTAMENTO AS D"
                    + " ,PERSONAL PER"
                    + " ,PLANTEL PTL"
                    + " WHERE"
                    + " MOV.FK_ID_Departamento=D.ID_Departamento"
                    + " AND MOV.FK_ID_Personal=PER.ID_Personal"
                    + " AND MOV.destino=PTL.ID_Plantel"
                    + " ORDER BY CAST(MOV.folio AS INT) DESC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoMovimiento);
            if (checkStatus) {
                pstmt.setString(2, estatus);
                if (!seeAll) {
                    pstmt.setString(3, ID_Plantel);
                }
            } else if (!seeAll) {
                pstmt.setString(2, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_MovimientoSalida4Fecha(String ID_Plantel, String ID_TipoMovimiento, boolean seeAll, String estatus, boolean igual, boolean checkStatus, String fechaInicio, String fechaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " MOV.ID_Movimiento"
                    + " ,MOV.FK_ID_Plantel "
                    + " ,CAST(MOV.folio AS INT) "
                    + " ,MOV.fechaMovimiento "
                    + " ,MOV.fechaActualizacion "
                    + " ,MOV.noFactura"
                    + " ,MOV.estatus "
                    + " ,MOV.observaciones "
                    + " ,MOV.iva"
                    + " ,MOV.FK_ID_Tipo_Movimiento"
                    + " ,MOV.FK_ID_Usuario "
                    + " ,MOV.motivoMovimiento "
                    + " ,MOV.nombre"
                    + " ,MOV.tipoMovimiento                     "
                    + " ,MOV.FK_ID_Departamento_Plantel"
                    + " ,MOV.FK_ID_Personal_Plantel"
                    + " ,MOV.FK_ID_Departamento"
                    + " ,MOV.FK_ID_Personal"
                    + " ,D.nombreDepartamento"
                    + " ,PER.aPaterno"
                    + " ,PER.aMaterno"
                    + " ,PER.nombre"
                    + " ,PTL.nombre"
                    + " ,PTL.ID_Plantel"
                    + " , MOV.noTurno"
                    + " FROM"
                    + " (SELECT "
                    + " M.ID_Movimiento"
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura"
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva"
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " ,CASE WHEN M.noTurno IS NULL THEN -1 ELSE M.noTurno END AS noTurno "
                    + " ,P.nombre"
                    + " ,TM.tipoMovimiento                     "
                    + " ,T.FK_ID_Departamento_Plantel"
                    + " ,T.FK_ID_Personal_Plantel"
                    + " ,DP.FK_ID_Departamento"
                    + " ,PP.FK_ID_Personal"
                    + " ,DP.FK_ID_Plantel AS destino"
                    + " FROM "
                    + " MOVIMIENTO M "
                    + " ,TIPO_MOVIMIENTO TM "
                    + " ,PLANTEL P "
                    + " ,TRASLADO T  "
                    + " ,DEPARTAMENTO_PLANTEL DP"
                    + " ,PERSONAL_PLANTEL PP "
                    + " WHERE T.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND T.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND T.FK_ID_Personal_Plantel=PP.ID_Personal_Plantel"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento"
                    + " AND TM.ID_Tipo_Movimiento = ?"
                    + " AND M.fechaMovimiento >= ? "
                    + " AND M.fechaMovimiento <= ?";

            if (checkStatus) {
                if (igual) {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI = ?";
                } else {
                    SQLSentence += " AND M.estatus COLLATE SQL_Latin1_General_CP1_CI_AI <> ?";
                }
            }
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            SQLSentence += " ) AS MOV"
                    + " ,DEPARTAMENTO AS D"
                    + " ,PERSONAL PER"
                    + " ,PLANTEL PTL"
                    + " WHERE"
                    + " MOV.FK_ID_Departamento=D.ID_Departamento"
                    + " AND MOV.FK_ID_Personal=PER.ID_Personal"
                    + " AND MOV.destino=PTL.ID_Plantel"
                    + " ORDER BY CAST(MOV.folio AS INT) DESC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoMovimiento);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechaFin);
            if (checkStatus) {
                pstmt.setString(4, estatus);
                if (!seeAll) {
                    pstmt.setString(5, ID_Plantel);
                }
            } else if (!seeAll) {
                pstmt.setString(4, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_MovimientoSalida(String ID_Movimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " MOV.ID_Movimiento"
                    + " ,MOV.FK_ID_Plantel "
                    + " ,MOV.folio "
                    + " ,MOV.fechaMovimiento "
                    + " ,MOV.fechaActualizacion "
                    + " ,MOV.noFactura"
                    + " ,MOV.estatus "
                    + " ,MOV.observaciones "
                    + " ,MOV.iva"
                    + " ,MOV.FK_ID_Tipo_Movimiento"
                    + " ,MOV.FK_ID_Usuario "
                    + " ,MOV.motivoMovimiento "
                    + " ,MOV.nombre"
                    + " ,MOV.tipoMovimiento                     "
                    + " ,MOV.FK_ID_Departamento_Plantel"
                    + " ,MOV.FK_ID_Personal_Plantel"
                    + " ,MOV.FK_ID_Departamento"
                    + " ,MOV.FK_ID_Personal"
                    + " ,D.nombreDepartamento"
                    + " ,PER.aPaterno"
                    + " ,PER.aMaterno"
                    + " ,PER.nombre"
                    + " ,PTL.nombre"
                    + " ,PTL.ID_Plantel"
                    + " ,MOV.ID_Traslado"
                    + " ,MOV.noTurno"
                    + " FROM"
                    + " (SELECT "
                    + " M.ID_Movimiento"
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura"
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva"
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " , CASE WHEN M.noTurno IS NULL THEN -1 ELSE M.noTurno END AS noTurno"
                    + " ,P.nombre"
                    + " ,TM.tipoMovimiento                     "
                    + " ,T.FK_ID_Departamento_Plantel"
                    + " ,T.FK_ID_Personal_Plantel"
                    + " ,T.ID_Traslado"
                    + " ,DP.FK_ID_Departamento"
                    + " ,PP.FK_ID_Personal"
                    + " ,DP.FK_ID_Plantel AS destino"
                    + " FROM "
                    + " MOVIMIENTO M "
                    + " , TIPO_MOVIMIENTO TM "
                    + " , PLANTEL P "
                    + " , TRASLADO T  "
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " , PERSONAL_PLANTEL PP "
                    + " WHERE T.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND T.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND T.FK_ID_Personal_Plantel=PP.ID_Personal_Plantel"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento"
                    + " AND M.ID_Movimiento = ?"
                    + " ) AS MOV"
                    + " ,DEPARTAMENTO AS D"
                    + " ,PERSONAL PER"
                    + " ,PLANTEL PTL"
                    + " WHERE"
                    + " MOV.FK_ID_Departamento=D.ID_Departamento"
                    + " AND MOV.FK_ID_Personal=PER.ID_Personal"
                    + " AND MOV.destino=PTL.ID_Plantel"
                    + " ORDER BY MOV.folio ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Movimiento);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
                listToSend.add(rs.getString(19));
                listToSend.add(rs.getString(20));
                listToSend.add(rs.getString(21));
                listToSend.add(rs.getString(22));
                listToSend.add(rs.getString(23));
                listToSend.add(rs.getString(24));
                listToSend.add(rs.getString(25));
                listToSend.add(rs.getString(26));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Medida(String medida) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Medida"
                    + ", medida"
                    + " FROM MEDIDA"
                    + " WHERE medida COLLATE SQL_Latin1_General_CP1_CI_AI = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, medida);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PDG(String ID_PDG) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " FK_ID_Grupo"
                    + ", FK_ID_Departamento_Plantel"
                    + " FROM RELACION_PDG"
                    + " WHERE ID_PDG = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PDG);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ConsumoCantidad4Plantel(
            String ID_PlantelDestino,
            String ID_Plantel,
            String fechaInicio,
            String fechaFin,
            String ID_Tipo_movimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " C.ID_Consumible"
                    + ", C.clave"
                    + ", C.descripcion"
                    + ", C.precioActual"
                    + ", C.noReferencia"
                    + ", MC.ID_Movimiento_Consumible"
                    + ", MC.cantidad"
                    + ", MC.precioUnitario"
                    + " ,MVT.ID_Movimiento "
                    + " ,MVT.FK_ID_Plantel  "
                    + " ,MVT.folio  "
                    + " ,MVT.fechaMovimiento "
                    + " ,MVT.fechaActualizacion "
                    + " ,MVT.noFactura "
                    + " ,MVT.estatus  "
                    + " ,MVT.observaciones "
                    + " ,MVT.iva "
                    + " ,MVT.FK_ID_Tipo_Movimiento "
                    + " ,MVT.FK_ID_Usuario"
                    + " ,MVT.motivoMovimiento  "
                    + " ,MVT.nombrePlantelDestino"
                    + " ,MVT.tipoMovimiento  "
                    + " ,MVT.FK_ID_Departamento_Plantel "
                    + " ,MVT.FK_ID_Personal_Plantel "
                    + " ,MVT.FK_ID_Departamento "
                    + " ,MVT.FK_ID_Personal "
                    + " ,MVT.nombreDepartamento"
                    + " ,MVT.aPaterno "
                    + " ,MVT.aMaterno "
                    + " ,MVT.nombrePersonalRecibe"
                    + " ,MVT.nombrePlantelOrigen"
                    + " ,MVT.ID_Plantel "
                    + " ,M.ID_Medida"
                    + " ,M.medida"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " ,MOVIMIENTO_CONSUMIBLE AS MC"
                    + " ,MEDIDA AS M"
                    + " ,(SELECT  "
                    + " MOV.ID_Movimiento "
                    + " ,MOV.FK_ID_Plantel  "
                    + " ,MOV.folio  "
                    + " ,MOV.fechaMovimiento "
                    + " ,MOV.fechaActualizacion "
                    + " ,MOV.noFactura "
                    + " ,MOV.estatus  "
                    + " ,MOV.observaciones "
                    + " ,MOV.iva "
                    + " ,MOV.FK_ID_Tipo_Movimiento "
                    + " ,MOV.FK_ID_Usuario"
                    + " ,MOV.motivoMovimiento  "
                    + " ,MOV.nombre AS nombrePlantelOrigen"
                    + " ,MOV.tipoMovimiento  "
                    + " ,MOV.FK_ID_Departamento_Plantel "
                    + " ,MOV.FK_ID_Personal_Plantel "
                    + " ,MOV.FK_ID_Departamento "
                    + " ,MOV.FK_ID_Personal "
                    + " ,D.nombreDepartamento"
                    + " ,PER.aPaterno "
                    + " ,PER.aMaterno "
                    + " ,PER.nombre AS nombrePersonalRecibe"
                    + " ,PTL.nombre AS nombrePlantelDestino"
                    + " ,PTL.ID_Plantel "
                    + " FROM "
                    + " (SELECT "
                    + " M.ID_Movimiento "
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio  "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion  "
                    + " ,M.noFactura "
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva "
                    + " ,M.FK_ID_Tipo_Movimiento "
                    + " ,M.FK_ID_Usuario  "
                    + " ,M.motivoMovimiento  "
                    + " ,P.nombre "
                    + " ,TM.tipoMovimiento "
                    + " ,T.FK_ID_Departamento_Plantel "
                    + " ,T.FK_ID_Personal_Plantel "
                    + " ,DP.FK_ID_Departamento "
                    + " ,PP.FK_ID_Personal "
                    + " ,DP.FK_ID_Plantel AS destino "
                    + " FROM  MOVIMIENTO M  "
                    + " , TIPO_MOVIMIENTO TM  "
                    + " , PLANTEL P  "
                    + " , TRASLADO T  "
                    + " , DEPARTAMENTO_PLANTEL DP "
                    + " , PERSONAL_PLANTEL PP "
                    + " WHERE "
                    + " T.FK_ID_Movimiento=M.ID_Movimiento "
                    + " AND T.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel "
                    + " AND T.FK_ID_Personal_Plantel=PP.ID_Personal_Plantel "
                    + " AND M.FK_ID_Plantel=P.ID_Plantel "
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento "
                    + " AND TM.ID_Tipo_Movimiento = ? "
                    + " AND P.ID_Plantel = ? ) AS MOV "//origen
                    + " ,DEPARTAMENTO AS D "
                    + " ,PERSONAL AS PER "
                    + " ,PLANTEL AS PTL"
                    + " WHERE "
                    + " MOV.FK_ID_Departamento=D.ID_Departamento "
                    + " AND MOV.FK_ID_Personal=PER.ID_Personal "
                    + " AND MOV.destino=PTL.ID_Plantel "
                    + " AND MOV.fechaMovimiento >= ?"
                    + " AND MOV.fechaMovimiento <= ?";
            if (!ID_PlantelDestino.equalsIgnoreCase("todos")) {
                SQLSentence += " AND PTL.ID_Plantel = ?";

            }
            SQLSentence += "  ) AS MVT"// destino
                    + " WHERE "
                    + " MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND MC.FK_ID_Movimiento=MVT.ID_Movimiento"
                    + " AND C.FK_ID_Medida=M.ID_Medida";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Tipo_movimiento);
            pstmt.setString(2, ID_Plantel);
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, fechaFin);
            if (!ID_PlantelDestino.equalsIgnoreCase("todos")) {
                pstmt.setString(5, ID_PlantelDestino);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                aux.add(rs.getString(26));
                aux.add(rs.getString(27));
                aux.add(rs.getString(28));
                aux.add(rs.getString(29));
                aux.add(rs.getString(30));
                aux.add(rs.getString(31));
                aux.add(rs.getString(32));
                aux.add(rs.getString(33));
                aux.add(rs.getString(34));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ConsumibleNuloMovimiento(String ID_Plantel, LinkedList subcategorias) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT  "
                    + " C.ID_Consumible "
                    + " ,C.clave "
                    + " ,C.descripcion "
                    + " ,C.estatus "
                    + " ,C.noReferencia "
                    + " ,C.fechaActualizacion "
                    + " ,C.fechaAlta "
                    + " ,C.observaciones "
                    + " ,C.precioActual "
                    + " ,C.total "
                    + " ,SC.ID_SubCategoria "
                    + " ,SC.nombreSubCategoria "
                    + " ,M.ID_Medida "
                    + " ,M.medida "
                    + " ,P.ID_Plantel "
                    + " ,P.nombre "
                    + " FROM "
                    + " CONSUMIBLE C "
                    + " , PLANTEL P "
                    + " , SUBCATEGORIA SC "
                    + " , CATEGORIA CT "
                    + " , MEDIDA M "
                    + " WHERE "
                    + " C.FK_ID_Plantel=P.ID_Plantel "
                    + " AND C.FK_ID_Medida=M.ID_Medida "
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria "
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria "
                    + " AND P.ID_Plantel = ?"
                    + " AND C.ID_Consumible  NOT IN"
                    + " ("
                    + " SELECT MC.FK_ID_Consumible"
                    + " FROM MOVIMIENTO_CONSUMIBLE AS MC"
                    + " )";
            if (!subcategorias.isEmpty()) {
                SQLSentence += " AND (";
                for (int i = 0; i < subcategorias.size(); i++) {
                    SQLSentence += " SC.ID_Subcategoria = ?";
                    if (i + 1 < subcategorias.size()) {
                        SQLSentence += " OR ";
                    }
                }
                SQLSentence += " )";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            if (!subcategorias.isEmpty()) {
                for (int i = 0; i < subcategorias.size(); i++) {
                    pstmt.setString(i + 2, subcategorias.get(i).toString());
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Modelo4Plantel(String ID_Plantel, LinkedList subcategorias) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " (SC.nombreSubCategoria+' '+M.marca+' '+MD.modelo) as modelo"
                    + " FROM"
                    + " BIEN B"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , PLANTEL P"
                    + " , DEPARTAMENTO D"
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA C"
                    + " WHERE"
                    + " MD.FK_ID_Marca = M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria";
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            if (!subcategorias.isEmpty()) {
                SQLSentence += " AND (";
                for (int i = 0; i < subcategorias.size(); i++) {
                    SQLSentence += " SC.ID_Subcategoria = ?";
                    if (i + 1 < subcategorias.size()) {
                        SQLSentence += " OR ";
                    }
                }
                SQLSentence += " )";
            }
            SQLSentence += " ORDER BY modelo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int paramIndex = 1;
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(1, ID_Plantel);
                paramIndex += 1;
            }

            if (!subcategorias.isEmpty()) {
                for (int i = 0; i < subcategorias.size(); i++) {
                    pstmt.setString(paramIndex, subcategorias.get(i).toString());
                    paramIndex += 1;
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ID_Modelo4Plantel(String ID_Plantel, LinkedList subcategorias) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " MD.ID_Modelo"
                    + " FROM"
                    + " BIEN B"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , PLANTEL P"
                    + " , DEPARTAMENTO D"
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA C"
                    + " WHERE"
                    + " MD.FK_ID_Marca = M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria";
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            if (!subcategorias.isEmpty()) {
                SQLSentence += " AND (";
                for (int i = 0; i < subcategorias.size(); i++) {
                    SQLSentence += " SC.ID_Subcategoria = ?";
                    if (i + 1 < subcategorias.size()) {
                        SQLSentence += " OR ";
                    }
                }
                SQLSentence += " )";
            }
            SQLSentence += " ORDER BY MD.ID_Modelo ASC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int paramIndex = 1;
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(1, ID_Plantel);
                paramIndex += 1;
            }

            if (!subcategorias.isEmpty()) {
                for (int i = 0; i < subcategorias.size(); i++) {
                    pstmt.setString(paramIndex, subcategorias.get(i).toString());
                    paramIndex += 1;
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Detalles4Modelo(LinkedList modelos, LinkedList detalles, String ID_Plantel, LinkedList subcategorias) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " nombreSubCategoria"
                    + " , marca "
                    + " , modelo ";

            for (int i = 0; i < detalles.size(); i++) {
                SQLSentence += " , CASE WHEN [" + detalles.get(i).toString() + "] IS NULL THEN ''ELSE([" + detalles.get(i).toString() + "]) END AS [" + detalles.get(i).toString() + "]";
            }

            SQLSentence += " FROM ("
                    + " SELECT"
                    + "  SC.nombreSubCategoria"
                    + " , MR.marca "
                    + " , M.modelo "
                    + " , D.nombreDetalle "
                    + " , VR.valor"
                    + " FROM "
                    + " BIEN B INNER JOIN MODELO M ON B.FK_ID_Modelo=M.ID_Modelo  "
                    + " INNER JOIN MARCA MR ON M.FK_ID_Marca=MR.ID_Marca "
                    + " INNER JOIN SUBCATEGORIA SC ON B.FK_ID_SubCategoria=SC.ID_SubCategoria  "
                    + " INNER JOIN CATEGORIA C ON SC.FK_ID_Categoria=C.ID_Categoria "
                    + " INNER JOIN PLANTEL P ON B.FK_ID_Plantel=P.ID_Plantel "
                    + " LEFT JOIN DEPARTAMENTO_PLANTEL AS DP ON B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel "
                    + " INNER JOIN DEPARTAMENTO AS DT ON DP.FK_ID_Departamento=DT.ID_Departamento AND DP.FK_ID_Plantel = P.ID_Plantel "
                    + " LEFT JOIN RELACION_BDS BDS ON BDS.FK_ID_Bien=B.ID_Bien "
                    + " LEFT JOIN DETALLE_SUBCATEGORIA DS ON BDS.FK_ID_Detalle_SubCategoria=DS.ID_Detalle_SubCategoria "
                    + " LEFT JOIN VALOR VR ON BDS.FK_ID_Valor=VR.ID_Valor "
                    + " LEFT JOIN DETALLE D ON VR.FK_ID_Detalle=D.ID_Detalle"
                    + " WHERE  P.ID_Plantel = ? "
                    + " AND (";

            for (int i = 0; i < modelos.size(); i++) {
                SQLSentence += " M.ID_Modelo = ?";
                if (i + 1 < modelos.size()) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) AND (";
            for (int i = 0; i < subcategorias.size(); i++) {
                SQLSentence += " B.FK_ID_Subcategoria = ? ";
                if (i + 1 < subcategorias.size()) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) GROUP BY  "
                    + " MR.marca "
                    + " , M.modelo "
                    + " , D.nombreDetalle "
                    + " , VR.valor "
                    + " , SC.nombreSubCategoria) AS SOURCE PIVOT (MAX([valor]) "
                    + " FOR nombreDetalle IN (";
            for (int i = 0; i < detalles.size(); i++) {
                SQLSentence += "[" + detalles.get(i).toString() + "]";
                if (i + 1 < detalles.size()) {
                    SQLSentence += " , ";
                }
            }
            SQLSentence += ")) AS PIVOTE "
                    + " GROUP BY  "
                    + " marca"
                    + " , modelo  ";
            for (int i = 0; i < detalles.size(); i++) {
                SQLSentence += " ,[" + detalles.get(i).toString() + "]";
            }
            SQLSentence += " , nombreSubCategoria";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int index = 2;
            pstmt.setString(1, ID_Plantel);
            for (int i = 0; i < modelos.size(); i++) {
                pstmt.setString(index, modelos.get(i).toString());
                index += 1;
            }
            for (int i = 0; i < subcategorias.size(); i++) {
                pstmt.setString(index, subcategorias.get(i).toString());
                index += 1;
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                for (int j = 0; j < detalles.size(); j++) {
                    aux.add(rs.getString(j + 4));
                }
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ConteoXDeparatmentos(String ID_Plantel, LinkedList modelos, LinkedList deptos, LinkedList subategoria) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " nombre"
                    + " ,nombreDepartamento";

            for (int i = 0; i < modelos.size(); i++) {
                SQLSentence += " , CASE WHEN [" + modelos.get(i).toString() + "] IS NULL THEN ''ELSE([" + modelos.get(i).toString() + "]) END AS [" + modelos.get(i).toString() + "]";
            }

            SQLSentence += " FROM"
                    + " (SELECT "
                    + " P.nombre"
                    + " , (SC.nombreSubCategoria+' '+M.marca+' '+MD.modelo) as modelo"
                    + " , D.nombreDepartamento"
                    + " , B.ID_Bien"
                    + " FROM"
                    + " BIEN B"
                    + " , MARCA M"
                    + " , MODELO MD"
                    + " , PLANTEL P"
                    + " , DEPARTAMENTO D"
                    + " , DEPARTAMENTO_PLANTEL DP"
                    + " , SUBCATEGORIA SC"
                    + " , CATEGORIA C"
                    + " WHERE"
                    + " MD.FK_ID_Marca = M.ID_Marca"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND B.status <> 'BAJA'";
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " AND ( ";
            for (int i = 0; i < deptos.size(); i++) {
                SQLSentence += " DP.ID_Departamento_Plantel=? ";
                if (i + 1 < deptos.size()) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += " ) AND (";
            for (int i = 0; i < subategoria.size(); i++) {
                SQLSentence += " SC.ID_SubCategoria=? ";
                if (i + 1 < subategoria.size()) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += ") GROUP BY"
                    + " P.nombre"
                    + " , M.marca,modelo"
                    + " , D.nombreDepartamento"
                    + " , SC.nombreSubCategoria"
                    + " , B.ID_Bien) AS SOURCE PIVOT (count(ID_Bien) FOR modelo IN(";
            for (int i = 0; i < modelos.size(); i++) {
                SQLSentence += "[" + modelos.get(i).toString() + "]";
                if (i + 1 < modelos.size()) {
                    SQLSentence += " , ";
                }
            }

            SQLSentence += ")) AS PIVOTE"
                    + " GROUP BY nombre, nombreDepartamento";

            for (int i = 0; i < modelos.size(); i++) {
                SQLSentence += " ,[" + modelos.get(i).toString() + "]";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            int paramIndex = 1;
            if (!ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(paramIndex, ID_Plantel);
                paramIndex += 1;
            }
            for (int i = 0; i < deptos.size(); i++) {
                pstmt.setString(paramIndex, deptos.get(i).toString());
                paramIndex += 1;
            }
            for (int i = 0; i < subategoria.size(); i++) {
                pstmt.setString(paramIndex, subategoria.get(i).toString());
                paramIndex += 1;
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                for (int i = 0; i < modelos.size(); i++) {
                    aux.add(rs.getString(i + 3));
                }
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Consolidacion_Facturas(String ID_Plantel, String fechaInicio, String fechaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " M.ID_Movimiento "
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura "
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva "
                    + " ,M.FK_ID_Tipo_Movimiento "
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " ,P.nombre "
                    + " ,TM.tipoMovimiento "
                    + " ,PV.ID_Proveedor "
                    + " ,PV.nombreProveedor "
                    + " ,TC.ID_Tipo_Compra "
                    + " ,TC.compra"
                    + " ,SUM(MC.precioUnitario*MC.cantidad)+M.iva  total"
                    + " FROM "
                    + " MOVIMIENTO M "
                    + " , TIPO_MOVIMIENTO TM "
                    + " , MOVIMIENTO_PROVEEDOR MP"
                    + " , TIPO_COMPRA TC "
                    + " , PLANTEL P "
                    + " , PROVEEDOR PV"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " WHERE "
                    + " M.FK_ID_Plantel=P.ID_Plantel "
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento "
                    + " AND MP.FK_ID_Movimiento=M.ID_Movimiento "
                    + " AND MP.FK_ID_Proveedor=PV.ID_Proveedor "
                    + " AND MP.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra "
                    + " AND MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND TM.ID_Tipo_Movimiento = 1"
                    + " AND P.ID_Plantel = ? "
                    + " AND M.fechaMovimiento >= ?"
                    + " AND M.fechaMovimiento <= ?"
                    + " GROUP BY"
                    + " M.ID_Movimiento "
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura "
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva "
                    + " ,M.FK_ID_Tipo_Movimiento "
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " ,P.nombre "
                    + " ,TM.tipoMovimiento "
                    + " ,PV.ID_Proveedor "
                    + " ,PV.nombreProveedor "
                    + " ,TC.ID_Tipo_Compra "
                    + " ,TC.compra";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechaFin);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_HistoriaEntrada4Conumible(String ID_Plantel, String ID_Consumible, String ID_TipoMovimiento, String fechaInicio, String fechaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " M.ID_Movimiento "
                    + " ,M.FK_ID_Plantel "
                    + " ,M.folio "
                    + " ,M.fechaMovimiento "
                    + " ,M.fechaActualizacion "
                    + " ,M.noFactura "
                    + " ,M.estatus "
                    + " ,M.observaciones "
                    + " ,M.iva "
                    + " ,M.FK_ID_Tipo_Movimiento "
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento "
                    + " ,P.nombre "
                    + " ,TM.tipoMovimiento "
                    + " ,PV.ID_Proveedor "
                    + " ,PV.nombreProveedor "
                    + " ,TC.ID_Tipo_Compra"
                    + " ,TC.compra "
                    + " , C.ID_Consumible"
                    + " , C.descripcion"
                    + " , MC.cantidad"
                    + " , MC.precioUnitario"
                    + " , C.total"
                    + " , C.precioActual"
                    + " FROM "
                    + " MOVIMIENTO M "
                    + " , TIPO_MOVIMIENTO TM "
                    + " , MOVIMIENTO_PROVEEDOR MP "
                    + " , TIPO_COMPRA TC "
                    + " , PLANTEL P "
                    + " , PROVEEDOR PV"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " , CONSUMIBLE C"
                    + " WHERE "
                    + " M.FK_ID_Plantel=P.ID_Plantel "
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento "
                    + " AND MP.FK_ID_Movimiento=M.ID_Movimiento "
                    + " AND MP.FK_ID_Proveedor=PV.ID_Proveedor "
                    + " AND MP.FK_ID_Tipo_Compra=TC.ID_Tipo_Compra "
                    + " AND MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND C.ID_Consumible = ?"
                    + " AND TM.ID_Tipo_Movimiento = ?"
                    + " AND M.fechaMovimiento >= ?"
                    + " AND M.fechaMovimiento <= ?"
                    + " AND P.ID_Plantel = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Consumible);
            pstmt.setString(2, ID_TipoMovimiento);
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, fechaFin);
            pstmt.setString(5, ID_Plantel);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_HistoriaSalida4Conumible(String ID_Plantel, String ID_Consumible, String ID_TipoMovimiento, String fechaInicio, String fechaFin) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " MOV.ID_Movimiento "
                    + " ,MOV.FK_ID_Plantel "
                    + " ,MOV.folio "
                    + " ,MOV.fechaMovimiento  "
                    + " ,MOV.fechaActualizacion  "
                    + " ,MOV.noFactura "
                    + " ,MOV.estatus  "
                    + " ,MOV.observaciones  "
                    + " ,MOV.iva "
                    + " ,MOV.FK_ID_Tipo_Movimiento "
                    + " ,MOV.FK_ID_Usuario "
                    + " ,MOV.motivoMovimiento  "
                    + " ,MOV.nombre"
                    + " ,MOV.tipoMovimiento                      "
                    + " ,MOV.FK_ID_Departamento_Plantel "
                    + " ,MOV.FK_ID_Personal_Plantel "
                    + " ,MOV.FK_ID_Departamento "
                    + " ,MOV.FK_ID_Personal "
                    + " ,D.nombreDepartamento "
                    + " ,PER.aPaterno "
                    + " ,PER.aMaterno "
                    + " ,PER.nombre "
                    + " ,PTL.nombre "
                    + " ,PTL.ID_Plantel"
                    + " ,C.ID_Consumible"
                    + " ,C.clave"
                    + " ,C.descripcion"
                    + " ,MC.cantidad"
                    + " ,MC.precioUnitario"
                    + " FROM ("
                    + " SELECT  "
                    + " M.ID_Movimiento "
                    + " ,M.FK_ID_Plantel  "
                    + " ,M.folio  "
                    + " ,M.fechaMovimiento  "
                    + " ,M.fechaActualizacion  "
                    + " ,M.noFactura"
                    + " ,M.estatus  "
                    + " ,M.observaciones "
                    + " ,M.iva "
                    + " ,M.FK_ID_Tipo_Movimiento"
                    + " ,M.FK_ID_Usuario "
                    + " ,M.motivoMovimiento  "
                    + " ,P.nombre "
                    + " ,TM.tipoMovimiento     "
                    + " ,T.FK_ID_Departamento_Plantel "
                    + " ,T.FK_ID_Personal_Plantel "
                    + " ,DP.FK_ID_Departamento "
                    + " ,PP.FK_ID_Personal "
                    + " ,DP.FK_ID_Plantel AS destino"
                    + " FROM  "
                    + " MOVIMIENTO M "
                    + " , TIPO_MOVIMIENTO TM "
                    + " , PLANTEL P  "
                    + " , TRASLADO T  "
                    + " , DEPARTAMENTO_PLANTEL DP "
                    + " , PERSONAL_PLANTEL PP"
                    + " WHERE "
                    + " T.FK_ID_Movimiento=M.ID_Movimiento "
                    + " AND T.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel "
                    + " AND T.FK_ID_Personal_Plantel=PP.ID_Personal_Plantel"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento "
                    + " AND TM.ID_Tipo_Movimiento = ? "
                    + " AND P.ID_Plantel = ? ) AS MOV "
                    + " ,DEPARTAMENTO AS D "
                    + " ,PERSONAL PER "
                    + " ,PLANTEL PTL"
                    + " ,MOVIMIENTO_CONSUMIBLE MC"
                    + " ,CONSUMIBLE C"
                    + " WHERE "
                    + " MOV.FK_ID_Departamento=D.ID_Departamento "
                    + " AND MOV.FK_ID_Personal=PER.ID_Personal "
                    + " AND MOV.destino=PTL.ID_Plantel "
                    + " AND MC.FK_ID_Movimiento=MOV.ID_Movimiento"
                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND C.ID_Consumible = ?"
                    + " AND MOV.fechaMovimiento >= ?"
                    + " AND MOV.fechaMovimiento <= ?"
                    + " ORDER BY MOV.folio DESC";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoMovimiento);
            pstmt.setString(2, ID_Plantel);
            pstmt.setString(3, ID_Consumible);
            pstmt.setString(4, fechaInicio);
            pstmt.setString(5, fechaFin);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                aux.add(rs.getString(23));
                aux.add(rs.getString(24));
                aux.add(rs.getString(25));
                aux.add(rs.getString(26));
                aux.add(rs.getString(27));
                aux.add(rs.getString(28));
                aux.add(rs.getString(29));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final Double select_CostoPromedio4Consumible(
            String ID_Plantel,
            String ID_Consumible,
            String fechaInicioAnioFiscal,
            String fechaFinAnioFiscal) {

        Double costoPromedio = 0.0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " SUM(MC.cantidad*MC.precioUnitario)/SUM(MC.cantidad) as costoPromedio"
                    + " FROM"
                    + " MOVIMIENTO M"
                    + " , CONSUMIBLE C"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " , PLANTEL P"
                    + " WHERE"
                    + " MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND P.ID_Plantel = ?"
                    + " AND C.ID_Consumible = ?"
                    + " AND M.fechaMovimiento >= ?"
                    + " AND M.fechaMovimiento<= ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_Consumible);
            pstmt.setString(3, fechaInicioAnioFiscal);
            pstmt.setString(4, fechaFinAnioFiscal);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                costoPromedio = rs.getDouble(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return costoPromedio;
    }

    public final LinkedList select_HistoriaCostoCantidad4Consumible(
            String ID_Plantel,
            String ID_Consumible,
            String fechaInicioAnioFiscal,
            String fechaFinAnioFiscal) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " MC.cantidad"
                    + " ,MC.precioUnitario"
                    + " FROM"
                    + " MOVIMIENTO M"
                    + " , CONSUMIBLE C"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " , PLANTEL P"
                    + " WHERE"
                    + " MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND P.ID_Plantel = ?"
                    + " AND C.ID_Consumible = ?"
                    + " AND M.fechaMovimiento >= ?"
                    + " AND M.fechaMovimiento<= ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_Consumible);
            pstmt.setString(3, fechaInicioAnioFiscal);
            pstmt.setString(4, fechaFinAnioFiscal);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

//    public final Double select_SumaActualCostos4Consumible(
//            String ID_Plantel,
//            String ID_Consumible,
//            String fechaInicioAnioFiscal,
//            String fechaFinAnioFiscal) {
//
//        Double sumaActual = 0.0;
//        JSpreadConnectionPool jscp = null;
//        Connection conn = null;
//        String SQLSentence = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            SQLSentence = ""
//                    + " SELECT "
//                    + " SUM(MC.cantidad*MC.precioUnitario) as costoPromedio"
//                    + " FROM"
//                    + " MOVIMIENTO M"
//                    + " , CONSUMIBLE C"
//                    + " , MOVIMIENTO_CONSUMIBLE MC"
//                    + " , PLANTEL P"
//                    + " WHERE"
//                    + " MC.FK_ID_Movimiento=M.ID_Movimiento"
//                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
//                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
//                    + " AND P.ID_Plantel = ?"
//                    + " AND C.ID_Consumible = ?"
//                    + " AND M.fechaMovimiento >= ?"
//                    + " AND M.fechaMovimiento<= ?";
//
//            jscp = JSpreadConnectionPool.getSingleInstance();
//            conn = jscp.getConnectionFromPool();
//            pstmt = conn.prepareStatement(SQLSentence);
//            pstmt.setQueryTimeout(statementTimeOut);
//            pstmt.setString(1, ID_Plantel);
//            pstmt.setString(2, ID_Consumible);
//            pstmt.setString(3, fechaInicioAnioFiscal);
//            pstmt.setString(4, fechaFinAnioFiscal);
//            rs = pstmt.executeQuery();
//            if (rs.next()) {
//                sumaActual = rs.getDouble(1);
//            }
//            endConnection(jscp, conn, pstmt, rs);
//        } catch (Exception ex) {
//            endConnection(jscp, conn, pstmt, rs);
//            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return sumaActual;
//    }
    public final LinkedList select_SumaActualCostos4Consumible(
            String ID_Plantel,
            String ID_Consumible,
            String fechaInicioAnioFiscal,
            String fechaFinAnioFiscal) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " SUM(MC.cantidad*MC.precioUnitario)"
                    + " , SUM(MC.cantidad)"
                    + " FROM"
                    + " MOVIMIENTO M"
                    + " , CONSUMIBLE C"
                    + " , MOVIMIENTO_CONSUMIBLE MC"
                    + " , PLANTEL P"
                    + " WHERE"
                    + " MC.FK_ID_Movimiento=M.ID_Movimiento"
                    + " AND MC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND P.ID_Plantel = ?"
                    + " AND C.ID_Consumible = ?"
                    + " AND M.fechaMovimiento >= ?"
                    + " AND M.fechaMovimiento<= ?"
                    + " AND M.FK_ID_Tipo_Movimiento=1";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_Consumible);
            pstmt.setString(3, fechaInicioAnioFiscal);
            pstmt.setString(4, fechaFinAnioFiscal);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getDouble(1));
                listToSend.add(rs.getDouble(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_DiferenciaConteoFisico(String ID_Plantel, String ID_ConteoFisico) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " CT.nombreCategoria"
                    + " , SUM(CFC.precioHistorico*CFC.conteoLogico) AS valorizado"
                    + " , SUM(CFC.precioHistorico*CFC.conteoFisico) AS fisico"
                    + " , SUM(CFC.precioHistorico*CFC.conteoFisico)-SUM(CFC.precioHistorico*CFC.conteoLogico) AS diferencia"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " , CONTEOFISICO_CONSUMIBLE AS CFC"
                    + " , CONTEOFISICO AS CF"
                    + " , PLANTEL AS P"
                    + " , SUBCATEGORIA AS SC"
                    + " , CATEGORIA AS CT"
                    + " , MEDIDA AS M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND CF.FK_ID_Plantel=P.ID_Plantel"
                    + " AND CFC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND CFC.FK_ID_ConteoFisico=CF.ID_ConteoFisico"
                    + " AND P.ID_Plantel = ?"
                    + " AND CF.ID_ConteoFisico = ?"
                    + " GROUP BY CT.nombreCategoria";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_DetalleConteoFisicoConsumible(String ID_ConteoFisico) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " CT.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , C.clave"
                    + " , C.descripcion"
                    + " , M.medida"
                    + " , CFC.precioHistorico"
                    + " , CFC.conteoLogico"
                    + " , CFC.conteoFisico"
                    + " , CFC.precioHistorico*CFC.conteoLogico AS valorizadoLogico"
                    + " , CFC.precioHistorico*CFC.conteoFisico AS valorizadoFisico"
                    + " , (CFC.precioHistorico*CFC.conteoFisico)-CFC.precioHistorico*CFC.conteoLogico AS diferencia"
                    + " , CFC.fechaTomaLectura"
                    + " , P.nombre"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " , CONTEOFISICO_CONSUMIBLE AS CFC"
                    + " , CONTEOFISICO AS CF"
                    + " , PLANTEL AS P"
                    + " , SUBCATEGORIA AS SC"
                    + " , CATEGORIA AS CT"
                    + " , MEDIDA AS M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND CF.FK_ID_Plantel=P.ID_Plantel"
                    + " AND CFC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND CFC.FK_ID_ConteoFisico=CF.ID_ConteoFisico"
                    + " AND CF.ID_ConteoFisico = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ConteoFisico(String ID_Plantel, Boolean seeAll) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " CF.ID_ConteoFisico"
                    + " , CF.estatus"
                    + " , CF.fechaModificacion"
                    + " , CF.fechaRegistro"
                    + " , CF.FK_ID_Plantel"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM CONTEOFISICO AS CF"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " CF.FK_ID_Plantel=P.ID_Plantel";
            if (!seeAll) {
                SQLSentence += " AND CF.FK_ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountConteoFisico4Estatus(String ID_Plantel, String estatus) {

        int conteo = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(*)"
                    + " FROM CONTEOFISICO AS CF"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " CF.FK_ID_Plantel=P.ID_Plantel"
                    + " AND CF.FK_ID_Plantel = ?"
                    + " AND CF.estatus = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, estatus);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                conteo = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conteo;
    }

    public final LinkedList select_ConteoFisico(String ID_ConteoFisico) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + "  CF.estatus"
                    + " , CF.fechaModificacion"
                    + " , CF.fechaRegistro"
                    + " , CF.FK_ID_Plantel"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM CONTEOFISICO AS CF"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " CF.FK_ID_Plantel=P.ID_Plantel"
                    + " AND CF.ID_ConteoFisico = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Conumible4ConteoFisico(String ID_Plantel, String ID_Subcategoria, String ID_ConteoFisico) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " nombre"
                    + " , nombreCategoria"
                    + " , nombreSubCategoria"
                    + " , ID_Consumible"
                    + " , clave"
                    + " , descripcion"
                    + " , medida"
                    + " , estatusConsumible"
                    + " , precioActual"
                    + " , total"
                    + " , valorizado"
                    + " , CFC.conteoFisico"
                    + " , CFC.conteoLogico"
                    + " , CASE WHEN CFC.conteoFisico IS NULL THEN 0-total ELSE CFC.conteoFisico-total END AS diferencia"
                    + " , CFC.FK_ID_ConteoFisico"
                    + " , CFC.ID_ConteoFisico_Consumible"
                    + " FROM"
                    + " (SELECT "
                    + " P.nombre"
                    + " , CT.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , C.ID_Consumible"
                    + " , C.clave"
                    + " , C.descripcion"
                    + " , M.medida"
                    + " , C.estatus as estatusConsumible"
                    + " , C.precioActual"
                    + " , C.total"
                    + " , CAST(CONVERT(DECIMAL(10,2),(C.total*C.precioActual)) as nvarchar) AS valorizado"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " , SUBCATEGORIA AS SC"
                    + " , CATEGORIA AS CT"
                    + " , PLANTEL AS P"
                    + " , MEDIDA AS M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND C.estatus<>'Inactivo'"
                    + " AND P.ID_Plantel = ?"
                    + " AND SC.ID_SubCategoria=?"
                    + " GROUP BY"
                    + " P.nombre"
                    + " , CT.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , C.ID_Consumible"
                    + " , C.clave"
                    + " , C.descripcion"
                    + " , M.medida"
                    + " , C.estatus"
                    + " , C.precioActual"
                    + " , C.total) AS BN LEFT JOIN CONTEOFISICO_CONSUMIBLE AS CFC ON CFC.FK_ID_Consumible=BN.ID_Consumible "
                    + " AND CFC.FK_ID_ConteoFisico = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_Subcategoria);
            pstmt.setString(3, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountConsumiblesSinConteoFisico(String ID_ConteoFisico, String ID_Plantel) {
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " (SELECT "
                    + " COUNT(*) as existentes"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " , PLANTEL AS P"
                    + " WHERE "
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND P.ID_Plantel=?"
                    + " AND C.estatus<>'Inactivo')"
                    + " -"
                    + " (SELECT"
                    + " COUNT(*) AS contados"
                    + " FROM"
                    + " CONTEOFISICO_CONSUMIBLE AS CFC"
                    + " , CONTEOFISICO AS CF"
                    + " WHERE"
                    + " CFC.FK_ID_ConteoFisico=CF.ID_ConteoFisico"
                    + " AND CF.ID_ConteoFisico=?)";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_ConteoFisico_Conumible(String ID_ConteoFisico) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " CFC.FK_ID_Consumible"
                    + " , CFC.conteoFisico"
                    + " FROM"
                    + " CONTEOFISICO_CONSUMIBLE AS CFC"
                    + " , CONTEOFISICO AS CF"
                    + " WHERE"
                    + " CFC.FK_ID_ConteoFisico=CF.ID_ConteoFisico"
                    + " AND CF.ID_ConteoFisico=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_ConteoFisico);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_ConteoFisico4Campo(String nombreCampo, String ID_ConteoFisico) {
        String valorCampo = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + nombreCampo
                    + " FROM CONTEOFISICO"
                    + " WHERE"
                    + " ID_ConteoFisico = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_ConteoFisico);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                valorCampo = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valorCampo;
    }

    public final LinkedList select_TipoActividad() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " TA.ID_TipoActividad "
                    + " , TA.tipoActividad"
                    + " FROM TIPO_ACTIVIDAD AS TA"
                    + " ORDER BY TA.tipoActividad ASC ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Actividad(String ID_Plantel, boolean seeAll) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " A.ID_Actividad"
                    + " , A.descripcion"
                    + " , A.fechaCreacion"
                    + " , A.fechaInicio"
                    + " , A.fechaFin"
                    + " , A.ultimaActualizacion"
                    + " , A.horaCreacion"
                    + " , A.horaInicio"
                    + " , A.horaFin"
                    + " , A.horaUltimaActualizacion"
                    + " , A.FK_ID_UsuarioCreo"
                    + " , A.FK_ID_UsuarioActualizo"
                    + " , A.fechaLimite"
                    + " , A.horaLimite"
                    + " , AP.estatus"
                    + " , AP.fechaCambioEstatus"
                    + " , AP.horaCambioEstatus"
                    + " , AP.observaciones"
                    + " , AP.porcentajeCompleto"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " , AP.ID_Actividad_Plantel"
                    + " FROM"
                    + " ACTIVIDAD AS A"
                    + " , PLANTEL AS P"
                    + " , ACTIVIDAD_PLANTEL AS AP"
                    + " , USUARIO AS U"
                    + " , TIPO_ACTIVIDAD AS TA"
                    + " "
                    + " WHERE"
                    + " AP.FK_ID_Actividad=A.ID_Actividad"
                    + " AND AP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND A.FK_ID_TipoActividad=TA.ID_TipoActividad"
                    + " AND A.FK_ID_UsuarioCreo=U.ID_Usuario"
                    + " AND P.ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                aux.add(rs.getString(16));
                aux.add(rs.getString(17));
                aux.add(rs.getString(18));
                aux.add(rs.getString(19));
                aux.add(rs.getString(20));
                aux.add(rs.getString(21));
                aux.add(rs.getString(22));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ActividadPlantel(String ID_Actividad_Plantel) {

        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " A.ID_Actividad"
                    + " , A.descripcion"
                    + " , A.fechaCreacion"
                    + " , A.fechaInicio"
                    + " , A.fechaFin"
                    + " , A.ultimaActualizacion"
                    + " , A.horaCreacion"
                    + " , A.horaInicio"
                    + " , A.horaFin"
                    + " , A.horaUltimaActualizacion"
                    + " , A.FK_ID_UsuarioCreo"
                    + " , A.FK_ID_UsuarioActualizo"
                    + " , A.fechaLimite"
                    + " , A.horaLimite"
                    + " , AP.estatus"
                    + " , AP.fechaCambioEstatus"
                    + " , AP.horaCambioEstatus"
                    + " , AP.observaciones"
                    + " , AP.porcentajeCompleto"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " , AP.ID_Actividad_Plantel"
                    + " , TA.ID_TipoActividad"
                    + " , TA.tipoActividad"
                    + " FROM"
                    + " ACTIVIDAD AS A"
                    + " , PLANTEL AS P"
                    + " , ACTIVIDAD_PLANTEL AS AP"
                    + " , USUARIO AS U"
                    + " , TIPO_ACTIVIDAD AS TA"
                    + " "
                    + " WHERE"
                    + " AP.FK_ID_Actividad=A.ID_Actividad"
                    + " AND AP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND A.FK_ID_TipoActividad=TA.ID_TipoActividad"
                    + " AND A.FK_ID_UsuarioCreo=U.ID_Usuario"
                    + " AND AP.ID_Actividad_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Actividad_Plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
                listToSend.add(rs.getString(16));
                listToSend.add(rs.getString(17));
                listToSend.add(rs.getString(18));
                listToSend.add(rs.getString(19));
                listToSend.add(rs.getString(20));
                listToSend.add(rs.getString(21));
                listToSend.add(rs.getString(22));
                listToSend.add(rs.getString(23));
                listToSend.add(rs.getString(24));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_ActividadPlantel4Campo(String campo, String ID_Actividad_Plantel) {

        String value = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + campo
                    + " FROM"
                    + " ACTIVIDAD AS A"
                    + " , PLANTEL AS P"
                    + " , ACTIVIDAD_PLANTEL AS AP"
                    + " , USUARIO AS U"
                    + " , TIPO_ACTIVIDAD AS TA"
                    + " "
                    + " WHERE"
                    + " AP.FK_ID_Actividad=A.ID_Actividad"
                    + " AND AP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND A.FK_ID_TipoActividad=TA.ID_TipoActividad"
                    + " AND A.FK_ID_UsuarioCreo=U.ID_Usuario"
                    + " AND AP.ID_Actividad_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Actividad_Plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                LinkedList aux = new LinkedList();
                value = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public final int select_CountActividad(String ID_Actividad) {

        int count = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(*)"
                    + " FROM"
                    + " ACTIVIDAD AS A"
                    + " , PLANTEL AS P"
                    + " , ACTIVIDAD_PLANTEL AS AP"
                    + " , USUARIO AS U"
                    + " , TIPO_ACTIVIDAD AS TA"
                    + " "
                    + " WHERE"
                    + " AP.FK_ID_Actividad=A.ID_Actividad"
                    + " AND AP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND A.FK_ID_TipoActividad=TA.ID_TipoActividad"
                    + " AND A.FK_ID_UsuarioCreo=U.ID_Usuario"
                    + " AND A.ID_Actividad = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Actividad);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final int select_CountConumible4ConteoFisico(String FK_ID_Consumible, String FK_ID_ConteoFisico) {

        int count = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(*)"
                    + " FROM"
                    + " CONTEOFISICO_CONSUMIBLE AS CFC"
                    + " WHERE"
                    + " CFC.FK_ID_ConteoFisico=?"
                    + " AND CFC.FK_ID_Consumible=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_ConteoFisico);
            pstmt.setString(2, FK_ID_Consumible);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final int select_CountActividad4Plantel(String ID_Plantel, String estatus) {
        int count = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(*)"
                    + " FROM"
                    + " ACTIVIDAD AS A"
                    + " , PLANTEL AS P"
                    + " , ACTIVIDAD_PLANTEL AS AP"
                    + " , USUARIO AS U"
                    + " , TIPO_ACTIVIDAD AS TA"
                    + " "
                    + " WHERE"
                    + " AP.FK_ID_Actividad=A.ID_Actividad"
                    + " AND AP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND A.FK_ID_TipoActividad=TA.ID_TipoActividad"
                    + " AND A.FK_ID_UsuarioCreo=U.ID_Usuario"
                    + " AND P.ID_Plantel = ?"
                    + " AND AP.estatus = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Plantel);
            pstmt.setString(2, estatus);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final LinkedList select_SolicitudPlantel(String ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , S.ID_Solicitud"
                    + " , S.numeroOficio"
                    + " , S.nombreSolicitante"
                    + " , S.fechaSolicitud"
                    + " , S.status"
                    + " , S.observaciones"
                    + " , S.asunto"
                    + " , S.solicitud"
                    + " , S.justificacion"
                    + " , S.fechaModificacion"
                    + " FROM"
                    + " SOLICITUD AS S"
                    + " , PLANTEL AS P"
                    + " WHERE "
                    + " S.FK_ID_Plantel = P.ID_Plantel"
                    + " AND S.tipoSolicitud <> ? ";
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "BAJA");
            if (!seeAll) {
                pstmt.setString(2, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_SolicitudPlantel(String ID_Solicitud) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , S.numeroOficio"
                    + " , S.fechaSolicitud"
                    + " , S.status"
                    + " , S.observaciones"
                    + " , S.asunto"
                    + " , S.solicitud"
                    + " , S.justificacion"
                    + " , S.fechaModificacion"
                    + " , S.horaSolicitud"
                    + " , S.fechaCreacion"
                    + " FROM"
                    + " SOLICITUD AS S"
                    + " , PLANTEL AS P"
                    + " WHERE "
                    + " S.FK_ID_Plantel = P.ID_Plantel"
                    + " AND S.ID_Solicitud = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Solicitud);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Enlace(String ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , E.ID_Enlace"
                    + " , E.tipo"
                    + " , E.velocidadSubida"
                    + " , E.velocidadBajada"
                    + " , E.noAlumnosConectados"
                    + " , E.noDocentesConectados"
                    + " , E.noAdministrativosConectados"
                    + " , E.noDispositivosConectados"
                    + " , E.noNodos"
                    + " , E.calidadServicio"
                    + " , E.FK_ID_Proveedor"
                    + " , E.fechaModificacion"
                    + " , PV.nombreProveedor"
                    + " FROM "
                    + " ENLACE AS E"
                    + " , PLANTEL AS P"
                    + " , PROVEEDOR AS PV"
                    + " WHERE"
                    + " E.FK_ID_Plantel=P.ID_Plantel"
                    + " AND E.FK_ID_Proveedor=PV.ID_Proveedor";

            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Enlace(String ID_Enlace) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , E.tipo"
                    + " , E.velocidadSubida"
                    + " , E.velocidadBajada"
                    + " , E.noAlumnosConectados"
                    + " , E.noDocentesConectados"
                    + " , E.noAdministrativosConectados"
                    + " , E.noDispositivosConectados"
                    + " , E.noNodos"
                    + " , E.calidadServicio"
                    + " , E.FK_ID_Proveedor"
                    + " , E.fechaModificacion"
                    + " , PV.nombreProveedor"
                    + " FROM "
                    + " ENLACE AS E"
                    + " , PLANTEL AS P"
                    + " , PROVEEDOR AS PV"
                    + " WHERE"
                    + " E.FK_ID_Plantel=P.ID_Plantel"
                    + " AND E.FK_ID_Proveedor=PV.ID_Proveedor"
                    + " AND  E.ID_Enlace = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Enlace);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ResumenAgrupacion(String ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , D.nombreDepartamento"
                    + " , G.nombreGrupo"
                    + " , C.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , B.noSerie"
                    + " , B.noInventario"
                    + " FROM"
                    + " BIEN AS B"
                    + " , PLANTEL AS P"
                    + " , GRUPO AS G"
                    + " , GRUPO_BIEN AS GB"
                    + " , DEPARTAMENTO AS D"
                    + " , DEPARTAMENTO_PLANTEL AS DP"
                    + " , RELACION_PDG AS PDG"
                    + " , SUBCATEGORIA AS SC"
                    + " , CATEGORIA AS C"
                    + " , MARCA AS M"
                    + " , MODELO AS MD"
                    + " WHERE"
                    + " B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND B.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND DP.FK_ID_Plantel=P.ID_Plantel"
                    + " AND DP.FK_ID_Departamento=D.ID_Departamento"
                    + " AND PDG.FK_ID_Departamento_Plantel=DP.ID_Departamento_Plantel"
                    + " AND PDG.FK_ID_Grupo=G.ID_Grupo"
                    + " AND GB.FK_ID_Grupo=G.ID_Grupo"
                    + " AND GB.FK_ID_Bien=B.ID_Bien"
                    + " AND B.status <> 'BAJA'";
            if (!seeAll) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }
            SQLSentence += " ORDER BY "
                    + " P.nombre"
                    + " , D.nombreDepartamento"
                    + " , G.nombreGrupo"
                    + " , C.nombreCategoria"
                    + " , SC.nombreSubCategoria";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ObjetoArchivo(String nombreObjeto, String FK_ID_Objeto, String FK_ID_Plantel, boolean publicAccess, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " A.ID_Archivo"
                    + " , A.descripcion"
                    + " , A.extension"
                    + " , A.fechaActualizacion"
                    + " , CASE WHEN A.keywords IS NULL THEN ' ' ELSE A.keywords END"
                    + " , CASE WHEN A.tamanio IS NULL THEN ' ' ELSE (A.tamanio/1048576) END"
                    + " , A.nombreArchivo"
                    + " , OA.ID_Objeto_Archivo"
                    + " , TA.nombreTipo"
                    + " , A.tipoAcceso"
                    + " FROM"
                    + " ARCHIVO AS A"
                    + " , TIPO_ARCHIVO AS TA"
                    + " , OBJETO_ARCHIVO AS OA"
                    + " WHERE"
                    + " A.FK_ID_Tipo_Archivo = TA.ID_Tipo_Archivo"
                    + " AND OA.FK_ID_Archivo = A.ID_Archivo"
                    + " AND OA.FK_ID_Objeto = ?"
                    + " AND OA.nombreObjeto = ?";

            if (!seeAll) {
                if (publicAccess) {
                    SQLSentence += " AND ( A.FK_ID_Plantel = ?"
                            + " OR A.tipoAcceso = 'PUBLICO' )";
                } else {
                    SQLSentence += " AND A.FK_ID_Plantel = ?";
                }
            }
            String idModelo = "-1";
            if (nombreObjeto.equalsIgnoreCase("BIEN")) {
                idModelo = this.select_Bien4Campo("FK_ID_Modelo", FK_ID_Objeto);
                SQLSentence += " UNION " + SQLSentence;
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Objeto);
            pstmt.setString(2, nombreObjeto);
            if (!seeAll) {
                pstmt.setString(3, FK_ID_Plantel);
                if (nombreObjeto.equalsIgnoreCase("BIEN")) {
                    pstmt.setString(4, idModelo);
                    pstmt.setString(5, "MODELO");
                    pstmt.setString(6, FK_ID_Plantel);
                }
            } else if (nombreObjeto.equalsIgnoreCase("BIEN")) {
                pstmt.setString(3, idModelo);
                pstmt.setString(4, "MODELO");
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_CountArchivo4Objeto(String FK_ID_Archivo, String FK_ID_Objeto) {

        int count = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(*)"
                    + " FROM"
                    + " OBJETO_ARCHIVO AS OA"
                    + " WHERE"
                    + " OA.FK_ID_Archivo = ?"
                    + " AND OA.FK_ID_Objeto <> ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Archivo);
            pstmt.setString(2, FK_ID_Objeto);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final LinkedList select_Rubro() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " R.ID_Rubro "
                    + " , R.rubro"
                    + " FROM RUBRO AS R";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Puntuacion(String Fk_ID_Plantel, boolean seeAll) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " PU.ID_Puntuacion"
                    + " , PU.estatus"
                    + " , PU.fechaRegistro"
                    + " , PU.observaciones"
                    + " , PU.puntuacion"
                    + " , R.rubro"
                    + " , R.ID_Rubro"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM"
                    + " PLANTEL AS P"
                    + " , PUNTUACION AS PU"
                    + " , RUBRO AS R"
                    + " WHERE"
                    + " PU.FK_ID_Plantel = P.ID_plantel"
                    + " AND PU.FK_Id_Rubro = R.ID_Rubro"
                    + " AND PU.estatus = 1";
            if (!seeAll) {
                SQLSentence += " AND P.ID_plantel = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!seeAll) {
                pstmt.setString(1, Fk_ID_Plantel);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Puntuacion(String ID_Puntuacion) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + "  PU.estatus"
                    + " , PU.fechaRegistro"
                    + " , PU.observaciones"
                    + " , PU.puntuacion"
                    + " , R.rubro"
                    + " , R.ID_Rubro"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM"
                    + " PLANTEL AS P"
                    + " , PUNTUACION AS PU"
                    + " , RUBRO AS R"
                    + " WHERE"
                    + " PU.FK_ID_Plantel = P.ID_Plantel"
                    + " AND PU.FK_Id_Rubro = R.ID_Rubro"
                    + " AND PU.ID_Puntuacion = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Puntuacion);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PlantelAlmacen(String FK_ID_PlantelSurte, String estatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " PA.ID_Plantel_Almacen"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM "
                    + " PLANTEL AS P"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " WHERE"
                    + " P.ID_Plantel=PA.FK_ID_PlantelSolicita"
                    + " AND PA.FK_ID_PlantelSurte = ?"
                    + " AND PA.estatus = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_PlantelSurte);
            pstmt.setString(2, estatus);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_PlantelAlmacen4Campo(String campo, String ID_PlantelAlmacen) {
        String value = "";
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + campo
                    + " FROM "
                    + " PLANTEL_ALMACEN AS PA"
                    + " WHERE"
                    + " PA.ID_Plantel_Almacen = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_PlantelAlmacen);

            rs = pstmt.executeQuery();
            if (rs.next()) {

                value = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public final LinkedList select_PlantelAlmacen4PlantelSurte(String FK_ID_PlantelSurte, String FK_ID_PlantelSolicita) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT TOP 1"
                    + " PA.ID_Plantel_Almacen"
                    + " , PA.estatus"
                    + " FROM "
                    + " PLANTEL AS P"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " WHERE"
                    + " P.ID_Plantel=PA.FK_ID_PlantelSolicita"
                    + " AND PA.FK_ID_PlantelSurte = ?"
                    + " AND PA.FK_ID_PlantelSolicita = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_PlantelSurte);
            pstmt.setString(2, FK_ID_PlantelSolicita);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_OrdenSurtimiento4PlantelSolicita(String FK_ID_PlantelSolicita) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , OS.ID_Orden_Surtimiento"
                    + " , OS.FK_ID_Plantel_Almacen"
                    + " , OS.fechaOrden"
                    + " , OS.folio"
                    + " , OS.fechaRequerida"
                    + " , OS.justificacion"
                    + " , OS.observaciones"
                    + " , OS.estatus"
                    + " , OS.FK_ID_UsuarioCrea"
                    + " , OS.FK_ID_UsuarioEntrega"
                    + " , OS.fechaAtencion"
                    + " , PA.FK_ID_PlantelSolicita"
                    + " , CASE WHEN OS.asuntoGeneral IS NULL THEN '' ELSE OS.asuntoGeneral END"
                    + " FROM"
                    + " ORDEN_SURTIMIENTO AS OS"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " PA.FK_ID_PlantelSurte=P.ID_Plantel"
                    + " AND OS.FK_ID_Plantel_Almacen=PA.ID_Plantel_Almacen"
                    + " AND PA.FK_ID_PlantelSolicita = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_PlantelSolicita);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PlantelAlmacen4PlantelSolicita(String FK_ID_PlantelSolicita, String estatus) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " PA.ID_Plantel_Almacen"
                    + " , P.ID_Plantel"
                    + " , P.nombre"
                    + " FROM "
                    + " PLANTEL AS P"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " WHERE"
                    + " P.ID_Plantel=PA.FK_ID_PlantelSurte"
                    + " AND PA.FK_ID_PlantelSolicita = ?"
                    + " AND PA.estatus = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_PlantelSolicita);
            pstmt.setString(2, estatus);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final long select_nextID4Table(String nombreTabla) {
        long nextID = 1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " COUNT(*) "
                    + " FROM "
                    + "  " + nombreTabla;

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            long count = 0;
            if (rs.next()) {
                count = rs.getLong(1);
            }
            SQLSentence = ""
                    + " SELECT"
                    + " IDENT_CURRENT(?) ";

            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreTabla);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                nextID = rs.getLong(1);
            }
            if (!(count == 0 && nextID == 1)) {
                nextID += 1;
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nextID;
    }

    public final LinkedList select_OrdenSurtimiento(String ID_OrdenSurtimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " OSS.ID_Plantel"
                    + " ,OSS.nombre"///plantel surte
                    + " ,OSS.FK_ID_Plantel_Almacen"
                    + " ,OSS.fechaOrden"
                    + " ,OSS.folio"
                    + " ,OSS.fechaRequerida"
                    + " ,OSS.justificacion"
                    + " ,OSS.observaciones"
                    + " ,OSS.estatus"
                    + " ,OSS.FK_ID_UsuarioCrea"
                    + " ,OSS.FK_ID_UsuarioEntrega"
                    + " ,OSS.fechaAtencion"
                    + " ,PP.ID_Plantel"
                    + " ,PP.nombre"//plantel solicita
                    + " ,OSS.asuntoGeneral"
                    + " FROM "
                    + " (SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , OS.FK_ID_Plantel_Almacen"
                    + " , OS.fechaOrden"
                    + " , OS.folio"
                    + " , OS.fechaRequerida"
                    + " , OS.justificacion"
                    + " , OS.observaciones"
                    + " , OS.estatus"
                    + " , OS.FK_ID_UsuarioCrea"
                    + " , OS.FK_ID_UsuarioEntrega"
                    + " , OS.fechaAtencion"
                    + " , PA.FK_ID_PlantelSolicita"
                    + " , CASE WHEN OS.asuntoGeneral IS NULL THEN '' ELSE OS.asuntoGeneral END AS asuntoGeneral"
                    + " FROM"
                    + " ORDEN_SURTIMIENTO AS OS"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " PA.FK_ID_PlantelSurte=P.ID_Plantel"
                    + " AND OS.FK_ID_Plantel_Almacen=PA.ID_Plantel_Almacen"
                    + " AND OS.ID_Orden_Surtimiento = ?) AS OSS"
                    + " , PLANTEL AS PP"
                    + " WHERE"
                    + " OSS.FK_ID_PlantelSolicita=PP.ID_Plantel";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_OrdenSurtimiento);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                listToSend.add(rs.getString(1));
                listToSend.add(rs.getString(2));
                listToSend.add(rs.getString(3));
                listToSend.add(rs.getString(4));
                listToSend.add(rs.getString(5));
                listToSend.add(rs.getString(6));
                listToSend.add(rs.getString(7));
                listToSend.add(rs.getString(8));
                listToSend.add(rs.getString(9));
                listToSend.add(rs.getString(10));
                listToSend.add(rs.getString(11));
                listToSend.add(rs.getString(12));
                listToSend.add(rs.getString(13));
                listToSend.add(rs.getString(14));
                listToSend.add(rs.getString(15));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_OrdenSurtimientoConsumible(String ID_OrdenSurtimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " OSC.ID_Orden_Surtimiento_Consumible"
                    + " , OSC.FK_ID_Consumible"
                    + " , OSC.cantidadSolicitada"
                    + " , OSC.cantidadSurtida"
                    + " , OSC.observaciones"
                    + " , C.clave"
                    + " , C.descripcion"
                    + " , M.medida"
                    + " FROM"
                    + " ORDEN_SURTIMIENTO AS OS"
                    + " , ORDEN_SURTIMIENTO_CONSUMIBLE AS OSC"
                    + " , CONSUMIBLE AS C"
                    + " , MEDIDA AS M"
                    + " WHERE"
                    + " OSC.FK_ID_OrdenSurtimiento=OS.ID_Orden_Surtimiento"
                    + " AND OSC.FK_ID_Consumible=C.ID_Consumible"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND OS.ID_Orden_Surtimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_OrdenSurtimiento);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_OrdenSurtimiento4Campo(String campo, String ID_OrdenSurtimiento) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;
        String value = "";

        try {
            SQLSentence = ""
                    + " SELECT "
                    + campo
                    + " FROM"
                    + " ORDEN_SURTIMIENTO AS OS"
                    + " WHERE"
                    + " OS.ID_Orden_Surtimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_OrdenSurtimiento);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public final Integer select_CountMovimiento_OrdenSurtimiento(String ID_OrdenSurtimiento) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;
        Integer value = -1;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(*)"
                    + " FROM"
                    + " MOVIEMIENTO_ORDENSURTIMIENTO AS MOS"
                    + " WHERE"
                    + " MOS.FK_ID_Orden_Surtimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_OrdenSurtimiento);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                value = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public final LinkedList select_OrdenSurtimiento4PlantelSurte(String FK_ID_PlantelSurte) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " P.ID_Plantel"
                    + " , P.nombre"
                    + " , OS.ID_Orden_Surtimiento"
                    + " , OS.FK_ID_Plantel_Almacen"
                    + " , OS.fechaOrden"
                    + " , OS.folio"
                    + " , OS.fechaRequerida"
                    + " , OS.justificacion"
                    + " , OS.observaciones"
                    + " , OS.estatus"
                    + " , OS.FK_ID_UsuarioCrea"
                    + " , OS.FK_ID_UsuarioEntrega"
                    + " , OS.fechaAtencion"
                    + " , PA.FK_ID_PlantelSolicita"
                    + " , CASE WHEN OS.asuntoGeneral IS NULL THEN '' ELSE OS.asuntoGeneral END "
                    + " FROM"
                    + " ORDEN_SURTIMIENTO AS OS"
                    + " , PLANTEL_ALMACEN AS PA"
                    + " , PLANTEL AS P"
                    + " WHERE"
                    + " PA.FK_ID_PlantelSolicita=P.ID_Plantel"
                    + " AND OS.FK_ID_Plantel_Almacen=PA.ID_Plantel_Almacen"
                    + " AND PA.FK_ID_PlantelSurte = ?"
                    + " AND OS.estatus <> ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_PlantelSurte);
            pstmt.setString(2, "Pendiente");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList aux = new LinkedList();
                aux.add(rs.getString(1));
                aux.add(rs.getString(2));
                aux.add(rs.getString(3));
                aux.add(rs.getString(4));
                aux.add(rs.getString(5));
                aux.add(rs.getString(6));
                aux.add(rs.getString(7));
                aux.add(rs.getString(8));
                aux.add(rs.getString(9));
                aux.add(rs.getString(10));
                aux.add(rs.getString(11));
                aux.add(rs.getString(12));
                aux.add(rs.getString(13));
                aux.add(rs.getString(14));
                aux.add(rs.getString(15));
                listToSend.add(aux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final String select_OrdenSurtimientoConsumible4Campo(String campo, String ID_OrdenSurtimientoConsumible) {
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;
        String value = "";

        try {
            SQLSentence = ""
                    + " SELECT "
                    + campo
                    + " FROM"
                    + " ORDEN_SURTIMIENTO_CONSUMIBLE AS OS"
                    + " WHERE"
                    + " OS.ID_Orden_Surtimiento_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_OrdenSurtimientoConsumible);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }

    public final LinkedList select_RandomSerialNumbers(String FK_ID_Modelo, int tamanioMuestra) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT TOP " + tamanioMuestra
                    + " noSerie "
                    + " FROM "
                    + " BIEN "
                    + " WHERE "
                    + " FK_ID_Modelo = ? "
                    + " ORDER BY NEWID()";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Modelo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ID_Modelos() {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " ID_Modelo "
                    + " FROM "
                    + " MODELO ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final Long select_countSerialByModelo(String ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long total = 0L;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " COUNT(ID_Bien)"
                    + " FROM "
                    + " BIEN"
                    + " WHERE"
                    + " FK_ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Modelo);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getLong(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            total = -1L;
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_Patron_SerieDeUsuario(String FK_ID_Modelo, String ID_Usuario) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Patron_Serie"
                    + " ,patron"
                    + " ,fechaActualizacion"
                    + " ,longitudNoSerie"
                    + " ,tipoPatron"
                    + " ,coeficientePresicion"
                    + " ,usuarioActualizo"
                    + " FROM PATRON_SERIE"
                    + " WHERE "
                    + " FK_ID_Modelo = ?";
            if (ID_Usuario.equalsIgnoreCase("")) {
                SQLSentence += " AND usuarioActualizo <> -1";
            } else {
                SQLSentence += " AND usuarioActualizo = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Modelo);
            if (!ID_Usuario.equalsIgnoreCase("")) {
                pstmt.setString(2, ID_Usuario);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Patron_SerieXId(String longitud, String tipoPatron, String FK_ID_Modelo, String usuarioActualizo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " ID_Patron_Serie"
                    + " FROM PATRON_SERIE"
                    + " WHERE "
                    + " FK_ID_Modelo = ?"
                    + " AND tipoPatron = ?"
                    + " AND longitudNoSerie = ?";
            if (!usuarioActualizo.equalsIgnoreCase("")) {
                SQLSentence += " AND usuarioActualizo = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Modelo);
            pstmt.setString(2, tipoPatron);
            pstmt.setString(3, longitud);
            if (!usuarioActualizo.equalsIgnoreCase("")) {
                pstmt.setString(4, usuarioActualizo);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getInt(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_Patron_Serie(String FK_ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " PE.ID_Patron_Serie"
                    + " ,PE.patron"
                    + " ,PE.fechaActualizacion"
                    + " ,PE.longitudNoSerie"
                    + " ,PE.tipoPatron"
                    + " ,PE.coeficientePresicion"
                    + " ,PE.usuarioActualizo"
                    + " ,CASE WHEN U.nombreCompleto IS NULL THEN 'sistema' ELSE U.nombreCompleto END"
                    + " FROM PATRON_SERIE AS PE LEFT JOIN USUARIO AS U ON"
                    + " PE.usuarioActualizo = U.ID_Usuario"
                    + " WHERE "
                    + " FK_ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Modelo);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PatronParticularXModelo(String FK_ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " patron "
                    + " FROM "
                    + " PATRON_SERIE"
                    + " WHERE"
                    //                    + " tipoPatron <> 'generica'"
                    + "  FK_ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Modelo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(PatronUtil.limpiaPatron(rs.getString(1)));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PatronParticularXMarca(String FK_ID_Modelo) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " patron "
                    + " FROM "
                    + " PATRON_SERIE"
                    + " WHERE"
                    //                    + " tipoPatron <> 'generica'"
                    + "  FK_ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Modelo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(PatronUtil.limpiaPatron(rs.getString(1)));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PatronXModelo(String FK_ID_Modelo, boolean includeGenerico) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " patron "
                    + " FROM "
                    + " PATRON_SERIE"
                    + " WHERE"
                    + "  FK_ID_Modelo = ?";
            if (!includeGenerico) {
                SQLSentence += " AND tipoPatron <> 'generica'";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Modelo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(PatronUtil.limpiaPatron(rs.getString(1)));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_MismoModeloDiferentePatron(String FK_ID_Modelo, LinkedList patrones, String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " B.ID_Bien"
                    + " ,P.nombre"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , C.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , B.noInventario"
                    + " , B.noSerie"
                    + " FROM"
                    + " Plantel AS P"
                    + " , BIEN AS B"
                    + " , Marca AS M"
                    + " , MODELO AS MD"
                    + " , CATEGORIA AS C"
                    + " , SUBCATEGORIA AS SC"
                    + " WHERE"
                    + " B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.status <> 'Baja'";

            if (!FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND MD.ID_Modelo = ?";
            }

            SQLSentence += " AND (";

            for (int i = 0; i < patrones.size(); i++) {
                SQLSentence += " B.noSerie NOT LIKE ?";
                if (i + 1 < patrones.size()) {
                    SQLSentence += " AND ";
                }
            }
            SQLSentence += ")";
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += "AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            int corrimiento = 1;
            if (!FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, FK_ID_Modelo);
                corrimiento = 2;
            }
            for (int i = 0; i < patrones.size(); i++) {
                pstmt.setString(i + corrimiento, patrones.get(i).toString());
            }
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(patrones.size() + corrimiento, FK_ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_DiferenteModeloMismoPatron(String FK_ID_Modelo, LinkedList patrones, String FK_ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT"
                    + " B.ID_Bien"
                    + " ,P.nombre"
                    + " , M.marca"
                    + " , MD.modelo"
                    + " , C.nombreCategoria"
                    + " , SC.nombreSubCategoria"
                    + " , B.noInventario"
                    + " , B.noSerie"
                    + " FROM"
                    + " Plantel AS P"
                    + " , BIEN AS B"
                    + " , Marca AS M"
                    + " , MODELO AS MD"
                    + " , CATEGORIA AS C"
                    + " , SUBCATEGORIA AS SC"
                    + " WHERE"
                    + " B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.FK_ID_Modelo=MD.ID_Modelo"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND MD.FK_ID_Marca=M.ID_Marca"
                    + " AND SC.FK_ID_Categoria=C.ID_Categoria"
                    + " AND B.status <> 'Baja'"
                    + " AND MD.ID_Modelo <> ?"
                    + " AND (";

            for (int i = 0; i < patrones.size(); i++) {
                SQLSentence += " B.noSerie LIKE ?";
                if (i + 1 < patrones.size()) {
                    SQLSentence += " OR ";
                }
            }
            SQLSentence += ")";
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                SQLSentence += "AND P.ID_Plantel = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Modelo);
            for (int i = 0; i < patrones.size(); i++) {
                pstmt.setString(i + 2, patrones.get(i).toString());
            }
            if (!FK_ID_Plantel.equalsIgnoreCase("todos")) {
                pstmt.setString(patrones.size() + 1, FK_ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_PatronXMarcaXModelo(String FK_ID_Modelo, String ID_Marca) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {

            SQLSentence = ""
                    + " SELECT"
                    + " MD.ID_Modelo"
                    + " ,M.marca"
                    + " ,MD.modelo"
                    + " ,P.patron"
                    + " ,P.longitudNoSerie"
                    + " ,P.coeficientePresicion"
                    + " ,P.tipoPatron"
                    + " ,P.fechaActualizacion"
                    + " ,CASE WHEN U.nombreCompleto IS NULL THEN 'sistema' ELSE U.nombreCompleto END"
                    + " FROM PATRON_SERIE AS P LEFT JOIN USUARIO AS U ON"
                    + " P.usuarioActualizo = U.ID_Usuario"
                    + " , MARCA AS M"
                    + " , MODELO AS MD"
                    + " WHERE "
                    + " MD.FK_ID_Marca=M.ID_Marca"
                    + " AND P.FK_ID_Modelo=MD.ID_Modelo";
            if (!ID_Marca.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND M.ID_Marca = ?";
            }

            if (!FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND MD.ID_Modelo = ?";
            }
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            if (!ID_Marca.equalsIgnoreCase("Todos") && !FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, ID_Marca);
                pstmt.setString(2, FK_ID_Modelo);
            } else if (!ID_Marca.equalsIgnoreCase("Todos") && FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, ID_Marca);
            } else if (ID_Marca.equalsIgnoreCase("Todos") && !FK_ID_Modelo.equalsIgnoreCase("Todos")) {
                pstmt.setString(1, FK_ID_Modelo);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getInt(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                llaux.add(rs.getString(7));
                llaux.add(rs.getString(8));
                llaux.add(rs.getString(9));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_siguienteFolioXTipoMovimiento(String movimiento) {
        int count = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT TOP 1"
                    + " cuentaTotalMovimiento"
                    + " FROM"
                    + " TIPO_MOVIMIENTO"
                    + " WHERE "
                    + " tipoMovimiento COLLATE SQL_Latin1_General_CP1_CI_AI = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setString(1, movimiento);
            pstmt.setQueryTimeout(statementTimeOut);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1) + 1;

            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public final LinkedList select_ModeloPosibleXNoSerieXSubcategoria(String ID_SubCategoria, String noSerie) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " MR.marca"
                    + " , M.modelo"
                    + " , SC.nombreSubCategoria"
                    + " FROM"
                    + " PATRON_SERIE AS PE"
                    + " , MODELO AS M"
                    + " , MARCA AS MR"
                    + " , BIEN as B"
                    + " , SUBCATEGORIA AS SC"
                    + " WHERE M.FK_ID_Marca=MR.ID_Marca"
                    + " AND B.FK_ID_Modelo=M.ID_Modelo"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND PE.FK_ID_Modelo=M.ID_Modelo"
                    + " AND '" + noSerie + "' LIKE PE.patron"
                    + " AND PE.tipoPatron <> 'generica'"
                    + " AND SC.ID_SubCategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_SubCategoria);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ModeloXPatronXSubcategoria(String ID_SubCategoria, String patron, String condicion, String ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " B.ID_Bien"
                    + " ,P.nombre"
                    + " ,MR.marca"
                    + " , M.modelo"
                    + " ,B.noSerie"
                    + " , SC.nombreSubCategoria"
                    + " FROM"
                    + " PATRON_SERIE AS PE"
                    + " , MODELO AS M"
                    + " , MARCA AS MR"
                    + " , BIEN as B"
                    + " , SUBCATEGORIA AS SC"
                    + " , PLANTEL AS P"
                    + " WHERE M.FK_ID_Marca=MR.ID_Marca"
                    + " AND B.FK_ID_Modelo=M.ID_Modelo"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND B.noSerie "
                    + condicion + " LIKE ?"
                    + " AND SC.ID_SubCategoria = ?";
            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, patron);
            pstmt.setString(2, ID_SubCategoria);
            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                pstmt.setString(3, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final LinkedList select_ModeloXPatronXSM(String patron, String ID_Plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " SELECT DISTINCT"
                    + " B.ID_Bien"
                    + " ,P.nombre"
                    + " ,MR.marca"
                    + " , M.modelo"
                    + " ,B.noSerie"
                    + " , SC.nombreSubCategoria"
                    + " FROM"
                    + " PATRON_SERIE AS PE"
                    + " , MODELO AS M"
                    + " , MARCA AS MR"
                    + " , BIEN as B"
                    + " , SUBCATEGORIA AS SC"
                    + " , PLANTEL AS P"
                    + " WHERE M.FK_ID_Marca=MR.ID_Marca"
                    + " AND B.FK_ID_Modelo=M.ID_Modelo"
                    + " AND B.FK_ID_SubCategoria=SC.ID_SubCategoria"
                    + " AND B.FK_ID_Plantel=P.ID_Plantel"
                    + " AND (M.modelo LIKE '%sin%' OR M.modelo LIKE '%s/m%' OR M.modelo LIKE '%s / m%' OR M.modelo LIKE 'sm' OR M.modelo = '' OR M.modelo LIKE '[ ]%' OR M.modelo LIKE '%[ ]')"
                    + " AND B.noSerie LIKE ?";
            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                SQLSentence += " AND P.ID_Plantel = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, patron);
            if (!ID_Plantel.equalsIgnoreCase("Todos")) {
                pstmt.setString(2, ID_Plantel);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList llaux = new LinkedList();
                llaux.add(rs.getString(1));
                llaux.add(rs.getString(2));
                llaux.add(rs.getString(3));
                llaux.add(rs.getString(4));
                llaux.add(rs.getString(5));
                llaux.add(rs.getString(6));
                listToSend.add(llaux);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int select_MovimientoxPlantelxFechas(int tipoMov, String fechaInicio, String FechaFin, int plantel) {
        //--------retorna el numero de movimientos realizados por ese plantel, por determinada fecha y por tipo de movimiento-----
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT COUNT(*) AS TOTAL"
                    + " FROM"
                    + " MOVIMIENTO AS M"
                    + " ,PLANTEL AS P"
                    + " WHERE M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=?"
                    + " AND P.ID_Plantel= ?"
                    + " AND M.fechaMovimiento BETWEEN ? AND ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, tipoMov);
            pstmt.setInt(2, plantel);
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, FechaFin);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final String select_Plantel(int idPlantel) {

        //Retorna plantel 
        String plantel = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BigDecimal id = null;

        try {
            SQLSentence = ""
                    + " SELECT "
                    + " P.nombre AS PLANTEL"
                    + " FROM PLANTEL AS P"
                    + " WHERE P.ID_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, idPlantel);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                plantel = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return plantel;
    }

    /**
     * <p>
     * Metodo - SELECT - retorna los movimientos realizados por ese plantel, por
     * determinada fecha y por tipo de movimiento
     * <p>
     *
     * @param int tipoMov - tipo de movimiento 1=etrada, 2=salida
     * @param String fechaInicio
     * @param String FechaFin
     * @param int plantel - ID del plantel
     * @return LinkedList - String de ID de los movimientos
     */
    public final LinkedList select_MovimientoxPlantelxFechasxTipo(int tipoMov, String fechaInicio, String FechaFin, int plantel) {
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            
            SQLSentence = " "
                    + " SELECT M.ID_Movimiento AS MOVIMIENTO"
                    + " FROM"
                    + " MOVIMIENTO AS M"
                    + " ,PLANTEL AS P"
                    + " WHERE M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=?";
            if(plantel==0)
            {
                SQLSentence+=" AND M.fechaMovimiento BETWEEN ? AND ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, tipoMov);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, FechaFin);
            
            }else{
                SQLSentence+= " AND P.ID_Plantel=?"
                             +" AND M.fechaMovimiento BETWEEN ? AND ?";
                jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, tipoMov);
            pstmt.setInt(2, plantel);
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, FechaFin);
            
            }
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                listToSend.add(rs.getString(1));
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }

    public final int contador_ConsumiblesxPlantel(int plantel) {
        //--------retorna el numero de consumibles dados de alta por ese plantel-----
        int total = 0;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + " SELECT"
                    + " COUNT(*) AS TOTAL"
                    + " FROM"
                    + " CONSUMIBLE AS C"
                    + " ,PLANTEL AS P"
                    + " ,SUBCATEGORIA AS SC"
                    + " ,CATEGORIA AS CT"
                    + " ,MEDIDA AS M"
                    + " WHERE"
                    + " C.FK_ID_Plantel=P.ID_Plantel"
                    + " AND C.FK_ID_Medida=M.ID_Medida"
                    + " AND C.FK_ID_Subcategoria=SC.ID_SubCategoria"
                    + " AND SC.FK_ID_Categoria=CT.ID_Categoria"
                    + " AND  P.ID_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, plantel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public final LinkedList select_PlantelxMovimientoxfechas(int tipoMov, String fechaInicio, String fechafin) {
        //--------retorna planteles que hicieron movimientos en determinadas fechas-----
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = " "
                    + "SELECT DISTINCT P.ID_Plantel, P.nombre"
                    + " FROM MOVIMIENTO AS M,"
                    + " PLANTEL AS P"
                    + ", TIPO_MOVIMIENTO AS TM"
                    + " WHERE M.FK_ID_Plantel=P.ID_Plantel"
                    + " AND M.FK_ID_Tipo_Movimiento=TM.ID_Tipo_Movimiento"
                    + " AND TM.ID_Tipo_Movimiento= ?"
                    + " AND M.fechaMovimiento BETWEEN ? AND ?"
                    + " ORDER BY P.ID_Plantel ASC;";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, tipoMov);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechafin);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList auxList = new LinkedList();
                auxList.add(rs.getInt(1));
                auxList.add(rs.getString(2));
                listToSend.add(auxList);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }
    
    public final LinkedList select_CCTyNombrePlantel(String searchText) {
        //--------retorna el CCT y Nombre de Plantel de todos los planteles.-----
        LinkedList listToSend = new LinkedList();
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        searchText = searchText.trim();
        try {
            SQLSentence = " "
                    + " SELECT P.claveCentroTrabajo, P.nombre, P.ID_Plantel"
                    + " FROM PLANTEL P"
                    + " WHERE P.claveCentroTrabajo LIKE ? OR P.nombre COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?"
                    + " ORDER BY P.claveCentroTrabajo ASC;";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "%" + searchText.toUpperCase() + "%");
            pstmt.setString(2, "%" + searchText.toUpperCase() + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LinkedList auxList = new LinkedList();
                auxList.add(rs.getString(1));
                auxList.add(rs.getString(2));
                auxList.add(rs.getString(3));
                listToSend.add(auxList);
            }

            endConnection(jscp, conn, pstmt, rs);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listToSend;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Insert">

    public final Transporter insert_Tipo_Proveedor(String nombreTipo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO TIPO_PROVEEDOR"
                    + " (nombreTipo)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreTipo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_Detalle(String nombreDetalle) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO DETALLE"
                    + " (nombreDetalle)"
                    + " VALUES"
                    + " ("
                    + " ? )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreDetalle);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_Plantel(
            String nombre,
            String direccion,
            String claveCentroTrabajo,
            String telefono,
            String correo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO PLANTEL"
                    + " ("
                    + " nombre"
                    + " ,direccion"
                    + " ,claveCentroTrabajo"
                    + " ,telefono"
                    + " ,correo"
                    + " )"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, claveCentroTrabajo);
            pstmt.setString(4, telefono);
            pstmt.setString(5, correo);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
        }
        return tport;
    }

    public final Transporter insert_Permiso(
            String nombrePermiso,
            String tipoPermiso,
            String descripcion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO PERMISO"
                    + " (nombrePermiso"
                    + " , tipoPermiso"
                    + " , descripcion)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, nombrePermiso);
            pstmt.setString(2, tipoPermiso);
            pstmt.setString(3, descripcion);
            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_Marca(
            String nombreMarca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO MARCA ("
                    + " marca"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Tipo_Software(
            String nombreMarca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO TIPO_SOFTWARE ("
                    + " tipo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Licencia(
            String nombreMarca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO LICENCIA ("
                    + " licencia"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Tipo_Compra(
            String nombreMarca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO TIPO_COMPRA ("
                    + " compra"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreMarca.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Tipo_Garantia(
            String garantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO TIPO_GARANTIA ("
                    + " garantia"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, garantia.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Modelo(
            String modelo,
            int FK_ID_Marca,
            String descricion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO MODELO ("
                    + " modelo"
                    + " ,FK_ID_Marca"
                    + " ,descripcion"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, modelo);
            pstmt.setInt(2, FK_ID_Marca);
            pstmt.setString(3, descricion);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    /**
     * Inserta un nuevo rol
     *
     * @param String nombre del rol
     * @param String descripción del rol
     * @return Transporter con información de la opración
     */
    public final String insert_newRol(
            String rolUsuario,
            String descripcion) {
        //Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String ID_rol = null;
        String[] idCol = {"ID_ROL"};
        try {
            SQLSentence = ""
                    + " INSERT INTO ROL"
                    + " (nombreRol"
                    + " ,descripcion)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, rolUsuario);
            pstmt.setString(2, descripcion);

            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                ID_rol = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
            //tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            //tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return ID_rol;
    }

    /**
     * Inserta los permisos de los roles de usuario
     *
     * @param int ID_Rol
     * @param int ID_Permiso
     * @return Transporter con información de la opración
     */
    public final Transporter insert_RolPermiso(
            int ID_Rol,
            int ID_Permiso) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO ROL_PERMISO"
                    + " (FK_ID_Rol"
                    + " ,FK_ID_Permiso)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Rol);
            pstmt.setInt(2, ID_Permiso);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_RolUsuario(
            String FK_ID_Usuario,
            String FK_ID_Rol) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO ROL_USUARIO"
                    + " (FK_ID_ROL"
                    + ", FK_ID_USUARIO)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Rol);
            pstmt.setString(2, FK_ID_Usuario);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_Usuario(
            String usuario,
            String password,
            String correo,
            String nombreCompleto,
            short status,
            String FK_ID_Plantel) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String[] idCol = {"ID_USUARIO"};
        String id_inserted = "";
        try {
            SQLSentence = ""
                    + " INSERT INTO USUARIO"
                    + " (usuario"
                    + ", password"
                    + ", correo"
                    + ", nombreCompleto"
                    + ", status"
                    + ", FK_ID_Plantel)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, usuario);
            pstmt.setString(2, JHash.getStringMessageDigest(password, JHash.MD5));
            pstmt.setString(3, correo);
            pstmt.setString(4, nombreCompleto);
            pstmt.setShort(5, status);
            pstmt.setString(6, FK_ID_Plantel);
            int rowCount = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter insert_Personal(
            String curp,
            String nombre,
            String aPaterno,
            String aMaterno,
            String telefono,
            String titulo,
            String correo,
            String siglas,
            String fechaAlta) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = null;
        String[] idCol = {"ID_PERSONAL"};
        try {
            SQLSentence = ""
                    + " INSERT INTO PERSONAL ("
                    + " nombre"
                    + " ,telefono"
                    + " ,correo"
                    + " ,fechaAlta"
                    + " ,titulo"
                    + " ,siglasTitulo"
                    + " ,aPaterno"
                    + " ,aMaterno"
                    + " ,curp"
                    + " ) "
                    + " VALUES ("
                    + "  ? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setString(2, telefono);
            pstmt.setString(3, correo);
            pstmt.setString(4, fechaAlta);
            pstmt.setString(5, titulo);
            pstmt.setString(6, siglas);
            pstmt.setString(7, aPaterno);
            pstmt.setString(8, aMaterno);
            pstmt.setString(9, curp);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter insert_Proveedor(
            String nombre,
            String clave,
            String telefono,
            String correo,
            String calle,
            String localidad,
            String colonia,
            String codigoPostal,
            String noExterior) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO PROVEEDOR ("
                    + " nombreProveedor"
                    + " ,claveProveedor"
                    + " ,telefono"
                    + " ,correo"
                    + " ,calle"
                    + " ,localidad"
                    + " ,colonia"
                    + " ,codigoPostal"
                    + " ,noExterior"
                    + " ) "
                    + " VALUES ("
                    + "  ? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setString(2, clave);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, calle);
            pstmt.setString(6, localidad);
            pstmt.setString(7, colonia);
            pstmt.setString(8, codigoPostal);
            pstmt.setString(9, noExterior);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter insert_PersonalPlantel(int FK_ID_Personal, int FK_ID_Plantel, String situacionActual, String cargo, String fechaCargo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO PERSONAL_PLANTEL ("
                    + " FK_ID_Personal"
                    + " ,FK_ID_Plantel"
                    + " ,estatus"
                    + " ,cargo"
                    + " ,fechaAsignacionCargo"
                    + " )"
                    + " VALUES ("
                    + "  ? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " ,? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, FK_ID_Personal);
            pstmt.setInt(2, FK_ID_Plantel);
            pstmt.setString(3, situacionActual);
            pstmt.setString(4, cargo);
            pstmt.setString(5, fechaCargo);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id_inserted = rs.getString(1);
            }

            endConnection(jscp, conn, pstmt, rs);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter insert_Categoria(
            String nombreCategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO CATEGORIA ("
                    + " nombreCategoria"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreCategoria.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_SubCategoria(
            int FK_ID_Categoria,
            String nombreSubCategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO SUBCATEGORIA ("
                    + " nombreSubCategoria"
                    + " ,FK_ID_Categoria"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSubCategoria);
            pstmt.setInt(2, FK_ID_Categoria);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Valor(String FK_ID_Detalle, String valor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO VALOR ("
                    + " valor"
                    + " ,FK_ID_Detalle"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, valor);
            pstmt.setString(2, FK_ID_Detalle);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_DetalleSubcategoria(String FK_ID_Detalle, String FK_ID_Subcategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO DETALLE_SUBCATEGORIA ("
                    + " FK_ID_Detalle"
                    + " ,FK_ID_Subcategoria"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Detalle);
            pstmt.setString(2, FK_ID_Subcategoria);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Grupo(String nombreGrupo, String fechaAlta) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String id_inserted = null;
        String[] idCol = {"ID_GRUPO"};
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO GRUPO ("
                    + " nombreGrupo"
                    + " ,fechaAlta"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreGrupo.toUpperCase());
            pstmt.setString(2, fechaAlta);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_RelacionPDG(String FK_ID_Grupo, String FK_ID_DepartamentoPlantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String id_inserted = null;
        String[] idCol = {"ID_PDG"};
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO RELACION_PDG ("
                    + " FK_ID_Grupo"
                    + " ,FK_ID_Departamento_Plantel"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Grupo);
            pstmt.setString(2, FK_ID_DepartamentoPlantel);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Software(
            String nombreSoftware,
            String version,
            String serial,
            String fechaVencimiento,
            String fechaAdquisicion,
            String noDictamen,
            String noLicencias,
            String hddRequerido,
            String ramRequerida,
            String soRequerido,
            String soporteTecnico,
            String emailSoporteTecnico,
            String telefonoSoporte,
            String FK_ID_Tipo_Software,
            String FK_ID_Licencia,
            String FK_ID_Tipo_Compra,
            String observaciones,
            String nombreArchivo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO SOFTWARE ("
                    + " nombreSoftware"
                    + " ,version"
                    + " ,serial"
                    + " ,fechaVencimiento"
                    + " ,fechaAdquisicion"
                    + " ,noDictamen"
                    + " ,noLicencias"
                    + " ,hddRequerido"
                    + " ,ramRequerida"
                    + " ,soRequerido"
                    + " ,soporteTecnico"
                    + " ,emailSoporteTecnico"
                    + " ,telefonoSoporte"
                    + " ,FK_ID_Tipo_Software"
                    + " ,FK_ID_Licencia"
                    + " ,FK_ID_Tipo_Compra"
                    + " ,observaciones"
                    + " ,nombreArchivo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSoftware);
            pstmt.setString(2, version);
            pstmt.setString(3, serial);
            pstmt.setString(4, fechaVencimiento);
            pstmt.setString(5, fechaAdquisicion);
            pstmt.setString(6, noDictamen);
            pstmt.setString(7, noLicencias);
            pstmt.setString(8, hddRequerido);
            pstmt.setString(9, ramRequerida);
            pstmt.setString(10, soRequerido);
            pstmt.setString(11, soporteTecnico);
            pstmt.setString(12, emailSoporteTecnico);
            pstmt.setString(13, telefonoSoporte);
            pstmt.setString(14, FK_ID_Tipo_Software);
            pstmt.setString(15, FK_ID_Licencia);
            pstmt.setString(16, FK_ID_Tipo_Compra);
            pstmt.setString(17, observaciones);
            pstmt.setString(18, nombreArchivo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Departamento(
            String nombreCategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO DEPARTAMENTO ("
                    + " nombreDepartamento"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreCategoria.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_NombreSoftware(
            String nombreSoftware) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO NOMBRE_SOFTWARE ("
                    + " nombre"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreSoftware.toUpperCase());
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Bien(
            String FK_ID_Subcategoria,
            String FK_ID_Modelo,
            String descripcionbien,
            String noserie,
            String nodictamen,
            String nofactura,
            String noinventario,
            String FK_ID_TipoCompra,
            String fechaCompra,
            String FK_ID_Plantel,
            String FK_ID_DepartamentoPlantel,
            String fechaAlta,
            String status,
            String observaciones,
            String archivo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String[] idCol = {"ID_BIEN"};
        String id_inserted = "";
        try {
            SQLSentence = ""
                    + " INSERT INTO BIEN ("
                    + " FK_ID_Subcategoria"
                    + " ,FK_ID_Modelo"
                    + " ,descripcionBien"
                    + " ,noSerie"
                    + " ,noDictamen"
                    + " ,noFactura"
                    + " ,noInventario"
                    + " ,FK_ID_Tipo_Compra"
                    + " ,fechaCompra"
                    + " ,FK_ID_Plantel"
                    + " ,FK_ID_Departamento_Plantel"
                    + " ,fechaAlta"
                    + " ,status"
                    + " ,observaciones"
                    + " ,archivo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Subcategoria);
            pstmt.setString(2, FK_ID_Modelo);
            pstmt.setString(3, descripcionbien);
            pstmt.setString(4, noserie);
            pstmt.setString(5, nodictamen);
            pstmt.setString(6, nofactura);
            pstmt.setString(7, noinventario);
            pstmt.setString(8, FK_ID_TipoCompra);
            pstmt.setString(9, fechaCompra);
            pstmt.setString(10, FK_ID_Plantel);
            pstmt.setString(11, FK_ID_DepartamentoPlantel);
            pstmt.setString(12, fechaAlta);
            pstmt.setString(13, status);
            pstmt.setString(14, observaciones);
            pstmt.setString(15, archivo);
            int rowCount = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_RelacionBDS(String FK_ID_Bien, String FK_ID_DetalleSubcategoria, String FK_ID_Valor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String id_inserted = null;
        String[] idCol = {"ID_Relacion_BDS"};
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO RELACION_BDS ("
                    + " FK_ID_BIEN"
                    + " ,FK_ID_Valor"
                    + " ,FK_ID_Detalle_Subcategoria"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, FK_ID_Valor);
            pstmt.setString(3, FK_ID_DetalleSubcategoria);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_GrupoBien(String FK_ID_Bien, String FK_ID_Grupo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String id_inserted = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO GRUPO_BIEN ("
                    + " FK_ID_BIEN"
                    + " ,FK_ID_Grupo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, FK_ID_Grupo);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_RelacionPBT(String FK_ID_Bien, String FK_ID_TipoProveedor, String FK_ID_Proveedor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        String id_inserted = null;
        String[] idCol = {"ID_PBT"};
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO RELACION_PBT ("
                    + " FK_ID_Proveedor"
                    + " ,FK_ID_Bien"
                    + " ,FK_ID_Tipo_Proveedor"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Proveedor);
            pstmt.setString(2, FK_ID_Bien);
            pstmt.setString(3, FK_ID_TipoProveedor);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Garantia(String FK_ID_Bien, String fechaInicio, String fechFin, String FK_ID_TipoGarantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO GARANTIA ("
                    + " FK_ID_Bien"
                    + " ,fechaInicio"
                    + " ,fechaFin"
                    + " ,FK_ID_Tipo_Garantia"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechFin);
            pstmt.setString(4, FK_ID_TipoGarantia);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Baja(String fechaBaja, String noOficio, String observaciones, String motivoBaja, String FK_ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO BAJA ("
                    + " FK_ID_Bien"
                    + " ,fechaBaja"
                    + " ,noOficio"
                    + " ,observaciones"
                    + " ,motivoBaja"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, fechaBaja);
            pstmt.setString(3, noOficio);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, motivoBaja);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Solicitud(
            String noOficio,
            String nombreSolicitante,
            String fechaSolicitud,
            String nombreResponsable,
            String status,
            String observaciones,
            String FK_ID_Plantel,
            String tipoSolicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO SOLICITUD ("
                    + " numeroOficio"
                    + " ,nombreSolicitante"
                    + " ,fechaSolicitud"
                    + " ,nombreResponsable"
                    + " ,status"
                    + " ,observaciones"
                    + " ,FK_ID_Plantel"
                    + " ,tipoSolicitud"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, noOficio);
            pstmt.setString(2, nombreSolicitante);
            pstmt.setString(3, fechaSolicitud);
            pstmt.setString(4, nombreResponsable);
            pstmt.setString(5, status);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, FK_ID_Plantel);
            pstmt.setString(8, tipoSolicitud);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_CheckList(String descripcion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO CHECKLIST ("
                    + " descripcion"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, descripcion);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Incidente(String incidente) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO INCIDENTE ("
                    + " incidente"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, incidente);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Bitacora_Incidente(
            String noReporte,
            String fechaCreacion,
            String fechaAtencion,
            String observaciones,
            String accion,
            String FK_ID_Incidente,
            String status,
            String FK_ID_Bien,
            String prioridad) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO BITACORA_INCIDENTE ("
                    + " noReporte"
                    + " ,fechaCreacion"
                    + " ,fechaAtencion"
                    + " ,observaciones"
                    + " ,accion"
                    + " ,FK_ID_Incidente"
                    + " ,status"
                    + " ,FK_ID_Bien"
                    + " ,prioridad"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, noReporte);
            pstmt.setString(2, fechaCreacion);
            pstmt.setString(3, fechaAtencion);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, accion);
            pstmt.setString(6, FK_ID_Incidente);
            pstmt.setString(7, status);
            pstmt.setString(8, FK_ID_Bien);
            pstmt.setString(9, prioridad);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Transpaso_Bien(
            String idBien,
            String idNuevoMovimiento,
            String estatusTraspaso,
            String FK_ID_Plantel,
            String FK_ID_Personal_Recibe,
            String observaciones,
            String fechaRecepcion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO TRASPASO_BIEN ("
                    + " FK_ID_Bien"
                    + " ,FK_ID_Traspaso"
                    + " ,status"
                    + " ,FK_ID_Plantel_Destino"
                    + " ,FK_ID_Personal_Recibe"
                    + " ,observaciones"
                    + " ,fechaRecepcion"
                    + ")"
                    + " VALUES ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";
            System.out.println("fecha QUID" + SQLSentence);
            System.out.println("idBien" + idBien);
            System.out.println("idNuevoMovimiento" + idNuevoMovimiento);
            System.out.println("status" + estatusTraspaso);
            System.out.println("FK_ID_Plantel" + FK_ID_Plantel);
            System.out.println("FK_ID_Personal_Recibe" + FK_ID_Personal_Recibe);
            System.out.println("observaciones" + observaciones);
            System.out.println("fechaRecepcion" + fechaRecepcion);
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = (OraclePreparedStatement) conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, idBien);
            pstmt.setString(2, idNuevoMovimiento);
            pstmt.setString(3, estatusTraspaso);
            pstmt.setString(4, FK_ID_Plantel);
            pstmt.setString(5, FK_ID_Personal_Recibe);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, fechaRecepcion);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_Resguardo(
            String FK_ID_Bien,
            String observaciones,
            String fechaAsignacion,
            String fechaCambioSituacion,
            String status,
            String noTarjetaResguardo,
            String FK_ID_Personal_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO RESGUARDO ("
                    + " FK_ID_Bien"
                    + " ,observaciones"
                    + " ,fechaAsignacion"
                    + " ,fechaCambioSituacion"
                    + " ,status"
                    + " ,noTarjetaResguardo"
                    + " ,FK_ID_Personal_Plantel"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, observaciones);
            pstmt.setString(3, fechaAsignacion);
            pstmt.setString(4, fechaCambioSituacion);
            pstmt.setString(5, status);
            pstmt.setString(6, noTarjetaResguardo);
            pstmt.setString(7, FK_ID_Personal_Plantel);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se creo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter insert_TipoArchivo(String nombreTipo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = null;
        try {
            SQLSentence = ""
                    + "INSERT INTO TIPO_ARCHIVO ( "
                    + " nombreTipo"
                    + ") VALUES ("
                    + " ?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreTipo);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Transporter insert_Medida(String medida) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = null;
        try {
            SQLSentence = ""
                    + "INSERT INTO MEDIDA ( "
                    + " medida"
                    + ") VALUES ("
                    + " ?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, medida);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            endConnection(jscp, conn, pstmt, rs);
            tport = new Transporter(0, id_inserted);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt, rs);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Long insert_Movimiento(
            String FK_ID_Plantel,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatus,
            String observaciones,
            String iva,
            String FK_ID_Tipo_Movimiento,
            String FK_ID_Usuario,
            String motivoMovimiento) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO MOVIMIENTO ("
                    + " FK_ID_Plantel"
                    + " ,folio"
                    + " ,fechaMovimiento"
                    + " ,fechaActualizacion"
                    + " ,noFactura"
                    + " ,noReferencia"
                    + " ,estatus"
                    + " ,observaciones"
                    + " ,iva"
                    + " ,FK_ID_Tipo_Movimiento"
                    + " ,FK_ID_Usuario"
                    + " ,motivoMovimiento"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatus);
            pstmt.setString(8, observaciones);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_Tipo_Movimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final Long insert_Consumible(
            String clave,
            String descripcion,
            String FK_ID_Plantel,
            String total,
            String FK_ID_Medida,
            String FK_ID_Subcategoria,
            String precioActual,
            String noReferencia,
            String fechaAlta,
            String fechaActualizacion,
            String estatus,
            String observaciones) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO CONSUMIBLE ("
                    + " clave"
                    + " , descripcion"
                    + " , FK_ID_Plantel"
                    + " , total"
                    + " , FK_ID_Medida"
                    + " , FK_ID_Subcategoria"
                    + " , precioActual"
                    + " , noReferencia"
                    + " , fechaAlta"
                    + " , fechaActualizacion"
                    + " , estatus"
                    + " , observaciones"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, clave);
            pstmt.setString(2, descripcion);
            pstmt.setString(3, FK_ID_Plantel);
            pstmt.setString(4, total);
            pstmt.setString(5, FK_ID_Medida);
            pstmt.setString(6, FK_ID_Subcategoria);
            pstmt.setString(7, precioActual);
            pstmt.setString(8, noReferencia);
            pstmt.setString(9, fechaAlta);
            pstmt.setString(10, fechaActualizacion);
            pstmt.setString(11, estatus);
            pstmt.setString(12, observaciones);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_ConteoFisico(
            String fechaRegistro,
            String estatus,
            String FK_ID_Plantel,
            String fechaModificacion,
            String observaciones) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO CONTEOFISICO ("
                    + " fechaRegistro"
                    + " , estatus"
                    + " , FK_ID_Plantel"
                    + " , fechaModificacion"
                    + " , observaciones"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, fechaRegistro);
            pstmt.setString(2, estatus);
            pstmt.setString(3, FK_ID_Plantel);
            pstmt.setString(4, fechaModificacion);
            pstmt.setString(5, observaciones);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final long insert_ConteoFisicoConsumible(
            String FK_ID_ConteoFisico,
            String FK_ID_Consumible,
            String conteoFisico,
            String conteoLogico,
            String fechaTomaLectura,
            String precioHistorico) {
        long idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO CONTEOFISICO_CONSUMIBLE ("
                    + " FK_ID_ConteoFisico"
                    + " , FK_ID_Consumible"
                    + " , conteoFisico"
                    + " , conteoLogico"
                    + " , fechaTomaLectura"
                    + " , precioHistorico"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_ConteoFisico);
            pstmt.setString(2, FK_ID_Consumible);
            pstmt.setString(3, conteoFisico);
            pstmt.setString(4, conteoLogico);
            pstmt.setString(5, fechaTomaLectura);
            pstmt.setString(6, precioHistorico);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_TipoActividad(String tipoActividad) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO TIPO_ACTIVIDAD ("
                    + " tipoActividad"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipoActividad);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_SolicitudPlantel(
            String numeroOficio,
            String nombreSolicitante,
            String fechaSolicitud,
            String nombreResponsable,
            String status,
            String observaciones,
            String FK_ID_Plantel,
            String asunto,
            String solicitud,
            String justificacion,
            String fechaModificacion,
            String FK_ID_UsuarioCreo,
            String FK_ID_UsuarioModifico,
            String FK_ID_UsuarioCerro,
            String horaSolicitud,
            String fechaCreacion,
            String tipoSolicitud) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO SOLICITUD ("
                    + " numeroOficio"
                    + " , nombreSolicitante"
                    + " , fechaSolicitud"
                    + " , nombreResponsable"
                    + " , status"
                    + " , observaciones"
                    + " , FK_ID_Plantel"
                    + " , asunto"
                    + " , solicitud"
                    + " , justificacion"
                    + " , fechaModificacion"
                    + " , FK_ID_UsuarioCreo"
                    + " , FK_ID_UsuarioModifico"
                    + " , FK_ID_UsuarioCerro"
                    + " , horaSolicitud"
                    + " , fechaCreacion"
                    + " , tipoSolicitud"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, numeroOficio);
            pstmt.setString(2, nombreSolicitante);
            pstmt.setString(3, fechaSolicitud);
            pstmt.setString(4, nombreResponsable);
            pstmt.setString(5, status);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, FK_ID_Plantel);
            pstmt.setString(8, asunto);
            pstmt.setString(9, solicitud);
            pstmt.setString(10, justificacion);
            pstmt.setString(11, fechaModificacion);
            pstmt.setString(12, FK_ID_UsuarioCreo);
            pstmt.setString(13, FK_ID_UsuarioModifico);
            pstmt.setString(14, FK_ID_UsuarioCerro);
            pstmt.setString(15, horaSolicitud);
            pstmt.setString(16, fechaCreacion);
            pstmt.setString(17, tipoSolicitud);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_Enlace(
            String tipo,
            String velocidadSubida,
            String velocidadBajada,
            String noAlumnosConectados,
            String noDocentesConectados,
            String noAdministrativosConectados,
            String noDispositivosConectados,
            String noNodos,
            String calidadServicio,
            String FK_ID_Proveedor,
            String FK_ID_Plantel,
            String fechaModificacion) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO ENLACE ("
                    + " tipo"
                    + " , velocidadSubida"
                    + " , velocidadBajada"
                    + " , noAlumnosConectados"
                    + " , noDocentesConectados"
                    + " , noAdministrativosConectados"
                    + " , noDispositivosConectados"
                    + " , noNodos"
                    + " , calidadServicio"
                    + " , FK_ID_Proveedor"
                    + " , FK_ID_Plantel"
                    + " , fechaModificacion"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipo);
            pstmt.setString(2, velocidadSubida);
            pstmt.setString(3, velocidadBajada);
            pstmt.setString(4, noAlumnosConectados);
            pstmt.setString(5, noDocentesConectados);
            pstmt.setString(6, noAdministrativosConectados);
            pstmt.setString(7, noDispositivosConectados);
            pstmt.setString(8, noNodos);
            pstmt.setString(9, calidadServicio);
            pstmt.setString(10, FK_ID_Proveedor);
            pstmt.setString(11, FK_ID_Plantel);
            pstmt.setString(12, fechaModificacion);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_Rubro(String rubro) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO RUBRO ("
                    + " rubro"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, rubro);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_PlabtelAlmacen(String FK_ID_PlantelSolicita, String FK_ID_PlantelSurte, String estatus) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO PLANTEL_ALMACEN ("
                    + " FK_ID_PlantelSolicita"
                    + " , FK_ID_PlantelSurte"
                    + " , estatus"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_PlantelSolicita);
            pstmt.setString(2, FK_ID_PlantelSurte);
            pstmt.setString(3, estatus);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    public final int insert_Patron_Serie(String patron, String fechaActualizacion, String longitudNoSerie, String tipoPatron, String coeficientePresicion, String usuarioActualizo, String FK_ID_Modelo) {
        int idInserted = -1;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO PATRON_SERIE ("
                    + " patron"
                    + " , fechaActualizacion"
                    + " , longitudNoSerie"
                    + " , tipoPatron"
                    + " , coeficientePresicion"
                    + " , usuarioActualizo"
                    + " , FK_ID_Modelo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, patron);
            pstmt.setString(2, fechaActualizacion);
            pstmt.setString(3, longitudNoSerie);
            pstmt.setString(4, tipoPatron);
            pstmt.setString(5, coeficientePresicion);
            pstmt.setString(6, usuarioActualizo);
            pstmt.setString(7, FK_ID_Modelo);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getInt(1);
            }
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idInserted;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Update">    
    public final Transporter update_Proveedor(
            String nombreProveedor,
            String claveProveedor,
            String telefono,
            String correo,
            String calle,
            String localidad,
            String colonia,
            String codigoPostal,
            String noExterior,
            String ID_Proveedor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PROVEEDOR SET "
                    + " nombreProveedor = ? "
                    + " ,claveProveedor = ? "
                    + " ,telefono = ? "
                    + " ,correo = ? "
                    + " ,calle = ? "
                    + " ,localidad = ? "
                    + " ,colonia = ? "
                    + " ,codigoPostal = ? "
                    + " ,noExterior = ? "
                    + " WHERE ID_Proveedor = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreProveedor);
            pstmt.setString(2, claveProveedor);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, calle);
            pstmt.setString(6, localidad);
            pstmt.setString(7, colonia);
            pstmt.setString(8, codigoPostal);
            pstmt.setString(9, noExterior);
            pstmt.setString(10, ID_Proveedor);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_TipoProveedor(String nombreTipo, String ID_Tipo_Proveedor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_PROVEEDOR SET "
                    + " nombreTipo = ? "
                    + " WHERE ID_Tipo_Proveedor = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreTipo);
            pstmt.setString(2, ID_Tipo_Proveedor);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Detalle(String nombreDetalle, String ID_Detalle) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE DETALLE SET "
                    + " nombreDetalle = ? "
                    + " WHERE ID_Detalle = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreDetalle);
            pstmt.setString(2, ID_Detalle);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Plantel(
            String nombre,
            String direccion,
            String claveCentroTrabajo,
            String telefono,
            String correo,
            int ID_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PLANTEL "
                    + " SET "
                    + "  nombre=?"
                    + " , direccion=?"
                    + " , claveCentroTrabajo=?"
                    + " , telefono=?"
                    + " , correo=?"
                    + " WHERE"
                    + " ID_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, claveCentroTrabajo);
            pstmt.setString(4, telefono);
            pstmt.setString(5, correo);
            pstmt.setInt(6, ID_Plantel);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            if (ex.toString().equalsIgnoreCase("duplicate")) {
                tport = new Transporter(1, "El registro ya existe.");
            } else {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Lo sentimos error inesperado de base de datos :(");
            }
        }
        return tport;
    }

    public final Transporter update_Modelo(
            String modelo,
            int FK_ID_Marca,
            String descripcion,
            String ID_Modelo) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE MODELO "
                    + " SET"
                    + "  modelo = ?"
                    + " , FK_ID_Marca = ?"
                    + " , descripcion = ?"
                    + " WHERE"
                    + " ID_Modelo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, modelo);
            pstmt.setInt(2, FK_ID_Marca);
            pstmt.setString(3, descripcion);
            pstmt.setString(4, ID_Modelo);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;

    }

    public final Transporter update_Marca(
            String marca,
            int ID_Marca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE MARCA SET "
                    + " marca = ? "
                    + " WHERE ID_Marca = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, marca);
            pstmt.setInt(2, ID_Marca);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Tipo_Software(
            String tipo,
            int ID_Tipo_Software) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_SOFTWARE SET "
                    + " tipo = ? "
                    + " WHERE ID_Tipo_Software = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipo);
            pstmt.setInt(2, ID_Tipo_Software);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Licencia(
            String licencia,
            int ID_Licencia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE LICENCIA SET "
                    + " licencia = ? "
                    + " WHERE ID_Licencia = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, licencia);
            pstmt.setInt(2, ID_Licencia);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Tipo_Compra(
            String compra,
            String ID_Tipo_compra) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_COMPRA SET "
                    + " compra = ? "
                    + " WHERE ID_Tipo_Compra = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, compra);
            pstmt.setString(2, ID_Tipo_compra);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Tipo_Garantia(
            String garantia,
            String ID_Tipo_Garantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_GARANTIA SET"
                    + " garantia = ?"
                    + " WHERE ID_Tipo_Garantia = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, garantia);
            pstmt.setString(2, ID_Tipo_Garantia);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Usuario(
            String usuario,
            String password,
            String correo,
            String nombreCompleto,
            short status,
            String FK_ID_Plantel,
            String ID_Usuario) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE USUARIO"
                    + " SET usuario = ?"
                    + " ,password=?"
                    + " ,correo = ?"
                    + " ,nombreCompleto = ?"
                    + " ,status = ?"
                    + " ,FK_ID_Plantel=?"
                    + " WHERE"
                    + " ID_Usuario=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, usuario);
            pstmt.setString(2, JHash.getStringMessageDigest(password, JHash.MD5));
            pstmt.setString(3, correo);
            pstmt.setString(4, nombreCompleto);
            pstmt.setShort(5, status);
            pstmt.setString(6, FK_ID_Plantel);
            pstmt.setString(7, ID_Usuario);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter update_PasswordUsuario(String password, String ID_Usuario) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE USUARIO"
                    + " SET"
                    + " password=?"
                    + " WHERE"
                    + " ID_Usuario=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, JHash.getStringMessageDigest(password, JHash.MD5));
            pstmt.setString(2, ID_Usuario);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter update_PasswordPersonal(String password, String ID_Personal) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE PERSONAL"
                    + " SET"
                    + " password=?"
                    + " WHERE"
                    + " ID_PERSONAL=?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, JHash.getStringMessageDigest(password, JHash.MD5));
            pstmt.setString(2, ID_Personal);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_UsuarioIgnorePassword(
            String usuario,
            String correo,
            String nombreCompleto,
            short status,
            String FK_ID_Plantel,
            String ID_Usuario) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE USUARIO"
                    + " SET usuario = ?"
                    + " ,correo = ?"
                    + " ,nombreCompleto = ?"
                    + " ,status = ?"
                    + " ,FK_ID_Plantel=?"
                    + " WHERE"
                    + " ID_Usuario=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, usuario);
            pstmt.setString(2, correo);
            pstmt.setString(3, nombreCompleto);
            pstmt.setShort(4, status);
            pstmt.setString(5, FK_ID_Plantel);
            pstmt.setString(6, ID_Usuario);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter update_RolUsuario(String FK_ID_Rol, String ID_Rol_Usuario) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE ROL_USUARIO"
                    + " SET FK_ID_Rol = ?"
                    + " WHERE"
                    + " ID_Rol_Usuario = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, FK_ID_Rol);
            pstmt.setString(2, ID_Rol_Usuario);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter update_RolInfo(
            String ID_Rol,
            String rolUsuario,
            String descripcion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE ROL "
                    + " SET "
                    + " nombreRol = ? "
                    + " ,descripcion = ? "
                    + " WHERE ID_Rol = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, rolUsuario);
            pstmt.setString(2, descripcion);
            pstmt.setInt(3, Integer.parseInt(ID_Rol));

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha actualizado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter update_PersonalPlantelXCampo(
            String campo,
            String valor,
            String ID_Personal_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERSONAL_PLANTEL SET "
                    + campo + " = '" + valor + "'"
                    + " WHERE ID_Personal_Plantel = ? "
                    + ";";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Personal_Plantel);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Transporter update_SituacionPersonalPlantel(
            String situacionActual,
            String FK_ID_Plantel,
            String FK_ID_Personal) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERSONAL_PLANTEL SET "
                    + " estatus= ?"
                    + " WHERE FK_ID_Personal = ? "
                    + " AND FK_ID_Plantel=?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, situacionActual);
            pstmt.setString(2, FK_ID_Personal);
            pstmt.setString(3, FK_ID_Plantel);
            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Transporter update_Permiso(
            String permiso,
            int ID_Permiso) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERMISO SET "
                    + " nombrePermiso = ? "
                    + " WHERE ID_Permiso = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, permiso);
            pstmt.setInt(2, ID_Permiso);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_PermisoXTipo(
            String permiso,
            int ID_Permiso) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERMISO SET "
                    + " tipoPermiso = ? "
                    + " WHERE ID_Permiso = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, permiso);
            pstmt.setInt(2, ID_Permiso);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_PermisoXDescripcion(
            String permiso,
            int ID_Permiso) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERMISO SET "
                    + " descripcion = ? "
                    + " WHERE ID_Permiso = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, permiso);
            pstmt.setInt(2, ID_Permiso);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_PersonalPlantel(
            String cargo,
            String situacionActual,
            String fechaCargo,
            String ID_Personal_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERSONAL_PLANTEL SET "
                    + " estatus = ? "
                    + " ,cargo = ? "
                    + " ,fechaAsignacionCargo = ?"
                    + "  WHERE ID_Personal_Plantel = ? ";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, situacionActual);
            pstmt.setString(2, cargo);
            pstmt.setString(3, fechaCargo);
            pstmt.setString(4, ID_Personal_Plantel);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);

            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Transporter update_Personal(
            String nombre,
            String aPaterno,
            String aMaterno,
            String telefono,
            String titulo,
            String correo,
            String siglasTitulo,
            int ID_Personal) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PERSONAL SET "
                    + " nombre = ? "
                    + " ,aPaterno = ? "
                    + " ,aMaterno = ? "
                    + " ,telefono = ? "
                    + " ,titulo = ? "
                    + " ,correo = ? "
                    + " ,siglasTitulo = ? "
                    + " WHERE ID_Personal = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setString(2, aPaterno);
            pstmt.setString(3, aMaterno);
            pstmt.setString(4, telefono);
            pstmt.setString(5, titulo);
            pstmt.setString(6, correo);
            pstmt.setString(7, siglasTitulo);
            pstmt.setInt(8, ID_Personal);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Categoria(
            String nombreCategoria,
            int ID_Categoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE CATEGORIA SET "
                    + " nombreCategoria = ? "
                    + " WHERE ID_Categoria = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreCategoria);
            pstmt.setInt(2, ID_Categoria);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Grupo(String nombreGrupo, String ID_Grupo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE GRUPO SET "
                    + " nombreGrupo = ? "
                    + " WHERE ID_Grupo = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreGrupo);
            pstmt.setString(2, ID_Grupo);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_RelacionPdg(String FK_ID_Grupo, String FK_ID_Departamento_Plantel, String ID_RelacionPDG) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE RELACION_PDG SET "
                    + " FK_ID_Grupo = ? "
                    + " , FK_ID_Departamento_Plantel = ? "
                    + " WHERE ID_PDG = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Grupo);
            pstmt.setString(2, FK_ID_Departamento_Plantel);
            pstmt.setString(3, ID_RelacionPDG);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_SubCategoria(
            String FK_ID_Categoria,
            String nombreSubCategoria,
            String ID_SubCategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE SUBCATEGORIA SET "
                    + " FK_ID_Categoria = ? "
                    + " ,nombreSubCategoria = ?"
                    + " WHERE ID_SubCategoria = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Categoria);
            pstmt.setString(2, nombreSubCategoria);
            pstmt.setString(3, ID_SubCategoria);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Departamento(
            String nombreDepartamento,
            int ID_Departamento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE DEPARTAMENTO SET "
                    + " nombreDepartamento = ? "
                    + " WHERE ID_Departamento = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreDepartamento);
            pstmt.setInt(2, ID_Departamento);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_NombreSoftware(
            String nombre,
            int ID_Nombre_Software) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE NOMBRE_SOFTWARE SET "
                    + " nombre = ? "
                    + " WHERE ID_Nombre_Software = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombre);
            pstmt.setInt(2, ID_Nombre_Software);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Bien(
            String FK_ID_Subcategoria,
            String FK_ID_Modelo,
            String descripcionbien,
            String noserie,
            String nodictamen,
            String nofactura,
            String noinventario,
            String FK_ID_TipoCompra,
            String fechaCompra,
            String FK_ID_Plantel,
            String FK_ID_DepartamentoPlantel,
            String fechaAlta,
            String status,
            String observaciones,
            String archivo,
            String ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE BIEN SET"
                    + " FK_ID_Subcategoria = ?"
                    + " ,FK_ID_Modelo = ?"
                    + " ,descripcionBien = ?"
                    + " ,noSerie = ?"
                    + " ,noDictamen = ?"
                    + " ,noFactura = ?"
                    + " ,noInventario = ?"
                    + " ,FK_ID_Tipo_Compra = ?"
                    + " ,fechaCompra = ?"
                    + " ,FK_ID_Plantel = ?"
                    + " ,FK_ID_Departamento_Plantel = ?"
                    + " ,fechaAlta = ?"
                    + " ,status = ?"
                    + " ,observaciones = ?"
                    + " ,archivo = ?"
                    + " WHERE ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Subcategoria);
            pstmt.setString(2, FK_ID_Modelo);
            pstmt.setString(3, descripcionbien);
            pstmt.setString(4, noserie);
            pstmt.setString(5, nodictamen);
            pstmt.setString(6, nofactura);
            pstmt.setString(7, noinventario);
            pstmt.setString(8, FK_ID_TipoCompra);
            pstmt.setString(9, fechaCompra);
            pstmt.setString(10, FK_ID_Plantel);
            pstmt.setString(11, FK_ID_DepartamentoPlantel);
            pstmt.setString(12, fechaAlta);
            pstmt.setString(13, status);
            pstmt.setString(14, observaciones);
            pstmt.setString(15, archivo);
            pstmt.setString(16, ID_Bien);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. Verifique log. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_GrupoBien(String FK_ID_Bien, String FK_ID_Grupo, String ID_Grupo_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE GRUPO_BIEN SET "
                    + " FK_ID_Grupo = ? "
                    + " ,FK_ID_Bien = ? "
                    + " WHERE ID_Grupo_Bien = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Grupo);
            pstmt.setString(2, FK_ID_Bien);
            pstmt.setString(3, ID_Grupo_Bien);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Garantia(String FK_ID_Bien, String fechaInicio, String fechaFin, String FK_ID_TipoGarantia, String ID_Garantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE GARANTIA SET "
                    + " FK_ID_Bien = ? "
                    + " ,fechaInicio = ? "
                    + " ,fechaFin = ? "
                    + " ,FK_ID_Tipo_Garantia = ? "
                    + " WHERE ID_Garantia = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechaFin);
            pstmt.setString(4, FK_ID_TipoGarantia);
            pstmt.setString(5, ID_Garantia);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Status4Bien(String status, String ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE BIEN SET "
                    + " status = ? "
                    + " WHERE ID_Bien = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, status);
            pstmt.setString(2, ID_Bien);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Solicitud(
            String numeroOficio,
            String nombreSolicitante,
            String fechaSolicitud,
            String nombreResponsable,
            String status,
            String observaciones,
            String FK_ID_Plantel,
            String ID_Solicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE SOLICITUD SET "
                    + " numeroOficio = ? "
                    + " ,nombreSolicitante = ? "
                    + " ,fechaSolicitud = ? "
                    + " ,nombreResponsable = ? "
                    + " ,status = ? "
                    + " ,observaciones = ? "
                    + " ,FK_ID_Plantel = ? "
                    + " WHERE ID_Solicitud = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, numeroOficio);
            pstmt.setString(2, nombreSolicitante);
            pstmt.setString(3, fechaSolicitud);
            pstmt.setString(4, nombreResponsable);
            pstmt.setString(5, status);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, FK_ID_Plantel);
            pstmt.setString(8, ID_Solicitud);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_SoftwareFile(String nombreArchivo, String ID_Software) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE SOFTWARE SET "
                    + " nombreArchivo = ? "
                    + " WHERE ID_Software = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreArchivo);
            pstmt.setString(2, ID_Software);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Bitacora_Incidente(
            String noReporte,
            String fechaCreacion,
            String fechaAtencion,
            String observaciones,
            String accion,
            String FK_ID_Incidente,
            String status,
            String FK_ID_Bien,
            String prioridad,
            String ID_Bitacora_Incidente) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE BITACORA_INCIDENTE SET"
                    + " noReporte = ?"
                    + " ,fechaCreacion = ?"
                    + " ,fechaAtencion = ?"
                    + " ,observaciones = ?"
                    + " ,accion = ?"
                    + " ,FK_ID_Incidente = ?"
                    + " ,status = ?"
                    + " ,FK_ID_Bien = ?"
                    + " ,prioridad = ?"
                    + " WHERE "
                    + " ID_Bitacora_Incidente = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, noReporte);
            pstmt.setString(2, fechaCreacion);
            pstmt.setString(3, fechaAtencion);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, accion);
            pstmt.setString(6, FK_ID_Incidente);
            pstmt.setString(7, status);
            pstmt.setString(8, FK_ID_Bien);
            pstmt.setString(9, prioridad);
            pstmt.setString(10, ID_Bitacora_Incidente);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_StatusResguardo(String status, String fechaCambioSituacion, String ID_Resguardo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE RESGUARDO SET "
                    + " status = ? "
                    + " , fechaCambioSituacion = ?"
                    + " WHERE ID_Resguardo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, status);
            pstmt.setString(2, status);
            pstmt.setString(3, ID_Resguardo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado.");
        }
        return tport;
    }

    public final Transporter update_Consumible(
            String clave,
            String descripcion,
            String FK_ID_Plantel,
            String total,
            String FK_ID_Medida,
            String FK_ID_Subcategoria,
            String precioActual,
            String noReferencia,
            String fechaActualizacion,
            String estatus,
            String observaciones,
            String ID_Consumible) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Transporter tport = null;
        try {
            SQLSentence = ""
                    + " UPDATE CONSUMIBLE SET"
                    + " clave = ?"
                    + " , descripcion = ?"
                    + " , FK_ID_Plantel = ?"
                    + " , total = ?"
                    + " , FK_ID_Medida = ?"
                    + " , FK_ID_Subcategoria = ?"
                    + " , precioActual = ?"
                    + " , noReferencia = ?"
                    + " , fechaActualizacion = ?"
                    + " , estatus = ?"
                    + " , observaciones = ?"
                    + " WHERE "
                    + " ID_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, clave);
            pstmt.setString(2, descripcion);
            pstmt.setString(3, FK_ID_Plantel);
            pstmt.setString(4, total);
            pstmt.setString(5, FK_ID_Medida);
            pstmt.setString(6, FK_ID_Subcategoria);
            pstmt.setString(7, precioActual);
            pstmt.setString(8, noReferencia);
            pstmt.setString(9, fechaActualizacion);
            pstmt.setString(10, estatus);
            pstmt.setString(11, observaciones);
            pstmt.setString(12, ID_Consumible);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Los datos se actualizarón correctamente.");
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al actualizar los datos.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter update_EstatusConsumible(String estatus, String ID_Consumible) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Transporter tport = null;
        try {
            SQLSentence = ""
                    + " UPDATE CONSUMIBLE SET"
                    + "  estatus = ?"
                    + " WHERE "
                    + " ID_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, estatus);
            pstmt.setString(2, ID_Consumible);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Los datos se actualizarón correctamente.");
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al actualizar los datos.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter update_ConteoFisico_Consumible(
            String FK_ID_ConteoFisico,
            String FK_ID_Consumible,
            String conteoFisico,
            String conteoLogico,
            String fechaTomaLectura,
            String precioHistorico,
            String ID_ConteoFisico_Cosumible) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE CONTEOFISICO_CONSUMIBLE SET"
                    + " FK_ID_ConteoFisico = ?"
                    + " ,FK_ID_Consumible = ?"
                    + " ,conteoFisico = ?"
                    + " ,conteoLogico = ?"
                    + " ,fechaTomaLectura = ?"
                    + " ,precioHistorico = ?"
                    + " WHERE "
                    + " ID_ConteoFisico_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_ConteoFisico);
            pstmt.setString(2, FK_ID_Consumible);
            pstmt.setString(3, conteoFisico);
            pstmt.setString(4, conteoLogico);
            pstmt.setString(5, fechaTomaLectura);
            pstmt.setString(6, precioHistorico);
            pstmt.setString(7, ID_ConteoFisico_Cosumible);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_fechaModificacioConteoFisico(String fechaModificacion, String ID_ConteoFisico) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Transporter tport = null;
        try {
            SQLSentence = ""
                    + " UPDATE CONTEOFISICO SET"
                    + "  fechaModificacion = ?"
                    + " WHERE "
                    + " ID_ConteoFisico = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, fechaModificacion);
            pstmt.setString(2, ID_ConteoFisico);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Los datos se actualizarón correctamente.");
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al actualizar los datos.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter update_TipoActividad(String tipoActividad, String ID_TipoActividad) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_ACTIVIDAD SET "
                    + " tipoActividad = ? "
                    + " WHERE ID_TipoActividad = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipoActividad);
            pstmt.setString(2, ID_TipoActividad);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_ActividadPlantel(
            String estatus,
            String fechaCambioEstatus,
            String observaciones,
            String horaCambioEstatus,
            String FK_ID_UsuarioActualizo,
            String porcentajeCompleto,
            String ID_Actividad_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE ACTIVIDAD_PLANTEL SET"
                    + " estatus = ?"
                    + " , fechaCambioEstatus = ?"
                    + " , observaciones = ?"
                    + " , horaCambioEstatus = ?"
                    + " , FK_ID_UsuarioActualizo = ?"
                    + " , porcentajeCompleto = ?"
                    + " WHERE "
                    + " ID_Actividad_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, estatus);
            pstmt.setString(2, fechaCambioEstatus);
            pstmt.setString(3, observaciones);
            pstmt.setString(4, horaCambioEstatus);
            pstmt.setString(5, FK_ID_UsuarioActualizo);
            pstmt.setString(6, porcentajeCompleto);
            pstmt.setString(7, ID_Actividad_Plantel);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Actividad(
            String descripcion,
            String fechaInicio,
            String fechaFin,
            String ultimaActualizacion,
            String horaInicio,
            String horaFin,
            String horaUltimaActualizacion,
            String FK_ID_UsuarioActualizo,
            String FK_ID_TipoActividad,
            String fechaLimite,
            String horaLimite,
            String ID_Actividad
    ) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE ACTIVIDAD SET"
                    + " descripcion = ?"
                    + " , fechaInicio = ?"
                    + " , fechaFin = ?"
                    + " , ultimaActualizacion = ?"
                    + " , horaInicio = ?"
                    + " , horaFin = ?"
                    + " , horaUltimaActualizacion = ?"
                    + " , FK_ID_UsuarioActualizo = ?"
                    + " , FK_ID_TipoActividad = ?"
                    + " , fechaLimite = ?"
                    + " , horaLimite = ?"
                    + " WHERE "
                    + " ID_Actividad = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, descripcion);
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechaFin);
            pstmt.setString(4, ultimaActualizacion);
            pstmt.setString(5, horaInicio);
            pstmt.setString(6, horaFin);
            pstmt.setString(7, horaUltimaActualizacion);
            pstmt.setString(8, FK_ID_UsuarioActualizo);
            pstmt.setString(9, FK_ID_TipoActividad);
            pstmt.setString(10, fechaLimite);
            pstmt.setString(11, horaLimite);
            pstmt.setString(12, ID_Actividad);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_EstatusSolicitudPlantel(String estatus, String ID_Solicitud) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Transporter tport = null;
        try {
            SQLSentence = ""
                    + " UPDATE SOLICITUD SET"
                    + "  status = ?"
                    + " WHERE "
                    + " ID_Solicitud = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, estatus);
            pstmt.setString(2, ID_Solicitud);
            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Los datos se actualizarón correctamente.");
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al actualizar los datos.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter update_SolicitudPlantel(
            String numeroOficio,
            String nombreSolicitante,
            String fechaSolicitud,
            String nombreResponsable,
            String status,
            String observaciones,
            String FK_ID_Plantel,
            String asunto,
            String solicitud,
            String justificacion,
            String fechaModificacion,
            String FK_ID_UsuarioModifico,
            String FK_ID_UsuarioCerro,
            String ID_Solicitud
    ) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE SOLICITUD SET"
                    + " numeroOficio = ?"
                    + " , nombreSolicitante = ?"
                    + " , fechaSolicitud = ?"
                    + " , nombreResponsable = ?"
                    + " , status = ?"
                    + " , observaciones = ?"
                    + " , FK_ID_Plantel = ?"
                    + " , asunto = ?"
                    + " , solicitud = ?"
                    + " , justificacion = ?"
                    + " , fechaModificacion = ?"
                    + " , FK_ID_UsuarioModifico = ?"
                    + " , FK_ID_UsuarioCerro = ?"
                    + " WHERE "
                    + " ID_Solicitud = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, numeroOficio);
            pstmt.setString(2, nombreSolicitante);
            pstmt.setString(3, fechaSolicitud);
            pstmt.setString(4, nombreResponsable);
            pstmt.setString(5, status);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, FK_ID_Plantel);
            pstmt.setString(8, asunto);
            pstmt.setString(9, solicitud);
            pstmt.setString(10, justificacion);
            pstmt.setString(11, fechaModificacion);
            pstmt.setString(12, FK_ID_UsuarioModifico);
            pstmt.setString(13, FK_ID_UsuarioCerro);
            pstmt.setString(14, ID_Solicitud);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Enlace(
            String tipo,
            String velocidadSubida,
            String velocidadBajada,
            String noAlumnosConectados,
            String noDocentesConectados,
            String noAdministrativosConectados,
            String noDispositivosConectados,
            String noNodos,
            String calidadServicio,
            String FK_ID_Proveedor,
            String FK_ID_Plantel,
            String fechaModificacion,
            String ID_Enlace
    ) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE ENLACE SET"
                    + "  tipo = ?"
                    + " , velocidadSubida = ?"
                    + " , velocidadBajada = ?"
                    + " , noAlumnosConectados = ?"
                    + " , noDocentesConectados = ?"
                    + " , noAdministrativosConectados = ?"
                    + " , noDispositivosConectados = ?"
                    + " , noNodos = ?"
                    + " , calidadServicio = ?"
                    + " , FK_ID_Proveedor = ?"
                    + " , FK_ID_Plantel = ?"
                    + " , fechaModificacion = ?"
                    + " WHERE "
                    + " ID_Enlace = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, tipo);
            pstmt.setString(2, velocidadSubida);
            pstmt.setString(3, velocidadBajada);
            pstmt.setString(4, noAlumnosConectados);
            pstmt.setString(5, noDocentesConectados);
            pstmt.setString(6, noAdministrativosConectados);
            pstmt.setString(7, noDispositivosConectados);
            pstmt.setString(8, noNodos);
            pstmt.setString(9, calidadServicio);
            pstmt.setString(10, FK_ID_Proveedor);
            pstmt.setString(11, FK_ID_Plantel);
            pstmt.setString(12, fechaModificacion);
            pstmt.setString(13, ID_Enlace);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Archivo(
            String nombreArchivo,
            String descripcion,
            String extension,
            String fechaActualizacion,
            String FK_ID_Tipo_Archivo,
            long tamanio,
            String tipoAcceso,
            String keywords,
            String ID_Archivo
    ) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE ARCHIVO SET"
                    + "  nombreArchivo = ?"
                    + " , descripcion = ?"
                    + " , extension = ?"
                    + " , fechaActualizacion = ?"
                    + " , FK_ID_Tipo_Archivo = ?"
                    + " , tamanio = ?"
                    + " , tipoAcceso = ?"
                    + " , keywords = ?"
                    + " WHERE "
                    + " ID_Archivo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreArchivo);
            pstmt.setString(2, descripcion);
            pstmt.setString(3, extension);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, FK_ID_Tipo_Archivo);
            pstmt.setLong(6, tamanio);
            pstmt.setString(7, tipoAcceso);
            pstmt.setString(8, keywords);
            pstmt.setString(9, ID_Archivo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Rubro(String rubro, String ID_Rubro) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE RUBRO SET "
                    + " rubro = ? "
                    + " WHERE ID_Rubro = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, rubro);
            pstmt.setString(2, ID_Rubro);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_Puntuacion(
            String FK_ID_Plantel,
            String FK_ID_Rubro,
            String puntuacion,
            String observaciones,
            String fechaRegistro,
            String estatus,
            String FK_ID_Usuario,
            String ID_Puntuacion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE PUNTUACION SET"
                    + "  FK_ID_Plantel = ?"
                    + " , FK_ID_Rubro = ?"
                    + " , puntuacion = ?"
                    + " , observaciones = ?"
                    + " , fechaRegistro = ?"
                    + " , estatus = ?"
                    + " , FK_ID_Usuario = ?"
                    + " WHERE "
                    + " ID_Puntuacion = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, FK_ID_Rubro);
            pstmt.setString(3, puntuacion);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, fechaRegistro);
            pstmt.setString(6, estatus);
            pstmt.setString(7, FK_ID_Usuario);
            pstmt.setString(8, ID_Puntuacion);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_PlanteAlmacen(
            String FK_ID_PlantelSolicita,
            String FK_ID_PlantelSurte,
            String estatus,
            String ID_PlantelAlmacen) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE PLANTEL_ALMACEN SET"
                    + "  FK_ID_PlantelSolicita = ?"
                    + " , FK_ID_PlantelSurte = ?"
                    + " , estatus = ?"
                    + " WHERE "
                    + " ID_Plantel_Almacen = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_PlantelSolicita);
            pstmt.setString(2, FK_ID_PlantelSurte);
            pstmt.setString(3, estatus);
            pstmt.setString(4, ID_PlantelAlmacen);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_estatusPlanteAlmacen(
            String estatus,
            String ID_PlantelAlmacen) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " UPDATE PLANTEL_ALMACEN SET"
                    + " , estatus = ?"
                    + " WHERE "
                    + " ID_Plantel_Almacen = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, estatus);
            pstmt.setString(2, ID_PlantelAlmacen);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se actualizo correctamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_PatronSerie(String patron, String ID_Patron_Serie, String fechaActualizacion, String usuarioActualizo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE PATRON_SERIE SET "
                    + " patron = ? "
                    + " ,fechaActualizacion = ? "
                    + " ,usuarioActualizo = ? "
                    + " WHERE ID_Patron_Serie = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, patron);
            pstmt.setString(2, fechaActualizacion);
            pstmt.setString(3, usuarioActualizo);
            pstmt.setString(4, ID_Patron_Serie);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    public final Transporter update_FoliosTipoMovimiento(String ID_TipoMovimiento, String nuevoValor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE TIPO_MOVIMIENTO SET "
                    + " cuentaTotalMovimiento = ? "
                    + " WHERE ID_Tipo_Movimiento = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nuevoValor);
            pstmt.setString(2, ID_TipoMovimiento);

            int rowCount = pstmt.executeUpdate();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
        }
        return tport;
    }

    //</editor-fold>  
    //<editor-fold defaultstate="collapsed" desc="Delete">    
    /**
     * Elimina el registro coincidente con ID_GRUPO en la tabla Grupo, previa
     * validación de relaciones.
     *
     * @param IDGrupo
     * @return Transporter con detalles de la operación
     */
    public final Transporter delete_Proveedor(String ID_Proveedor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM PROVEEDOR"
                    + " WHERE ID_Proveedor = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Proveedor);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_TipoProveedor(String ID_TipoProveedor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM TIPO_PROVEEDOR"
                    + " WHERE ID_Tipo_Proveedor = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoProveedor);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_Detalle(String ID_Detalle) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM DETALLE"
                    + " WHERE ID_Detalle = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Detalle);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_Usuario(String ID_Usuario) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM USUARIO"
                    + " WHERE ID_Usuario = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);

            pstmt.setString(1, ID_Usuario);
            pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Registro borrado exitosamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");

        }
        return tport;
    }

    public final Transporter delete_Marca(int ID_Marca) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsMarcainMarcaModelo(ID_Marca) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM MARCA"
                        + " WHERE ID_Marca = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Marca);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Tipo_Garantia(int ID_Tipo_Garantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsTipoGarantiainTipoGarantiaBien(ID_Tipo_Garantia) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM TIPO_GARANTIA"
                        + " WHERE ID_Tipo_Garantia = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Tipo_Garantia);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Tipo_Software(int ID_Tipo_Software) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsTipoSoftwareinTipoTipoSoftwareSoftware(ID_Tipo_Software) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM TIPO_SOFTWARE"
                        + " WHERE ID_Tipo_Software = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Tipo_Software);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Tipo_Compra(int ID_Tipo_Compra) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsTipoComprainTipoCompraSoftware(ID_Tipo_Compra) == 0
                && this.getRowsTipoComprainTipoCompraBien(ID_Tipo_Compra) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM TIPO_COMPRA"
                        + " WHERE ID_Tipo_Compra = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Tipo_Compra);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Modelo(int ID_Modelo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsModeloinModeloBien(ID_Modelo) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM MODELO"
                        + " WHERE ID_Modelo = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Modelo);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Licencia(int ID_Licencia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsLicenciainLicenciaSoftware(ID_Licencia) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM LICENCIA"
                        + " WHERE ID_Licencia = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Licencia);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_rolPermisos(String ID_Rol) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM ROL_PERMISO "
                    + " WHERE FK_ID_Rol = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Rol));
            pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Registro borrado exitosamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");

        }
        return tport;
    }

    public final Transporter delete_rolUsuario(String ID_Rol) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM ROL "
                    + " WHERE ID_Rol = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(ID_Rol));
            pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Registro borrado exitosamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");

        }
        return tport;
    }

    public final Transporter delete_Personal(int ID_Personal) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        try {
            SQLSentence = ""
                    + " DELETE FROM PERSONAL "
                    + " WHERE ID_Personal = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Personal);

            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado... Verifique log ");
        }
        return tport;
    }

    public final Transporter delete_Plantel(int ID_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsPlantelinPlantelDepartamentoPlantel(ID_Plantel) == 0
                && this.getRowsPlantelinPlantelBien(ID_Plantel) == 0
                && this.getRowsPlantelinPlantelPersonalPlantel(ID_Plantel) == 0
                && this.getRowsPlantelinPlantelSoftwarePlantel(ID_Plantel) == 0
                && this.getRowsPlantelinPlantelTranspasoBien(ID_Plantel) == 0
                && this.getRowsPlantelinPlantelUsuario(ID_Plantel) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM PLANTEL"
                        + " WHERE ID_Plantel = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Plantel);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");

            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Categoria(int ID_Categoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsCategoriainCategoriaSubCategoria(ID_Categoria) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM CATEGORIA"
                        + " WHERE ID_Categoria = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Categoria);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_DeptoPlantel(String FK_ID_Plantel, String FK_ID_Departamento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM DEPARTAMENTO_PLANTEL"
                    + " WHERE FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, Integer.parseInt(FK_ID_Plantel));
            pstmt.setInt(2, Integer.parseInt(FK_ID_Departamento));
            pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Registro borrado exitosamente");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");

        }
        return tport;
    }

    public final Transporter insert_DeptoPlantel(
            int ID_Plantel,
            int ID_Departamento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " INSERT INTO DEPARTAMENTO_PLANTEL"
                    + " (FK_ID_Plantel"
                    + " ,FK_ID_Departamento)"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Plantel);
            pstmt.setInt(2, ID_Departamento);

            int rowCount = pstmt.executeUpdate();

            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se ha creado correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error inesperado de base de datos");
        }
        return tport;
    }

    public final Transporter delete_Grupo(String ID_Grupo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM GRUPO"
                    + " WHERE ID_Grupo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Grupo);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_RelacionPdg(String ID_PDG) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM RELACION_PDG"
                    + " WHERE ID_Pdg = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PDG);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_SubCategoria(int ID_SubCategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsSubCategoriainSubCategoriaDetalleSubCategoria(ID_SubCategoria) == 0
                && getRowsSubCategoriainSubCategoriaBien(ID_SubCategoria) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM SUBCATEGORIA"
                        + " WHERE ID_SubCategoria = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_SubCategoria);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Permiso(int ID_Permiso) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.select_getRowsPermisoinPermisoRol(ID_Permiso) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM PERMISO"
                        + " WHERE ID_Permiso = ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Permiso);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);
                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Departamento(int ID_Departamento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM DEPARTAMENTO"
                    + " WHERE ID_Departamento= ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Departamento);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
             tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
             } else {*/
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
            //}
        }

        return tport;
    }

    public final Transporter delete_DepartamentoPlantel(String ID_Departamento_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsBieninBienDepartamentoPlantel(ID_Departamento_Plantel) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM DEPARTAMENTO_PLANTEL"
                        + " WHERE ID_Departamento_Plantel= ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setString(1, ID_Departamento_Plantel);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado.");
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }
        return tport;
    }

    public final Transporter delete_NombreSoftware(int ID_Nombre_Software) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        if (this.getRowsNombreSoftwareinNombreSoftwareSoftware(ID_Nombre_Software) == 0) {
            try {
                SQLSentence = ""
                        + " DELETE FROM NOMBRE_SOFTWARE"
                        + " WHERE ID_Nombre_Software= ?";

                jscp = JSpreadConnectionPool.getSingleInstance();
                conn = jscp.getConnectionFromPool();
                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setInt(1, ID_Nombre_Software);
                filas_afectadas = pstmt.executeUpdate();

                endConnection(jscp, conn, pstmt);

                tport = new Transporter(0, "");
            } catch (Exception ex) {
                endConnection(jscp, conn, pstmt);
                /* if (ex.toString().contains("The DELETE statement conflicted with the REFERENCE constraint")) {
                 tport = new Transporter(1, "El registro no se puede borrar porque tiene asociaciones.");
                 } else {*/
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error de base de datos inesperado. " + ex.getMessage());
                //}
            }
        } else {
            tport = new Transporter(1, "El registro no se puede borrar por que tiene asociaciones.");
        }

        return tport;
    }

    public final Transporter delete_Valor(String ID_Valor) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM VALOR"
                    + " WHERE ID_Valor = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Valor);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            tport = new Transporter(1, "Error de base de datos inesperado.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter delete_DetalleSubcategoria(String ID_Detalle_Subcategoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM DETALLE_SUBCATEGORIA"
                    + " WHERE ID_Detalle_Subcategoria = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Detalle_Subcategoria);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_Bien(String ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM BIEN"
                    + " WHERE ID_Bien = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bien);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_BDS(String ID_BDS) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM RELACION_BDS"
                    + " WHERE ID_Relacion_BDS = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_BDS);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_GrupoBien(String ID_GrupoBien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM Grupo_Bien"
                    + " WHERE ID_Grupo_Bien = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_GrupoBien);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_PBT(String ID_PBT) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM RELACION_PBT"
                    + " WHERE ID_PBT = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_PBT);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_Garantia(String ID_Garantia) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM GARANTIA"
                    + " WHERE ID_Garantia = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Garantia);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_Solicitud(String ID_Solicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM SOLICITUD"
                    + " WHERE ID_Solicitud = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Solicitud);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_CheckList(String ID_CheckList) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM CHECKLIST"
                    + " WHERE ID_CheckList = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_CheckList);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_CheckListRubro(String CheckListRubro) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM CHECKLIST_RUBRO"
                    + " WHERE ID_CheckList_Rubro = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, CheckListRubro);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            tport = new Transporter(1, "Error de base de datos inesperado.");
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tport;
    }

    public final Transporter delete_Incidente(String ID_Incidente) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM INCIDENTE"
                    + " WHERE ID_Incidente = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Incidente);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_BitacoraIncidente(String ID_Bitacora_Incidente) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM BITACORA_INCIDENTE"
                    + " WHERE ID_Bitacora_Incidente = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Bitacora_Incidente);
            filas_afectadas = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            tport = new Transporter(1, "Error de base de datos inesperado.");
        }
        return tport;
    }

    public final Transporter delete_TipoArchivo(String ID_Tipo_Archivo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM TIPO_ARCHIVO"
                    + " WHERE ID_Tipo_Archivo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Tipo_Archivo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_Medida(String ID_Medida) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM MEDIDA"
                    + " WHERE ID_Medida = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Medida);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_TipoActividad(String ID_TipoActividad) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM TIPO_ACTIVIDAD"
                    + " WHERE ID_TipoActividad = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TipoActividad);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_SolicitudPlantel(String ID_Solicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM SOLICITUD"
                    + " WHERE ID_Solicitud = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Solicitud);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_Enlace(String ID_Enlace) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM ENLACE"
                    + " WHERE ID_Enlace = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Enlace);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_ObjetoArchivo(String ID_Objeto_Archivo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM OBJETO_ARCHIVO"
                    + " WHERE ID_Objeto_Archivo = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Objeto_Archivo);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_Rubro(String ID_Rubro) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM RUBRO"
                    + " WHERE ID_Rubro = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Rubro);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_Puntuacion(String ID_Puntuacion) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM PUNTUACION"
                    + " WHERE ID_Puntuacion = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Puntuacion);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_OrdenSurtimiento_Consumible(String ID_OrdenSurtimiento_Consumible) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM ORDEN_SURTIMIENTO_CONSUMIBLE"
                    + " WHERE ID_Orden_Surtimiento_Consumible = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_OrdenSurtimiento_Consumible);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    public final Transporter delete_Patron_Serie(String ID_Patron_Serie) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM PATRON_SERIE"
                    + " WHERE ID_Patron_Serie = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Patron_Serie);
            int rowCount = pstmt.executeUpdate();
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            endConnection(jscp, conn, pstmt);
            Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().contains("conflicted with the REFERENCE constraint")) {
                tport = new Transporter(1, "El registro no se puede borrar porque contiene asociaciones.");
            } else {
                tport = new Transporter(1, "Error inesperado... Verifique log ");
            }

        }
        return tport;
    }

    //</editor-fold>  
    //<editor-fold defaultstate="collapsed" desc="FunctionGrals">   
//</editor-fold>    
    //<editor-fold defaultstate="collapsed" desc="Transaccion_Delete">  
    public final Transporter trans_delete_SolicitudBaja(String FK_ID_Baja, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;

        SQLSentence = ""
                + " DELETE FROM SOLICITUD_BAJA"
                + " WHERE FK_ID_Baja = ?";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Baja);
        filas_afectadas = pstmt.executeUpdate();
        this.trans_delete_Baja(FK_ID_Baja, conn);
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");

        return tport;
    }

    public final Transporter trans_delete_SolicitudBaja(String ID_SolicitudBaja, String ID_Baja) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM SOLICITUD_BAJA"
                    + " WHERE ID_Solicitud_Baja = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_SolicitudBaja);
            filas_afectadas = pstmt.executeUpdate();
            this.trans_delete_Baja(ID_Baja, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_Baja(String ID_Baja, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM BAJA"
                + " WHERE ID_Baja = ?";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, ID_Baja);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_SoftwarePlantel(
            int ID_Software_Plantel,
            int ID_Software
    ) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " DELETE FROM SOFTWARE_PLANTEL"
                    + " WHERE ID_Software_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setInt(1, ID_Software_Plantel);
            int rowCount = pstmt.executeUpdate();
            trans_delete_Software(ID_Software, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final void trans_delete_Software(int ID_Software, Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " DELETE FROM SOFTWARE"
                + " WHERE ID_Software = ?";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setInt(1, ID_Software);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Transporter trans_delete_GrupoBien(String FK_ID_Bien, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM Grupo_Bien"
                + " WHERE FK_ID_Bien = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Bien);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_GrupoBien_(String FK_ID_GrupoBien, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM Grupo_Bien"
                + " WHERE ID_Grupo_Bien = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_GrupoBien);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_RelacionCRB(String FK_CheckListRubro, String FK_ID_Bien, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM RELACION_CRB"
                + " WHERE "
                + " FK_ID_Bien = ?"
                + " AND FK_ID_CheckList_Rubro = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Bien);
        pstmt.setString(2, FK_CheckListRubro);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_CRB(String FK_ID_Bien, LinkedList ID_Rubros) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        try {

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            Iterator t = ID_Rubros.iterator();
            while (t.hasNext()) {
                LinkedList datos = (LinkedList) t.next();
                this.trans_delete_RelacionCRB(datos.get(0).toString(), FK_ID_Bien, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_ChechkList_Rubro(String FK_ID_CheckList, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM CHECKLIST_RUBRO"
                + " WHERE "
                + " FK_ID_CheckList = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_CheckList);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_CheckList(String FK_ID_CheckList) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM CHECKLIST"
                    + " WHERE ID_CheckList = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            this.trans_delete_ChechkList_Rubro(FK_ID_CheckList, conn);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_CheckList);
            filas_afectadas = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_Archivo(String ID_TablaArchivo, String idArchivo, String FormFrom) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = "";
            if (FormFrom.equalsIgnoreCase("file4Software")) {
                SQLSentence = ""
                        + " DELETE FROM SOFTWARE_ARCHIVO"
                        + " WHERE ID_Software_Archivo = ?";
            } else if (FormFrom.equalsIgnoreCase("file4Bien")) {
                SQLSentence = ""
                        + " DELETE FROM BIEN_ARCHIVO"
                        + " WHERE ID_Bien_Archivo = ?";
            }

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_TablaArchivo);
            int rowCount = pstmt.executeUpdate();
            this.trans_delete_Archivo(idArchivo, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }

        }
        return tport;
    }

    public final Transporter trans_delete_Archivo(String ID_Archivo, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM ARCHIVO"
                + " WHERE "
                + " ID_Archivo = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, ID_Archivo);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_MovimientoConsumible(
            String ID_MovimientoConsumible,
            String ID_Consumible,
            Double nuevaCantidad,
            Double precio,
            String fechaActual) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM MOVIMIENTO_CONSUMIBLE"
                    + " WHERE ID_Movimiento_Consumible = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);

            this.trans_update_CantidadPrecio4Consumible(
                    ID_Consumible,
                    nuevaCantidad,
                    precio,
                    fechaActual,
                    conn);

            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_MovimientoConsumible);
            filas_afectadas = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_ActividadPlantel(
            String ID_Actividad_Plantel,
            String ID_Actividad,
            boolean deleteMainActividad) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM ACTIVIDAD_PLANTEL"
                    + " WHERE ID_Actividad_Plantel = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Actividad_Plantel);
            filas_afectadas = pstmt.executeUpdate();
            if (deleteMainActividad) {
                this.trans_delete_Actividad(ID_Actividad, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_Actividad(String ID_Actividad, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM ACTIVIDAD"
                + " WHERE "
                + " ID_Actividad = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, ID_Actividad);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_ObjetoArchivo_Archivo(
            String ID_ObjetoArchivo,
            String ID_Archivo,
            Boolean deleteArchivo) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM OBJETO_ARCHIVO"
                    + " WHERE ID_Objeto_Archivo = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_ObjetoArchivo);
            filas_afectadas = pstmt.executeUpdate();
            if (deleteArchivo) {
                this.trans_delete_Actividad(ID_Archivo, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_delete_Archivo4Objeto(String ID_Archivo, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        SQLSentence = ""
                + " DELETE FROM ARCHIVO"
                + " WHERE "
                + " ID_Archivo = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, ID_Archivo);
        filas_afectadas = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "El registro se elimino correctamente.");
        return tport;
    }

    public final Transporter trans_delete_OrdenSurtimiento(
            String ID_Orden_Surtimiento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        int filas_afectadas = -1;
        try {
            SQLSentence = ""
                    + " DELETE FROM ORDEN_SURTIMIENTO_CONSUMIBLE"
                    + " WHERE FK_ID_OrdenSurtimiento = ?";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Orden_Surtimiento);
            filas_afectadas = pstmt.executeUpdate();

            SQLSentence = ""
                    + " DELETE FROM ORDEN_SURTIMIENTO"
                    + " WHERE ID_Orden_Surtimiento = ?";

            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Orden_Surtimiento);
            filas_afectadas = pstmt.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "El registro se elimino correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

//</editor-fold>  
    //<editor-fold defaultstate="collapsed" desc="Transaccion_Insert">   
    public final Transporter trans_insert_Baja_SolicitudBaja(
            String FK_ID_Bien,
            String fechaBaja,
            String noOficio,
            String observaciones,
            String motivoBaja,
            String FK_ID_Solicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        String[] idCol = {"ID_Baja"};
        try {
            SQLSentence = ""
                    + " INSERT INTO BAJA"
                    + " ( FK_ID_Bien"
                    + " , fechaBaja"
                    + " , noOficio"
                    + " , observaciones"
                    + " , motivoBaja)"
                    + " VALUES"
                    + " ( ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?)";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, fechaBaja);
            pstmt.setString(3, noOficio);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, motivoBaja);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_insert_SolicitudBaja(id_inserted, FK_ID_Solicitud, conn);
                this.trans_update_Status4Bien("Posible baja", FK_ID_Bien, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_insert_SolicitudBaja(String FK_ID_Baja, String FK_ID_Solicitud, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO SOLICITUD_BAJA"
                + " ( FK_ID_Baja"
                + " , FK_ID_Solicitud)"
                + " VALUES"
                + " ( ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Baja);
        pstmt.setString(2, FK_ID_Solicitud);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final String trans_insert_Software_Plantel(
            String FK_ID_Plantel,
            String FK_ID_Nombre_Software,
            String version,
            String serial,
            String fechaVencimiento,
            String fechaAdquisicion,
            String noDictamen,
            String noLicencias,
            String noLicenciasAsignadas,
            String hddRequerido,
            String ramRequerida,
            String soRequerido,
            String soporteTecnico,
            String emailSoporteTecnico,
            String telefonoSoporte,
            String FK_ID_Tipo_Software,
            String FK_ID_Licencia,
            String FK_ID_Tipo_Compra,
            String nombreResponsable,
            String observaciones,
            String nombreArchivo,
            String estatus
    ) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        String[] idCol = {"ID_Software"};
        try {
            SQLSentence = ""
                    + " INSERT INTO SOFTWARE ("
                    + " FK_ID_NombreSoftware"
                    + " ,version"
                    + " ,serial"
                    + " ,fechaVencimiento"
                    + " ,fechaAdquisicion"
                    + " ,noDictamen"
                    + " ,noLicencias"
                    + " ,hddRequerido"
                    + " ,ramRequerida"
                    + " ,soRequerido"
                    + " ,soporteTecnico"
                    + " ,emailSoporteTecnico"
                    + " ,telefonoSoporte"
                    + " ,FK_ID_Tipo_Software"
                    + " ,FK_ID_Licencia"
                    + " ,FK_ID_Tipo_Compra"
                    + " ,observaciones"
                    + " ,nombreArchivo"
                    + " ,status"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Nombre_Software);
            pstmt.setString(2, version);
            pstmt.setString(3, serial);
            pstmt.setString(4, fechaVencimiento);
            pstmt.setString(5, fechaAdquisicion);
            pstmt.setString(6, noDictamen);
            pstmt.setString(7, noLicencias);
            pstmt.setString(8, hddRequerido);
            pstmt.setString(9, ramRequerida);
            pstmt.setString(10, soRequerido);
            pstmt.setString(11, soporteTecnico);
            pstmt.setString(12, emailSoporteTecnico);
            pstmt.setString(13, telefonoSoporte);
            pstmt.setString(14, FK_ID_Tipo_Software);
            pstmt.setString(15, FK_ID_Licencia);
            pstmt.setString(16, FK_ID_Tipo_Compra);
            pstmt.setString(17, observaciones);
            pstmt.setString(18, nombreArchivo);
            pstmt.setString(19, estatus);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_insert_SoftwarePlantel(FK_ID_Plantel,
                        id_inserted, noLicenciasAsignadas,
                        nombreResponsable, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return id_inserted;
    }

    public final Transporter trans_insert_SoftwarePlantel(String FK_ID_Plantel,
            String FK_ID_Software,
            String noLicenciasAsignadas,
            String nombreResponsable,
            Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO SOFTWARE_PLANTEL ("
                + " FK_ID_Plantel"
                + " ,FK_ID_Software"
                + " ,noLicenciasAsignadas"
                + " ,nombreResponsable"
                + ")"
                + " VALUES ("
                + "  ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " )";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Plantel);
        pstmt.setString(2, FK_ID_Software);
        pstmt.setString(3, noLicenciasAsignadas);
        pstmt.setString(4, nombreResponsable);
        int rowCount = pstmt.executeUpdate();

        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_insert_CheckList_Rubro(
            String FK_ID_Rubro,
            String FK_ID_CheckList,
            String categoria,
            Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO CHECKLIST_RUBRO ("
                + " categoria"
                + " ,FK_ID_Rubro"
                + " ,FK_ID_CheckList"
                + ")"
                + " VALUES ("
                + "  ? "
                + " , ? "
                + " , ? "
                + " )";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, categoria);
        pstmt.setString(2, FK_ID_Rubro);
        pstmt.setString(3, FK_ID_CheckList);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_insert_Rubro_ChecklistBrubro(String FK_ID_Checklist, String rubro, String categoria) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        String[] idCol = {"ID_Rubro"};
        try {
            SQLSentence = ""
                    + " INSERT INTO RUBRO"
                    + " ( rubro)"
                    + " VALUES"
                    + " ( ?)";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, rubro);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_insert_CheckList_Rubro(id_inserted, FK_ID_Checklist, categoria, conn);

            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_insert_RelacionCRB(String FK_ID_Bien, String[] FK_IDs_CheckListRubro, LinkedList idsRubros, HttpSession session) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        try {
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            Iterator t = idsRubros.iterator();
            while (t.hasNext()) {
                LinkedList datos = (LinkedList) t.next();
                this.trans_delete_RelacionCRB(datos.get(0).toString(), FK_ID_Bien, conn);
            }
            for (int i = 0; i < FK_IDs_CheckListRubro.length; i++) {
                this.trans_insert_CRB(
                        FK_ID_Bien,
                        WebUtil.decode(session, FK_IDs_CheckListRubro[i]),
                        conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn);
            tport = new Transporter(0, "La operación se completo correctamente.");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_insert_CRB(String FK_ID_Bien, String FK_ID_CheckListRubro, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO RELACION_CRB"
                + " ( FK_ID_Bien"
                + " , FK_ID_CheckList_Rubro)"
                + " VALUES"
                + " ( ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Bien);
        pstmt.setString(2, FK_ID_CheckListRubro);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final String trans_insert_traspaso(
            String fechaEmision,
            String motivoTraspaso,
            String estatusTraspaso,
            String FK_ID_Personal,
            String FK_ID_Usuario,
            String observaciones,
            String FK_ID_Plantel
    ) {

        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        String[] idCol = {"ID_Traspaso"};
        try {
            SQLSentence = ""
                    + " INSERT INTO TRASPASO"
                    + " (fechaEmision"
                    + ", motivoTraspaso"
                    + " ,estatusTraspaso"
                    + ", FK_ID_Personal"
                    + ", FK_ID_Usuario"
                    + ", observaciones"
                    + ", FK_ID_Plantel"
                    + " )"
                    + " VALUES"
                    + " ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";
            System.out.println("SQLSentence::" + SQLSentence);
            System.out.println("fechaEmision" + fechaEmision);
            System.out.println("motivoTraspaso" + motivoTraspaso);
            System.out.println("estatusTraspaso" + estatusTraspaso);
            System.out.println("FK_ID_Personal" + FK_ID_Personal);
            System.out.println("FK_ID_Usuario" + FK_ID_Usuario);
            System.out.println("observaciones" + observaciones);
            System.out.println("FK_ID_Plantel" + FK_ID_Plantel);

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setString(1, fechaEmision);
            pstmt.setString(2, motivoTraspaso);
            pstmt.setString(3, estatusTraspaso);
            pstmt.setString(4, FK_ID_Personal);
            pstmt.setString(5, FK_ID_Usuario);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, FK_ID_Plantel);
            pstmt.setQueryTimeout(statementTimeOut);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
            }
            System.out.println("id_inserted" + id_inserted);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return id_inserted;
    }

    public final String trans_insert_Software_Plantel(
            String FK_ID_Plantel,
            String FK_ID_Nombre_Software,
            String version,
            String serial,
            String fechaVencimiento,
            String fechaAdquisicion,
            String noDictamen,
            String noLicencias,
            String noLicenciasAsignadas,
            int hddRequerido,
            int ramRequerida,
            String soRequerido,
            String soporteTecnico,
            String emailSoporteTecnico,
            String telefonoSoporte,
            String FK_ID_Tipo_Software,
            String FK_ID_Licencia,
            String FK_ID_Tipo_Compra,
            String nombreResponsable,
            String observaciones,
            String nombreArchivo,
            String estatus,
            String FK_ID_Proveedor,
            String fechaInstalacion,
            String noContrato,
            String noAutorizacion,
            int aniosLicencia,
            String noFactura,
            int upgrade,
            int degrade,
            int noActualizacionesPermitidas
    ) {
        System.out.println("soporte: " + soporteTecnico);
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        String[] idCol = {"ID_Software"};
        try {
            SQLSentence = ""
                    + " INSERT INTO SOFTWARE ("
                    + " FK_ID_NombreSoftware"
                    + " ,version"
                    + " ,serial"
                    + " ,fechaVencimiento"
                    + " ,fechaAdquisicion"
                    + " ,noDictamen"
                    + " ,noLicencias"
                    + " ,hddRequerido"
                    + " ,ramRequerida"
                    + " ,soRequerido"
                    + " ,soporteTecnico"
                    + " ,emailSoporteTecnico"
                    + " ,telefonoSoporte"
                    + " ,FK_ID_Tipo_Software"
                    + " ,FK_ID_Licencia"
                    + " ,FK_ID_Tipo_Compra"
                    + " ,observaciones"
                    + " ,nombreArchivo"
                    + " ,status"
                    + " ,FK_ID_Proveedor"
                    + " ,noContrato"
                    + " ,noAutorizacion"
                    + " ,fechaInstalacion"
                    + " ,aniosLicencia"
                    + " ,noFactura"
                    + " ,upgrade"
                    + " ,degrade"
                    + " ,noActualizacionesPermitidas"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, idCol);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Nombre_Software);
            pstmt.setString(2, version);
            pstmt.setString(3, serial);
            pstmt.setString(4, fechaVencimiento);
            pstmt.setString(5, fechaAdquisicion);
            pstmt.setString(6, noDictamen);
            pstmt.setString(7, noLicencias);
            pstmt.setInt(8, hddRequerido);
            pstmt.setInt(9, ramRequerida);
            pstmt.setString(10, soRequerido);
            pstmt.setString(11, soporteTecnico);
            pstmt.setString(12, emailSoporteTecnico);
            pstmt.setString(13, telefonoSoporte);
            pstmt.setString(14, FK_ID_Tipo_Software);
            pstmt.setString(15, FK_ID_Licencia);
            pstmt.setString(16, FK_ID_Tipo_Compra);
            pstmt.setString(17, observaciones);
            pstmt.setString(18, nombreArchivo);
            pstmt.setString(19, estatus);
            pstmt.setString(20, FK_ID_Proveedor);
            pstmt.setString(21, noContrato);
            pstmt.setString(22, noAutorizacion);
            pstmt.setString(23, fechaInstalacion);
            pstmt.setInt(24, aniosLicencia);
            pstmt.setString(25, noFactura);
            pstmt.setInt(26, upgrade);
            pstmt.setInt(27, degrade);
            pstmt.setInt(28, noActualizacionesPermitidas);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_insert_SoftwarePlantel(FK_ID_Plantel,
                        id_inserted, noLicenciasAsignadas,
                        nombreResponsable, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            id_inserted = "";
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return id_inserted;
    }

    public final Transporter trans_insert_Archivo4Software(String mainID, String FK_ID_Tipo_Archivo, String nombreArchivo,
            String descripcion, String ubicacionFisica, String extension, String fechaActualizacion, String forTable) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        try {
            SQLSentence = ""
                    + "INSERT INTO ARCHIVO ( "
                    + " FK_ID_Tipo_Archivo"
                    + " ,nombreArchivo"
                    + " ,descripcion"
                    + " ,ubicacionFisica"
                    + " ,extension"
                    + " ,fechaActualizacion"
                    + ") VALUES ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Tipo_Archivo);
            pstmt.setString(2, nombreArchivo);
            pstmt.setString(3, descripcion);
            pstmt.setString(4, ubicacionFisica);
            pstmt.setString(5, extension);
            pstmt.setString(6, fechaActualizacion);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                if (forTable.equalsIgnoreCase("file4Software")) {
                    this.trans_insert_Software_Archivo(mainID, id_inserted, conn);
                } else if (forTable.equalsIgnoreCase("file4Bien")) {
                    this.trans_insert_Bien_Archivo(mainID, id_inserted, conn);
                }

            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_insert_Archivo4Objeto(
            String FK_ID_Objeto,
            String nombreObjeto,
            String FK_ID_Tipo_Archivo,
            String nombreArchivo,
            String descripcion,
            String ubicacionFisica,
            String extension,
            String fechaActualizacion,
            long tamanio,
            String tipoAcceso,
            String keywords,
            String hashName,
            String FK_ID_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        try {
            SQLSentence = ""
                    + "INSERT INTO ARCHIVO ( "
                    + " FK_ID_Tipo_Archivo"
                    + " ,nombreArchivo"
                    + " ,descripcion"
                    + " ,ubicacionFisica"
                    + " ,extension"
                    + " ,fechaActualizacion"
                    + " ,tamanio"
                    + " ,tipoAcceso"
                    + " ,keywords"
                    + " ,hashName"
                    + " ,FK_ID_Plantel"
                    + ") VALUES ("
                    + " ?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Tipo_Archivo);
            pstmt.setString(2, nombreArchivo);
            pstmt.setString(3, descripcion);
            pstmt.setString(4, ubicacionFisica);
            pstmt.setString(5, extension);
            pstmt.setString(6, fechaActualizacion);
            pstmt.setLong(7, tamanio);
            pstmt.setString(8, tipoAcceso);
            pstmt.setString(9, keywords);
            pstmt.setString(10, hashName);
            pstmt.setString(11, FK_ID_Plantel);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_insert_Objeto_Archivo(FK_ID_Objeto, id_inserted, nombreObjeto, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_insert_Objeto_Archivo(String FK_ID_Objeto, String FK_ID_Archivo, String nombreObjeto, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO OBJETO_ARCHIVO"
                + " ( nombreObjeto"
                + " , FK_ID_Objeto"
                + " , FK_ID_Archivo)"
                + " VALUES"
                + " ( ?"
                + " , ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, nombreObjeto);
        pstmt.setString(2, FK_ID_Objeto);
        pstmt.setString(3, FK_ID_Archivo);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_insert_Software_Archivo(String FK_ID_Software, String FK_ID_Archivo, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO SOFTWARE_ARCHIVO"
                + " ( FK_ID_Software"
                + " , FK_ID_Archivo)"
                + " VALUES"
                + " ( ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Software);
        pstmt.setString(2, FK_ID_Archivo);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_insert_Bien_Archivo(String FK_ID_Bien, String FK_ID_Archivo, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO Bien_ARCHIVO"
                + " ( FK_ID_Bien"
                + " , FK_ID_Archivo)"
                + " VALUES"
                + " ( ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Bien);
        pstmt.setString(2, FK_ID_Archivo);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Long trans_insert_MovimientoEntrada(
            String FK_ID_Plantel,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatus,
            String observaciones,
            String iva,
            String FK_ID_Tipo_Movimiento,
            String FK_ID_Usuario,
            String motivoMovimiento,
            String FK_ID_Proveedor,
            String FK_ID_TipoCompra,
            String noTurno,
            String fechaFactura) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO MOVIMIENTO ("
                    + " FK_ID_Plantel"
                    + " ,folio"
                    + " ,fechaMovimiento"
                    + " ,fechaActualizacion"
                    + " ,noFactura"
                    + " ,noReferencia"
                    + " ,estatus"
                    + " ,observaciones"
                    + " ,iva"
                    + " ,FK_ID_Tipo_Movimiento"
                    + " ,FK_ID_Usuario"
                    + " ,motivoMovimiento"
                    + " ,noTurno"
                    + " ,fechaFactura"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatus);
            pstmt.setString(8, observaciones);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_Tipo_Movimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            pstmt.setString(13, noTurno);
            pstmt.setString(14, fechaFactura);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                this.trans_insert_MovimientpProveedor(idInserted, FK_ID_Proveedor, FK_ID_TipoCompra, conn);
                this.trans_update_CuentaTotalMovimiento(FK_ID_Tipo_Movimiento, folio, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final Transporter trans_insert_MovimientpProveedor(Long FK_ID_Movimiento, String FK_ID_Proveedor, String FK_ID_TipoCompra, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " INSERT INTO MOVIMIENTO_PROVEEDOR"
                + " ( FK_ID_Movimiento"
                + " , FK_ID_Proveedor"
                + " , FK_ID_Tipo_Compra)"
                + " VALUES"
                + " ( ?"
                + " , ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setLong(1, FK_ID_Movimiento);
        pstmt.setString(2, FK_ID_Proveedor);
        pstmt.setString(3, FK_ID_TipoCompra);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_insert_ConsumibleMovimiento(
            String ID_Consumible,
            String ID_Movimiento,
            String cantidad,
            String precioUnitario,
            Double nuevoPrecio,
            Double nuevaCantidad,
            String fechaActual) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id_inserted = "";
        try {
            SQLSentence = ""
                    + " INSERT INTO MOVIMIENTO_CONSUMIBLE"
                    + " ( FK_ID_Movimiento"
                    + " , FK_ID_Consumible"
                    + " , cantidad"
                    + " , precioUnitario)"
                    + " VALUES"
                    + " ( ?"
                    + " , ?"
                    + " , ?"
                    + " , ?)";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Movimiento);
            pstmt.setString(2, ID_Consumible);
            pstmt.setString(3, cantidad);
            pstmt.setString(4, precioUnitario);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id_inserted = rs.getString(1);
                this.trans_update_CantidadPrecio4Consumible(ID_Consumible, nuevoPrecio, nuevaCantidad, fechaActual, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Long trans_insert_MovimientoSalida(
            String FK_ID_Plantel,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatus,
            String observaciones,
            String iva,
            String FK_ID_Tipo_Movimiento,
            String FK_ID_Usuario,
            String motivoMovimiento,
            String FK_ID_DepartamentoPlantel,
            String FK_ID_PersonalPlantel,
            String noTurno) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO MOVIMIENTO ("
                    + " FK_ID_Plantel"
                    + " ,folio"
                    + " ,fechaMovimiento"
                    + " ,fechaActualizacion"
                    + " ,noFactura"
                    + " ,noReferencia"
                    + " ,estatus"
                    + " ,observaciones"
                    + " ,iva"
                    + " ,FK_ID_Tipo_Movimiento"
                    + " ,FK_ID_Usuario"
                    + " ,motivoMovimiento"
                    + " ,noTurno"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatus);
            pstmt.setString(8, observaciones);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_Tipo_Movimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            pstmt.setString(13, noTurno);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                this.trans_insert_Traslado(idInserted, FK_ID_DepartamentoPlantel, FK_ID_PersonalPlantel, fechaActualizacion, conn);
                this.trans_update_CuentaTotalMovimiento(FK_ID_Tipo_Movimiento, folio, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final Transporter trans_insert_Traslado(Long FK_ID_Movimiento, String ID_DepartamentoPlantel, String ID_PersonalPlantel, String fechaActualizacion, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO TRASLADO"
                + " ( FK_ID_Movimiento"
                + " , FK_ID_Departamento_Plantel"
                + " , FK_ID_Personal_Plantel"
                + " , fechaActualizacion)"
                + " VALUES"
                + " ( ?"
                + " , ?"
                + " , ?"
                + " , ?)";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setLong(1, FK_ID_Movimiento);
        pstmt.setString(2, ID_DepartamentoPlantel);
        pstmt.setString(3, ID_PersonalPlantel);
        pstmt.setString(4, fechaActualizacion);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final void trans_insert_GrupoBien(String FK_ID_Bien, String FK_ID_Grupo, Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO GRUPO_BIEN"
                + " ( FK_ID_Grupo "
                + " , FK_ID_Bien )"
                + " VALUES "
                + " ( ? "
                + " , ? )";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Grupo);
        pstmt.setString(2, FK_ID_Bien);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Long trans_insert_GrupoBien(
            String FK_ID_Bien,
            String FK_ID_Grupo,
            String FK_ID_DepartamentoPlantel,
            String ID_Plantel) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " INSERT INTO GRUPO_BIEN ("
                    + " FK_ID_BIEN"
                    + " ,FK_ID_Grupo"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Bien);
            pstmt.setString(2, FK_ID_Grupo);
            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                this.trans_update_DeptoBien(FK_ID_DepartamentoPlantel, FK_ID_Bien, ID_Plantel, conn);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final Long trans_insert_Actividad(
            String descripcion,
            String fechaCreacion,
            String fechaInicio,
            String fechaFin,
            String ultimaActualizacion,
            String horaCreacion,
            String horaInicio,
            String horaFin,
            String horaUltimaActuaizacion,
            String FK_ID_UsuarioCreo,
            String FK_ID_UsuarioActualizo,
            String FK_ID_TipoActividad,
            String fechaLimite,
            String horaLimite,
            String estatus,
            String fechaCambioEstatus,
            String horaCambioEstatus,
            String observaciones,
            String AP_FK_ID_UsuarioActualizo,
            String porcentajeCompleto,
            LinkedList ID_Plantel) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " INSERT INTO ACTIVIDAD ("
                    + " descripcion"
                    + " , fechaCreacion"
                    + " , fechaInicio"
                    + " , fechaFin"
                    + " , ultimaActualizacion"
                    + " , horaCreacion"
                    + " , horaInicio"
                    + " , horaFin"
                    + " , horaUltimaActualizacion"
                    + " , FK_ID_UsuarioCreo"
                    + " , FK_ID_UsuarioActualizo"
                    + " , FK_ID_TipoActividad"
                    + " , fechaLimite"
                    + " , horaLimite"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, descripcion);
            pstmt.setString(2, fechaCreacion);
            pstmt.setString(3, fechaInicio);
            pstmt.setString(4, fechaFin);
            pstmt.setString(5, ultimaActualizacion);
            pstmt.setString(6, horaCreacion);
            pstmt.setString(7, horaInicio);
            pstmt.setString(8, horaFin);
            pstmt.setString(9, horaUltimaActuaizacion);
            pstmt.setString(10, FK_ID_UsuarioCreo);
            pstmt.setString(11, FK_ID_UsuarioActualizo);
            pstmt.setString(12, FK_ID_TipoActividad);
            pstmt.setString(13, fechaLimite);
            pstmt.setString(14, horaLimite);

            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                for (int i = 0; i < ID_Plantel.size(); i++) {
                    this.trans_insert_ActividadPlantel(idInserted.toString(),
                            ID_Plantel.get(i).toString(),
                            estatus,
                            fechaCambioEstatus,
                            observaciones,
                            horaCambioEstatus,
                            FK_ID_UsuarioActualizo,
                            porcentajeCompleto,
                            conn);
                }

            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final void trans_insert_ActividadPlantel(String FK_ID_Actividad,
            String FK_ID_Plantel,
            String estatus,
            String fechaCambioEstatus,
            String obervaciones,
            String horaCambioEstatus,
            String FK_ID_UsuarioActualizo,
            String porcentajeCompleto,
            Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO ACTIVIDAD_PLANTEL"
                + " ("
                + "  FK_ID_Actividad"
                + " , FK_ID_Plantel"
                + " , estatus"
                + " , fechaCambioEstatus"
                + " , observaciones"
                + " , horaCambioEstatus"
                + " , FK_ID_UsuarioActualizo"
                + " , porcentajeCompleto"
                + ")"
                + " VALUES "
                + " ( ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + ")";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Actividad);
        pstmt.setString(2, FK_ID_Plantel);
        pstmt.setString(3, estatus);
        pstmt.setString(4, fechaCambioEstatus);
        pstmt.setString(5, obervaciones);
        pstmt.setString(6, horaCambioEstatus);
        pstmt.setString(7, FK_ID_UsuarioActualizo);
        pstmt.setString(8, porcentajeCompleto);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Long trans_insert_Puntuacion(
            String FK_ID_Plantel,
            String FK_ID_Rubro,
            String puntuacion,
            String observaciones,
            String fechaRegistro,
            String estatus,
            String FK_ID_Usuario) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " UPDATE PUNTUACION SET"
                    + " estatus = ?"
                    + " WHERE FK_ID_Rubro = ?"
                    + " AND FK_ID_Plantel = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "0");
            pstmt.setString(2, FK_ID_Rubro);
            pstmt.setString(3, FK_ID_Plantel);
            int rowCount = pstmt.executeUpdate();

            SQLSentence = ""
                    + " INSERT INTO PUNTUACION"
                    + " ("
                    + "  FK_ID_Plantel"
                    + " , FK_ID_Rubro"
                    + " , puntuacion"
                    + " , observaciones"
                    + " , fechaRegistro"
                    + " , estatus"
                    + " , FK_ID_Usuario"
                    + ")"
                    + " VALUES "
                    + " ( ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + " , ? "
                    + ")";

            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, FK_ID_Rubro);
            pstmt.setString(3, puntuacion);
            pstmt.setString(4, observaciones);
            pstmt.setString(5, fechaRegistro);
            pstmt.setString(6, estatus);
            pstmt.setString(7, FK_ID_Usuario);

            rowCount = pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final Long trans_insert_OrdenSurtimiento(
            String FK_ID_PlantelAlmacen,
            String asuntoGeneral,
            String fechaOrden,
            String folio,
            String fechaRequerida,
            String justificacion,
            String observaciones,
            String estatus,
            String FK_ID_UsuarioCrea,
            String FK_ID_UsuarioEntrega,
            String fechaAtencion,
            LinkedList consumibles) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " INSERT INTO ORDEN_SURTIMIENTO ("
                    + " FK_ID_Plantel_Almacen"
                    + " , fechaOrden"
                    + " , folio"
                    + " , fechaRequerida"
                    + " , justificacion"
                    + " , observaciones"
                    + " , estatus"
                    + " , FK_ID_UsuarioCrea"
                    + " , FK_ID_UsuarioEntrega"
                    + " , fechaAtencion"
                    + " , asuntoGeneral"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " , ?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_PlantelAlmacen);
            pstmt.setString(2, fechaOrden);
            pstmt.setString(3, folio);
            pstmt.setString(4, fechaRequerida);
            pstmt.setString(5, justificacion);
            pstmt.setString(6, observaciones);
            pstmt.setString(7, estatus);
            pstmt.setString(8, FK_ID_UsuarioCrea);
            pstmt.setString(9, FK_ID_UsuarioEntrega);
            pstmt.setString(10, fechaAtencion);
            pstmt.setString(11, asuntoGeneral);

            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                Iterator t = consumibles.iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
                    this.trans_insert_OrdenSurtimiento_Consumible(
                            idInserted.toString(),
                            datos.get(0).toString(),
                            datos.get(1).toString(),
                            "0",
                            "", conn);
                }

            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }

    public final void trans_insert_OrdenSurtimiento_Consumible(
            String FK_ID_OrdenSurtimiento,
            String FK_ID_Consumible,
            String cantidadSolicitada,
            String cantidadSurtida,
            String observaciones,
            Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " INSERT INTO ORDEN_SURTIMIENTO_CONSUMIBLE"
                + " ("
                + "  FK_ID_OrdenSurtimiento"
                + " , FK_ID_Consumible"
                + " , cantidadSolicitada"
                + " , cantidadSurtida"
                + " , observaciones"
                + ")"
                + " VALUES "
                + " ( ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + " , ? "
                + ")";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_OrdenSurtimiento);
        pstmt.setString(2, FK_ID_Consumible);
        pstmt.setString(3, cantidadSolicitada);
        pstmt.setString(4, cantidadSurtida);
        pstmt.setString(5, observaciones);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Long trans_insert_OrdenSurtimientoMovimientoSalida(
            String FK_ID_PlantelSurte,
            String FK_ID_PlantelSolicita,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatusMovimiento,
            String observacionesMovimiento,
            String iva,
            String FK_ID_TipoMovimiento,
            String FK_ID_Usuario,
            String motivoMovimiento,
            String noTurno,
            String observacionesOrden,
            String estatusOrden,
            String FK_ID_UsuarioEntrega,
            String fechaAtencion,
            String FK_ID_OrdenSurtimiento,
            LinkedList consumibles) {
        Long idInserted = new Long(-1);
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            SQLSentence = ""
                    + " INSERT INTO MOVIMIENTO ("
                    + " FK_ID_Plantel"
                    + " ,folio"
                    + " ,fechaMovimiento"
                    + " ,fechaActualizacion"
                    + " ,noFactura"
                    + " ,noReferencia"
                    + " ,estatus"
                    + " ,observaciones"
                    + " ,iva"
                    + " ,FK_ID_Tipo_Movimiento"
                    + " ,FK_ID_Usuario"
                    + " ,motivoMovimiento"
                    + " ,noTurno"
                    + ")"
                    + " VALUES ("
                    + "  ? "
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " ,?"
                    + " )";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_PlantelSurte);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatusMovimiento);
            pstmt.setString(8, observacionesMovimiento);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_TipoMovimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            pstmt.setString(13, noTurno);

            int rowCount = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                idInserted = rs.getLong(1);
                SQLSentence = ""
                        + " INSERT INTO MOVIMIENTO_ORDENSURTIMIENTO ("
                        + " FK_ID_Movimiento"
                        + " ,FK_ID_Orden_Surtimiento"
                        + " ,FK_ID_PlantelDestino"
                        + ")"
                        + " VALUES ("
                        + "  ? "
                        + " ,?"
                        + " ,?"
                        + " )";
                pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setString(1, idInserted.toString());
                pstmt.setString(2, FK_ID_OrdenSurtimiento);
                pstmt.setString(3, FK_ID_PlantelSolicita);
                rowCount = pstmt.executeUpdate();

                SQLSentence = ""
                        + " UPDATE ORDEN_SURTIMIENTO SET"
                        + " observaciones = ?"
                        + " , estatus = ?"
                        + " , FK_ID_UsuarioEntrega = ?"
                        + " , fechaAtencion = ?"
                        + " WHERE "
                        + "  ID_Orden_Surtimiento = ? ";
                pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setString(1, observacionesOrden);
                pstmt.setString(2, estatusOrden);
                pstmt.setString(3, FK_ID_UsuarioEntrega);
                pstmt.setString(4, fechaAtencion);
                pstmt.setString(5, FK_ID_OrdenSurtimiento);
                rowCount = pstmt.executeUpdate();

                Iterator t = consumibles.iterator();
                while (t.hasNext()) {
                    LinkedList datos = (LinkedList) t.next();
                    this.trans_update_OrdenSurtimiento_Consumible(
                            datos.get(1).toString(),
                            datos.get(2).toString(),
                            datos.get(3).toString(), //id
                            datos.get(4).toString(), //total
                            datos.get(5).toString(), //estatus
                            datos.get(0).toString(),
                            conn);
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            idInserted = new Long(-1);
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return idInserted;
    }
//</editor-fold>  
    //<editor-fold defaultstate="collapsed" desc="Transaccion_Update">  

    public final Transporter trans_update_Bien(
            String FK_ID_Subcategoria,
            String FK_ID_Modelo,
            String descripcionbien,
            String noserie,
            String nodictamen,
            String nofactura,
            String noinventario,
            String FK_ID_TipoCompra,
            String fechaCompra,
            String FK_ID_Plantel,
            String FK_ID_DepartamentoPlantel,
            String fechaAlta,
            String status,
            String observaciones,
            String archivo,
            String ID_Grupo,
            String ID_Grupo_Bien,
            String ID_Bien,
            String groupOperation,
            boolean deleteBaja,
            LinkedList ID_RegistroBaja) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE BIEN SET"
                    + " FK_ID_Subcategoria = ?"
                    + " ,FK_ID_Modelo = ?"
                    + " ,descripcionBien = ?"
                    + " ,noSerie = ?"
                    + " ,noDictamen = ?"
                    + " ,noFactura = ?"
                    + " ,noInventario = ?"
                    + " ,FK_ID_Tipo_Compra = ?"
                    + " ,fechaCompra = ?"
                    + " ,FK_ID_Plantel = ?"
                    + " ,FK_ID_Departamento_Plantel = ?"
                    + " ,fechaAlta = ?"
                    + " ,status = ?"
                    + " ,observaciones = ?"
                    + " ,archivo = ?"
                    + " WHERE ID_Bien = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Subcategoria);
            pstmt.setString(2, FK_ID_Modelo);
            pstmt.setString(3, descripcionbien);
            pstmt.setString(4, noserie);
            pstmt.setString(5, nodictamen);
            pstmt.setString(6, nofactura);
            pstmt.setString(7, noinventario);
            pstmt.setString(8, FK_ID_TipoCompra);
            pstmt.setString(9, fechaCompra);
            pstmt.setString(10, FK_ID_Plantel);
            pstmt.setString(11, FK_ID_DepartamentoPlantel);
            pstmt.setString(12, fechaAlta);
            pstmt.setString(13, status);
            pstmt.setString(14, observaciones);
            pstmt.setString(15, archivo);
            pstmt.setString(16, ID_Bien);
            int rowCount = pstmt.executeUpdate();
            if (groupOperation.equalsIgnoreCase("update")) {
                this.trans_update_GrupoBien(ID_Bien, ID_Grupo, ID_Grupo_Bien, conn);
            } else if (groupOperation.equalsIgnoreCase("insert")) {
                this.trans_insert_GrupoBien(ID_Bien, ID_Grupo, conn);
            } else if (groupOperation.equalsIgnoreCase("delete")) {
                this.trans_delete_GrupoBien_(ID_Grupo_Bien, conn);
            }
            if (deleteBaja && ID_RegistroBaja != null) {
                for (int i = 0; i < ID_RegistroBaja.size(); i++) {

                    this.trans_delete_SolicitudBaja(ID_RegistroBaja.get(i).toString(), conn);
                }
            }
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final void trans_update_GrupoBien(String FK_ID_Bien, String FK_ID_Grupo, String ID_Grupo_Bien, Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " UPDATE GRUPO_BIEN SET "
                + " FK_ID_Grupo = ? "
                + " ,FK_ID_Bien = ? "
                + " WHERE ID_Grupo_Bien = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Grupo);
        pstmt.setString(2, FK_ID_Bien);
        pstmt.setString(3, ID_Grupo_Bien);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Transporter trans_update_Status4Bien(String status, String ID_Bien, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE BIEN SET "
                + " status = ? "
                + " WHERE ID_Bien = ? ";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, status);
        pstmt.setString(2, ID_Bien);
        int rowCount = pstmt.executeUpdate();
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        endConnection(pstmt);
        return tport;
    }

    public final Transporter trans_update_Baja_Bien_Solicitud(
            LinkedList bienes,
            String statusSolicitud,
            String fechaBaja,
            String noOficio,
            String statusBien,
            String ID_Solicitud) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE SOLICITUD SET"
                    + " status = ?"
                    + " WHERE ID_SOLICITUD = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, statusSolicitud);
            pstmt.setString(2, ID_Solicitud);
            int rowCount = pstmt.executeUpdate();
            this.trans_update_Baja4Solicitud(bienes, fechaBaja, noOficio, statusBien, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_update_Baja4Solicitud(LinkedList bienes, String fechaBaja, String noOficio, String statusBien, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        Iterator t = bienes.iterator();
        SQLSentence = ""
                + " UPDATE BAJA SET "
                + " fechaBaja = ? "
                + " , noOficio = ? "
                + " WHERE ID_Baja = ? ";
        while (t.hasNext()) {
            LinkedList baja = (LinkedList) t.next();
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, fechaBaja);
            pstmt.setString(2, noOficio);
            pstmt.setString(3, baja.get(15).toString());
            int rowCount = pstmt.executeUpdate();
            this.trans_update_Status4Bien(statusBien, baja.get(10).toString(), conn);
            this.trans_delete_GrupoBien(baja.get(10).toString(), conn);
            endConnection(pstmt);
        }

        tport = new Transporter(0, "La operación se realizo correctamente");

        return tport;
    }

    public final Transporter trans_update_Software(
            String FK_ID_Plantel,
            String ID_Nombre_Software,
            String version,
            String serial,
            String fechaVencimiento,
            String fechaAdquisicion,
            String noDictamen,
            String noLicencias,
            String noLicenciasAsignadas,
            int hddRequerido,
            int ramRequerida,
            String soRequerido,
            String soporteTecnico,
            String emailSoporteTecnico,
            String telefonoSoporte,
            String FK_ID_Tipo_Software,
            String FK_ID_Licencia,
            String FK_ID_Tipo_Compra,
            String nombreResponsable,
            String observaciones,
            String status,
            int FK_ID_Proveedor,
            String noContrato,
            String noAutorizacion,
            String fechaIstalacion,
            int aniosLicencia,
            String noFactura,
            int upgrade,
            int degrade,
            int noActualizacionesPermitidas,
            int ID_Software,
            String ID_Software_Plantel) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE SOFTWARE SET"
                    + " version = ?"
                    + " ,serial = ?"
                    + " ,fechaVencimiento = ?"
                    + " ,fechaAdquisicion = ?"
                    + " ,noDictamen = ?"
                    + " ,noLicencias = ?"
                    + " ,hddRequerido = ?"
                    + " ,ramRequerida = ?"
                    + " ,soRequerido = ?"
                    + " ,soporteTecnico = ?"
                    + " ,emailSoporteTecnico = ?"
                    + " ,telefonoSoporte = ?"
                    + " ,FK_ID_Tipo_Software = ?"
                    + " ,FK_ID_Licencia = ?"
                    + " ,FK_ID_Tipo_Compra = ?"
                    + " ,observaciones = ?"
                    + " ,FK_ID_NombreSoftware = ?"
                    + " ,status = ?"
                    + " ,FK_ID_Proveedor = ?"
                    + " ,noContrato = ?"
                    + " ,noAutorizacion = ?"
                    + " ,fechaInstalacion = ?"
                    + " ,aniosLicencia = ?"
                    + " ,noFactura = ?"
                    + " ,upgrade = ?"
                    + " ,degrade = ?"
                    + " ,noActualizacionesPermitidas = ?"
                    + " WHERE ID_Software = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, version);
            pstmt.setString(2, serial);
            pstmt.setString(3, fechaVencimiento);
            pstmt.setString(4, fechaAdquisicion);
            pstmt.setString(5, noDictamen);
            pstmt.setString(6, noLicencias);
            pstmt.setInt(7, hddRequerido);
            pstmt.setInt(8, ramRequerida);
            pstmt.setString(9, soRequerido);
            pstmt.setString(10, soporteTecnico);
            pstmt.setString(11, emailSoporteTecnico);
            pstmt.setString(12, telefonoSoporte);
            pstmt.setString(13, FK_ID_Tipo_Software);
            pstmt.setString(14, FK_ID_Licencia);
            pstmt.setString(15, FK_ID_Tipo_Compra);
            pstmt.setString(16, observaciones);
            pstmt.setString(17, ID_Nombre_Software);
            pstmt.setString(18, status);
            pstmt.setInt(19, FK_ID_Proveedor);
            pstmt.setString(20, noContrato);
            pstmt.setString(21, noAutorizacion);
            pstmt.setString(22, fechaIstalacion);
            pstmt.setInt(23, aniosLicencia);
            pstmt.setString(24, noFactura);
            pstmt.setInt(25, upgrade);
            pstmt.setInt(26, degrade);
            pstmt.setInt(27, noActualizacionesPermitidas);
            pstmt.setInt(28, ID_Software);
            int rowCount = pstmt.executeUpdate();

            trans_update_SoftwarePlantel(FK_ID_Plantel, ID_Software, noLicenciasAsignadas, nombreResponsable, ID_Software_Plantel, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final void trans_update_SoftwarePlantel(String FK_ID_Plantel,
            int FK_ID_Software,
            String noLicenciasAsignadas,
            String nombreResponsable,
            String ID_Software_Plantel, Connection conn) throws SQLException {
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        SQLSentence = ""
                + " UPDATE SOFTWARE_PLANTEL SET "
                + " FK_ID_Plantel = ? "
                + " , FK_ID_Software = ? "
                + " ,noLicenciasAsignadas = ? "
                + ", nombreResponsable = ?"
                + " WHERE ID_Software_Plantel = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Plantel);
        pstmt.setInt(2, FK_ID_Software);
        pstmt.setString(3, noLicenciasAsignadas);
        pstmt.setString(4, nombreResponsable);
        pstmt.setString(5, ID_Software_Plantel);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
    }

    public final Transporter trans_update_CantidadPrecio4Consumible(
            String ID_Consumible,
            Double cantidad,
            Double precio,
            String fechaActualizacion,
            Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE CONSUMIBLE SET"
                + " total = ?"
                + " , precioActual= ?"
                + " , fechaActualizacion = ?"
                + " WHERE ID_Consumible = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setDouble(1, cantidad);
        pstmt.setDouble(2, precio);
        pstmt.setString(3, fechaActualizacion);
        pstmt.setString(4, ID_Consumible);
        int rowCount = pstmt.executeUpdate();
        conn.commit();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_Movimiento(
            String FK_ID_Plantel,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatus,
            String observaciones,
            String iva,
            String FK_ID_Tipo_Movimiento,
            String FK_ID_Usuario,
            String motivoMovimiento,
            String FK_ID_Proveedor,
            String FK_ID_TipoCompra,
            String ID_MovimientoProveedor,
            String fechaFactura,
            String ID_Movimiento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " UPDATE MOVIMIENTO SET "
                    + " FK_ID_Plantel = ?"
                    + " , folio = ?"
                    + " , fechaMovimiento = ?"
                    + " , fechaActualizacion = ?"
                    + " , noFactura = ?"
                    + " , noReferencia = ?"
                    + " , estatus = ?"
                    + " , observaciones = ?"
                    + " , iva = ?"
                    + " , FK_ID_Tipo_Movimiento = ?"
                    + " , FK_ID_Usuario = ?"
                    + " , motivoMovimiento = ?"
                    + " , fechaFactura = ?"
                    + " WHERE"
                    + " ID_Movimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatus);
            pstmt.setString(8, observaciones);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_Tipo_Movimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            pstmt.setString(13, fechaFactura);
            pstmt.setString(14, ID_Movimiento);
            int rowCount = pstmt.executeUpdate();
            this.trans_update_MovimientoProveedor(ID_Movimiento, FK_ID_Proveedor, FK_ID_TipoCompra, ID_MovimientoProveedor, conn);
            conn.commit();
            tport = new Transporter(0, "El registro se actualizo correctamente.");
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al acualizar el registro.");
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return tport;
    }

    public final Transporter trans_update_MovimientoProveedor(String FK_ID_Movimiento, String FK_ID_Proveedor, String FK_ID_TipoCompra, String ID_MovimientoProveedor, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE MOVIMIENTO_PROVEEDOR SET"
                + "  FK_ID_Proveedor = ?"
                + " , FK_ID_Tipo_Compra = ?"
                + " , FK_ID_Movimiento = ?"
                + " WHERE ID_Movimiento_Proveedor = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Proveedor);
        pstmt.setString(2, FK_ID_TipoCompra);
        pstmt.setString(3, FK_ID_Movimiento);
        pstmt.setString(4, ID_MovimientoProveedor);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_MovimientoSalida(
            String FK_ID_Plantel,
            String folio,
            String fechaMovimiento,
            String fechaActualizacion,
            String noFactura,
            String noReferencia,
            String estatus,
            String observaciones,
            String iva,
            String FK_ID_Tipo_Movimiento,
            String FK_ID_Usuario,
            String motivoMovimiento,
            String FK_ID_DepartamentoPlantel,
            String FK_ID_PersonalPlantel,
            String ID_Traslado,
            String noTurno,
            String ID_Movimiento) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            SQLSentence = ""
                    + " UPDATE MOVIMIENTO SET "
                    + " FK_ID_Plantel = ?"
                    + " , folio = ?"
                    + " , fechaMovimiento = ?"
                    + " , fechaActualizacion = ?"
                    + " , noFactura = ?"
                    + " , noReferencia = ?"
                    + " , estatus = ?"
                    + " , observaciones = ?"
                    + " , iva = ?"
                    + " , FK_ID_Tipo_Movimiento = ?"
                    + " , FK_ID_Usuario = ?"
                    + " , motivoMovimiento = ?"
                    + " , noTurno = ?"
                    + " WHERE"
                    + " ID_Movimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, FK_ID_Plantel);
            pstmt.setString(2, folio);
            pstmt.setString(3, fechaMovimiento);
            pstmt.setString(4, fechaActualizacion);
            pstmt.setString(5, noFactura);
            pstmt.setString(6, noReferencia);
            pstmt.setString(7, estatus);
            pstmt.setString(8, observaciones);
            pstmt.setString(9, iva);
            pstmt.setString(10, FK_ID_Tipo_Movimiento);
            pstmt.setString(11, FK_ID_Usuario);
            pstmt.setString(12, motivoMovimiento);
            pstmt.setString(13, noTurno);
            pstmt.setString(14, ID_Movimiento);
            int rowCount = pstmt.executeUpdate();
            this.trans_update_Traslado(ID_Movimiento, FK_ID_DepartamentoPlantel, FK_ID_PersonalPlantel, fechaActualizacion, ID_Traslado, conn);
            conn.commit();
            tport = new Transporter(0, "El registro se actualizo correctamente.");
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Ocurrio un error al acualizar el registro.");
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return tport;
    }

    public final Transporter trans_update_Traslado(String FK_ID_Movimiento, String FK_ID_DepartamentoPlantel, String FK_ID_PersonalPlantel, String fechaActualizacion, String ID_Traslado, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE TRASLADO SET"
                + " FK_ID_Movimiento = ?"
                + " , FK_ID_Departamento_Plantel = ?"
                + " , FK_ID_personal_Plantel = ?"
                + " , fechaActualizacion = ?"
                + " WHERE ID_Traslado = ?";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Movimiento);
        pstmt.setString(2, FK_ID_DepartamentoPlantel);
        pstmt.setString(3, FK_ID_PersonalPlantel);
        pstmt.setString(4, fechaActualizacion);
        pstmt.setString(5, ID_Traslado);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_Grupo(
            String nombreGrupo,
            String ID_Grupo,
            String ID_PDG,
            String ID_DepartamentoPlantel,
            String ID_Plantel,
            LinkedList ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE GRUPO SET "
                    + " nombreGrupo = ? "
                    + " WHERE ID_Grupo = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, nombreGrupo);
            pstmt.setString(2, ID_Grupo);
            int rowCount = pstmt.executeUpdate();
            this.trans_update_RelacionPdg(ID_Grupo, ID_DepartamentoPlantel, ID_PDG, conn);
            Iterator t = ID_Bien.iterator();
            while (t.hasNext()) {
                LinkedList bien = (LinkedList) t.next();
                this.trans_update_DeptoBien(ID_DepartamentoPlantel, bien.get(0).toString(), ID_Plantel, conn);
            }
            conn.commit();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return tport;
    }

    public final Transporter trans_update_RelacionPdg(
            String FK_ID_Grupo,
            String FK_ID_Departamento_Plantel,
            String ID_RelacionPDG,
            Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE RELACION_PDG SET "
                + " FK_ID_Grupo = ? "
                + " , FK_ID_Departamento_Plantel = ? "
                + " WHERE ID_PDG = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Grupo);
        pstmt.setString(2, FK_ID_Departamento_Plantel);
        pstmt.setString(3, ID_RelacionPDG);
        int rowCount = pstmt.executeUpdate();
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_DeptoBien(String FK_ID_Departamento_Plantel, String ID_Bien, String ID_Plantel, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE BIEN SET "
                + " FK_ID_Departamento_Plantel = ? "
                + " , FK_ID_Plantel = ?"
                + " WHERE ID_Bien = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, FK_ID_Departamento_Plantel);
        pstmt.setString(2, ID_Plantel);
        pstmt.setString(3, ID_Bien);
        int rowCount = pstmt.executeUpdate();
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_RelacionPdg4DeptoPlanetl(
            String ID_PDG,
            String ID_DepartamentoPlantel,
            String ID_Plantel,
            LinkedList ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            SQLSentence = ""
                    + " UPDATE RELACION_PDG SET "
                    + " FK_ID_Departamento_Plantel = ? "
                    + " WHERE ID_PDG = ? ";
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_DepartamentoPlantel);
            pstmt.setString(2, ID_PDG);
            int rowCount = pstmt.executeUpdate();
            Iterator t = ID_Bien.iterator();
            while (t.hasNext()) {
                LinkedList bien = (LinkedList) t.next();
                this.trans_update_DeptoBien(ID_DepartamentoPlantel, bien.get(0).toString(), ID_Plantel, conn);
            }
            conn.commit();
            tport = new Transporter(0, "Filas afectadas: " + rowCount);
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            tport = new Transporter(1, "Error inesperado. " + ex.getMessage());
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return tport;
    }

    public final Transporter trans_update_ExitenciasConsumible(LinkedList consumible4Update, String ID_ConteoFisico) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            Iterator t = consumible4Update.iterator();
            while (t.hasNext()) {
                LinkedList datos = (LinkedList) t.next();
                String estatus = Double.parseDouble(datos.get(1).toString()) > 0 ? "Disponible" : "Agotado";
                SQLSentence = ""
                        + " UPDATE CONSUMIBLE SET"
                        + " total = ?"
                        + " , estatus = ?"
                        + " WHERE ID_Consumible = ?";

                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setString(1, datos.get(1).toString());
                pstmt.setString(2, estatus);
                pstmt.setString(3, datos.get(0).toString());
                int rowCount = pstmt.executeUpdate();
            }
            SQLSentence = " UPDATE CONTEOFISICO SET"
                    + " estatus = ?"
                    + " , fechaModificacion = ?"
                    + " WHERE"
                    + " ID_ConteoFisico = ?";
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "terminado");
            pstmt.setString(2, UTime.calendar2SQLDateFormat(Calendar.getInstance()));
            pstmt.setString(3, ID_ConteoFisico);
            int rowCount = pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_update_estatusPlantelAlmacen(
            LinkedList ID_PlantelAlmacen,
            String FK_ID_PlantelSurte,
            String estatus) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {
            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);

            SQLSentence = " UPDATE PLANTEL_ALMACEN SET"
                    + " estatus = ?"
                    + " WHERE"
                    + " FK_ID_PlantelSurte = ?";
            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, "0");
            pstmt.setString(2, FK_ID_PlantelSurte);
            int rowCount = pstmt.executeUpdate();
            for (int i = 0; i < ID_PlantelAlmacen.size(); i++) {

                SQLSentence = ""
                        + " UPDATE PLANTEL_ALMACEN SET"
                        + " estatus = ?"
                        + " WHERE ID_Plantel_Almacen = ?";

                pstmt = conn.prepareStatement(SQLSentence);
                pstmt.setQueryTimeout(statementTimeOut);
                pstmt.setString(1, estatus);
                pstmt.setString(2, ID_PlantelAlmacen.get(i).toString());
                rowCount = pstmt.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_update_OrdenSurtimiento(
            String asuntoGeneral,
            String fechaRequerida,
            String justificacion,
            String observaciones,
            String estatus,
            String fechaAtencion,
            LinkedList consumibles,
            String ID_Orden_Surtimiento) {

        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Transporter tport = null;
        try {

            SQLSentence = ""
                    + " UPDATE ORDEN_SURTIMIENTO SET"
                    + "  fechaRequerida = ?"
                    + " , justificacion = ?"
                    + " , observaciones = ?"
                    + " , estatus = ?"
                    + " , fechaAtencion = ?"
                    + " , asuntoGeneral = ?"
                    + " WHERE ID_Orden_Surtimiento = ?";

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQLSentence, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, fechaRequerida);
            pstmt.setString(2, justificacion);
            pstmt.setString(3, observaciones);
            pstmt.setString(4, estatus);
            pstmt.setString(5, fechaAtencion);
            pstmt.setString(6, asuntoGeneral);
            pstmt.setString(7, ID_Orden_Surtimiento);

            int rowCount = pstmt.executeUpdate();

            SQLSentence = ""
                    + " DELETE FROM ORDEN_SURTIMIENTO_CONSUMIBLE"
                    + " WHERE FK_ID_OrdenSurtimiento = ?";

            pstmt = conn.prepareStatement(SQLSentence);
            pstmt.setQueryTimeout(statementTimeOut);
            pstmt.setString(1, ID_Orden_Surtimiento);
            rowCount = pstmt.executeUpdate();

            Iterator t = consumibles.iterator();
            while (t.hasNext()) {
                LinkedList datos = (LinkedList) t.next();
                this.trans_insert_OrdenSurtimiento_Consumible(
                        ID_Orden_Surtimiento,
                        datos.get(0).toString(),
                        datos.get(1).toString(),
                        "0",
                        "", conn);
            }

            conn.commit();
            tport = new Transporter(0, "");
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }

    public final Transporter trans_update_OrdenSurtimiento_Consumible(
            String cantidadSurtida,
            String observaciones,
            String ID_Consumible,
            String total,
            String estatus,
            String ID_OrdenSurtimientoConsumible,
            Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE ORDEN_SURTIMIENTO_CONSUMIBLE SET "
                + " cantidadSurtida = ? "
                + " , observaciones = ? "
                + " WHERE ID_Orden_Surtimiento_Consumible = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, cantidadSurtida);
        pstmt.setString(2, observaciones);
        pstmt.setString(3, ID_OrdenSurtimientoConsumible);
        int rowCount = pstmt.executeUpdate();

        SQLSentence = ""
                + " UPDATE CONSUMIBLE SET "
                + " total = ? "
                + " , estatus = ? "
                + " WHERE ID_Consumible = ? ";
        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, total);
        pstmt.setString(2, estatus);
        pstmt.setString(3, ID_Consumible);
        rowCount = pstmt.executeUpdate();

        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_CuentaTotalMovimiento(String ID_Tipo_Movimiento, String cuentaTotal, Connection conn) throws SQLException {
        Transporter tport = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        SQLSentence = ""
                + " UPDATE TIPO_MOVIMIENTO SET "
                + " cuentaTotalMovimiento = ? "
                + " WHERE ID_Tipo_Movimiento = ? ";

        pstmt = conn.prepareStatement(SQLSentence);
        pstmt.setQueryTimeout(statementTimeOut);
        pstmt.setString(1, cuentaTotal);
        pstmt.setString(2, ID_Tipo_Movimiento);
        int rowCount = pstmt.executeUpdate();
        endConnection(pstmt);
        tport = new Transporter(0, "Filas afectadas: " + rowCount);
        return tport;
    }

    public final Transporter trans_update_SetBajaBienDeleteFromAnyGroup(String ID_Bien) {
        Transporter tport = null;
        JSpreadConnectionPool jscp = null;
        Connection conn = null;
        String SQLSentence = null;
        PreparedStatement pstmt = null;

        try {

            jscp = JSpreadConnectionPool.getSingleInstance();
            conn = jscp.getConnectionFromPool();
            conn.setAutoCommit(false);

            this.trans_delete_GrupoBien(ID_Bien, conn);
            this.trans_update_Status4Bien("BAJA", ID_Bien, conn);
            conn.commit();
            conn.setAutoCommit(true);
            endConnection(jscp, conn, pstmt);
            tport = new Transporter(0, "Operación Correcta");
        } catch (Exception ex) {
            try {
                conn.rollback();
                endConnection(jscp, conn, pstmt);
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(QUID.class.getName()).log(Level.SEVERE, null, ex1);
                tport = new Transporter(1, "Error inesperado." + ex.getMessage());
            }
        }
        return tport;
    }
    //</editor-fold>
}
