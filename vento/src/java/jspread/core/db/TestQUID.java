package jspread.core.db;

import java.util.Iterator;
import java.util.LinkedList;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jspread.core.db.DeveloperQUID;
import jspread.core.db.util.PreparedStatementSQL;
import jspread.core.util.security.JHash;
import systemSettings.SystemSettings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jspread.core.models.Transporter;
import jspread.core.util.SystemUtil;
import jspread.core.util.UTime;

/**
 *
 * @author Hewlet
 */
public class TestQUID {
    
    
    
    

    public static void main(String[] args) {
        try {

            SystemSettings.ignition();
            QUID kit = new QUID();

//            DeveloperQUID dquid = new DeveloperQUID("sqlserver");
//            PreparedStatementSQL ps = new PreparedStatementSQL();
//            String StrTest = ps.getSQLPreparedStatement(dquid, "sqlserver", "DELETE", "CECYTEMNG.DBO.RELACION_AGP", "WHERE ID_RELACION_AGP = ?");
//            System.out.println(StrTest);
//            PreparedStatementSQL PSSQL = new PreparedStatementSQL();
//            LinkedList ListFields = null;
//            LinkedList listAux = null;
//            Transporter Trans = null;
//            //String StrTest = TranslatorSQL.getSQLTranslated("INSERT", "Xend.tabletest", "WHERE ID_Cecyte = 1 AND ID_Cecyte = 3");
//            String StrTest = PSSQL.getSQLPreparedStatement(dquid, "mysql", "SELECT", "psa.cat_academias", "");
//            System.out.println(StrTest);//WHERE id = ?
            //Trans = quid.insert_tabletest(3, "Jelipe_3", "Perez_3", 900, null, "Gelipe", (byte) 25,"Descripcion de un nuevo Gelipe");
            //WHERE id = ?
            //Trans = quid.update_tabletest(3, "Jelipe_3", "Perez_3", 900, "31-08-2012", "Gelipe", (byte) 23, "Descripcion de uno nuevo");
            //System.out.println(Trans.getMsg());
            //Trans = quid.delete_tabletest(2);
            //System.out.println(Trans.getMsg());
//            ListFields = dquid.select_psa_cat_academias();
//            Iterator it = ListFields.iterator();
//
//            while (it.hasNext()) {
//                listAux = (LinkedList) it.next(); //String Aux
//                System.out.println("\nDato1: " + listAux.get(0));
//                System.out.println("Dato2: " + listAux.get(1));
//                System.out.println("Dato3: " + listAux.get(2));
//                //System.out.println("Dato4: " + listAux.get(3));
//            }

            /*
             LinkedList subordinadas = new LinkedList();
             LinkedList proyectos = new LinkedList();
             LinkedList idProyectos = new LinkedList();
             LinkedList actividades = new LinkedList();
             int area = 12;
             dquid.select_segpro_todosSubordinados(area, subordinadas);
             dquid.select_segpro_proyectos(subordinadas, proyectos);
             dquid.select_segpro_idProyectos(subordinadas, idProyectos);

             System.out.println("" + subordinadas);

             Iterator it = proyectos.iterator();
             Iterator auxIt;
             while (it.hasNext()) {
             auxIt = ((LinkedList) it.next()).iterator();
             System.out.println("\n");
             while (auxIt.hasNext()) {
             System.out.println("" + auxIt.next());
             }
             System.out.println("\n");
             }

             it = null;
             it = idProyectos.iterator();
             int cont = 0;
             while (it.hasNext()) {
             cont = Integer.parseInt(it.next().toString());
             System.out.println("id: " + cont);
             dquid.select_segpro_historiaactividades(cont, actividades);

             }

             it = actividades.iterator();
             while (it.hasNext()) {
             auxIt = ((LinkedList) it.next()).iterator();
             System.out.println("\n");
             while (auxIt.hasNext()) {
             System.out.println("" + auxIt.next());
             }
             System.out.println("\n");
             }


             */
//
//            kit.update_Alumno("MARCO","LOPZ","OTAVIANO",Byte.parseByte("18"), 
//                    "LOOM861017hmcpcr","1986-10-18","EL rosal","7121336539","7121346540",
//                    "987654321",
//                    "74050", 
//                    1, 15, 
//                    1, "corpmalo@gmail.com","MARIO LOPEZ G.", true, 
//                    1, 8.3, 
//                    Byte.parseByte("5"),
//                    //"2013-07-30",
//                    //"0000-00-00",
//                    //"2013/2014",
//                    Byte.parseByte("1"), 
//                    2, true, 3, 
//                    //"xyz9345",
//                    "666", "999",
//                    "DOm conocido","Ixtlahuaca","entre el rosal", "y privada orta","frente a cerveceria",
//                    //"20101504789",
//                    "Ninguna solo gripa", 
//                    //"C",
//                    "35");
//            LinkedList l=null;
//            Iterator t=kit.select_AlumnoXNoControl("20101504789").iterator();
//            while(t.hasNext()){
//                l=(LinkedList)t.next();
//                for(int i=0;i<l.size();i++){
//                 System.out.println("param("+i+"):"+l.get(i));
//                }
//                
//            }
            //System.out.println("clave:"+kit.getClavePlantel("2"));
            // System.out.println("Tu nuevo número de control es: " + kit.getGenerateNoControl("2","264", "4", "15", "08"));
//            
//            Iterator t=kit.getDocumentsNames(kit.select_Documento("35").iterator()).iterator();
//            LinkedList l=null;
//            t=kit.select_DocumentoWithNames("39").iterator();
//            while(t.hasNext()){
//                l=(LinkedList)t.next();
//                
//                 System.out.println("id:"+l.get(0)+" tipo:"+l.get(1)+"seemstre:"+l.get(2)+"Tdoc"+l.get(3));
//                
//                
//            }
//           for(Integer i=1;i<9;i++){
//             System.out.println("exis("+i+")="+kit.exitsDocument("35",i.toString()));
//           }
            //  kit.delete_Documento("35","1");
//            String[] docs= {"8","3","6","7","1"};
//            
////            kit.update_Documentos(docs, "35");
//            Iterator t=kit.select_Alumno_Grupo().iterator();
//            int reg=0;
//            LinkedList l=null;
//            while(t.hasNext()){
//                l=(LinkedList) t.next();
//                reg+=1;
//                for(int i=0;i<l.size();i++){
//                System.out.println("REG"+reg+" campo("+i+")="+l.get(i));
//                }
//            }
            //System.out.println("total alumnos:"+kit.insert_AlumnoGrupo("39","3","R","2013-07-31"));
            //System.out.println("especialidad:"+kit.select_EspecialidadGrupo("2"));
//            for(int i=0; i<45;i++){
//                kit.insert_AlumnoGrupo("41", "19","Y", "2013/08/06");
//            }
            //kit.insert_Usuario("admin","xyz","admin@yahoo.com","administrador",Short.parseShort("1"));
            //kit.update_Usuario("administrator","xyz9345","administrat@yahoo.com","administrador root",Short.parseShort("2"),"3");
            //kit.delete_Usuario("2");
//            Iterator t;
//            LinkedList l=kit.select_Usuario("3");
//            t=kit.select_Usuario("3").iterator();
//            //while(t.hasNext()){
//              //  l=(LinkedList)t.next();
//                
//                 System.out.println("id:"+l.get(0).toString()+" tipo:"+l.get(1)+"seemstre:"+l.get(2)+"Tdoc"+l.get(3)+"Tdoc");
//                
//                
//            //}
            // System.out.println("id:"+kit.select_IDUsuario("tere45","teresita juarez","11"));
            //System.out.println("pass:"+ kit.select_PasswordQSL("kvz%*+9347"));
            //System.out.println("pass "+JHash.getStringMessageDigest("123",JHash.MD5));
//            LinkedList l=kit.select_PlantelUsuario("19");
//            System.out.println("id:"+l.get(0));
//            System.out.println("pltl:"+l.get(1));
//            
            //System.out.println("no:" + kit.select_AlumnoXCampo("nombreTutor", "1"));
            //kit.update_SituacionPersonalPlantel("in","2","1");
            //kit.insert_Actividad("actividad de prueba");
            //kit.insert_CalendarioEscolar("5", "2013-01-01", "2013-06-06", "2013/2014","2");
//            Iterator l=kit.select_ActividadCalendarioEscolarLike("EVALUACIÓN", "1", "2013-2014").iterator();
//              
//            while(l.hasNext()){
//               LinkedList t=(LinkedList)l.next();
//                System.out.println("fecha i:"+t.get(3)+" fecha F:"+t.get(4));
//            }
            //System.out.println("asistencia: "+kit.calcularPorcentajeAsistencia("5","455","2013-2014"));
            //System.out.println("% asistenci eval: "+kit.get.getPorcentajeAsistenciaXEvaluacion("455","4", "1042"));
//            String[] evaluaciones = {"P1", "p2", "p3"};
//            String cadena = "";
//            for (int i = 0; i < evaluaciones.length; i++) {
//                cadena += " E.abreviacion = " + evaluaciones[i] ;
//                if(i<evaluaciones.length-1){
//                    cadena+=" OR ";
//                }
//            }
////            System.out.println(cadena);
//            LinkedList ids=kit.select_PlantelIDs();
//            LinkedList personal=kit.select_PersonalProvicional();
//            if(personal!=null && personal.size()>0){
//            Integer idPersonal=Integer.parseInt(personal.get(0).toString());
//            for (int i = 0; i < ids.size(); i++) {
//                kit.insert_PersonalPlantel(
//                        idPersonal,
//                        Integer.parseInt(ids.get(i).toString()),
//                        "ACTIVO",
//                        "2013-11-20",
//                        "PROFESOR PROVICIONAL",
//                        "SIEMPRE",
//                        "REGISTRO PARA PERMITIR CLASES SIN PROFESOR");
//            }
//            }
            //System.out.println("in: "+kit.select_ActividadCalendarioEscolarXActividad("2013-2014","50","INICIO DE CURSOS"));
            /**
             * ***corregir bd
             */
             
//            String[] ids = {
//                "13415082040548"};
//            int sucess = 0;
//            for (int i = 0; i < ids.length; i++) {
//                Transporter tport = kit.trans_update_CorregirBD(ids[i]);
//                if (tport.getCode() == 0) {
//                    sucess += 1;
//                }
//                System.out.println("-----\nCodigo: " + tport.getCode());
//                System.out.println("Mensaje: " + tport.getMsg());
//            }
//                     LinkedList t=kit.select_Usuarios();
//            System.out.println("data: "+t.toString());
            
            //for (int i = 0; i < 1; i++) {
            //kit.speedTest();
            //}
            ///System.out.println(JHash.getStringMessageDigest("admin", JHash.MD5));            
            //System.out.println( JHash.getStringMessageDigest("admin", JHash.MD5));
            
            System.out.println(JHash.getStringMessageDigest("admin", JHash.MD5));
        } catch (Exception ex) {
            System.out.println("Hubo un error: " + ex);
        }

    }
}
