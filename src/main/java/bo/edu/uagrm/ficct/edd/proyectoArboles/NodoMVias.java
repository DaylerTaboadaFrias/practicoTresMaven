/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dayle
 */
public class NodoMVias<K,V> {

    static void raiz(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private List<K> listaDeClaves;
    private List<V> listaDeValores;
    private List<NodoMVias<K,V>> listaDeHijos;
    
    public NodoMVias(int orden){
        listaDeClaves = new LinkedList<>();
        listaDeValores = new LinkedList<>();
        listaDeHijos = new LinkedList<>();
        for(int i = 0 ; i<orden-1 ; i++){
            listaDeClaves.add((K) NodoMVias.claveVacia());
            listaDeValores.add((V) NodoMVias.valorVacio());
            listaDeHijos.add((NodoMVias<K, V>) NodoMVias.nodoVacio());
        }
        listaDeHijos.add(null);
    }
    public NodoMVias(int orden , K clave , V valor){
        this(orden);
        listaDeClaves.set(0,clave);
        listaDeValores.set(0,valor);
    }
    public static NodoMVias<?,?> nodoVacio(){
        return null;
    }
    public static Object claveVacia(){
        return null;
    }
    public static Object valorVacio(){
        return null;
    }
    public K getClave(int posicion){
        return listaDeClaves.get(posicion);
    }
    
    public V getValor(int posicion){
        return listaDeValores.get(posicion);
    }
    
    public void setClave(int posicion ,K clave){
        listaDeClaves.set(posicion, clave);
    }
    public void setValor (int posicion ,V valor){
        listaDeValores.set(posicion, valor);
    }
    
    public NodoMVias<K,V> getHijo(int posicion){
        return listaDeHijos.get(posicion);
    }
    public void setHijo(int posicion ,NodoMVias<K,V> nodo){
        listaDeHijos.set(posicion, nodo);
    }
    
    public static boolean esNodoVacio(NodoMVias nodo){
        return nodo == NodoMVias.nodoVacio();
    }
    public boolean esDatoVacio(int posicion){
        return listaDeClaves.get(posicion) == NodoMVias.valorVacio();
    }
    public boolean esHijoVacio(int posicion){
        return listaDeHijos.get(posicion) == NodoMVias.nodoVacio();
    }
    
    public boolean esHoja(){
        for(int i = 0 ; i< listaDeHijos.size();i++){
            if(!this.esHijoVacio(i)){
                return false;
            }
        }
        return true;
    }
    public boolean estanDatosLlenos(){
        for(K clave : listaDeClaves){
            if(clave == NodoMVias.valorVacio()){
                return false;
            }
        }
        return true;
    }
    public boolean estanDatosVacios(){
        for(K clave : listaDeClaves){
            if(clave != NodoMVias.valorVacio()){
                return false;
            }
        }
        return true;
    }
    
    public int cantidadDeDatosNoVacios(){
        int cantidad = 0;
        if(this.listaDeClaves.isEmpty()){
            return cantidad;
        }
        for(int i = 0 ; i< listaDeClaves.size();i++){
            if(!this.esDatoVacio(i)){
                cantidad = cantidad +1 ;
            }
        }
        return cantidad;
    }
    public int cantidadDeHijosVacios(){
        int cantidad = 0;
        for(int i = 0 ; i< listaDeClaves.size();i++){
            if(this.esHijoVacio(i)){
                cantidad = cantidad +1 ;
            }
        }
        return cantidad;
    }
    public int cantidadDeHijosNoVacios(){
        int cantidad = 0;
        for(int i = 0 ; i< listaDeHijos.size();i++){
            if(!this.esHijoVacio(i)){
                cantidad = cantidad +1 ;
            }
        }
        return cantidad;
    }
}
