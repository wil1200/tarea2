
package com.emergentes.controlador;

import com.emergentes.modelo.Producto;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "MainControler", urlPatterns = {"/MainControler"})
public class MainControler extends HttpServlet {

  
   

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         /// verificar si la coleccion de peronas ya existe en el objeto sesion
        HttpSession ses = request.getSession();
        if (ses.getAttribute("listaprod") == null) {
            // crear la coleccion y almacenarla en el onjeto session
            ArrayList<Producto> listaux = new ArrayList<Producto>();
            //colocar listaux como atributo de la session

            ses.setAttribute("listaprod", listaux);

        }

        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaprod");

        String op = request.getParameter("op");
        String opcion = (op != null) ? request.getParameter("op") : " view";
       Producto obj1 = new Producto();
        int id, pos;
        switch (opcion) {
            case "nuevo":
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "editar":

                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request, id);
                obj1 = lista.get(pos);
                request.setAttribute("miProducto", obj1);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
                break;
            case "eliminar":
                id = Integer.parseInt(request.getParameter("id"));
                pos = buscarIndice(request, id);
                lista.remove(pos);
                ses.setAttribute("listaprod", lista);
                response.sendRedirect("index.jsp");
                break;

            case "view":
                // ses.setAttribute("listaper", lista);
                response.sendRedirect("index.jsp");
                break;

        }
        
    }

 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaprod");
       Producto obj1 = new Producto();

        int idt;

        obj1.setId(Integer.parseInt(request.getParameter("id")));
        obj1.setDescripcion(request.getParameter("descripcion"));
        obj1.setCantidad(Integer.parseInt(request.getParameter("cantidad")));
        obj1.setPrecio(Integer.parseInt(request.getParameter("precio")));

        idt = obj1.getId();
        
        if (idt == 0) {
            obj1.setId(ultimoId(request));
            lista.add(obj1);
        } else {
            lista.set(buscarIndice(request, idt), obj1);
        }
        ses.setAttribute("listaprod", lista);
        response.sendRedirect("index.jsp");

    }

   
    private int ultimoId(HttpServletRequest request) {
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaprod");
        int idaux = 0;
        for (Producto item : lista) {
            idaux = item.getId();
        }
        return idaux + 1;
    }

    private int buscarIndice(HttpServletRequest request, int id) {
        HttpSession ses = request.getSession();
        ArrayList<Producto> lista = (ArrayList<Producto>) ses.getAttribute("listaprod");
        int i = 0;
        if (lista.size() > 0) {
            while (i < lista.size()) {
                if (lista.get(i).getId() == id) {
                    break;
                } else {
                    i++;
                }
            }
        }
        return i;
    }



}
