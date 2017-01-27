package systemSettings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.NoSuchProviderException;
import javax.servlet.http.HttpServletResponse;
import jspread.core.db.JSpreadConnectionPool;
import jspread.core.db.QUID;
import jspread.core.util.PageParameters;
import jspread.core.util.SendMailUtil;
import jspread.core.util.SpreadParameters;

/**
 * @author VIOX
 */
public final class SystemSettings {

    private static final String version = "V0.4";
    private static boolean isBurning = false;

    //public static synchronized void ignition() throws Exception {
    public static void ignition() throws Exception {
        if (!isBurning) {
            try {
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //parametros de clases para el spread
                SpreadParameters.getSingleInstance();
                SpreadParameters.addParameter("CustomCode", "CostumSnippet.CustomCode");
                SpreadParameters.addParameter("core", "jspread.core");
                SpreadParameters.addParameter("Executor", "jspread.core.Executor");
                SpreadParameters.addParameter("WebUtil", "spread.core.util.WebUtil");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //Aqui van los links y los parametros de pagina
                PageParameters.getSingleInstance();
                PageParameters.addParameter("appName", "vento");
                PageParameters.addParameter("controllerName", "controller");
                PageParameters.addParameter("mainContext", "/" + PageParameters.getParameter("appName"));
                //PageParameters.addParanmeter("charset", "UTF-8");
                PageParameters.addParameter("charset", "utf-8");
                System.setProperty("file.encoding", "UTF8");//-Dfile.encoding=UTF8 en la jvm
                PageParameters.addParameter("servletSetContentType", "text/html;charset=UTF-8");
                PageParameters.addParameter("Content-Language", "es-mx");
                PageParameters.addParameter("faviconType", "image/x-icon"); // image/png  image/jpg
                PageParameters.addParameter("exitmessageask", "Estas a punto de salir de la pagina deseas hacerlo???");
                PageParameters.addParameter("cssType", "text/css"); // text/css

                PageParameters.addParameter("encodeParameters", "true");
                //PageParameters.addParameter("encodeParameters", "false");
                PageParameters.addParameter("encodeParametersSemilla", "parangatirimicuaro");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                
//include definitions

                //pageComoponents URL
                PageParameters.addParameter("pageComponents", "/gui/pageComponents");

                //globalLibs URL
                PageParameters.addParameter("globalLibs", PageParameters.getParameter("pageComponents") + "/globalLibsInstitucional.jsp");

                //header URL
                PageParameters.addParameter("header", PageParameters.getParameter("pageComponents") + "/globalLibsInstitucional.jsp");

                //logo URL
                PageParameters.addParameter("logo", PageParameters.getParameter("pageComponents") + "/logoInstitucional.jsp");

                //barMenu URL
                PageParameters.addParameter("barMenu", PageParameters.getParameter("pageComponents") + "/barMenu.jsp");

                //footer URL
                PageParameters.addParameter("footer", PageParameters.getParameter("pageComponents") + "/footerInstitucional.jsp");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                
//paginas del sistema                
                //MainController URL
                PageParameters.addParameter("mainController", PageParameters.getParameter("mainContext") + "/" + PageParameters.getParameter("controllerName"));

                //interface URL
                //PageParameters.addParameter("gui", PageParameters.getParameter("mainContext") + "/gui");
                PageParameters.addParameter("gui", "/gui");
                PageParameters.addParameter("scanedFiles", PageParameters.getParameter("mainContext") + "/scanedFiles");

                //MainMenu URL
                PageParameters.addParameter("mainMenu", PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/MainMenu.jsp");

                //MainMenu ServLet
                PageParameters.addParameter("mainMenuServLet", PageParameters.getParameter("gui") + "/MainMenu.jsp");

                //acceso ilegal URL
                PageParameters.addParameter("illegalAttempt", PageParameters.getParameter("mainContext") + "/JSpread/JSPTemplates/BlankSpreadTemplateUserIllegalAttempt.jsp");

                //error URL
                //PageParameters.addParameter("errorURL", PageParameters.getParameter("mainContext") + "http://www.google.com/ncr");
                PageParameters.addParameter("errorURL", "http://www.google.com/ncr");

                //LogInPage URL
                PageParameters.addParameter("LogInPage", PageParameters.getParameter("gui") + "/LogIn.jsp");

                //index URL
                PageParameters.addParameter("index", PageParameters.getParameter("mainContext") + "/index.jsp");

                //AJAXFunctions URL
                PageParameters.addParameter("ajaxFunctions", "/ajaxFunctions");

                //msgUtil URL
                PageParameters.addParameter("msgUtil", PageParameters.getParameter("ajaxFunctions") + "/msgUtil");

                //reports URL
                PageParameters.addParameter("reports", PageParameters.getParameter("gui") + "/reports");

                //prints URL
                //los reportes de impresion estan basados en el navegador firefox v23, sin margenes, en hoja carta, impresoras de inyeccion de tinta y sin la opcion de imprimir fondo
                PageParameters.addParameter("prints", PageParameters.getParameter("gui") + "/prints");

                //excel reports URL
                PageParameters.addParameter("excelReports", PageParameters.getParameter("reports") + "/aExcel");

                //Pagina en mantenimiento
                PageParameters.addParameter("SiteOnMaintenance", "false");
                PageParameters.addParameter("SiteOnMaintenanceURL", PageParameters.getParameter("mainContext") + PageParameters.getParameter("gui") + "/SiteOnMaintenance.jsp");
                PageParameters.addParameter("msgOnMaintenance", "Sitio en mantenimiento, intente nuevamente en 10 minutos, disculpe las molestias.");
                PageParameters.addParameter("emptyField", "Este campo no puede estar vacio");

                //Notificacion
                PageParameters.addParameter("comunicado", "");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                
//recursos del sistema
                //resources rsc URL
                PageParameters.addParameter("rsc", PageParameters.getParameter("mainContext") + "/rsc");

                //jq resources rsc URL 
                PageParameters.addParameter("jqRsc", PageParameters.getParameter("rsc") + "/jq");

                //css resources rsc URL
                PageParameters.addParameter("cssRsc", PageParameters.getParameter("rsc") + "/css");

                //img resources rsc URL
                PageParameters.addParameter("imgRsc", PageParameters.getParameter("rsc") + "/img");

                //external resources xtrsc URL
                PageParameters.addParameter("xtRsc", PageParameters.getParameter("mainContext") + "/xtrsc");

                //javascript resources URL
                PageParameters.addParameter("jsRcs", PageParameters.getParameter("rsc") + "/js");

                //fuentes URL
                PageParameters.addParameter("fontsPath", PageParameters.getParameter("rsc") + "/fonts");

                //ajax URL
                PageParameters.addParameter("ajax", PageParameters.getParameter("jsRcs") + "/ajax.js");

                //jquery URL
                // para jalarlo desde internet http://code.jquery.com/jquery-latest.js
                //direccion de google apis https://developers.google.com/speed/libraries/devguide
                //PageParameters.addParameter("JQueryLink", PageParameters.getParameter("jqRsc") + "/jquery-1.8.2_google_ajax.js");
                //PageParameters.addParameter("JQueryLink", PageParameters.getParameter("jqRsc") + "/jquery-1.7.2.js"); 
                PageParameters.addParameter("JQueryLink", PageParameters.getParameter("jqRsc") + "/jquery-2.0.3_google_ajax.js");

                //jqCalendar Funcion URL
                PageParameters.addParameter("jqCalendar", PageParameters.getParameter("jqRsc") + "/jqCalendar");

                //jqMsgBox - requiere licencia
                PageParameters.addParameter("jqMsgBox", PageParameters.getParameter("mainContext") + "/JSpread/JS/jqMsgBox");
                PageParameters.addParameter("jqAlerts", PageParameters.getParameter("mainContext") + "/JSpread/JS/jqAlerts");

                //google bar
                PageParameters.addParameter("googleBarScript", PageParameters.getParameter("cssRsc") + "/google_bar/jquery.google_menu.js");
                PageParameters.addParameter("googleBarCSS", PageParameters.getParameter("cssRsc") + "/google_bar/google_menu.css");

                //jqDataTables URL
                PageParameters.addParameter("jqDataTables", PageParameters.getParameter("jqRsc") + "/jqDataTables");

                //datePicker URL
                PageParameters.addParameter("datePicker", PageParameters.getParameter("jqRsc") + "/date-picker");

                //javaCaptcha Funcion URL
                PageParameters.addParameter("javacaptcha", PageParameters.getParameter("mainContext") + "/jspread/javacaptcha/javacaptcha.jsp");

                //jspBarcode Funcion URL
                PageParameters.addParameter("jspBarcode", PageParameters.getParameter("mainContext") + "/jspread/jspBarcode/jspBarcode.jsp");

                //favicon URL
                PageParameters.addParameter("faviconLink", PageParameters.getParameter("imgRsc") + "/ico/control_escolar.ico"); //http://www.wikipedia.org/favicon.ico
                //PageParameters.addParameter("uploadArchivo", PageParameters.getParameter("mainContext") + "/uploadArchivo");
                //PageParameters.addParameter("leerArchivo", PageParameters.getParameter("mainContext") + "/leerArchivo");
                //styleFormCorrections URL
                PageParameters.addParameter("styleFormCorrections", PageParameters.getParameter("pageComponents") + "/styleFormCorrections.jsp");

                //dataTablesFullFunctionParameters URL
                PageParameters.addParameter("dataTablesFullFunctionParameters", PageParameters.getParameter("pageComponents") + "/dataTablesFullFunctionParameters.jsp");

                //dataTablesBasicFunctionParameters URL
                PageParameters.addParameter("dataTablesBasicFunctionParameters", PageParameters.getParameter("pageComponents") + "/dataTablesBasicFunctionParameters.jsp");

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //Parametros del sistema
                PageParameters.addParameter("leyendaoficio", "2015. Año del Bicentenario Luctuoso de José María Morelos y Pavón.");
                PageParameters.addParameter("revision", "75");

                PageParameters.addParameter("folderDocs", "c:\\sibien_Files\\files\\");//carpeta donde el sistema guarda archivos, sino existe se crea automaticamente 
                PageParameters.addParameter("folderMovs", "c:\\sibien_Files\\movimientos\\");//carpeta donde el sistema guarda archivos, sino existe se crea automaticamente
                //PageParameters.addParameter("folderMovs", "/home/Corden/sibien_Files/movimientos/");//carpeta donde el sistema guarda archivos, sino existe se crea automaticamente
                //PageParameters.addParameter("folderDocs", "/home/v10x/sibien_Files/");//carpeta donde el sistema guarda archivos, sino existe se crea automaticamente 
                PageParameters.addParameter("folderLogs", "c:\\sibien_Files\\log\\");
                //PageParameters.addParameter("maxSizeToUpload", "130971520");//en bytes (115MB)
                PageParameters.addParameter("maxSizeToUpload", "1574864");//Limitamos mas el espacio en bytes (1.5MB)
                PageParameters.addParameter("deleteFileOnModifyBD", "1");//Elimina los archivos adjuntos en el servidor
                //Indica con que extensiones trabaja el sistema banned o permited, si es baned el sistema permite subir cualquier extension que no este baneada
                // si el parametro es permited, el sistema permite solo las extensiones permitidas indcadas en la clase SystemUtil
                //PageParameters.addParameter("workExtensions", "banned");
                PageParameters.addParameter("workExtensions", "permited");
                PageParameters.addParameter("timeOutToUploadFile", "0");// tiempo de espera para poder subir un archivo en milis (11min)
                PageParameters.addParameter("fileSizeLimited", "1");//Indica si se limita el tamaño de los archivos que son subidos al servidor 1=si 0=no
                PageParameters.addParameter("inicioAnioContable", "2016-01-01");
                PageParameters.addParameter("finAnioContable", "2016-12-31");
                PageParameters.addParameter("testDBLabel", "");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //Database parameters
                try {
                    JSpreadConnectionPool jscp = JSpreadConnectionPool.getSingleInstance();
                    jscp.setCloseNOpen(true);
                    jscp.setIsolationLevel(Connection.TRANSACTION_SERIALIZABLE);
                    jscp.setClassforname("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    jscp.setProtocol("jdbc");
                    jscp.setDBMS("sqlserver");
                    //jscp.setDBMS("sqlserverSSL");

                    /**
                     * PRUEBAS
                     */
                 
                    PageParameters.addParameter("DBDir", "172.16.1.13");
                    jscp.setHost("172.16.1.13");
                    jscp.setPort(1433);
                    jscp.setSchema("SIBIEN");
                    jscp.setUser("loginSBN");
                    jscp.setPassword("Matrix_Reload2016");
                    
                    /**
                     * *******PRODUCCION*********
                     */
//                    PageParameters.addParameter("DBDir", "172.16.1.2");
//                    jscp.setHost("172.16.1.2");
//                    jscp.setPort(1433);
//                    jscp.setSchema("SIBIEN");
//                    jscp.setUser("loginSBN");
//                    jscp.setPassword("Matrix_Reload2016");                    

                    
                    jscp.setMAX_POOL_SIZE(3);
                    jscp.createDatabaseURL();

                    /**
                     * ***********************CONECXION A
                     * ORACLE****************************************
                     */
//                    JSpreadConnectionPool jscp = JSpreadConnectionPool.getSingleInstance();
//                    jscp.setCloseNOpen(true);
//                    jscp.setIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
//                    jscp.setClassforname("oracle.jdbc.driver.OracleDriver");
//                    jscp.setProtocol("jdbc");
//                    jscp.setDBMS("oracle");
//                    jscp.setHost("172.16.2.215");
//                    jscp.setPort(1521);
//                    jscp.setSchema("KOOB");
//                    jscp.setUser("SIBIEN");
//                    jscp.setPassword("123456");
//                    jscp.setHost("172.16.1.2");
//                    jscp.setPort(1521);
//                    jscp.setSchema("KOOB");
//                    jscp.setUser("SIBIEN");
//                    jscp.setPassword("CexCy73m51813n");
//                    jscp.setMAX_POOL_SIZE(1);
//                    jscp.createDatabaseURL();
//*************************************************************************************************/
                    System.out.println("Direccion de la base de datos: " + jscp.getDBURL());
                    jscp.initialize();
                    if (!jscp.getHost().equalsIgnoreCase(PageParameters.getParameter("DBDir"))) {
                        PageParameters.addParameter("testDBLabel", " --Base de pruebas");
                    }
                } catch (Exception ex) {
                    DBError(ex);
                }

                /* try {
                 JSpreadConnectionPool jscp = JSpreadConnectionPool.getSingleInstance();
                 jscp.setClassforname("sun.jdbc.odbc.JdbcOdbcDriver");
                 jscp.setProtocol("jdbc:odbc");
                 jscp.setDBMS("Driver={Microsoft Access Driver (*.mdb)};");
                 jscp.setHost("");
                 jscp.setPort(0);
                 jscp.setSchema("DBQ=C:\\C.mdb;");
                 jscp.setUser("");
                 jscp.setPassword("7km!8thm@28$%");
                 jscp.setMAX_POOL_SIZE(5);
                 jscp.initialize();
                 System.out.println("Direccion de la base de datos: " + jscp.getDBURL());


                 } catch (Exception e) {

                 DBError(e);
                 }*/
                //Connection to MS ACCESS DBMS
//                JSpreadConnectionPool jscp = JSpreadConnectionPool.getSingleInstance();
//                jscp.setClassforname("sun.jdbc.odbc.JdbcOdbcDriver");
//                jscp.setProtocol("jdbc:odbc");
//                jscp.setDBMS("Driver={Microsoft Access Driver (*.mdb)};");
//                jscp.setHost("");
//                jscp.setPort(0);
//                jscp.setSchema("DBQ=C:\\C.mdb;");
//                jscp.setUser("Admin");
//                jscp.setPassword("7km!8thm@28$%");
//                jscp.setMAX_POOL_SIZE(5);
//                jscp.initialize();
//                System.out.println("Direccion de la base de datos: " + jscp.getDBURL());
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Iniciar Base de datos
                String logSchema = "SecLog";
                logSchema = PageParameters.getParameter("appName") + logSchema;

                //OJO esto puede causar problemas con el tomcat al momento de reiniciar eo a paragar el servidor de aplicaciones
//                if (!H2Service.reStartService(logHost, logPort, logBaseDir, logSchema, tcpPassword)) {
//                    throw new SQLException("Log Database has a problem");
//                };
                //LOG Database parameters
//                try {
//                    JSpreadConnectionPool logDB = new JSpreadConnectionPool();
//                    logDB.setClassforname("org.h2.Driver");
//                    //logDB.setConnectionType("Embedded");
//                    logDB.setConnectionType("tcp");
//                    //logDB.setConnectionType("ssl");
//                    logDB.setProtocol("jdbc");
//                    logDB.setDBMS("h2");
//                    logDB.setHost("172.16.1.2");
//                    //logDB.setHost("189.254.108.19");
//                    logDB.setPort(9091);
//                    logDB.setSchema(logSchema);
//                    logDB.setUser("God");
//                    logDB.setPassword("579C-e2430aRT");
//                    logDB.setMAX_POOL_SIZE(1);
//                    logDB.createDatabaseURL();
//                    System.out.println("Direccion de la base de datos del LOG: " + logDB.getDBURL());
//                    logDB.initialize();
//                    PageParameters.addParameter("LogBase", logDB);
//
//                    QUID quid = new QUID();
//                    if (!quid.existLogTable(logDB)) {
//                        System.out.println("The log table dosen't exist, proceed to creation");
//                        if (!quid.createLogTable(logDB)) {
//                            System.out.println("The log table can't be create");
//                            throw new Exception("The log table can't be create");
//                        }
//                    } else {
//                        System.out.println("Log table already exist");
//                    }
//
//                } catch (Exception ex) {
//                    DBError(ex);
//                }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//mail configurations
                PageParameters.addParameter("mail.transport.protocol", "smtp");
                PageParameters.addParameter("mail.smtp.starttls.enable", "true");
                PageParameters.addParameter("mail.smtp.host", "smtp.gmail.com");
                PageParameters.addParameter("mail.smtp.port", "587");
                PageParameters.addParameter("mail.smtp.auth", "true");
                PageParameters.addParameter("mail.smtp.user", "cecytem.v.urrutia@gmail.com");
                PageParameters.addParameter("mail.smtp.password", "password");
                try {
                    SendMailUtil.initialize();
                } catch (NoSuchProviderException ex) {
                    ex.printStackTrace();
                }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                isBurning = true;
            } catch (Exception ex) {
                Logger.getLogger(SystemSettings.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception(ex);
            }
        }
    }

//    public static synchronized void shutdown() {
//        try {
//            JSpreadConnectionPool jscp = JSpreadConnectionPool.getSingleInstance();
//            jscp.closePool();
//            JSpreadConnectionPool logDB = JSpreadConnectionPool.getSingleInstance();
//            logDB.closePool();
//            PageParameters.getSingleInstance();
//            PageParameters.clearParameters();
//            SpreadParameters.getSingleInstance();
//            SpreadParameters.clearParameters();
//            H2Service.stopService(logHost, logPort, logBaseDir, logSchema, tcpPassword);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(SystemSettings.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(SystemSettings.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static synchronized void stopLogService() {
//        H2Service.stopService(logHost, logPort, logBaseDir, logSchema, tcpPassword);
//    }
    private static void DBError(Exception ex) throws Exception {
        System.out.println("\n\n\n\n");
        System.out.println("El sisguiente error de base de datos se puede resolver verificando que:");
        System.out.println("-Que el protocolo y el DBMS ya este programado en el jspread (driver)");
        System.out.println("-Que la direccion URL al host y DBMS que te quieres conectar sea la correcta");
        System.out.println("-Usuario y contraseña sean correctas");
        System.out.println("-Que se tenga ping al host donde esta la base de datos");
        System.out.println("-La red en donde se encuentre el cliente no bloque los puestos hacia la base datos");
        System.out.println("-La red en donde se encuentra el host de la base este configurado el firewall tanto de host como el de la propia red");
        System.out.println("-Verificar el puerto de la base datos sea el correcto");
        System.out.println("-Que el servicio de la base de datos este arriba");
        System.out.println("-Que la base datos exista en el host, asi tambien con el path de las mismas");
        System.out.println("\n");
        throw new Exception(ex);
    }

    public static void setHeaders(HttpServletResponse response) throws Exception {
        //System.out.println("Configurando headres");
        /*      
         response.setContentType("text/javascript");
         response.setHeader("Last-Modified", "Mon, 15 Feb 2010 23:30:12 GMT");
         response.setHeader("Date", "Tue, 28 Sep 2010 19:45:24 GMT");
         response.setHeader("Expires", "Wed, 28 Sep 2021 19:45:24 GMT");
         response.setHeader("Vary", "Accept-Encoding");
         response.setHeader("X-Content-Type-Options", "nosniff");
         response.setHeader("Cache-Control", "public, max-age=31536000");
         response.setHeader("Age", "36");
         */
//        response.addHeader("Last-Modified", "Mon, 16 Feb 2010 23:30:12 GMT");
//        response.addHeader("Vary", "Accept-Encoding");
//        response.addHeader("Content-Type", "text/html");
//        response.addHeader("X-Content-Type-Options", "nosniff");
        //response.addHeader("Cache-Control", "max-age=20, must-revalidate, s-maxage=12000000, public, private");
//        response.addHeader("Pragma", "no-cache");
        //expirar paginas con parametros
//        response.setHeader("Cache-Control", "no-cache");
//        response.setHeader("Expires", "0");
//        response.setHeader("Pragma", "No-cache");
//        response.addHeader("Cache-control", "no-store");
//        response.addHeader("Cache-control", "max-age=0");
        //No cache
        //response.setHeader("Vary", "Accept-Encoding");
        //response.setHeader("Pragma", "no-cache");
        //response.setHeader("Cache-Control", "no-cache");
        //response.setDateHeader("Expires", 0);
        //response.addHeader("Cache-control", "max-age=1200000");
    }
}
