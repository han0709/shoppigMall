package com.nonage.admin.controller.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nonage.dao.ProductDAO;
import com.nonage.controller.action.Action;
import com.nonage.dto.ProductVO;

public class AdminProductDetailAction implements Action {

  @Override
  public void execute(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String url = "admin/product/productDetail.jsp";

    String pseq = request.getParameter("pseq").trim();

    ProductDAO productDAO = ProductDAO.getInstance();
    ProductVO productVO = productDAO.getProduct(pseq);//��ǰ��ȣ�� �ش��ϴ� ��� ������ �����´�.

    request.setAttribute("productVO", productVO);

    // ��ǰ ����Ʈ(product_list.jsp) ���������� ���� ��Ʈ������ �Ѱ��� ���� �������� ���´�.
    String tpage = "1";
    if (request.getParameter("tpage") != null) {
      tpage = request.getParameter("tpage");
    }
    String kindList[] = { "0", "Heels", "Boots", "Sandals", "Slipers","Shcakers", "Sale" };
    request.setAttribute("tpage", tpage);
    int index=Integer.parseInt(productVO.getKind().trim());
    request.setAttribute("kind", kindList[index]);
    
    request.getRequestDispatcher(url).forward(request, response);
  }
}