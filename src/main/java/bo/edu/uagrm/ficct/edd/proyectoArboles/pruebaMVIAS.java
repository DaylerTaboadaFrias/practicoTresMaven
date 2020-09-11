/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.edd.proyectoArboles;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import bo.edu.uagrm.ficct.edd.proyectoArboles.ArbolMViasBusqueda;
/**
 *
 * @author Asus
 */
public class pruebaMVIAS {
    public static void main(String[] argumentos) throws Exception{
int tipoArbol ;
        
        
        boolean b = true;
         BufferedReader reader = 
                   new BufferedReader(new InputStreamReader(System.in));
                    ArbolB<Integer,String> arbolB = null;
            ArbolMViasBusqueda<Integer,String> arbolMvias = null ;
            
        while( b ){
            System.out.println("Lista de metodos:");
            System.out.println("(1) Insertar en arbol MVias :");
            System.out.println("(2) Eliminar en arbol MVias:");
            System.out.println("(3) Insertar en arbol B :");
            System.out.println("(4) Eliminar en arbol B (No implementado):");
            System.out.println("(5) Cantidad de nodos con datos vacios ,Arbol B recursivo:");
            System.out.println("(6) Cantidad de nodos con datos vacios ,Arbol Mvias recursivo:");
            System.out.println("(7) Cantidad de nodos con datos vacios pero solo en el nivel N ,Arbol B recursivo:");
            System.out.println("(8)  Cantidad de nodos con datos vacios pero solo en el nivel N ,Arbol B iterativo:");
            System.out.println("(9) Verificar si solo solo hay hojas en el ultimo nivel de un arbol MVias");
            System.out.println("(10) Verificar si el arbol MVias esta balanceado ");
            System.out.println("(11) Buscar predecesor :");
            System.out.println("(12) Cantidad de nodos que tengan hijos distintos de vacio despues de un nivel:");
            System.out.println("(13) FINALIZAR PROGRAMA:");
            System.out.println("Elije un metodo (introducir numero) :");
            int metodo = Integer.parseInt(reader.readLine());
            int clave;

		switch(metodo) {
                    
			case 1:
                            int orden;
                            System.out.println("Introduzca el orden del arbol Mvias:");
                             orden = Integer.parseInt(reader.readLine());
                             arbolMvias = new ArbolMViasBusqueda<>(orden) ;
                             System.out.println("Introduzca la cantidad de datos a insertar :");
                             int cantidad = Integer.parseInt(reader.readLine());
                             for(int i = 1 ; i <= cantidad ; i++){
                                System.out.println("Clave del nodo :");
                                clave = Integer.parseInt(reader.readLine());
                                System.out.println("Valor del nodo :");
                                String valor = (reader.readLine());
                                arbolMvias.insertar(clave, valor);
                             }
                            
                            System.out.println("LOS DATOS SE INSERTARON CORRECTAMENTE RECORIDO POR NIVELES :" +arbolMvias.recorridoPorNiveles() );
                            break;
			case 2:
                            int orden2;
                            System.out.println("Introduzca el orden del arbol Mvias:");
                             orden2 = Integer.parseInt(reader.readLine());
                             arbolB = new ArbolB<>(orden2) ;
                             System.out.println("Introduzca la cantidad de datos a insertar :");
                             int cantidad2 = Integer.parseInt(reader.readLine());
                             for(int i = 1 ; i <= cantidad2 ; i++){
                                System.out.println("Clave del nodo :");
                                clave = Integer.parseInt(reader.readLine());
                                System.out.println("Valor del nodo :");
                                String valor = (reader.readLine());
                                arbolB.insertar(clave, valor);
                             }
                            
                            System.out.println("LOS DATOS SE INSERTARON CORRECTAMENTE RECORIDO POR NIVELES :" +arbolB.recorridoPorNiveles() );
                            break;
			case 3:
                            System.out.println("Clave del nodo a buscar:");
                            clave = Integer.parseInt(reader.readLine());
                            
                            System.out.println("Resultado : "+ arbolB.buscar(clave));
                            break;
                        case 4:
                            System.out.println("No implementado");
                            break;
                        case 5:
                            System.out.println("Resultado : "+ arbolB.cantidadDeNodosConDatosVacios());
                            break;
                        case 6:
                            
                            System.out.println("Resultado : "+ arbolMvias.cantidadDeNodosConDatosVacios());
                            break;
                        case 7:
                            System.out.println("Introduzca un nivel :");
                            int nivel3 = Integer.parseInt(reader.readLine());
                            System.out.println("Resultado : "+ arbolB.cantidadDeNodosConDatosVaciosEnUnNivel(nivel3));
                            break;
                        case 8:
                            System.out.println("Introduzca un nivel :");
                            int nivel = Integer.parseInt(reader.readLine());
                            System.out.println("Resultado : "+ arbolB.cantidadDeNodosConDatosVaciosEnUnNivelIterativo(nivel));
                            break;
                        case 9:
                            System.out.println("Resultado : "+ arbolMvias.verificarSiSoloHayHojasEnElUltimoNivel());
                            break;
                        case 10:
                            System.out.println("Resultado : "+ arbolB.verificarSiEstaBalanceado());
                            break;
                        case 11:
                            System.out.println("Introduzca una clave :");
                            int cl = Integer.parseInt(reader.readLine());
                            System.out.println("Resultado : "+ arbolB.buscarSucesor(cl));
                            break;
                        case 12:
                            System.out.println("Introduzca una clave :");
                            int nv = Integer.parseInt(reader.readLine());
                            System.out.println("Resultado : "+  arbolMvias.cantidadDeNodosConTodosSusHijos(nv));
                            break;
			case 13:
                            b = false;
                            break;
		}

        }
    }
}
