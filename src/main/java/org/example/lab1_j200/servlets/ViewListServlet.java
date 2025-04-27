package org.example.lab1_j200.servlets;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab1_j200.beans.SelectInterface;
import org.example.lab1_j200.repositories.entities.AddressEntity;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@WebServlet(name = "viewListServlet", value = {"/viewList", "/"})
public class ViewListServlet extends HttpServlet {
    @EJB
    private SelectInterface selectBean;
    public ViewListServlet() {
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        htmlPage(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        htmlPage(request, response);
    }
    private void htmlPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        String filter = Objects.toString(request.getParameter("filter"), "");
        String type = request.getParameter("type");


        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<title>Create</title>");
        out.println("</head>");
        out.println("<body>\n");
        out.println("<form action=\"viewList\" method=\"post\">\n");
        out.println("    <label>Поиск клиента</label>\n");
        out.println("    <input type=\"text\" size=\"50\" value=\"" + filter + "\" name=\"filter\"/> &nbsp;\n");
        out.println("    <label>Фильтр по типу клиента</label>\n");
        out.println("    <select id=\"type\" name=\"type\">\n");
        out.println("      <option value=\"\">Показать всех</option>\n");
        out.println("      <option value=\"Физическое лицо\">Физическое лицо</option>\n");
        out.println("      <option value=\"Юридическое лицо\">Юридическое лицо</option>\n" );
        out.println("    </select> &nbsp;\n");
        out.println("    <input type=\"submit\" name=\"find\" value=\"Искать\"/>\n");
        out.println("<td><a href=\"" + request.getContextPath() + "/create"+ "\">Создать клиента</a></td>");
        out.println("  </form><br/><br/>");
        out.println("<table border=\"1\">\n");
        out.println("  <tr>\n");
        out.println("    <th>Id клиента</th>\n");
        out.println("    <th>Имя клиента</th>\n");
        out.println("    <th>Тип клиента</th>\n");
        out.println("    <th>Дата добавления</th>\n");
        out.println("    <th>IP адрес</th>\n");
        out.println("    <th>MAC адрес</th>\n");
        out.println("    <th>Модель устройства</th>\n");
        out.println("    <th>Адрес подключения</th>\n");
        out.println("    <th></th>\n");
        out.println("    <th></th>\n");
        out.println("    <th></th>\n");
        out.println("  </tr>\n");
        selectBean.getClientsByRegex(filter, type).forEach(client -> {
            Set <AddressEntity> addressEntities= new HashSet<>();
            addressEntities.addAll(client.getAddresses());
            for (AddressEntity addressEntity : addressEntities) {
                out.println("  <tr>\n");
                out.println("    <td>" + client.getId() + "</td>\n");
                out.println("    <td>" + client.getClientName() + "</td>\n");
                out.println("    <td>" + client.getType() + "</td>\n");
                out.println("    <td>" + client.getAdded() + "</td>\n");
                out.println("<td>" + addressEntity.getIpAddress() + "</td>\n");
                out.println("<td>" + addressEntity.getMacAddress() + "</td>\n");
                out.println("<td>" + addressEntity.getModel() + "</td>\n");
                out.println("<td>" + addressEntity.getAddress() + "</td>\n");
                out.println("<td><a href=\"" + request.getContextPath() + "/update?id=" + client.getId() + "&mode=add\">Добавить адрес</a></td>");
                out.println("<td><a href=\"" + request.getContextPath() + "/update?id=" + client.getId() + "&mode=edit\">Редактировать</a></td>");
                out.println("<td><a href=\"" + request.getContextPath() + "/delete?id=" + addressEntity.getId() + "\">Удалить</a></td>");
                out.println("  </tr>\n");
            }
        });
        out.println("</table>\n");
        out.println("</body>\n");
        out.println("</html>");
    }

}

