/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

import java.util.List;

/**
 *
 * @author dayle
 */
public interface IArbolBinarioBusqueda<K extends Comparable<K>,V> {
    void insertar(K clave ,V valor) throws DatoYaExisteExcepcion;
    List<K> recorridoInOrden();
    List<K> recorridoPostOrden();
    List<K> recorridoPorNiveles();
    List<K> recorridoPreOrden();
    V eliminar(K clave) throws DatoNoExisteExcepcion;
    V buscar(K clave);
    int altura();
    int size();
    public void printBinaryTree();
}
