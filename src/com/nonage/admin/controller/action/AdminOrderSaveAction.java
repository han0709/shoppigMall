package com.nonage.admin.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nonage.controller.action.Action;
import com.nonage.dao.OrderDAO;

public class AdminOrderSaveAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      String url = "NonageServlet?command=admin_order_list";
      
      String[] resultArr = request.getParameterValues("result");
      
      for(String odseq:resultArr){
        System.out.println(odseq);
        OrderDAO orderDAO = OrderDAO.getInstance();
        orderDAO.updateOrderResult(odseq);
      }
      request.getRequestDispatcher(url).forward(request, response);
    }
}
