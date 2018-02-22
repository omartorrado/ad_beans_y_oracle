/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ad_beans_y_oracle;

import beans.Producto;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author oracle
 */
public class Ad_beans_y_oracle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Cargamos la clase de control de la db
        BaseDatos db= new BaseDatos("jdbc:oracle:thin:","hr","hr","localhost","1521","orcl");
        db.setCrearConexion();
        //pruebas
        /*
        ArrayList<Producto> al=db.consultaPro("select * from productos");
        int test=db.obterUltimoID("pedidos");//Al estar vacia devuelve un 0, -1 en caso de error
        int test2=db.obterUltimoID("productos");
        System.out.println(test+","+test2);
        System.out.println(getCurrentDate());
        */
        crearVenta(db,1,20);
        
        
    }
    
    private static void verPedidos(BaseDatos db){
        
    }
    
    private static void verProductos(BaseDatos db){
        
    }
    
    private static void verVentas(BaseDatos db){
        
    }
    
    private static java.sql.Date getCurrentDate(){
        Date d=new Date();
        java.sql.Date sqldate=new java.sql.Date(d.getTime());
        
        return sqldate;
    }
    
    private static void crearVenta(BaseDatos db,int idProducto,int cantidad){
        Producto p=db.consultaUnProducto(idProducto);
        System.out.println(p.getDescripcion());
        //si el producto no es nulo
        if(p!=null){
            //si el stock menos la cantidad no es inferior al stockminimo
            if(p.getStockactual()-cantidad>=p.getStockminimo()){
                int id=db.obterUltimoID("ventas")+1;                
                Venta v=new Venta(id,p.getIdproducto(),getCurrentDate(),cantidad);
                System.out.println(v.getNumeroventa()+","+v.getFechaventa()+","+v.getCantidad());
                //insertamos la venta en la bd
                int i=db.inxerirVenta(v);
                /*
                AQUI FALTA ACTUALIZAR EL STOCK
                */
                System.out.println("Venta realizada: "+i);
            }
        }
    }
}
