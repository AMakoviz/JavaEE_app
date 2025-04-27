package org.example.lab1_j200.servlets;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.ejb.EJB;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.lab1_j200.beans.SelectInterface;
import org.example.lab1_j200.beans.UpdateInterface;
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
        out.println(" <script >\n" );
        out.println("function addAddressField(){\n");
        out.println("let l = document.getElementById(\"addresses\").cloneNode(true);");
        out.println("document.body.append(l);\n");
        out.println("}\n");
        out.println("</script>");
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
        out.println("<button type=\"button\" onclick=\"addAddressField()\">Добавить адрес</button><br/><br/>\n");
        out.println("<input type=\"submit\" name=\"save\" value=\"Сохранить\"/>");
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
        String[] ip = request.getParameterValues("ip");
        String[] mac = request.getParameterValues("mac");
        String[] model = request.getParameterValues("model");
        String[] location = request.getParameterValues("location");
        String message;
        ClientEntity clientEntity;
        clientEntity = updateBean.create(clientName, type, ip, mac, model, location);
        if (clientEntity != null) {
            response.sendRedirect("viewList");
        } else {
            message= response(clientName, ip, mac, model, location, type);
            //message = "Ошибка при добавлении клиента. Убедитесь, что все поля заполнены корректно";
            PrintWriter out = response.getWriter();
            out.println("<html><head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Error</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Ошибка добавления клиента:</h2><br/><br/>\n");
            out.println("<h3> "+ message + "</h3>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    public void destroy() {
    }

    private boolean checkClientName(String clientName) {
        String regex = "^[А-Яа-яЁё\\-,. ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clientName);
        if (clientName == null || clientName.equals("")) {
            return false;
        } else if (clientName.length() >100) {
            return false;
        } else if (!matcher.matches()) {
            return false;
        } else return true;
    }
    private boolean checkIp(String[] ip) {
        String regex = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.[0-9]{1,3}";
        Pattern pattern = Pattern.compile(regex);
        for (String s : ip) {
            Matcher matcher = pattern.matcher(s);
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() >25) {
                return false;
            }
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }
    private boolean checkMac(String[] mac) {
        String regex = "([A-F0-9]{2}-){5}[A-F0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        for (String s : mac) {
            Matcher matcher = pattern.matcher(s);
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() > 20) {
                return false;
            }
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkModel(String[] model) {
        for (String s : model) {
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;}
            if (s.length() >100) {
                    return false;
                }
        }
        return true;
    }
    private boolean checkLocation(String[] location) {
        for (String s : location) {
            if (s.trim() == null || s.trim().isEmpty()) {
                return false;
            }
            if (s.length() >200) {
                return false;
            }
        }
        return true;

    }
    private boolean checkType(String type) {
        if (type.trim() == null || type.trim().isEmpty()) {
            return false;
        }
        if (!type.equals("Юридическое лицо") && !type.equals("Физическое лицо")) {
            return false;
        }
        return true;
    }
    private String response(String clientName, String[] ip, String[] mac, String[] model, String[] location, String type) {
       StringBuilder message = new StringBuilder();
        if (!checkClientName(clientName)) {
            message.append("Имя клиента не может быть пустым, не может содержать более 100 символов и должно быть написано на кирилице \n");
        }
        if (!checkIp(ip)) {
            message.append("Введите корректный IP адрес. \n");
        }
        if (!checkMac(mac)) {
            message.append("Введите корректный MAC адрес. \n");
        }
        if (!checkModel(model)) {
            message.append("Модель устройтсва не может содержать более 100 символов или быть пустой \n");
        }
        if (!checkLocation(location)) {
            message.append("Адрес не может содержать более 200 символов или быть пустым \n");
        }
        if (!checkType(type)) {
            message.append("Неверный тип \n");
        }

        return message.toString();

    }

}