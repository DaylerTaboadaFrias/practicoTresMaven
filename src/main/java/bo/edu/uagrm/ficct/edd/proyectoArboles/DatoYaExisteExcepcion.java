/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

/**
 *
 * @author dayle
 */
public class DatoYaExisteExcepcion extends Exception {
    public DatoYaExisteExcepcion(){
    super("Dato ya existe exception");
    }
    public DatoYaExisteExcepcion(String mensaje){
    super(mensaje);
    }
}
