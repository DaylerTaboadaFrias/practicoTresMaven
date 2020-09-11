/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Asus
 */
public class ArbolB<K extends Comparable<K>,V> extends ArbolMViasBusqueda<K,V> implements IArbolBinarioBusqueda<K,V> {
    private int nroMaximoDeDatos;
    private int nroMinimoDeDatos;
    private int nroMinimoDeHijos;
    
    public ArbolB(){
        super();
        this.nroMaximoDeDatos=2;
        this.nroMinimoDeDatos=1;
        this.nroMinimoDeHijos=2;
    }
    
    public ArbolB(int orden) throws ExcepcionOrdenInvalido{
        super(orden);
        this.nroMaximoDeDatos = super.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos= this.nroMinimoDeHijos + 1;
    }
    @Override 
    public void insertar(K clave, V valor) throws DatoYaExisteExcepcion {
        
        if(NodoMVias.esNodoVacio(this.raiz)){
            this.raiz=new NodoMVias(orden+1,clave,valor);
          return ;
        }
        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.raiz;
        while(! NodoMVias.esNodoVacio(nodoActual)){
        
            if(this.existeClaveEnNodo(nodoActual,clave)){
                throw new DatoYaExisteExcepcion();
            }
            
            if(nodoActual.esHoja()){
                this.insertarEnOrden(nodoActual,clave,valor);
                if(nodoActual.cantidadDeDatosNoVacios() >  this.nroMaximoDeDatos){
                    this.dividir(nodoActual,pilaDeAncestros);
                }
                nodoActual = (NodoMVias<K, V>) NodoMVias.nodoVacio();
            }else{
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual,clave);
                pilaDeAncestros.push(nodoActual);
                nodoActual=nodoActual.getHijo(posicionPorDondeBajar);
            }
        }
    }
    
    public V eliminar(K claveAEliminar) throws DatoNoExisteExcepcion {
        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar,pilaDeAncestros);
        if(NodoMVias.esNodoVacio(nodoActual)){
           throw new DatoNoExisteExcepcion();
        }
        int posicionDeLaClaveEnElNodo =  this.porDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeLaClaveEnElNodo);
        if(nodoActual.esHoja()){
            super.eliminarDatoDelNodo(nodoActual, posicionDeLaClaveEnElNodo);
            if(nodoActual.cantidadDeDatosNoVacios() < this.nroMinimoDeDatos){
                if(pilaDeAncestros.isEmpty()){
                    if(nodoActual.estanDatosVacios()){
                       super.vaciar();
                    }   
                }else{
                    this.prestarOFusionar(nodoActual,pilaDeAncestros);
                }
            }
        }
        else{
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K,V> nodoDelPredecesor = this.buscarNodoDelPredecesor(pilaDeAncestros, nodoActual.getHijo(posicionDeLaClaveEnElNodo));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeDatosNoVacios()-1;
            K clavePredecesora =  nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarDatoDelNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeLaClaveEnElNodo, clavePredecesora);
            nodoActual.setValor(posicionDeLaClaveEnElNodo, valorPredecesor);
            if(nodoDelPredecesor.cantidadDeDatosNoVacios() < this.nroMinimoDeDatos){
                this.prestarOFusionar(nodoDelPredecesor, pilaDeAncestros);
            }
        }
        return valorARetornar;
    }
    private boolean existeClaveEnNodo(NodoMVias<K, V> nodoActual, K clave) {
        for(int i = 0 ; i < this.orden-1 ; i++){
            if(!nodoActual.esDatoVacio(i)){
                K claveActual = nodoActual.getClave(i);
                if(clave.compareTo(claveActual)==0){
                    return true;
                }
            }
            
        }
        return false;
    }

    private int porDondeBajar(NodoMVias<K, V> nodoActual, K clave) {
        for(int i = 0 ; i < this.orden - 1 ;i++){
            if(!nodoActual.esDatoVacio(i)){
               K claveActual = nodoActual.getClave(i);
               if(clave.compareTo(claveActual) <= 0){
                   return i;
               }
            }else{
                return i;
            }
        }
        return this.orden - 1;
    }

    private void insertarEnOrden(NodoMVias<K, V> nodoActual, K clave, V valor) {
        int indice= 0;
        for(int i = 0 ;  i < orden+1 ; i++){
            K claveActual = nodoActual.getClave(i);
            if(!nodoActual.esDatoVacio(i)){
                if(clave.compareTo(claveActual) < 0){
                    indice = i;
                    K claveRecorrer = nodoActual.getClave(i);
                    V valorRecorrer= nodoActual.getValor(i);
                    int datosNoVacios = nodoActual.cantidadDeDatosNoVacios();
                    for(int j = i ; j <=datosNoVacios-1 ;j++){
                        K claveSiguiente = null;
                        V valorSiguiente = null;
                        if(j+1 <= orden){
                            if(!nodoActual.esDatoVacio(j+1)){
                            claveSiguiente = nodoActual.getClave(j+1);
                            valorSiguiente = nodoActual.getValor(j+1);
                            }
                        }
                        
                        nodoActual.setClave(j+1, claveRecorrer);
                        nodoActual.setValor(j+1, valorRecorrer);
                        claveRecorrer = claveSiguiente;
                        valorRecorrer=valorSiguiente;
                    }
                    nodoActual.setClave(indice, clave);
                    nodoActual.setValor(indice, valor);
                    return;
                }
            }else{
                nodoActual.setClave(i, clave);
                nodoActual.setValor(i, valor);
                return;
            }
        }
        
    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        boolean entraBucle = true;//siempre entrará la primera vez
        boolean creaNodo = true;
        NodoMVias<K,V> nodoAncestro = null;
        if(!pilaDeAncestros.isEmpty()){
           nodoAncestro = pilaDeAncestros.pop();  
           creaNodo = false;
        }else{
            creaNodo = true;
        }
        while(entraBucle ){
           if(nodoActual.cantidadDeDatosNoVacios() >  this.nroMaximoDeDatos && creaNodo ){
            NodoMVias<K,V> nuevaRaiz = new NodoMVias<>(orden+1,nodoActual.getClave(this.nroMinimoDeDatos),nodoActual.getValor(this.nroMinimoDeDatos));
            NodoMVias<K,V> nuevoNodoIzquierdo = new NodoMVias<>(orden+1);
            NodoMVias<K,V> nuevoNodoDerecho = new NodoMVias<>(orden+1);
            for(int i = 0 ; i < this.nroMinimoDeDatos ; i++){
                nuevoNodoIzquierdo.setValor(i, nodoActual.getValor(i));
                nuevoNodoIzquierdo.setClave(i, nodoActual.getClave(i));
                nuevoNodoIzquierdo.setHijo(i, nodoActual.getHijo(i));
                nodoActual.setHijo(i, null);
            }
            nuevoNodoIzquierdo.setHijo(nroMinimoDeDatos, nodoActual.getHijo(nroMinimoDeDatos));
            nodoActual.setHijo(nroMinimoDeDatos, null);
            int indiceDerecho = 0;
            for(int i = nroMinimoDeDatos+1 ; i < orden ; i++){
                nuevoNodoDerecho.setValor(indiceDerecho, nodoActual.getValor(i));
                nuevoNodoDerecho.setClave(indiceDerecho, nodoActual.getClave(i));
                nuevoNodoDerecho.setHijo(indiceDerecho, nodoActual.getHijo(i));
                nodoActual.setHijo(i, null);
                indiceDerecho = indiceDerecho +1;
            }
            nuevoNodoDerecho.setHijo(nuevoNodoDerecho.cantidadDeDatosNoVacios(), nodoActual.getHijo(orden));
            nuevaRaiz.setHijo(0, nuevoNodoIzquierdo );
            nuevaRaiz.setHijo(1, nuevoNodoDerecho );
            this.raiz = nuevaRaiz;
        }else{
                this.insertarEnOrden(nodoAncestro, nodoActual.getClave(nroMinimoDeDatos), nodoActual.getValor(nroMinimoDeDatos));
                int porDondeBajar = this.porDondeBajar(nodoAncestro, nodoActual.getClave(nroMinimoDeDatos));
                NodoMVias<K,V> nuevoNodoDerecho = new NodoMVias<>(orden+1);
                int indiceDerecho = 0;
                for(int i = nroMinimoDeDatos+1 ; i < orden ; i++){
                    nuevoNodoDerecho.setValor(indiceDerecho, nodoActual.getValor(i));
                    nuevoNodoDerecho.setClave(indiceDerecho, nodoActual.getClave(i));
                    nuevoNodoDerecho.setHijo(indiceDerecho, nodoActual.getHijo(i));
                    nodoActual.setHijo(i, null);
                    indiceDerecho = indiceDerecho +1;
                    
                }
                nuevoNodoDerecho.setHijo(orden, nodoActual.getHijo(orden));
                nodoActual.setHijo(orden, null);
                for(int i = nroMinimoDeDatos ; i < orden+1 ; i++){
                    super.eliminarDatoDelNodo(nodoActual, nroMinimoDeDatos);
                }
                
                
                //meter nodos hijos
                int adelante = 0 ; int inice =  porDondeBajar+2;
                for(int i = porDondeBajar+2 ; i < nodoAncestro.cantidadDeDatosNoVacios() ; i++){
                    NodoMVias<K,V> nodoAnterior = nodoAncestro.getHijo(inice+adelante);
                    nodoAncestro.setHijo(i, null);
                    nodoAncestro.setHijo(i, nodoAncestro.getHijo(i-1));
                    nodoAncestro.setHijo(i+1, nodoAnterior);
                    adelante = adelante+1;
                }
                nodoAncestro.setHijo(porDondeBajar+1, nuevoNodoDerecho);
                
                
            
        }  if(creaNodo){
            nodoActual = null;
           }else{
            nodoActual = nodoAncestro;
            }
           
            if((raiz.cantidadDeDatosNoVacios() >  this.nroMaximoDeDatos)){
                entraBucle = true;  
                creaNodo=true;
            }else{
                if(NodoMVias.esNodoVacio(nodoActual)){
                entraBucle = false;
                }else{
                    if(nodoActual.cantidadDeDatosNoVacios() >  this.nroMaximoDeDatos){
                    entraBucle = true;
                    }else{
                    entraBucle = false;
                    }
                
                }
                
            }
       } 

    }

    private NodoMVias<K, V> buscarNodoDeLaClave(K claveABuscar, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        NodoMVias<K,V> nodoActual = this.raiz;
        while(! NodoMVias.esNodoVacio(nodoActual)){
            int tamanhioDeNodoActual = nodoActual.cantidadDeDatosNoVacios();
            NodoMVias<K,V> nodoAnterior = nodoActual;
            for(int i = 0 ; i <tamanhioDeNodoActual && nodoAnterior==nodoActual ; i++ ){
                K claveActual = nodoActual.getClave(i);
                if( claveABuscar.compareTo(claveActual) ==0){
                    return nodoActual;
                }
                if(claveABuscar.compareTo(claveActual)<0){
                    if(! nodoActual.esHoja()){
                        pilaDeAncestros.push(nodoActual);
                        nodoActual = nodoActual.getHijo(i);
                    }else{
                        nodoActual = (NodoMVias<K, V>) NodoMVias.nodoVacio();
                    }
                }
            }
            if( nodoAnterior == nodoActual){
                    if(! nodoActual.esHoja()){
                        pilaDeAncestros.push(nodoActual);
                        nodoActual = nodoActual.getHijo(tamanhioDeNodoActual);
                    }else{
                        nodoActual = (NodoMVias<K, V>) NodoMVias.nodoVacio();
                    }
            }
        }
        return (NodoMVias<K, V>) NodoMVias.nodoVacio();
    }

    private void prestarOFusionar(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private NodoMVias<K, V> buscarNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> hijo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    //5. Implemente un método recursivo que retorne la cantidad nodos con datos vacíos en unárbol B
    public int cantidadDeNodosConDatosVacios(){
        return cantidadDeNodosConDatosVaciosRecursivo(this.raiz);
    }

    private int cantidadDeNodosConDatosVaciosRecursivo(NodoMVias<K, V> nodoActual) {
        int cantidad = 0;
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0 ;
        }
        
        if(nodoActual.cantidadDeDatosNoVacios() < orden-1){
            cantidad = 1;
        }
        
        for(int i = 0 ; i<orden ; i++){
            cantidad = cantidad + cantidadDeNodosConDatosVaciosRecursivo(nodoActual.getHijo(i));
        }
        return cantidad ;
    }
    
    
    //7. Implemente un método recursivo que retorne la cantidad nodos con datos vacíos en un
    //árbol B, pero solo en el nivel N
    public int cantidadDeNodosConDatosVaciosEnUnNivel(int nivel){
        return cantidadDeNodosConDatosVaciosEnUnNivelRecursivo(this.raiz,nivel,0);
    }

    private int cantidadDeNodosConDatosVaciosEnUnNivelRecursivo(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0 ;
        }
        int cantidad = 0 ;
        if(nivelActual == nivel && nodoActual.cantidadDeDatosNoVacios() < orden-1 ){
            cantidad= 1;
        }
        for(int i = 0; i < orden ; i++){
            cantidad = cantidad + cantidadDeNodosConDatosVaciosEnUnNivelRecursivo(nodoActual.getHijo(i) , nivel,nivelActual+1);
        }
        return cantidad;
    }
    
    //8. Implemente un método iterativo que retorne la cantidad nodos con datos vacíos en un árbol
        //b, pero solo en el nivel N
        public int cantidadDeNodosConDatosVaciosEnUnNivelIterativo(int nivel) {
        if(this.esArbolVacio()){
            return 0;
        }
        Queue<NodoMVias<K,V>> colaNodos = new LinkedList<>();
        Queue<Integer> colaDeNiveles = new LinkedList<>();
        colaNodos.offer(this.raiz);
        colaDeNiveles.offer(0);
        int cantidad= 0;
        while(!colaNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaNodos.poll();
            int nivelActual = colaDeNiveles.poll();
            if(nodoActual.cantidadDeDatosNoVacios() < orden -1 && nivelActual==nivel){
                cantidad = cantidad + 1 ;
            }
            for(int i = 0 ; i < orden;i++ ){
                if(! nodoActual.esHijoVacio(i)){
                    colaNodos.offer(nodoActual.getHijo(i));
                    colaDeNiveles.offer(nivelActual+1);
                }
            }
            
        }
        return cantidad;
    }
        
       
}
