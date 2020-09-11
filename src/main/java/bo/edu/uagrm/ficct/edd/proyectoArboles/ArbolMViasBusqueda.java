/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

import java.util.List;
import bo.edu.uagrm.ficct.edd.proyectoArboles.ExcepcionOrdenInvalido;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author Asus
 */
public class ArbolMViasBusqueda<K extends Comparable<K>,V> implements IArbolBinarioBusqueda<K,V> {
    protected NodoMVias<K,V> raiz ;
    protected int orden;
    
    public ArbolMViasBusqueda(){
        this.orden = 3;
    }
    public ArbolMViasBusqueda(int orden)  throws ExcepcionOrdenInvalido{
        if(orden < 3 ){
         throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
        
    }
    
    public void vaciar(){
         this.raiz = nodoVacioParaElArbol();
    }
    
    public NodoMVias<K,V> nodoVacioParaElArbol(){
         return (NodoMVias<K,V> ) NodoMVias.nodoVacio();
    }
    
    public boolean esArbolVacio(){
        return NodoMVias.esNodoVacio(this.raiz);
    }
    public boolean contiene(K clave){
        return this.buscar(clave)!= NodoMVias.valorVacio();
    }
    @Override 
    public void insertar(K clave, V valor) throws DatoYaExisteExcepcion {
        NodoMVias<K,V> nodoActual = this.raiz;
        if(NodoMVias.esNodoVacio(this.raiz)){
            this.raiz=new NodoMVias(orden,clave,valor);
          return ;
        }
        while(! NodoMVias.esNodoVacio(nodoActual)){
        
            if(this.existeClaveEnNodo(nodoActual,clave)){
                throw new DatoYaExisteExcepcion();
            }
            
            if(nodoActual.esHoja()){
                if(nodoActual.estanDatosLlenos()){
                    int posicionPorDondeBajar = this.porDondeBajar(nodoActual,clave);
                    NodoMVias<K,V> nuevo = new NodoMVias<K,V>(orden,clave,valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevo);
                }else{
                    this.insertarEnOrden(nodoActual,clave,valor);
                }
                nodoActual = (NodoMVias<K, V>) NodoMVias.nodoVacio();
            }else{
                int posicionPorDondeBajar = this.porDondeBajar(nodoActual,clave);
                if(nodoActual.esHijoVacio(posicionPorDondeBajar)){
                    NodoMVias<K,V> nuevo = new NodoMVias<K,V>(orden,clave,valor);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevo);
                    nodoActual = (NodoMVias<K, V>) NodoMVias.nodoVacio();
                }else{
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ArbolMViasBusqueda{" + "raiz=" + raiz + ", orden=" + orden + '}';
    }

    @Override
    public List<K> recorridoInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoInOrdenRecursivo(raiz,recorrido);
        return recorrido;
    }

    @Override
    public List<K> recorridoPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPostOrdenRecursivo(raiz,recorrido);
        return recorrido;
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> listakeys= new LinkedList<>();
        if(this.esArbolVacio()){
            return listakeys;
        }
        Queue<NodoMVias<K,V>> colaNodos = new LinkedList<>();
        colaNodos.offer(this.raiz);
        while(!colaNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaNodos.poll();
            for(int i = 0 ; i < nodoActual.cantidadDeDatosNoVacios();i++ ){
                listakeys.add(nodoActual.getClave(i));
                if(! nodoActual.esHijoVacio(i)){
                    colaNodos.offer(nodoActual.getHijo(i));
                }
            }
            if(! nodoActual.esHijoVacio(orden - 1)){
                colaNodos.offer(nodoActual.getHijo(orden-1));
            }
            
            
        }
        return listakeys;
    }

    @Override
    public List<K> recorridoPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoPreOrdenRecursivo(raiz,recorrido);
        return recorrido;
    }

    @Override
    public V eliminar(K clave) throws DatoNoExisteExcepcion  {
        V valor = this.buscar(clave);
        if(NodoMVias.esNodoVacio(raiz)){
             throw new DatoNoExisteExcepcion();
        }else{
            this.raiz = this.eliminar(this.raiz,clave);
            return valor;
        }
    }

    @Override
    public V buscar(K clave) {
        NodoMVias<K,V> nodoActual = this.raiz;
        while(!NodoMVias.esNodoVacio(nodoActual)){
           NodoMVias<K,V> nodoAnterior = nodoActual; 
           for(int i = 0 ; i < nodoActual.cantidadDeDatosNoVacios() && nodoActual==nodoAnterior ;i++){
               K claveActual = nodoActual.getClave(i);
               if(clave.compareTo(claveActual) == 0){
                   return nodoActual.getValor(i);
               }
               if(clave.compareTo(claveActual) < 0){
                   if(NodoMVias.esNodoVacio(nodoActual.getHijo(i))){
                       return (V) NodoMVias.valorVacio();
                   }
                   nodoActual = nodoActual.getHijo(i);
               }
           }
           if(nodoAnterior == nodoActual){
               nodoActual = nodoActual.getHijo(orden-1);
           }
        }
        return (V) NodoMVias.valorVacio();
    }

    @Override
    public int altura() {
        return altura(raiz);
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
               if(clave.compareTo(claveActual) < 0){
                   return i;
               }
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

    @Override
    public void printBinaryTree() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void recorridoPreOrdenRecursivo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if(NodoMVias.esNodoVacio(nodoActual)){
            return ;
        }
        
        for(int i = 0; i < nodoActual.cantidadDeDatosNoVacios() ;i++){
            recorrido.add(nodoActual.getClave(i));
            recorridoPreOrdenRecursivo(nodoActual.getHijo(i),recorrido);
        }
        recorridoPreOrdenRecursivo(nodoActual.getHijo(orden-1),recorrido);
    }

    private void recorridoInOrdenRecursivo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if(NodoMVias.esNodoVacio(nodoActual)){
            return ;
        }
        
        for(int i = 0; i < nodoActual.cantidadDeDatosNoVacios() ;i++){
            recorridoPreOrdenRecursivo(nodoActual.getHijo(i),recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoPreOrdenRecursivo(nodoActual.getHijo(orden-1),recorrido);
    }

    private void recorridoPostOrdenRecursivo(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if(NodoMVias.esNodoVacio(nodoActual)){
            return ;
        }
        recorridoPreOrdenRecursivo(nodoActual.getHijo(0),recorrido);
        for(int i = 0; i < nodoActual.cantidadDeDatosNoVacios() ;i++){
            recorridoPreOrdenRecursivo(nodoActual.getHijo(i+1),recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
        
    }

    @Override
    public int size() {
        return size(raiz);
    }

    private int size(NodoMVias<K, V> nodoActual) {
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0;
        }
        int cantidad = 0 ;
        for(int i = 0 ; i < orden; i++){
            cantidad = cantidad + size(nodoActual.getHijo(i));
        }
        cantidad = cantidad + 1 ;
        return cantidad;
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0;
        }
        int alturaMayor = 0 ;
        for(int i = 0 ; i < orden; i++){
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if(alturaDeHijo > alturaMayor){
                alturaMayor = alturaDeHijo;
            }
        }
        return alturaMayor + 1;    
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveEliminar) {
        for(int i = 0 ; i < nodoActual.cantidadDeDatosNoVacios();i++){
            K claveActual = nodoActual.getClave(i);
            if(claveEliminar.compareTo(claveActual)==0){
                if(nodoActual.esHoja()){
                    this.eliminarDatoDelNodo(nodoActual,i);
                    if(nodoActual.estanDatosVacios()){
                        return (NodoMVias<K, V>) NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                K claveRemplazo ; 
                if(this.hayHijosMasAdelante(nodoActual,i)){
                    claveRemplazo = this.buscarSucesorInOrden(nodoActual,claveEliminar);
                }else{
                    claveRemplazo = this.buscarPredecesorInOrden(nodoActual,claveEliminar);
                }
                V valorDeRemplazo = this.buscar(claveRemplazo);
                nodoActual = eliminar(nodoActual,claveRemplazo);
                nodoActual.setClave(i, claveRemplazo);
                nodoActual.setValor(i, valorDeRemplazo);
                return nodoActual;
            }
            if(claveEliminar.compareTo(claveActual)<0){
                NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
            
            
        }
        NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(orden-1), claveEliminar);
                nodoActual.setHijo(orden-1, supuestoNuevoHijo);
                return nodoActual;
        
    }

    protected void eliminarDatoDelNodo(NodoMVias<K, V> nodoActual, int indiceDato) {
        
        for(int i = indiceDato ; i < orden  ; i++){
            K claveDespuesDelIndice ;
            V valorDespuesDelIndice ;
            if( i + 1 < orden){
                if(!nodoActual.esDatoVacio(i+1)){
                claveDespuesDelIndice = nodoActual.getClave(i+1);
                valorDespuesDelIndice = nodoActual.getValor(i+1);
                }else{
                claveDespuesDelIndice =null;
                valorDespuesDelIndice = null;
                }
            
            }else{
            claveDespuesDelIndice =null;
            valorDespuesDelIndice = null;
            }
            
            nodoActual.setClave(i, claveDespuesDelIndice);
            nodoActual.setValor(i, valorDespuesDelIndice);
        }
        
    }

    private boolean hayHijosMasAdelante(NodoMVias<K, V> nodoActual, int indiceActual) {
        for(int i =indiceActual ; i < orden ; i++ ){
            if(!NodoMVias.esNodoVacio(nodoActual.getHijo(i))){
                return true;
            }
        }
        return false;
    }

    private K buscarSucesorInOrden(NodoMVias<K, V> nodoActual, K claveEliminar) {

        for(int i = 0 ; i < orden-1 ; i++){
            K claveActual= nodoActual.getClave(i);
            if(claveEliminar.compareTo(claveActual)<=0){
               if(!NodoMVias.esNodoVacio(nodoActual.getHijo(i))){
                NodoMVias<K,V> nodoInOrden = nodoActual.getHijo(i);
                return nodoInOrden.getClave(0);
               }else{
                   return nodoActual.getClave(i+1);
               } 

            }
        }
        NodoMVias<K,V> nodoInOrden = nodoActual.getHijo(orden-1);
        if(!NodoMVias.esNodoVacio(nodoInOrden)){
            return nodoInOrden.getClave(0);
        }
        return null;
    }

    private K buscarPredecesorInOrden(NodoMVias<K, V> nodoActual, K claveEliminar) {
        for(int i = 0 ; i < orden-1 ; i++){
            K claveActual= nodoActual.getClave(i);
            if(claveEliminar.compareTo(claveActual)<0){
               if(NodoMVias.esNodoVacio(nodoActual.getHijo(i))){
                NodoMVias<K,V> nodoInOrden = nodoActual.getHijo(i);
                while(!NodoMVias.esNodoVacio(nodoInOrden)){
                    nodoInOrden= nodoInOrden.getHijo(0);
                }
                return nodoInOrden.getClave(nodoInOrden.cantidadDeDatosNoVacios()-1);
               } 

            }
        }
        NodoMVias<K,V> nodoInOrden = nodoActual.getHijo(orden-1);
        while(!NodoMVias.esNodoVacio(nodoInOrden)){
            nodoInOrden= nodoInOrden.getHijo(0);
        }
        return nodoInOrden.getClave(nodoInOrden.cantidadDeDatosNoVacios()-1);
    }
    //6. Implemente un método recursivo que retorne la cantidad nodos con datos vacíos en un
//árbol m-vias de bússqueda
    public int cantidadDeNodosConDatosVacios(){
        return cantidadDeNodosConDatosVaciosRecursivo(this.raiz);
    }

    private int cantidadDeNodosConDatosVaciosRecursivo(NodoMVias<K, V> nodoActual) {
        int cantidad = 0;
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0 ;
        }
        
        if((nodoActual.cantidadDeDatosNoVacios() < orden-1)){
            cantidad = 1;
        }
        
        for(int i = 0 ; i<orden ; i++){
            cantidad = cantidad + cantidadDeNodosConDatosVaciosRecursivo(nodoActual.getHijo(i));
        }
        return cantidad ;
    }
  //9.Implemente un método que retorne verdadero si solo hay hojas en el último nivel de un
//árbol m-vias de búsqueda. Falso en caso contrario
    public boolean verificarSiSoloHayHojasEnElUltimoNivel(){
        return verificarSiSoloHayHojasEnElUltimoNivelRecursivo(this.raiz,nivel(),0);
    }

    private boolean verificarSiSoloHayHojasEnElUltimoNivelRecursivo(NodoMVias<K, V> nodoActual, int nivel,int nivelActual) {
        boolean condicional = true;
        if(NodoMVias.esNodoVacio(nodoActual)){
            return true;
        }
        

        for(int i= 0 ; i < orden ; i++){
            condicional =  verificarSiSoloHayHojasEnElUltimoNivelRecursivo(nodoActual.getHijo(i),nivel,nivelActual+1);
        }
        if((nivel == nivelActual && nodoActual.esHoja() || condicional == false)){
            return  false;
        }
        return true;
    }
    //12. Para un árbol m-vias implemente un método que retorne la cantidad de nodos que tienen
//todos sus hijos distintos de vacío luego del nivel N (La excepción a la regla son las hojas).
    //9. Implemente un método que retorne verdadero si solo hay hojas en el último nivel de un
//árbol m-vias de búsqueda. Falso en caso contrario
    
    
    public int nivel() {
        if(this.esArbolVacio()){
            return 0;
        }
        Queue<NodoMVias<K,V>> colaNodos = new LinkedList<>();
        Queue<Integer> colaDeNiveles = new LinkedList<>();
        colaNodos.offer(this.raiz);
        colaDeNiveles.offer(0);
        int nivelAnterior= 0;
        while(!colaNodos.isEmpty()){
            NodoMVias<K,V> nodoActual = colaNodos.poll();
            int nivelActual = colaDeNiveles.poll();
            if(nivelActual > nivelAnterior){
                nivelAnterior = nivelActual;
            }
            for(int i = 0 ; i < orden;i++ ){
                if(! nodoActual.esHijoVacio(i)){
                    colaNodos.offer(nodoActual.getHijo(i));
                    colaDeNiveles.offer(nivelActual+1);
                }
            }
        }
        return nivelAnterior;
    }
    
        //12. Para un árbol m-vias implemente un método que retorne la cantidad de nodos que tienen
//todos sus hijos distintos de vacío luego del nivel N (La excepción a la regla son las hojas).
    public int cantidadDeNodosConTodosSusHijos( int nivel){
        return cantidadDeNodosConTodosSusHijosRecursivo(this.raiz,nivel,0);
    }

    private int cantidadDeNodosConTodosSusHijosRecursivo(NodoMVias<K, V> nodoActual, int nivel, int nivelActual) {
        if(NodoMVias.esNodoVacio(nodoActual)){
           return 0 ;
        }
        int cantidad = 0 ;
        if(nivelActual >=nivel && nodoActual.cantidadDeHijosNoVacios()==orden){
            cantidad = 1;
        }
        for(int i = 0 ; i < orden ; i++){
            cantidad = cantidad + cantidadDeNodosConTodosSusHijosRecursivo(nodoActual.getHijo(i),nivel,nivelActual+1);
        }
        return cantidad;
    }
    
    //10. Implemente un método que retorne verdadero si un árbol m-vias esta balanceado según las
//reglas de un árbol B. Falso en caso contrario.
        public boolean verificarSiEstaBalanceado(){
            return verificarSiEstaBalanceadoRecursivo(this.raiz);
        }

    private boolean verificarSiEstaBalanceadoRecursivo(NodoMVias<K, V> nodoActual) {
        int hijosMaximos = this.orden-1;
        int datosMaximos = this.orden-2;
        int datosMinimos = datosMaximos/2;
        int hijosMinimos = datosMinimos + 1;
        if(NodoMVias.esNodoVacio(nodoActual)){
            return true;
        }
        boolean condicion = true;
        for(int i = 0 ; i < orden ; i++){
             condicion = verificarSiEstaBalanceadoRecursivo(nodoActual.getHijo(i));
        }
        
        
        if((nodoActual.cantidadDeDatosNoVacios()>= datosMinimos && nodoActual.cantidadDeDatosNoVacios()<= datosMaximos) &&
            ((nodoActual.cantidadDeHijosNoVacios() >= hijosMinimos &&  nodoActual.cantidadDeHijosNoVacios()<=hijosMaximos) && !nodoActual.esHoja()) && condicion==true ){
            return true;
        }
        return false;
    }
    //. Implemente un método privado que reciba un dato como parámetro y que retorne cual seria
//el predecesor inorden de dicho dato, sin realizar el recorrido en inOrden.
        public K buscarSucesor(K clave) {
        NodoMVias<K,V> nodoActual = this.raiz;
        while(!NodoMVias.esNodoVacio(nodoActual)){
           NodoMVias<K,V> nodoAnterior = nodoActual; 
           for(int i = 0 ; i < nodoActual.cantidadDeDatosNoVacios() && nodoActual==nodoAnterior ;i++){
               K claveActual = nodoActual.getClave(i);
               if(clave.compareTo(claveActual) == 0){
                   if(i== orden-2){
                       while(!NodoMVias.esNodoVacio(nodoActual.getHijo(0))){
                           nodoActual = nodoActual.getHijo(0);
                       }
                       return nodoActual.getClave(0);
                   }
                   for(int j = nodoActual.cantidadDeDatosNoVacios()-1 ; j < orden -1 ;j++){
                       K claveSucesor ;
                       if(!nodoActual.esDatoVacio(i)){
                           claveSucesor =nodoActual.getClave(i);
                           return nodoActual.getClave(j);
                       }
                   }
                   return (K) NodoMVias.claveVacia();
               }
               if(clave.compareTo(claveActual) < 0){
                   if(NodoMVias.esNodoVacio(nodoActual.getHijo(i))){
                       return (K) NodoMVias.claveVacia();
                   }
                   nodoActual = nodoActual.getHijo(i);
               }
           }
           if(nodoAnterior == nodoActual){
               nodoActual = nodoActual.getHijo(orden-1);
           }
        }
        return (K) NodoMVias.claveVacia();
    }
}
