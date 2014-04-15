package DivideYVenceras2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Principal {

	public static ArrayList<Punto> puntosUsados = new ArrayList<Punto>();	//ArrayList que almacena todos los puntos del fichero
	public static int numPuntos;											//Númeero de puntos totales
	public static Punto puntoInflexion;										//Punto más bajo de la nube de dispersión de puntos
	public static boolean sepInflexion=false;								//Indicador de estado del algoritmo
	public static ArrayList<Punto> puntosLeidos = puntosUsados;
		
	public static void main(String[] args) {		
		long inicio = System.currentTimeMillis();							
		boolean flag=true;													//Indicador para "jugar" con la eliminación de puntos en los extremos. Explicación más adelante.
		leerArchivo("C:" + File.separator +/*"WORKSPACES"+ File.separator + "PracticasEDA2" +File.separator +*/"PruebaAlgoritmia" + File.separator + "DivideYVenceras"+ File.separator+ "test6.txt");
		ArrayList<Punto> resultado=divideYVenceras(puntosUsados);			//Llamada al método divide y vencerás	
		if(resultado.size()!=1000){																	
			while(resultado.size()<1000){												//En esta parte de código controlamos que el resultado tenga, en efecto, el 1% de los 100,000
				puntosUsados.removeAll(resultado);										//puntos especificados. Primero comprobamos que es distinto del número pedido.																																												 					
				sepInflexion=false;														//Si hay menos puntos de los esperados, añadimos más puntos realizando más "rodajas" a la nube de puntos.				
				for(int i=0;i<puntosUsados.size();i++){									//Para ello eliminamos los puntos ya escogidos del array de puntos, y buscamos el nuevo punto de inflexión
					puntoInflexion=new Punto(Integer.MIN_VALUE, Integer.MAX_VALUE);		//Una vez realizado este proceso, tendremos que tener el 1% deseado, o más puntos
					Punto aux = puntosUsados.get(i);					
					if(aux.getY()<puntoInflexion.getY()){
						puntoInflexion=aux;
					}
				}
				if(puntosUsados.size()<1000){break;}
				ArrayList<Punto> resultado2=divideYVenceras(puntosUsados);
				resultado.addAll(resultado2);
				Collections.sort(resultado);	
				//System.out.println("Realiza divide");
			}											
			Voraz(resultado, puntosUsados);
		}																				
		while(resultado.size()>1000){													//Si se tienen más puntos, comenzará el proceso de eliminación de puntos sobrantes.
			if(flag){																	//Al ser las fronteras las zonas menos significativas del problema, iremos eliminando elementos de ellas, uno a uno
				resultado.remove(0);													//y de forma alternada. Primero un lado, despues otro, y esto lo controlaremos con la variable de control 'flag'
				flag=false;
			}else{
				resultado.remove(resultado.size()-1);
				flag=true;
			}
		}
		escribirArchivo("C:" + File.separator +/*"WORKSPACES"+ File.separator + "PracticasEDA2" +*/File.separator +"PruebaAlgoritmia" + File.separator + "DivideYVenceras"+ File.separator+ "Sol6.txt", resultado);
		long fin = System.currentTimeMillis();
		long tiempoFinal = fin - inicio;											//Finalmente escribiremos el archivo de salida con la solución especificada
		System.out.println(tiempoFinal);
		
	}
		
	public static ArrayList<Punto> divideYVenceras(ArrayList<Punto> puntos){		//Método divide y vencerás
		ArrayList<Punto> puntosDevueltos;											//Guardamos la solución en un ArrayList llamado 'PuntosDevueltos'													 
		boolean sepCombinar=false;
		if(puntos.size()<=4){														//Zona del caso base
																					//Debido al número de puntos que habrá en el problema, hemos considerado que se deben recoger como
			if(puntos.get(0).getX()<puntoInflexion.getX()){							//la fracción más pequeña 4 puntos o menos. Más que nada es porque, si realizamos las consecuentes divisiones,
				puntosDevueltos=recogerPuntosIzq(puntos);							//de dos en dos, con esta cifra nos dará un número exacto, necesario para que la comprobación de los puntos sea correcta
			}else{
				puntosDevueltos=recogerPuntosDer(puntos);							//Segun la posición en la que se encuentren los puntos, estos usaran un método u otro para recoger los puntos válidos
			}
		}else{
																					//Zona de la división y casos recursivos
			int contador=0;															//Este contador mantendrá controladola zona a la que es enviado cada punto
			ArrayList<Punto> divIzquierda = new ArrayList<Punto>();					//ArrayList que contiene los puntos de la "zona izquierda" trás dividir
			ArrayList<Punto> divDerecha = new ArrayList<Punto>();					//ArrayList que contiene los puntos de la "zona derecha" trás dividir
			if(sepInflexion==false){												//Aquí controlaremos que, si los puntos no han sido divididos previamente en dos zonas a izquierda y derecha del punto de inflexión (caso que solo ocurrirá la primera vez),
				for(int i=0; puntos.get(i).getX()<puntoInflexion.getX();i++){		//sean separados en dos zonas distintas, con diferente tratamiento.
					divIzquierda.add(puntos.get(i));
				}
				for(int i=puntos.indexOf(puntoInflexion); i<puntos.size();i++){
					divDerecha.add(puntos.get(i));
				}
				sepInflexion=true;	
				sepCombinar=true;													//Con esta variable controlaremos que se mantenga cerrada la entrada a la división en las partes creciente y decreciente de la función
			}else{
				Iterator<Punto> iteradorPuntos= puntos.iterator();					//En esta parte del algoritmo dividimos las listas de puntos, ya divididas en decrecientes o crecientes, en dos partes iguales
				while(iteradorPuntos.hasNext()){									
					if (contador < puntos.size() / 2) {
						divIzquierda.add(iteradorPuntos.next());
						contador++;
					}else{
						divDerecha.add(iteradorPuntos.next());
					}
				}
			}		
			ArrayList<Punto> listIzq = divideYVenceras(divIzquierda);				//Aquí aplicamos a ambas listas el método divide y vencerás, iniciando así la recursión
			ArrayList<Punto> listDer = divideYVenceras(divDerecha);			
			if(sepCombinar==true){													//Finalmente, en la zona de combinar, juntamos con los resultados de las zonas a la izquierda y derecha del punto de inflexión en una misma colección
				puntosDevueltos = combinarPendientes(listIzq, listDer);				
			}else{																	//Si, por el contrario, nos encontramos con elementos divididos, comprobaremos la región a la que pertenecen y se combinaran entre ellos de forma adecuada a su posición en la función
				if(listIzq.get(0).getX()<puntoInflexion.getX()){
					puntosDevueltos=combinarIzquierda(listIzq, listDer);
				}else{
					puntosDevueltos = combinarDerecha(listIzq, listDer);
				}
			}					
		}
		//System.out.println("Sale de recursión");
		return puntosDevueltos;														//Finalmente, devolvemos el ArrayList con los puntos
		
	}
	
	private static ArrayList<Punto> recogerPuntosIzq(ArrayList<Punto> puntos){		//En este método, pasamos por parametro un caso base y recogemos los puntos que son adecuados como solución. Este metodo será el usado para los puntos 
		ArrayList<Punto> puntosDevolver=new ArrayList<Punto>();						//A la izquierda del punto de inflexión
		if(puntos.size()<=2){														
			return puntos;															//Si el tamaño es de 2 puntos o menos, los devolvemos tal cual, ya que no se pueden realizar comprobaciones entre ellos
		}
		puntosDevolver.add(puntos.get(0));											//En el resto de casos, seguiremos un analisis de los puntos según su posición, recorriendolos ordenadamente en el eje X
		int indiceLista=0;															//Añadiremos el primer punto, que será el que se encuentre más a la izquierda, y será el punto de referencia para las comprobaciones 
		
		for(int i=1; i<puntos.size();i++){											//Siguiendo un recorrido descendiente, iremos añadiendo los puntos que sean inferiores al punto de referencia ya tomado
			if(puntos.get(i).compareY(puntosDevolver.get(indiceLista))==-1			//Si su altura es igual o menor, será añadido, y una vez añadido, este nuevo punto será la nueva referencia para las comprobaciones
					||puntos.get(i).compareY(puntosDevolver.get(indiceLista))==0){
				puntosDevolver.add(puntos.get(i));
				indiceLista++;				
			}							
		}	
		return puntosDevolver;														//Devolveremos los puntos recogidos. No es necesario ordenarlos ya que han sido leidos en orden.
	}
	
	private static ArrayList<Punto> recogerPuntosDer(ArrayList<Punto> puntos){		//Este método, similar al anterior, recogerá los puntos situados a la derecha del punto de inflexión
		ArrayList<Punto> puntosDevolver=new ArrayList<Punto>();						
		if(puntos.size()<=2){
			return puntos;
		}																			//El planteamiento del ejercicio es muy similar al anterior, solo que, para conservar la estructura descendiente, 
		puntosDevolver.add(puntos.get(puntos.size()-1));							//Se recorreran de derecha a izquierda, tomando el último punto como el de referencia (ya que será el más alto)
		int indiceLista=0;
		
		for(int i=puntos.size()-2;i>-1;i--){
			if(puntos.get(i).compareY(puntosDevolver.get(indiceLista))==-1
					||puntos.get(i).compareY(puntosDevolver.get(indiceLista))==0){
				puntosDevolver.add(puntos.get(i));
				indiceLista++;				
			}							
		}
		Collections.sort(puntosDevolver);											//En este caso, al estar insertados de derecha a izquierda, será necesario ordenar la solución antes de devolverla
		return puntosDevolver;
	}
	
	public static void leerArchivo(String direccion) {								//En este método leemos los puntos del archivo
		try {
			puntoInflexion=new Punto(Integer.MIN_VALUE,Integer.MAX_VALUE);			//Le damos al punto de referencia un valor que siempre estará por encima de cualquier punto de la gráfica
			File f = new File(direccion);
			FileReader fr = new FileReader(f);
			BufferedReader buffer = new BufferedReader(fr);
			String entrada;
			entrada = buffer.readLine();
			String numPntString = entrada;
			numPuntos = Integer.parseInt(numPntString);								//Aquí leemos el número de puntos que contendrá la gráfica inicial
			//int cont = 0;	
			int contador=0;
			while ((entrada = buffer.readLine()) != null) {							//Leemos lineas hasta que tenemos tantos puntos como numero de puntos haya indicado en la primera linea del archivo	
			//	System.out.println(cont);			
				//cont++;
				StringTokenizer token = new StringTokenizer(entrada, ", ");
				String xString = "", yString = "";																		
				if (token.hasMoreTokens()) {										//Comprobamos que cada punto tiene 2 valores, considerando una linea vacia como que faltan datos sobe el punto
					xString = token.nextToken();
				}				
				if (token.hasMoreTokens()) {
					yString = token.nextToken();
				} else {
					int nPunto = contador + 1;
					System.out.println("No hay suficientes datos para el punto "+ nPunto + ", o es una línea en blanco, añada los datos que faltan o borre la línea.");
					System.exit(0);
				}
				int x = 0, y = 0;																					
				try {																//En esta zona comprobamos que los valores son correctos
					x = Integer.parseInt(xString);					
					y = Integer.parseInt(yString);
					if (x < 0 || y < 0) {											//No aceptaremos valores negativos
						int nPunto = contador + 1;
						System.out.println("No se permiten medidas negativas. Modifica los datos del punto "+nPunto);
						System.exit(0);
					}
					
				} catch (NumberFormatException e) {
					int nPunto = contador + 1;
					System.out.println("Algunos de los datos introducidos no son correctos. Modifica los datos punto "+nPunto);
					System.exit(0);
				}
				
				Punto aux = new Punto(x, y);										//Aquí aprovechando la lectura de los puntos, indicaremos cual es el punto de inflexión
				if(aux.getY()<puntoInflexion.getY()){
					puntoInflexion=aux;
				}																				
				
				if (!puntosUsados.contains(aux)) {									//Si el punto es repetido, se ignorará																					
					if (token.hasMoreTokens()) {									//Si en una linea hay mas de 2 datos, se cogen solo los 2 primeros
						System.out.println("Un punto solo puede tener 2 datos, utilizamos estos datos: "+ aux.toString());
					}
					puntosUsados.add(aux);
				}
				contador++;
			}
			//Si el numero de puntos que pone que hay en el archivo es distinto al numero de puntos que hay realmente,
			//mostramos un mensaje, pero no se detiene la ejecución, ya que el numero de puntos no influye en los calculos.
			//Para mostrar el numero de puntos usamos puntosLeidos.size()
			if (contador != numPuntos) {
				System.out.println("El número de puntos no coincide con la cantidad de puntos del archivo.");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void escribirArchivo(String direccion, ArrayList<Punto> resultado){			//En este método recogemos la solución y escribimos un archivo de salida
		try {																					//con el resultado del programa
			Punto p = new Punto();
			PrintWriter pw = new PrintWriter(new FileWriter(direccion));
			pw.println(resultado.size());
			for(int i = 0; i < resultado.size(); i++){
				p = resultado.get(i);
				pw.print(p.getX());
				pw.print(",");
				pw.print(p.getY());
				pw.println();
			}
			pw.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}																					
																					
																					
	private static ArrayList<Punto> combinarPendientes(ArrayList<Punto> listaIzquierda, ArrayList<Punto> listaDerecha){
		ArrayList<Punto> listaDevolver=new ArrayList<Punto>();						//En este método juntaremos los resultados a la izquierda del punto
		listaDevolver.addAll(listaIzquierda);										//de inflexión con los resultados a la derecha del punto de inflexión
		listaDevolver.addAll(listaDerecha);											//en un mismo ArrayList
		return listaDevolver;
	}
	
	private static ArrayList<Punto> combinarIzquierda(ArrayList<Punto> listaIzquierda, ArrayList<Punto> listaDerecha) {
		ArrayList<Punto> listaPuntos = listaIzquierda;								//En este método realizaremos la combinación de resultados.
		Punto puntoAux = listaIzquierda.get(listaIzquierda.size()-1);				//Para ello, tal y como se hizo en la recolección de puntos,
		for(int i = listaDerecha.size()-1; i > 0; i--){								//recorreremos la colección, realizando	comprobaciones, pero con una excepción	
			if(listaDerecha.get(i).getY() <= puntoAux.getY()){						
				listaPuntos.add(listaDerecha.get(i));								//Recorreremos los puntos empezando por el final. Si el punto a unificar
			}else{																	//no es valido, tampoco lo serán los anteriores, ya que estos tendrá un valor de Y
				break;																//superior a los puntos anteriores.
			}																		//De este modo filtraremos aun más laa busqueda, mejorando la eficiencia
		}				
		Collections.sort(listaPuntos);												//Teniendo en cuenta que recorremos la lista al reves, necesitaremos ordenar los puntos antes de devolverlos
		return listaPuntos;
	}
	
	private static ArrayList<Punto> combinarDerecha(ArrayList<Punto> listaIzquierda, ArrayList<Punto> listaDerecha) {
		ArrayList<Punto> listaPuntos = listaDerecha;								//Método similar al anterior, pero recorriendo de derecha a izquierda en el eje de X
		for(int i=0;i<listaIzquierda.size();i++){
			if(listaIzquierda.get(i).getY() <= listaPuntos.get(0).getY()){
				listaPuntos.add(listaIzquierda.get(i));
			}else{
				break;
			}
		}
		Collections.sort(listaPuntos);
		return listaPuntos;
	}	
	
	private static void Voraz(ArrayList<Punto> resultado,
			ArrayList<Punto> puntosLeidos2) {
		// TODO Auto-generated method stub
		
	}

}
