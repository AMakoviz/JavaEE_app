package org.example.lab1_j200.servlets;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab1_j200.beans.UpdateInterface;

import java.io.IOException;

@WebServlet(name = "deleteServlet", value = "/delete")
public class DeleteServlet extends HttpServlet {
    @EJB
    private UpdateInterface updateBean;
    public DeleteServlet() {
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(request.getParameter("id"));
        updateBean.delete(id);
        response.sendRedirect("viewList");
    }
}
