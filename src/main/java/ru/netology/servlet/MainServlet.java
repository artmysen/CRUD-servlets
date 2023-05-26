package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.javaconfig.JavaConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private final String PATH = "/api/posts";
  private final String GET = "GET";
  private final String POST = "POST";
  private final String DELETE = "DELETE";
  private final String PATHID = "/\\d+";

  @Override
  public void init() {
    final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
    controller = context.getBean(PostController.class);
    final var service = context.getBean("postService");
    final var repository = context.getBean("postRepository");
  }
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    try {
      //System.out.println("Проверка");
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      if (method.equals(GET) && path.equals(PATH)) {
        // System.out.println("Првоерка");
        controller.all(resp);
        return;
      }
      if (method.equals(GET) && path.matches(PATH+PATHID)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(POST) && path.equals(PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && path.matches(PATH+PATHID)) {
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}