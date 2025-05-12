package org.example.lab1_j200.servlets;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import jakarta.ejb.EJB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.lab1_j200.beans.UpdateInterface;
import org.example.lab1_j200.repositories.entities.AddressEntity;
import org.example.lab1_j200.repositories.entities.ClientEntity;

@WebServlet(name = "createServlet", value = "/create")
public class CreateServlet extends HttpServlet {

    @EJB
    private UpdateInterface updateBean;

    public CreateServlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Create</title>");
//        out.println(" <script >\n" );
//        out.println("function addAddressField(){\n");
//        out.println("let l = document.getElementById(\"addresses\").cloneNode(true);");
//        out.println("document.body.append(l);\n");
//        out.println("}\n");
//        out.println("</script>");
        out.println("</head>");
        out.println("<body>");
        out.println(" <form action=\"create\" method=\"post\">\n");
        out.println("<h2>Добавление клиента</h2><br/>\n");
        out.println("<label>Имя клиента</label>\n");
        out.println("<input type=\"text\" size=\"70\" name=\"client-name\"/> &nbsp;\n");
        out.println("<label for=\"type\">Тип клиента</label>\n");
        out.println("<select id=\"type\" name=\"type\">\n");
        out.println("<option value=\"Физическое лицо\">Физическое лицо</option>\n");
        out.println("<option value=\"Юридическое лицо\">Юридическое лицо</option>\n" );
        out.println("</select><br/><br/>\n");
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
//        out.println("<button type=\"button\" onclick=\"addAddressField()\">Добавить адрес</button><br/><br/>\n");
        out.println("<input type=\"submit\" name=\"save\" value=\"Сохранить\"/>&nbsp;");
        out.println("</form>");
        out.println("<form action=\"viewList\" method=\"get\">");
        out.println("<input type=\"submit\" name=\"view\" value=\"К списку клиентов\">");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String clientName = request.getParameter("client-name");
        String type = request.getParameter("type");
        String ip = request.getParameter("ip");
        String mac = request.getParameter("mac");
        String model = request.getParameter("model");
        String location = request.getParameter("location");
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientName(clientName);
        clientEntity.setType(type);
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setIpAddress(ip);
        addressEntity.setMacAddress(mac);
        addressEntity.setModel(model);
        addressEntity.setAddress(location);
        addressEntity.setClient(clientEntity);
        Set<AddressEntity> addresses = new HashSet<AddressEntity>();
        addresses.add(addressEntity);
        clientEntity.setAddresses(addresses);
        clientEntity = updateBean.create(clientEntity);
        if (clientEntity != null) {
            response.sendRedirect("viewList");
        } else {
            PrintWriter out = response.getWriter();
            out.println("<html><head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Error</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Проверьте правильность вводимых значений </h2><br/><br/>\n");
            out.println("<form action=\"create\" method=\"get\">\n" +
                    "        <input type=\"submit\" name=\"create\" value=\"Создать нового клиента\">");
            out.println("</form>");
            out.println("<form action=\"viewList\" method=\"get\">\n" +
                    "        <input type=\"submit\" name=\"view\" value=\"К списку клиентов\">");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public void destroy() {
    }

}