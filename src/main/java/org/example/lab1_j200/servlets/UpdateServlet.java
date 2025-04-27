package org.example.lab1_j200.servlets;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab1_j200.beans.SelectInterface;
import org.example.lab1_j200.beans.UpdateInterface;
import org.example.lab1_j200.repositories.entities.AddressEntity;
import org.example.lab1_j200.repositories.entities.ClientEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@WebServlet(name = "updateServlet", value = "/update")
public class UpdateServlet extends HttpServlet {
    @EJB
    private UpdateInterface updateBean;
    @EJB
    private SelectInterface selectBean;

    public UpdateServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(request.getParameter("id"));
        String mode = request.getParameter("mode");
        ClientEntity clientEntity = selectBean.getClientByParam(id);
        if (mode.equals("add")){
            htmlFormAdd(request, response, clientEntity);
        } else if (mode.equals("edit")){
            htmlFormEdit(request, response, clientEntity);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String[] addressId = request.getParameterValues("address-id");
        Long clientId = Long.parseLong(request.getParameter("client-id"));
        String clientName = request.getParameter("client-name");
        String type = request.getParameter("type");
        String[] ip = request.getParameterValues("ip");
        String[] mac = request.getParameterValues("mac");
        String[] model = request.getParameterValues("model");
        String[] location = request.getParameterValues("location");
        ClientEntity clientEntity = updateBean.update(clientId,clientName, type, addressId, ip, mac, model, location);
        if (clientEntity != null){
            response.sendRedirect("viewList");
        } else {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Edit</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Проверьте правильность вводимых значений </h2><br/>\n");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void htmlFormEdit(HttpServletRequest request, HttpServletResponse response, ClientEntity clientEntity) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Edit</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(" <form action=\"update\" method=\"post\">\n");
        out.println("<h2>Редактирование данных клиента</h2><br/>\n");
        out.println("<input type=\"text\" value=\""+ clientEntity.getId() + "\" name=\"client-id\" hidden/>&nbsp;\n");
        out.println("<label>Имя клиента</label>\n");
        out.println("<input type=\"text\"value=\"" + (clientEntity.getClientName()) + "\"size=\"70\" name=\"client-name\"/> &nbsp;\n");
        out.println("<label for=\"type\">Тип клиента</label>\n");
        out.println("<select id=\"type\" name=\"type\">\n");
        out.println("  <option value=\"Физическое лицо\"" + ("Физическое лицо".equals(clientEntity.getType()) ? " selected" : "") + ">Физическое лицо</option>\n");
        out.println("  <option value=\"Юридическое лицо\"" + ("Юридическое лицо".equals(clientEntity.getType()) ? " selected" : "") + ">Юридическое лицо</option>\n");
        out.println("</select>");
        out.println("<br/><br/>\n");
        Set <AddressEntity> addressEntities = new HashSet<AddressEntity>();
        addressEntities.addAll(clientEntity.getAddresses());
        for (AddressEntity addressEntity : addressEntities){
            out.println("<hr>");
            out.println("<label>IP-адрес</label>\n");
            out.println("<input type=\"text\" value=\""+ addressEntity.getIpAddress() +  "\" size=\"25\" name=\"ip\"/>&nbsp;\n");
            out.println("<label>MAC-адрес</label>\n");
            out.println("<input type=\"text\" value=\"" + addressEntity.getMacAddress() + "\"size=\"20\" name=\"mac\"/>&nbsp;\n");
            out.println("<label>Модель устройства</label>\n");
            out.println("<input type=\"text\" value=\"" + addressEntity.getModel() + "\"size=\"70\" name=\"model\"/><br/><br/>\n");
            out.println("<label>Адрес местонахождения</label>\n");
            out.println(" <input type=\"text\" value=\"" + addressEntity.getAddress() + "\"size=\"200\" name=\"location\"/><br/><br/>");
            out.println("<br/><br/>\n");
            out.println("<input type=\"text\" value=\""+ addressEntity.getId() + "\" name=\"address-id\" hidden/>&nbsp;\n");
            out.println("<hr>");
        }
        out.println("<input type=\"submit\" name=\"save\" value=\"Сохранить\"/>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
    private void htmlFormAdd(HttpServletRequest request, HttpServletResponse response, ClientEntity clientEntity) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Add</title>");
        out.println(" <script >\n" );
        out.println("function addAddressField(){\n");
        out.println("let l = document.getElementById(\"addresses\").cloneNode(true);");
        out.println("document.body.append(l);\n");
        out.println("}\n");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println(" <form action=\"update\" method=\"post\">\n");
        out.println("<h2>Добавление адресов</h2><br/>\n");
        out.println("<input type=\"text\" value=\""+ clientEntity.getId() + "\" name=\"client-id\" hidden/>&nbsp;\n");
        out.println("<label>Имя клиента</label>\n");
        out.println("<input type=\"text\" value=\"" + (clientEntity.getClientName()) + "\"size=\"70\" name=\"client-name\" readonly/> &nbsp;\n");
        out.println("<label for=\"type\">Тип клиента</label>\n");
        out.println("<input type=\"text\" value=\"" + (clientEntity.getType()) + "\" name=\"type\" readonly/> &nbsp;\n");
        out.println("<br/><br/>\n");
        out.println("<hr>");
        out.println("<div id=\"addresses\">");
        out.println("<label>IP-адрес</label>\n");
        out.println("<input type=\"text\" size=\"25\" name=\"ip\"/>&nbsp;\n");
        out.println("<label>MAC-адрес</label>\n");
        out.println("<input type=\"text\" size=\"20\" name=\"mac\"/>&nbsp;\n");
        out.println("<label>Модель устройства</label>\n");
        out.println("<input type=\"text\" size=\"70\" name=\"model\"/><br/><br/>\n");
        out.println("<label>Адрес местонахождения</label>\n");
        out.println(" <input type=\"text\" size=\"200\" name=\"location\"/><br/><br/>");
        out.println("<hr>");
        out.println("</div>\n");
        out.println("<button type=\"button\" onclick=\"addAddressField()\">Добавить адрес</button><br/><br/>\n");
        out.println("<input type=\"submit\" name=\"save\" value=\"Сохранить\"/>");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }
}
