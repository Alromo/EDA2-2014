package DivideYVenceras;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class GeneradorArchivos {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		generarAleatorio("C:" + File.separator +/*"WORKSPACES"+ File.separator + "PracticasEDA2" +File.separator+*/"PruebaAlgoritmia" + File.separator + "DivideYVenceras"+ File.separator+ "test6.txt");
	}
	
	   private static void generarAleatorio(String archivoEntrada){
           try {
                   File f = new File(archivoEntrada);
                   FileWriter fw = new FileWriter(f);
                   PrintWriter pw = new PrintWriter(fw);
                   int numElementos = 100000; // Aquí se selecciona el numero de elementos aleatorios
                   
                   
                   int a, b;
                   ArrayList<Punto> array=new ArrayList<Punto>();
                   pw.println(Integer.toString(numElementos));
                   for (int i = 0; i < numElementos; i++) {
                           a = (int) (Math.random() * 1000);                           
                           b = (int) (Math.random() * 100000);
                           Punto punto=new Punto(a,b);
                           array.add(punto);                                                 
                           //pw.print((a + b));                           
                   }
                   Collections.sort(array);
                   for(int i=0; i<array.size();i++){
                	   pw.println(array.get(i));
                   }
                   pw.close();
           }catch (Exception e){
                   e.printStackTrace();
           }
   }

}
